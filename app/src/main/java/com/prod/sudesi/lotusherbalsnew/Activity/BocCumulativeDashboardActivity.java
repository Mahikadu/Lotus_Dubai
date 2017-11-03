package com.prod.sudesi.lotusherbalsnew.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.prod.sudesi.lotusherbalsnew.Models.DashboardDetailsModel;
import com.prod.sudesi.lotusherbalsnew.Models.OutletModel;
import com.prod.sudesi.lotusherbalsnew.Models.ProductModel;
import com.prod.sudesi.lotusherbalsnew.R;
import com.prod.sudesi.lotusherbalsnew.Utils.SharedPref;
import com.prod.sudesi.lotusherbalsnew.Utils.Utils;
import com.prod.sudesi.lotusherbalsnew.dbconfig.DbHelper;
import com.prod.sudesi.lotusherbalsnew.libs.ConnectionDetector;
import com.prod.sudesi.lotusherbalsnew.libs.LotusWebservice;

import org.ksoap2.serialization.SoapObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Admin on 30-10-2017.
 */

public class BocCumulativeDashboardActivity extends Activity implements View.OnClickListener {

    Context context;
    ConnectionDetector cd;
    String str_Month, year, username;
    TextView txtboc, txtyear, tv_h_username;
    SharedPref sharedPref;
    Button btn_home, btn_logout;

    Date startdate, enddate;
    ArrayList<String> dates_array;
    private ProgressDialog prgdialog;
    private LotusWebservice service;
    String strDate, strSoldQty, strSoldvalue;
    private Utils utils;
    TableLayout tl_cumulative;
    DashboardDetailsModel dashboardDetailsModel;


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
        prgdialog = new ProgressDialog(context);
        utils = new Utils(context);


        txtboc = (TextView) findViewById(R.id.txt_boc);
        txtyear = (TextView) findViewById(R.id.txt_year);

        tl_cumulative = (TableLayout) findViewById(R.id.tl_cumulative);

        Intent intent = getIntent();
        str_Month = intent.getStringExtra("month");
        String y[] = intent.getStringExtra("year").split("-");
//		str_year = intent.getStringExtra("");
        Log.e("str_Month", str_Month);
        year = y[0];
        //year1 = y[1];

