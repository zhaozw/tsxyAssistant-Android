package cn.bllli.tsxy.tsxyhelper;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonDecode {
    public static String jsonObjectTest(String json_text) throws JSONException {
        Log.d("Json_r", json_text);
        JSONObject jsonobj = new JSONObject(json_text);
        String name = jsonobj.get("stu_name").toString();

        Log.d("name",name);
        String age = jsonobj.getString("stu_id");
        return "name:" + name + " ,stu_id:" + age;
    }
}
