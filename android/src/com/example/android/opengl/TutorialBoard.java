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
	ONE_TILE, WALKTHROUGH_INIT,WALKTHROUGH, SUMMARY
    }
    
    Banner mBanner; 
    TutorialState mTutorialState;
    TutorialInfo mTutorialInfo = new TutorialInfo();
    
    public TutorialBoard() {
    	//Worse fix ever
    	super();
    	restoreBoard(TutorialInfo.solutionNumbers, TutorialInfo.initialNumbers, TutorialInfo.initialArrows, TutorialInfo.solutionArrows);
    	mBanner = new Banner(.7f);
    	mBanner.setCenter(0,.8f);
    	mTutorialState = TutorialState.WALKTHROUGH_INIT;
    }
    
    public boolean userNumberInput(int val) {
	if (val > 0) {
	    System.out.println("val = " + Integer.toString(val));
	    System.out.println("correct num = " + Integer.toString( mTutorialInfo.solutionNumbers[mTutorialInfo.activeTile[mTutorialInfo.Counter]]));
	    if (val == mTutorialInfo.solutionNumbers[mTutorialInfo.activeTile[mTutorialInfo.Counter]]) {
		tiles[mTutorialInfo.activeTile[mTutorialInfo.Counter]].setUserInput(val);
		return true;
	    }
	}
	return false;
    }
    
    
    public void setState()	{
	switch(mTutorialState) {
	    /*case ONE_TILE:
	      WalkThrough sl = new WalkThrough(tiles);
	      state = sl;
	      mTutorialState = TutorialState.WALKTHROUGH;
	      break;*/
	case WALKTHROUGH_INIT:
	    state = new WalkThrough(tiles, mTutorialInfo);
	    mTutorialState = TutorialState.WALKTHROUGH;
	    break;
	case WALKTHROUGH: 
	    boolean b = ((WalkThrough)state).setCounter();
	    System.out.println("I Got into Walkthrough");
	    if(!b){
		state = new Summary(tiles);
		mTutorialState = TutorialState.SUMMARY;
		setState();
	    }
	    break;
	case SUMMARY:
	    
	    break;
	}
    }
    
    class WalkThrough extends State<BoardTile> {
	
	BoardTile[] tiles;
	long refTime;
	TutorialInfo TI;
	
	public WalkThrough(BoardTile[] tiles, TutorialInfo inTI){
	    this.tiles = tiles;
	    refTime = System.currentTimeMillis();
	    TI = inTI;
	    TI.Counter = -1;
	}
	
	public void enterAnimation(BoardTile[] tiles) {
	    long time = System.currentTimeMillis() - refTime;
	    float totalTime = 2000f;
	    
	    if(time < totalTime) {
		for (int i = 0; i < tiles.length; i++) {
		    tiles[i].setTextures(TextureManager.CLEAR, tiles[i].flowerTexture);
		    float Sx = ( (i/6) - 2.5f )/4.0f;
		    float Sy = ( (i%6) - 2.5f )/4.0f;
		    tiles[i].setSize(.12f*time/totalTime+(1-time/totalTime)*.15f);
		    float newX = tiles[i].center[0]+time/totalTime*(Sx - tiles[i].center[0]);
		    float newY = tiles[i].center[1]+time/totalTime*(Sy - tiles[i].center[1]);
		    float center[] = { newX, newY, 0.0f};
		    tiles[i].center = center;
		}			
	    } 
	    else if( time < 4000.0f && time > totalTime) {
		float[] pivot = {1,0,0};
		for (int i = 0; i < tiles.length; i++) {
		    tiles[i].setPivot(pivot);
		    tiles[i].setAngle((time-totalTime)*.045f);
		}
	    }	
	    else {
		for(int i = 0;i<tiles.length;i++){
		    tiles[i].setAngle(0);
		    tiles[i].setSize(.12f);
		    tiles[i].setColor("transparent");
		}
		period = DrawPeriod.DURING;
		setState();
	    }
	}
	
	public void duringAnimation(BoardTile[] tiles) {
	    for (int i = 0; i < tiles.length; i++) {
		((BoardTile)tiles[i]).setTextures();
	    }
	}	
	
	@Override
	    public void draw(BoardTile[] tiles, MyGLRenderer r){
	    super.draw(tiles, r);
	    mBanner.draw(r);
	}
	
	public boolean setCounter() {
	    if (TI.Counter >= 0) {
		tiles[TI.activeTile[TI.Counter]].setColor("white");
	    } 
	    TI.Counter += 1;
	    mBanner.set("banner_"+Integer.toString(TI.Counter));
	    if(TI.Counter >= TI.activeTile.length){
		return false;
	    }
	    tiles[TI.activeTile[TI.Counter]].setColor("blue"); 
	    return true;
	}
	
	@Override
	    public int touched(float[] pt) {
	    
	    int in = -1;
	    for (int i = 0; i < tiles.length; i++) {
		if( tiles[i].touched(pt)) {
		    in =  i;
		    break;
		}
	    }
	    
	    System.out.println("in: " + Integer.toString(in));
	    
	    if (TI.Counter < TI.activeTile.length)
		if (in == TI.activeTile[TI.Counter]) 
		    return in;
	    return -1;   
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
	
	@Override
	    public int touched(float[] pt) {
	    return -1;
	}	
	
	
    }
    
}
