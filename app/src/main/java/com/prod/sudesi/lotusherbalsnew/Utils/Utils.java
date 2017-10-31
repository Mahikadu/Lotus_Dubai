package com.prod.sudesi.lotusherbalsnew.Utils;

import android.content.Context;

import java.util.Arrays;

/**
 * Created by Admin on 7/6/2017.
 */
public class Utils {
    public String[] columnNamesLogin= new String[50];
    public String[] columnNamesSyncLog= new String[50];
    public String[] columnNamesMasterSync= new String[50];
    public String[] columnNamesOutlet= new String[50];
    public String[] columnNamesOutletAttendance= new String[50];


    private Context mContext;

    public Utils(Context mContext) {
        this.mContext = mContext;

        String[] uploadArray = {"username", "password","android_uid","created_date","last_modified_date",
                "ba_name","attendance"};
        columnNamesLogin = Arrays.copyOf(uploadArray, uploadArray.length);

        String[] synclogArray = {"ID","EXCEPTION","LINE_NO","METHOD","CREATED_DATE","LASTMODIFIED_DATE",
                "USERNAME","SYNCMETHOD","RESULT"};
        columnNamesSyncLog = Arrays.copyOf(synclogArray, synclogArray.length);

        String[] mastersyncArray = {"id","Barcodes","Brand","Category","A_Id","Message",
                "PTT","ProductName","ShortName","SingleOffer","SubCategory","order_flag","size","last_sync_date"};
        columnNamesMasterSync = Arrays.copyOf(mastersyncArray, mastersyncArray.length);

        String[] outletArray = {"id","Message","OutletCode","OutletName"};
        columnNamesOutlet = Arrays.copyOf(outletArray, outletArray.length);

        String[] outletAttendanceArray = {"id","BA_id","Adate","Actual_date","lat","lon","outletcode","outletname","outletstatus"};
        columnNamesOutletAttendance = Arrays.copyOf(outletAttendanceArray, outletAttendanceArray.length);



    }
}
