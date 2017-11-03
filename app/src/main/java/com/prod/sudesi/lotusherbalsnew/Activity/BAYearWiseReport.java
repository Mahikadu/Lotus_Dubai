package com.prod.sudesi.lotusherbalsnew.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.prod.sudesi.lotusherbalsnew.Models.OutletModel;
import com.prod.sudesi.lotusherbalsnew.R;
import com.prod.sudesi.lotusherbalsnew.Utils.SharedPref;
import com.prod.sudesi.lotusherbalsnew.Utils.Utils;
import com.prod.sudesi.lotusherbalsnew.dbconfig.DbHelper;
import com.prod.sudesi.lotusherbalsnew.libs.ConnectionDetector;
import com.prod.sudesi.lotusherbalsnew.libs.LotusWebservice;

import java.util.ArrayList;

/**
 * Created by Admin on 04-11-2017.
 */

public class BAYearWiseReport extends Activity implements View.OnClickListener {

    Context context;
    ConnectionDetector cd;
    SharedPref sharedPref;
    Button btn_home, btn_logout;
    private ProgressDialog prgdialog;
    private Utils utils;
    LotusWebservice service;

    String str_Month, year, username;
    TextView txtboc, txtyear, tv_h_username;

    private ArrayList<OutletModel> outletDetailsArraylist;
    OutletModel outletModel;
    String[] strOutletArray = null;
    String outletstring;
    AutoCompleteTextView sp_outletName;
    String outletCode = "";
    String outletName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bayear_wise_report);

        context = BAYearWiseReport.this;

        cd = new ConnectionDetector(context);
        sharedPref = new SharedPref(context);
        prgdialog = new ProgressDialog(context);
        utils = new Utils(context);
        service = new LotusWebservice(BAYearWiseReport.this);

        username = sharedPref.getLoginId();
        tv_h_username = (TextView) findViewById(R.id.tv_h_username);
        tv_h_username.setText(username);

        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        sp_outletName = (AutoCompleteTextView) findViewById(R.id.spin_outletname);

        btn_home.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

        fetchOutletDetails();

        if (outletDetailsArraylist.size() > 0) {
            strOutletArray = new String[outletDetailsArraylist.size()];
            for (int i = 0; i < outletDetailsArraylist.size(); i++) {
                strOutletArray[i] = outletDetailsArraylist.get(i).getOutletName();
            }
        }
        if (outletDetailsArraylist != null && outletDetailsArraylist.size() > 0) {
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strOutletArray) {
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
            sp_outletName.setAdapter(adapter1);
        }

        sp_outletName.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sp_outletName.showDropDown();
                return false;
            }
        });

        sp_outletName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = "",outletcode = "";
                if (strOutletArray != null && strOutletArray.length > 0) {

                    outletstring = parent.getItemAtPosition(position).toString();
                    for (int i = 0; i < outletDetailsArraylist.size(); i++) {
                        text = outletDetailsArraylist.get(i).getOutletName();
                        outletcode = outletDetailsArraylist.get(i).getOutletCode();
                        if (text.equalsIgnoreCase(outletstring)) {
                            outletName = text;
                            outletCode = outletcode;
                        }
                    }

                    if (text != null && text.length() > 0) {
                        /*ReportActivity.ShowReportofStock showReportofStock = new ReportActivity.ShowReportofStock();
                        showReportofStock.execute(outletCode);*/
                    }else{
                        cd.displayMessage("Please Select outlet");
                    }


                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_home:
                v.startAnimation(AnimationUtils.loadAnimation(BAYearWiseReport.this, R.anim.button_click));
                Intent i = new Intent(getApplicationContext(),
                        DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                break;

            case R.id.btn_logout:
                v.startAnimation(AnimationUtils.loadAnimation(BAYearWiseReport.this, R.anim.button_click));
                Intent i1 = new Intent(getApplicationContext(), LoginActivity.class);
                i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i1);

                break;

            default:
                break;
        }
    }

    public void fetchOutletDetails() {
        try {
            outletDetailsArraylist = new ArrayList<OutletModel>();
            Cursor cursor = LOTUS.dbCon.fetchAlldata(DbHelper.TABLE_OUTLET);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    outletModel = new OutletModel();
                    outletModel.setMessage(cursor.getString(cursor.getColumnIndex("Message")));
                    outletModel.setOutletCode(cursor.getString(cursor.getColumnIndex("OutletCode")));
                    outletModel.setOutletName(cursor.getString(cursor.getColumnIndex("OutletName")));
                    outletDetailsArraylist.add(outletModel);
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


