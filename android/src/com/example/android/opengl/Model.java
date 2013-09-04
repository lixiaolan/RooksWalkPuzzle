package com.example.android.opengl;

import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;
import java.io.IOException;

class Model{
    public Board mBoard;
    public Menu mMenu;

    public Model() {
	mBoard = new Board();
	mMenu = new Menu();
    }

    public void touched(float[] pt) {
	if( mMenu.menuActive) {
	    mBoard.clearFlags();
	    mMenu.menuActive = false;
	    if (mMenu.touched(pt) ) {
		//do work
	    }
	    else {
		if( mBoard.touched(pt) ) {
		    pt[0] = mBoard.activeCenter[0];
		    pt[1] = mBoard.activeCenter[1];
		    mMenu.activate(pt);
		}	
	    }
	}
	else {
	    if( mBoard.touched(pt) ) {
		pt[0] = mBoard.activeCenter[0];
		pt[1] = mBoard.activeCenter[1];
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
