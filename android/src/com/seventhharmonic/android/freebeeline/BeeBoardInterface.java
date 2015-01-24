package com.seventhharmonic.android.freebeeline;

/**
   This defines exactly how the "bee" object and the "board" object
   communicate.

   The way things are set up, this interface is actually used between
   Board and BoardBeeController. BoardBeeController then interacts
   with the Bee using BeeInterface.

   The naming is misleading.

   TODO: Change the naming of these interfaces!
 */

public interface BeeBoardInterface {
    
    public BoardTile getTile(int index);
    public int getBoardHeight();
    public int getBoardWidth();
    public int getPathLength();
    public void setTileRotate(int index);
    public int getPathToArray(int index);
    public float[] getBeeBoxCenter();
}

