package com.github.viperdream;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class AddPinActivity extends Activity {
	
	public static boolean getLocations = false;
	public static int getPinX;
	public static int getPinY;
	public static final String PREFS_NAME = "pinDetails";
	public static String pinMessage;
	public static String pinTitle;
	public static String pinColor;
	public static int pinDuration;
	public static String pinAuthor;
	public static int pinSQLID;
	public static final String PREFS_AUTHOR = "Author";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		getActionBar().setTitle("Add a new pin");
		setContentView(R.layout.activity_add_pin);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_add_pin, menu);
		return true;
	}
	
	public void btnCreatePin(View view){
		SharedPreferences author = getSharedPreferences(PREFS_AUTHOR, 0);
		MySQLiteHelper dbHandler = new MySQLiteHelper(this, "PinDB", null, 4);
		EditText titleBox = (EditText) findViewById(R.id.pinTitle);
		EditText messageBox = (EditText) findViewById(R.id.pinMessage);
		Spinner durationSpinner = (Spinner) findViewById(R.id.pinDuration);
		
		pinTitle = titleBox.getText().toString().trim();
		pinMessage = messageBox.getText().toString().trim();
		pinDuration = 1;
		pinColor = "red";
		pinAuthor = author.getString("username", "");
		
		Pin debugPin = dbHandler.findPin(pinTitle);
		
		placePin();

	}
	
	public void placePin(){
		
		getLocations = true;
		Intent intent = new Intent(this, DashboardActivity.class);
		startActivity(intent);
	}
}
