package com.prod.sudesi.lotusherbalsnew.Utils;

import android.content.Context;

import java.util.Arrays;

/**
 * Created by Admin on 7/6/2017.
 */
public class Utils {
    public String[] columnNamesLogin= new String[50];
    public String[] columnNamesSyncLog= new String[50];


    private Context mContext;

    public Utils(Context mContext) {
        this.mContext = mContext;

        String[] uploadArray = {"username", "password","android_uid","created_date","last_modified_date",
                "ba_name","attendance"};
        columnNamesLogin = Arrays.copyOf(uploadArray, uploadArray.length);

        String[] syncArray = {"ID","EXCEPTION","LINE_NO","METHOD","CREATED_DATE","LASTMODIFIED_DATE",
                "USERNAME","SYNCMETHOD","RESULT"};
        columnNamesSyncLog = Arrays.copyOf(syncArray, syncArray.length);



    }
}
