package com.seventhharmonic.android.freebeeline;

public interface BeeBoardInterface {
    BoardTile getTile(int i);
    int getBoardHeight();
    int getBoardWidth();
    int getPathLength();
    void setTileRotate(int i);
    int getPathToArray(int i);
}

