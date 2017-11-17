package com.prod.sudesi.lotusherbalsdubai.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TextView;

import com.prod.sudesi.lotusherbalsdubai.Models.BAYearWiseModel;
import com.prod.sudesi.lotusherbalsdubai.Models.OutletModel;
import com.prod.sudesi.lotusherbalsdubai.R;
import com.prod.sudesi.lotusherbalsdubai.Utils.SharedPref;
import com.prod.sudesi.lotusherbalsdubai.Utils.Utils;
import com.prod.sudesi.lotusherbalsdubai.adapter.BAYearWiseReportAdapter;
import com.prod.sudesi.lotusherbalsdubai.Dbconfig.DbHelper;
import com.prod.sudesi.lotusherbalsdubai.libs.ConnectionDetector;
import com.prod.sudesi.lotusherbalsdubai.libs.LotusWebservice;

import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;

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
    TextView txtboc, txtyear, tv_h_username,tvcurrentyear,tvNextyear;

    private ArrayList<OutletModel> outletDetailsArraylist;
    OutletModel outletModel;
    String[] strOutletArray = null;
    String outletstring;
    AutoCompleteTextView sp_outletName;
    String outletCode = "";
    String outletName = "";

    private ArrayList<BAYearWiseModel> bayearDetailsArraylist;
    BAYearWiseModel baYearWiseModel;
    ListView lv_ba_report;
    String GrowthCSkin,GrowthPSkin,Message,NetAmountCSkin,NetAmountPSkin,years_MonthsC,years_MonthsP;
    private BAYearWiseReportAdapter adapter;

    String NextYear,CurrentYear;


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

        try{
            String current_year_n2 = sharedPref.getCurrentYear();
            int int_current_year_n2 = Integer.parseInt(current_year_n2);

            if(!current_year_n2.equalsIgnoreCase("")){

                int int_previous_year_n22 =  int_current_year_n2 + 1 ;

                 NextYear = String.valueOf(int_previous_year_n22);

                 CurrentYear = String.valueOf(int_current_year_n2);

            }
        }catch(Exception e){
            e.printStackTrace();
        }

        tvNextyear = (TextView) findViewById(R.id.tvCurrentyear);
        tvcurrentyear = (TextView) findViewById(R.id.tvPreviousyear);

        tvNextyear.setText(NextYear);
        tvcurrentyear.setText(CurrentYear);

        lv_ba_report = (ListView) findViewById(R.id.listView_ba_year_report);

        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        sp_outletName = (AutoCompleteTextView) findViewById(R.id.spin_outletname);

        btn_home.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

        cd.displayMessage("Please Select outlet");

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
                String text = "", outletcode = "";
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
                        new GetBAYearWisereport().execute();
                    } else {
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
            }else{
                new SweetAlertDialog(BAYearWiseReport.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("ERROR !!")
                        .setContentText("Please Select outlet")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Intent intent = new Intent(BAYearWiseReport.this, DashBoardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);

                                sDialog.dismiss();
                            }
                        })
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class GetBAYearWisereport extends AsyncTask<Void, Void, SoapObject> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            prgdialog.setMessage("Please Wait...");
            prgdialog.show();
            prgdialog.setCancelable(false);
        }

        @Override
        protected SoapObject doInBackground(Void... params) {
            // TODO Auto-generated method stub

            SoapObject result = null;
            try {
                if (cd.isConnectingToInternet()) {

                    result = service.BAOutletSale(username,outletCode);

                    if (result != null) {
                        for (int i = 0; i < result.getPropertyCount(); i++) {

                            SoapObject root = (SoapObject) result.getProperty(i);

                            if (root.getPropertyAsString("GrowthCSkin") != null) {

                                if (!root.getPropertyAsString("GrowthCSkin").equalsIgnoreCase("anyType{}")) {
                                    GrowthCSkin = root.getPropertyAsString("GrowthCSkin");
                                } else {
                                    GrowthCSkin = "";
                                }
                            } else {
                                GrowthCSkin = "";
                            }

                            if (root.getPropertyAsString("GrowthPSkin") != null) {

                                if (!root.getPropertyAsString("GrowthPSkin").equalsIgnoreCase("anyType{}")) {
                                    GrowthPSkin = root.getPropertyAsString("GrowthPSkin");
                                } else {
                                    GrowthPSkin = "";
                                }
                            } else {
                                GrowthPSkin = "";
                            }

                            if (root.getPropertyAsString("Message") != null) {

                                if (!root.getPropertyAsString("Message").equalsIgnoreCase("anyType{}")) {
                                    Message = root.getPropertyAsString("Message");
                                } else {
                                    Message = "";
                                }
                            } else {
                                Message = "";
                            }


                            if (root.getPropertyAsString("NetAmountCSkin") != null) {

                                if (!root.getPropertyAsString("NetAmountCSkin").equalsIgnoreCase("anyType{}")) {
                                    NetAmountCSkin = root.getPropertyAsString("NetAmountCSkin");
                                } else {
                                    NetAmountCSkin = "";
                                }
                            } else {
                                NetAmountCSkin = "";
                            }

                            if (root.getPropertyAsString("NetAmountPSkin") != null) {

                                if (!root.getPropertyAsString("NetAmountPSkin").equalsIgnoreCase("anyType{}")) {
                                    NetAmountPSkin = root.getPropertyAsString("NetAmountPSkin");
                                } else {
                                    NetAmountPSkin = "";
                                }
                            } else {
                                NetAmountPSkin = "";
                            }

                            if (root.getPropertyAsString("years_MonthsC") != null) {

                                if (!root.getPropertyAsString("years_MonthsC").equalsIgnoreCase("anyType{}")) {
                                    years_MonthsC = root.getPropertyAsString("years_MonthsC");
                                } else {
                                    years_MonthsC = "";
                                }
                            } else {
                                years_MonthsC = "";
                            }

                            if (root.getPropertyAsString("years_MonthsP") != null) {

                                if (!root.getPropertyAsString("years_MonthsP").equalsIgnoreCase("anyType{}")) {
                                    years_MonthsP = root.getPropertyAsString("years_MonthsP");
                                } else {
                                    years_MonthsP = "";
                                }
                            } else {
                                years_MonthsP = "";
                            }

                            String valuesArray[] = {GrowthCSkin,GrowthPSkin,Message,NetAmountCSkin,NetAmountPSkin,years_MonthsC,years_MonthsP};
                            boolean rowid = LOTUS.dbCon.updateBulk(DbHelper.TABLE_BAYEARREPORT_DETAILS, " years_MonthsC = ?", valuesArray, utils.columnNamesBAyearReport, new String[]{years_MonthsC});


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

                        String valuesArray[] = {ID, "Soup is Null While BAOutletSale()", String.valueOf(n), "BAOutletSale()",
                                Createddate, Createddate, sharedPref.getLoginId(), "BA Outlet Sale", "Fail"};
                        boolean output = LOTUS.dbCon.updateBulk(DbHelper.SYNC_LOG, selection, valuesArray, utils.columnNamesSyncLog, selectionArgs);

                        LOTUS.dbCon.close();
                        cd.displayMessage("Soup is Null While BAOutletSale()");
                    }

                }

            } catch (Exception e) {
                e.getMessage();
            }
            return result;

        }

        @Override
        protected void onPostExecute(SoapObject soapObject) {
            // TODO Auto-generated method stub
            super.onPostExecute(soapObject);

            prgdialog.dismiss();

            if(soapObject != null){
                try {
                    bayearDetailsArraylist = new ArrayList<BAYearWiseModel>();
                    Cursor cursor = LOTUS.dbCon.fetchAlldata(DbHelper.TABLE_BAYEARREPORT_DETAILS);
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {
                            baYearWiseModel = new BAYearWiseModel();
                            baYearWiseModel.setGrowthCSkin(cursor.getString(cursor.getColumnIndex("GrowthCSkin")));
                            baYearWiseModel.setGrowthPSkin(cursor.getString(cursor.getColumnIndex("GrowthPSkin")));
                            baYearWiseModel.setMessage(cursor.getString(cursor.getColumnIndex("Message")));
                            baYearWiseModel.setNetAmountCSkin(cursor.getString(cursor.getColumnIndex("NetAmountCSkin")));
                            baYearWiseModel.setNetAmountPSkin(cursor.getString(cursor.getColumnIndex("NetAmountPSkin")));
                            baYearWiseModel.setYears_MonthsC(cursor.getString(cursor.getColumnIndex("years_MonthsC")));
                            baYearWiseModel.setYears_MonthsP(cursor.getString(cursor.getColumnIndex("years_MonthsP")));

                            bayearDetailsArraylist.add(baYearWiseModel);
                        } while (cursor.moveToNext());
                        cursor.close();
                    }

                    loadReports();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



        }
    }


    private void loadReports() {
        adapter = new BAYearWiseReportAdapter(BAYearWiseReport.this, bayearDetailsArraylist);
        lv_ba_report.setAdapter(adapter);// add custom adapter to listview
    }
}


