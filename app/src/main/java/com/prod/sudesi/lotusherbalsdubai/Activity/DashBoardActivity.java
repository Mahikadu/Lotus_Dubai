package com.prod.sudesi.lotusherbalsdubai.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.prod.sudesi.lotusherbalsdubai.R;
import com.prod.sudesi.lotusherbalsdubai.Utils.SharedPref;
import com.prod.sudesi.lotusherbalsdubai.libs.ConnectionDetector;

/**
 * Created by Admin on 16-10-2017.
 */

public class DashBoardActivity extends Activity implements View.OnClickListener {

    private Context mContext;

    private SharedPref sharedPref;

    Button btn_attendance, btn_visibility, btn_outlet_atten, btn_stock, btn_reports,
            btn_notification, btn_datasync, btn_BAoutsales, btn_dashboard, btn_total_outlet_sales, btn_home, btn_logout;

    TextView tv_h_username;

    String username;
    ConnectionDetector cd;

    @Override
    protected void onStart() {
        super.onStart();

        //DataUploadAlaramReceiver();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_dashboard);

        mContext = getApplicationContext();

        cd = new ConnectionDetector(mContext);

        sharedPref  = new SharedPref(mContext);
       // LOTUS.dbCon.open();

        //Button referance on dashboard screen
        btn_attendance = (Button) findViewById(R.id.btn_atten);
        btn_visibility = (Button) findViewById(R.id.btn_visibility);
        btn_outlet_atten = (Button) findViewById(R.id.btn_outlet_atten);
        btn_stock = (Button) findViewById(R.id.btn_stock);
        btn_reports = (Button) findViewById(R.id.btn_report);
        btn_notification = (Button) findViewById(R.id.btn_notifi);
        btn_datasync = (Button) findViewById(R.id.btn_master_sync);
        btn_BAoutsales = (Button) findViewById(R.id.btn_ba_sale_yr);
        btn_dashboard = (Button) findViewById(R.id.btn_dashboard);
        btn_total_outlet_sales = (Button) findViewById(R.id.btn_totaloutletSales);

        tv_h_username = (TextView) findViewById(R.id.tv_h_username);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        username = sharedPref.getLoginId();
        tv_h_username.setText(username);

        btn_home.setVisibility(View.INVISIBLE);

        //Set Onclick listner
        btn_attendance.setOnClickListener(this);
        btn_visibility.setOnClickListener(this);
        btn_outlet_atten.setOnClickListener(this);
        btn_stock.setOnClickListener(this);
        btn_reports.setOnClickListener(this);
        btn_notification.setOnClickListener(this);
        btn_datasync.setOnClickListener(this);
        btn_BAoutsales.setOnClickListener(this);
        btn_dashboard.setOnClickListener(this);
        btn_total_outlet_sales.setOnClickListener(this);
        btn_logout.setOnClickListener(this);


    }

   /* private void DataUploadAlaramReceiver() {

        *//*try {
            //Create alarm manager

            AlarmManager mAlarmManger = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            //Create pending intent & register it to your alarm notifier class
            Intent intent = new Intent(this, UploadDataBrodcastReceiver.class);
            intent.putExtra("uur", "1e"); // if you want
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

            //set timer you want alarm to work (here I have set it to 24.00)
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 10);
            calendar.set(Calendar.SECOND, 0);

            //set that timer as a RTC Wakeup to alarm manager object
            mAlarmManger.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
           // mAlarmManger.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000, pendingIntent); //Repeat every 24 hours
        } catch (Exception e) {
            e.printStackTrace();
        }*//*

        try {
            Intent intentAlarm = new Intent(this, UploadDataBrodcastReceiver.class);
            // create the object
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            //set the alarm for particular time
            //  alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 3600000, PendingIntent.getBroadcast(this, 0, intentAlarm, 0));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/


    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.btn_atten:
                v.startAnimation(AnimationUtils.loadAnimation(DashBoardActivity.this, R.anim.button_click));
                Intent i = new Intent(getApplicationContext(),AttendanceActivity.class);
                i.putExtra("FromLoginpage", "");
                startActivity(i);
                break;
            case R.id.btn_visibility:
                v.startAnimation(AnimationUtils.loadAnimation(DashBoardActivity.this, R.anim.button_click));
               // startActivity(new Intent(getApplicationContext(), VisibilityFragment.class));
                break;
            case R.id.btn_outlet_atten:
                v.startAnimation(AnimationUtils.loadAnimation(DashBoardActivity.this, R.anim.button_click));
                startActivity(new Intent(getApplicationContext(), OutletAttendanceActivity.class));
                break;
            case R.id.btn_stock:
                v.startAnimation(AnimationUtils.loadAnimation(DashBoardActivity.this, R.anim.button_click));
                startActivity(new Intent(getApplicationContext(), StockActivity.class));
                break;
            case R.id.btn_report:
                v.startAnimation(AnimationUtils.loadAnimation(DashBoardActivity.this, R.anim.button_click));
                startActivity(new Intent(getApplicationContext(), ReportActivity.class));
                break;
            case R.id.btn_notifi:
                v.startAnimation(AnimationUtils.loadAnimation(DashBoardActivity.this, R.anim.button_click));
                //startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                break;
            case R.id.btn_master_sync:
                v.startAnimation(AnimationUtils.loadAnimation(DashBoardActivity.this, R.anim.button_click));
                startActivity(new Intent(getApplicationContext(), SyncMasterActivity.class));
                break;
            case R.id.btn_ba_sale_yr:
                v.startAnimation(AnimationUtils.loadAnimation(DashBoardActivity.this, R.anim.button_click));
                //cd.displayMessage("Record Not Ready!!");
                startActivity(new Intent(getApplicationContext(), BAYearWiseReport.class));
                break;
            case R.id.btn_dashboard:
                v.startAnimation(AnimationUtils.loadAnimation(DashBoardActivity.this, R.anim.button_click));
                //cd.displayMessage("Record Not Ready!!");
                startActivity(new Intent(getApplicationContext(), BocDashBoardActivity.class));
                break;
            case R.id.btn_totaloutletSales:
                v.startAnimation(AnimationUtils.loadAnimation(DashBoardActivity.this, R.anim.button_click));
                startActivity(new Intent(getApplicationContext(), TotalOutletSaleActivity.class));
                break;
            case R.id.btn_logout:
                v.startAnimation(AnimationUtils.loadAnimation(DashBoardActivity.this, R.anim.button_click));
                Intent logout = new Intent(getApplicationContext(), LoginActivity.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logout);
                break;

        }
    }

    @Override
    public void onBackPressed(){

    }
}
