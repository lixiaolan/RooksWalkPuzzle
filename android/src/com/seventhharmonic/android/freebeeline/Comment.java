package com.seventhharmonic.android.freebeeline;

public class Comment {
  private int id;
  private int unlocked;

  public int getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public int getUnlocked() {
    return unlocked;
  }

  public void setUnlocked(String unlocked) {
    this.unlocked = unlocked;
  }

  // Will be used by the ArrayAdapter in the ListView
  @Override
  public int toString() {
    return unlocked;
  }
} 
