package cn.bllli.tsxy.tsxyhelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by 张志雄 on 2016/12/9.
 */

public class LoginActivity extends Activity {
    static final String db_name = "mdb";
    static final String tb_name = "login";

    private SQLiteDatabase db;
    private Button login ;
    private EditText etAccount;
    private EditText etPwd;

    private static String rand_text;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //(Toast.makeText(LoginActivity.this, (String)msg.obj , Toast.LENGTH_LONG)).show();
            // 你好像已经绑定了～请直接发送‘查询’查询成绩～
            // 绑定成功!
            //
            String get = (String)msg.obj;
            if (get.contains("绑定成功!") || get.contains("你好像已经绑定了")) {
                Log.d("LOGIN", msg.obj + "OK!!!");
                (Toast.makeText(LoginActivity.this, "登陆成功" , Toast.LENGTH_LONG)).show();

                db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
                db.execSQL("CREATE TABLE IF NOT EXISTS " +
                        tb_name +
                        "(stu_id VARCHAR(10)," +
                        "rand_num VARCHAR(64) )"
                );
                String name = etAccount.getText().toString();

                ContentValues cv = new ContentValues(2);
                cv.put("stu_id", name);
                cv.put("rand_num", rand_text);
                db.insert(tb_name, null ,cv);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                LoginActivity.this.startActivity(intent);
                LoginActivity.this.finish();
            }else if (get.contains("账号密码有误")){
                (Toast.makeText(LoginActivity.this, "账号或密码错误" , Toast.LENGTH_LONG)).show();
                Log.d("LOGIN", msg.obj + "ERROR!");
            }else{
                (Toast.makeText(LoginActivity.this, "登陆出现问题，请联系作者" , Toast.LENGTH_LONG)).show();
            }
            login.setEnabled(Boolean.TRUE);
            login.setText("登录");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etAccount = (EditText)findViewById(R.id.etAcount);
        etPwd =(EditText)findViewById(R.id.etPwd);
        login = (Button)findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {               //点击登录后的事件

                Date date = new Date();
                Random rand =new Random(date.getTime());
                int num = rand.nextInt() + rand.nextInt();
                rand_text = getMD5(""+ num);

                login.setEnabled(Boolean.FALSE);
                login.setText("登录中，请稍后");

                if(etAccount.getText().toString().length() == 10){
                    if (etPwd.getText().toString().length() > 4) {
                        login(rand_text);
                    }else {
                        Toast.makeText(LoginActivity.this, "密码是不是太短了？", Toast.LENGTH_SHORT).show();
                        login.setEnabled(Boolean.TRUE);
                        login.setText("登录");
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "学号必须为十位数字", Toast.LENGTH_SHORT).show();
                    login.setEnabled(Boolean.TRUE);
                    login.setText("登录");
                }

            }
        });
    }

    private void login(final String demo) {
        new Thread(new Runnable() {

            private Handler ctx;

            @Override
            public void run() {
                String name = etAccount.getText().toString();
                String passwd = etPwd.getText().toString();
                Map<String, String> params = new HashMap<String, String>();
                String post_result = null;
                String content =  "绑定+"+ name + "+" +passwd;
                params.put("content", content);
                params.put("do", "cmd");
                params.put("wxID", demo);
                String str_temp = "NULL";
                try {
                    post_result = HttpUtils.submitPostData(params, "utf-8");
                    Log.i("POST_RESULT", post_result + "|" + name +  "|" + content);
                    str_temp = post_result;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    str_temp = "获取失败";
                }

                Message msg = mHandler.obtainMessage();
                msg.obj = str_temp;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    public static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            return "-1";
        }
    }
}

