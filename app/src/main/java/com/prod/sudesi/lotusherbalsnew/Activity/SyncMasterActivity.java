package com.prod.sudesi.lotusherbalsnew.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.prod.sudesi.lotusherbalsnew.R;
import com.prod.sudesi.lotusherbalsnew.Utils.SharedPref;
import com.prod.sudesi.lotusherbalsnew.Utils.Utils;
import com.prod.sudesi.lotusherbalsnew.dbconfig.DataBaseCon;
import com.prod.sudesi.lotusherbalsnew.dbconfig.DbHelper;
import com.prod.sudesi.lotusherbalsnew.libs.ConnectionDetector;
import com.prod.sudesi.lotusherbalsnew.libs.LotusWebservice;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Admin on 16-10-2017.
 */

public class SyncMasterActivity extends Activity implements View.OnClickListener {

    Context context;
    Button master_sync, data_upload, data_download, btn_usermanual, btn_changePass;

    private ProgressDialog mProgress = null;

    LotusWebservice service;

    private SharedPref sharedPref;
    //
    ConnectionDetector cd;
    TextView tv_h_username;
    Button btn_home, btn_logout;

    String outletcode, username, producttype, syncdate;

    int id = 0;
    String ID;
    private Utils utils;

    String Barcodes, Brand, Category, A_Id, Message, PTT, ProductName, ShortName, SingleOffer, SubCategory, order_flag, size, lastsyncdate;
    String O_Message, OutletCode, OutletName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_syncmaster);

        context = getApplicationContext();

        sharedPref = new SharedPref(context);

        utils = new Utils(context);

        mProgress = new ProgressDialog(context);

        LOTUS.dbCon = DataBaseCon.getInstance(context);

        //Button referances
        data_upload = (Button) findViewById(R.id.btn_data_upload);
        master_sync = (Button) findViewById(R.id.btn_master_sync);
        btn_changePass = (Button) findViewById(R.id.btn_changePass);
        data_download = (Button) findViewById(R.id.btn_data_download);
        btn_usermanual = (Button) findViewById(R.id.btn_usermanual);

        cd = new ConnectionDetector(context);
        mProgress = new ProgressDialog(SyncMasterActivity.this);
        service = new LotusWebservice(SyncMasterActivity.this);


        LOTUS.dbCon.open();
        // outletcode = LOTUS.dbCon.getActiveoutletCode();
        LOTUS.dbCon.close();

        //producttype = sp.getString("producttype", "");
        Log.e("", "producttype==" + producttype);

        username = sharedPref.getLoginId();
        Log.e("", "username==" + username);

        tv_h_username = (TextView) findViewById(R.id.tv_h_username);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        tv_h_username.setText(username);


        //Set Onclick listner
        data_upload.setOnClickListener(this);
        master_sync.setOnClickListener(this);
        btn_changePass.setOnClickListener(this);
        data_download.setOnClickListener(this);
        btn_usermanual.setOnClickListener(this);
        btn_home.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_data_upload:
                v.startAnimation(AnimationUtils.loadAnimation(SyncMasterActivity.this, R.anim.button_click));
                // new syncAllData().execute();
                break;
            case R.id.btn_master_sync:
                v.startAnimation(AnimationUtils.loadAnimation(SyncMasterActivity.this, R.anim.button_click));
                //fetchLastSyncDate();
                MasterSync syncMaster = new MasterSync();
                syncMaster.execute();
                break;
            case R.id.btn_changePass:
                v.startAnimation(AnimationUtils.loadAnimation(SyncMasterActivity.this, R.anim.button_click));
                startActivity(new Intent(SyncMasterActivity.this, ChangePasswordActivity.class));
                break;
            case R.id.btn_data_download:
                v.startAnimation(AnimationUtils.loadAnimation(SyncMasterActivity.this, R.anim.button_click));
                // new InsertFirstTimeMaster().execute();
                break;
            case R.id.btn_usermanual:
                v.startAnimation(AnimationUtils.loadAnimation(SyncMasterActivity.this, R.anim.button_click));
                try {
                    File pdfFile = new File(Environment
                            .getExternalStorageDirectory(), "/sample.pdf");

                    readusermanual();

                } catch (Exception e) {

                }
                break;
            case R.id.btn_home:
                v.startAnimation(AnimationUtils.loadAnimation(SyncMasterActivity.this, R.anim.button_click));
                startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                break;
            case R.id.btn_logout:
                v.startAnimation(AnimationUtils.loadAnimation(SyncMasterActivity.this, R.anim.button_click));
                Intent logout = new Intent(getApplicationContext(),
                        LoginActivity.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logout);
                break;
        }
    }

    public void readusermanual() {

        Log.v("", "u1");
        AssetManager assetManager = getAssets();

        InputStream in = null;
        OutputStream out = null;
        File file = new File(getFilesDir(), "sample.pdf");
        Log.v("", "u1");
        try {
            in = assetManager.open("sample.pdf");
            out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);
            Log.v("", "u1");
            copyFile(in, out);
            Log.v("", "u1");
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));

            e.printStackTrace();

        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(
                    Uri.parse("file://" + getFilesDir() + "/sample.pdf"),
                    "application/pdf");

            startActivity(intent);

        } catch (Exception e) {

            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));

            e.printStackTrace();

        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public class MasterSync extends AsyncTask<Void, Void, SoapObject> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                if (mProgress != null && !mProgress.isShowing()) {
                    mProgress.setMessage("MasterSync Please wait..");
                    mProgress.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected SoapObject doInBackground(Void... params) {
            SoapObject result = null;
            try {
                result = service.MasterSync(username);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(SoapObject soapObject) {
            super.onPostExecute(soapObject);
            try {

                if (soapObject != null) {
                    String response = String.valueOf(soapObject);
                    System.out.println("Response =>: " + response);
                    try {
                        int id = 0;
                        for (int j = 0; j < soapObject.getPropertyCount(); j++) {
                            SoapObject root = (SoapObject) soapObject.getProperty(j);

                            if (root.getPropertyAsString("Barcodes") != null) {

                                if (!root.getPropertyAsString("Barcodes").equalsIgnoreCase("anyType{}")) {
                                    Barcodes = root.getPropertyAsString("Barcodes");
                                } else {
                                    Barcodes = "";
                                }
                            } else {
                                Barcodes = "";
                            }

                            if (root.getPropertyAsString("Brand") != null) {

                                if (!root.getPropertyAsString("Brand").equalsIgnoreCase("anyType{}")) {
                                    Brand = root.getPropertyAsString("Brand");
                                } else {
                                    Brand = "";
                                }
                            } else {
                                Brand = "";
                            }

                            if (root.getPropertyAsString("Category") != null) {

                                if (!root.getPropertyAsString("Category").equalsIgnoreCase("anyType{}")) {
                                    Category = root.getPropertyAsString("Category").trim();
                                } else {
                                    Category = "";
                                }
                            } else {
                                Category = "";
                            }
                            if (root.getPropertyAsString("Id") != null) {

                                if (!root.getPropertyAsString("Id").equalsIgnoreCase("anyType{}")) {
                                    A_Id = root.getPropertyAsString("Id");
                                } else {
                                    A_Id = "";
                                }
                            } else {
                                A_Id = "";
                            }
                            if (root.getPropertyAsString("Message") != null) {

                                if (!root.getPropertyAsString("Message").equalsIgnoreCase("anyType{}")) {
                                    Message = root.getPropertyAsString("Message");
                                } else {
                                    Message = "";
                                }
                            } else {
                                Message = "";
                            }
                            if (root.getPropertyAsString("PTT") != null) {

                                if (!root.getPropertyAsString("PTT").equalsIgnoreCase("anyType{}")) {
                                    PTT = root.getPropertyAsString("PTT");
                                } else {
                                    PTT = "";
                                }
                            } else {
                                PTT = "";
                            }
                            if (root.getPropertyAsString("ProductName") != null) {

                                if (!root.getPropertyAsString("ProductName").equalsIgnoreCase("anyType{}")) {
                                    ProductName = root.getPropertyAsString("ProductName");
                                } else {
                                    ProductName = "";
                                }
                            } else {
                                ProductName = "";
                            }
                            if (root.getPropertyAsString("ShortName") != null) {

                                if (!root.getPropertyAsString("ShortName").equalsIgnoreCase("anyType{}")) {
                                    ShortName = root.getPropertyAsString("ShortName");
                                } else {
                                    ShortName = "";
                                }
                            } else {
                                ShortName = "";
                            }
                            if (root.getPropertyAsString("SingleOffer") != null) {

                                if (!root.getPropertyAsString("SingleOffer").equalsIgnoreCase("anyType{}")) {
                                    SingleOffer = root.getPropertyAsString("SingleOffer");
                                } else {
                                    SingleOffer = "";
                                }
                            } else {
                                SingleOffer = "";
                            }
                            if (root.getPropertyAsString("SubCategory") != null) {

                                if (!root.getPropertyAsString("SubCategory").equalsIgnoreCase("anyType{}")) {
                                    SubCategory = root.getPropertyAsString("SubCategory");
                                } else {
                                    SubCategory = "";
                                }
                            } else {
                                SubCategory = "";
                            }
                            if (root.getPropertyAsString("order_flag") != null) {

                                if (!root.getPropertyAsString("order_flag").equalsIgnoreCase("anyType{}")) {
                                    order_flag = root.getPropertyAsString("order_flag");
                                } else {
                                    order_flag = "";
                                }
                            } else {
                                order_flag = "";
                            }

                            if (root.getPropertyAsString("size") != null) {

                                if (!root.getPropertyAsString("size").equalsIgnoreCase("anyType{}")) {
                                    size = root.getPropertyAsString("size");
                                } else {
                                    size = "";
                                }
                            } else {
                                size = "";
                            }

                            final Calendar calendar = Calendar
                                    .getInstance();
                            SimpleDateFormat formatter = new SimpleDateFormat(
                                    "MM/dd/yyyy HH:mm:ss");
                            lastsyncdate = formatter.format(calendar
                                    .getTime());

                            String selection = "id = ?";
                            id = id + 1;
                            // WHERE clause arguments
                            String[] selectionArgs = {id + ""};

                            String valuesArray[] = {id + "", Barcodes, Brand, Category, A_Id, Message, PTT, ProductName,
                                    ShortName, SingleOffer, SubCategory, order_flag, size, lastsyncdate};
                            boolean output = LOTUS.dbCon.updateBulk(DbHelper.TABLE_MASTERSYNC, selection, valuesArray, utils.columnNamesMasterSync, selectionArgs);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // cd.displayMessage("Master Data Sync Completed Successfully!!");


                } else {

                    final Calendar calendar = Calendar
                            .getInstance();
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "MM/dd/yyyy HH:mm:ss");
                    String Createddate = formatter.format(calendar
                            .getTime());

                    int n = Thread.currentThread().getStackTrace()[2]
                            .getLineNumber();

                    LOTUS.dbCon.open();

                    id = LOTUS.dbCon.getCountOfRows(DbHelper.SYNC_LOG) + 1;
                    String selection = "ID = ?";
                    ID = String.valueOf(id);
                    // WHERE clause arguments
                    String[] selectionArgs = {ID};

                    String valuesArray[] = {ID, "Soup is Null While MasterSync()", String.valueOf(n), "MasterSync()",
                            Createddate, Createddate, sharedPref.getLoginId(), "MasterSync", "Fail"};
                    boolean output = LOTUS.dbCon.updateBulk(DbHelper.SYNC_LOG, selection, valuesArray, utils.columnNamesSyncLog, selectionArgs);

                    LOTUS.dbCon.close();

                    cd.displayMessage("Master Data Sync Incomplete, Please try again!!");

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                GetOutlet getOutlet = new GetOutlet();
                getOutlet.execute();
            }
        }
    }

    public class GetOutlet extends AsyncTask<Void, Void, SoapObject> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected SoapObject doInBackground(Void... params) {
            SoapObject result = null;
            try {
                result = service.GetOutlet(username);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(SoapObject soapObject) {
            super.onPostExecute(soapObject);
            try {

                if (soapObject != null) {
                    String response = String.valueOf(soapObject);
                    System.out.println("Response =>: " + response);
                    try {
                        int id = 0;
                        for (int j = 0; j < soapObject.getPropertyCount(); j++) {
                            SoapObject root = (SoapObject) soapObject.getProperty(j);

                            if (root.getPropertyAsString("Message") != null) {

                                if (!root.getPropertyAsString("Message").equalsIgnoreCase("anyType{}")) {
                                    O_Message = root.getPropertyAsString("Message");
                                } else {
                                    O_Message = "";
                                }
                            } else {
                                O_Message = "";
                            }

                            if (root.getPropertyAsString("OutletCode") != null) {

                                if (!root.getPropertyAsString("OutletCode").equalsIgnoreCase("anyType{}")) {
                                    OutletCode = root.getPropertyAsString("OutletCode");
                                } else {
                                    OutletCode = "";
                                }
                            } else {
                                OutletCode = "";
                            }

                            if (root.getPropertyAsString("OutletName") != null) {

                                if (!root.getPropertyAsString("OutletName").equalsIgnoreCase("anyType{}")) {
                                    OutletName = root.getPropertyAsString("OutletName").trim();
                                } else {
                                    OutletName = "";
                                }
                            } else {
                                OutletName = "";
                            }

                            String selection = "id = ?";
                            id = id + 1;
                            // WHERE clause arguments
                            String[] selectionArgs = {id + ""};

                            String valuesArray[] = {id + "", O_Message, OutletCode, OutletName};
                            boolean output = LOTUS.dbCon.updateBulk(DbHelper.TABLE_OUTLET, selection, valuesArray, utils.columnNamesOutlet, selectionArgs);

                            if (output) {
                                if (mProgress != null && mProgress.isShowing()) {
                                    mProgress.dismiss();
                                }
                                new SweetAlertDialog(SyncMasterActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Master Sync")
                                        .setContentText("Master Data Sync Completed Successfully!!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                Intent intent = new Intent(SyncMasterActivity.this, DashBoardActivity.class);
                                                startActivity(intent);

                                                sDialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // cd.displayMessage("Master Data Sync Completed Successfully!!");


                } else {

                    final Calendar calendar = Calendar
                            .getInstance();
                    SimpleDateFormat formatter = new SimpleDateFormat(
                            "MM/dd/yyyy HH:mm:ss");
                    String Createddate = formatter.format(calendar
                            .getTime());

                    int n = Thread.currentThread().getStackTrace()[2]
                            .getLineNumber();

                    LOTUS.dbCon.open();

                    id = LOTUS.dbCon.getCountOfRows(DbHelper.SYNC_LOG) + 1;
                    String selection = "ID = ?";
                    ID = String.valueOf(id);
                    // WHERE clause arguments
                    String[] selectionArgs = {ID};

                    String valuesArray[] = {ID, "Soup is Null While GetOutlet()", String.valueOf(n), "GetOutlet()",
                            Createddate, Createddate, sharedPref.getLoginId(), "GetOutlet", "Fail"};
                    boolean output = LOTUS.dbCon.updateBulk(DbHelper.SYNC_LOG, selection, valuesArray, utils.columnNamesSyncLog, selectionArgs);

                    LOTUS.dbCon.close();

                    cd.displayMessage("Master Data Sync Incomplete, Please try again!!");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*public void fetchLastSyncDate(){
        try {
            final Calendar calendar = Calendar
                    .getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "MM/dd/yyyy HH:mm:ss");
            String currentdate = formatter.format(calendar
                    .getTime());

            String orderBy = "id";
            Cursor cursor = LOTUS.dbCon.fetchLastRow(DbHelper.TABLE_MASTERSYNC, orderBy, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                syncdate = cursor.getString(cursor.getColumnIndex("updated_date"));

            } else {
                syncdate = "";
            }
            cursor.close();
            if(!currentdate.equalsIgnoreCase(syncdate)){
                MasterSync syncMaster = new MasterSync();
                syncMaster.execute();
            }else{
                cd.displayMessage("Master Data Sync Already Done !!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
