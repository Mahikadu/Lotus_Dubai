package com.prod.sudesi.lotusherbalsnew.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.prod.sudesi.lotusherbalsnew.Models.ProductListModel;
import com.prod.sudesi.lotusherbalsnew.Models.ProductModel;
import com.prod.sudesi.lotusherbalsnew.R;
import com.prod.sudesi.lotusherbalsnew.Utils.SharedPref;
import com.prod.sudesi.lotusherbalsnew.Dbconfig.DbHelper;
import com.prod.sudesi.lotusherbalsnew.libs.ConnectionDetector;

import java.util.ArrayList;
import java.util.Collections;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Admin on 18-10-2017.
 */

public class StockActivity extends Activity implements View.OnClickListener {

    AutoCompleteTextView brand, offerAuto; //product_category, product_subcategory,

    Button btn_proceed, btn_home, btn_logout;

    TableLayout tl_productList;

    TableRow tr_header;

    TextView tv_h_username, textView1;

    private SharedPref sharedPref;

    RadioGroup radio_stock_sale;
    RadioButton radio_stock, radio_sale;

    LinearLayout stock_salelayout;
    // CardView modecardview;

    String username;

    ConnectionDetector cd;

    Context context;

    private ArrayList<String> listBrand;
    String[] strBrandArray = null;
    private ArrayList<String> listCategory;
    String[] strCategoryArray = null;
    private ArrayList<String> listSubCategory;
    String[] strSubCategoryArray = null;

    private ArrayList<String> listoffer;
    String[] strOfferArray = null;

    String brandstring, brandname, categorystring, categoryname, subcategorystring, subcategoryname,offerstring,offername;

    private ArrayList<ProductModel> productDetailsArraylist;
    ProductModel productModel;
    String[] strMrpArray = null;

    ProductListModel productListModel;

    boolean stock = false, sale = false;

    private ArrayList<String> liststockdbid;
    private ArrayList<ProductModel> selectedproductArraylist;

