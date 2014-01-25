package com.github.viperdream;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	HttpPost httpPost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpClient;
	List<NameValuePair> userData;
	ProgressDialog dialog = null;
	public static final String PREFS_NAME = "PrefsFile";
	public static final String PREFS_AUTHOR = "Author";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle("Login");
		
		SharedPreferences loginDetails = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences author = getSharedPreferences(PREFS_AUTHOR, 0);
			
		EditText mailBox = (EditText) findViewById(R.id.mailIn_box);
		EditText passBox = (EditText) findViewById(R.id.passwordIn_box);
		CheckBox rememberMe = (CheckBox) findViewById(R.id.rememberme);
			
		mailBox.setText(loginDetails.getString("username", ""));
		passBox.setText(loginDetails.getString("password", ""));
		System.out.println("username: " + loginDetails.getString("username", ""));
		System.out.println("password: " + loginDetails.getString("password", ""));
			
		if(loginDetails.getBoolean("remember", false) == true){
				rememberMe.setChecked(true);
			}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	public void loginEvent(View view){
		
		SharedPreferences loginDetails = getSharedPreferences(PREFS_NAME, 0);
		
		CheckBox rememberMe = (CheckBox) findViewById(R.id.rememberme);
		EditText mailBox = (EditText) findViewById(R.id.mailIn_box);
		EditText passBox = (EditText) findViewById(R.id.passwordIn_box);
		
		final String username = mailBox.getText().toString();
		final String password = passBox.getText().toString();
		
		if (checkForNull() == true){
			System.out.println("so it's a null");
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(250);
			Toast toast = Toast.makeText(LoginActivity.this, "Please fill in the fields", Toast.LENGTH_SHORT);
			toast.show();
			return;
		}
		
		if(rememberMe.isChecked()){
			SharedPreferences.Editor editor = loginDetails.edit();
			
			editor.putString("username", username);
			editor.putString("password", password);
			editor.putBoolean("remember", true);
			editor.commit();
			
			System.out.println("Remember me: true");
			System.out.println("username: " + loginDetails.getString("username", ""));
			System.out.println("password: " + loginDetails.getString("password", ""));
		}
		
		dialog = ProgressDialog.show(LoginActivity.this, "Logging in...", "Validating user");
		
		new Thread(new Runnable(){
			public void run(){
				login(username, password);
			}
		}).start();
	}
	
	void login(String username, String password){
		try{
			SharedPreferences author = getSharedPreferences(PREFS_AUTHOR, 0);
			
			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost("http://www.memo-link.be/php/check.php");
			userData = new ArrayList<NameValuePair>(2);
			//final TextView result = (TextView) findViewById(R.id.tv);
			
			SharedPreferences.Editor authorEditor = author.edit();
			authorEditor.putString("username", username);
			authorEditor.commit();
			
			userData.add(new BasicNameValuePair("username", username));
			userData.add(new BasicNameValuePair("password", password));
			//System.out.println(username + " " + password);
			httpPost.setEntity(new UrlEncodedFormEntity(userData));
			
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpClient.execute(httpPost, responseHandler);
			System.out.println("Response : " + response);
			
			runOnUiThread(new Runnable(){
				public void run(){
					//result.setText(response);
					dialog.dismiss();
				}
			});
			
			if (response.equalsIgnoreCase("User Found")){
				runOnUiThread(new Runnable(){
					public void run(){
						Toast toast = Toast.makeText(LoginActivity.this, "Login Succesfull", Toast.LENGTH_SHORT);
						toast.show();
					}
				});
				startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
			}else{
			showAlert();
		}
		}catch(UnknownHostException  e){
			dialog.dismiss();
			runOnUiThread(new Runnable(){
				public void run(){
					Toast toast = Toast.makeText(LoginActivity.this, "No connection!", Toast.LENGTH_SHORT);
					toast.show();
				}
			});
		
		}catch (Exception e){
			dialog.dismiss();
			runOnUiThread(new Runnable(){
				public void run(){
					Toast toast = Toast.makeText(LoginActivity.this, "Something went wrong! Please try again", Toast.LENGTH_SHORT);
					toast.show();
				}
			});
			System.out.println("Exception : " + e.getMessage());
		}
	}
	public void showAlert(){
		LoginActivity.this.runOnUiThread(new Runnable(){
			public void run(){
				AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
				builder.setTitle("Login Error");
				builder.setMessage("Username/Password incorrect")
						.setCancelable(false)
						.setPositiveButton("OK", new DialogInterface.OnClickListener(){
							public void onClick(DialogInterface dialog, int id){
							}
						});
				AlertDialog alert = builder.create();
				alert.show();
			}
			
			
		});
	}
	public boolean checkForNull(){
		EditText mailBox = (EditText) findViewById(R.id.mailIn_box);
		EditText passBox = (EditText) findViewById(R.id.passwordIn_box);
		
		if (mailBox.getText().toString().trim().length() > 0 || passBox.getText().toString().trim().length() > 0){
			return false;
		}else{
			return true;
		}
	}
}