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
    public String[] columnNamesStock= new String[50];
    public String[] columnNamesDashboardDetails= new String[50];
    public String[] columnNamesBAyearReport= new String[50];


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

        String[] outletAttendanceArray = {"BA_id","Adate","Actual_date","lat","lon","outletcode","outletname","outletstatus","savedServer"};
        columnNamesOutletAttendance = Arrays.copyOf(outletAttendanceArray, outletAttendanceArray.length);

        String[] stockArray = {"A_id","Barcodes","Brand","Category","SubCategory","SingleOffer",
                "ProductName","PTT","size","ba_code","opening_stock","stock_received","stock_in_hand","close_bal",
        "sold_stock","total_gross_amount","total_net_amount","discount","savedServer","insert_date",
        "last_modified_date","month","year","updateDate","outletcode"};
        columnNamesStock = Arrays.copyOf(stockArray, stockArray.length);

        String[] dashboardDetailsArray = {"Date","SoldQty","Soldvalue"};
        columnNamesDashboardDetails = Arrays.copyOf(dashboardDetailsArray, dashboardDetailsArray.length);

        String[] bayearreportArray = {"GrowthCSkin","GrowthPSkin","Message","NetAmountCSkin","NetAmountPSkin","years_MonthsC",
                "years_MonthsP"};
        columnNamesBAyearReport = Arrays.copyOf(bayearreportArray, bayearreportArray.length);



    }
}
