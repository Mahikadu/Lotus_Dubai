package com.prod.sudesi.lotusherbalsdubai.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MotionEvent;
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
 * Created by Admin on 02-11-2017.
 */

public class SaleDetailsActivity extends Activity implements View.OnClickListener {

    Context context;
    Button btn_save, btn_back, btn_home, btn_logout;
    private SharedPref sharedPref;

    EditText edt_gross, edt_discount, edt_netamt;

    String outletcode, username;

    TextView tv_h_username;

    TableLayout tablel_sale_calculation;

    ConnectionDetector cd;
    Utils utils;

    ArrayList<ProductModel> productDetailsArraylist;
    ArrayList<ProductModel> newproductDetailsArraylist;
    ProductModel productModel;
    ProductListModel productListModel;

    private static int ecolor;
    private static String namestring, fieldValue;
    private static ForegroundColorSpan fgcspan;
    private static SpannableStringBuilder ssbuilder;

    String str_openingstock = "0", soldstock = "0", closebal = "0", old_stock_recive = "0", str_stockinhand = "0",
            str_totalgrossamount = "0", str_totalnetamount = "0", Str_discount = "0", str_stockreceived = "0";

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
        //edt_discount = (EditText) findViewById(R.id.edt_discount);
        edt_netamt = (EditText) findViewById(R.id.edt_netamt);

