package cn.bllli.tsxy.tsxyhelper;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class GetScoreActivity extends AppCompatActivity {
    static final String db_name = "mdb";
    static final String tb_name = "login";
    private TextView text;
    private Button button;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            text.setText((String)msg.obj);
            String info;
            try{
                JSONObject p_s = new JSONObject((String)msg.obj);
                info = p_s.getString("grade");
                info = info + " " +p_s.getString("stu_name") +"\n";
                JSONArray scores = p_s.getJSONArray("scores");
                for (int i = 0;i < scores.length();i++){
                    JSONObject single_score = scores.getJSONObject(i);
                    info = info + single_score.getString("id") + " " +
                            single_score.getString("name") + " " + single_score.getString("score")+ "\n";
                }
            }catch (JSONException e){
                e.printStackTrace();
                info = "获取出错，可能是没有出成绩，或者您的网络有问题。\n请重试或联系我们";
            }
            text.setText(info);
            button.setVisibility(View.VISIBLE);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_score);

        button = (Button) findViewById(R.id.button);
        text = (TextView) findViewById(R.id.text);
        postData();
        button.setVisibility(View.INVISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.setVisibility(View.INVISIBLE);
                postData();
            }
        });
    }

    private void postData() {
        new Thread(new Runnable() {


            private Handler ctx;

            @Override
            public void run() {
                String name = getStuId();
                Map<String, String> params = new HashMap<String, String>();
                String post_result = null;
                String content =  "root+" + name + "+new";
                params.put("content", content);
                params.put("do", "cmd");
                params.put("wxID", "1");
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

    private String getStuId(){
        try {
            SQLiteDatabase db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);

            Cursor c = db.rawQuery("SELECT * FROM " + tb_name, null);
            String stuid;
            if (c.moveToFirst()){
                stuid = c.getString(0);
                if (stuid.length() == 10){
                    return stuid;
                }
            }
            c.close();
            Intent it = new Intent(GetScoreActivity.this, LoginActivity.class);
            startActivity(it);
            return "0";
        }catch (SQLiteException e) {
            Intent it = new Intent(GetScoreActivity.this, LoginActivity.class);
            startActivity(it);
            return "0";
        }
    }

}
