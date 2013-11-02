package com.seventhharmonic.android.freebeeline.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
    
    public static final String TABLE_PUZZLES = "puzzels";
    public static final String TABLE_HINTS = "hints";


    public static final String TABLE_PURCHASED = "purchased"; 


    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PUZZLE = "completed";
    public static final String COLUMN_HINTS = "hintsLeft";
    public static final String COLUMN_PURCHASED = "bought";
    public static final String COLUMN_SKU = "sku";
    private static final String DATABASE_NAME = "puzzles.db";
    private static final int DATABASE_VERSION = 1;
    
    // Database creation sql statement
    private static final String DATABASE_CREATE_PUZZLES = "create table "
	+ TABLE_PUZZLES + "(" + COLUMN_ID
	+ " integer primary key, " + COLUMN_PUZZLE
	+ " text not null);";
    private static final String DATABASE_CREATE_HINTS = "create table "
	+ TABLE_HINTS + "(" + COLUMN_ID
	+ " integer primary key, " + COLUMN_HINTS
	+ " integer);";


    //It may be the case that one could use text as a primay key, but it
    //one should test this before assuming it will work.  There are varied
    //opinions out there about this.

    private static final String DATABASE_CREATE_PURCHASED = "create table "
	+ TABLE_PURCHASED + "(" + COLUMN_ID
	+ " integer primary key autoincrement, " + COLUMN_SKU
	+ " text not null, " + COLUMN_PURCHASED
	+ " integer);";
    
    public MySQLiteHelper(Context context) {
	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase database) {
	database.execSQL(DATABASE_CREATE_PURCHASED);
	database.execSQL(DATABASE_CREATE_PUZZLES);
	database.execSQL(DATABASE_CREATE_HINTS);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	// Log.w(MySQLiteHelper.class.getName(),
	//       "Upgrading database from version " + oldVersion + " to "
	//       + newVersion + ", which will destroy all old data");
	// db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUZZLES);
	// onCreate(db);
    }    
} 