        txtboc.setText(str_Month);
        txtyear.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));
        username = sharedPref.getLoginId();
        tv_h_username = (TextView) findViewById(R.id.tv_h_username);
        tv_h_username.setText(username);

        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        btn_home.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

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

        if (Month.equalsIgnoreCase("January")) {
            startend[0] = year + "-01-01";
            startend[1] = year + "-01-31";
        } else if (Month.equalsIgnoreCase("February")) {
            startend[0] = year + "-02-01";
            startend[1] = year + "-02-28";
        } else if (Month.equalsIgnoreCase("March")) {
            startend[0] = year + "-03-01";
            startend[1] = year + "-03-31";
        } else if (Month.equalsIgnoreCase("April")) {
            startend[0] = year + "-04-01";
            startend[1] = year + "-04-30";
        } else if (Month.equalsIgnoreCase("May")) {
            startend[0] = year + "-05-01";
            startend[1] = year + "-05-31";
        } else if (Month.equalsIgnoreCase("June")) {
            startend[0] = year + "-06-01";
            startend[1] = year + "-06-30";
        } else if (Month.equalsIgnoreCase("July")) {
            startend[0] = year + "-07-01";
            startend[1] = year + "-07-31";
        } else if (Month.equalsIgnoreCase("Augest")) {
            startend[0] = year + "-08-01";
            startend[1] = year + "-08-31";
        } else if (Month.equalsIgnoreCase("September")) {
            startend[0] = year + "-09-01";
            startend[1] = year + "-09-30";
        } else if (Month.equalsIgnoreCase("October")) {
            startend[0] = year + "-10-01";
            startend[1] = year + "-10-31";
        } else if (Month.equalsIgnoreCase("November")) {
            startend[0] = year + "-11-01";
            startend[1] = year + "-11-30";
        } else if (Month.equalsIgnoreCase("December")) {
            startend[0] = year + "-12-01";
            startend[1] = year + "-12-31";
        }

        return startend;
    }

    public class DashbardData extends AsyncTask<Void, Void, SoapObject> {

        String returnMessage = "";

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

            SoapObject results = null;
            try {

                if (cd.isConnectingToInternet()) {
                    LOTUS.dbCon.open();

                    String startdate[] = getStartEnd(str_Month, year);

                    String outletcode = null;
                    outletcode = LOTUS.dbCon.getActiveoutletCode();

                    if (outletcode != null || outletcode != "") {

                        results = service.GetDashboardData(startdate[0], startdate[1], outletcode, username);

                    } else {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(BocCumulativeDashboardActivity.this);
                        // set title
                        alertDialogBuilder.setTitle("ERROR !!");

                        // set dialog message
                        alertDialogBuilder.setMessage("Please Select outlet").setCancelable(false)

                                .setNegativeButton("YES",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();

                                                Intent i = new Intent(getApplicationContext(), DashBoardActivity.class);
                                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(i);
                                            }
                                        });

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder
                                .create();

                        // show it
                        alertDialog.show();

                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                //flag="1";
                e.printStackTrace();
            }
            return results;
        }


        @Override
        protected void onPostExecute(SoapObject soapObject) {
            // TODO Auto-generated method stub
            super.onPostExecute(soapObject);
            prgdialog.dismiss();

            if (soapObject != null) {
                for (int i = 0; i < soapObject.getPropertyCount(); i++) {

                    SoapObject root = (SoapObject) soapObject.getProperty(i);

                    if (root.getPropertyAsString("Date ") != null) {

                        if (!root.getPropertyAsString("Date ").equalsIgnoreCase("anyType{}")) {
                            strDate = root.getPropertyAsString("Date ");
                        } else {
                            strDate = "";
                        }
                    } else {
                        strDate = "";
                    }

                    if (root.getPropertyAsString("SoldQty  ") != null) {

                        if (!root.getPropertyAsString("SoldQty  ").equalsIgnoreCase("anyType{}")) {
                            strSoldQty = root.getPropertyAsString("SoldQty  ");
                        } else {
                            strSoldQty = "";
                        }
                    } else {
                        strSoldQty = "";
                    }

                    if (root.getPropertyAsString("Soldvalue   ") != null) {

                        if (!root.getPropertyAsString("Soldvalue   ").equalsIgnoreCase("anyType{}")) {
                            strSoldvalue = root.getPropertyAsString("Soldvalue   ");
                        } else {
                            strSoldvalue = "";
                        }
                    } else {
                        strSoldvalue = "";
                    }

                    String valuesArray[] = {strDate, strSoldQty, strSoldvalue};
                    long rowid = LOTUS.dbCon.insert(DbHelper.TABLE_DASHBOARD_DETAILS, valuesArray, utils.columnNamesDashboardDetails);

                    if (rowid > 0) {

                        try {
                            LOTUS.dbCon.open();
                            Cursor c = LOTUS.dbCon.fetchAll(DbHelper.TABLE_DASHBOARD_DETAILS);
                            if (c != null && c.getCount() > 0) {
                                c.moveToFirst();
                                do {
                                    dashboardDetailsModel = new DashboardDetailsModel();
                                    dashboardDetailsModel.setStrDate(c.getString(c.getColumnIndex("Date")));
                                    dashboardDetailsModel.setStrSoldQty(c.getString(c.getColumnIndex("SoldQty")));
                                    dashboardDetailsModel.setStrSoldvalue(c.getString(c.getColumnIndex("Soldvalue")));

                                    View tr = (TableRow) View.inflate(BocCumulativeDashboardActivity.this,
                                            R.layout.inflate_commulative_row, null);

                                    TextView txtdate = (TextView) tr.findViewById(R.id.txtdate);
                                    TextView txtsaleValue = (TextView) tr.findViewById(R.id.txtvalue);
                                    TextView txtsaleUnit = (TextView) tr.findViewById(R.id.txtunit);
//

                                    txtdate.setText(dashboardDetailsModel.getStrDate());
                                    txtsaleValue.setText(dashboardDetailsModel.getStrSoldvalue());
                                    txtsaleUnit.setText(dashboardDetailsModel.getStrSoldQty());

                                    tl_cumulative.addView(tr);

                                } while (c.moveToNext());
                            }
                            LOTUS.dbCon.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }
            }

        }

    }
}
