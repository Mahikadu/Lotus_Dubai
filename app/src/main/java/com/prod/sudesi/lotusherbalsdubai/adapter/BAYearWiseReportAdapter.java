package com.prod.sudesi.lotusherbalsdubai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.prod.sudesi.lotusherbalsdubai.Models.BAYearWiseModel;
import com.prod.sudesi.lotusherbalsdubai.R;

import java.util.List;

/**
 * Created by Admin on 06-11-2017.
 */

public class BAYearWiseReportAdapter extends BaseAdapter {

    Context mContext;
    private List<BAYearWiseModel> bayearReportDetailsArraylist;



    public BAYearWiseReportAdapter(Context mContext, List<BAYearWiseModel> bayearwisereportlist) {

        this.mContext = mContext;
        this.bayearReportDetailsArraylist = bayearwisereportlist;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return bayearReportDetailsArraylist.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return bayearReportDetailsArraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        convertView = null;
        if(convertView==null){
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.layout_ba_year_wise_report, null);



            TextView txtmonthp = (TextView)convertView.findViewById(R.id.txtmonthp);
            TextView txtnetamtp =(TextView)convertView.findViewById(R.id.txtnetamtp);
            TextView txtgrowthp =(TextView)convertView.findViewById(R.id.txtgrowthp);
            TextView txtmonthc = (TextView)convertView.findViewById(R.id.txtmonthc);
            TextView txtnetamtc = (TextView)convertView.findViewById(R.id.txtnetamtc);
            TextView txtgrowthc = (TextView)convertView.findViewById(R.id.txtgrowthc);


            BAYearWiseModel baYearWiseModel = bayearReportDetailsArraylist.get(position);

            txtmonthp.setText(baYearWiseModel.getYears_MonthsP());
            txtnetamtp.setText(baYearWiseModel.getNetAmountPSkin());
            txtgrowthp.setText(baYearWiseModel.getGrowthPSkin());
            txtmonthc.setText(baYearWiseModel.getYears_MonthsC());
            txtnetamtc.setText(baYearWiseModel.getNetAmountCSkin());
            txtgrowthc.setText(baYearWiseModel.getGrowthCSkin());


        }
        return convertView;

    }
}
