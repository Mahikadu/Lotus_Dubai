package com.prod.sudesi.lotusherbalsdubai.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.prod.sudesi.lotusherbalsdubai.Models.StockModel;
import com.prod.sudesi.lotusherbalsdubai.R;
import com.prod.sudesi.lotusherbalsdubai.Utils.SharedPref;
import com.prod.sudesi.lotusherbalsdubai.Utils.Utils;
import com.prod.sudesi.lotusherbalsdubai.Dbconfig.DataBaseCon;
import com.prod.sudesi.lotusherbalsdubai.Dbconfig.DbHelper;
import com.prod.sudesi.lotusherbalsdubai.libs.ConnectionDetector;
import com.prod.sudesi.lotusherbalsdubai.libs.LotusWebservice;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


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

    private ArrayList<StockModel> stockDetailsArraylist;
    StockModel stockModel;

    String outletcode, username, producttype, syncdate;

    int id = 0;
    String ID;
    private Utils utils;

    Date startdate, enddate;
    ArrayList<String> dates_array;
    String str_Month, year;

    String ClosingBal,  FreshStock, GrossAmount, Message1, NetAmount, OutletCode1, ProductId, SoldStock, StockDate;

    String barcodes, brand, category, subCategory, singleOffer, productName,shortName, PTTamt, Size, Year, month;

    String A_id, ba_code, stock_received, opening_stock, sold_stock, close_bal, total_gross_amount, discount, total_net_amount, stroutletcode;

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

        try {
            LOTUS.dbCon.open();
            outletcode = LOTUS.dbCon.getActiveoutletCode();
            LOTUS.dbCon.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //producttype = sp.getString("producttype", "");
        Log.e("", "producttype==" + producttype);

        username = sharedPref.getLoginId();
        Log.e("", "username==" + username);

        tv_h_username = (TextView) findViewById(R.id.tv_h_username);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        tv_h_username.setText(username);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MMM-dd");
        String insert_timestamp = sdf.format(cal
                .getTime());

        String[] items1 = insert_timestamp.split("-");
        year = items1[0];
        str_Month = items1[1];


        System.out.println("   startdate--" + getStartEnd(str_Month, year)[0]);
        System.out.println("   enddate--" + getStartEnd(str_Month, year)[1]);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {

            startdate = format.parse(getStartEnd(str_Month, year)[0]);
            enddate = format.parse(getStartEnd(str_Month, year)[1]);

            System.out.println("   startdate1--" + startdate);
            System.out.println("   enddate1--" + enddate);

            List<Date> dates = getDaysBetweenDates(startdate, enddate);

            Log.e("dates", dates.toString());

            dates_array = new ArrayList<String>();

            for (int i = 0; i < dates.size(); i++) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                String reportDate = df.format(dates.get(i));
                Log.d("Date is", " " + reportDate);
                dates_array.add(reportDate);

                // Print what date is today!
                System.out.println("Report Date: " + reportDate);
            }


        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


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
                if (cd.isConnectingToInternet()) {
                    if(cd.isCurrentDateMatchDeviceDate()) {
                        new syncAllData().execute();
                    }else{
                        Toast.makeText(SyncMasterActivity.this, "Your Handset Date Not Match Current Date", Toast.LENGTH_LONG).show();

                    }
                } else {
                    cd.displayMessage("Check Your Internet Connection!!!");
                }

                break;
            case R.id.btn_master_sync:
                v.startAnimation(AnimationUtils.loadAnimation(SyncMasterActivity.this, R.anim.button_click));
                //fetchLastSyncDate();
                if(cd.isCurrentDateMatchDeviceDate()) {
                    MasterSync syncMaster = new MasterSync();
                    syncMaster.execute();
                }else{
                    Toast.makeText(SyncMasterActivity.this, "Your Handset Date Not Match Current Date", Toast.LENGTH_LONG).show();

                }

                break;
            case R.id.btn_changePass:
                v.startAnimation(AnimationUtils.loadAnimation(SyncMasterActivity.this, R.anim.button_click));
                startActivity(new Intent(SyncMasterActivity.this, ChangePasswordActivity.class));
                break;
            case R.id.btn_data_download:
                v.startAnimation(AnimationUtils.loadAnimation(SyncMasterActivity.this, R.anim.button_click));
                if(cd.isCurrentDateMatchDeviceDate()) {
                    DataUpload dataUpload = new DataUpload();
                    dataUpload.execute();
                }else{
                    Toast.makeText(SyncMasterActivity.this, "Your Handset Date Not Match Current Date", Toast.LENGTH_LONG).show();

                }

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

    @SuppressLint("WrongConstant")
    public static List<Date> getDaysBetweenDates(Date startdate, Date enddate) {
        List<Date> dates = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);

        while (calendar.getTime().before(enddate) || calendar.getTime().equals(enddate)) {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public String[] getStartEnd(String Month, String year) {
        String startend[] = new String[2];

        if (Month.equalsIgnoreCase("Jan")) {
            startend[0] = year + "-01-01";
            startend[1] = year + "-01-31";
        } else if (Month.equalsIgnoreCase("Feb")) {
            startend[0] = year + "-02-01";
            startend[1] = year + "-02-28";
        } else if (Month.equalsIgnoreCase("Mar")) {
            startend[0] = year + "-03-01";
            startend[1] = year + "-03-31";
        } else if (Month.equalsIgnoreCase("Apr")) {
            startend[0] = year + "-04-01";
            startend[1] = year + "-04-30";
        } else if (Month.equalsIgnoreCase("May")) {
            startend[0] = year + "-05-01";
            startend[1] = year + "-05-31";
        } else if (Month.equalsIgnoreCase("Jun")) {
            startend[0] = year + "-06-01";
            startend[1] = year + "-06-30";
        } else if (Month.equalsIgnoreCase("Jul")) {
            startend[0] = year + "-07-01";
            startend[1] = year + "-07-31";
        } else if (Month.equalsIgnoreCase("Aug")) {
            startend[0] = year + "-08-01";
            startend[1] = year + "-08-31";
        } else if (Month.equalsIgnoreCase("Sept")) {
            startend[0] = year + "-09-01";
            startend[1] = year + "-09-30";
        } else if (Month.equalsIgnoreCase("Oct")) {
            startend[0] = year + "-10-01";
            startend[1] = year + "-10-31";
        } else if (Month.equalsIgnoreCase("Nov")) {
            startend[0] = year + "-11-01";
            startend[1] = year + "-11-30";
        } else if (Month.equalsIgnoreCase("Dec")) {
            startend[0] = year + "-12-01";
            startend[1] = year + "-12-31";
        }

        return startend;
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
                                    Barcodes = root.getPropertyAsString("Barcodes").trim();
                                } else {
                                    Barcodes = "";
                                }
                            } else {
                                Barcodes = "";
                            }

                            if (root.getPropertyAsString("Brand") != null) {

                                if (!root.getPropertyAsString("Brand").equalsIgnoreCase("anyType{}")) {
                                    Brand = root.getPropertyAsString("Brand").trim();
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
                                    A_Id = root.getPropertyAsString("Id").trim();
                                } else {
                                    A_Id = "";
                                }
                            } else {
                                A_Id = "";
                            }
                            if (root.getPropertyAsString("Message") != null) {

                                if (!root.getPropertyAsString("Message").equalsIgnoreCase("anyType{}")) {
                                    Message = root.getPropertyAsString("Message").trim();
                                } else {
                                    Message = "";
                                }
                            } else {
                                Message = "";
                            }
                            if (root.getPropertyAsString("PTT") != null) {

                                if (!root.getPropertyAsString("PTT").equalsIgnoreCase("anyType{}")) {
                                    PTT = root.getPropertyAsString("PTT").trim();
                                } else {
                                    PTT = "";
                                }
                            } else {
                                PTT = "";
                            }
                            if (root.getPropertyAsString("ProductName") != null) {

                                if (!root.getPropertyAsString("ProductName").equalsIgnoreCase("anyType{}")) {
                                    ProductName = root.getPropertyAsString("ProductName").trim();
                                } else {
                                    ProductName = "";
                                }
                            } else {
                                ProductName = "";
                            }
                            if (root.getPropertyAsString("ShortName") != null) {

                                if (!root.getPropertyAsString("ShortName").equalsIgnoreCase("anyType{}")) {
                                    ShortName = root.getPropertyAsString("ShortName").trim();
                                } else {
                                    ShortName = "";
                                }
                            } else {
                                ShortName = "";
                            }
                            if (root.getPropertyAsString("SingleOffer") != null) {

                                if (!root.getPropertyAsString("SingleOffer").equalsIgnoreCase("anyType{}")) {
                                    SingleOffer = root.getPropertyAsString("SingleOffer").trim();
                                } else {
                                    SingleOffer = "";
                                }
                            } else {
                                SingleOffer = "";
                            }
                            if (root.getPropertyAsString("SubCategory") != null) {

                                if (!root.getPropertyAsString("SubCategory").equalsIgnoreCase("anyType{}")) {
                                    SubCategory = root.getPropertyAsString("SubCategory").trim();
                                } else {
                                    SubCategory = "";
                                }
                            } else {
                                SubCategory = "";
                            }
                            if (root.getPropertyAsString("order_flag") != null) {

                                if (!root.getPropertyAsString("order_flag").equalsIgnoreCase("anyType{}")) {
                                    order_flag = root.getPropertyAsString("order_flag").trim();
                                } else {
                                    order_flag = "";
                                }
                            } else {
                                order_flag = "";
                            }

                            if (root.getPropertyAsString("size") != null) {

                                if (!root.getPropertyAsString("size").equalsIgnoreCase("anyType{}")) {
                                    size = root.getPropertyAsString("size").trim();
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

                if (result != null) {
                    for (int i = 0; i < result.getPropertyCount(); i++) {

                        SoapObject root = (SoapObject) result.getProperty(i);

                        if (root.getPropertyAsString("Message") != null) {

                            if (!root.getPropertyAsString("Message").equalsIgnoreCase("anyType{}")) {
                                O_Message = root.getPropertyAsString("Message").trim();
                            } else {
                                O_Message = "";
                            }
                        } else {
                            O_Message = "";
                        }

                        if (root.getPropertyAsString("OutletCode") != null) {

                            if (!root.getPropertyAsString("OutletCode").equalsIgnoreCase("anyType{}")) {
                                OutletCode = root.getPropertyAsString("OutletCode").trim();
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


                    }
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
            return result;
        }

        @Override
        protected void onPostExecute(SoapObject soapObject) {
            super.onPostExecute(soapObject);

            mProgress.dismiss();

            if (soapObject != null) {
                String response = String.valueOf(soapObject.toString());

                SoapObject res = (SoapObject) soapObject.getProperty(0);
                String msg = res.getPropertyAsString("Message");

                if (msg.equalsIgnoreCase("Success")) {

                    //cd.DisplayDialogMessage("Master Data Sync Completed Successfully!!");
                    AlertDialog.Builder builder = new AlertDialog.Builder(SyncMasterActivity.this);
                    builder.setMessage("Master Data Sync Completed Successfully!!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                    Intent intent = new Intent(SyncMasterActivity.this, DashBoardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }

        }
    }

    public class syncAllData extends AsyncTask<Void, Void, SoapPrimitive> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mProgress.setMessage("Please Wait");
            mProgress.show();
            mProgress.setCancelable(false);
        }

        @Override
        protected SoapPrimitive doInBackground(Void... params) {
            SoapPrimitive result = null;
            if (cd.isConnectingToInternet()) {

                try {
                    LOTUS.dbCon.open();
                    Cursor stock_array = LOTUS.dbCon.getStockdetails(outletcode);
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
                            stockModel.setCurrentdate(stock_array.getString(stock_array.getColumnIndex("insert_date")));
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
                                    stockModel.getOpening_stock(), stockModel.getSold_stock(), stockModel.getClose_bal(),
                                    stockModel.getTotal_gross_amount(), stockModel.getDiscount(), stockModel.getTotal_net_amount(),
                                    stockModel.getCurrentdate(), stockModel.getStroutletcode());

                            String response = String.valueOf(result.toString());

                            if (response.equalsIgnoreCase("success")) {

                                LOTUS.dbCon.update(DbHelper.TABLE_STOCK, "outletcode = ?", new String[]{"1"},
                                        new String[]{"savedServer"}, new String[]{outletcode});
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return result;
        }

        @Override
        protected void onPostExecute(SoapPrimitive result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            mProgress.dismiss();
            if (result != null) {
                String response = String.valueOf(result.toString());

                if (response.equalsIgnoreCase("success")) {

                   // cd.DisplayDialogMessage("Data Upload Completed!!");
                    AlertDialog.Builder builder = new AlertDialog.Builder(SyncMasterActivity.this);
                    builder.setMessage("Data Upload Completed!!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                    Intent intent = new Intent(SyncMasterActivity.this, DashBoardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();

                }
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
                String ID = String.valueOf(id);
                // WHERE clause arguments
                String[] selectionArgs = {ID};

                String valuesArray[] = {ID, "Soup is Null While DataUpload()", String.valueOf(n), "DataUpload()",
                        Createddate, Createddate, sharedPref.getLoginId(), "DataUpload", "Fail"};
                boolean output = LOTUS.dbCon.updateBulk(DbHelper.SYNC_LOG, selection, valuesArray, utils.columnNamesSyncLog, selectionArgs);

                LOTUS.dbCon.close();

                cd.displayMessage("Connectivity Error, Please check Internet connection!!");
            }
        }
    }

    public class DataUpload extends AsyncTask<Void, Void, SoapPrimitive> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mProgress.setMessage("Please Wait");
            mProgress.show();
            mProgress.setCancelable(false);
        }

        @Override
        protected SoapPrimitive doInBackground(Void... params) {
            SoapPrimitive result = null;
            if (cd.isConnectingToInternet()) {

                try {
                    LOTUS.dbCon.open();
                    Cursor stock_array = LOTUS.dbCon.getStockdetails(outletcode);
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
                            stockModel.setCurrentdate(stock_array.getString(stock_array.getColumnIndex("insert_date")));
                            stockModel.setStroutletcode(stock_array.getString(stock_array.getColumnIndex("outletcode")));
                            stockDetailsArraylist.add(stockModel);

                        } while (stock_array.moveToNext());
                        stock_array.close();
                    }else{
                        cd.displayMessage("No Record Found");
                    }
                    if (stockDetailsArraylist.size() > 0) {
                        //strMrpArray = new String[productDetailsArraylist.size()];
                        for (int i = 0; i < stockDetailsArraylist.size(); i++) {
                            stockModel = stockDetailsArraylist.get(i);

                            result = service.DataUpload(stockModel.getA_id(), stockModel.getBa_code(), stockModel.getStock_received(),
                                    stockModel.getOpening_stock(), stockModel.getSold_stock(), stockModel.getClose_bal(),
                                    stockModel.getTotal_gross_amount(), stockModel.getDiscount(), stockModel.getTotal_net_amount(),
                                    stockModel.getCurrentdate(), stockModel.getStroutletcode());

                            String response = String.valueOf(result.toString());

                            if (response.equalsIgnoreCase("success")) {

                                LOTUS.dbCon.update(DbHelper.TABLE_STOCK, "outletcode = ?", new String[]{"1"},
                                        new String[]{"savedServer"}, new String[]{outletcode});


                            }

                        }
                    } else {
                        cd.displayMessage("Record not found!!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return result;
        }

        @Override
        protected void onPostExecute(SoapPrimitive result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            try {
                if (result != null) {
                    String response = String.valueOf(result.toString());

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
                    String ID = String.valueOf(id);
                    // WHERE clause arguments
                    String[] selectionArgs = {ID};

                    String valuesArray[] = {ID, "Soup is Null While DataUpload()", String.valueOf(n), "DataUpload()",
                            Createddate, Createddate, sharedPref.getLoginId(), "DataUpload", "Fail"};
                    boolean output = LOTUS.dbCon.updateBulk(DbHelper.SYNC_LOG, selection, valuesArray, utils.columnNamesSyncLog, selectionArgs);

                    LOTUS.dbCon.close();

                    //cd.displayMessage("Connectivity Error, Please check Internet connection!!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                DataDownload dataDownload = new DataDownload();
                dataDownload.execute();
            }
        }
    }

    public class DataDownload extends AsyncTask<Void, Void, SoapObject> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mProgress.setMessage("Please Wait...");
            mProgress.show();
            mProgress.setCancelable(false);
        }

        @Override
        protected SoapObject doInBackground(Void... params) {
            // TODO Auto-generated method stub

            SoapObject result = null;
            try {
                if (cd.isConnectingToInternet()) {

                    String startdate[] = getStartEnd(str_Month, year);

                    result = service.DataDownload(username, startdate[0], startdate[1], outletcode);

                    if (result != null) {
                        for (int i = 0; i < result.getPropertyCount(); i++) {

                            SoapObject root = (SoapObject) result.getProperty(i);

                            if (root.getPropertyAsString("ClosingBal") != null) {

                                if (!root.getPropertyAsString("ClosingBal").equalsIgnoreCase("anyType{}")) {
                                    ClosingBal = root.getPropertyAsString("ClosingBal").trim();
                                } else {
                                    ClosingBal = "";
                                }
                            } else {
                                ClosingBal = "";
                            }

                           /* if (root.getPropertyAsString("Discount") != null) {

                                if (!root.getPropertyAsString("Discount").equalsIgnoreCase("anyType{}")) {
                                    Discount = root.getPropertyAsString("Discount").trim();
                                } else {
                                    Discount = "";
                                }
                            } else {
                                Discount = "";
                            }*/

                            if (root.getPropertyAsString("FreshStock") != null) {

                                if (!root.getPropertyAsString("FreshStock").equalsIgnoreCase("anyType{}")) {
                                    FreshStock = root.getPropertyAsString("FreshStock").trim();
                                } else {
                                    FreshStock = "";
                                }
                            } else {
                                FreshStock = "";
                            }


                            if (root.getPropertyAsString("GrossAmount") != null) {

                                if (!root.getPropertyAsString("GrossAmount").equalsIgnoreCase("anyType{}")) {
                                    GrossAmount = root.getPropertyAsString("GrossAmount").trim();
                                } else {
                                    GrossAmount = "";
                                }
                            } else {
                                GrossAmount = "";
                            }

                            if (root.getPropertyAsString("Message") != null) {

                                if (!root.getPropertyAsString("Message").equalsIgnoreCase("anyType{}")) {
                                    Message1 = root.getPropertyAsString("Message").trim();
                                } else {
                                    Message1 = "";
                                }
                            } else {
                                Message1 = "";
                            }

                            if (root.getPropertyAsString("NetAmount") != null) {

                                if (!root.getPropertyAsString("NetAmount").equalsIgnoreCase("anyType{}")) {
                                    NetAmount = root.getPropertyAsString("NetAmount").trim();
                                } else {
                                    NetAmount = "";
                                }
                            } else {
                                NetAmount = "";
                            }
                            if (root.getPropertyAsString("OutletCode") != null) {

                                if (!root.getPropertyAsString("OutletCode").equalsIgnoreCase("anyType{}")) {
                                    OutletCode1 = root.getPropertyAsString("OutletCode").trim();
                                } else {
                                    OutletCode1 = "";
                                }
                            } else {
                                OutletCode1 = "";
                            }
                            if (root.getPropertyAsString("ProductId") != null) {

                                if (!root.getPropertyAsString("ProductId").equalsIgnoreCase("anyType{}")) {
                                    ProductId = root.getPropertyAsString("ProductId").trim();
                                } else {
                                    ProductId = "";
                                }
                            } else {
                                ProductId = "";
                            }
                            if (root.getPropertyAsString("SoldStock") != null) {

                                if (!root.getPropertyAsString("SoldStock").equalsIgnoreCase("anyType{}")) {
                                    SoldStock = root.getPropertyAsString("SoldStock").trim();
                                } else {
                                    SoldStock = "";
                                }
                            } else {
                                SoldStock = "";
                            }

                            if (root.getPropertyAsString("StockDate") != null) {

                                if (!root.getPropertyAsString("StockDate").equalsIgnoreCase("anyType{}")) {
                                    StockDate = root.getPropertyAsString("StockDate").trim();
                                } else {
                                    StockDate = "";
                                }
                            } else {
                                StockDate = "";
                            }

                            /*String[] items1 = StockDate.split("-");
                            Year = items1[0];
                            month = items1[1];*/

                            //12/13/2017 12:00:00 AM
                            SimpleDateFormat oldformat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss aaa");

                            Date theDate = null;
                            try {
                                theDate = oldformat.parse(StockDate);
                                oldformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                StockDate = oldformat.format(theDate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            Calendar myCal = new GregorianCalendar();
                            myCal.setTime(theDate);

                            SimpleDateFormat month_date = new SimpleDateFormat(
                                    "MMMM");
                             month = month_date.format(theDate);

                            @SuppressLint("WrongConstant")
                            int year = myCal.get(Calendar.YEAR);
                            Year = String.valueOf(year);

                            getProductDetailsagainstID(ProductId);

                            String valuesArray[] = {ProductId, barcodes, brand, category, subCategory, singleOffer, productName,shortName,
                                    PTTamt, Size, username, "0", FreshStock, FreshStock, ClosingBal, SoldStock, GrossAmount,
                                    NetAmount, "1", StockDate, StockDate, month, Year, StockDate, OutletCode1};
                            boolean rowid = LOTUS.dbCon.updateBulk(DbHelper.TABLE_STOCK, " A_id = ? AND outletcode = ? ", valuesArray, utils.columnNamesStock, new String[]{ProductId, OutletCode1});


                        }
                    } else {
                        final Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat formatter = new SimpleDateFormat(
                                "MM/dd/yyyy HH:mm:ss");
                        String Createddate = formatter.format(calendar
                                .getTime());

                        int n = Thread.currentThread().getStackTrace()[2]
                                .getLineNumber();

                        LOTUS.dbCon.open();

                        int id = 0;
                        id = LOTUS.dbCon.getCountOfRows(DbHelper.SYNC_LOG) + 1;
                        String selection = "ID = ?";
                        String ID = String.valueOf(id);
                        // WHERE clause arguments
                        String[] selectionArgs = {ID};

                        String valuesArray[] = {ID, "Soup is Null While DataDownload()", String.valueOf(n), "DataDownload()",
                                Createddate, Createddate, sharedPref.getLoginId(), "DataDownload", "Fail"};
                        boolean output = LOTUS.dbCon.updateBulk(DbHelper.SYNC_LOG, selection, valuesArray, utils.columnNamesSyncLog, selectionArgs);

                        LOTUS.dbCon.close();
                        //cd.displayMessage("Connectivity Error, Please check Internet connection!!");
                    }

                }

            } catch (Exception e) {
                e.getMessage();
            }
            return result;

        }

        @Override
        protected void onPostExecute(SoapObject result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            mProgress.dismiss();

            if (result != null) {
                String response = String.valueOf(result.toString());

                SoapObject res = (SoapObject) result.getProperty(0);
                String msg = res.getPropertyAsString("Message");

                if (msg.equalsIgnoreCase("success")) {

                    //cd.DisplayDialogMessage("Data Download Succesfully!!");
                    AlertDialog.Builder builder = new AlertDialog.Builder(SyncMasterActivity.this);
                    builder.setMessage("Data Download Succesfully!!")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                    Intent intent = new Intent(SyncMasterActivity.this, DashBoardActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }else{
                cd.displayMessage("Connectivity Error, Please check Internet connection!!");
            }

        }

        private void getProductDetailsagainstID(String productid) {

            try {

                String where = " where A_Id = " + "'" + productid + "'";
                Cursor cursor = LOTUS.dbCon.fetchFromSelect(DbHelper.TABLE_MASTERSYNC, where);
                if (cursor != null && cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    do {
                        barcodes = String.valueOf(cursor.getString(cursor.getColumnIndex("Barcodes")));
                        brand = String.valueOf(cursor.getString(cursor.getColumnIndex("Brand")));
                        category = String.valueOf(cursor.getString(cursor.getColumnIndex("Category")));
                        subCategory = String.valueOf(cursor.getString(cursor.getColumnIndex("SubCategory")));
                        singleOffer = String.valueOf(cursor.getString(cursor.getColumnIndex("SingleOffer")));
                        productName = String.valueOf(cursor.getString(cursor.getColumnIndex("ProductName")));
                        shortName = String.valueOf(cursor.getString(cursor.getColumnIndex("ShortName")));
                        PTTamt = String.valueOf(cursor.getString(cursor.getColumnIndex("PTT")));
                        Size = String.valueOf(cursor.getString(cursor.getColumnIndex("size")));

                    } while (cursor.moveToNext());
                    cursor.close();
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
