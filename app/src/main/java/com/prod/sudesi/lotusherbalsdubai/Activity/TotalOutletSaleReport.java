package com.prod.sudesi.lotusherbalsdubai.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.prod.sudesi.lotusherbalsdubai.R;
import com.prod.sudesi.lotusherbalsdubai.Utils.SharedPref;
import com.prod.sudesi.lotusherbalsdubai.adapter.TotalOutletSalesAdapter;
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

    String str_Month;
    String year;
    ListView total_outlet_list;
    TextView tv_h_username;
    Button btn_home, btn_logout;
    String username;
    Date startdate, enddate;
    ArrayList<String> dates_array;
    ProgressDialog progress;

    private TotalOutletSalesAdapter adapter;

    ArrayList<HashMap<String, String>> final_array;


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
        progress = new ProgressDialog(context);

        service = new LotusWebservice(TotalOutletSaleReport.this);

        final_array = new ArrayList<HashMap<String, String>>();


        Intent intent = getIntent();
        str_Month = intent.getStringExtra("month");
        String y[] = intent.getStringExtra("year").split("-");
//		str_year = intent.getStringExtra("");
        Log.e("str_Month", str_Month);
        year = y[0];
        //year1 = y[1];

        total_outlet_list = (ListView) findViewById(R.id.lv_total_outlet_sales);

        tv_h_username = (TextView) findViewById(R.id.tv_h_username);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        btn_home.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

        username = sharedPref.getLoginId();
        Log.v("", "username==" + username);
        tv_h_username.setText(username);

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

        try {
            new TotalOutletSale().execute();
        }catch(Exception e){
            e.printStackTrace();
        }

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
        } else if (Month.equalsIgnoreCase("August")) {
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



    public class TotalOutletSale extends AsyncTask<Void, Void, SoapObject> {

        SoapObject soap_result;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress.setTitle("Status");
            progress.setMessage("Please wait...");
            progress.show();
        }


        @Override
        protected SoapObject doInBackground(Void... params) {
            // TODO Auto-generated method stub

            if (!cd.isConnectingToInternet()) {
                soap_result=null;
            } else {
                String startdate[] = getStartEnd(str_Month, year);

                soap_result = service.TotalOutletSaleAPK(username,startdate[0],startdate[1]);
                if (soap_result != null) {
                    for (int i = 0; i < soap_result.getPropertyCount(); i++)
                    {
                        SoapObject getmessaage = (SoapObject) soap_result.getProperty(i);

                        if (getmessaage != null )
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
        protected void onPostExecute(SoapObject result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progress.dismiss();
            if (result != null)
            {
                adapter = new TotalOutletSalesAdapter(TotalOutletSaleReport.this, final_array);
                total_outlet_list.setAdapter(adapter);// add custom adapter to


            } else {
                cd.displayMessage("Please check internet Connectivity");
            }
        }

    }
}
