package com.github.viperdream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBUtil extends MySQLiteHelper{
	
	private static final int DATABASE_VERSION = 8;
	private static final String DATABASE_NAME = "PinDB";
	private static final String TABLE_PINS = "tblPins";
	
	public DBUtil(Context context, String name, CursorFactory factory,int version) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}
}
