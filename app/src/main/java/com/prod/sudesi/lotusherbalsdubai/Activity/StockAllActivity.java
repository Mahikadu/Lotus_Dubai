package com.prod.sudesi.lotusherbalsdubai.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.prod.sudesi.lotusherbalsdubai.Models.ProductListModel;
import com.prod.sudesi.lotusherbalsdubai.Models.ProductModel;
import com.prod.sudesi.lotusherbalsdubai.R;
import com.prod.sudesi.lotusherbalsdubai.Utils.SharedPref;
import com.prod.sudesi.lotusherbalsdubai.Utils.Utils;
import com.prod.sudesi.lotusherbalsdubai.Dbconfig.DbHelper;
import com.prod.sudesi.lotusherbalsdubai.libs.ConnectionDetector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Admin on 30-10-2017.
 */

public class StockAllActivity extends Activity implements View.OnClickListener {

    Context context;
    Button btn_save, btn_back, btn_home, btn_logout;
    private SharedPref sharedPref;

    String outletcode, username;

    TextView tv_h_username;

    TableLayout tl_sale_calculation;

    ArrayList<ProductModel> productDetailsArraylist;
    ArrayList<ProductModel> newproductDetailsArraylist;
    ProductModel productModel;
    ProductListModel productListModel;

    ConnectionDetector cd;
    Utils utils;

