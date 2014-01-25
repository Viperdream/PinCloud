package com.github.viperdream;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	
	HttpPost httpPost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpClient;
	List<NameValuePair> registerData;
	ProgressDialog dialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		ActionBar ab = getActionBar();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		ab.setTitle("Sign up");
		ab.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	public void btnRegister(View view){
		
		String response = checkForNull();
		
		if (response.equals("empty")){
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(250);
			Toast toast = Toast.makeText(RegisterActivity.this, "Please fill in the fields", Toast.LENGTH_SHORT);
			toast.show();
			return;
		}else if(response.equals("too short")){
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(250);
			Toast toast = Toast.makeText(RegisterActivity.this, "Username or password is too short!", Toast.LENGTH_SHORT);
			toast.show();
			return;
		}else if(response.equals("invalid mail")){
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(250);
			Toast toast = Toast.makeText(RegisterActivity.this, "Please use a valid mail address", Toast.LENGTH_SHORT);
			toast.show();
			return;
		}else if(response.equals("invalid password")){
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(250);
			Toast toast = Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT);
			toast.show();
			return;
		}
		
		dialog = ProgressDialog.show(RegisterActivity.this, "Logging in...", "Validating user");
		
		new Thread(new Runnable(){
			public void run(){
				register();
			}
		}).start();
	}
	
	public void register(){
	    try{

	        httpClient = new DefaultHttpClient();
	        httpPost = new HttpPost("http://www.memo-link.be/php/register.php");
	        registerData = new ArrayList<NameValuePair>(2);
	        EditText mailBox = (EditText) findViewById(R.id.mail_box);
	        EditText userBox = (EditText) findViewById(R.id.username_box);
	        EditText passBox = (EditText) findViewById(R.id.password_box);

	        String mail = mailBox.getText().toString();
	        String username = userBox.getText().toString();
	        String password = passBox.getText().toString();

	        registerData.add(new BasicNameValuePair("mail", mail));
	        registerData.add(new BasicNameValuePair("username", username));
	        registerData.add(new BasicNameValuePair("password", password));
	        httpPost.setEntity(new UrlEncodedFormEntity(registerData));
	        
	        ResponseHandler<String> responseHandler = new BasicResponseHandler();
	        String response = httpClient.execute(httpPost, responseHandler);
	        System.out.println("Response : " + response.toString());

	        if (response.equalsIgnoreCase("username or email already exists")){
	            runOnUiThread(new Runnable(){
	                public void run(){
	                    dialog.dismiss();
	                }
	        });
	            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	            v.vibrate(250);
	            Toast toast = Toast.makeText(RegisterActivity.this, "Username already exists!", Toast.LENGTH_SHORT);
	            toast.show();
	        }else if(response.toString().equalsIgnoreCase("registration complete")){
	            runOnUiThread(new Runnable(){
	                public void run(){
	                    Toast toast = Toast.makeText(RegisterActivity.this, "Registration Succesfull", Toast.LENGTH_SHORT);
	                    toast.show();
	                }
	            });
	            startActivity(new Intent(RegisterActivity.this, DashboardActivity.class));
	        }

	        runOnUiThread(new Runnable(){
	            public void run(){
	                dialog.dismiss();
	            }
	    });

	    }catch(UnknownHostException  e){
			dialog.dismiss();
			runOnUiThread(new Runnable(){
				public void run(){
					Toast toast = Toast.makeText(RegisterActivity.this, "No connection!", Toast.LENGTH_SHORT);
					toast.show();
				}
			});
		
		}catch (Exception e){
	        dialog.dismiss();
	        runOnUiThread(new Runnable(){
				public void run(){
					Toast toast = Toast.makeText(RegisterActivity.this, "Something went wrong! Please try again", Toast.LENGTH_SHORT);
					toast.show();
				}
			});
	        System.out.println("Exception : " + e.getMessage());
	    }
	}   
	public String checkForNull(){
		EditText mailBox = (EditText) findViewById(R.id.mail_box);
		EditText userBox = (EditText) findViewById(R.id.username_box);
		EditText passBox = (EditText) findViewById(R.id.password_box);
		EditText rptPassBox = (EditText) findViewById(R.id.ValidationPassword_box);
		
		String mail = mailBox.getText().toString();
		String user = userBox.getText().toString();
		String password = passBox.getText().toString();
		String rptPassword = rptPassBox.getText().toString();
		String response = "";

	    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence inputStr = mail;
	    
	    if (mail.trim().equals("") || user.trim().equals("") || password.trim().equals("")){ //check for null
	    	response = "empty";
	    	return response;
	    }
	    
	    if (user.trim().length() < 3 || password.trim().length() < 5){
	    	response = "too short";
	    	return response;
	    }
	    
	    if (!password.matches(rptPassword)){
	    	response = "invalid password";
	    	return response;
	    }
	    
	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE); //check whether email is right format
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches()) {
	    }else{
	    	response ="invalid mail";
	    	return response;
	    }
	    
	    response = "OK";
	    return response;
	    
	}
}


