package com.example.android.opengl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;


import android.os.Parcel;
import android.os.Parcelable;

class TutorialBoard extends Board {

    enum TutorialState {
	ONE_TILE, WALKTHROUGH, SUMMARY
    }
    
    Banner mBanner; 
    TutorialState mTutorialState;

    public TutorialBoard() {
    	//Worse fix ever
    	super();
    	restoreBoard(TutorialInfo.solutionNumbers, TutorialInfo.initialNumbers, TutorialInfo.solutionArrows, TutorialInfo.initialArrows);
		mBanner = new Banner(.5f);
		mTutorialState = TutorialState.WALKTHROUGH;
		state = new WalkThrough(tiles);
    }

    public void setState()	{
    	switch(mTutorialState) {
    	/*case ONE_TILE:
    		WalkThrough sl = new WalkThrough(tiles);
    		state = sl;
    		mTutorialState = TutorialState.WALKTHROUGH;
    		break;*/
    	case WALKTHROUGH: 
    			//(WalkThrough)state;
    			if(((WalkThrough)state).setCounter()){
    				mTutorialState = TutorialState.SUMMARY;
    			}
    			break;
    	/*case SUMMARY:
    		Summary sl2 = new Summary(tiles);
    		break;*/
    	}
    }
    



class WalkThrough extends State<BoardTile> {

    int Counter = -1;
    BoardTile[] tiles;
    
    public WalkThrough(BoardTile[] tiles){
    	this.tiles = tiles;
    }
    
    public void enterAnimation(BoardTile[] tiles) {
	    period = DrawPeriod.DURING;
    }
    
    public void duringAnimation(BoardTile[] tiles) {

	for (int i = 0; i < tiles.length; i++) {
	    ((BoardTile)tiles[i]).setTextures();
	}
    }
    
    @Override
    public void draw(BoardTile[] tiles, MyGLRenderer r){
    	super.draw(tiles, r);
    	//mBanner.draw(r);
    }

    public boolean setCounter() {
	Counter++;
	mBanner.set(TutorialInfo.banners[Counter]);
	tiles[TutorialInfo.activeTile[Counter]].setColor("blue"); 
	if(Counter >= 1){
		return true;
	}
	return false;
    }

}



class Summary extends State<BoardTile> {

	BoardTile[] tiles;
	
	 public Summary(BoardTile[] tiles){
	    	this.tiles = tiles;
	    }
	
    public void enterAnimation(BoardTile[] tiles) {
	    period = DrawPeriod.DURING;
    }
    
    public void duringAnimation(BoardTile[] tiles) {

	for (int i = 0; i < tiles.length; i++) {
	    ((BoardTile)tiles[i]).setTextures();
	}
    }

    public void draw(BoardTile[] tiles, MyGLRenderer r){
	super.draw(tiles,r);
	//mBanner.draw(r);
	
    }

}

}
