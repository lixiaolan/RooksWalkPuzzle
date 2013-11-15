package com.seventhharmonic.android.freebeeline;

public interface BeeBoardInterface {
    
    public BoardTile getTile(int index);
    public int getBoardHeight();
    public int getBoardWidth();
    public int getPathLength();
    public void setTileRotate(int index);
    public int getPathToArray(int index);

}

