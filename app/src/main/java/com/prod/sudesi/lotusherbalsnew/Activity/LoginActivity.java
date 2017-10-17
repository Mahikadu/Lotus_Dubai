package com.prod.sudesi.lotusherbalsnew.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.prod.sudesi.lotusherbalsnew.R;

import com.prod.sudesi.lotusherbalsnew.dbConfig.DataBaseCon;
import com.prod.sudesi.lotusherbalsnew.dbConfig.DatabaseCopy;
import com.prod.sudesi.lotusherbalsnew.dbConfig.DbHelper;
import com.prod.sudesi.lotusherbalsnew.dbConfig.Utils;
import com.prod.sudesi.lotusherbalsnew.libs.ConnectionDetector;
import com.prod.sudesi.lotusherbalsnew.libs.LotusWebservice;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LoginActivity extends Activity {

    Button btn_login;
    LocationInfo locationInfo;
    EditText edt_username;
    EditText edt_password;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    ConnectionDetector cd;
    Context context;
    private ProgressDialog pd;
    LotusWebservice service;
    String month, year, deviceId, username, pass;
    private Utils utils;
    private static final int RECORD_REQUEST_CODE = 101;
    private double lon = 0.0, lat = 0.0;
    int logCounter = 0;
    DataBaseCon db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login_);

        btn_login = (Button) findViewById(R.id.btn_login);
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);

        context = getApplicationContext();
        db = new DataBaseCon(context);
        utils = new Utils(context);
        cd = new ConnectionDetector(LoginActivity.this);
        sp = getSharedPreferences("Lotus", MODE_PRIVATE);
        spe = sp.edit();

        pd = new ProgressDialog(LoginActivity.this);
        service = new LotusWebservice(LoginActivity.this); // webservice object

        // Calendar Details
        Calendar c = Calendar.getInstance();
        int year1 = c.get(Calendar.YEAR);
        int month1 = c.get(Calendar.MONTH);

        month = String.valueOf(month1 + 1);
        year = String.valueOf(year1);

        //   Add permission for marshmallow device
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("", "Permission to record denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Permission to access the photos,camera,storage and files is required for this app .")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("", "Clicked");
                        makeRequest();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                makeRequest();
            }
        }

        //  Copy Database from Asset folder
        DatabaseCopy databaseCopy = new DatabaseCopy();
        AssetManager assetManager = this.getAssets();
        databaseCopy.copy(assetManager, LoginActivity.this);
        LOTUS.dbCon = DataBaseCon.getInstance(getApplicationContext());

        exportDB();

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        deviceId = telephonyManager.getDeviceId();

        //  Getting location info
        refreshDisplay();

        // Login btn setonclicklistner
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.button_click));
                try {

                    if (sp.getBoolean("Upload_data_flag", true) == true) {
                        Log.e("Upload Data Receivert", String.valueOf(sp.getBoolean("Upload_data_flag_true", true)));
                    } else {
                        //  DataUploadAlaramReceiver();
                    }

                    if (edt_username.getText().toString().equalsIgnoreCase("")) {
                        edt_username.setError(null);
                        edt_username.setError("Enter Username");
                    } else if (edt_password.getText().toString().equalsIgnoreCase("")) {
                        edt_password.setError(null);
                        edt_password.setError("Enter Password");
                    } else {
                        username = edt_username.getText().toString().toUpperCase();
                        pass = edt_password.getText().toString();

                        db.open();
                        int count = db.checkcount(username, pass);
                        db.close();

                        if (count > 0) {

                            SimpleDateFormat sdf = new SimpleDateFormat(
                                    "yyyy-MM-dd");
                            String currentDateandTime = sdf.format(new Date())
                                    .toString();

                            db.open();
                            String attendance = db.getdatepresentorabsent(currentDateandTime, username);
                            db.close();
                            // String a="P";

                            if (attendance.equalsIgnoreCase("")) {

                               // new Check_Login().execute();
                            }
                            if (attendance.equalsIgnoreCase("P")) {

                                // SetClosingISOpeningOnlyOnce();

                              /*  Intent i = new Intent(getApplicationContext(),
                                        DashboardNewActivity.class);

                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                startActivity(i);*/

                            }
                            if (attendance.equalsIgnoreCase("A")) {

                                Toast.makeText(context, "U r absent today",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {

                            db.open();
                            int count1 = db.checkcount123();
                            db.close();

                            if (count1 == 1) {

                                Toast.makeText(
                                        getApplicationContext(),
                                        "Please Enter Correct user name and password",
                                        Toast.LENGTH_SHORT).show();
                            } else {

                               // new Check_Login().execute();
                            }

                        }
                    }

                } catch (Exception e) {
                    // TODO: handle exception
                    StringWriter errors = new StringWriter();
                    e.printStackTrace(new PrintWriter(errors));

                    e.printStackTrace();

                }
            }
        });
    }

    private void exportDB() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + "com.prod.sudesi.lotusherbalsnew" + "/databases/" + DbHelper.DATABASE_NAME;
        String backupDBPath = DbHelper.DATABASE_NAME;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            //          Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //    Permissions marshmallow
    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS},
                RECORD_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RECORD_REQUEST_CODE: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {

                    Log.i("", "Permission has been denied by user");
                } else {
                    Log.i("", "Permission has been granted by user");
                }
                return;
            }
        }
    }

    private void refreshDisplay() {
        refreshDisplay(new LocationInfo(context));
    }

    private void refreshDisplay(final LocationInfo locationInfo) {
        if (locationInfo.anyLocationDataReceived()) {
            lat = locationInfo.lastLat;
            lon = locationInfo.lastLong;
            logCounter = 1;
        } else {

            Toast.makeText(context,
                    "Unable to get GPS location! Try again later!!",
                    Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
    }

   /* private void DataUploadAlaramReceiver() {

        try {
            Intent intentAlarm = new Intent(this, UploadDataBrodcastReceiver.class);
            // create the object
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            //set the alarm for particular time
            //  alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 180, PendingIntent.getBroadcast(this, 0, intentAlarm, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

    /*public class Check_Login extends AsyncTask<Void, Void, SoapObject> {

        private SoapObject soap_result = null;
        private SoapPrimitive server_date_result = null;
        private SoapPrimitive soap_result2 = null;

        String Flag = "";

        @Override
        protected SoapObject doInBackground(Void... params) {

            Flag = "0";
            if (!cd.isConnectingToInternet()) {
                Flag = "0";
            } else {
                try {
                    String serverdate;
                    server_date_result = service.GetServerDate();

                    PackageInfo pInfo = getPackageManager().getPackageInfo(
                            getPackageName(), 0);
                    String version = pInfo.versionName;

                    soap_result = service.GetLogin(username, pass, version);

                    if (soap_result.equals("anyType{}")) {

                        Flag = "5";
                    } else {
                        if (server_date_result != null) {

                            serverdate = server_date_result.toString();

                            if (soap_result != null) {
                                for (int i = 0; i < soap_result
                                        .getPropertyCount(); i++) {

                                    SoapObject root = (SoapObject) soap_result.getProperty(i);

                                    if (root.getProperty("bdecode") != null) {

                                        if (!root.getProperty("bdecode").toString().equalsIgnoreCase("anyType{}")) {
                                            bdecode = root.getProperty("bdecode").toString();
                                            spe.putString("bdecode", bdecode);
                                            spe.commit();

                                        } else {
                                            bdecode = "";
                                        }
                                    } else {
                                        bdecode = "";
                                    }

                                    if (root.getProperty("bdename") != null) {

                                        if (!root.getProperty("bdename").toString().equalsIgnoreCase("anyType{}")) {
                                            bdename = root.getProperty("bdename").toString();
                                            spe.putString("bdename", bdename);
                                            spe.commit();

                                        } else {
                                            bdename = "";
                                        }
                                    } else {
                                        bdename = "";
                                    }

                                    if (root.getProperty("div") != null) {

                                        if (!root.getProperty("div").toString().equalsIgnoreCase("anyType{}")) {
                                            div = root.getProperty("div").toString();
                                            spe.putString("div", div);
                                            spe.commit();

                                        } else {
                                            div = "";
                                        }
                                    } else {
                                        div = "";
                                    }
                                    if (root.getProperty("status") != null) {

                                        if (!root.getProperty("status").toString().equalsIgnoreCase("anyType{}")) {
                                            status = root.getProperty("status").toString();

                                        } else {
                                            status = "";
                                        }
                                    } else {
                                        status = "";
                                    }
                                    if (root.getProperty("username") != null) {

                                        if (!root.getProperty("username").toString().equalsIgnoreCase("anyType{}")) {
                                            username = root.getProperty("username").toString();
                                            spe.putString("username", username);
                                            spe.commit();

                                        } else {
                                            username = "";
                                        }
                                    } else {
                                        username = "";
                                    }
                                }
                            } else {
                                Flag = "SOUP NULL";
                                String errors = "Login soap giving null response. GetLogin Method";
                                // we.writeToSD(errors.toString());
                                final Calendar calendar = Calendar
                                        .getInstance();
                                SimpleDateFormat formatter = new SimpleDateFormat(
                                        "MM/dd/yyyy HH:mm:ss");
                                String Createddate = formatter.format(calendar
                                        .getTime());

                                int n = Thread.currentThread().getStackTrace()[2]
                                        .getLineNumber();
                                db.open();
                                db.insertSyncLog(
                                        "Soup is Null While GetLogin()",
                                        String.valueOf(n), "GetLogin()",
                                        Createddate, Createddate,
                                        sp.getString("username", ""),
                                        "Login Check", "Fail");
                                db.close();

                            }
                        } else {

                            Flag = "SOUP NULL";

                            final Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat formatter = new SimpleDateFormat(
                                    "MM/dd/yyyy HH:mm:ss");
                            String Createddate = formatter.format(calendar
                                    .getTime());

                            int n = Thread.currentThread().getStackTrace()[2]
                                    .getLineNumber();
                            db.open();
                            db.insertSyncLog(
                                    "Soup is Null While GetServerDate()",
                                    String.valueOf(n), "GetServerDate()",
                                    Createddate, Createddate,
                                    sp.getString("username", ""),
                                    "Login Check", "Fail");
                            db.close();

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }*/

}
