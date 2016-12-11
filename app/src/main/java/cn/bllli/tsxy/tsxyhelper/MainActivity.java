package cn.bllli.tsxy.tsxyhelper;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static final String db_name = "mdb";
    static final String tb_name = "classes";
    private SQLiteDatabase db;

    private int[][] lessons={
            {R.id.lesson11,R.id.lesson12,R.id.lesson13,R.id.lesson14,R.id.lesson15,R.id.lesson16,R.id.lesson17},
            {R.id.lesson21,R.id.lesson22,R.id.lesson23,R.id.lesson24,R.id.lesson25,R.id.lesson26,R.id.lesson27},
            {R.id.lesson31,R.id.lesson32,R.id.lesson33,R.id.lesson34,R.id.lesson35,R.id.lesson36,R.id.lesson37},
            {R.id.lesson41,R.id.lesson42,R.id.lesson43,R.id.lesson44,R.id.lesson45,R.id.lesson46,R.id.lesson47},
            {R.id.lesson51,R.id.lesson52,R.id.lesson53,R.id.lesson54,R.id.lesson55,R.id.lesson56,R.id.lesson57},
            {R.id.lesson61,R.id.lesson62,R.id.lesson63,R.id.lesson64,R.id.lesson65,R.id.lesson66,R.id.lesson67},
    };

    private int[] colors={
            R.drawable.kb1,R.drawable.kb2,R.drawable.kb3,R.drawable.kb4,R.drawable.kb5,R.drawable.kb6,R.drawable.kb7
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //text.setText((String)msg.obj);
            db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS " +
                    tb_name +
                    "(class_name VARCHAR(64)," +
                    "class_when VARCHAR(32)," +
                    "class_dsz VARCHAR(16)," +
                    "class_where VARCHAR(16)," +
                    "class_week VARCHAR(16))"
            );
            try{
                JSONObject p_c = new JSONObject((String)msg.obj);
                JSONArray scores = p_c.getJSONArray("classes");
                for (int i = 0;i < scores.length();i++){
                    JSONObject single_class = scores.getJSONObject(i);
                    ContentValues cv = new ContentValues(5);
                    cv.put("class_name", single_class.getString("class_name"));
                    cv.put("class_when", single_class.getString("class_when"));
                    cv.put("class_dsz", single_class.getString("class_dsz"));
                    cv.put("class_where", single_class.getString("class_where"));
                    cv.put("class_week", single_class.getString("class_week"));
                    db.insert(tb_name, null ,cv);
                    Button bu = (Button)findViewById(getInTable(single_class.getString("class_when")));
                    bu.setText(single_class.getString("class_name")+"@"+single_class.getString("class_where"));
                    bu.setBackgroundResource(R.drawable.kb1);
                }
            }catch (JSONException e){
                e.printStackTrace();
            }
            db.close();
        }
    };

    private int getInTable(String a){
        //['1-2节', '3-4节', '5-6节', '7-8节', '9-10节']
        int xq;
        int j;
        if(a.contains("一")){
            xq = 1;
        }else if(a.contains("二")){
            xq = 2;
        }else if(a.contains("三")){
            xq = 3;
        }else if(a.contains("四")){
            xq = 4;
        }else if(a.contains("五")){
            xq = 5;
        }else if(a.contains("六")){
            xq = 6;
        }else if(a.contains("七")){
            xq = 7;
        }else {
            xq = 1;
            (Toast.makeText(MainActivity.this, "出错了！" , Toast.LENGTH_LONG)).show();
        }

        if(a.contains("1-2节")){
            j = 1;
        }else if (a.contains("3-4节")){
            j = 2;
        }else if (a.contains("5-6节")){
            j = 3;
        }else if (a.contains("7-8节")){
            j = 4;
        }else if (a.contains("9-10节")){
            j = 5;
        }else if (a.contains("11-12节")){
            j = 6;
        }else {
            j = 1;
            (Toast.makeText(MainActivity.this, "出错了！" , Toast.LENGTH_LONG)).show();
        }
        return lessons[j-1][xq-1];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        try{
            db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
            Cursor c = db.rawQuery("SELECT * FROM classes", null);
            if (c.moveToFirst()){
                do {
                    Button bu = (Button) findViewById(getInTable(c.getString(1)));
                    bu.setText(c.getString(0)+"@"+c.getString(3));
                    Random rand =new Random();
                    bu.setBackgroundResource(colors[Math.abs(rand.nextInt()%7)]);
                }while (c.moveToNext());
            }
            c.close();
        }catch (SQLiteException e){
            e.printStackTrace();
            getAllClassesInfo();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_search_score) {
            Intent it = new Intent();
            it.setClass(this, GetScoreActivity.class);
            startActivity(it);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            getAllClassesInfo();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getAllClassesInfo(){
        new Thread(new Runnable() {

            private Handler ctx;
            @Override
            public void run() {

                String name = getStuId();

                Map<String, String> params = new HashMap<String, String>();
                String post_result = null;
                String content =  "kcb_json+" + name;
                params.put("content", content);
                params.put("do", "cmd");
                params.put("wxID", "1");
                String class_json = "NULL";
                try {
                    post_result = HttpUtils.submitPostData(params, "utf-8");
                    Log.i("POST_RESULT", post_result + "|" + name +  "|" + content);
                    class_json = post_result;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    class_json = "获取失败";
                }
                Message msg = mHandler.obtainMessage();

                msg.obj = class_json;
                mHandler.sendMessage(msg);
            }
        }).start();
    }

    private String getStuId(){
        try {
            SQLiteDatabase db = openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);

            Cursor c = db.rawQuery("SELECT * FROM " + "login", null);
            String stuid;
            if (c.moveToFirst()){
                stuid = c.getString(0);
                if (stuid.length() == 10){
                    return stuid;
                }
            }
            c.close();
            Intent it = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(it);
            return "0";
        }catch (SQLiteException e) {
            Intent it = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(it);
            return "0";
        }
    }
}
