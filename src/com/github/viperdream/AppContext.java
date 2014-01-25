package com.github.viperdream;

import android.app.Application;
import android.content.Context;

public class AppContext extends Application{

	private static Context context;
	
	public void onCreate(){
		context = this.getApplicationContext();
	}
	
	public static Context getAppContext(){
		return context;
	}
	
}
