package com.prod.sudesi.lotusherbalsdubai.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.littlefluffytoys.littlefluffylocationlibrary.LocationInfo;
import com.prod.sudesi.lotusherbalsdubai.Dbconfig.DataBaseCon;
import com.prod.sudesi.lotusherbalsdubai.Dbconfig.DatabaseCopy;
import com.prod.sudesi.lotusherbalsdubai.Dbconfig.DbHelper;
import com.prod.sudesi.lotusherbalsdubai.Models.LoginDetailsModel;
import com.prod.sudesi.lotusherbalsdubai.R;

import com.prod.sudesi.lotusherbalsdubai.Utils.SharedPref;
import com.prod.sudesi.lotusherbalsdubai.Utils.Utils;
import com.prod.sudesi.lotusherbalsdubai.libs.ConnectionDetector;
import com.prod.sudesi.lotusherbalsdubai.libs.LotusWebservice;

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
import java.util.Locale;

public class LoginActivity extends Activity {

    Button btn_login;
    LocationInfo locationInfo;
    EditText edt_username;
    EditText edt_password;
    private SharedPref sharedPref;
    ConnectionDetector cd;
    Context context;
    LotusWebservice service;
    String month, year, deviceId, username, pass, version, baname, serverdate, attendant;
    String server_date, todaydate1;

    private static final int RECORD_REQUEST_CODE = 101;
    private double lon = 0.0, lat = 0.0;
    int logCounter = 0;
    ProgressDialog progress;
    int id = 0;
    String ID;
    private Utils utils;
    private LoginDetailsModel loginDetailsModel;

    @SuppressLint("WrongConstant")
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
        sharedPref = new SharedPref(context);
        utils = new Utils(context);
        cd = new ConnectionDetector(LoginActivity.this);

        progress = new ProgressDialog(LoginActivity.this);
        service = new LotusWebservice(LoginActivity.this); // webservice object

        // Calendar Details
        Calendar c = Calendar.getInstance();

        int year1 = c.get(Calendar.YEAR);
        int month1 = c.get(Calendar.MONTH);

        month = String.valueOf(month1 + 1);
        year = String.valueOf(year1);


        //  Copy Database from Asset folder
        DatabaseCopy databaseCopy = new DatabaseCopy();
        AssetManager assetManager = this.getAssets();
        databaseCopy.copy(assetManager, LoginActivity.this);
        LOTUS.dbCon = DataBaseCon.getInstance(getApplicationContext());

        exportDB();


