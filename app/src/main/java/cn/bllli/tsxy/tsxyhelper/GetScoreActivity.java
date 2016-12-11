package cn.bllli.tsxy.tsxyhelper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class GetScoreActivity extends AppCompatActivity {
    private EditText nameText;
    private TextView text;
    private Button button;
    private Button button2;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            text.setText((String)msg.obj);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_score);

        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        nameText = (EditText) findViewById(R.id.nameText);
        text = (TextView) findViewById(R.id.text);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameText.setVisibility(View.INVISIBLE);
                button.setVisibility(View.INVISIBLE);
                button2.setVisibility(View.VISIBLE);
                postData();
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameText.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                button2.setVisibility(View.GONE);
            }
        });
    }

    private void postData() {
        new Thread(new Runnable() {


            private Handler ctx;

            @Override
            public void run() {
                String name = nameText.getText().toString();
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


}
