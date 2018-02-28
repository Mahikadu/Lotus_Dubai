package com.prod.sudesi.lotusherbalsdubai.Dbconfig;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

/**
 * Created by Admin on 27-10-2016.
 */

public class DataBaseCon {

    private static DbHelper dbHelper;
    private static DataBaseCon dbConInstance = null;
    private Context context;
    private static SQLiteDatabase db;

    private DataBaseCon(Context context) {
        try {
            this.context = context;
            open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static synchronized DataBaseCon getInstance(Context ctx) {

        try {
            if (dbConInstance == null) {
                dbConInstance = new DataBaseCon(ctx);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbConInstance;
    }

    public DataBaseCon open() {
        try {
            dbHelper = DbHelper.getInstance(context);
            dbHelper.open();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void closeCursor(Cursor cursor) {
        dbHelper.closeCursor(cursor);
    }


    public long insert(String tbl, String values[], String names[]) {
        return dbHelper.insert(values, names, tbl);
    }

    public long insert1(String tbl, String values[], String names[]) {
        return dbHelper.insert1(values, names, tbl);
    }

    public Cursor fetch(String tbl, String names[], String where, String args[], String order,
                        String limit, boolean isDistinct, String groupBy, String having) {

        return dbHelper.fetch(tbl, names, where, args, order, limit, isDistinct, groupBy, having);
    }

    public Cursor fetchLastRow(String tbl, String order, String args[]) {

        return dbHelper.fetchLastRow(tbl, order, args);
    }

    public Cursor fetchFromSelect(String tbl, String where) {
        String query = null;
        try {
            query = "select * from " + tbl + where;
            Log.i("TAG", "query :" + query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbHelper.rawQuery(query);
    }

    public Cursor fetchFromSaleDetails(String tbl, String offer, String brand) {
        String query = null;
        try {
            query = "select * from " + tbl + " t inner join (select A_id, max(insert_date) as insert_date from "
                    + tbl + " group by A_id) tm on t.A_id = tm.A_id AND t.insert_date = tm.insert_date  AND SingleOffer = '"
                    + offer + "' AND Brand = '" + brand + "'";
            Log.i("TAG", "query :" + query);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dbHelper.rawQuery(query);
    }

    public Cursor fetchFromSelectDistinctWhere(String colName, String tbl, String where) {
        String query = null;
        try {
            query = "select distinct " + colName + " from " + tbl + where;
            Log.i("TAG", "query :" + query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbHelper.rawQuery(query);
    }

    public Cursor fetchFromSelectDistinctWheremultiplecolumn(String colName1, String colName2, String colName3,
                                                             String colName4, String colName5, String colName6,
                                                             String colName7, String tbl, String where) {
        String query = null;
        try {
            query = "select distinct " + colName1 + ", " + colName2 + ", " + colName3 + ", " + colName4 + ", " + colName5 + ", " + colName6 + ", " + colName7 + " from " + tbl + where;
            Log.i("TAG", "query :" + query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbHelper.rawQuery(query);
    }

    public Cursor fetchAllproductslistforstock(String Brand,
                                               String offer) {
        // TODO Auto-generated method stub

        String selectquery = "select distinct(ProductName) from table_master_sync where Brand = " + "'" + Brand + "'" + " AND SingleOffer = " + "'" + offer + "' ORDER BY order_flag ";

        Cursor cursort = dbHelper.rawQuery(selectquery);


        return cursort;

    }

    public String check_password_from_db(String username, String str_old_password) {
        // TODO Auto-generated method stub

        Cursor c;
        String count = "0";

        String sql = "select * from table_login where username = '" + username + "' and  password= '" + str_old_password + "'";
        c = dbHelper.rawQuery(sql);
        if (c != null)
            if (c.getCount() > 0 && c.moveToFirst()) {


                count = "1";

            }

        return count;
    }


    public Cursor fetchallSpecifyMSelect(String Productname, String Brand, String offer) {

        String query = null;
        try {
            query = "select * from table_master_sync where ProductName = " + "'" + Productname + "'" + " AND Brand = " + "'" + Brand + "'" + " AND SingleOffer = " + "'" + offer + "'";
            Log.i("TAG", "query :" + query);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dbHelper.rawQuery(query);

    }


    public Cursor fetchFromSelectDistinct(String colName, String tbl) {
        String query = null;
        try {
            query = "select distinct " + colName + " from " + tbl;
            Log.i("TAG", "query :" + query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbHelper.rawQuery(query);
    }

    public Cursor fetchonestock(CharSequence db_id, String outletcode) {
        String sql = "SELECT DISTINCT opening_stock, sold_stock, total_gross_amount, total_net_amount, discount FROM table_stock WHERE A_id = '" + db_id + "' and outletcode ='" + outletcode + "'";
        Cursor c = dbHelper.rawQuery(sql);
        return c;

    }

    public Cursor fetchonestockmultplecolumn(CharSequence db_id, String outletcode) {

        String sql = "Select total_gross_amount,total_net_amount,discount,close_bal,sold_stock,opening_stock,stock_received,stock_in_hand FROM table_stock t inner join (select max(insert_date) as insert_date from table_stock group by A_id) tm on t.insert_date = tm.insert_date  AND A_id = '"
                + db_id + "' AND outletcode = '" + outletcode + "'";

        //String sql="SELECT DISTINCT total_gross_amount,total_net_amount,discount,close_bal,sold_stock,opening_stock,stock_received,stock_in_hand FROM table_stock WHERE A_id = '" + db_id + "' and outletcode ='" + outletcode + "'";
        Cursor c = dbHelper.rawQuery(sql);
        return c;

    }

    public Cursor getstockdata(String d_id, String outletcode) {
        // TODO Auto-generated method stub
        //String sql = "select * from stock where " + " db_id ="+"'"+d_id+"'";

        String sql = "select * from table_stock where outletcode = " + "'" + outletcode + "'" + " and A_id=" + "'" + d_id + "'";

        Cursor c = dbHelper.rawQuery(sql);

        return c;
    }


    public Cursor fetchTwoFromTable(String colName1, String colName2, String tbl, String where) {
        String query = null;
        try {
            query = "select " + colName1 + ", " + colName2 + " from " + tbl + where;
            Log.i("TAG", "query :" + query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbHelper.rawQuery(query);
    }

    public Cursor fetchAlldata(String tbl) {
        String query = null;
        try {
            query = "select * from " + tbl;

            Log.i("TAG", "query :" + query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbHelper.rawQuery(query);
    }

    public void updateOutletStatus(long rowid) {
        int id = (int) rowid;
        String sql = "UPDATE outlet_attendance SET outletstatus = 'DeActive' WHERE id != '" + id + "'";
        Log.e("local database", "sql=" + sql);
        db.execSQL(sql);

    }

    public Cursor fetchNotificationdata(String tbl) {
        String query = null;
        try {
            query = "select * from table_Notification";

            Log.i("TAG", "query :" + query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbHelper.rawQuery(query);
    }

    public String fetchStockDbID(String productName, String mrp, String offer) {

        String sql = "select A_Id from table_master_sync where ProductName like '%" + productName + "%' and PTT = '" + mrp + "' and SingleOffer ='" + offer + "'";

        Log.e("sql", sql);

        String result = "";

        Cursor c = dbHelper.rawQuery(sql);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            result = c.getString(0);


        }

        return result;


    }

    public String fetchStockDbIDNew(String ShortName, String mrp, String brand) {

        String sql = "select A_Id from table_master_sync where ShortName like '%" + ShortName + "%' and PTT = '" + mrp + "' and Brand ='" + brand + "'";

        Log.e("sql", sql);

        String result = "";

        Cursor c = dbHelper.rawQuery(sql);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            result = c.getString(0);


        }

        return result;


    }

    public String getActiveoutletCode() {
        // TODO Auto-generated method stub
        String outletcode = "";
        String selectquery = "select outletcode from outlet_attendance where outletstatus = 'Active'";
        Cursor cursor = dbHelper.rawQuery(selectquery);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    outletcode = cursor.getString(cursor.getColumnIndex("outletcode"));
                } while (cursor.moveToNext());

            }
        } else {
            outletcode = "";
        }
        return outletcode;
    }


    public Cursor fetchAll2(String Category, String where1, String tbl) {
        String query = null;
        try {
            //    query = "select *  from table_M_Category where  trim(Produt_type_id) ='1014'";

            query = "select * from table_M_Category where trim(Category) ='" + Category + "'";

            //    query = "select * from table_M_Category where trim(Subcategory) ='"+colName+"'";

            Log.i("TAG", "query :" + query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbHelper.rawQuery(query);
    }

    public Cursor fetchAll(String tbl) {
        String query = null;
        try {
            query = "select * from " + tbl;
            Log.i("TAG", "query :" + query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dbHelper.rawQuery(query);
    }

    public Cursor fetchallSpecify(String tbl, String names[], String fName,
                                  String fValue, String order) {
        return dbHelper.fetchallSpecify(tbl, names, fName, fValue, order);

    }

    public Cursor getStockdetails(String outletcode) {
        // TODO Auto-generated method stub
        Cursor stockddetails = null;
        try {

            Log.e("", "getStockdetails==");
            String sql = "select * from table_stock where savedServer = '0' And outletcode = '" + outletcode + "'";

            stockddetails = dbHelper.rawQuery(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return stockddetails;

    }


    public int getCountOfRows(String tbl) {
        int count = 0;
        try {
            String query = "select * from " + tbl;
            count = 0;
            count = (int) dbHelper.getCountOfRows(tbl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public int checkcount(String username, String pass) {
        // TODO Auto-generated method stub
        Cursor c;
        int count = 0;

        String sql = "select username,password from table_login where username = '" + username + "' and password ='" + pass + "'";
        c = dbHelper.rawQuery(sql);
        if (c != null && c.moveToFirst()) {
            count = c.getCount();
        }
        return count;
    }

    public Cursor getAllDataFromTable(String tbl) {
        Cursor cursor = null;
        try {
            String query = "select * from " + tbl;
            cursor = dbHelper.rawQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

    public boolean delete(String tbl, String where, String args[]) {
        return dbHelper.delete(tbl, where, args);
    }

    public boolean update(String tbl, String where, String values[], String names[], String args[]) {
        return dbHelper.update(where, values, names, tbl, args);
    }


    public boolean updateBulk(String tbl, String where, String values[], String names[], String args[]) {
        return dbHelper.updateBulk(where, values, names, tbl, args);
    }

    public boolean alterTable(String tbl) {
        return dbHelper.alterTable(tbl);
    }


    public SQLiteStatement beginDBTransaction(String tableName, String names[]) {
        SQLiteStatement statement = null;
        try {
            statement = null;
            statement = dbHelper.beginDBTransaction(tableName, names);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statement;
    }

    public void beginDBTransaction() {

        dbHelper.beginDBTransaction();
    }

    public void endDBTransaction() {

        dbHelper.endDBTransaction();
    }

    public void dbTransactionSucessFull() {
        dbHelper.dbTransactionSucessFull();
    }

    public void updateServerStatus(String status) {
        dbHelper.updateServerStatus(status);
    }

    public int getCountOfRows(String table, String whereClause, String[] whereArgs) {
        int count = 0;
        try {
            String query = "select * from " + table;
            count = 0;
            count = (int) dbHelper.getCountOfRows(table, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public String getdatepresentorabsent(String currentdate, String username) {
        // TODO Auto-generated method stub
        Log.v("", "in finding p or a");
        String a = "";
        String sql = "select attendance from attendance where emp_id = " + "'" + username + "'" + " and Adate LIKE " + "'%" + currentdate + "%'";

        Log.v("", "sql==" + sql);
        Cursor c = dbHelper.rawQuery(sql);

        if (c != null) {
            c.moveToFirst();
            if (c.getCount() > 0) {

                a = c.getString(c.getColumnIndex("attendance"));
            }
        }


        return a;
    }

    public Cursor getpreviousData(String ystdate, String username) {
        // TODO Auto-generated method stub

        String sql = "select * from attendance where emp_id = " + "'" + username + "'" + " and Adate LIKE " + "'%" + ystdate + "%'";

        Log.v("", "sql==" + sql);
        Cursor c = dbHelper.rawQuery(sql);

        return c;
    }

    public int updateAttendance(String wheredate, String BA_id, String logoutDate) {

        int status;

        String sql = "update attendance set logout_date = '" + logoutDate + "' ,logout_status ='OUT' where Adate LIKE '%" + wheredate + "%' and emp_id ='" + BA_id + "'";

        Cursor c = dbHelper.rawQuery(sql);
        if (c != null && c.getCount() > 0) {
            status = 1;
        } else {
            status = 0;
        }
        return status;

    }

    public Cursor getAttendanceData() {
        // TODO Auto-generated method stub

        String sql = "select * from attendance where savedServer = '0'";

        Cursor c = dbHelper.rawQuery(sql);

        return c;
    }

    public void update_Attendance_data(String id) {
        // TODO Auto-generated method stub

        String sql = "UPDATE attendance SET savedServer = '1' WHERE savedServer = '0' AND id= '" + id + "'";
        db.execSQL(sql);
        Log.e("local database", "UPDATE attendance table data");

    }

    public String isholiday(String selecteddate) {
        // TODO Auto-generated method stub

        String desc = "";
        String selectquery = "select  *  from attendance where Adate Like '%" + selecteddate + "%'" + " and attendance = 'H'";

        Cursor cursor = dbHelper.rawQuery(selectquery);
        Log.v("", "currosr.count---" + cursor.getCount());
        if (cursor != null) {

            if (cursor.moveToFirst()) {

                do {
                    desc = cursor.getString(cursor.getColumnIndex("holiday_desc"));

                } while (cursor.moveToNext());

            }


        }

        return desc;

    }

    public void updateChangepassword(String username, String password) {
        // TODO Auto-generated method stub

        String sql = "update table_login set password = '" + password + "' where username='" + username + "'";
        Log.v("", "sql=" + sql);

        db.execSQL(sql);

        // and password ='password'


    }

    public String getLastLogintime() {
        // TODO Auto-generated method stub

        Cursor c;
        String LastLoginTime = "";

        String sql = "select created_date from table_login"; //15.05.2015

        c = dbHelper.rawQuery(sql);

        if (c != null) {
            Log.e("", "getCount=" + c.getCount());
            if (c.getCount() > 0) {

                c.moveToFirst();


                LastLoginTime = c.getString(c.getColumnIndex("created_date"));

                Log.v("", "last login time=" + LastLoginTime);

            }
        } else {

            LastLoginTime = "";
        }
        return LastLoginTime;
    }
}
