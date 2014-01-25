package com.github.viperdream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper{
	
	private static final int DATABASE_VERSION = 8;
	
	private static final String DATABASE_NAME = "PinDB";
	
	private static final String TABLE_PINS = "tblPins";
	
	private static final String KEY_PINID = "pin_id";
	private static final String KEY_PINTITLE = "pinTitle";
	private static final String KEY_PINMESSAGE = "pinMessage";
	private static final String KEY_PINDURATION = "pinDuration";
	private static final String KEY_PINX = "pinX";
	private static final String KEY_PINY = "pinY";
	private static final String KEY_PINCOLOR = "pinColor";
	private static final String KEY_PINAUTHOR = "author";
	private static final String KEY_PINSQLID = "pinSQLId";
	
	public MySQLiteHelper(Context context, String name, CursorFactory factory, int version){
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db){
		try{
			db.execSQL("CREATE TABLE " + TABLE_PINS + " (" +
					KEY_PINID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_PINTITLE + " TEXT," +
					KEY_PINMESSAGE +" TEXT," + 
					KEY_PINDURATION +" INTEGER, " +
					KEY_PINX + " INTEGER, " +
					KEY_PINY + " INTEGER, " + 
					KEY_PINCOLOR + " STRING, " +
					KEY_PINAUTHOR + " STRING, " +
					KEY_PINSQLID + " INTEGER);"
					);
		}catch (SQLException e){
			Log.d("SQL Error", "SQL Error: " + e);
		}
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("DROP TABLE IF EXISTS tblPins");
		this.onCreate(db);
	}
	
	public void deleteTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS tblPins");
		this.onCreate(db);
	}
	
	public void addPin(Pin pin){
		Log.d("addPin", pin.toString());
		SQLiteDatabase db = this.getWritableDatabase();
		onCreate(db);
		ContentValues values = new ContentValues();
		
		values.put(KEY_PINTITLE, pin.getPinTitle());
		values.put(KEY_PINMESSAGE, pin.getPinMessage());
		values.put(KEY_PINDURATION, pin.getPinDuration());
		values.put(KEY_PINX, pin.getPinX());
		values.put(KEY_PINY, pin.getPinY());
		values.put(KEY_PINCOLOR, pin.getPinColor());
		values.put(KEY_PINAUTHOR, pin.getPinAuthor());
		values.put(KEY_PINSQLID, pin.getPinSQLId());
		
		db.insert(TABLE_PINS, null, values);
		db.close();
	}
	
	public Pin findPin(String pinTitle){
		String query = "Select * FROM " + TABLE_PINS + " WHERE " + KEY_PINTITLE + " = \"" + pinTitle +"\"";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		Pin pin = new Pin();
		
		if (cursor.moveToFirst()){
			cursor.moveToFirst();
			pin.setPinID(Integer.parseInt(cursor.getString(0)));
			pin.setPinTitle(cursor.getString(1));
			pin.setPinMessage(cursor.getString(2));
			pin.setPinDuration(Integer.parseInt(cursor.getString(3)));
			pin.setPinX(Float.parseFloat(cursor.getString(4)));
			pin.setPinY(Float.parseFloat(cursor.getString(5)));
			pin.setPinColor(cursor.getString(6));
			pin.setPinAuthor(cursor.getString(7));
			pin.setPinSQLId(Integer.parseInt(cursor.getString(8)));
			cursor.close();
		}else{
			pin = null;
		}
		db.close();
		return pin;
	}
	
	public Pin findPinById(Integer pinID){
		String query = "Select * FROM " + TABLE_PINS + " WHERE " + KEY_PINID + " = \"" + pinID +"\"";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		Pin pin = new Pin();
		
		if (cursor.moveToFirst()){
			cursor.moveToFirst();
			pin.setPinID(Integer.parseInt(cursor.getString(0)));
			pin.setPinTitle(cursor.getString(1));
			pin.setPinMessage(cursor.getString(2));
			pin.setPinDuration(Integer.parseInt(cursor.getString(3)));
			pin.setPinX(Float.parseFloat(cursor.getString(4)));
			pin.setPinY(Float.parseFloat(cursor.getString(5)));
			pin.setPinColor(cursor.getString(6));
			pin.setPinAuthor(cursor.getString(7));
			pin.setPinSQLId(Integer.parseInt(cursor.getString(8)));
			cursor.close();
		}else{
			pin = null;
		}
		db.close();
		return pin;
	}
	
	public boolean deletePin(Integer pinID){
		boolean result = false;
		String query = "Select * FROM " + TABLE_PINS + " WHERE " + KEY_PINID + " = \"" + pinID + "\"";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(query,  null);
		Pin pin = new Pin();
		
		if(cursor.moveToFirst()){
			pin.setPinID(Integer.parseInt(cursor.getString(0)));
			db.delete(TABLE_PINS, KEY_PINID + " = ?",
					new String[] { String.valueOf(pin.getID()) });
			cursor.close();
			result = true;
		}
		db.close();
		return result;
	}
	
	public Integer getPinsAmount(){
		int rows = 0;
		SQLiteDatabase db = this.getWritableDatabase();
		
		Cursor pinCount = db.rawQuery("SELECT Count(*) FROM tblPins", null);
		pinCount.moveToFirst();
		rows = pinCount.getInt(0);
		
		db.close();
		
		return rows;
	}
	
}
