package com.seventhharmonic.android.freebeeline;

public interface ModelBoardInterface {
    
    // Called by board when a puzzle is completed
    public void launchDialog();
    
    // Called by board when touching the '?' icon.
    public void launchTutorial();
    
    // Called by board whena puzzle solution has been found. 
    public void puzzleSolved();

    // public BoardTile getTile(int index);
    // public int getBoardHeight();
    // public int getBoardWidth();
    // public int getPathLength();
    // public void setTileRotate(int index);
    // public int getPathToArray(int index);
    // public float[] getBeeBoxCenter();
}

