package com.prod.sudesi.lotusherbalsnew.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.prod.sudesi.lotusherbalsnew.R;
import com.prod.sudesi.lotusherbalsnew.Utils.SharedPref;
import com.prod.sudesi.lotusherbalsnew.dbconfig.DataBaseCon;
import com.prod.sudesi.lotusherbalsnew.libs.LotusWebservice;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Admin on 24-10-2017.
 */

public class OutletAttendanceActivity extends Activity implements View.OnClickListener {

    TextView txt_currentDate, txt_BDE;
    TimePicker time_picker;
    Resources system;
    String BDEusername, username, BDE_CODE, Adate = "", Actual_date = "", outletCode = "";
    private SharedPref sharedPref;
    Spinner sp_outletName;
    Button btn_submit, btn_home, btn_logout;
    private double lon = 0.0, lat = 0.0;
    LotusWebservice service;
   // AttendanceAsync upload_attendance;
    TextView tv_h_username;
    String outletName = "";
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_outlet_attedence);

        context = OutletAttendanceActivity.this;

        sharedPref = new SharedPref(context);
        LOTUS.dbCon = DataBaseCon.getInstance(getApplicationContext());

        LOTUS.dbCon.open();

        service = new LotusWebservice(OutletAttendanceActivity.this);
        BDEusername = sharedPref.getbaName();
        //BDE_CODE = sp.getString("BDE_Code", "");
        username = sharedPref.getLoginId();

        txt_currentDate = (TextView) findViewById(R.id.txt_currentDate);
        tv_h_username = (TextView) findViewById(R.id.tv_h_username);
        txt_BDE = (TextView) findViewById(R.id.txt_BDE);
        time_picker = (TimePicker) findViewById(R.id.timePicker1);
        time_picker.setIs24HourView(true);
        set_timepicker_text_colour();
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        btn_home.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

        Log.v("", "username==" + username);
        tv_h_username.setText(username);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(cal.getTime());
        txt_currentDate.setText(date);

        txt_BDE.setText(BDEusername);
        ArrayList<String> outletNameArray = new ArrayList<String>();
    }

    private void set_timepicker_text_colour() {
        system = Resources.getSystem();
        int hour_numberpicker_id = system
                .getIdentifier("hour", "id", "android");
        int minute_numberpicker_id = system.getIdentifier("minute", "id",
                "android");

        NumberPicker hour_numberpicker = (NumberPicker) time_picker
                .findViewById(hour_numberpicker_id);
        NumberPicker minute_numberpicker = (NumberPicker) time_picker
                .findViewById(minute_numberpicker_id);

        set_numberpicker_text_colour(hour_numberpicker);
        set_numberpicker_text_colour(minute_numberpicker);
    }

    private void set_numberpicker_text_colour(NumberPicker number_picker) {
        final int count = number_picker.getChildCount();
        final int color = Color.parseColor("#ffffff");

        for (int i = 0; i < count; i++) {
            View child = number_picker.getChildAt(i);

            try {
                Field wheelpaint_field = number_picker.getClass()
                        .getDeclaredField("mSelectorWheelPaint");
                wheelpaint_field.setAccessible(true);

                ((Paint) wheelpaint_field.get(number_picker)).setColor(color);
                ((EditText) child).setTextColor(color);
                number_picker.invalidate();
            } catch (NoSuchFieldException e) {
                Log.w("NumberPickerTxtColor", e);
            } catch (IllegalAccessException e) {
                Log.w("NumberPickerTxtColor", e);
            } catch (IllegalArgumentException e) {
                Log.w("NumberPickerTxtColor", e);
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_home:

                Intent i = new Intent(getApplicationContext(),
                        DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                break;

            case R.id.btn_logout:

                Intent i1 = new Intent(getApplicationContext(), LoginActivity.class);
                i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i1);

                break;

            default:
                break;
        }
    }
}
