package com.seventhharmonic.android.freebeeline;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PuzzleDataSource {

  // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
				    MySQLiteHelper.COLUMN_PUZZLE };
    
    public CommentsDataSource(Context context) {
	dbHelper = new MySQLiteHelper(context);
    }
    
    public void open() throws SQLException {
	database = dbHelper.getWritableDatabase();
    }
    
    public void close() {
	dbHelper.close();
    }
    
    public SQLPuzzle getPuzzle(long id) {

	Cursor cursor = database.query(MySQLiteHelper.TABLE_PUZZLES, allColumns,
				       MySQLiteHelper.COLUMN_ID + " = " + id, 
				       null, null, null, null);
	if (cursor == null) {
	    	ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_PUZZLE, "false");
		values.put(MySQLiteHelper.COLUMN_ID, id); 
		database.insert(MySQLiteHelper.TABLE_PUZZLES, null, values);
		cursor = database.query(MySQLiteHelper.TABLE_PUZZLES, allColumns,
					MySQLiteHelper.COLUMN_ID + " = " + id, 
					null, null, null, null);
	}

	cursor.moveToFirst();
	SQLPuzzle newSQLPuzzle = cursorToSQLPuzzle(cursor);
	cursor.close();
	return newSQLPuzzle;
    }
    
    public void setPuzzle(long id, String solved) {
	ContentValues values = new ContentValues();
	values.put(MySQLiteHelper.COLUMN_PUZZLE, solved);
	values.put(MySQLiteHelper.COLUMN_ID, id);
 
	Cursor cursor = database.query(MySQLiteHelper.TABLE_PUZZLES, allColumns,
				       MySQLiteHelper.COLUMN_ID + " = " + id, 
				       null, null, null, null);
	if (cursor == null) {
		database.insert(MySQLiteHelper.TABLE_PUZZLES, null, values);
	}

	
	database.update(MySQLiteHelper.TABLE_PUZZLES, values, MySQLiteHelper.COLUMN_ID + " = " + id, null);
	cursor.moveToFirst();
	
	cursor.setString(1, solved)
	cursor.close();
	return;
    }

    public void deletePuzzle(SQLPuzzle puz) {
	long id = puz.getId();
	System.out.println("SQLPuzzle deleted with id: " + id);
	database.delete(MySQLiteHelper.TABLE_PUZZLES, MySQLiteHelper.COLUMN_ID
			+ " = " + id, null);
    }
    
    public List<SQLPuzzle> getAllSQLPuzzles() {
	List<SQLPuzzle> puzs = new ArrayList<SQLPuzzle>();
	
	Cursor cursor = database.query(MySQLiteHelper.TABLE_PUZZLES,
				       allColumns, null, null, null, null, null);
	
	cursor.moveToFirst();
	while (!cursor.isAfterLast()) {
	    SQLPuzzle puz = cursorToSQLPuzzle(cursor);
	    puzs.add(puz);
	    cursor.moveToNext();
	}
	// make sure to close the cursor
	cursor.close();
	return puzs;
    }
    
    private SQLPuzzle cursorToSQLPuzzle(Cursor cursor) {
	SQLPuzzle puz = new SQLPuzzle();
	puz.setId(cursor.getLong(0));
	puz.setSQLPuzzle(cursor.getString(1));
	return puz;
    }
} 
