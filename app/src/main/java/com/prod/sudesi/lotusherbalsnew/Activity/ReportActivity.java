package com.prod.sudesi.lotusherbalsnew.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.prod.sudesi.lotusherbalsnew.R;
import com.prod.sudesi.lotusherbalsnew.Utils.SharedPref;

import java.util.ArrayList;

/**
 * Created by Admin on 30-10-2017.
 */

public class ReportActivity extends Activity implements View.OnClickListener{

    Context context;
    RadioButton rbstock,rbattendance;
    TableRow table_row_stock, table_row_attend;
    private SharedPref sharedPref;
    TextView tv_h_username;
    Button btn_home, btn_logout;
    String username;
    String displayCategory;
    String outletCode = "";
    TextView txt_outletnm;
    Spinner sp_outletName;

    ListView stocklistview,attendancelist;

    HorizontalScrollView horizantalscrollviewforstock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        context = ReportActivity.this;

        sharedPref = new SharedPref(context);

        rbstock = (RadioButton) findViewById(R.id.rb_stock);
        rbattendance = (RadioButton) findViewById(R.id.rb_attendance);

        table_row_stock = (TableRow) findViewById(R.id.tr_label_stock);
        table_row_attend = (TableRow) findViewById(R.id.tr_label_attend);

        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        username = sharedPref.getLoginId();
        sp_outletName = (Spinner) findViewById(R.id.sp_outletName);
        txt_outletnm=(TextView) findViewById(R.id.txt_outletnm);
        ArrayList<String> outletNameArray = new ArrayList<String>();

        stocklistview = (ListView) findViewById(R.id.stock_list);
        attendancelist = (ListView) findViewById(R.id.attendancelist);

        horizantalscrollviewforstock = (HorizontalScrollView) findViewById(R.id.horizantal_scrollview_stock_report);

        horizantalscrollviewforstock.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                int scrollX = v.getScrollX();
                int scrollY = v.getScrollY();

                horizantalscrollviewforstock.scrollTo(scrollX, scrollY);

                return false;

            }
        });

        rbstock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (rbstock.isChecked()) {

                    txt_outletnm.setVisibility(View.VISIBLE);
                    sp_outletName.setVisibility(View.VISIBLE);
                    table_row_stock.setVisibility(View.VISIBLE);

                    table_row_attend.setVisibility(View.GONE);
                    attendancelist.setVisibility(View.GONE);
                    rbattendance.setChecked(false);


                }
            }
        });

        rbattendance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub

                if (isChecked) {

                    txt_outletnm.setVisibility(View.GONE);
                    sp_outletName.setVisibility(View.GONE);

                    table_row_stock.setVisibility(View.GONE);
                    table_row_attend.setVisibility(View.VISIBLE);
                    stocklistview.setVisibility(View.GONE);
                    attendancelist.setVisibility(View.VISIBLE);
                    rbstock.setChecked(false);
                    /*report_attendance = new ShowReportofAttendance();
                    report_attendance.execute();*/
                } else {

                    attendancelist.setVisibility(View.GONE);
                }
            }
        });

        btn_home.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_home:
                v.startAnimation(AnimationUtils.loadAnimation(ReportActivity.this, R.anim.button_click));
                Intent i = new Intent(getApplicationContext(),
                        DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                break;

            case R.id.btn_logout:
                v.startAnimation(AnimationUtils.loadAnimation(ReportActivity.this, R.anim.button_click));
                Intent i1 = new Intent(getApplicationContext(), LoginActivity.class);
                i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i1);

                break;

            default:
                break;
        }
    }
}
