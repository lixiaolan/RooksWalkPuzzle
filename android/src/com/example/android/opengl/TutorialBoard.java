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
	ONE_TILE, WALKTHOUGH, SUMMARY
    }
    
    Banner mBanner; 
    TutorailState mTutorialState;

    public TutorialBoard() {
	restoreBoard(TutorialInfo.solutionNumbers, TutorailInfo.initialNumbers, TutorailInfo.solutionArrows, TutorailInfo.initialArrows);
	banner = new Banner(.2f);
	setState(TutorialState.STATE0);
    }

    public setState(TutorialState s){
	mTutorialState = s;
    }    
}

class WalkThrough extends State<BoardTile> {

    int Counter = 0;

    public void enterAnimation() {
	    period = DrawPeriod.DURING;
    }
    
    public void duringAnimation() {

	for (int i = 0; i < tiles.length; i++) {
	    ((BoardTile)tiles[i]).setTextures();
	}
    }

    public void draw(MyGLRenderer r){
	super.draw(r);
	banner.draw(r);
	
    }

    public void setCounter(int in) {
	Counter = in;
	mBanner.set(TutorialInfo.banners[Counter]);
	tiles[TutorialInfo.activeTile[Counter]].setColor("blue"); 
    }

}




