package com.prod.sudesi.lotusherbalsnew.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.prod.sudesi.lotusherbalsnew.Models.ProductListModel;
import com.prod.sudesi.lotusherbalsnew.Models.ProductModel;
import com.prod.sudesi.lotusherbalsnew.R;
import com.prod.sudesi.lotusherbalsnew.Utils.SharedPref;
import com.prod.sudesi.lotusherbalsnew.Utils.Utils;
import com.prod.sudesi.lotusherbalsnew.libs.ConnectionDetector;

import java.util.ArrayList;

/**
 * Created by Admin on 02-11-2017.
 */

public class SaleDetailsActivity extends Activity implements View.OnClickListener {

    Context context;
    Button btn_save, btn_back, btn_home, btn_logout;
    private SharedPref sharedPref;

    EditText edt_gross,edt_discount,edt_netamt;

    String outletcode, username;

    TextView tv_h_username;

    TableLayout tablel_sale_calculation;

    ConnectionDetector cd;
    Utils utils;

    ArrayList<ProductModel> productDetailsArraylist;
    ArrayList<ProductModel> newproductDetailsArraylist;
    ProductModel productModel;
    ProductListModel productListModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sale_details);

        context = SaleDetailsActivity.this;

        sharedPref = new SharedPref(context);

        utils = new Utils(context);

        cd = new ConnectionDetector(context);

        tablel_sale_calculation = (TableLayout) findViewById(R.id.tablel_sale_calculation);

        edt_gross = (EditText) findViewById(R.id.edt_gross);
        edt_discount = (EditText) findViewById(R.id.edt_discount);
        edt_netamt = (EditText) findViewById(R.id.edt_netamt);

        btn_save = (Button) findViewById(R.id.btn_save_sale);
        btn_back = (Button) findViewById(R.id.btn_back_sale);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        try {
            LOTUS.dbCon.open();
            outletcode = LOTUS.dbCon.getActiveoutletCode();
            LOTUS.dbCon.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        // ---------------------
        tv_h_username = (TextView) findViewById(R.id.tv_h_username);

        username = sharedPref.getLoginId();
        tv_h_username.setText(username);

        btn_save.setOnClickListener(this);
        btn_back.setOnClickListener(this);
        btn_home.setOnClickListener(this);
        btn_logout.setOnClickListener(this);



        productModel = new ProductModel();
        productListModel = new ProductListModel();
        productDetailsArraylist = new ArrayList<>();
        newproductDetailsArraylist = new ArrayList<>();

        if (getIntent() != null) {
            try {

                productListModel = (ProductListModel) getIntent().getSerializableExtra("Salelist");
                newproductDetailsArraylist = productListModel.getProductListModels();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (newproductDetailsArraylist.size() > 0) {
            tablel_sale_calculation.removeAllViews();
            for (int i = 0; i < newproductDetailsArraylist.size(); i++) {
                View tr = (TableRow) View.inflate(SaleDetailsActivity.this, R.layout.inflate_sale_row, null);

                TextView product = (TextView) tr.findViewById(R.id.txtproductsale);
                TextView amount = (TextView) tr.findViewById(R.id.txtmrpsale);
                EditText quantity = (EditText) tr.findViewById(R.id.edtquantitysale);


                productModel = newproductDetailsArraylist.get(i);

                String a_id = productModel.getA_Id();

                product.setText(productModel.getProductName());
                amount.setText(productModel.getPTT());

                tablel_sale_calculation.addView(tr);

            }
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_home:
                v.startAnimation(AnimationUtils.loadAnimation(SaleDetailsActivity.this, R.anim.button_click));
                Intent i = new Intent(getApplicationContext(),
                        DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                break;

            case R.id.btn_logout:
                v.startAnimation(AnimationUtils.loadAnimation(SaleDetailsActivity.this, R.anim.button_click));
                Intent i1 = new Intent(getApplicationContext(), LoginActivity.class);
                i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i1);

                break;

            case R.id.btn_save:

                try {
                    int etcount = 0;
                    int count = 0;
                    if (tablel_sale_calculation.getChildCount() >0) {
                        for (int j = 0; j < tablel_sale_calculation.getChildCount(); j++) {

                            TableRow t = (TableRow) tablel_sale_calculation
                                    .getChildAt(j);
                            EditText edt_qty = (EditText) t.getChildAt(1);

                            if (edt_qty.getText().toString().trim()
                                    .equalsIgnoreCase("0")
                                    || edt_qty.getText().toString().trim()
                                    .equalsIgnoreCase("")
                                    || edt_qty.getText().toString().trim()
                                    .equalsIgnoreCase(" ")) {

                                cd.displayMessage("Please Enter in All Fields");

                            } else {
                                etcount++;

                            }
                            Log.e("etcount", String.valueOf(etcount));
                        }

                    }

                    int numberofproduct = (tablel_sale_calculation.getChildCount());

                    if (numberofproduct == etcount) {

                        if (!edt_gross.getText().toString().equals("")
                                && !edt_netamt.getText().toString().equals("")) {

                        }
                        /*if (tablel_sale_calculation.getChildCount() >0) {

                            //count = saveData();

                        }
                        showAlertDialog(count);*/

                    }else {

                        cd.displayMessage("Please enter valid value in quantity fields");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.btn_back:
                v.startAnimation(AnimationUtils.loadAnimation(SaleDetailsActivity.this, R.anim.button_click));
                finish();
                startActivity(new Intent(SaleDetailsActivity.this,
                        StockActivity.class));
                break;

            default:
                break;
        }
    }

    public void showAlertDialog(int count) {
        if (count == tablel_sale_calculation.getChildCount()) {

            // mProgress.dismiss();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    SaleDetailsActivity.this);

            // set title
            alertDialogBuilder.setTitle("Saved Successfully!!");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Go  TO  :")
                    .setCancelable(false)

                    .setNegativeButton(
                            "Stock Page",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int id) {

                                    dialog.cancel();
//*******************
                                    finish();
                                    startActivity(new Intent(
                                            SaleDetailsActivity.this,
                                            StockActivity.class));
                                }
                            })

                    .setPositiveButton(
                            "Home",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int id) {

                                    dialog.cancel();

                                    finish();
                                    startActivity(new Intent(
                                            SaleDetailsActivity.this,
                                            DashBoardActivity.class));

                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder
                    .create();

            // show it
            alertDialog.show();
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    SaleDetailsActivity.this);

            // set title
            alertDialogBuilder.setTitle("Data Not Saved!!!");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Problem with Insert")
                    .setCancelable(false)

                    .setNegativeButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int id) {

                                    dialog.cancel();

                                    finish();
                                    startActivity(new Intent(
                                            SaleDetailsActivity.this,
                                            StockActivity.class));

                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder
                    .create();

            // show it
            alertDialog.show();
        }
    }
}
