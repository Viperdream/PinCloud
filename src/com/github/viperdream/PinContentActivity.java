package com.github.viperdream;

import java.io.IOException;
import java.net.UnknownHostException;
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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class PinContentActivity extends Activity {

	private String[] pinInfo = new String[2]; 
	Integer pinID;
	HttpPost httpPost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpClient;
	ProgressDialog dialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pin_content);
		
		Intent intent = getIntent();
		pinID = Integer.parseInt(intent.getStringExtra(DashboardActivity.PIN_ID));
		
		pinInfo = getPinContent(pinID);
		
		TextView pinMessageBox = (TextView) findViewById(R.id.pinMessageBox);
		pinMessageBox.setText(pinInfo[1] + " by " + pinInfo [2]);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.pin_buttons, menu);
		getActionBar().setTitle(pinInfo[0]);
		
		return true;
	}
	@Override
	public boolean onOptionsItemSelected (MenuItem item){
		switch(item.getItemId()){
		case R.id.action_logout:
			Intent intent = new Intent (this, MainActivity.class);
			startActivity(intent);
			return true;
		case R.id.new_pin:
			Intent newPinIntent = new Intent(this, AddPinActivity.class);
			startActivity(newPinIntent);
			return true;
		case R.id.delete_pin:
			//MySQLiteHelper dbHandler = new MySQLiteHelper(this, "PinDB", null, 4);
			//Pin pin = dbHandler.findPinById(pinID);
			
			dialog = ProgressDialog.show(PinContentActivity.this, "Deleting pin...", "Deleting pin...");
			
			new Thread(new Runnable(){
				public void run(){
					deletePin(pinID);
				}
			}).start();
			
		default:
            return super.onOptionsItemSelected(item);
		}
	}
	
	public void deletePin(Integer id){
		try {
			MySQLiteHelper dbHandler = new MySQLiteHelper(this, "PinDB", null, 4);
			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost("http://www.memo-link.be/php/deletePin.php");
			Pin pin = dbHandler.findPinById(id);
			Integer pinID = pin.getPinSQLId();
			
			Log.d("Delete Pin", pinID.toString());
			
			ArrayList<NameValuePair> pinIDList = new ArrayList<NameValuePair>(2);
			
			pinIDList.add(new BasicNameValuePair("pinID", pinID.toString()));
			httpPost.setEntity(new UrlEncodedFormEntity(pinIDList));
			
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpClient.execute(httpPost, responseHandler);	
			Log.d("PHP Response", response);
			
			if (response.equalsIgnoreCase("pin deleted!")){
				dbHandler.deleteTable();
				runOnUiThread(new Runnable(){
					public void run(){
						Toast toast = Toast.makeText(PinContentActivity.this, "Pin Deleted!", Toast.LENGTH_SHORT);
						toast.show();
					}
				});
			}else{
				Log.d("Delete Pin", "Something went wrong!");
			}
			
			runOnUiThread(new Runnable(){
				public void run(){
					//result.setText(response);
					dialog.dismiss();
				}
			});
			
			Intent dashboardIntent = new Intent(this, DashboardActivity.class);
			this.startActivity(dashboardIntent);
			
		} catch (UnknownHostException e) {
			runOnUiThread(new Runnable(){
				public void run(){
					Toast toast = Toast.makeText(PinContentActivity.this, "No internet connection!", Toast.LENGTH_SHORT);
					toast.show();
				}
			});
			e.printStackTrace();
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public String[] getPinContent(int pinID){
		MySQLiteHelper dbHandler = new MySQLiteHelper(this, "PinDB", null, 4);
		Pin pin = dbHandler.findPinById(pinID);
		
		return new String[]{pin.getPinTitle(),pin.getPinMessage(), pin.getPinAuthor()};
	}
}
