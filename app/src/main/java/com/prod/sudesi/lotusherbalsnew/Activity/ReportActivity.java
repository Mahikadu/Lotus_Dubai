package com.prod.sudesi.lotusherbalsnew.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
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
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.prod.sudesi.lotusherbalsnew.Models.OutletModel;
import com.prod.sudesi.lotusherbalsnew.Models.ProductModel;
import com.prod.sudesi.lotusherbalsnew.Models.StockReportModel;
import com.prod.sudesi.lotusherbalsnew.R;
import com.prod.sudesi.lotusherbalsnew.Utils.SharedPref;
import com.prod.sudesi.lotusherbalsnew.adapter.ReportAdapter;
import com.prod.sudesi.lotusherbalsnew.dbconfig.DbHelper;
import com.prod.sudesi.lotusherbalsnew.libs.ConnectionDetector;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Admin on 30-10-2017.
 */

public class ReportActivity extends Activity implements View.OnClickListener{

    Context context;
    RadioButton rbstock,rbattendance;
    TableRow table_row_stock, table_row_attend;
    private SharedPref sharedPref;
    TextView tv_h_username;
    Button btn_home, btn_logout;
    String username;
    String displayCategory;
    String outletCode = "";
    String outletName = "";
    AutoCompleteTextView sp_outletName;

    ReportAdapter adapter;

    ListView stocklistview,attendancelist;

    HorizontalScrollView horizantalscrollviewforstock;

    private ArrayList<OutletModel> outletDetailsArraylist;
    OutletModel outletModel;
    String[] strOutletArray = null;
    String outletstring;

    public StockReportModel stockReportModel;
    private ArrayList<StockReportModel> stockReportDetailsArraylist;

    ConnectionDetector cd;

    private ProgressDialog mProgress = null;

    CardView outletcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        context = ReportActivity.this;

        sharedPref = new SharedPref(context);

        cd = new ConnectionDetector(context);

        mProgress = new ProgressDialog(context);

        rbstock = (RadioButton) findViewById(R.id.rb_stock);
        rbattendance = (RadioButton) findViewById(R.id.rb_attendance);

        table_row_stock = (TableRow) findViewById(R.id.tr_label_stock);
        table_row_attend = (TableRow) findViewById(R.id.tr_label_attend);

        outletcard = (CardView) findViewById(R.id.outletcardview);

        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        username = sharedPref.getLoginId();
        sp_outletName = (AutoCompleteTextView) findViewById(R.id.spin_outletname);
        ArrayList<String> outletNameArray = new ArrayList<String>();

        stocklistview = (ListView) findViewById(R.id.stock_list);
        attendancelist = (ListView) findViewById(R.id.attendancelist);

        horizantalscrollviewforstock = (HorizontalScrollView) findViewById(R.id.horizantal_scrollview_stock_report);

        horizantalscrollviewforstock.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                int scrollX = v.getScrollX();
                int scrollY = v.getScrollY();

                horizantalscrollviewforstock.scrollTo(scrollX, scrollY);

