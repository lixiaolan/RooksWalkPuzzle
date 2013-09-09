package com.example.android.opengl;

class Model{
    public Board mBoard;
    public Menu mMenu;
    public Border mBorder;

    public Model() {
	mBoard = new Board();
	mMenu = new Menu();
	mBorder = new Border(mBoard.columnSums, mBoard.rowSums);
    }

    public Model(Board b){
    	mBoard = b;
    	mMenu = new Menu();
    	mBorder = new Border(mBoard.columnSums, mBoard.rowSums);
    }

    public void resetBoard(){
    	mBoard = new Board();
    	mBorder = new Border(mBoard.columnSums, mBoard.rowSums);
    }
    
    public void touched(float[] pt) {
	if( mMenu.menuActive) {
	    mBoard.clearFlags();
	    mMenu.menuActive = false;
	    int val = mMenu.touched(pt);
	    if (val != -1) {
		int at = mBoard.activeTile;
		if (val == 0) {
		    //turn off the arrow:
		    mBoard.puzzleTiles[at].arrow = "clear";
		    mBoard.puzzleTiles[at].number = "clear";
		}
		else {
		    mBoard.puzzleTiles[at].number = Integer.toString(val);
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
    
    public void swiped(float[] pt, String direction) {
	mBoard.swiped(pt, direction);
    }

    public void animate() {
	mMenu.animate();
    }

    public void draw(MyGLRenderer r) {
	mBoard.draw(r);
	mMenu.draw(r);
    }
}
