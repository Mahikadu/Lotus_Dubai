package com.prod.sudesi.lotusherbalsnew.dbConfig;

import android.content.Context;

import java.util.Arrays;

/**
 * Created by Admin on 7/6/2017.
 */
public class Utils {
    public String[] columnNamesLogin= new String[50];


    private Context mContext;

    public Utils(Context mContext) {
        this.mContext = mContext;
        String[] uploadArray = {"Lid","username", "password","android_uid","created_date","last_modified_date",
                "status","div","flag","bde_Code","bde_Name"};
        columnNamesLogin = Arrays.copyOf(uploadArray, uploadArray.length);



    }
}
