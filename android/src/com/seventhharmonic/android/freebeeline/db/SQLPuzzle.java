package com.seventhharmonic.android.freebeeline.db;

public class SQLPuzzle {
  private long id;
  private String completed;

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

  @Override
  public String toString() {
    return completed;
  }
} 
