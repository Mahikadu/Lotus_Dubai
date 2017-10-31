package com.prod.sudesi.lotusherbalsnew.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.prod.sudesi.lotusherbalsnew.R;
import com.prod.sudesi.lotusherbalsnew.Utils.SharedPref;
import com.prod.sudesi.lotusherbalsnew.libs.LotusWebservice;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 17-10-2017.
 */

public class NotificationActivity extends Activity {

    Context context;

    private SharedPref sharedPref;

    static public ArrayList<HashMap<String, String>> todaymessagelist = new ArrayList<HashMap<String, String>>();

    private ListView listView;

    //private NotificationAdapter adapter;


    LotusWebservice service;
    private ProgressDialog mProgress = null;

    TextView tv_h_username;
    Button btn_home,btn_logout;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_notification);

        context = getApplicationContext();

        sharedPref = new SharedPref(context);

        listView = (ListView) findViewById(android.R.id.list);

        mProgress = new ProgressDialog(NotificationActivity.this);
        service = new LotusWebservice(NotificationActivity.this);

        //------------------

        tv_h_username = (TextView) findViewById(R.id.tv_h_username);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        username = sharedPref.getLoginId();
        tv_h_username.setText(username);

        btn_logout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                v.startAnimation(AnimationUtils.loadAnimation(NotificationActivity.this, R.anim.button_click));
                Intent i = new  Intent(getApplicationContext(), LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

        btn_home.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                v.startAnimation(AnimationUtils.loadAnimation(NotificationActivity.this, R.anim.button_click));
                Intent i = new  Intent(getApplicationContext(), DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
        //---------------------

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        try{
            new SendToSeverImageData().execute();
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }

    public class SendToSeverImageData extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub

            String returnMessage = null;
            try {
                todaymessagelist.clear();
               // getTodayMessageWebservice();//soap messag call
            } catch (Exception e) {
                returnMessage = e.getMessage();
            }
            return returnMessage;


        }

        @Override
        protected void onPreExecute() {

            //System.out.println("running in stage 1******");
            mProgress.setMessage("Sending..");
            mProgress.show();
            mProgress.setCancelable(false);
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            mProgress.dismiss();
            //loadmessage();
        }

    }


   /* private void getTodayMessageWebservice() {
        try {
            // //soap call
            Log.e("","not2");
           // SoapObject resultsRequestSOAP = service.GetNotification(EmpID);

            if (resultsRequestSOAP != null) {
                Log.e("","not3");
                //	todaymessagelist.clear();
                Log.e("","not4");
                // soap response
                for (int i = 0; i < resultsRequestSOAP.getPropertyCount(); i++) {
                    Log.e("","not5");

                    SoapObject getmessaage = (SoapObject) resultsRequestSOAP
                            .getProperty(i);
                    HashMap<String, String> map = new HashMap<String, String>();

                    // display messages by adding it to listview
                    if (!String.valueOf(getmessaage.getProperty("CreatedDate"))
                            .equals("false")) {

                        if(String.valueOf(getmessaage.getProperty("Message")).equals("anyType{}")){

                            map.put("Message","");
                        }else{


                            map.put("Message", String.valueOf(getmessaage.getProperty("Message")));
                        }

                        map.put("Receiver", String.valueOf(getmessaage.getProperty("Sender")));

                        map.put("Date",String.valueOf(getmessaage.getProperty("CreatedDate")));

                        todaymessagelist.add(map);
                    }
                }
            }else{

                Toast.makeText(NotificationFragment.this, "Connectivity Error!!", Toast.LENGTH_SHORT).show();

            }

            Log.e("", "todaymessagelist="+todaymessagelist.toString());
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }*/

   /* private void loadmessage() {
        adapter = new NotificationAdapter(NotificationFragment.this, todaymessagelist);
        setListAdapter(adapter);// add custom adapter to
        // listview
    }*/


}
