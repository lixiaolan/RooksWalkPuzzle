package com.seventhharmonic.android.freebeeline.db;



import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BaseDataSource {
	
  // Database fields
    protected SQLiteDatabase database;
    protected MySQLiteHelper dbHelper;
    
    public BaseDataSource(Context context, MySQLiteHelper MSQLH) {
	dbHelper = MSQLH;
    }
    
    public void open() throws SQLException {
	if (database == null) {
	    database = dbHelper.getWritableDatabase();
	}
	else if (!database.isOpen()) {
	    database = dbHelper.getWritableDatabase();
	}
    }
    
    public void close() {
    	dbHelper.close();
    }
    
    
   
} 
