package com.prod.sudesi.lotusherbalsnew.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.prod.sudesi.lotusherbalsnew.R;
import com.prod.sudesi.lotusherbalsnew.dbConfig.DataBaseCon;
import com.prod.sudesi.lotusherbalsnew.libs.AlarmManagerBroadcastReceiver;

/**
 * Created by Admin on 16-10-2017.
 */

public class DashBoardActivity extends Activity implements View.OnClickListener {

    private Context mContext;
    private DataBaseCon db = null;
    SharedPreferences sp;
    SharedPreferences.Editor spe;

    Button btn_attendance, btn_visibility, btn_outlet_atten, btn_stock, btn_reports, btn_sale,
            btn_notification, btn_datasync, btn_BAoutsales, btn_dashboard, btn_total_outlet_sales, btn_home, btn_logout;

    TextView tv_h_username;

    private AlarmManagerBroadcastReceiver alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_dashboard);

        mContext = getApplicationContext();
        db = new DataBaseCon(mContext);
        db.open();

        sp = mContext.getSharedPreferences("Lotus", Context.MODE_PRIVATE);
        spe = sp.edit();

        //Button referance on dashboard screen
        btn_attendance = (Button) findViewById(R.id.btn_atten);
        btn_visibility = (Button) findViewById(R.id.btn_visibility);
        btn_outlet_atten = (Button) findViewById(R.id.btn_outlet_atten);
        btn_stock = (Button) findViewById(R.id.btn_stock);
        btn_reports = (Button) findViewById(R.id.btn_report);
        btn_sale = (Button) findViewById(R.id.btn_sale);
        btn_notification = (Button) findViewById(R.id.btn_notifi);
        btn_datasync = (Button) findViewById(R.id.btn_master_sync);
        btn_BAoutsales = (Button) findViewById(R.id.btn_ba_sale_yr);
        btn_dashboard = (Button) findViewById(R.id.btn_dashboard);
        btn_total_outlet_sales = (Button) findViewById(R.id.btn_totaloutletSales);

        alarm = new AlarmManagerBroadcastReceiver();

        tv_h_username = (TextView) findViewById(R.id.tv_h_username);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        btn_home.setVisibility(View.INVISIBLE);

        //Set Onclick listner
        btn_attendance.setOnClickListener(this);
        btn_visibility.setOnClickListener(this);
        btn_outlet_atten.setOnClickListener(this);
        btn_stock.setOnClickListener(this);
        btn_reports.setOnClickListener(this);
        btn_sale.setOnClickListener(this);
        btn_notification.setOnClickListener(this);
        btn_datasync.setOnClickListener(this);
        btn_BAoutsales.setOnClickListener(this);
        btn_dashboard.setOnClickListener(this);
        btn_total_outlet_sales.setOnClickListener(this);
        btn_logout.setOnClickListener(this);


    }

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
                //startActivity(new Intent(getApplicationContext(), TotalOutletSaleActivity.class));
                break;
            case R.id.btn_stock:
                v.startAnimation(AnimationUtils.loadAnimation(DashBoardActivity.this, R.anim.button_click));
                startActivity(new Intent(getApplicationContext(), StockActivity.class));
                break;
            case R.id.btn_report:
                v.startAnimation(AnimationUtils.loadAnimation(DashBoardActivity.this, R.anim.button_click));
               // startActivity(new Intent(getApplicationContext(), ReportsForUser.class));
                break;
            case R.id.btn_sale:
                v.startAnimation(AnimationUtils.loadAnimation(DashBoardActivity.this, R.anim.button_click));
                startActivity(new Intent(getApplicationContext(), SaleActivity.class));
                break;
            case R.id.btn_notifi:
                v.startAnimation(AnimationUtils.loadAnimation(DashBoardActivity.this, R.anim.button_click));
                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                break;
            case R.id.btn_master_sync:
                v.startAnimation(AnimationUtils.loadAnimation(DashBoardActivity.this, R.anim.button_click));
                startActivity(new Intent(getApplicationContext(), SyncMasterActivity.class));
                break;
            case R.id.btn_ba_sale_yr:
                v.startAnimation(AnimationUtils.loadAnimation(DashBoardActivity.this, R.anim.button_click));
               // startActivity(new Intent(getApplicationContext(), BAYearWiseReport.class));
                break;
            case R.id.btn_dashboard:
                v.startAnimation(AnimationUtils.loadAnimation(DashBoardActivity.this, R.anim.button_click));
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
}
