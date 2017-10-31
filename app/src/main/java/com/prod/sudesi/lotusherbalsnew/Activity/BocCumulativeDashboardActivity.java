package com.prod.sudesi.lotusherbalsnew.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.prod.sudesi.lotusherbalsnew.Models.ProductModel;
import com.prod.sudesi.lotusherbalsnew.R;
import com.prod.sudesi.lotusherbalsnew.Utils.SharedPref;
import com.prod.sudesi.lotusherbalsnew.dbconfig.DbHelper;
import com.prod.sudesi.lotusherbalsnew.libs.ConnectionDetector;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Admin on 30-10-2017.
 */

public class BocCumulativeDashboardActivity extends Activity implements View.OnClickListener {

    Context context;
    ConnectionDetector cd;
    String str_BOC,year, year1,username;
    TextView txtboc, txtyear,tv_h_username;
    SharedPref sharedPref;
    Button btn_home, btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_boc_dashboard_commulative);

        context = BocCumulativeDashboardActivity.this;

        cd = new ConnectionDetector(context);
        sharedPref = new SharedPref(context);


        txtboc = (TextView) findViewById(R.id.txt_boc);
        txtyear = (TextView) findViewById(R.id.txt_year);

        Intent intent = getIntent();
        str_BOC = intent.getStringExtra("month");
        String y[] = intent.getStringExtra("year").split("-");
//		str_year = intent.getStringExtra("");
        Log.e("str_BOC", str_BOC);
        year = y[0];
        year1 = y[1];

        txtboc.setText(str_BOC);
        txtyear.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        username = sharedPref.getLoginId();
        tv_h_username = (TextView) findViewById(R.id.tv_h_username);
        tv_h_username.setText(username);

        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        btn_home.setOnClickListener(this);
        btn_logout.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_home:
                v.startAnimation(AnimationUtils.loadAnimation(BocCumulativeDashboardActivity.this, R.anim.button_click));
                Intent i = new Intent(getApplicationContext(),
                        DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                break;

            case R.id.btn_logout:
                v.startAnimation(AnimationUtils.loadAnimation(BocCumulativeDashboardActivity.this, R.anim.button_click));
                Intent i1 = new Intent(getApplicationContext(), LoginActivity.class);
                i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i1);

                break;

            default:
                break;
        }
    }
}
