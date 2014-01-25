package com.github.viperdream;

import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

public class exportPinTask extends AsyncTask<String, Void, String>{

	HttpPost httpPost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpClient;
	
	@Override
	protected String doInBackground(String... pinArray) {
		
		httpClient = new DefaultHttpClient();
		httpPost = new HttpPost("http://www.memo-link.be/php/exportPin.php");
		
		try {
			ArrayList<NameValuePair> pinData = new ArrayList<NameValuePair>(2);
		
			pinData.add(new BasicNameValuePair("pinID", pinArray[0]));
			pinData.add(new BasicNameValuePair("pinTitle", pinArray[1]));
			pinData.add(new BasicNameValuePair("pinMessage", pinArray[2]));
			pinData.add(new BasicNameValuePair("pinDuration", pinArray[3]));
			pinData.add(new BasicNameValuePair("pinX", pinArray[4]));
			pinData.add(new BasicNameValuePair("pinY", pinArray[5]));
			pinData.add(new BasicNameValuePair("pinColor", pinArray[6]));
			pinData.add(new BasicNameValuePair("pinAuthor", pinArray[7]));
			httpPost.setEntity(new UrlEncodedFormEntity(pinData));
		
		for (int i = 0; i < 7; i++){ //debugging purposes
			Log.d("async", pinArray[i]);
		}
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpClient.execute(httpPost, responseHandler);	
			Pin pin = Session.getInstance().getDbUtil().findPinById(Integer.parseInt(pinArray[0]));
			pin.setPinSQLId(Integer.parseInt(response));
			Log.d("PHP Response", response);
			Log.d("Pin", "Pin SQL ID: " + Integer.toString(pin.getPinSQLId()));
		} catch (Exception e) {
			e.printStackTrace();
			return "fail";
		}
		
		return "success";
	}
	protected void onPostExecute(ArrayList<String> result) {

    }

}
