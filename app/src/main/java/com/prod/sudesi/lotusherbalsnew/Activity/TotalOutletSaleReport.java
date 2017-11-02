package com.prod.sudesi.lotusherbalsnew.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.prod.sudesi.lotusherbalsnew.R;
import com.prod.sudesi.lotusherbalsnew.Utils.SharedPref;
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
 * Created by Admin on 31-10-2017.
 */

public class TotalOutletSaleReport extends Activity implements View.OnClickListener {

    public Context context;
    ConnectionDetector cd;
    SharedPref sharedPref;
    LotusWebservice service;

    String str_BOC;
    String year, year1;
    ListView total_outlet_list;
    TextView tv_h_username;
    Button btn_home, btn_logout;
    String username;
    Date startdate, enddate;
    ArrayList<String> dates_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_total_outlet);

        context = TotalOutletSaleReport.this;

        cd = new ConnectionDetector(context);
        sharedPref = new SharedPref(context);

        service = new LotusWebservice(TotalOutletSaleReport.this);

        Intent intent = getIntent();
        str_BOC = intent.getStringExtra("month");
        String y[] = intent.getStringExtra("year").split("-");
//		str_year = intent.getStringExtra("");
        Log.e("str_BOC", str_BOC);
        year = y[0];
        year1 = y[1];

        total_outlet_list = (ListView) findViewById(R.id.lv_total_outlet_sales);

        tv_h_username = (TextView) findViewById(R.id.tv_h_username);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        btn_home.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

        username = sharedPref.getLoginId();
        Log.v("", "username==" + username);
        tv_h_username.setText(username);

       /* System.out.println("   startdate--" + getStartEnd(str_BOC, year, year1)[0]);
        System.out.println("   enddate--" + getStartEnd(str_BOC, year, year1)[1]);
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        try {

            startdate = format.parse(getStartEnd(str_BOC, year, year1)[0]);
            enddate = format.parse(getStartEnd(str_BOC, year, year1)[1]);

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
        }*/

       // new TotalOutletSale().execute();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_home:
                v.startAnimation(AnimationUtils.loadAnimation(TotalOutletSaleReport.this, R.anim.button_click));
                Intent i = new Intent(getApplicationContext(),
                        DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                break;

            case R.id.btn_logout:
                v.startAnimation(AnimationUtils.loadAnimation(TotalOutletSaleReport.this, R.anim.button_click));
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


    public String[] getStartEnd(String BOC, String year, String year1) {
        String startend[] = new String[2];

        if (BOC.equalsIgnoreCase("BOC1")) {
            startend[0] = year + "-03-26";
            startend[1] = year + "-04-25";
        } else if (BOC.equalsIgnoreCase("BOC2")) {
            startend[0] = year + "-04-26";
            startend[1] = year + "-05-25";
        } else if (BOC.equalsIgnoreCase("BOC3")) {
            startend[0] = year + "-05-26";
            startend[1] = year + "-06-25";
        } else if (BOC.equalsIgnoreCase("BOC4")) {
            startend[0] = year + "-06-26";
            startend[1] = year + "-07-25";
        } else if (BOC.equalsIgnoreCase("BOC5")) {
            startend[0] = year + "-07-26";
            startend[1] = year + "-08-25";
        } else if (BOC.equalsIgnoreCase("BOC6")) {
            startend[0] = year + "-08-26";
            startend[1] = year + "-09-25";
        } else if (BOC.equalsIgnoreCase("BOC7")) {
            startend[0] = year + "-09-26";
            startend[1] = year + "-10-25";
        } else if (BOC.equalsIgnoreCase("BOC8")) {
            startend[0] = year + "-10-26";
            startend[1] = year + "-11-25";
        } else if (BOC.equalsIgnoreCase("BOC9")) {
            startend[0] = year + "-11-26";
            startend[1] = year + "-12-25";
        } else if (BOC.equalsIgnoreCase("BOC10")) {
            startend[0] = year + "-12-26";
            startend[1] = year1 + "-01-25";
        } else if (BOC.equalsIgnoreCase("BOC11")) {
            startend[0] = year1 + "-01-26";
            startend[1] = year1 + "-02-25";
        } else if (BOC.equalsIgnoreCase("BOC12")) {
            startend[0] = year1 + "-02-26";
            startend[1] = year1 + "-03-25";
        }

        return startend;
    }

    /*public class TotalOutletSale extends AsyncTask<Void, Void, SoapObject> {

        SoapObject soap_result;
//        ProgressDialog progress;


        @Override
        protected SoapObject doInBackground(Void... params) {
            // TODO Auto-generated method stub

            if (!cd.isConnectingToInternet()) {

                soap_result=null;
            } else
            {

                DateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                String startdate[] = getStartEnd(str_BOC, year, year1);

                soap_result = service.TotalOutletSaleAPK(username,startdate[0],startdate[1]);
                if (soap_result != null) {
                    for (int i = 0; i < soap_result.getPropertyCount(); i++)
                    {
                        SoapObject getmessaage = (SoapObject) soap_result.getProperty(i);
                        if (getmessaage != null)
                        {
                            if(getmessaage.getProperty("outletname")!=null && !getmessaage.getProperty("outletname").toString().equalsIgnoreCase("anyType{}"))

                            {
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("outletname", String.valueOf(getmessaage.getProperty("outletname")));
                                if(getmessaage.getProperty("SoldStock")!=null && !getmessaage.getProperty("SoldStock").toString().equalsIgnoreCase("anyType{}"))
                                {
                                    map.put("SoldStock", String.valueOf(getmessaage.getProperty("SoldStock")));
                                }else
                                {
                                    map.put("SoldStock", "0");
                                }

                                if(getmessaage.getProperty("NetAmount")!=null && !getmessaage.getProperty("NetAmount").toString().equalsIgnoreCase("anyType{}"))
                                {
                                    map.put("NetAmount", String.valueOf(getmessaage.getProperty("NetAmount")));
                                }else
                                {
                                    map.put("NetAmount", "0");
                                }

                                final_array.add(map);
                            }
                        }
                    }

                }
            }

            return soap_result;
        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
//            progress = new ProgressDialog(getApplicationContext());
//            progress.setTitle("Status");
//            progress.setMessage("Please wait...");
//            progress.show();
            progress.show();
        }

        @Override
        protected void onPostExecute(SoapObject result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progress.dismiss();
            if (result != null)
            {
                adapter = new TotalOutletSalesAdapter(TotalOutletSales.this, final_array);
                total_outlet_list.setAdapter(adapter);// add custom adapter to


            } else {
                Toast.makeText(TotalOutletSales.this,
                        "Please check internet Connectivity", Toast.LENGTH_LONG)
                        .show();
            }
        }

    }*/
}
