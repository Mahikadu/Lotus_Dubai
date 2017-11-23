package com.prod.sudesi.lotusherbalsdubai.libs;

import android.content.Context;
import android.util.Log;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class LotusWebservice {

	Context context;

	//String url = "http://192.168.0.141/LotusDubaiWcf/Service1.svc";//New UAT Link Oct-25-2017(Sadhana pc)

	String url = "http://ifli-lms-wcf.smartforcecrm.com/Service1.svc";//UAT Link Dubai nov-8-2017


	public LotusWebservice(Context con) {
		context = con;
	}

	public SoapObject GetLogin(String EmpId, String Password,String UID, String version) {
		SoapObject result = null;
		try {
			SoapObject request = new SoapObject("http://tempuri.org/","Get_login");
			// /// send link
			request.addProperty("bacode", EmpId);
			request.addProperty("Password", Password);
			request.addProperty("Android_Uid", UID);
			request.addProperty("version", version);

			Log.e("REQUEST", request.toString());

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);// soap envelop with version
			envelope.setOutputSoapObject(request); // set request object
			envelope.dotNet = true;

			HttpTransportSE androidHttpTransport = new HttpTransportSE(url,6000);// http transport call
			androidHttpTransport.call("http://tempuri.org/IService1/Get_login",envelope);

			result = (SoapObject) envelope.getResponse();

			Log.e("GetLogin=", result.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public SoapPrimitive GetServerDate() {
		SoapPrimitive result = null;
		try {
			SoapObject request = new SoapObject("http://tempuri.org/",
					"GetServerDate");
			// /// send link

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);// soap envelop with version
			envelope.setOutputSoapObject(request); // set request object
			envelope.dotNet = true;

			HttpTransportSE androidHttpTransport = new HttpTransportSE(url,6000);// http
			// transport
			// call
			androidHttpTransport.call(
					"http://tempuri.org/IService1/GetServerDate", envelope);

			result = (SoapPrimitive) envelope.getResponse();

			Log.e("GetServerDate=", result.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public SoapPrimitive Logout(String baCode,String Logout_time)
	{
		SoapPrimitive result = null;
		try
		{

			SoapObject request = new SoapObject("http://tempuri.org/", "Logout");

			request.addProperty("bacode", baCode);
			request.addProperty("Logout", Logout_time);


			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);// soap envelop with version
			envelope.setOutputSoapObject(request); // set request object
			envelope.dotNet = true;

			HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
			// transport
			// call
			androidHttpTransport.call(
					"http://tempuri.org/IService1/Logout", envelope);

			result = (SoapPrimitive) envelope.getResponse();

			Log.e("Logout", result.toString());

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;

	}

	public SoapPrimitive SaveAttendance(String empid, String date,
										String attend, String absent_type, String lat, String lon) {
		SoapPrimitive result = null;
		try {
			Log.v("", "attendace service called");
			SoapObject request = new SoapObject("http://tempuri.org/",
					"SaveAttendance");

			request.addProperty("bacode", empid);
			request.addProperty("ADate", date);
			request.addProperty("Attendance", attend);
			request.addProperty("AbsentType", absent_type);
			request.addProperty("LAT", lat);
			request.addProperty("LON", lon);

			Log.e("AttendanceValues", empid + "---" + date + "---" + attend
					+ "---" + absent_type + "---" + lat + "---" + lon);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);// soap envelop with version
			envelope.setOutputSoapObject(request); // set request object
			envelope.dotNet = true;

			HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
																			// transport
																			// call
			androidHttpTransport.call(
					"http://tempuri.org/IService1/SaveAttendance", envelope);

			result = (SoapPrimitive) envelope.getResponse();

			Log.e("SaveAttendance", result.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	public SoapPrimitive ChangePassword(String usercode, String password) {
		SoapPrimitive result = null;

		try {
			Log.v("", "username=" + usercode);
			Log.v("", "password=" + password);

			SoapObject request = new SoapObject("http://tempuri.org/",
					"SaveNewPassword");// soap object
			request.addProperty("username", usercode);
			request.addProperty("password", password);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);// soap envelop with version
			envelope.setOutputSoapObject(request); // set request object
			envelope.dotNet = true;

			HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
			// transport
			// call
			androidHttpTransport.call(
					"http://tempuri.org/IService1/SaveNewPassword", envelope);
			// response soap object
			result = (SoapPrimitive) envelope.getResponse();

			if (result != null) {
				// Log.e("syncLoginTable","Count -- " +
				// String.valueOf(result.getPropertyCount()));
				Log.v("", "CHANGE PASSWORD" + result.toString());
			}

			else {
				Log.e("CHANGE PASSWORD", "NULL");
			}

			return result;

		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	public SoapObject MasterSync(String bacode) {
		SoapObject result = null;
		try {
			SoapObject request = new SoapObject("http://tempuri.org/","MasterSync");
			// /// send link
			request.addProperty("bacode", bacode);

			Log.e("REQUEST", request.toString());

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);// soap envelop with version
			envelope.setOutputSoapObject(request); // set request object
			envelope.dotNet = true;

			HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http transport call
			androidHttpTransport.call("http://tempuri.org/IService1/MasterSync",envelope);

			result = (SoapObject) envelope.getResponse();

			Log.e("MasterSync=", result.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public SoapObject GetOutlet(String bacode)
	{
		SoapObject result = null;
		try {

			SoapObject request = new SoapObject("http://tempuri.org/",
					"GetOutlet");

			request.addProperty("bacode", bacode);

			Log.e("Request", request.toString());

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);// soap envelop with version
			envelope.setOutputSoapObject(request); // set request object
			envelope.dotNet = true;

			HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
			// transport
			// call
			androidHttpTransport.call("http://tempuri.org/IService1/GetOutlet", envelope);

			//androidHttpTransport.getServiceConnection().disconnect();  //23.04.2015

			result = (SoapObject) envelope.getResponse();

			Log.e("GetOutlet", result.toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

	public SoapPrimitive SaveOutletAttendance(String bacode, String ADate, String LAT, String LON, String OutletCode)
	{
		SoapPrimitive result = null;
		try
		{

			SoapObject request = new SoapObject("http://tempuri.org/", "SaveOutletAttendance");

			request.addProperty("bacode", bacode);
			request.addProperty("ADate", ADate);
			request.addProperty("LAT", LAT);
			request.addProperty("LON", LON);
			request.addProperty("OutletCode", OutletCode);

			Log.e("SaveOutletAttendance", request.toString());

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);// soap envelop with version
			envelope.setOutputSoapObject(request); // set request object
			envelope.dotNet = true;

			HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
			// transport
			// call
			androidHttpTransport.call(
					"http://tempuri.org/IService1/SaveOutletAttendance", envelope);

			result = (SoapPrimitive) envelope.getResponse();

			Log.e("SaveOutletAttendance", result.toString());

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;

	}

	public SoapPrimitive DataUpload(String ProductId, String Bacode, String FreshStock,
								   String Opening_Stock, String SoldStock, String ClosingBal,
								   String GrossAmount, String Discount, String NetAmount,
								   String AndroidCreatedDate, String OutletCode) {

		SoapPrimitive result = null;
		try {
			SoapObject request = new SoapObject("http://tempuri.org/",
					"DataUpload");

			request.addProperty("ProductId", ProductId);
			request.addProperty("Bacode", Bacode);
			request.addProperty("FreshStock", FreshStock);
			request.addProperty("Opening_Stock", Opening_Stock);
			request.addProperty("SoldStock", SoldStock);
			request.addProperty("ClosingBal", ClosingBal);
			request.addProperty("GrossAmount", GrossAmount);
			request.addProperty("Discount", Discount);
			request.addProperty("NetAmount", NetAmount);
			request.addProperty("AndroidCreatedDate", AndroidCreatedDate);
			request.addProperty("OutletCode", OutletCode);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);// soap envelop with version
			envelope.setOutputSoapObject(request); // set request object
			envelope.dotNet = true;

			HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
			// transport
			// call
			androidHttpTransport.call("http://tempuri.org/IService1/DataUpload",
					envelope);

			result = (SoapPrimitive) envelope.getResponse();

			Log.e("DataUpload", result.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public SoapObject GetDashboardData(String fromdate, String todate, String outletcode, String bacode){

		SoapObject result = null;
		try {

			SoapObject request = new SoapObject("http://tempuri.org/",
					"GetDashboardData");

			request.addProperty("MinDate", fromdate);
			request.addProperty("MaxDate", todate);
			request.addProperty("OutletCode", outletcode);
			request.addProperty("bacode ", bacode);


			Log.e("Request", request.toString());

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);// soap envelop with version
			envelope.setOutputSoapObject(request); // set request object
			envelope.dotNet = true;

			HttpTransportSE androidHttpTransport = new HttpTransportSE(url,60000);// http
			// transport
			// call
			androidHttpTransport.call("http://tempuri.org/IService1/GetDashboardData", envelope);

			//androidHttpTransport.getServiceConnection().disconnect();  //23.04.2015

			result = (SoapObject) envelope.getResponse();

			Log.e("GetDashboardData==", result.toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public SoapObject TotalOutletSaleAPK(String bacode, String FromDate, String Todate)
	{
		SoapObject result = null;
		try {

			SoapObject request = new SoapObject("http://tempuri.org/",
					"TotalOutletSaleAPK");

			request.addProperty("bacode", bacode);
			request.addProperty("FromDate", FromDate);
			request.addProperty("Todate", Todate);

			Log.e("Request", request.toString());

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);// soap envelop with version
			envelope.setOutputSoapObject(request); // set request object
			envelope.dotNet = true;

			HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
			// transport
			// call
			androidHttpTransport.call("http://tempuri.org/IService1/TotalOutletSaleAPK", envelope);

			//androidHttpTransport.getServiceConnection().disconnect();  //23.04.2015

			result = (SoapObject) envelope.getResponse();

			Log.e("TotalOutletSaleAPK", result.toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

	public SoapObject BAOutletSale(String bacode, String OutletCode) {

		SoapObject result = null;
		try {

			SoapObject request = new SoapObject("http://tempuri.org/",
					"BAOutletSale");

			request.addProperty("bacode", bacode);
			request.addProperty("OutletCode", OutletCode);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);// soap envelop with version
			envelope.setOutputSoapObject(request); // set request object
			envelope.dotNet = true;

			HttpTransportSE androidHttpTransport = new HttpTransportSE(url,60000);// http
			// transport
			// call
			androidHttpTransport.call("http://tempuri.org/IService1/BAOutletSale", envelope);

			//androidHttpTransport.getServiceConnection().disconnect();  //23.04.2015

			result = (SoapObject) envelope.getResponse();

			Log.e("BAOutletSale==", result.toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public SoapObject GetAttendance(String bacode, String Fromdate, String Todate)
	{
		SoapObject result = null;
		try {

			SoapObject request = new SoapObject("http://tempuri.org/",
					"GetAttendance");

			request.addProperty("bacode", bacode);
			request.addProperty("Fromdate", Fromdate);
			request.addProperty("Todate", Todate);

			Log.e("Request", request.toString());

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);// soap envelop with version
			envelope.setOutputSoapObject(request); // set request object
			envelope.dotNet = true;

			HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
			// transport
			// call
			androidHttpTransport.call("http://tempuri.org/IService1/GetAttendance", envelope);

			//androidHttpTransport.getServiceConnection().disconnect();  //23.04.2015

			result = (SoapObject) envelope.getResponse();

			Log.e("GetAttendance", result.toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

	public SoapObject DataDownload(String bacode, String Fromdate, String Todate, String outletcode)
	{
		SoapObject result = null;
		try {

			SoapObject request = new SoapObject("http://tempuri.org/",
					"DataDownload");

			request.addProperty("bacode", bacode);
			request.addProperty("Fromdate", Fromdate);
			request.addProperty("Todate", Todate);
			request.addProperty("outletcode", outletcode);

			Log.e("Request", request.toString());

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);// soap envelop with version
			envelope.setOutputSoapObject(request); // set request object
			envelope.dotNet = true;

			HttpTransportSE androidHttpTransport = new HttpTransportSE(url);// http
			// transport
			// call
			androidHttpTransport.call("http://tempuri.org/IService1/DataDownload", envelope);

			//androidHttpTransport.getServiceConnection().disconnect();  //23.04.2015

			result = (SoapObject) envelope.getResponse();

			Log.e("DataDownload", result.toString());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}




}
