package com.example.android.opengl;

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;
import java.io.IOException;

class Model{
    public Board mBoard;
    public Menu mMenu;
    public Border mBorder;

    public Model() {
	mBoard = new Board();
	mMenu = new Menu();
	mBorder = new Border(mBoard.columnSums, mBoard.rowSums);
    }

    public void touched(float[] pt) {
	if( mMenu.menuActive) {
	    mBoard.clearFlags();
	    mMenu.menuActive = false;
	    int val = mMenu.touched(pt);
	    if (val != -1 ) {
		int at = mBoard.activeTile;
		mBoard.puzzleTiles[at].number = val;
		if (val == 0) {
		    //turn off the arrow:
		    mBoard.puzzleTiles[at].arrow = -1;
		}
	    }
	    else {
		if( mBoard.touched(pt) ) {
		    int at = mBoard.activeTile;
		    pt[0] = mBoard.puzzleTiles[at].center[0];
		    pt[1] = mBoard.puzzleTiles[at].center[1];
		    mMenu.activate(pt);
		}	
	    }
	}
	else {
	    if( mBoard.touched(pt) ) {
		int at = mBoard.activeTile;
		pt[0] = mBoard.puzzleTiles[at].center[0];
		pt[1] = mBoard.puzzleTiles[at].center[1];
		mMenu.activate(pt);
	    }
	}
    }
    
    public void swiped(float[] pt, int direction) {
	mBoard.swiped(pt, direction);
    }

    public void animate() {
	mMenu.animate();
    }
}
