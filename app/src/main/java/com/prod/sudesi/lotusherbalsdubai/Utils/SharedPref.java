package com.prod.sudesi.lotusherbalsdubai.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Admin on 6/19/2017.
 */
public class SharedPref {

    private Context context;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private static final String KEY_LoginId = "key_loginid";
    private static final String KEY_PWD = "key_password";
    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_Year = "key_year";
    private static final String KEY_ServerDate = "key_serverdate";
    private static final String KEY_TodayDate = "key_todaydate";
    private static final String KEY_NODATA = "key_nodata";

    public SharedPref(Context _ctx) {
        context = _ctx;
        sharedPref = _ctx.getSharedPreferences("Lotus", context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void setLoginInfo(String loginId, String password, String baname) {
        editor.putString(KEY_LoginId, loginId);
        editor.putString(KEY_PWD, password);
        editor.putString(KEY_USERNAME, baname);
        editor.commit();
    }



    public void setDateDetails(String currentyear, String serverdate, String todaydate){
        editor.putString(KEY_Year, currentyear);
        editor.putString(KEY_ServerDate, serverdate);
        editor.putString(KEY_TodayDate, todaydate);
        editor.commit();
    }

    public String getLoginId() {
        String loginId = sharedPref.getString(KEY_LoginId, "");
        return loginId;
    }

    public String getPassword() {
        String pass = sharedPref.getString(KEY_PWD, "");
        return pass;
    }

    public String getbaName() {
        String baname = sharedPref.getString(KEY_USERNAME, "");
        return baname;
    }

    public String getCurrentYear() {
        String Currentyear = sharedPref.getString(KEY_Year, "");
        return Currentyear;
    }

    public String getServerDate() {
        String Serverdate = sharedPref.getString(KEY_ServerDate, "");
        return Serverdate;
    }

    public String getTodayDate() {
        String Todaydate = sharedPref.getString(KEY_TodayDate, "");
        return Todaydate;
    }



    public void setKeyNodata(boolean isData) {
        editor.putBoolean(KEY_NODATA, isData);
        editor.commit();
    }

    public boolean getvalue(){
        Boolean value = sharedPref.getBoolean(KEY_NODATA,false);
        return value;
    }
    public void clearPref() {
        try {
            editor.remove(KEY_LoginId);
            editor.remove(KEY_PWD);
            editor.remove(KEY_USERNAME);
            editor.clear();
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}