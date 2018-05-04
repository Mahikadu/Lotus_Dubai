package com.prod.sudesi.lotusherbalsdubai.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.prod.sudesi.lotusherbalsdubai.R;
import com.prod.sudesi.lotusherbalsdubai.Utils.SharedPref;
import com.prod.sudesi.lotusherbalsdubai.adapter.ListAdapter;
import com.prod.sudesi.lotusherbalsdubai.libs.ConnectionDetector;
import com.prod.sudesi.lotusherbalsdubai.libs.LotusWebservice;
import com.prod.sudesi.lotusherbalsdubai.sudesi.image.ImageModel;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Mahesh on 1/12/2018.
 */

public class VisibilityActivity extends ListActivity implements View.OnClickListener {

    Context context;
    private Uri outputFileUri;

    private String pathCamera = "";

    // shredpreference
    private SharedPreferences sharedpre = null;

    private SharedPreferences.Editor saveuser = null;

    private SharedPref sharedPref;

    ConnectionDetector cd;
    private File root = null;

    private String fname = "";

    private String scannedId;

    private TextView welcome;

    private ListView listView;

    private ArrayAdapter<ImageModel> adapter;

    private Button btn_back, btn_next, preview_button;

    Button back, home;

    String producttype;
    String username, imgpth;

    Cursor image_array, image_array1;
    LotusWebservice service;

    private EditText image_description;

    private ProgressDialog mProgress = null;

    int soapresultforvisibilityid;

    private double lon = 0.0, lat = 0.0;

    Fragment content;

    RadioGroup rg_image_category;
    RadioButton r_lh, r_lhm;

    List<ImageModel> list;

    TextView tv_h_username;
    Button btn_home,btn_logout;

