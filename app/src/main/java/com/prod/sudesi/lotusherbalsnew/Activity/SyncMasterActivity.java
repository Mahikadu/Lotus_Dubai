package com.prod.sudesi.lotusherbalsnew.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.prod.sudesi.lotusherbalsnew.R;
import com.prod.sudesi.lotusherbalsnew.dbConfig.DataBaseCon;
import com.prod.sudesi.lotusherbalsnew.libs.ConnectionDetector;
import com.prod.sudesi.lotusherbalsnew.libs.LotusWebservice;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Admin on 16-10-2017.
 */

public class SyncMasterActivity extends Activity implements View.OnClickListener{

    Context context;
    Button master_sync, data_upload, data_download, btn_usermanual,btn_changePass;

    private DataBaseCon db;
    private ProgressDialog mProgress = null;

    LotusWebservice service;

    // shredpreference
    private SharedPreferences sharedpre = null;

    private SharedPreferences.Editor saveuser = null;

    SharedPreferences sp;
    SharedPreferences.Editor spe;
    //
    ConnectionDetector cd;
    TextView tv_h_username;
    Button btn_home, btn_logout;

    String outletcode,username,producttype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_syncmaster);

        context = getApplicationContext();


        //Button referances
        data_upload = (Button) findViewById(R.id.btn_data_upload);
        master_sync = (Button) findViewById(R.id.btn_master_sync);
        btn_changePass = (Button) findViewById(R.id.btn_changePass);
        data_download = (Button) findViewById(R.id.btn_data_download);
        btn_usermanual = (Button) findViewById(R.id.btn_usermanual);

        cd = new ConnectionDetector(context);
        db = new DataBaseCon(context);
        mProgress = new ProgressDialog(SyncMasterActivity.this);
        service = new LotusWebservice(SyncMasterActivity.this);

        sharedpre = context
                .getSharedPreferences("Sudesi", context.MODE_PRIVATE);
        saveuser = sharedpre.edit();

        sp = context.getSharedPreferences("Lotus", context.MODE_PRIVATE);
        spe = sp.edit();

        db.open();
       // outletcode = db.getActiveoutletCode();
        db.close();

        producttype = sp.getString("producttype", "");
        Log.e("", "producttype==" + producttype);

        username = sp.getString("username", "");
        Log.e("", "username==" + username);

        tv_h_username = (TextView) findViewById(R.id.tv_h_username);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        tv_h_username.setText(username);


        //Set Onclick listner
        data_upload.setOnClickListener(this);
        master_sync.setOnClickListener(this);
        btn_changePass.setOnClickListener(this);
        data_download.setOnClickListener(this);
        btn_usermanual.setOnClickListener(this);
        btn_home.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_data_upload:
                v.startAnimation(AnimationUtils.loadAnimation(SyncMasterActivity.this, R.anim.button_click));
               // new syncAllData().execute();
                break;
            case R.id.btn_master_sync:
                v.startAnimation(AnimationUtils.loadAnimation(SyncMasterActivity.this, R.anim.button_click));
               // new InsertProductMaster().execute();
                break;
            case R.id.btn_changePass:
                v.startAnimation(AnimationUtils.loadAnimation(SyncMasterActivity.this, R.anim.button_click));
                startActivity(new Intent(SyncMasterActivity.this, ChangePasswordActivity.class));
                break;
            case R.id.btn_data_download:
                v.startAnimation(AnimationUtils.loadAnimation(SyncMasterActivity.this, R.anim.button_click));
               // new InsertFirstTimeMaster().execute();
                break;
            case R.id.btn_usermanual:
                v.startAnimation(AnimationUtils.loadAnimation(SyncMasterActivity.this, R.anim.button_click));
                try {
                    File pdfFile = new File(Environment
                            .getExternalStorageDirectory(), "/sample.pdf");

                    readusermanual();

                } catch (Exception e) {

                }
                break;
            case R.id.btn_home:
                v.startAnimation(AnimationUtils.loadAnimation(SyncMasterActivity.this, R.anim.button_click));
                startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                break;
            case R.id.btn_logout:
                v.startAnimation(AnimationUtils.loadAnimation(SyncMasterActivity.this, R.anim.button_click));
                Intent logout = new Intent(getApplicationContext(),
                        LoginActivity.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logout);
                break;
        }
    }

    public void readusermanual() {

        Log.v("", "u1");
        AssetManager assetManager = getAssets();

        InputStream in = null;
        OutputStream out = null;
        File file = new File(getFilesDir(), "sample.pdf");
        Log.v("", "u1");
        try {
            in = assetManager.open("sample.pdf");
            out = openFileOutput(file.getName(), Context.MODE_WORLD_READABLE);
            Log.v("", "u1");
            copyFile(in, out);
            Log.v("", "u1");
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));

            e.printStackTrace();

        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(
                    Uri.parse("file://" + getFilesDir() + "/sample.pdf"),
                    "application/pdf");

            startActivity(intent);

        } catch (Exception e) {

            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));

            e.printStackTrace();

        }
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }
}
