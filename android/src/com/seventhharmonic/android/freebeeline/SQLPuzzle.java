package com.seventhharmonic.android.freebeeline;

public class SQLPuzzle {
  private long id;
  private String unlocked;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUnlocked() {
    return unlocked;
  }

  public void setUnlocked(String unlocked) {
    this.unlocked = unlocked;
  }

  // Will be used by the ArrayAdapter in the ListView
  @Override
  public String toString() {
    return unlocked;
  }
} 
