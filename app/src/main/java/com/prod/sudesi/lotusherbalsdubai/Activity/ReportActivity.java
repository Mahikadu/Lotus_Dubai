package com.prod.sudesi.lotusherbalsdubai.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.prod.sudesi.lotusherbalsdubai.Models.OutletModel;
import com.prod.sudesi.lotusherbalsdubai.Models.ServerAttendanceModel;
import com.prod.sudesi.lotusherbalsdubai.Models.StockReportModel;
import com.prod.sudesi.lotusherbalsdubai.R;
import com.prod.sudesi.lotusherbalsdubai.Utils.SharedPref;
import com.prod.sudesi.lotusherbalsdubai.Utils.Utils;
import com.prod.sudesi.lotusherbalsdubai.adapter.AttendanceAdapter;
import com.prod.sudesi.lotusherbalsdubai.adapter.ReportAdapter;
import com.prod.sudesi.lotusherbalsdubai.Dbconfig.DbHelper;
import com.prod.sudesi.lotusherbalsdubai.libs.ConnectionDetector;
import com.prod.sudesi.lotusherbalsdubai.libs.LotusWebservice;

import org.ksoap2.serialization.SoapObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

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
    String outletName = "";
    AutoCompleteTextView sp_outletName;

    ReportAdapter adapter;
    AttendanceAdapter adapterAttend;

    ListView stocklistview,attendancelist;

    HorizontalScrollView horizantalscrollviewforstock;

    private ArrayList<OutletModel> outletDetailsArraylist;
    OutletModel outletModel;
    String[] strOutletArray = null;
    String outletstring;

    public StockReportModel stockReportModel;
    private ArrayList<StockReportModel> stockReportDetailsArraylist;

    ConnectionDetector cd;

    private ProgressDialog mProgress = null;

    CardView outletcard;

    Date startdate, enddate;
    ArrayList<String> dates_array;
    private LotusWebservice service;
    String str_Month, year;

    private Utils utils;
    String ADatenew,AbsentType,Attendance,Message;

    private ArrayList<ServerAttendanceModel> serverAttendanceArraylist;
    ServerAttendanceModel serverAttendanceModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        context = ReportActivity.this;

        sharedPref = new SharedPref(context);

        cd = new ConnectionDetector(context);

        service = new LotusWebservice(context);

        utils = new Utils(context);

        mProgress = new ProgressDialog(context);

        rbstock = (RadioButton) findViewById(R.id.rb_stock);
        rbattendance = (RadioButton) findViewById(R.id.rb_attendance);

        table_row_stock = (TableRow) findViewById(R.id.tr_label_stock);
        table_row_attend = (TableRow) findViewById(R.id.tr_label_attend);

        outletcard = (CardView) findViewById(R.id.outletcardview);

        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        username = sharedPref.getLoginId();
        sp_outletName = (AutoCompleteTextView) findViewById(R.id.spin_outletname);
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

        cd.displayMessage("Please Select outlet");


        rbstock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (rbstock.isChecked()) {

                    sp_outletName.setText("");
                    sp_outletName.setVisibility(View.VISIBLE);
                    outletcard.setVisibility(View.VISIBLE);
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

                    sp_outletName.setText("");
                    sp_outletName.setVisibility(View.GONE);
                    outletcard.setVisibility(View.GONE);

                    table_row_stock.setVisibility(View.GONE);
                    table_row_attend.setVisibility(View.VISIBLE);
                    stocklistview.setVisibility(View.GONE);
                    attendancelist.setVisibility(View.VISIBLE);
                    rbstock.setChecked(false);
                    GetAttendance getAttendance = new GetAttendance();
                    getAttendance.execute();
                } else {

                    attendancelist.setVisibility(View.GONE);
                }
            }
        });

        btn_home.setOnClickListener(this);
        btn_logout.setOnClickListener(this);


        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(
                "yyyy-MMM-dd");
        String insert_timestamp = sdf.format(cal
                .getTime());

        String[] items1 = insert_timestamp.split("-");
         year = items1[0];
         str_Month = items1[1];


        System.out.println("   startdate--" + getStartEnd(str_Month, year)[0]);
        System.out.println("   enddate--" + getStartEnd(str_Month, year)[1]);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {

            startdate = format.parse(getStartEnd(str_Month, year)[0]);
            enddate = format.parse(getStartEnd(str_Month, year)[1]);

            System.out.println("   startdate1--" + startdate);
            System.out.println("   enddate1--" + enddate);

            List<Date> dates = getDaysBetweenDates(startdate, enddate);

            Log.e("dates", dates.toString());

            dates_array = new ArrayList<String>();

            for (int i = 0; i < dates.size(); i++) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                String reportDate = df.format(dates.get(i));
                Log.d("Date is", " " + reportDate);
                dates_array.add(reportDate);

                // Print what date is today!
                System.out.println("Report Date: " + reportDate);
            }


        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


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
                        ShowReportofStock showReportofStock = new ShowReportofStock();
                        showReportofStock.execute(outletCode);
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
                new SweetAlertDialog(ReportActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("ERROR !!")
                        .setContentText("Please Select outlet")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                Intent intent = new Intent(ReportActivity.this, DashBoardActivity.class);
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

    @SuppressLint("WrongConstant")
    public static List<Date> getDaysBetweenDates(Date startdate, Date enddate) {
        List<Date> dates = new ArrayList<Date>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startdate);

        while (calendar.getTime().before(enddate) || calendar.getTime().equals(enddate)) {
            Date result = calendar.getTime();
            dates.add(result);
            calendar.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public String[] getStartEnd(String Month, String year) {
        String startend[] = new String[2];

        if (Month.equalsIgnoreCase("Jan")) {
            startend[0] = year + "-01-01";
            startend[1] = year + "-01-31";
        } else if (Month.equalsIgnoreCase("Feb")) {
            startend[0] = year + "-02-01";
            startend[1] = year + "-02-28";
        } else if (Month.equalsIgnoreCase("Mar")) {
            startend[0] = year + "-03-01";
            startend[1] = year + "-03-31";
        } else if (Month.equalsIgnoreCase("Apr")) {
            startend[0] = year + "-04-01";
            startend[1] = year + "-04-30";
        } else if (Month.equalsIgnoreCase("May")) {
            startend[0] = year + "-05-01";
            startend[1] = year + "-05-31";
        } else if (Month.equalsIgnoreCase("Jun")) {
            startend[0] = year + "-06-01";
            startend[1] = year + "-06-30";
        } else if (Month.equalsIgnoreCase("Jul")) {
            startend[0] = year + "-07-01";
            startend[1] = year + "-07-31";
        } else if (Month.equalsIgnoreCase("Aug")) {
            startend[0] = year + "-08-01";
            startend[1] = year + "-08-31";
        } else if (Month.equalsIgnoreCase("Sept")) {
            startend[0] = year + "-09-01";
            startend[1] = year + "-09-30";
        } else if (Month.equalsIgnoreCase("Oct")) {
            startend[0] = year + "-10-01";
            startend[1] = year + "-10-31";
        } else if (Month.equalsIgnoreCase("Nov")) {
            startend[0] = year + "-11-01";
            startend[1] = year + "-11-30";
        } else if (Month.equalsIgnoreCase("Dec")) {
            startend[0] = year + "-12-01";
            startend[1] = year + "-12-31";
        }

        return startend;
    }

    public class ShowReportofStock extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            mProgress.setMessage("Please Wait");
            mProgress.show();
            mProgress.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {

                LOTUS.dbCon.open();
                String outlet = params[0];

                String where = " where outletcode = '" + outlet + "'";

                Cursor cursor = LOTUS.dbCon.fetchFromSelect(DbHelper.TABLE_STOCK, where);

                stockReportDetailsArraylist = new ArrayList<>();
                if (cursor != null && cursor.moveToFirst()) {
                    cursor.moveToFirst();
                    do {
                        stockReportModel = new StockReportModel();
                        stockReportModel.setA_id(cursor.getString(cursor.getColumnIndex("A_id")));
                        stockReportModel.setBrand(cursor.getString(cursor.getColumnIndex("Brand")));
                        stockReportModel.setCategory(cursor.getString(cursor.getColumnIndex("Category")));
                        stockReportModel.setSubCategory(cursor.getString(cursor.getColumnIndex("SubCategory")));
                        stockReportModel.setProductName(cursor.getString(cursor.getColumnIndex("ProductName")));
                        stockReportModel.setSize(cursor.getString(cursor.getColumnIndex("size")));
                        stockReportModel.setPTT(cursor.getString(cursor.getColumnIndex("PTT")));
                        stockReportModel.setOpening_stock(cursor.getString(cursor.getColumnIndex("opening_stock")));
                        stockReportModel.setStock_received(cursor.getString(cursor.getColumnIndex("stock_received")));
                        stockReportModel.setStock_in_hand(cursor.getString(cursor.getColumnIndex("stock_in_hand")));
                        stockReportModel.setSold_stock(cursor.getString(cursor.getColumnIndex("sold_stock")));
                        stockReportModel.setClose_bal(cursor.getString(cursor.getColumnIndex("close_bal")));
                        stockReportModel.setTotal_gross_amount(cursor.getString(cursor.getColumnIndex("total_gross_amount")));
                        stockReportModel.setDiscount(cursor.getString(cursor.getColumnIndex("discount")));
                        stockReportModel.setTotal_net_amount(cursor.getString(cursor.getColumnIndex("total_net_amount")));
                        stockReportModel.setOutletcode(cursor.getString(cursor.getColumnIndex("outletcode")));
                        stockReportModel.setSaveServer(cursor.getString(cursor.getColumnIndex("savedServer")));


                        stockReportDetailsArraylist.add(stockReportModel);

                    } while (cursor.moveToNext());

                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onPostExecute(String result) {


            Log.e("", "reportlist===" + stockReportDetailsArraylist);
            mProgress.dismiss();
            // TODO Auto-generated method stub
            stocklistview.setVisibility(View.VISIBLE);
            adapter = new ReportAdapter(context, stockReportDetailsArraylist);
            stocklistview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            LOTUS.dbCon.close();

            // }
        }

    }

    public class GetAttendance extends AsyncTask<Void, Void, SoapObject> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            mProgress.setMessage("Please Wait...");
            mProgress.show();
            mProgress.setCancelable(false);
        }

        @Override
        protected SoapObject doInBackground(Void... params) {
            // TODO Auto-generated method stub

            SoapObject result = null;
            try {
                if (cd.isConnectingToInternet()) {

                    String startdate[] = getStartEnd(str_Month, year);

                    result = service.GetAttendance(username,startdate[0], startdate[1]);

                    if (result != null) {
                        for (int i = 0; i < result.getPropertyCount(); i++) {


                            SoapObject root = (SoapObject) result.getProperty(i);

                            if (root.getPropertyAsString("ADatenew") != null) {

                                if (!root.getPropertyAsString("ADatenew").equalsIgnoreCase("anyType{}")) {
                                    ADatenew = root.getPropertyAsString("ADatenew");
                                } else {
                                    ADatenew = "";
                                }
                            } else {
                                ADatenew = "";
                            }

                            if (root.getPropertyAsString("AbsentType") != null) {

                                if (!root.getPropertyAsString("AbsentType").equalsIgnoreCase("anyType{}")) {
                                    AbsentType = root.getPropertyAsString("AbsentType");
                                } else {
                                    AbsentType = "";
                                }
                            } else {
                                AbsentType = "";
                            }

                            if (root.getPropertyAsString("Attendance") != null) {

                                if (!root.getPropertyAsString("Attendance").equalsIgnoreCase("anyType{}")) {
                                    Attendance = root.getPropertyAsString("Attendance");
                                } else {
                                    Attendance = "";
                                }
                            } else {
                                Attendance = "";
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

                            String valuesArray[] = {ADatenew,AbsentType,Attendance,Message};
                            boolean rowid = LOTUS.dbCon.updateBulk(DbHelper.TABLE_SERVER_ATTENDANCE, " ADatenew = ?", valuesArray, utils.columnNamesServerAttendance, new String[]{ADatenew});


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

                        String valuesArray[] = {ID, "Soup is Null While GetAttendance()", String.valueOf(n), "GetAttendance()",
                                Createddate, Createddate, sharedPref.getLoginId(), "Get Attendance", "Fail"};
                        boolean output = LOTUS.dbCon.updateBulk(DbHelper.SYNC_LOG, selection, valuesArray, utils.columnNamesSyncLog, selectionArgs);

                        LOTUS.dbCon.close();
                        cd.displayMessage("Soup is Null While GetAttendance()");
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

            mProgress.dismiss();

            if(soapObject != null){
                try {
                    serverAttendanceArraylist = new ArrayList<ServerAttendanceModel>();
                    Cursor cursor = LOTUS.dbCon.fetchAlldata(DbHelper.TABLE_SERVER_ATTENDANCE);
                    if (cursor != null && cursor.getCount() > 0) {
                        cursor.moveToFirst();
                        do {
                            serverAttendanceModel = new ServerAttendanceModel();
                            serverAttendanceModel.setADatenew(cursor.getString(cursor.getColumnIndex("ADatenew")));
                            serverAttendanceModel.setAbsentType(cursor.getString(cursor.getColumnIndex("AbsentType")));
                            serverAttendanceModel.setAttendance(cursor.getString(cursor.getColumnIndex("Attendance")));
                            serverAttendanceModel.setMessage(cursor.getString(cursor.getColumnIndex("Message")));

                            serverAttendanceArraylist.add(serverAttendanceModel);
                        } while (cursor.moveToNext());
                        cursor.close();
                    }

                    ShowAttendance();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



        }
    }

    private void ShowAttendance() {
        adapterAttend = new AttendanceAdapter(ReportActivity.this, serverAttendanceArraylist);
        attendancelist.setAdapter(adapterAttend);// add custom adapter to listview
        adapterAttend.notifyDataSetChanged();
    }
}
