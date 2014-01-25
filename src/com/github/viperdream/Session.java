package com.github.viperdream;

import android.content.Context;

public class Session {

	private static final int DATABASE_VERSION = 8;
	private static final String DATABASE_NAME = "PinDB";
	private static final String TABLE_PINS = "tblPins";
	
	private static Session session = new Session();
	private static DBUtil DBUtil = new DBUtil(null, DATABASE_NAME, null, DATABASE_VERSION );

    protected Session() {
        //
    }

    public static Session getInstance() {
        if (session == null) {
        	session = new Session();
        }
        return session;
    }

    public void initDBUtil(Context ctx){
        DBUtil = new DBUtil(ctx.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBUtil getDbUtil()
    {
      return DBUtil;
    }
}
