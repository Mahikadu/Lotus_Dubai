package com.prod.sudesi.lotusherbalsdubai.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.prod.sudesi.lotusherbalsdubai.Models.StockReportModel;
import com.prod.sudesi.lotusherbalsdubai.R;

import java.util.List;


public class ReportAdapter extends BaseAdapter {

	Context mContext;
	private List<StockReportModel> stockReportDetailsArraylist;


	
	public ReportAdapter(Context mContext, List<StockReportModel> stockReportModels) {

		this.mContext = mContext;
		this.stockReportDetailsArraylist = stockReportModels;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return stockReportDetailsArraylist.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return stockReportDetailsArraylist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = mInflater.inflate(R.layout.activity_stockreport_item, null);



			TextView A_id = (TextView)convertView.findViewById(R.id.txtaid);
			TextView Brand =(TextView)convertView.findViewById(R.id.txtbrand);
			TextView Category =(TextView)convertView.findViewById(R.id.txtcat);
			TextView SubCategory = (TextView)convertView.findViewById(R.id.txtsubcat);
			TextView ProductName = (TextView)convertView.findViewById(R.id.txtproduct);
			TextView size = (TextView)convertView.findViewById(R.id.txtsize);
			TextView PTT = (TextView)convertView.findViewById(R.id.txtptt);
			TextView opening_stock = (TextView)convertView.findViewById(R.id.txtopening);
			TextView stock_received =(TextView)convertView.findViewById(R.id.txtreceive);
			TextView stock_in_hand=(TextView)convertView.findViewById(R.id.txtinhand);
			TextView sold_stock = (TextView)convertView.findViewById(R.id.txtsold);
			TextView close_bal = (TextView)convertView.findViewById(R.id.txtclosing);
			TextView total_gross_amount =(TextView)convertView.findViewById(R.id.txttotal);
			TextView discount = (TextView)convertView.findViewById(R.id.txtdiscount);
			TextView total_net_amount = (TextView)convertView.findViewById(R.id.txtnetamt);
			TextView outletcode=(TextView)convertView.findViewById(R.id.txtoutlet);
			TextView tv_status = (TextView)convertView.findViewById(R.id.txtstatus);


			StockReportModel stockReportModels = stockReportDetailsArraylist.get(position);

			A_id.setText(stockReportModels.getA_id());
			Brand.setText(stockReportModels.getBrand());
			Category.setText(stockReportModels.getCategory());
			SubCategory.setText(stockReportModels.getSubCategory());
			ProductName.setText(stockReportModels.getProductName());
			size.setText(stockReportModels.getSize());
			PTT.setText(stockReportModels.getPTT());
			opening_stock.setText(stockReportModels.getOpening_stock());
			stock_received.setText(stockReportModels.getStock_received());
			stock_in_hand.setText(stockReportModels.getStock_in_hand());
			sold_stock.setText(stockReportModels.getSold_stock());
			close_bal.setText(stockReportModels.getClose_bal());
			total_gross_amount.setText(stockReportModels.getTotal_gross_amount());
			discount.setText(stockReportModels.getDiscount());
			total_net_amount.setText(stockReportModels.getTotal_net_amount());
			outletcode.setText(stockReportModels.getOutletcode());

			if(stockReportModels.getSaveServer().equalsIgnoreCase("0"))
			{
				Log.e("---", "NU");
				tv_status.setText("NU");
			}
			else if(stockReportModels.getSaveServer().equalsIgnoreCase("1"))
			{
				Log.e("---", "U");
				tv_status.setText("U");
			}



		}
		return convertView;
		
	}

}
