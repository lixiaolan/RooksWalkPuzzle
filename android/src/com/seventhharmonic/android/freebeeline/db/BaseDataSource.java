package com.seventhharmonic.android.freebeeline.db;

import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BaseDataSource {
	
  // Database fields
    protected SQLiteDatabase database;
    protected MySQLiteHelper dbHelper;
    
    public BaseDataSource(Context context) {
	dbHelper = new MySQLiteHelper(context);
    }
    
    public void open() throws SQLException {
	database = dbHelper.getWritableDatabase();
    }
    
    public void close() {
	dbHelper.close();
    }
   
} 
