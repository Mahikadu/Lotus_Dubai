package com.prod.sudesi.lotusherbalsnew.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.prod.sudesi.lotusherbalsnew.R;
import com.prod.sudesi.lotusherbalsnew.dbConfig.DataBaseCon;

/**
 * Created by Admin on 18-10-2017.
 */

public class SaleActivity extends Activity implements View.OnClickListener {

    AutoCompleteTextView product_category, product_type, product_mode;

    Button btn_proceed, btn_home, btn_logout;

    TableLayout tl_productList;

    TableRow tr_header;

    TextView tv_h_username,txt_header;

    SharedPreferences shp;
    SharedPreferences.Editor shpeditor;

    DataBaseCon db;

    CardView modecardview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        product_category = (AutoCompleteTextView) findViewById(R.id.spin_category);
        product_type = (AutoCompleteTextView) findViewById(R.id.spin_type);
        product_mode = (AutoCompleteTextView) findViewById(R.id.spin_mode);
        modecardview = (CardView)findViewById(R.id.modecardview);
        tl_productList = (TableLayout) findViewById(R.id.tl_productList);
        btn_proceed = (Button) findViewById(R.id.btn_proceed);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        tr_header = (TableRow) findViewById(R.id.tr_header);
        tv_h_username = (TextView) findViewById(R.id.tv_h_username);
        txt_header = (TextView) findViewById(R.id.textView1);
        txt_header.setText("SALE");

        btn_proceed.setOnClickListener(this);
        btn_home.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        product_mode.setVisibility(View.GONE);
        modecardview.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_proceed:

                break;

            case R.id.btn_home:
                v.startAnimation(AnimationUtils.loadAnimation(SaleActivity.this, R.anim.button_click));
                Intent i = new Intent(getApplicationContext(),
                        DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                break;

            case R.id.btn_logout:
                v.startAnimation(AnimationUtils.loadAnimation(SaleActivity.this, R.anim.button_click));
                Intent i1 = new Intent(getApplicationContext(), LoginActivity.class);
                i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i1);

                break;

            default:
                break;
        }
    }
}
