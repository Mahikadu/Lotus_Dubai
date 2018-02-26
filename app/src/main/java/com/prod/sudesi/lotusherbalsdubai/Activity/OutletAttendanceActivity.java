package com.prod.sudesi.lotusherbalsdubai.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.prod.sudesi.lotusherbalsdubai.Models.OutletModel;
import com.prod.sudesi.lotusherbalsdubai.R;
import com.prod.sudesi.lotusherbalsdubai.Utils.SharedPref;
import com.prod.sudesi.lotusherbalsdubai.Utils.Utils;
import com.prod.sudesi.lotusherbalsdubai.Dbconfig.DataBaseCon;
import com.prod.sudesi.lotusherbalsdubai.Dbconfig.DbHelper;
import com.prod.sudesi.lotusherbalsdubai.libs.ConnectionDetector;
import com.prod.sudesi.lotusherbalsdubai.libs.LotusWebservice;

import org.ksoap2.serialization.SoapPrimitive;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.nlopez.smartlocation.OnActivityUpdatedListener;
import io.nlopez.smartlocation.OnGeofencingTransitionListener;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.geofencing.model.GeofenceModel;
import io.nlopez.smartlocation.geofencing.utils.TransitionGeofence;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;

/**
 * Created by Admin on 24-10-2017.
 */

public class OutletAttendanceActivity extends Activity implements View.OnClickListener, OnLocationUpdatedListener, OnActivityUpdatedListener, OnGeofencingTransitionListener {

    TextView txt_currentDate, txt_BDE;
    TimePicker time_picker;
    Resources system;
    String BDEusername, username, BDE_CODE, Adate = "", Actual_date = "", outletCode = "";
    private SharedPref sharedPref;
    Button btn_submit, btn_home, btn_logout;
    private double lon = 0.0, lat = 0.0;
    LotusWebservice service;
    // AttendanceAsync upload_attendance;
    TextView tv_h_username;
    String outletName = "";
    Context context;
    AutoCompleteTextView spin_outletname;

    private ArrayList<OutletModel> outletDetailsArraylist;
    OutletModel outletModel;
    String[] strOutletArray = null;
    String outletstring;

    Utils utils;
    ConnectionDetector cd;

