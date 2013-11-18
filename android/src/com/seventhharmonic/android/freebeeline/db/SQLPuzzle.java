package com.seventhharmonic.android.freebeeline.db;

public class SQLPuzzle {
    private long id;
    private String completed;
    private long movesUsed;
    
    public long getId() {
	return id;
    }
    
    public void setId(long id) {
	this.id = id;
    }
    
    public String getCompleted() {
	return completed;
    }
    
    public void setCompleted(String completed) {
	this.completed = completed;
    }

    public long getMovesUsed() {
	return movesUsed;
    }
    
    public void setMovesUsed(long movesUsed) {
	this.movesUsed = movesUsed;
    }
    
    @Override
    public String toString() {
	return completed;
    }
} 
