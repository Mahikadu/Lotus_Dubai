package com.prod.sudesi.lotusherbalsdubai.libs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.prod.sudesi.lotusherbalsdubai.Activity.BAYearWiseReport;
import com.prod.sudesi.lotusherbalsdubai.Activity.DashBoardActivity;

public class ConnectionDetector {
 
    private Context _context;
 
    public ConnectionDetector(Context context){
        this._context = context;
    }
 
    /**
     * Checking for all possible internet providers
     * **/
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null)
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null)
                  for (int i = 0; i < info.length; i++)
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
 
          }
          return false;
    }

    /*  method to display toast message*/
    public void displayMessage(String message) {
        Toast.makeText(_context, message, Toast.LENGTH_SHORT).show();
    }

    public void DisplayDialogMessage(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        Intent intent = new Intent(_context, DashBoardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        _context.startActivity(intent);
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
