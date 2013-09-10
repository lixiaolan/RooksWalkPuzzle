package com.example.android.opengl;

class Model{
    public Board mBoard;
    public Menu mMenu;
    public Border mBorder;
    public Bee	mBee;
    
    private GameState state;
    
    public Model() {
	mBoard = new Board();
	mMenu = new Menu();
	mBorder = new Border(mBoard.columnSums, mBoard.rowSums);
	mBee = new Bee();
    }

    public Model(Board b){
    	mBoard = b;
    	mMenu = new Menu();
    	mBorder = new Border(mBoard.columnSums, mBoard.rowSums);
    	mBee = new Bee();
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
		    mBoard.tiles[at].arrow = "clear";
		    mBoard.tiles[at].number = "clear";
		}
		else {
		    mBoard.tiles[at].number = Integer.toString(val);
		}
	    }
	    else {
		if( mBoard.touched(pt) ) {
		    int at = mBoard.activeTile;
		    pt[0] = mBoard.tiles[at].center[0];
		    pt[1] = mBoard.tiles[at].center[1];
		    mMenu.activate(pt);
		}	
	    }
	}
	else {
	    if( mBoard.touched(pt) ) {
		int at = mBoard.activeTile;
		pt[0] = mBoard.tiles[at].center[0];
		pt[1] = mBoard.tiles[at].center[1];
		mMenu.activate(pt);
	    }
	}
		
    }
    
    public void swiped(float[] pt, String direction) {
	mBoard.swiped(pt, direction);
    }

    public void draw(MyGLRenderer r) {
	mBoard.draw(r);
	mMenu.draw(r);
	mBee.draw(r);
	//mBorder.draw(r);
    }

    public void setState(GameState s){
    	mBee.setState(s);
    	mBoard.setState(s);
    	System.out.println(mBee.currState());
    }
}