                return false;

            }
        });

        cd.displayMessage("Please Select outlet");


        rbstock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (rbstock.isChecked()) {

                    sp_outletName.setText("");
                    sp_outletName.setVisibility(View.VISIBLE);
                    outletcard.setVisibility(View.VISIBLE);
                    table_row_stock.setVisibility(View.VISIBLE);

                    table_row_attend.setVisibility(View.GONE);
                    attendancelist.setVisibility(View.GONE);
                    rbattendance.setChecked(false);


                }
            }
        });

        rbattendance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub

                if (isChecked) {

                    sp_outletName.setText("");
                    sp_outletName.setVisibility(View.GONE);
                    outletcard.setVisibility(View.GONE);

                    table_row_stock.setVisibility(View.GONE);
                    table_row_attend.setVisibility(View.VISIBLE);
                    stocklistview.setVisibility(View.GONE);
                    attendancelist.setVisibility(View.VISIBLE);
                    rbstock.setChecked(false);
                    /*report_attendance = new ShowReportofAttendance();
                    report_attendance.execute();*/
                } else {

                    attendancelist.setVisibility(View.GONE);
                }
            }
        });

        btn_home.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

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
            sp_outletName.setAdapter(adapter1);
        }

        sp_outletName.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sp_outletName.showDropDown();
                return false;
            }
        });

        sp_outletName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = "",outletcode = "";
                if (strOutletArray != null && strOutletArray.length > 0) {

                    outletstring = parent.getItemAtPosition(position).toString();
                    for (int i = 0; i < outletDetailsArraylist.size(); i++) {
                         text = outletDetailsArraylist.get(i).getOutletName();
                        outletcode = outletDetailsArraylist.get(i).getOutletCode();
                        if (text.equalsIgnoreCase(outletstring)) {
                            outletName = text;
                            outletCode = outletcode;
                        }
                    }

                    if (text != null && text.length() > 0) {
                        ShowReportofStock showReportofStock = new ShowReportofStock();
                        showReportofStock.execute(outletCode);
                    }else{
                        cd.displayMessage("Please Select outlet");
                    }


                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_home:
                v.startAnimation(AnimationUtils.loadAnimation(ReportActivity.this, R.anim.button_click));
                Intent i = new Intent(getApplicationContext(),
                        DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                break;

            case R.id.btn_logout:
                v.startAnimation(AnimationUtils.loadAnimation(ReportActivity.this, R.anim.button_click));
                Intent i1 = new Intent(getApplicationContext(), LoginActivity.class);
                i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i1);

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

    public class ShowReportofStock extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            mProgress.setMessage("Please Wait");
            mProgress.show();
            mProgress.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {

                LOTUS.dbCon.open();
                String outlet = params[0];

                String where = " where outletcode = '" + outlet + "'";

                Cursor cursor = LOTUS.dbCon.fetchFromSelect(DbHelper.TABLE_STOCK, where);

                stockReportDetailsArraylist = new ArrayList<>();
                if (cursor != null && cursor.moveToFirst()) {
                    cursor.moveToFirst();
                    do {
                        stockReportModel = new StockReportModel();
                        stockReportModel.setA_id(cursor.getString(cursor.getColumnIndex("A_id")));
                        stockReportModel.setBrand(cursor.getString(cursor.getColumnIndex("Brand")));
                        stockReportModel.setCategory(cursor.getString(cursor.getColumnIndex("Category")));
                        stockReportModel.setSubCategory(cursor.getString(cursor.getColumnIndex("SubCategory")));
                        stockReportModel.setProductName(cursor.getString(cursor.getColumnIndex("ProductName")));
                        stockReportModel.setSize(cursor.getString(cursor.getColumnIndex("size")));
                        stockReportModel.setPTT(cursor.getString(cursor.getColumnIndex("PTT")));
                        stockReportModel.setOpening_stock(cursor.getString(cursor.getColumnIndex("opening_stock")));
                        stockReportModel.setStock_received(cursor.getString(cursor.getColumnIndex("stock_received")));
                        stockReportModel.setStock_in_hand(cursor.getString(cursor.getColumnIndex("stock_in_hand")));
                        stockReportModel.setSold_stock(cursor.getString(cursor.getColumnIndex("sold_stock")));
                        stockReportModel.setClose_bal(cursor.getString(cursor.getColumnIndex("close_bal")));
                        stockReportModel.setTotal_gross_amount(cursor.getString(cursor.getColumnIndex("total_gross_amount")));
                        stockReportModel.setDiscount(cursor.getString(cursor.getColumnIndex("discount")));
                        stockReportModel.setTotal_net_amount(cursor.getString(cursor.getColumnIndex("total_net_amount")));
                        stockReportModel.setOutletcode(cursor.getString(cursor.getColumnIndex("outletcode")));
                        stockReportModel.setSaveServer(cursor.getString(cursor.getColumnIndex("savedServer")));


                        stockReportDetailsArraylist.add(stockReportModel);

                    } while (cursor.moveToNext());

                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            return null;
        }



        @Override
        protected void onPostExecute(String result) {


            Log.e("", "reportlist===" + stockReportDetailsArraylist);
            mProgress.dismiss();
            // TODO Auto-generated method stub
            stocklistview.setVisibility(View.VISIBLE);
            adapter = new ReportAdapter(context, stockReportDetailsArraylist);
            stocklistview.setAdapter(adapter);
            LOTUS.dbCon.close();

            // }
        }

    }
}
