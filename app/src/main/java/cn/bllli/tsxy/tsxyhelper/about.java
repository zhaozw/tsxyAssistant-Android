package cn.bllli.tsxy.tsxyhelper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class about extends AppCompatActivity {
    private Button addQQqun;
    private Button smss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        addQQqun = (Button)findViewById(R.id.button2);
        smss = (Button)findViewById(R.id.button3);
        addQQqun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {               //点击后的事件
                joinQQGroup("VyCa_KnyJmBaANUVOG61CrYGONOatNy_");
            }
        });
        smss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {               //点击后的事件
                Intent it = new Intent();
                it.setAction(Intent.ACTION_VIEW);
                it.setData(Uri.parse("sms:13025064985?body=请输入您的反馈"));
                startActivity(it);
            }
        });
    }


    /****************
     *
     * 发起添加群流程。群号：《唐院助手》需要你(608827825) 的 key 为： VyCa_KnyJmBaANUVOG61CrYGONOatNy_
     * 调用 joinQQGroup(VyCa_KnyJmBaANUVOG61CrYGONOatNy_) 即可发起手Q客户端申请加群 《唐院助手》需要你(608827825)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     ******************/
    public boolean joinQQGroup(String key) {
        Intent intent = new Intent();
        intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent);
            return true;
        } catch (Exception e) {
            // 未安装手Q或安装的版本不支持
            Toast.makeText(about.this, "未安装手Q或安装的版本不支持", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}

