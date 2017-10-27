package com.prod.sudesi.lotusherbalsnew.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.prod.sudesi.lotusherbalsnew.R;
import com.prod.sudesi.lotusherbalsnew.Utils.SharedPref;

/**
 * Created by Admin on 18-10-2017.
 */

public class StockActivity extends Activity implements View.OnClickListener {

    AutoCompleteTextView product_category, product_type, product_mode;

    Button btn_proceed, btn_home, btn_logout;

    TableLayout tl_productList;

    TableRow tr_header;

    TextView tv_h_username, textView1;

    private SharedPref sharedPref;

    RadioGroup radio_stock_sale;
    RadioButton radio_stock, radio_sale;

    LinearLayout stock_salelayout;
    CardView modecardview;

    String username;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        context = StockActivity.this;

        sharedPref = new SharedPref(context);

        product_category = (AutoCompleteTextView) findViewById(R.id.spin_category);
        product_type = (AutoCompleteTextView) findViewById(R.id.spin_type);
        product_mode = (AutoCompleteTextView) findViewById(R.id.spin_mode);
        tl_productList = (TableLayout) findViewById(R.id.tl_productList);
        btn_proceed = (Button) findViewById(R.id.btn_proceed);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        tr_header = (TableRow) findViewById(R.id.tr_header);
        tv_h_username = (TextView) findViewById(R.id.tv_h_username);

        modecardview = (CardView) findViewById(R.id.modecardview);

        stock_salelayout = (LinearLayout) findViewById(R.id.stock_salelayout);

        radio_stock_sale = (RadioGroup) findViewById(R.id.radio_stock_sale);
        radio_stock = (RadioButton) findViewById(R.id.radio_stock);
        radio_sale = (RadioButton) findViewById(R.id.radio_sale);

        username = sharedPref.getLoginId();
        Log.v("", "username==" + username);

        tv_h_username.setText(username);

        btn_proceed.setOnClickListener(this);
        btn_home.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

        radio_stock_sale.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_stock:
                        stock_salelayout.setVisibility(View.VISIBLE);
                        product_mode.setVisibility(View.VISIBLE);
                        modecardview.setVisibility(View.VISIBLE);
                        break;

                    case R.id.radio_sale:
                        stock_salelayout.setVisibility(View.VISIBLE);
                        product_mode.setVisibility(View.GONE);
                        modecardview.setVisibility(View.GONE);
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_proceed:

                break;

            case R.id.btn_home:
                v.startAnimation(AnimationUtils.loadAnimation(StockActivity.this, R.anim.button_click));
                Intent i = new Intent(getApplicationContext(),
                        DashBoardActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                break;

            case R.id.btn_logout:
                v.startAnimation(AnimationUtils.loadAnimation(StockActivity.this, R.anim.button_click));
                Intent i1 = new Intent(getApplicationContext(), LoginActivity.class);
                i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i1);

                break;

            default:
                break;
        }
    }


}
