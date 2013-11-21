package com.seventhharmonic.android.freebeeline.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class HintsDataSource extends BaseDataSource{
        
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_HINTS};
    private final long hintIndex = 0;
    
    public HintsDataSource(Context context, MySQLiteHelper MSQLH) {
	super(context, MSQLH);
    }
    
    public SQLHint getHints() {
	open();

	Cursor cursor = database.query(MySQLiteHelper.TABLE_HINTS, allColumns,
				       MySQLiteHelper.COLUMN_ID + " = " + hintIndex, 
				       null, null, null, null);
	
	if (!cursor.moveToFirst()) {
	    ContentValues values = new ContentValues();
	    int n = 0;
	    values.put(MySQLiteHelper.COLUMN_ID, hintIndex); 
	    values.put(MySQLiteHelper.COLUMN_HINTS, n); 
	    
	    database.insert(MySQLiteHelper.TABLE_HINTS, null, values);
	    cursor = database.query(MySQLiteHelper.TABLE_HINTS, allColumns,
				    MySQLiteHelper.COLUMN_ID + " = " + hintIndex, 
				    null, null, null, null);
	}

	cursor.moveToFirst();
	SQLHint newSQLHint = new SQLHint(cursor.getLong(1));
	cursor.close();
	return newSQLHint;
    }
    
    public void setHint(long num) {
	open();

	ContentValues values = new ContentValues();
	values.put(MySQLiteHelper.COLUMN_HINTS, num);
	values.put(MySQLiteHelper.COLUMN_ID, hintIndex);
 
	Cursor cursor = database.query(MySQLiteHelper.TABLE_HINTS, allColumns,
				       MySQLiteHelper.COLUMN_ID + " = " + hintIndex, 
				       null, null, null, null);
	if (!cursor.moveToFirst()) {
		database.insert(MySQLiteHelper.TABLE_HINTS, null, values);
	}
	
	database.update(MySQLiteHelper.TABLE_HINTS, values, MySQLiteHelper.COLUMN_ID + " = " + hintIndex, null);
	cursor.close();
	return;
    }

    public void addHints(long num) {
	open();

	SQLHint old = getHints();
	
	ContentValues values = new ContentValues();
	values.put(MySQLiteHelper.COLUMN_ID, hintIndex);
	values.put(MySQLiteHelper.COLUMN_HINTS, num + old.getNum());
 
	Cursor cursor = database.query(MySQLiteHelper.TABLE_HINTS, allColumns,
				       MySQLiteHelper.COLUMN_ID + " = " + hintIndex, 
				       null, null, null, null);

	if (!cursor.moveToFirst()) {
		database.insert(MySQLiteHelper.TABLE_HINTS, null, values);
	}

	database.update(MySQLiteHelper.TABLE_HINTS, values, MySQLiteHelper.COLUMN_ID + " = " + hintIndex, null);
	cursor.close();
	return;
    }

    public boolean useHint() {
	
	
	SQLHint old = getHints();
	
	if (old.getNum() < 1) {
	    return false;
	}
	
	addHints(-1);
	
	return true;
    }

} 
