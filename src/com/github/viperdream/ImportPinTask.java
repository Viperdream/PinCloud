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

public class ImportPinTask extends AsyncTask<String, Void, String>{

	HttpPost httpPost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpClient;
	
	@Override
	protected String doInBackground(String... author) {
		httpClient = new DefaultHttpClient();
		httpPost = new HttpPost("http://www.memo-link.be/php/pinImport.php");
		ArrayList <NameValuePair> authorList = new ArrayList<NameValuePair>(2);
		
		authorList.add(new BasicNameValuePair("author", author[0]));
		
		try {
			
			httpPost.setEntity(new UrlEncodedFormEntity(authorList));
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpClient.execute(httpPost, responseHandler);	
			Log.d("PHP Response", response);
			JSONDecoder.decodePin(response);
		
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