    SaveOutletAttendance saveOutletAttendance;

    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static GoogleApiClient mGoogleApiClient;
    private static final int ACCESS_FINE_LOCATION_INTENT_ID = 3;
    private static final int LOCATION_PERMISSION_ID = 1001;
    private LocationGooglePlayServicesProvider provider;
    private static final String BROADCAST_ACTION = "android.location.PROVIDERS_CHANGED";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_outlet_attedence);

        context = OutletAttendanceActivity.this;

        utils = new Utils(context);

        cd = new ConnectionDetector(context);

        sharedPref = new SharedPref(context);
        LOTUS.dbCon = DataBaseCon.getInstance(getApplicationContext());

        LOTUS.dbCon.open();

        service = new LotusWebservice(OutletAttendanceActivity.this);
        BDEusername = sharedPref.getbaName();
        //BDE_CODE = sp.getString("BDE_Code", "");
        username = sharedPref.getLoginId();

        txt_currentDate = (TextView) findViewById(R.id.txt_currentDate);
        tv_h_username = (TextView) findViewById(R.id.tv_h_username);
        txt_BDE = (TextView) findViewById(R.id.txt_BDE);
        time_picker = (TimePicker) findViewById(R.id.timePicker1);
        time_picker.setIs24HourView(true);
        set_timepicker_text_colour();
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);

        spin_outletname = (AutoCompleteTextView) findViewById(R.id.spin_outletname);

        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        btn_home.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

        Log.v("", "username==" + username);
        tv_h_username.setText(username);

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String date = sdf.format(cal.getTime());
        txt_currentDate.setText(date);

        txt_BDE.setText(BDEusername);


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
            spin_outletname.setAdapter(adapter1);
        }

        spin_outletname.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(strOutletArray == null){
                    cd.displayMessage("Please MasterSync First!!");
                }
                spin_outletname.showDropDown();
                return false;
            }
        });

        spin_outletname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (strOutletArray != null && strOutletArray.length > 0) {

                    outletstring = parent.getItemAtPosition(position).toString();
                    for (int i = 0; i < outletDetailsArraylist.size(); i++) {
                        String text = outletDetailsArraylist.get(i).getOutletName();
                        String outletcode = outletDetailsArraylist.get(i).getOutletCode();
                        if (text.equalsIgnoreCase(outletstring)) {
                            outletName = text;
                            outletCode = outletcode;
                        }
                    }

                }
            }
        });

        initGoogleAPIClient();//Init Google API Client
        checkPermissions();//Check Permission
        if (ContextCompat.checkSelfPermission(OutletAttendanceActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(OutletAttendanceActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
            return;
        }

        startLocation();

    }

    private void set_timepicker_text_colour() {
        system = Resources.getSystem();
        int hour_numberpicker_id = system
                .getIdentifier("hour", "id", "android");
        int minute_numberpicker_id = system.getIdentifier("minute", "id",
                "android");

        NumberPicker hour_numberpicker = (NumberPicker) time_picker
                .findViewById(hour_numberpicker_id);
        NumberPicker minute_numberpicker = (NumberPicker) time_picker
                .findViewById(minute_numberpicker_id);

        set_numberpicker_text_colour(hour_numberpicker);
        set_numberpicker_text_colour(minute_numberpicker);
    }

    private void set_numberpicker_text_colour(NumberPicker number_picker) {
        final int count = number_picker.getChildCount();
        final int color = Color.parseColor("#ffffff");

        for (int i = 0; i < count; i++) {
            View child = number_picker.getChildAt(i);

            try {
                Field wheelpaint_field = number_picker.getClass()
                        .getDeclaredField("mSelectorWheelPaint");
                wheelpaint_field.setAccessible(true);

                ((Paint) wheelpaint_field.get(number_picker)).setColor(color);
                ((EditText) child).setTextColor(color);
                number_picker.invalidate();
            } catch (NoSuchFieldException e) {
                Log.w("NumberPickerTxtColor", e);
            } catch (IllegalAccessException e) {
                Log.w("NumberPickerTxtColor", e);
            } catch (IllegalArgumentException e) {
                Log.w("NumberPickerTxtColor", e);
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btn_home:
                v.startAnimation(AnimationUtils.loadAnimation(OutletAttendanceActivity.this, R.anim.button_click));
                Intent i = new Intent(getApplicationContext(),
                        DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                break;

            case R.id.btn_logout:
                v.startAnimation(AnimationUtils.loadAnimation(OutletAttendanceActivity.this, R.anim.button_click));
                Intent i1 = new Intent(getApplicationContext(), LoginActivity.class);
                i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i1);

                break;

            case R.id.btn_submit:
                v.startAnimation(AnimationUtils.loadAnimation(OutletAttendanceActivity.this, R.anim.button_click));
                int id = 0;
                try {
                    int hr = time_picker.getCurrentHour();
                    int mins = time_picker.getCurrentMinute();
                    String d1;

                    String d[] = txt_currentDate.getText().toString().split("-");

                    d1 = d[1] + "-" + d[0] + "-" + d[2];

                    Adate = d1 + " " + String.valueOf(hr) + ":"
                            + String.valueOf(mins) + ":" + "00";
                    // }

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdft = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
                    Actual_date = sdft.format(c.getTime());

                    if (outletstring != null && outletstring.length() > 0) {
                        try {

                            String valuesArray[] = {username, Adate, Actual_date, String.valueOf(lat), String.valueOf(lon), outletCode, outletName, "Active", "0"};
                            long rowid = LOTUS.dbCon.insert(DbHelper.TABLE_OUTLET_ATTENDANCE, valuesArray, utils.columnNamesOutletAttendance);

                       if(rowid > 0) {
                           LOTUS.dbCon.update(DbHelper.TABLE_OUTLET_ATTENDANCE, "id != ?", new String[]{"DeActive"},
                                   new String[]{"outletstatus"}, new String[]{String.valueOf(rowid)});

                       }
                            if (rowid > -1) {
                                saveOutletAttendance = new SaveOutletAttendance();
                                saveOutletAttendance.execute();

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        cd.displayMessage("Please select outlet!!");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

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


    private class SaveOutletAttendance extends AsyncTask<Void, Void, SoapPrimitive> {

        SoapPrimitive soap_result;
        ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress = new ProgressDialog(OutletAttendanceActivity.this);
            progress.setTitle("Status");
            progress.setMessage("Uploading....Please wait...");
            progress.show();
            progress.setCancelable(false);
        }

        @Override
        protected SoapPrimitive doInBackground(Void... params) {
            // TODO Auto-generated method stub

            soap_result = service.SaveOutletAttendance(username, Adate, String.valueOf(lat),
                    String.valueOf(lon), outletCode);

            return soap_result;
        }


        @Override
        protected void onPostExecute(SoapPrimitive result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progress.dismiss();
            int id = 0;
            if (result != null) {
                if (result.toString().equalsIgnoreCase("Success")) {
                    cd.displayMessage("Data Uploaded Successfully!!");
                    startActivity(new Intent(OutletAttendanceActivity.this,
                            DashBoardActivity.class));

                    LOTUS.dbCon.update(DbHelper.TABLE_OUTLET_ATTENDANCE, "outletcode = ?", new String[]{"1"},
                            new String[]{"savedServer"}, new String[]{outletCode});


                } else {
                    cd.displayMessage("Data Not uploaded");
                }
            } else {
                final Calendar calendar = Calendar
                        .getInstance();
                SimpleDateFormat formatter = new SimpleDateFormat(
                        "MM/dd/yyyy HH:mm:ss");
                String Createddate = formatter.format(calendar
                        .getTime());

                int n = Thread.currentThread().getStackTrace()[2]
                        .getLineNumber();

                LOTUS.dbCon.open();

                id = LOTUS.dbCon.getCountOfRows(DbHelper.SYNC_LOG) + 1;
                String selection = "ID = ?";
                String ID = String.valueOf(id);
                // WHERE clause arguments
                String[] selectionArgs = {ID};

                String valuesArray[] = {ID, "Soup is Null While SaveOutletAttendance()", String.valueOf(n), "SaveOutletAttendance()",
                        Createddate, Createddate, sharedPref.getLoginId(), "SaveOutletAttendance", "Fail"};
                boolean output = LOTUS.dbCon.updateBulk(DbHelper.SYNC_LOG, selection, valuesArray, utils.columnNamesSyncLog, selectionArgs);

                LOTUS.dbCon.close();

                cd.displayMessage("Connectivity Error, Please check Internet connection!!");
            }
        }

    }


    // Getting lat and lon value using location liabarary and showing gps dialog

    private void showLocation(final Location location) {
        if (location != null) {
            lat = location.getLatitude();
            lon = location.getLongitude();
            //  Toast.makeText(mContext, "latitude:"+lat+"longitude"+lang, Toast.LENGTH_SHORT).show();
            SmartLocation.with(OutletAttendanceActivity.this).location().stop();

        }
    }

    /* Initiate Google API Client  */
    private void initGoogleAPIClient() {
        //Without Google API Client Auto Location Dialog will not work
        mGoogleApiClient = new GoogleApiClient.Builder(OutletAttendanceActivity.this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();

    }

    /* Check Location Permission for Marshmallow Devices */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(OutletAttendanceActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)
                requestLocationPermission();
            else
                showSettingDialogbox();
        } else
            showSettingDialogbox();

    }

    /*  Show Popup to access User Permission  */
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(OutletAttendanceActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(OutletAttendanceActivity.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);

        } else {
            ActivityCompat.requestPermissions(OutletAttendanceActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_FINE_LOCATION_INTENT_ID);
        }
    }

    /* Show Location Access Dialog */
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
                            status.startResolutionForResult(OutletAttendanceActivity.this, REQUEST_CHECK_SETTINGS);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.e("Settings", "Result OK");
                        updateGPSStatus("GPS is Enabled in your device");
                        //startLocationUpdates();
                        startLocation();


                        break;
                    case RESULT_CANCELED:
                        Log.e("Settings", "Result Cancel");
                        updateGPSStatus("GPS is Disabled in your device");
                        break;
                }
                break;
        }
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

    /* Broadcast receiver to check status of GPS */
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

    /* On Request permission method to check the permisison is granted or not for Marshmallow+ Devices  */
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
                    Toast.makeText(OutletAttendanceActivity.this, "Location Permission denied.", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }


}
