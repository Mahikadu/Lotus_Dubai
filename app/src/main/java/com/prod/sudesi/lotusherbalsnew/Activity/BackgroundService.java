package com.prod.sudesi.lotusherbalsnew.Activity;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;

import com.prod.sudesi.lotusherbalsnew.Models.StockModel;
import com.prod.sudesi.lotusherbalsnew.Utils.SharedPref;
import com.prod.sudesi.lotusherbalsnew.Utils.Utils;
import com.prod.sudesi.lotusherbalsnew.Dbconfig.DbHelper;
import com.prod.sudesi.lotusherbalsnew.libs.ConnectionDetector;
import com.prod.sudesi.lotusherbalsnew.libs.LotusWebservice;

import org.ksoap2.serialization.SoapPrimitive;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Mahi on 06-11-2017.
 */

public class BackgroundService extends Service {

    private boolean isRunning;
    private Context context;
//    Context context;
    private Thread backgroundThread;


    private ConnectionDetector cd;

    LotusWebservice service;
    SharedPref sharedPref;
    String username;
    private Utils utils;
    private ArrayList<StockModel> stockDetailsArraylist;
    StockModel stockModel;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.context = this;
//        context=this;
        this.isRunning = false;
        this.backgroundThread = new Thread(myTask);
    }



    private Runnable myTask = new Runnable() {
        public void run() {

            SoapPrimitive result = null;

            cd = new ConnectionDetector(context);
            service = new LotusWebservice(context);
            sharedPref = new SharedPref(context);
            utils = new Utils(context);

            username = sharedPref.getLoginId();

            if (!cd.isConnectingToInternet()) {

               // cd.displayMessage("Check Your Internet Connection!!!");

            }else{
                //cd.displayMessage("Service calling");

                try {

                   // Toast.makeText(context,"Service calling",Toast.LENGTH_SHORT).show();

                    LOTUS.dbCon.open();
                    Cursor stock_array = LOTUS.dbCon.getStockdetails();
                    //LOTUS.dbCon.close();

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss");
                    String insert_timestamp = sdf.format(c
                            .getTime());

                    stockDetailsArraylist = new ArrayList<>();
                    if (stock_array != null && stock_array.getCount() > 0) {
                        stock_array.moveToFirst();
                        do {
                            stockModel = new StockModel();
                            stockModel.setA_id(stock_array.getString(stock_array.getColumnIndex("A_id")));
                            stockModel.setBa_code(stock_array.getString(stock_array.getColumnIndex("ba_code")));
                            stockModel.setStock_received(stock_array.getString(stock_array.getColumnIndex("stock_received")));
                            stockModel.setOpening_stock(stock_array.getString(stock_array.getColumnIndex("opening_stock")));
                            stockModel.setSold_stock(stock_array.getString(stock_array.getColumnIndex("sold_stock")));
                            stockModel.setClose_bal(stock_array.getString(stock_array.getColumnIndex("close_bal")));
                            stockModel.setTotal_gross_amount(stock_array.getString(stock_array.getColumnIndex("total_gross_amount")));
                            stockModel.setDiscount(stock_array.getString(stock_array.getColumnIndex("discount")));
                            stockModel.setTotal_net_amount(stock_array.getString(stock_array.getColumnIndex("total_net_amount")));
                            stockModel.setCurrentdate(insert_timestamp);
                            stockModel.setStroutletcode(stock_array.getString(stock_array.getColumnIndex("outletcode")));
                            stockDetailsArraylist.add(stockModel);

                        } while (stock_array.moveToNext());
                        stock_array.close();
                    }
                    if (stockDetailsArraylist.size() > 0) {
                        //strMrpArray = new String[productDetailsArraylist.size()];
                        for (int i = 0; i < stockDetailsArraylist.size(); i++) {
                            stockModel = stockDetailsArraylist.get(i);

                            result = service.DataUpload(stockModel.getA_id(), stockModel.getBa_code(), stockModel.getStock_received(),
                                    stockModel.getOpening_stock(),stockModel.getSold_stock(),stockModel.getClose_bal(),
                                    stockModel.getTotal_gross_amount(),stockModel.getDiscount(),stockModel.getTotal_net_amount(),
                                    stockModel.getCurrentdate(),stockModel.getStroutletcode());

                            String response = String.valueOf(result.toString());

                            if(response.equalsIgnoreCase("success")){

                                LOTUS.dbCon.update(DbHelper.TABLE_STOCK, "outletcode = ?", new String[]{"1"},
                                        new String[]{"savedServer"}, new String[]{ stockModel.getStroutletcode()});

                                //cd.displayMessage("Upload Data Automatically!!");
                            }

                            if(result == null){
                                final Calendar calendar = Calendar
                                        .getInstance();
                                SimpleDateFormat formatter = new SimpleDateFormat(
                                        "MM/dd/yyyy HH:mm:ss");
                                String Createddate = formatter.format(calendar
                                        .getTime());

                                int n = Thread.currentThread().getStackTrace()[2]
                                        .getLineNumber();

                                LOTUS.dbCon.open();

                                int id  = 0;
                                id = LOTUS.dbCon.getCountOfRows(DbHelper.SYNC_LOG) + 1;
                                String selection = "ID = ?";
                                String ID = String.valueOf(id);
                                // WHERE clause arguments
                                String[] selectionArgs = {ID};

                                String valuesArray[] = {ID, "Soup is Null While DataUpload()", String.valueOf(n), "DataUpload()",
                                        Createddate, Createddate, sharedPref.getLoginId(), "DataUpload", "Fail"};
                                boolean output = LOTUS.dbCon.updateBulk(DbHelper.SYNC_LOG, selection, valuesArray, utils.columnNamesSyncLog, selectionArgs);

                                LOTUS.dbCon.close();

                               // cd.displayMessage("Soup is Null While DataUpload()");
                            }

                        }
                    }
                } catch (Exception e) {
                   e.printStackTrace();
                }
            }
            stopSelf();
        }
    };

    @Override
    public void onDestroy() {
        this.isRunning = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(!this.isRunning) {
            this.isRunning = true;
            this.backgroundThread.start();
        }
        return START_STICKY;
    }

}
