package com.prod.sudesi.lotusherbalsnew.Activity;

import android.app.Application;

import com.prod.sudesi.lotusherbalsnew.Dbconfig.DataBaseCon;


/**
 * Created by Admin on 13-10-2017.
 */

public class LOTUS extends Application {

    public static DataBaseCon dbCon = null;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        try {
            if (dbCon != null) {
                dbCon.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
