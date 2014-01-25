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

public class pinsAmountTask extends AsyncTask<String, Integer, Integer>{

	HttpPost httpPost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpClient;
	
	
	protected Integer doInBackground(String... author) {
			ArrayList <NameValuePair> authorList = new ArrayList<NameValuePair>(2);
			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost("http://www.memo-link.be/php/pinsAmount.php");
			
			try {
				authorList.add(new BasicNameValuePair("author", author[0]));
				
				httpPost.setEntity(new UrlEncodedFormEntity(authorList));
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				final String response = httpClient.execute(httpPost, responseHandler);	
				int result = Integer.parseInt(response);
				Log.d("PHP Response", response);
				
				return result;
				
			} catch (Exception e) {
				Log.d("pinsAmountAsync", "Something went horribly wrong!");
				Log.d("pinsAmountAsync", e.toString());
				return null;
			}
	}

	public void onPostExecute(Integer result) {
		
		Integer localPins = Session.getInstance().getDbUtil().getPinsAmount();
		
		if (localPins < result){
			Log.d("Import MYSQL", "New pins have arrived! Importing...");
			
			DashboardActivity.importPinMysql(DashboardActivity.pinIDS, DashboardActivity.author);
		} else{
			Log.d("Import MYSQL", "No new pins!");
		}
		
	}

}
