package com.prod.sudesi.lotusherbalsdubai.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.prod.sudesi.lotusherbalsdubai.Models.ServerAttendanceModel;
import com.prod.sudesi.lotusherbalsdubai.R;

import java.util.List;

/**
 * Created by Admin on 07-11-2017.
 */

public class AttendanceAdapter extends BaseAdapter {

    Context mContext;
    private List<ServerAttendanceModel> serverAttendanceArraylist;



    public AttendanceAdapter(Context mContext, List<ServerAttendanceModel> serverAttendancelist) {

        this.mContext = mContext;
        this.serverAttendanceArraylist = serverAttendancelist;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return serverAttendanceArraylist.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return serverAttendanceArraylist.get(position);
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

            convertView = mInflater.inflate(R.layout.inflate_attendance_row, null);



            TextView txtattdate = (TextView)convertView.findViewById(R.id.txtattdate);
            TextView txtattattendance =(TextView)convertView.findViewById(R.id.txtattattendance);
            TextView txtattabsenttype =(TextView)convertView.findViewById(R.id.txtattabsenttype);

            ServerAttendanceModel serverAttendanceModel = serverAttendanceArraylist.get(position);

            txtattdate.setText(serverAttendanceModel.getADatenew());
            txtattattendance.setText(serverAttendanceModel.getAttendance());
            txtattabsenttype.setText(serverAttendanceModel.getAbsentType());


        }
        return convertView;

    }
}