        // Login btn setonclicklistner
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, R.anim.button_click));
                try {

                    if (sharedPref.getvalue() == true) {
                        Log.e("Upload Data Receivert", String.valueOf(sharedPref.getvalue()));
                    } else {
                        DataUploadAlaramReceiver();
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

                        //LOTUS.dbCon.open();
                        int count = LOTUS.dbCon.checkcount(username, pass);
                        //LOTUS.dbCon.close();

                        if (count > 0) {

                            SimpleDateFormat sdf = new SimpleDateFormat(
                                    "yyyy-MM-dd");
                            String currentDateandTime = sdf.format(new Date())
                                    .toString();

                            LOTUS.dbCon.open();
                            String attendance = LOTUS.dbCon.getdatepresentorabsent(currentDateandTime, username);
                            LOTUS.dbCon.close();
                            // String a="P";

                            if (attendance.equalsIgnoreCase("")) {

                                if (cd.isConnectingToInternet()) {
                                    Check_Login login = new Check_Login();
                                    login.execute();
                                } else {
                                    cd.displayMessage("Time out or No Network or Wrong Credentials");

                                }
                            }
                            if (attendance.equalsIgnoreCase("P")) {

                                // SetClosingISOpeningOnlyOnce();

                                Intent i = new Intent(getApplicationContext(),
                                        DashBoardActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);

                            }
                            if (attendance.equalsIgnoreCase("A")) {
                                cd.displayMessage("U r absent today");

                            }

                        } else {

                            LOTUS.dbCon.open();
                            int count1 = LOTUS.dbCon.getCountOfRows(DbHelper.TABLE_LOGIN);
                            LOTUS.dbCon.close();

                            if (count1 == 1) {
                                cd.displayMessage("Please Enter Correct user name and password");
                            } else {
                                if (cd.isConnectingToInternet()) {
                                    Check_Login login = new Check_Login();
                                    login.execute();
                                } else {
                                    cd.displayMessage("Time out or No Network or Wrong Credentials");
                                }
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
        String currentDBPath = "/data/" + "com.prod.sudesi.lotusherbalsdubai" + "/databases/" + DbHelper.DATABASE_NAME;
        String backupDBPath = DbHelper.DATABASE_NAME;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @SuppressLint("WrongConstant")
    private void DataUploadAlaramReceiver() {

        try {

            //Create pending intent & register it to your alarm notifier class
            Intent intent = new Intent(this, UploadDataBrodcastReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            //set timer you want alarm to work (here I have set it to 24.00)
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 24);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            //Create alarm manager
            AlarmManager mAlarmManger = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            //set that timer as a RTC Wakeup to alarm manager object
            mAlarmManger.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*try {
            Intent intentAlarm = new Intent(this, UploadDataBrodcastReceiver.class);
            // create the object
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 12);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            //set the alarm for particular time
            //  alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000, PendingIntent.getBroadcast(this, 0, intentAlarm, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        finish();
    }

    private void insertDataInDb() {
        try {


            String valuesArray[] = {username, pass, deviceId, serverdate, serverdate, baname, attendant};
            // WHERE   clause
            String selection = " username = ?";

            // WHERE clause arguments
            String[] selectionArgs = {username};

            boolean result = LOTUS.dbCon.updateBulk(DbHelper.TABLE_LOGIN, selection, valuesArray, utils.columnNamesLogin, selectionArgs);

            if (result) {

                try {
                    String where = " where username = '" + username + "'";

                    Cursor cursor = LOTUS.dbCon.fetchFromSelect(DbHelper.TABLE_LOGIN, where);
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {
                            loginDetailsModel = loginDetails(cursor);
                        } while (cursor.moveToNext());
                        cursor.close();

                    } else {
                        cd.displayMessage("No data found..!");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (attendant.equalsIgnoreCase("True")) {


                    if (sharedPref.getvalue() == false) {
                        DataUploadAlaramReceiver();
                        sharedPref.setKeyNodata(true);

                    } else {

                    }

                    Intent i = new Intent(getApplicationContext(), AttendanceActivity.class);
                    i.putExtra("FromLoginpage", "L");
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                } else {
                    Intent i = new Intent(getApplicationContext(),
                            DashBoardActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LoginDetailsModel loginDetails(Cursor cursor) {
        loginDetailsModel = new LoginDetailsModel();
        try {
            loginDetailsModel.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            loginDetailsModel.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            loginDetailsModel.setAndroid_uid(cursor.getString(cursor.getColumnIndex("android_uid")));
            loginDetailsModel.setCreated_date(cursor.getString(cursor.getColumnIndex("created_date")));
            loginDetailsModel.setLast_modified_date(cursor.getString(cursor.getColumnIndex("last_modified_date")));
            loginDetailsModel.setBa_name(cursor.getString(cursor.getColumnIndex("ba_name")));
            loginDetailsModel.setAttendance(cursor.getString(cursor.getColumnIndex("attendance")));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginDetailsModel;

    }

    public class Check_Login extends AsyncTask<Void, Void, SoapObject> {

        private SoapPrimitive server_date_result = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                if (progress != null && !progress.isShowing()) {
                    progress.setMessage("Login Please wait..");
                    progress.setCancelable(false);
                    progress.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected SoapObject doInBackground(Void... params) {
            PackageManager manager = getPackageManager();
            deviceId = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            try {
                PackageInfo pInfo = manager.getPackageInfo(getPackageName(), 0);
                version = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            SoapObject object2 = service.GetLogin(username, pass, deviceId, version);

            server_date_result = service.GetServerDate();

            return object2;

        }

        @Override
        protected void onPostExecute(SoapObject soapObject) {
            super.onPostExecute(soapObject);
            try {
                if (progress != null && progress.isShowing()) {
                    progress.dismiss();
                }

                if (server_date_result != null) {

                    serverdate = server_date_result.toString();

                    if (soapObject != null) {
                        String response = String.valueOf(soapObject);
                        System.out.println("Response =>: " + response);

                        SoapObject res = (SoapObject) soapObject.getProperty(0);
                        String result = res.getPropertyAsString("Flag");
                        String message = res.getPropertyAsString("Message");
                        attendant = res.getPropertyAsString("attendance");

                        if (result.equalsIgnoreCase("True")) {
                            cd.displayMessage("You have login successfully..!");
                            baname = res.getPropertyAsString("baname");
                            sharedPref.clearPref();
                            sharedPref.setLoginInfo(username, pass, baname);

                            String[] serverdatearray = serverdate
                                    .split(" ");

                            server_date = serverdatearray[0];


                           /* String[] serverdate1 = server_date
                                    .split("-");*/

                            String[] serverdate1 = server_date
                                    .split("/");//using UAt server

                            String currentyear = serverdate1[2];


                            /*SimpleDateFormat sdf = new SimpleDateFormat(
                                    "dd-MM-yyyy");*/

                            SimpleDateFormat sdf = new SimpleDateFormat(
                                    "dd/MM/yyyy", Locale.ENGLISH);//Using UAT server

                            // dob = jsonPoi.getString("dob").trim().replaceAll("\\-", "/");

                            Date curntdte = null;
                            try {
                                curntdte = sdf.parse(server_date);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            sdf.applyPattern("yyyy-MM-dd");
                            todaydate1 = sdf.format(curntdte);
                            sharedPref.setDateDetails(currentyear, serverdate, todaydate1);
                            insertDataInDb();

                        } else {
                            if (message.equalsIgnoreCase("Username is incorrect") ||
                                    message.equalsIgnoreCase("Password is incorrect")) {
                                cd.displayMessage("Username or password is incorrect");
                            } else if (message.equalsIgnoreCase("Update Version")) {
                                cd.displayMessage("Please Update to Newer Version!");
                            } else if (message.equalsIgnoreCase("Activation Key expired")) {
                                cd.displayMessage("Activation Key expired");
                            }else if(message.equalsIgnoreCase("This device is already assign to another handset")){
                                cd.displayMessage("This device is already assign to another handset");
                            }

                        }
                    } else {

                        String errors = "Login soap giving null response. GetLogin Method";
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

                        String valuesArray[] = {ID, "Soup is Null While GetLogin()", String.valueOf(n), "GetServerDate()",
                                Createddate, Createddate, sharedPref.getLoginId(), "Login Check", "Fail"};
                        boolean output = LOTUS.dbCon.updateBulk(DbHelper.SYNC_LOG, selection, valuesArray, utils.columnNamesSyncLog, selectionArgs);

                        LOTUS.dbCon.close();
                        cd.displayMessage("Soup is Null While GetLogin()");

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

                    id = LOTUS.dbCon.getCountOfRows(DbHelper.SYNC_LOG) + 1;
                    String selection = "ID = ?";
                    ID = String.valueOf(id);
                    // WHERE clause arguments
                    String[] selectionArgs = {ID};

                    String valuesArray[] = {ID, "Soup is Null While GetServerDate()", String.valueOf(n), "GetServerDate()",
                            Createddate, Createddate, sharedPref.getLoginId(), "Login Check", "Fail"};
                    boolean output = LOTUS.dbCon.updateBulk(DbHelper.SYNC_LOG, selection, valuesArray, utils.columnNamesSyncLog, selectionArgs);

                    LOTUS.dbCon.close();
                    cd.displayMessage("Soup is Null While GetServerDate()");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