    String outletcode;
    CheckBox cb;
    Spinner spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_stock);

        context = StockActivity.this;

        cd = new ConnectionDetector(context);

        sharedPref = new SharedPref(context);

        brand = (AutoCompleteTextView) findViewById(R.id.spin_brand);
       // product_category = (AutoCompleteTextView) findViewById(R.id.spin_category);
       // product_subcategory = (AutoCompleteTextView) findViewById(R.id.spin_subcategory);
        offerAuto = (AutoCompleteTextView) findViewById(R.id.spin_offer);
        tl_productList = (TableLayout) findViewById(R.id.tl_productList);
        btn_proceed = (Button) findViewById(R.id.btn_proceed);
        btn_home = (Button) findViewById(R.id.btn_home);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        tr_header = (TableRow) findViewById(R.id.tr_header);
        tv_h_username = (TextView) findViewById(R.id.tv_h_username);

        //modecardview = (CardView) findViewById(R.id.modecardview);

        stock_salelayout = (LinearLayout) findViewById(R.id.stock_salelayout);

        radio_stock_sale = (RadioGroup) findViewById(R.id.radio_stock_sale);
        radio_stock = (RadioButton) findViewById(R.id.radio_stock);
        radio_sale = (RadioButton) findViewById(R.id.radio_sale);

        username = sharedPref.getLoginId();
        Log.v("", "username==" + username);

        tv_h_username.setText(username);

        selectedproductArraylist = new ArrayList<>();

        btn_proceed.setOnClickListener(this);
        btn_home.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

        //
        try {
            LOTUS.dbCon.open();
            outletcode = LOTUS.dbCon.getActiveoutletCode();
            LOTUS.dbCon.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        //

        if (outletcode != null && !outletcode.equalsIgnoreCase("")){

        }else{
            new SweetAlertDialog(StockActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("ERROR !!")
                    .setContentText("Please Select outlet")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Intent intent = new Intent(StockActivity.this, DashBoardActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            sDialog.dismiss();
                        }
                    })
                    .show();
        }

        radio_stock_sale.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_stock:
                        stock = true;
                        sale = false;
                        stock_salelayout.setVisibility(View.VISIBLE);
                        // product_mode.setVisibility(View.VISIBLE);
                        //modecardview.setVisibility(View.VISIBLE);
                        brand.setText("");
                        //product_category.setText("");
                        //product_subcategory.setText("");
                        offerAuto.setText("");
                        tl_productList.removeAllViews();
                        break;

                    case R.id.radio_sale:
                        sale = true;
                        stock = false;
                        stock_salelayout.setVisibility(View.VISIBLE);
                        //product_mode.setVisibility(View.GONE);
                        // modecardview.setVisibility(View.GONE);
                        brand.setText("");
                        //product_category.setText("");
                       //product_subcategory.setText("");
                        offerAuto.setText("");
                        tl_productList.removeAllViews();
                        break;
                }
            }
        });

        brand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (brand.length() > 0) {
                    brand.setError(null);
                }

            }
        });

       /* product_category.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (product_category.length() > 0) {
                    product_category.setError(null);
                }

            }
        });
        product_subcategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (product_subcategory.length() > 0) {
                    product_subcategory.setError(null);
                }

            }
        });*/

        offerAuto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (offerAuto.length() > 0) {
                    offerAuto.setError(null);
                }

            }
        });
        fetchBrandDetails();
        /////////Details of Brand
        if (listBrand.size() > 0) {
            strBrandArray = new String[listBrand.size()];
            for (int i = 0; i < listBrand.size(); i++) {
                strBrandArray[i] = listBrand.get(i);
            }
        }
        if (listBrand != null && listBrand.size() > 0) {
            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strBrandArray) {
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
            brand.setAdapter(adapter1);
        }

        brand.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //product_category.setText("");
                //product_subcategory.setText("");
                offerAuto.setText("");
                tl_productList.removeAllViews();
                brand.showDropDown();
                return false;
            }
        });

        brand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (strBrandArray != null && strBrandArray.length > 0) {

                    brandstring = parent.getItemAtPosition(position).toString();
                    for (int i = 0; i < listBrand.size(); i++) {
                        String text = listBrand.get(i);
                        if (text.equalsIgnoreCase(brandstring)) {
                            brandname = text;
                        }
                    }
                    if (brandstring != null && brandstring.length() > 0) {
                        //fetchCategoryDetails(brandname);
                        fetchOfferDetails(brandname);

                    }
                }
            }
        });


       /* product_category.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                product_subcategory.setText("");
                offerAuto.setText("");
                tl_productList.removeAllViews();
                product_category.showDropDown();
                return false;
            }
        });

        product_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (strCategoryArray != null && strCategoryArray.length > 0) {

                    categorystring = parent.getItemAtPosition(position).toString();
                    for (int i = 0; i < listCategory.size(); i++) {
                        String text = listCategory.get(i);
                        if (text.equalsIgnoreCase(categorystring)) {
                            categoryname = text;
                        }
                    }
                    if (categorystring != null && categorystring.length() > 0) {
                        fetchSubCategoryDetails(brandname,categoryname);

                    }
                }
            }
        });

        product_subcategory.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                offerAuto.setText("");
                tl_productList.removeAllViews();
                product_subcategory.showDropDown();
                return false;
            }
        });

        product_subcategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (strSubCategoryArray != null && strSubCategoryArray.length > 0) {

                    subcategorystring = parent.getItemAtPosition(position).toString();
                    for (int i = 0; i < listSubCategory.size(); i++) {
                        String text = listSubCategory.get(i);
                        if (text.equalsIgnoreCase(subcategorystring)) {
                            subcategoryname = text;
                        }
                    }

                    if (subcategorystring != null && subcategorystring.length() > 0) {
                        fetchOfferDetails(brandname,categoryname,subcategoryname);

                    }

                }
            }
        });*/

        offerAuto.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tl_productList.removeAllViews();
                offerAuto.showDropDown();
                return false;
            }
        });

        offerAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (strOfferArray != null && strOfferArray.length > 0) {

                    offerstring = parent.getItemAtPosition(position).toString();
                    for (int i = 0; i < listoffer.size(); i++) {
                        String text = listoffer.get(i);
                        if (text.equalsIgnoreCase(offerstring)) {
                            offername = text;
                        }
                    }

                    try {
                        if (sale) {
                            tl_productList.addView(tr_header);
                            //getproductDetailsforSale(offername,subcategoryname,categoryname,brandname);
                            getproductDetailsforSale(offername,brandname);
                            if(productDetailsArraylist.size() > 0) {
                                for (int i = 0; i < productDetailsArraylist.size(); i++) {
                                    productModel = productDetailsArraylist.get(i);
                                    if (productModel != null) {
                                        if (productModel.getBrand().equalsIgnoreCase(brandname)) {
                                            //if (productModel.getCategory().equalsIgnoreCase(categoryname)) {
                                                //if (productModel.getSubCategory().equalsIgnoreCase(subcategoryname)) {
                                                    if(productModel.getSingleOffer().equalsIgnoreCase(offername)){
                                                    //tl_productList.removeAllViews();
                                                    View tr = (TableRow) View.inflate(StockActivity.this, R.layout.inflate_stocksale_row, null);

                                                    cb = (CheckBox) tr.findViewById(R.id.chck_product);

                                                    spin = (Spinner) tr.findViewById(R.id.spin_mrp);

                                                    productModel = productDetailsArraylist.get(i);

                                                    cb.setText(productModel.getProductName());

                                                    //strMrpArray[i] = productModel.getPTT();

                                                    String mrp = productModel.getPTT();

                                                    String mrps[] = new String[]{mrp};

                                                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(StockActivity.this, android.R.layout.simple_spinner_item, mrps);

                                                    spin.setAdapter(adapter);

                                                    tl_productList.addView(tr);
                                                } else {
                                                    //tl_productList.removeAllViews();
                                                    //tl_productList.addView(tr_header);
                                                }
                                            //}
                                            //}
                                        }
                                    }
                                }
                                View tr1 = (TableRow) View.inflate(StockActivity.this, R.layout.inflate_stocksale_row, null);
                                CheckBox cb = (CheckBox) tr1.findViewById(R.id.chck_product);

                                Spinner spin = (Spinner) tr1.findViewById(R.id.spin_mrp);

                                tr1.setVisibility(View.INVISIBLE);

                                tl_productList.addView(tr1);
                            }
                        }else {
                            tl_productList.removeAllViews();
                            tl_productList.addView(tr_header);
                            //getproductsDetails(offername,subcategoryname,categoryname,brandname);
                            getproductsDetails(offername,brandname);

                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_proceed:
                v.startAnimation(AnimationUtils.loadAnimation(StockActivity.this, R.anim.button_click));
                int chckCount = 0;
                selectedproductArraylist.clear();
                liststockdbid = new ArrayList<>();
                if (brandstring != null && brandstring.length() > 0) {
                    //if (categorystring != null && categorystring.length() > 0) {
                       // if (subcategorystring != null && subcategorystring.length() > 0) {
                            if (offerstring != null && offerstring.length() > 0) {

                                for (int i = 1; i < tl_productList.getChildCount(); i++) {
                                    TableRow tr = (TableRow) tl_productList.getChildAt(i);
                                    CheckBox cb = (CheckBox) tr.getChildAt(0);
                                    if (cb.isChecked()) {
                                        chckCount++;
                                        break;
                                    }
                                }

                                if (chckCount == 0) {
                                    Toast.makeText(getApplicationContext(),
                                            "Please select atleast 1 product",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    for (int i = 1; i < tl_productList.getChildCount(); i++) {
                                        TableRow tr = (TableRow) tl_productList.getChildAt(i);
                                        CheckBox cb = (CheckBox) tr.getChildAt(0);
                                        Spinner spin = (Spinner) tr.getChildAt(1);
                                        if (cb.isChecked()) {
                                            liststockdbid.add(LOTUS.dbCon.fetchStockDbID(cb.getText().toString(), spin.getSelectedItem().toString(), offerstring));

                                        }
                                    }

                                    for (int i = 0; i < liststockdbid.size(); i++) {
                                        listCategory = new ArrayList<>();
                                        String a_id = liststockdbid.get(i);

                                        String where = " where A_Id = '" + a_id + "'";

                                        Cursor cursor = LOTUS.dbCon.fetchFromSelect(DbHelper.TABLE_MASTERSYNC, where);
                                        if (cursor != null && cursor.getCount() > 0) {
                                            cursor.moveToFirst();
                                            do {
                                                productModel = new ProductModel();
                                                productListModel = new ProductListModel();
                                                productModel.setA_Id(cursor.getString(cursor.getColumnIndex("A_Id")));
                                                productModel.setBarcodes(cursor.getString(cursor.getColumnIndex("Barcodes")));
                                                productModel.setProductName(cursor.getString(cursor.getColumnIndex("ProductName")));
                                                productModel.setPTT(cursor.getString(cursor.getColumnIndex("PTT")));
                                                productModel.setSingleOffer(cursor.getString(cursor.getColumnIndex("SingleOffer")));
                                                productModel.setSize(cursor.getString(cursor.getColumnIndex("size")));
                                                productModel.setBrand(cursor.getString(cursor.getColumnIndex("Brand")));
                                                productModel.setCategory(cursor.getString(cursor.getColumnIndex("Category")));
                                                productModel.setSubCategory(cursor.getString(cursor.getColumnIndex("SubCategory")));
                                                selectedproductArraylist.add(productModel);
                                                productListModel.setProductListModels(selectedproductArraylist);

                                            } while (cursor.moveToNext());
                                            cursor.close();
                                        }
                                    }
                                    if (sale) {
                                        Intent intent = new Intent(StockActivity.this, SaleDetailsActivity.class);
                                        intent.putExtra("Salelist", productListModel);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(StockActivity.this, StockAllActivity.class);
                                        intent.putExtra("Stocklist", productListModel);
                                        startActivity(intent);
                                    }

                                }
                            }else{
                                cd.displayMessage("Please select Offer");
                            }

                       /* } else {
                            cd.displayMessage("Please select SubCategory");
                        }*/

                    /*} else {
                        cd.displayMessage("Please select Category");
                    }*/
                } else {
                    cd.displayMessage("Please select Brand");
                }
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

    @Override
    protected void onResume() {
        super.onResume();

        brand.setText("");
        //product_category.setText("");
        //product_subcategory.setText("");
        offerAuto.setText("");
        tl_productList.removeAllViews();

    }

   /* private void fetchCategoryDetails(String brandname) {
        try {
            listCategory = new ArrayList<>();
            String category = "";
            String Category = "Category";
            String where = " where Brand = " + "'" + brandname + "'";
            Cursor cursor = LOTUS.dbCon.fetchFromSelectDistinctWhere(Category, DbHelper.TABLE_MASTERSYNC, where);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    category = cursor.getString(cursor.getColumnIndex("Category"));
                    listCategory.add(category);
                } while (cursor.moveToNext());
                cursor.close();
            }
            Collections.sort(listCategory);
            if (listCategory.size() > 0) {
                strCategoryArray = new String[listCategory.size()];
                for (int i = 0; i < listCategory.size(); i++) {
                    strCategoryArray[i] = listCategory.get(i);
                }
            }
            if (listCategory != null && listCategory.size() > 0) {
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strCategoryArray) {
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
                product_category.setAdapter(adapter1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchSubCategoryDetails(String brandname,String categoryname) {
        try {
            listSubCategory = new ArrayList<>();
            String subcategory = "";
            String SubCategory = "SubCategory";
            String where = " where Category = " + "'" + categoryname + "' AND Brand = " + "'" + brandname + "'";
            Cursor cursor = LOTUS.dbCon.fetchFromSelectDistinctWhere(SubCategory, DbHelper.TABLE_MASTERSYNC, where);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    subcategory = cursor.getString(cursor.getColumnIndex("SubCategory"));
                    listSubCategory.add(subcategory);
                } while (cursor.moveToNext());
                cursor.close();
            }
            Collections.sort(listSubCategory);
            if (listSubCategory.size() > 0) {
                strSubCategoryArray = new String[listSubCategory.size()];
                for (int i = 0; i < listSubCategory.size(); i++) {
                    strSubCategoryArray[i] = listSubCategory.get(i);
                }
            }
            if (listSubCategory != null && listSubCategory.size() > 0) {
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strSubCategoryArray) {
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
                product_subcategory.setAdapter(adapter1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

   /* private void fetchOfferDetails(String brandname,String categoryname,String subcategoryname) {
        try {
            listoffer = new ArrayList<>();
            String offer = "";
            String Offer = "SingleOffer";
            String where = " where SubCategory = " + "'" + subcategoryname + "' AND Brand = " + "'" + brandname + "' AND Category = " + "'" + categoryname + "'";
            Cursor cursor = LOTUS.dbCon.fetchFromSelectDistinctWhere(Offer, DbHelper.TABLE_MASTERSYNC, where);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    offer = cursor.getString(cursor.getColumnIndex("SingleOffer"));
                    listoffer.add(offer);
                } while (cursor.moveToNext());
                cursor.close();
            }
            Collections.sort(listoffer);
            if (listoffer.size() > 0) {
                strOfferArray = new String[listoffer.size()];
                for (int i = 0; i < listoffer.size(); i++) {
                    strOfferArray[i] = listoffer.get(i);
                }
            }
            if (listoffer != null && listoffer.size() > 0) {
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strOfferArray) {
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
                offerAuto.setAdapter(adapter1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void fetchOfferDetails(String brandname) {
        try {
            listoffer = new ArrayList<>();
            String offer = "";
            String Offer = "SingleOffer";
            String where = " where Brand = " + "'" + brandname + "'";
            Cursor cursor = LOTUS.dbCon.fetchFromSelectDistinctWhere(Offer, DbHelper.TABLE_MASTERSYNC, where);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    offer = cursor.getString(cursor.getColumnIndex("SingleOffer"));
                    listoffer.add(offer);
                } while (cursor.moveToNext());
                cursor.close();
            }
            Collections.sort(listoffer);
            if (listoffer.size() > 0) {
                strOfferArray = new String[listoffer.size()];
                for (int i = 0; i < listoffer.size(); i++) {
                    strOfferArray[i] = listoffer.get(i);
                }
            }
            if (listoffer != null && listoffer.size() > 0) {
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strOfferArray) {
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
                offerAuto.setAdapter(adapter1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* private void getproductDetailsforSale(String Offername,String Subcategoryname,String Categoryname,String Brandname){
        try {
            productDetailsArraylist = new ArrayList<>();

            String where = " where SingleOffer = " + "'" + Offername + "' AND SubCategory = " + "'" + Subcategoryname +
                    "' AND Category = " + "'" + Categoryname + "' AND Brand = " + "'" + Brandname + "'";
            //Cursor cursor = LOTUS.dbCon.fetchFromSelectDistinctWheremultiplecolumn(A_Id, Barcodes, ProductName, PTT, SingleOffer, size, DbHelper.TABLE_MASTERSYNC, where);
            Cursor cursor = LOTUS.dbCon.fetchFromSelect(DbHelper.TABLE_STOCK,where);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    productModel = new ProductModel();
                    productModel.setA_Id(cursor.getString(cursor.getColumnIndex("A_id")));
                    productModel.setBarcodes(cursor.getString(cursor.getColumnIndex("Barcodes")));
                    productModel.setProductName(cursor.getString(cursor.getColumnIndex("ProductName")));
                    productModel.setPTT(cursor.getString(cursor.getColumnIndex("PTT")));
                    productModel.setSize(cursor.getString(cursor.getColumnIndex("size")));
                    productModel.setBrand(cursor.getString(cursor.getColumnIndex("Brand")));
                    productModel.setCategory(cursor.getString(cursor.getColumnIndex("Category")));
                    productModel.setSubCategory(cursor.getString(cursor.getColumnIndex("SubCategory")));
                    productModel.setSingleOffer(cursor.getString(cursor.getColumnIndex("SingleOffer")));
                    productDetailsArraylist.add(productModel);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

    private void getproductDetailsforSale(String Offername,String Brandname){
        try {
            productDetailsArraylist = new ArrayList<>();

            String where = " where SingleOffer = " + "'" + Offername + "' AND Brand = " + "'" + Brandname + "'";
            Cursor cursor = LOTUS.dbCon.fetchFromSelect(DbHelper.TABLE_STOCK,where);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    productModel = new ProductModel();
                    productModel.setA_Id(cursor.getString(cursor.getColumnIndex("A_id")));
                    productModel.setBarcodes(cursor.getString(cursor.getColumnIndex("Barcodes")));
                    productModel.setProductName(cursor.getString(cursor.getColumnIndex("ProductName")));
                    productModel.setPTT(cursor.getString(cursor.getColumnIndex("PTT")));
                    productModel.setSize(cursor.getString(cursor.getColumnIndex("size")));
                    productModel.setBrand(cursor.getString(cursor.getColumnIndex("Brand")));
                    productModel.setCategory(cursor.getString(cursor.getColumnIndex("Category")));
                    productModel.setSubCategory(cursor.getString(cursor.getColumnIndex("SubCategory")));
                    productModel.setSingleOffer(cursor.getString(cursor.getColumnIndex("SingleOffer")));
                    productDetailsArraylist.add(productModel);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /*private void getproductsDetails(String Offername,String Subcategoryname,String Categoryname,String Brandname) {
        try {
            productDetailsArraylist = new ArrayList<>();

            String A_Id = "A_Id";
            String Barcodes = "Barcodes";
            String ProductName = "ProductName";
            String PTT = "PTT";
            String SingleOffer = "SingleOffer";
            String size = "size";
            String where = " where SingleOffer = " + "'" + Offername + "' AND SubCategory = " + "'" + Subcategoryname + "' AND Category = " + "'" + Categoryname + "' AND Brand = " + "'" + Brandname + "'";
            Cursor cursor = LOTUS.dbCon.fetchFromSelectDistinctWheremultiplecolumn(A_Id, Barcodes, ProductName, PTT, SingleOffer, size, DbHelper.TABLE_MASTERSYNC, where);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    productModel = new ProductModel();
                    productModel.setA_Id(cursor.getString(cursor.getColumnIndex("A_Id")));
                    productModel.setBarcodes(cursor.getString(cursor.getColumnIndex("Barcodes")));
                    productModel.setProductName(cursor.getString(cursor.getColumnIndex("ProductName")));
                    productModel.setPTT(cursor.getString(cursor.getColumnIndex("PTT")));
                    productModel.setSingleOffer(cursor.getString(cursor.getColumnIndex("SingleOffer")));
                    productModel.setSize(cursor.getString(cursor.getColumnIndex("size")));
                    productDetailsArraylist.add(productModel);
                } while (cursor.moveToNext());
                cursor.close();
            }
            if (productDetailsArraylist.size() > 0) {
                //strMrpArray = new String[productDetailsArraylist.size()];
                for (int i = 0; i < productDetailsArraylist.size(); i++) {
                    View tr = (TableRow) View.inflate(StockActivity.this, R.layout.inflate_stocksale_row, null);

                     cb = (CheckBox) tr.findViewById(R.id.chck_product);

                     spin = (Spinner) tr.findViewById(R.id.spin_mrp);

                    productModel = productDetailsArraylist.get(i);

                    cb.setText(productModel.getProductName());

                    //strMrpArray[i] = productModel.getPTT();

                    String mrp = productModel.getPTT();

                    String mrps[] = new String[]{mrp};

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(StockActivity.this, android.R.layout.simple_spinner_item, mrps);

                    spin.setAdapter(adapter);

                    tl_productList.addView(tr);

                }
            }
                View tr1 = (TableRow) View.inflate(StockActivity.this, R.layout.inflate_stocksale_row, null);
                CheckBox cb = (CheckBox) tr1.findViewById(R.id.chck_product);

                Spinner spin = (Spinner) tr1.findViewById(R.id.spin_mrp);

                tr1.setVisibility(View.INVISIBLE);

                tl_productList.addView(tr1);

        }catch (Exception e){
            e.printStackTrace();
        }

    }*/

    private void getproductsDetails(String Offername,String Brandname) {
        try {
            productDetailsArraylist = new ArrayList<>();

            String A_Id = "A_Id";
            String Barcodes = "Barcodes";
            String ProductName = "ProductName";
            String PTT = "PTT";
            String SingleOffer = "SingleOffer";
            String size = "size";
            String where = " where SingleOffer = " + "'" + Offername + "' AND Brand = " + "'" + Brandname + "'";
            Cursor cursor = LOTUS.dbCon.fetchFromSelectDistinctWheremultiplecolumn(A_Id, Barcodes, ProductName, PTT, SingleOffer, size, DbHelper.TABLE_MASTERSYNC, where);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    productModel = new ProductModel();
                    productModel.setA_Id(cursor.getString(cursor.getColumnIndex("A_Id")));
                    productModel.setBarcodes(cursor.getString(cursor.getColumnIndex("Barcodes")));
                    productModel.setProductName(cursor.getString(cursor.getColumnIndex("ProductName")));
                    productModel.setPTT(cursor.getString(cursor.getColumnIndex("PTT")));
                    productModel.setSingleOffer(cursor.getString(cursor.getColumnIndex("SingleOffer")));
                    productModel.setSize(cursor.getString(cursor.getColumnIndex("size")));
                    productDetailsArraylist.add(productModel);
                } while (cursor.moveToNext());
                cursor.close();
            }
            if (productDetailsArraylist.size() > 0) {
                //strMrpArray = new String[productDetailsArraylist.size()];
                for (int i = 0; i < productDetailsArraylist.size(); i++) {
                    View tr = (TableRow) View.inflate(StockActivity.this, R.layout.inflate_stocksale_row, null);

                    cb = (CheckBox) tr.findViewById(R.id.chck_product);

                    spin = (Spinner) tr.findViewById(R.id.spin_mrp);

                    productModel = productDetailsArraylist.get(i);

                    cb.setText(productModel.getProductName());

                    //strMrpArray[i] = productModel.getPTT();

                    String mrp = productModel.getPTT();

                    String mrps[] = new String[]{mrp};

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(StockActivity.this, android.R.layout.simple_spinner_item, mrps);

                    spin.setAdapter(adapter);

                    tl_productList.addView(tr);

                }
            }
            View tr1 = (TableRow) View.inflate(StockActivity.this, R.layout.inflate_stocksale_row, null);
            CheckBox cb = (CheckBox) tr1.findViewById(R.id.chck_product);

            Spinner spin = (Spinner) tr1.findViewById(R.id.spin_mrp);

            tr1.setVisibility(View.INVISIBLE);

            tl_productList.addView(tr1);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void fetchBrandDetails() {
        try {
            listBrand = new ArrayList<>();
            String brand = "Brand";
            Cursor cursor = LOTUS.dbCon.fetchFromSelectDistinct(brand, DbHelper.TABLE_MASTERSYNC);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    String brandname = cursor.getString(cursor.getColumnIndex("Brand"));
                    listBrand.add(brandname);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
