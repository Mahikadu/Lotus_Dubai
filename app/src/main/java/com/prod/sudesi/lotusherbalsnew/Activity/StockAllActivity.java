package com.prod.sudesi.lotusherbalsnew.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.prod.sudesi.lotusherbalsnew.Models.ProductListModel;
import com.prod.sudesi.lotusherbalsnew.Models.ProductModel;
import com.prod.sudesi.lotusherbalsnew.R;
import com.prod.sudesi.lotusherbalsnew.Utils.SharedPref;
import com.prod.sudesi.lotusherbalsnew.dbconfig.DbHelper;

import java.util.ArrayList;

/**
 * Created by Admin on 30-10-2017.
 */

public class StockAllActivity extends Activity implements View.OnClickListener{

    Context context;
    Button btn_save, btn_back, btn_home, btn_logout;
    private SharedPref sharedPref;

    String outletcode,username;

    TextView tv_h_username;

    TableLayout tl_sale_calculation;

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

        setContentView(R.layout.activity_stock_all);

        context = StockAllActivity.this;

        sharedPref = new SharedPref(context);

        tl_sale_calculation = (TableLayout) findViewById(R.id.tl_sale_calculation);

        btn_save = (Button) findViewById(R.id.btn_save);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);

        LOTUS.dbCon.open();
        outletcode = LOTUS.dbCon.getActiveoutletCode();
        LOTUS.dbCon.close();

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
                EditText quantity = (EditText) tr.findViewById(R.id.edtquantity);


                productModel = newproductDetailsArraylist.get(i);

                product.setText(productModel.getProductName());
                amount.setText(productModel.getPTT());
                openingbal.setText("0");

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

            default:
                break;
        }
    }
}
