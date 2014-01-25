package com.github.viperdream;

import java.lang.reflect.Type;
import java.util.List;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JSONDecoder {

		public static void decodePin(String data){
			
			int id = Session.getInstance().getDbUtil().getPinsAmount();
			int i = 1;
			
			Gson gson = new Gson();
			
			Type type = new TypeToken<List<Pin>>(){}.getType();
			List<Pin> pin = gson.fromJson(data, type);
			
			for (Pin newPin : pin){
				
				Log.d("GSON", newPin.getPinTitle());
				newPin.setPinSQLId(newPin.getID());
				newPin.setPinID(id);
				Session.getInstance().getDbUtil().addPin(newPin);
				Log.d("Pin " + i, Session.getInstance().getDbUtil().findPin(newPin.getPinTitle()).getPinMessage());
				
				i += 1;
				id += 1;
			}
	        
	    }
		  
		
}
	

