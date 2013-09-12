package com.example.android.opengl;

import android.content.Context;
import android.os.Vibrator;


class Model{
    public Board mBoard;
    public Menu mMenu;
    public Border mBorder;
    public Bee	mBee;
    public Background mBg;
    public Background mBoardBg;
    private GameState state;
    
    Vibrator vibe;
    Context context;
    
    public Model(Context c) {
	mBoard = new Board();
	mMenu = new Menu();
	mBorder = new Border(mBoard.columnSums, mBoard.rowSums);
	mBee = new Bee();
	mBg = new Background("paperbg", 2f);
	mBoardBg = new Background("boardbg", .75f);
    context = c;
	vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE); 
	state = GameState.MAIN_MENU;
    }

    public Model(Board b, Context c){
    	mBoard = b;
    	mMenu = new Menu();
    	mBorder = new Border(mBoard.columnSums, mBoard.rowSums);
    	mBee = new Bee();
    	mBg = new Background("paperbg", 2f);
    	mBoardBg = new Background("boardbg", .9f);
    	context = c;
    	vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE); 
        state = GameState.MAIN_MENU;
    }

    public void resetBoard(){
    	mBoard = new Board();
    	mBorder = new Border(mBoard.columnSums, mBoard.rowSums);
    }
    
    public void touched(float[] pt) {
    	if(state == GameState.PLAY) {
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
    					System.out.println(Integer.toString(at));
    					System.out.println(Integer.toString(mBoard.tiles[at].true_solution));
    					if(mBoard.tiles[at].true_solution != -1) {
    						mMenu.activate(pt);
    					} else {
    						mMenu.menuActive = false;
    					}
    				}	
    			}
    		}
    		else {
    			if( mBoard.touched(pt) ) {
    				int at = mBoard.activeTile;
    				pt[0] = mBoard.tiles[at].center[0];
    				pt[1] = mBoard.tiles[at].center[1];
					System.out.println(Integer.toString(at));
					System.out.println(Integer.toString(mBoard.tiles[at].true_solution));
					if(mBoard.tiles[at].true_solution != -1) {
						mMenu.activate(pt);
					} else {
						mMenu.menuActive = false;
					}
    			}
    		}
    	} else if(state==GameState.MAIN_MENU) {
    		if(pt[0]< mBee.bee.center[0]+0.25f && pt[0] > mBee.bee.center[0]-.25f 
    				&& pt[1]< mBee.bee.center[1]+0.25f && pt[1] > mBee.bee.center[1]-.25f ){
    			vibe.vibrate(500);
    		}
    		
    		if(mBoard.touched(pt)) {
    			int at = mBoard.activeTile;
    			mBoard.tiles[at].rotate = true;
    		}
    	}
    }
    
    public void swiped(float[] pt, String direction) {
	int at = mBoard.activeTile;
	if (at != -1) {
	    mBoard.tiles[at].arrow = direction; 
	    mMenu.menuActive = false;
	}
    }

    public void draw(MyGLRenderer r) {
    	mBg.draw(r);
    	mBoard.draw(r);
    	mMenu.draw(r);
    	mBee.draw(r);
    	if(state == GameState.PLAY){
        	mBoardBg.draw(r);
        	mBorder.draw(r);
    	}
    	
    }

    public void setState(GameState s){
    	state = s;
    	mBee.setState(s);
    	mBoard.setState(s);
    	System.out.println(mBee.currState());
    }
}