   /* private static final int LOCATION_PERMISSION_ID = 1001;
    private static GoogleApiClient mGoogleApiClient;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private LocationGooglePlayServicesProvider provider;
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";*/


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        // TODO Auto-generated method stub
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_visibility);

        context = VisibilityActivity.this;

        cd = new ConnectionDetector(context);
        sharedPref = new SharedPref(context);

        sharedpre = context.getSharedPreferences("Sudesi",context.MODE_PRIVATE);
        saveuser = sharedpre.edit();

        mProgress = new ProgressDialog(this);
        service = new LotusWebservice(this);

        producttype = sharedpre.getString("producttype","");
        Log.e("", "producttype=="+producttype);

        btn_back = (Button) findViewById(R.id.button_back);
        btn_next = (Button) findViewById(R.id.buttonNext);
        preview_button = (Button) findViewById(R.id.preview_button);
        image_description = (EditText) findViewById(R.id.et_image_description);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        tv_h_username = (TextView) findViewById(R.id.tv_h_username);
        Log.v("","in visisisi");

        listView = (ListView) findViewById(android.R.id.list);

        username = sharedPref.getLoginId();
        Log.v("", "username==" + username);

        tv_h_username.setText(username);

        btn_back.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        preview_button.setOnClickListener(this);
        btn_home.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

       /* initGoogleAPIClient();//Init Google API Client
        checkPermissions();//Check Permission
        if (ContextCompat.checkSelfPermission(VisibilityActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(VisibilityActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
            return;
        }

        startLocation();*/

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_home:

                Intent i = new Intent(getApplicationContext(), DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                break;

            case R.id.btn_logout:

                Intent i1 = new Intent(getApplicationContext(), LoginActivity.class);
                i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i1);

                break;

            case R.id.button_back:

                Intent back = new  Intent(getApplicationContext(), DashBoardActivity.class);
                back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(back);

                break;

            case R.id.buttonNext:

                try {
                    // = sharedpre.getInt("iCount", 0);

                    int count1 =  listView.getAdapter().getCount() ;

                    Log.e("", "count1==" + count1);
                    if (count1 == 0) {

                        cd.displayMessage("Please Capture images");

                    } else {

                        String desc = image_description.getText().toString().trim();
                        LOTUS.dbCon.open();

                       /* LOTUS.dbCon.Scanimage(String.valueOf(lon), String.valueOf(lat),
                                desc, username, producttype, String.valueOf(count1), getModel());*/

                        LOTUS.dbCon.close();
                        cd.displayMessage("Successfully images saved");

                        Intent ii = new  Intent(getApplicationContext(), DashBoardActivity.class);
                        ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(ii);

                        //startActivity(new Intent(context,
                        //DashboardNewActivity.class));

                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }


                break;

            case R.id.preview_button:
                openImageIntent();
                break;
        }
    }

    @SuppressLint("NewApi")
    private void openImageIntent() {

        try {

            fname = username+"_"+System.currentTimeMillis() + ".jpeg";

            FileOutputStream fos = context.openFileOutput(fname,
                    Context.MODE_WORLD_WRITEABLE);
            fos.close();

            root = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "sudesi" + File.separator);

            root.mkdirs();

            root.setWritable(true);

            File f = new File(root, fname);

            outputFileUri = Uri.fromFile(f);

            pathCamera = Environment.getExternalStorageDirectory() + "/sudesi/"
                    + fname;

            final Intent captureIntent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);

            startActivityForResult(captureIntent, 202);
        } catch (Exception e) {

            Toast.makeText(context,
                    "Something Wrong with The Camera. Try Again!!!",
                    Toast.LENGTH_LONG).show();
        }
    }


   /* // Getting lat and lon value using location liabarary and showing gps dialog

    private void showLocation(final Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            //  Toast.makeText(mContext, "latitude:"+lat+"longitude"+lang, Toast.LENGTH_SHORT).show();
            SmartLocation.with(VisibilityActivity.this).location().stop();

        }
    }

    *//* Initiate Google API Client  *//*
    private void initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient = new GoogleApiClient.Builder(VisibilityActivity.this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    *//* Check Location Permission for Marshmallow Devices *//*
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(VisibilityActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission();
            else
                showSettingDialogbox();
        } else
            showSettingDialogbox();

    }

    *//*  Show Popup to access User Permission  *//*
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(VisibilityActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(VisibilityActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);

        } else {
            ActivityCompat.requestPermissions(VisibilityActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);
        }
    }

    *//* Show Location Access Dialog *//*
    private void showSettingDialogbox() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);//Setting priotity of Location request to high
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);//5 sec Time interval for location update
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient to show dialog always when GPS is off

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        updateGPSStatus("GPS is Enabled in your device");
                        // startLocation();


                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(VisibilityActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }

    private void startLocation() {

        provider = new LocationGooglePlayServicesProvider();
        provider.setCheckLocationSettings(true);

        SmartLocation smartLocation = new SmartLocation.Builder(this).logging(true).build();

        smartLocation.location(provider).start(this);
        smartLocation.activity().start(this);

        // Create some geofences
        GeofenceModel mestalla = new GeofenceModel.Builder("1").setTransition(Geofence.GEOFENCE_TRANSITION_ENTER).setLatitude(39.47453120000001).setLongitude(-0.358065799999963).setRadius(500).build();
        smartLocation.geofencing().add(mestalla).start(this);
    }

    //Method to update GPS status text
    private void updateGPSStatus(String status) {
        //   gps_status.setText(status);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.e("Settings", "Result OK");
                        updateGPSStatus("GPS is Enabled in your device");
                        //startLocationUpdates();
                        startLocation();
                        try{
                            if (requestCode == 202) {

                                String selectedImageUri = pathCamera;

                                saveuser = sharedpre.edit();

                                int count = sharedpre.getInt("iCount", 0);

                                count++;

                                saveuser.putString("ImgPath_" + String.valueOf(count),
                                        selectedImageUri.toString());

                                saveuser.putInt("iCount", count);

                                saveuser.commit();

                                adapter = null;

                                adapter = new ListAdapter(VisibilityActivity.this, getModel());
                                setListAdapter(adapter);

                            }
                        }catch (Exception e){
                            e.printStackTrace();

                            cd.displayMessage("Something Wrong with The Camera. Try Again!!! ");
                        }



                        break;
                    case RESULT_CANCELED:
                        Log.e("Settings", "Result Cancel");
                        updateGPSStatus("GPS is Disabled in your device");
                        break;
                }
                break;
        }

       *//* super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == RESULT_OK) {

                if (requestCode == 202) {

                    String selectedImageUri = pathCamera;

                    saveuser = sharedpre.edit();

                    int count = sharedpre.getInt("iCount", 0);

                    count++;

                    saveuser.putString("ImgPath_" + String.valueOf(count),
                            selectedImageUri.toString());

                    saveuser.putInt("iCount", count);

                    saveuser.commit();

                    adapter = null;

                    adapter = new ListAdapter(VisibilityActivity.this, getModel());
                    setListAdapter(adapter);

                }
            }
        } catch (Exception e) {

            e.printStackTrace();

            Toast.makeText(VisibilityActivity.this,
                    "Something Wrong with The Camera. Try Again!!! ",
                    Toast.LENGTH_LONG).show();

        }*//*
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(gpsLocationReceiver, new IntentFilter(BROADCAST_ACTION));//Register broadcast receiver to check the status of GPS
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Unregister receiver on destroy
        if (gpsLocationReceiver != null)
            unregisterReceiver(gpsLocationReceiver);
    }

    //Run on UI
    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            //   showSettingDialogbox();
        }
    };

    *//* Broadcast receiver to check status of GPS *//*
    private BroadcastReceiver gpsLocationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            //If Action is Location
            if (intent.getAction().matches(BROADCAST_ACTION)) {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                //Check if GPS is turned ON or OFF
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.e("About GPS", "GPS is Enabled in your device");
                    updateGPSStatus("GPS is Enabled in your device");
                    //  startLocation();

                } else {
                    //If GPS turned OFF show Location Dialog
                    new Handler().postDelayed(sendUpdatesToUI, 10);
                    updateGPSStatus("GPS is Disabled in your device");
                    Log.e("About GPS", "GPS is Disabled in your device");
                }

            }
        }
    };

    *//* On Request permission method to check the permisison is granted or not for Marshmallow+ Devices  *//*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case ACCESS_FINE_LOCATION_INTENT_ID: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //If permission granted show location dialog if APIClient is not null
                    if (mGoogleApiClient == null) {
                        initGoogleAPIClient();
                    }

                } else {
                    updateGPSStatus("Location Permission denied.");
                    cd.displayMessage("Location Permission denied.");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private List<ImageModel> getModel() {

        list = new ArrayList<ImageModel>();

        try {

            int count = sharedpre.getInt("iCount", 0);

            File fTemp = null;

            String pathLocal = "";

            int localCount = 0;

            for (int j = count; j > 0; j--) {
                pathLocal = sharedpre.getString("ImgPath_" + String.valueOf(j),"");

                if (!pathLocal.trim().equalsIgnoreCase("")) {
                    fTemp = new File(pathLocal);

                    if (fTemp.exists()) {
                        list.add(get(pathLocal, j));

                        localCount++;
                    }
                }

            }

            if (localCount == 0) {
                // list.add(get("No Images",0));
            }

        } catch (Exception e) {
            list = null;

        }

        return list;
    }

    private ImageModel get(String s, int i) {
        return new ImageModel(s, i);
    }


    @Override
    public void onActivityUpdated(DetectedActivity detectedActivity) {

    }

    @Override
    public void onGeofenceTransition(TransitionGeofence transitionGeofence) {

    }

    @Override
    public void onLocationUpdated(Location location) {
        showLocation(location);
    }
*/

}
