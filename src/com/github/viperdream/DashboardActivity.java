package com.github.viperdream;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class DashboardActivity extends FragmentActivity {
	
	public final static String PIN_ID= "com.github.viperdream.PINID";
	HttpPost httpPost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpClient;
	public static final String PREFS_AUTHOR = "Author";
	public static int localPins;
	public static List<Integer> pinIDS = new ArrayList<Integer>();
	public static String author;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dashboard);
		
		MySQLiteHelper dbHandler = new MySQLiteHelper(this, "PinDB", null, 4);
		Session.getInstance().initDBUtil(this);
		SharedPreferences authorPref = getSharedPreferences(PREFS_AUTHOR, 0);
		author = authorPref.getString("username", "");
		
		localPins = dbHandler.getPinsAmount();
		Log.d("Dashboard Create", "author: " + author + " amount: " + localPins);
		
		if (localPins < 1){
			Log.d("localpins", "No pins stored on this device!");
			pinIDS.add(0);
		}else{
			for (int i=1; i < localPins+1; i++){
				Pin pin = dbHandler.findPinById(i);	
				
				if (pin.getPinSQLId() == null){
					return;
				}else{
					pinIDS.add(pin.getPinSQLId());
				}
			}
		}
		
		new pinsAmountTask().execute(author);
		initPins();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//TODO: add title of board
		if(AddPinActivity.getLocations == true){
			getActionBar().setTitle("Pick a location for your Pin!");
		}else{
			getActionBar().setTitle("Main Pins");
			getMenuInflater().inflate(R.menu.main_buttons, menu);
		}
		
		
		return true;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		
		if (AddPinActivity.getLocations == true){
			MySQLiteHelper dbHandler = new MySQLiteHelper(this, "PinDB", null, 4);
			
			Float pinX = event.getX();
			Float pinY = event.getY();
			
			AddPinActivity.getLocations = false;
			
			Pin pin = new Pin(AddPinActivity.pinTitle, AddPinActivity.pinMessage, AddPinActivity.pinDuration, pinX, pinY, AddPinActivity.pinColor, AddPinActivity.pinAuthor, AddPinActivity.pinSQLID);
			dbHandler.addPin(pin);
			
			exportPinMysql(AddPinActivity.pinTitle);
			
			Intent intent = new Intent(this, DashboardActivity.class);
			startActivity(intent);
		}
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
		default:
            return super.onOptionsItemSelected(item);
		}
	}
	
	public static void importPinMysql(List<Integer> pinIDS, String author){
		
		Integer localpins = Session.getInstance().getDbUtil().getPinsAmount();
		
		if (localpins == 0){
			Log.d("MySQL", "Importing all new pins!");
			new ImportPinTask().execute(author);
			
		}
		
	}

	public void exportPinMysql(String pinTitle){
		
		ArrayList<NameValuePair> pinData = new ArrayList<NameValuePair>(2);
		String[] pinArray = new String[7];
		
		MySQLiteHelper dbHandler = new MySQLiteHelper(this, "PinDB", null, 4);
		Pin pin = dbHandler.findPin(pinTitle);
		
		Log.d("MySQL", "Starting pin export...");
		
		try{
			
			pinArray = new String[]{Integer.toString(pin.getID()), 
					pin.getPinTitle(), pin.getPinMessage(), 
					Integer.toString(pin.getPinDuration()), 
					Float.toString(pin.getPinX()), 
					Float.toString(pin.getPinY()), 
					pin.getPinColor(), 
					pin.getPinAuthor()};
		
			new exportPinTask().execute(pinArray);
			
		}catch (Exception e){
			Log.d("MYSQL", "Something went oh so wrong!");
			Log.d("MYSQL", e.toString());
		}
	}
	
	public static int dpToPixels(Context context, float dp) {
	    final float scale = context.getResources().getDisplayMetrics().density;
	    return (int) (dp * scale + 0.5f);
	}
	@Override
	public void onBackPressed() {
		moveTaskToBack(true);
	}

	public void initPins(){
		MySQLiteHelper dbHandler = new MySQLiteHelper(this, "PinDB", null, 4);
		int rows = 0;
		
		rows = dbHandler.getPinsAmount();
		Log.d("initPins: ", Integer.toString(rows));
		
		LayoutParams params = new LayoutParams(dpToPixels(getApplicationContext(), 5), dpToPixels(getApplicationContext(), 5));
		
		RelativeLayout pinLayout = new RelativeLayout(this);
		pinLayout.setBackgroundColor(R.color.babyblue_color);
		setContentView(pinLayout);
		
		for (int i=1; i < rows+1; i++){
			Log.d("initPins: ", "initializing pin " + i + "...");
			
			ImageButton btnPin = new ImageButton(getApplicationContext());
			
			Pin pin = dbHandler.findPinById(i);
			
			
			float x = pin.getPinX();
			float y = pin.getPinY();
			
			//params.setMargins(x, y, 0, 0);
			
			btnPin.setOnClickListener(pinClickListener(btnPin));
			btnPin.setClickable(true);
			btnPin.setImageResource(R.drawable.pin_red);
			btnPin.setBackground(null);
			btnPin.setId(pin.getID());
			btnPin.setX(x);
			btnPin.setY(y);
			pinLayout.addView(btnPin);
			
			Log.d("initPins: ", "Added pin " + i + "!");
		}
	}
	
	public View.OnClickListener pinClickListener (final ImageButton btnPin){
	    return new View.OnClickListener() {
	        public void onClick(View v) {
	            openPinContent(btnPin.getId());
	        }
	    };
	}
	
	public void openPinContent(int pinID){
		MySQLiteHelper dbHandler = new MySQLiteHelper(this, "PinDB", null, 4);
		Pin pin = dbHandler.findPinById(pinID);
		
		Log.d("pinmessage:", pin.getPinMessage());
		
		Intent intent = new Intent(this, PinContentActivity.class);
		intent.putExtra(PIN_ID, Integer.toString(pinID));
		startActivity(intent);
	}
}
