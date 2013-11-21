package com.seventhharmonic.android.freebeeline.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PuzzleDataSource extends BaseDataSource{
        
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
				    MySQLiteHelper.COLUMN_PUZZLE,
				    MySQLiteHelper.COLUMN_MOVES_USED};
    
    public PuzzleDataSource(Context context, MySQLiteHelper MSQLH) {
	super(context, MSQLH);
    }
    
    public SQLPuzzle getPuzzle(long id) {
	open();

	Cursor cursor = database.query(MySQLiteHelper.TABLE_PUZZLES, allColumns,
				       MySQLiteHelper.COLUMN_ID + " = " + id, 
				       null, null, null, null);
	
	if (!cursor.moveToFirst()) {
	    long zero = 0;
	    ContentValues values = new ContentValues();
	    values.put(MySQLiteHelper.COLUMN_PUZZLE, "false");
	    values.put(MySQLiteHelper.COLUMN_ID, id); 
	    values.put(MySQLiteHelper.COLUMN_MOVES_USED, zero);
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
    
    public void setPuzzle(long id, String completed) {
	open();

	ContentValues values = new ContentValues();
	long zero = 0;
	values.put(MySQLiteHelper.COLUMN_PUZZLE, completed);
	values.put(MySQLiteHelper.COLUMN_ID, id);
 	values.put(MySQLiteHelper.COLUMN_MOVES_USED, zero);

	Cursor cursor = database.query(MySQLiteHelper.TABLE_PUZZLES, allColumns,
				       MySQLiteHelper.COLUMN_ID + " = " + id, 
				       null, null, null, null);

	if (!cursor.moveToFirst()) {
	    database.insert(MySQLiteHelper.TABLE_PUZZLES, null, values);
	}
	
	database.update(MySQLiteHelper.TABLE_PUZZLES, values, MySQLiteHelper.COLUMN_ID + " = " + id, null);
	
	cursor.close();
	return;
    }
    //overloaded so that old code is not broken.
    public void setPuzzle(long id, String completed, long movesUsed) {
	open();

	ContentValues values = new ContentValues();
	values.put(MySQLiteHelper.COLUMN_PUZZLE, completed);
	values.put(MySQLiteHelper.COLUMN_ID, id);
	values.put(MySQLiteHelper.COLUMN_MOVES_USED, movesUsed);
	
 
	Cursor cursor = database.query(MySQLiteHelper.TABLE_PUZZLES, allColumns,
				       MySQLiteHelper.COLUMN_ID + " = " + id, 
				       null, null, null, null);

	if (!cursor.moveToFirst()) {
		database.insert(MySQLiteHelper.TABLE_PUZZLES, null, values);
	}
	
	database.update(MySQLiteHelper.TABLE_PUZZLES, values, MySQLiteHelper.COLUMN_ID + " = " + id, null);
	
	cursor.close();
	return;
    }

    public void deletePuzzle(SQLPuzzle puz) {
	open();

	long id = puz.getId();
	System.out.println("SQLPuzzle deleted with id: " + id);
	database.delete(MySQLiteHelper.TABLE_PUZZLES, MySQLiteHelper.COLUMN_ID
			+ " = " + id, null);
    }
    
    public List<SQLPuzzle> getAllSQLPuzzles() {
	open();
	
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
    	puz.setCompleted(cursor.getString(1));
	puz.setMovesUsed(cursor.getLong(2));
    	return puz;
    }
} 