    String str_openingstock = "0", soldstock = "0",closebal = "0",old_stock_recive = "0",str_stockinhand = "0";
    Cursor mCursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_stock_all);

        context = StockAllActivity.this;

        sharedPref = new SharedPref(context);

        utils = new Utils(context);

        cd = new ConnectionDetector(context);

        tl_sale_calculation = (TableLayout) findViewById(R.id.tl_sale_calculation);

        btn_save = (Button) findViewById(R.id.btn_save);
        btn_back = (Button) findViewById(R.id.btn_back);
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


        if (getIntent() != null) {
            try {
                tl_sale_calculation.removeAllViews();
                newproductDetailsArraylist = new ArrayList<>();
                productListModel = (ProductListModel) getIntent().getSerializableExtra("Stocklist");
                newproductDetailsArraylist = productListModel.getProductListModels();
//                productDetailsArraylist.add(newproductDetailsArraylist.get(0));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if (newproductDetailsArraylist.size() > 0) {
            //strMrpArray = new String[newproductDetailsArraylist.size()];

            for (int i = 0; i < newproductDetailsArraylist.size(); i++) {
                View tr = (TableRow) View.inflate(StockAllActivity.this, R.layout.inflate_stockall_row, null);

                TextView product = (TextView) tr.findViewById(R.id.txtproduct);
                TextView amount = (TextView) tr.findViewById(R.id.txtmrp);
                TextView openingbal = (TextView) tr.findViewById(R.id.txtopeningbal);
                final EditText quantity = (EditText) tr.findViewById(R.id.edtquantity);


                quantity.addTextChangedListener(new TextWatcher()
                {
                    public void afterTextChanged(Editable s)
                    {
                        String x = s.toString();
                        if(x.startsWith("0"))
                        {
                            quantity.setError("should not starts with 0");
                        }
                    }
                    public void beforeTextChanged(CharSequence s, int start, int count, int after)
                    {

                    }
                    public void onTextChanged(CharSequence s, int start, int before, int count)
                    {

                    }
                });



                productModel = newproductDetailsArraylist.get(i);

                String a_id = productModel.getA_Id();
                String s = getLastInsertIDofStock1(a_id, outletcode);

                product.setText(productModel.getProductName());
                amount.setText(productModel.getPTT());
                openingbal.setText(s);

                tl_sale_calculation.addView(tr);

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_home:
                v.startAnimation(AnimationUtils.loadAnimation(StockAllActivity.this, R.anim.button_click));
                Intent i = new Intent(getApplicationContext(),
                        DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                break;

            case R.id.btn_logout:
                v.startAnimation(AnimationUtils.loadAnimation(StockAllActivity.this, R.anim.button_click));
                Intent i1 = new Intent(getApplicationContext(), LoginActivity.class);
                i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i1);

                break;

            case R.id.btn_save:

                try {
                    int etcount = 0;
                    int count = 0;


                    if (tl_sale_calculation.getChildCount() >0) {
                        for (int j = 0; j < tl_sale_calculation.getChildCount(); j++) {

                            TableRow t = (TableRow) tl_sale_calculation
                                    .getChildAt(j);
                            EditText edt_qty = (EditText) t.getChildAt(1);

                            if (edt_qty.getText().toString().trim()
                                    .equalsIgnoreCase("0")
                                    || edt_qty.getText().toString().trim()
                                    .startsWith("0")
                                    || edt_qty.getText().toString().trim()
                                    .equalsIgnoreCase("")
                                    || edt_qty.getText().toString().trim()
                                    .equalsIgnoreCase(" ")) {

                                cd.displayMessage("Please Enter proper value");

                            } else {
                                etcount++;

                            }
                            Log.e("etcount", String.valueOf(etcount));
                        }

                    }

                    int numberofproduct = (tl_sale_calculation.getChildCount());

                    if (numberofproduct == etcount) {
                        if (tl_sale_calculation.getChildCount() >0) {

                            count = saveData();

                        }
                        showAlertDialog(count);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.btn_back:
                v.startAnimation(AnimationUtils.loadAnimation(StockAllActivity.this, R.anim.button_click));
                finish();
                startActivity(new Intent(StockAllActivity.this,
                        StockActivity.class));
                break;

            default:
                break;
        }
    }

    public void showAlertDialog(int count) {
        if (count == tl_sale_calculation.getChildCount()) {

            // mProgress.dismiss();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    StockAllActivity.this);

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
                                            StockAllActivity.this,
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
                                            StockAllActivity.this,
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
                    StockAllActivity.this);

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
                                            StockAllActivity.this,
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

    @SuppressLint("SimpleDateFormat")
    public String getLastInsertIDofStock1( String Aid,String outletcode) {

        LOTUS.dbCon.open();
        mCursor = LOTUS.dbCon.getstockdata(Aid,outletcode);

        int count = mCursor.getCount();

        if (mCursor != null) {

            if (mCursor.moveToFirst()) {

                do {

                    closebal = mCursor.getString(mCursor.getColumnIndex("close_bal"));
                    old_stock_recive = mCursor.getString(mCursor.getColumnIndex("stock_received"));
                    str_stockinhand = mCursor.getString(mCursor.getColumnIndex("stock_in_hand"));

                } while (mCursor.moveToNext());


            }
        }
        LOTUS.dbCon.close();

        return closebal;
    }

    public int saveData() {

        int count = 0;
        int id = 0;
        for (int i = 0, j = 0; i < tl_sale_calculation.getChildCount() && newproductDetailsArraylist.size() >0; i++, j++) {

            TableRow t = (TableRow) tl_sale_calculation
                    .getChildAt(i);
            EditText edt_qty = (EditText) t.getChildAt(1);
            TextView tv_mrp = (TextView) t.getChildAt(2);
            TextView openingbal = (TextView) t.getChildAt(3);


            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm:ss");
            String insert_timestamp = sdf.format(c
                    .getTime());

            productModel = newproductDetailsArraylist.get(j);

            String Barcodes = productModel.getBarcodes();
            String A_Id = productModel.getA_Id();
            String size = productModel.getSize();
            String Brand = productModel.getBrand();
            String Category = productModel.getCategory();
            String SubCategory = productModel.getSubCategory();
            String SingleOffer = productModel.getSingleOffer();
            String ProductName = productModel.getProductName();

            String s = getLastInsertIDofStock1(A_Id, outletcode);

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat month_date = new SimpleDateFormat(
                    "MMMM");
            String month_name = month_date.format(cal
                    .getTime());

            Calendar cal1 = Calendar.getInstance();
            SimpleDateFormat year_format = new SimpleDateFormat(
                    "yyyy");
            String year_name = year_format.format(cal1
                    .getTime());

            String price = tv_mrp.getText().toString()
                    .trim();

            if (price.equalsIgnoreCase("")) {
                price = "0";
            }


            LOTUS.dbCon.open();

            Cursor cur = LOTUS.dbCon.fetchonestock(A_Id, outletcode);


            if (cur != null && cur.getCount() > 0) {
                cur.moveToFirst();
                soldstock = cur.getString(cur.getColumnIndex("sold_stock"));
                str_openingstock = cur.getString(cur.getColumnIndex("opening_stock"));
                if (soldstock != null) {

                    if (soldstock.equals("")) {

                        soldstock = "0";
                    }

                } else {
                    soldstock = "0";
                }

                if (str_openingstock != null) {

                    if (str_openingstock.equals("")) {

                        str_openingstock = "0";
                    }

                } else {
                    str_openingstock = "0";
                }

            } else {
                soldstock = "0";
                str_openingstock = "0";
            }

            Log.e("solddd", soldstock);
            Log.e("str_openingstock", str_openingstock);
            // }
            LOTUS.dbCon.close();

            String fresh_stock = edt_qty.getText().toString();

            if (old_stock_recive != null) {

                if (old_stock_recive.equalsIgnoreCase("")
                        || old_stock_recive
                        .equalsIgnoreCase("null")
                        || old_stock_recive
                        .equalsIgnoreCase(" ")) {

                    old_stock_recive = "0";
                }
            } else {
                old_stock_recive = "0";
            }

            Log.e("old_stock_recive", old_stock_recive);

            int new_fresh_stock = Integer
                    .parseInt(fresh_stock)
                    + Integer.parseInt(old_stock_recive);

            Log.e("new_fresh_stock", String.valueOf(new_fresh_stock));

            int stock_in_hand = Integer
                    .parseInt(str_openingstock)
                    + new_fresh_stock;

            Log.e("stock_in_hand", String.valueOf(stock_in_hand));

            int closing_stock = stock_in_hand
                    - Integer.parseInt(soldstock);


            Log.e("i_close", String.valueOf(closing_stock));


            String selection = "A_id = ? AND outletcode = ?";
            // WHERE clause arguments
            String[] selectionArgs = {A_Id,outletcode};

            String valuesArray[] = {A_Id,Barcodes, Brand, Category, SubCategory, SingleOffer, ProductName, price, size, username,
                    str_openingstock, String.valueOf(new_fresh_stock), String.valueOf(stock_in_hand),
                     String.valueOf(closing_stock), soldstock, "0", "0", "0","0",insert_timestamp, insert_timestamp,
                    month_name,year_name,insert_timestamp,outletcode};
            boolean output = LOTUS.dbCon.updateBulk(DbHelper.TABLE_STOCK, selection, valuesArray, utils.columnNamesStock, selectionArgs);

            if (output){
                count++;
            }

        }

        return count;
    }


}
