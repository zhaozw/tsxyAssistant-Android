package cn.bllli.tsxy.tsxyhelper;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {
    static final String db_name = "mdb";
    static final String tb_name = "login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ((ImageView) findViewById(R.id.imageView2)).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isLogin()){
                    Intent it = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(it);
                    finish();
                }else {
                    Intent it = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(it);
                    finish();
                }
            }
        },3000);
    }

    private Boolean isLogin(){
        try {
            SQLiteDatabase db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);

            Cursor c = db.rawQuery("SELECT * FROM " + tb_name, null);
            String stuid;
            if (c.moveToFirst()){
                stuid = c.getString(0);
                Log.d("isLogin", stuid);
                if (stuid.length() == 10){
                    return Boolean.TRUE;
                }
            }
            c.close();
            return Boolean.FALSE;
        }catch (SQLiteException e) {
            return Boolean.FALSE;
        }
    }
}
