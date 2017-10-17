package com.prod.sudesi.lotusherbalsnew.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.prod.sudesi.lotusherbalsnew.R;
import com.prod.sudesi.lotusherbalsnew.libs.LotusWebservice;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 17-10-2017.
 */

public class NotificationActivity extends Activity {

    Context context;
    SharedPreferences sp;
    SharedPreferences.Editor spe;

    static public ArrayList<HashMap<String, String>> todaymessagelist = new ArrayList<HashMap<String, String>>();

    private ListView listView;

    //private NotificationAdapter adapter;

    String EmpID;

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

        listView = (ListView) findViewById(android.R.id.list);

        sp = context.getSharedPreferences("Lotus", context.MODE_PRIVATE);
        spe = sp.edit();

        mProgress = new ProgressDialog(NotificationActivity.this);
        service = new LotusWebservice(NotificationActivity.this);


        EmpID = sp.getString("username", "");
        Log.e("", "username==" + EmpID);

        //------------------

        tv_h_username = (TextView) findViewById(R.id.tv_h_username);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        username = sp.getString("username", "");
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

    }
}