        btn_save = (Button) findViewById(R.id.btn_save_sale);
        btn_back = (Button) findViewById(R.id.btn_back_sale);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        try {
            LOTUS.dbCon.open();
            outletcode = LOTUS.dbCon.getActiveoutletCode();
            LOTUS.dbCon.close();
        } catch (Exception e) {
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
        edt_gross.setOnClickListener(this);
        edt_netamt.setOnClickListener(this);

       /* edt_discount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edt_discount.setFocusable(true);
                edt_discount.setFocusableInTouchMode(true);
                return false;
            }
        });*/


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
                final EditText quantity = (EditText) tr.findViewById(R.id.edtquantitysale);

                quantity.addTextChangedListener(new TextWatcher() {
                    public void afterTextChanged(Editable s) {
                        String x = s.toString();
                        if (x.startsWith("0")) {
                            quantity.setError("should not starts with 0");
                        }
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }
                });


                productModel = newproductDetailsArraylist.get(i);

                String a_id = productModel.getA_Id();

                product.setText(productModel.getProductName());
                amount.setText(productModel.getPTT());

                tablel_sale_calculation.addView(tr);

            }
        }
       /* edt_discount.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                String x = s.toString();
                if (x.startsWith("0")) {
                    edt_discount.setError("should not starts with 0");
                }
                edt_netamt.setText("");
            }
        });*/
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.edt_gross:
                try {
                    Float total = 0.0f;
                    for (int i = 0; i < tablel_sale_calculation.getChildCount(); i++) {

                        TableRow t = (TableRow) tablel_sale_calculation.getChildAt(i);

                        EditText edt_qty = (EditText) t.getChildAt(1);
                        TextView tv_mrp = (TextView) t.getChildAt(2);
                        Float int_quantity, int_mrp;

                        if (!edt_qty.getText().toString().trim()
                                .equalsIgnoreCase("0")
                                || !edt_qty.getText().toString().trim()
                                .startsWith("0")
                                || !edt_qty.getText().toString().trim()
                                .equalsIgnoreCase("")
                                || !edt_qty.getText().toString().trim()
                                .equalsIgnoreCase(" ")) {
                            int_quantity = Float.parseFloat(edt_qty
                                    .getText().toString().trim());
                            int_mrp = Float.parseFloat(tv_mrp.getText()
                                    .toString());
                            Float multiply = int_quantity * int_mrp;
                            total = total + multiply;
                            edt_gross.setText(String.valueOf(total));
                        } else {
                            cd.displayMessage("Please Enter proper quantity");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.edt_netamt:

                if (!edt_gross.getText().toString().equals("")) {
                    edt_netamt.setText(edt_gross.getText().toString());
                }
                break;
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

            case R.id.btn_save_sale:
                v.startAnimation(AnimationUtils.loadAnimation(SaleDetailsActivity.this, R.anim.button_click));
                if(cd.isCurrentDateMatchDeviceDate()){
                try {
                    int etcount = 0;
                    int count = 0;
                    if (tablel_sale_calculation.getChildCount() > 0) {
                        for (int j = 0; j < tablel_sale_calculation.getChildCount(); j++) {

                            TableRow t = (TableRow) tablel_sale_calculation
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

                            String tablecount = String.valueOf(tablel_sale_calculation
                                    .getChildCount());

                            /*try {
                                if (dis == 0.0f) {
                                    a = dis;
                                } else {
                                    float ttt = Float.parseFloat(tablecount);

                                    float a1 = dis / (ttt);
                                    String adis = String.format("%.02f", a1);
                                    a = Float.parseFloat(adis);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }*/
                            float net1;

                            if (tablel_sale_calculation.getChildCount() > 0) {
                                for (int k = 0, j = 0; k < tablel_sale_calculation.getChildCount() && newproductDetailsArraylist.size() > 0; k++, j++) {

                                    TableRow t = (TableRow) tablel_sale_calculation
                                            .getChildAt(k);
                                    EditText edt_qty = (EditText) t
                                            .getChildAt(1);
                                    TextView tv_mrp = (TextView) t
                                            .getChildAt(2);

                                    productModel = newproductDetailsArraylist.get(j);

                                    String Barcodes = productModel.getBarcodes();
                                    String A_Id = productModel.getA_Id();
                                    String size = productModel.getSize();
                                    String Brand = productModel.getBrand();
                                    String Category = productModel.getCategory();
                                    String SubCategory = productModel.getSubCategory();
                                    String SingleOffer = productModel.getSingleOffer();
                                    String ProductName = productModel.getProductName();

                                    float calc_gross = Float.parseFloat(tv_mrp
                                            .getText().toString())
                                            * Integer.parseInt(edt_qty
                                            .getText().toString());

                                    //float boc_date_net = calc_gross - a;
                                    float gross = 0;
                                    int net = 0, closing = 0, sold_stock = 0;
                                    int stkinhand = 0;
                                    int i_sold = 0;

                                    LOTUS.dbCon.open();
                                    Cursor cur = LOTUS.dbCon.fetchonestockmultplecolumn(A_Id, outletcode);

                                    if (cur != null && cur.getCount() > 0) {
                                        cur.moveToFirst();

                                        str_openingstock = cur.getString(cur.getColumnIndex("opening_stock"));
                                        str_totalgrossamount = cur.getString(cur.getColumnIndex("total_gross_amount"));
                                        str_totalnetamount = cur.getString(cur.getColumnIndex("total_net_amount"));
                                        //Str_discount = cur.getString(cur.getColumnIndex("discount"));
                                        closebal = cur.getString(cur.getColumnIndex("close_bal"));
                                        soldstock = cur.getString(cur.getColumnIndex("sold_stock"));
                                        str_stockreceived = cur.getString(cur.getColumnIndex("stock_received"));
                                        str_stockinhand = cur.getString(cur.getColumnIndex("stock_in_hand"));

                                        boolean boo = validateEdit(edt_qty, "Quantity is greater than available stock", closebal);


                                        if (boo == true) {

                                            if (soldstock != null) {
                                                if (soldstock.trim().equalsIgnoreCase("0")
                                                        || soldstock.trim().equalsIgnoreCase("")) {

                                                    i_sold = 0;

                                                } else {
                                                    i_sold = Integer.parseInt(soldstock.trim());
                                                }
                                            }
                                            Log.e("old sold", String.valueOf(i_sold));

                                            i_sold = i_sold + Integer.parseInt(edt_qty.getText().toString());
                                            Log.e("new sold", String.valueOf(i_sold));

                                            int i_stokreceive = 0;
                                            if (str_stockreceived != null) {
                                                if (str_stockreceived.trim().equalsIgnoreCase("0")
                                                        || str_stockreceived.trim().equalsIgnoreCase("")) {

                                                    i_stokreceive = 0;

                                                } else {
                                                    i_stokreceive = Integer.parseInt(str_stockreceived.trim());
                                                }
                                            }
                                            Log.e("i_stokinhand", String.valueOf(i_stokreceive));

                                            int i_stokinhand = 0;
                                            if (str_stockinhand != null) {
                                                if (str_stockinhand.trim().equalsIgnoreCase("0")
                                                        || str_stockinhand.trim().equalsIgnoreCase("")) {

                                                    i_stokinhand = 0;

                                                } else {
                                                    i_stokinhand = Integer.parseInt(str_stockinhand.trim());
                                                }
                                            }
                                            Log.e("i_stokinhand", String.valueOf(i_stokinhand));

                                            int i_clstk = i_stokinhand - i_sold;

                                            Log.e("i_clstk", String.valueOf(i_clstk));

                                            if (str_totalgrossamount != null) {
                                                if (!str_totalgrossamount.equalsIgnoreCase("")) {

                                                    if (!str_totalgrossamount.equalsIgnoreCase(" ")) {
                                                        Float total_gross = Float.parseFloat(str_totalgrossamount);

                                                        gross = total_gross + calc_gross;

                                                    } else {
                                                        gross = calc_gross;

                                                    }

                                                } else {
                                                    gross = calc_gross;

                                                }
                                            } else {
                                                gross = calc_gross;

                                            }
                                            /*if (Str_discount != null) {
                                                if (!Str_discount.equalsIgnoreCase("")) {

                                                    if (!Str_discount.contains(" ")) {

                                                        disss = (Float.parseFloat(Str_discount) + a);

                                                    } else {
                                                        if (edt_discount.getText().toString().equals("")) {
                                                            // discount = 0;//
                                                            disss = 0;
                                                        } else {

                                                            disss = a;

                                                        }

                                                    }

                                                } else {
                                                    if (edt_discount.getText().toString().equals("")) {

                                                        disss = 0;
                                                    } else {

                                                        disss = 0;

                                                    }
                                                }
                                            } else {
                                                if (edt_discount.getText().toString().equals("")) {
                                                    // discount = 0;//
                                                    disss = 0;
                                                } else {

                                                    disss = a;
                                                }
                                            }*/

                                            if (str_totalnetamount != null) {
                                                if (!str_totalnetamount.equalsIgnoreCase("")) {

                                                    if (!str_totalnetamount.contains(" ")) {

                                                        String cal_gross = String.valueOf(calc_gross);

                                                        net1 = Float.parseFloat(str_totalnetamount) + Float.parseFloat(cal_gross);

                                                    } else {
                                                        String cal_gross = String.valueOf(calc_gross);

                                                        net1 = (Float.parseFloat(cal_gross));

                                                    }

                                                } else {
                                                    String cal_gross = String.valueOf(calc_gross);

                                                    net1 = (Float.parseFloat(cal_gross));
                                                }

                                            } else {
                                                String cal_gross = String.valueOf(calc_gross);

                                                net1 = (Float.parseFloat(cal_gross));
                                            }

                                            Calendar cal = Calendar
                                                    .getInstance();
                                            SimpleDateFormat month_date = new SimpleDateFormat(
                                                    "MMMM");
                                            String month_name = month_date
                                                    .format(cal.getTime());

                                            Calendar cal1 = Calendar
                                                    .getInstance();
                                            SimpleDateFormat year_format = new SimpleDateFormat(
                                                    "yyyy");
                                            String year_name = year_format
                                                    .format(cal1.getTime());

                                            Calendar cal2 = Calendar
                                                    .getInstance();
                                            SimpleDateFormat sdf = new SimpleDateFormat(
                                                    "yyyy-MM-dd HH:mm:ss");
                                            String insert_timestamp = sdf
                                                    .format(cal2.getTime());

                                            String selectedDateStr = android.text.format.DateFormat.format("yyyy-MM-dd", cal2.getTime()).toString();

                                            String[] insert_timestamps = insert_timestamp
                                                    .split(" ");

                                            String check_timestamp = insert_timestamps[0];

                                            String price = tv_mrp.getText().toString()
                                                    .trim();

                                            if (price.equalsIgnoreCase("")) {
                                                price = "0";
                                            }


                                            String selection = "A_id = ? AND outletcode = ? AND insert_date like '" + selectedDateStr  + " %'";
                                            // WHERE clause arguments
                                            String[] selectionArgs = {A_Id, outletcode};

                                            String valuesArray[] = {A_Id, Barcodes, Brand, Category, SubCategory, SingleOffer, ProductName, price, size, username,
                                                    str_openingstock, String.valueOf(i_stokreceive), String.valueOf(i_stokinhand),
                                                    String.valueOf(i_clstk), String.valueOf(i_sold), String.valueOf(gross), String.valueOf(net1),
                                                    "0", insert_timestamp, insert_timestamp,
                                                    month_name, year_name, insert_timestamp, outletcode};
                                            boolean output = LOTUS.dbCon.updateBulk(DbHelper.TABLE_STOCK, selection, valuesArray, utils.columnNamesStock, selectionArgs);

                                            if (output) {
                                                count++;
                                            }

                                        }

                                    } else {
                                        cd.displayMessage("Stock not available");
                                    }

                                }

                                if (count == tablel_sale_calculation.getChildCount()) {

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SaleDetailsActivity.this);

                                    // set title
                                    alertDialogBuilder.setTitle("Saved Successfully!!");

                                    // set dialog message
                                    alertDialogBuilder
                                            .setMessage("Go  TO  :")
                                            .setCancelable(false)
                                            .setNegativeButton(
                                                    "Stock & Sale Page",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int id) {

                                                            dialog.cancel();
                                                            finish();
                                                            startActivity(new Intent(SaleDetailsActivity.this, StockActivity.class));

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
                                                            startActivity(new Intent(SaleDetailsActivity.this, DashBoardActivity.class));

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
                                    alertDialogBuilder
                                            .setTitle("Data Not Saved!!!");

                                    // set dialog message
                                    alertDialogBuilder
                                            .setMessage(
                                                    "Please check the available stock for specified products")
                                            .setCancelable(false)
                                            .setNegativeButton(
                                                    "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(
                                                                DialogInterface dialog,
                                                                int id) {

                                                            dialog.cancel();
                                                            finish();
                                                            startActivity(new Intent(SaleDetailsActivity.this, StockActivity.class));

                                                        }
                                                    });

                                    // create alert dialog
                                    AlertDialog alertDialog = alertDialogBuilder
                                            .create();

                                    // show it
                                    alertDialog.show();
                                }
                            }


                        }else{
                            cd.displayMessage("Please Enter GrossTotal and NetTotal");
                        }

                    } else {

                        cd.displayMessage("Please enter valid value in quantity fields");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                }else{
                    Toast.makeText(SaleDetailsActivity.this, "Your Handset Date Not Match Current Date", Toast.LENGTH_LONG).show();

                }

                break;

            case R.id.btn_back_sale:
                v.startAnimation(AnimationUtils.loadAnimation(SaleDetailsActivity.this, R.anim.button_click));
                finish();
                startActivity(new Intent(SaleDetailsActivity.this,
                        StockActivity.class));
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

    }


    public boolean validateEdit(EditText edt, String errorString,
                                String valueString) {

        Boolean result = false;

        if (valueString != null) {
            if (!valueString.equals("")) {
                if (!valueString.equalsIgnoreCase(" ")) {
                    if (!valueString.equalsIgnoreCase("0")) {
                        if (Integer.parseInt(edt.getText().toString()) > Integer
                                .parseInt(valueString)) {
                            result = false;
                            ecolor = Color.RED; // whatever color you want
                            namestring = errorString;
                            fgcspan = new ForegroundColorSpan(ecolor);
                            ssbuilder = new SpannableStringBuilder(namestring);
                            ssbuilder.setSpan(fgcspan, 0, namestring.length(),
                                    0);
                            edt.setError(ssbuilder);
                        } else {
                            result = true;
                        }
                    } else {
                        result = false;
                        ecolor = Color.RED; // whatever color you want
                        namestring = "No Stock Available";
                        fgcspan = new ForegroundColorSpan(ecolor);
                        ssbuilder = new SpannableStringBuilder(namestring);
                        ssbuilder.setSpan(fgcspan, 0, namestring.length(), 0);
                        edt.setError(ssbuilder);
                    }
                } else {
                    result = false;
                    ecolor = Color.RED; // whatever color you want
                    namestring = "No Stock Available";
                    fgcspan = new ForegroundColorSpan(ecolor);
                    ssbuilder = new SpannableStringBuilder(namestring);
                    ssbuilder.setSpan(fgcspan, 0, namestring.length(), 0);
                    edt.setError(ssbuilder);
                }
            } else {
                result = false;
                ecolor = Color.RED; // whatever color you want
                namestring = "No Stock Available";
                ;
                fgcspan = new ForegroundColorSpan(ecolor);
                ssbuilder = new SpannableStringBuilder(namestring);
                ssbuilder.setSpan(fgcspan, 0, namestring.length(), 0);
                edt.setError(ssbuilder);
            }
        } else {
            result = false;
            ecolor = Color.RED; // whatever color you want
            namestring = errorString;
            fgcspan = new ForegroundColorSpan(ecolor);
            ssbuilder = new SpannableStringBuilder(namestring);
            ssbuilder.setSpan(fgcspan, 0, namestring.length(), 0);
            edt.setError(ssbuilder);
        }
        return result;
    }
}
