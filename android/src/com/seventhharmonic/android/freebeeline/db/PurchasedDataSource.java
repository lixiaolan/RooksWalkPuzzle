package com.seventhharmonic.android.freebeeline.db;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PurchasedDataSource extends BaseDataSource{
        
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_SKU ,MySQLiteHelper.COLUMN_PURCHASED};
    
    public PurchasedDataSource(Context context, MySQLiteHelper MSQLH) {
	super(context, MSQLH);
    }
    
    public SQLPurchased getPurchased() {
	open();

	Cursor cursor = database.query(MySQLiteHelper.TABLE_PURCHASED, allColumns,
				       null, null, null, null, null);
	
	Map<String, Boolean> mSkuMap = new HashMap<String, Boolean>();

	cursor.moveToFirst();
	String str;
	Boolean bool;
	while (!cursor.isAfterLast()) {
	    str = cursor.getString(1);
	    if (cursor.getLong(2) == 1) {
		bool = true;
	    }
	    else {
		bool = false;
	    }

	    mSkuMap.put(str, bool);
	    cursor.moveToNext();
	}
	cursor.close();
	SQLPurchased newSQLPurchased = new SQLPurchased(mSkuMap);
	return newSQLPurchased;
    }

    public Boolean getPurchased(String sku) {

	//Note: when doing a query on a string type column, you MUST put single quotes ''
	//      around any sring variable (like sku) that you specify.  See below.  Not
	//      doing this leads to very unstable results!
	open();

	Cursor cursor = database.query(MySQLiteHelper.TABLE_PURCHASED, allColumns,
				       MySQLiteHelper.COLUMN_SKU + " = '" + sku + "'",
				       null, null, null, null);	
	
	if (!cursor.moveToFirst()) {
	    ContentValues values = new ContentValues();
	    Boolean bool = true;
	    values.put(MySQLiteHelper.COLUMN_SKU, sku); 
	    values.put(MySQLiteHelper.COLUMN_PURCHASED, bool); 
	    
	    database.insert(MySQLiteHelper.TABLE_PURCHASED, null, values);
	    cursor = database.query(MySQLiteHelper.TABLE_PURCHASED, allColumns,
				    MySQLiteHelper.COLUMN_SKU + " = '" + sku + "'", 
				    null, null, null, null);
	}
	
	cursor.moveToFirst();
	Boolean bool;

	if (cursor.getLong(2) == 1) {
	    bool = true;
	}
	else {
	    bool = false;
	}

       	cursor.close();
		return bool;
	


    }

    
    public void setPurchased(String sku, Boolean pur) {
	open();

	ContentValues values = new ContentValues();
	
	long num = pur ? 1 : 0;

	values.put(MySQLiteHelper.COLUMN_SKU, sku);
	values.put(MySQLiteHelper.COLUMN_PURCHASED, num);
 
	Cursor cursor = database.query(MySQLiteHelper.TABLE_PURCHASED, allColumns,
				       MySQLiteHelper.COLUMN_SKU + " = '" + sku + "'", 
				       null, null, null, null);

	if (!cursor.moveToFirst()) {
		database.insert(MySQLiteHelper.TABLE_PURCHASED, null, values);
	}
	
	database.update(MySQLiteHelper.TABLE_PURCHASED, values, MySQLiteHelper.COLUMN_SKU + " = '" + sku + "'", null);
	cursor.close();

	return;
    }
    
    
} 
