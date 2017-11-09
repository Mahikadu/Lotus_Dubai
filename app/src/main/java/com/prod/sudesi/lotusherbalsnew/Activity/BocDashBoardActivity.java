package com.prod.sudesi.lotusherbalsnew.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.prod.sudesi.lotusherbalsnew.R;
import com.prod.sudesi.lotusherbalsnew.Utils.SharedPref;
import com.prod.sudesi.lotusherbalsnew.libs.ConnectionDetector;
import com.prod.sudesi.lotusherbalsnew.libs.LotusWebservice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 17-10-2017.
 */

public class BocDashBoardActivity extends Activity {

    private SharedPref sharedPref;

    Context context;

    private ProgressDialog prgdialog;

    TextView tv_h_username;
    Button btn_home, btn_logout;
    String username;

    LotusWebservice service;

    Button btn_search;
    AutoCompleteTextView spin_year,spin_month;

    String selected_month;
    String selected_year;

    //----------------
    String current_year_n2,CurrentYear,NextYear;

    int int_current_year_n1,int_current_year_n2,int_previous_year_p1,int_previous_year_p2;

    String current_server_date;

    String firstyear,sencondyear;

    //----------------

    String[] strBocArray = null;
    String[] strYearArray = null;

    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_boc_dashboard);

        context = BocDashBoardActivity.this;

        sharedPref = new SharedPref(context);
        prgdialog = new ProgressDialog(context);
        service = new LotusWebservice(BocDashBoardActivity.this);
        cd = new ConnectionDetector(context);

        tv_h_username = (TextView) findViewById(R.id.tv_h_username);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);


        username = sharedPref.getLoginId();
        Log.v("", "username==" + username);

        tv_h_username.setText(username);

        btn_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                v.startAnimation(AnimationUtils.loadAnimation(BocDashBoardActivity.this, R.anim.button_click));
                Intent i = new  Intent(getApplicationContext(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        btn_home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                v.startAnimation(AnimationUtils.loadAnimation(BocDashBoardActivity.this, R.anim.button_click));
                Intent i = new  Intent(getApplicationContext(), DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });


        //--------------/---------

        //--------------/---------
        try{
            current_year_n2 = sharedPref.getCurrentYear();
            int_current_year_n2 = Integer.parseInt(current_year_n2);

            if(!current_year_n2.equalsIgnoreCase("")){

                int int_current_year_n22 =  int_current_year_n2 + 1 ;

                NextYear = String.valueOf(int_current_year_n22);

                int_current_year_n1 = int_current_year_n22 - 1;

                CurrentYear = String.valueOf(int_current_year_n1);

            }
        }catch(Exception e){
            e.printStackTrace();
        }

        spin_month  = (AutoCompleteTextView) findViewById(R.id.spin_boc);
        spin_year = (AutoCompleteTextView)findViewById(R.id.spin_year);

        List<String> list_moth = new ArrayList<String>();
        list_moth.add("January");
        list_moth.add("February");
        list_moth.add("March");

        list_moth.add("April");
        list_moth.add("May");
        list_moth.add("June");

        list_moth.add("July");
        list_moth.add("Augest");
        list_moth.add("September");

        list_moth.add("October");
        list_moth.add("November");
        list_moth.add("December");


        if (list_moth.size() > 0) {
            strBocArray = new String[list_moth.size()];
            for (int i = 0; i < list_moth.size(); i++) {
                strBocArray[i] = list_moth.get(i);
            }
        }
        if (list_moth != null && list_moth.size() > 0) {
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strBocArray) {
                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View v = null;
                    // If this is the initial dummy entry, make it hidden
                    if (position == 0) {
                        TextView tv = new TextView(getContext());
                        tv.setHeight(0);
                        tv.setVisibility(View.GONE);
                        v = tv;
                    } else {
                        // Pass convertView as null to prevent reuse of special case views
                        v = super.getDropDownView(position, null, parent);
                    }
                    // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                    parent.setVerticalScrollBarEnabled(false);
                    return v;
                }
            };

            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin_month.setAdapter(adapter1);
        }

        spin_month.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                spin_month.showDropDown();
                return false;
            }
        });

        spin_month.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (strBocArray != null && strBocArray.length > 0) {
                    //strSourceofLead = spin_month.getText().toString();
                    selected_month = parent.getItemAtPosition(position).toString();
                }
            }
        });

        List<String> list_year = new ArrayList<String>();

        //list_year.add("Select");
        list_year.add(CurrentYear);
        list_year.add(NextYear);

        if (list_year.size() > 0) {
            strYearArray = new String[list_year.size()];
            for (int i = 0; i < list_year.size(); i++) {
                strYearArray[i] = list_year.get(i);
            }
        }
        if (list_year != null && list_year.size() > 0) {
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strYearArray) {
                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View v = null;
                    // If this is the initial dummy entry, make it hidden
                    if (position == 0) {
                        TextView tv = new TextView(getContext());
                        tv.setHeight(0);
                        tv.setVisibility(View.GONE);
                        v = tv;
                    } else {
                        // Pass convertView as null to prevent reuse of special case views
                        v = super.getDropDownView(position, null, parent);
                    }
                    // Hide scroll bar because it appears sometimes unnecessarily, this does not prevent scrolling
                    parent.setVerticalScrollBarEnabled(false);
                    return v;
                }
            };

            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin_year.setAdapter(adapter1);
        }

        spin_year.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                spin_year.showDropDown();
                return false;
            }
        });

        spin_year.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (strYearArray != null && strYearArray.length > 0) {
                    //strSourceofLead = spin_month.getText().toString();
                    selected_year = parent.getItemAtPosition(position).toString();
                }
            }
        });

        btn_search = (Button)findViewById(R.id.btn_search);

        btn_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                v.startAnimation(AnimationUtils.loadAnimation(BocDashBoardActivity.this, R.anim.button_click));

                try{

                    if(selected_month.equalsIgnoreCase("")){
                        Log.v("", "sdfsfddfsf");
                        //Toast.makeText(getApplicationContext(), "Select Month", Toast.LENGTH_SHORT).show();
                        cd.displayMessage("Select Month");

                    }else if(selected_year.equalsIgnoreCase("")){
                        Log.v("", "sdfsfddfsf1");
                        //Toast.makeText(getApplicationContext(), "Select Year", Toast.LENGTH_SHORT).show();
                        cd.displayMessage("Select Year");

                    }else{

                        Intent i = new Intent(getApplicationContext(), BocCumulativeDashboardActivity.class);
                        i.putExtra("month", selected_month);
                        i.putExtra("year", selected_year);
                        startActivity(i);
                    }
                }catch(Exception e){

                    e.printStackTrace();
                }
            }
        });
    }
}
