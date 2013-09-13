package com.example.android.opengl;

import android.content.Context;
import android.os.Vibrator;


class Model{
    private Board mBoard;
    private Menu mMenu;
    private Border mBorder;
    private Bee	mBee;
    private Background mBg;
    private Background mBoardBg;
    private GameState state;
    private int at = -1;
    private Vibrator vibe;
    private Context context;
    
    public Model(Context c) {
    	initiateMembers(c, new Board());
    }

    public Model(Context c, Board b){
    	initiateMembers(c, b);
    }

    public void initiateMembers(Context c, Board b){
    	mBoard = b;
    	mMenu = new Menu();
    	mBorder = new Border(mBoard.columnSums, mBoard.rowSums);
    	mBee = new Bee(mBoard);
    	mBg = new Background("paperbg", 2f);
    	mBoardBg = new Background("boardbg", .75f);
    	context = c;
    	vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE); 
    	state = GameState.MAIN_MENU;
    }
    
    public void createPuzzle(int length) {
    	mBoard.createPuzzle(length);
    	mBorder = new Border(mBoard.columnSums, mBoard.rowSums);
    }
    
    public void touched(float[] pt) {
    	switch(state){
    		case PLAY: 
    			//Internally close menu.    		
    			int val = mMenu.touched(pt);
    			if(val == -1){
    				if (at != -1) {
    					mBoard.tiles[at].setColor("transparent");
    				}
    				at = mBoard.touched(pt);
    				if(at != -1 ) {
    					if(mBoard.tiles[at].true_solution != -1) {
    						mBoard.tiles[at].setColor("blue");
    						mMenu.activate(pt);
    					}
    				}
    			} else {
    				if (at != -1)
    					mBoard.tiles[at].setUserInput(val);
    			}
    				
    			break;
    	
    	case MAIN_MENU: 
    		at = mBoard.touched(pt);
    		if(at != -1) {
    			float[] pivot = {0,0,1};
    			mBoard.setRotate(at, pivot);
    		}
    		break;
    		
    	
    	case GAME_MENU: break;
    	
    	}
  
    	if(mBee.touched(pt)){
			vibe.vibrate(500);
		}
    	}
    
    
    public void swiped(float[] pt, String direction) {
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
    	if(state != GameState.MAIN_MENU){
        	mBoardBg.draw(r);
        	mBorder.draw(r);
    	}
    	
    }

    public void setState(GameState s){
    	state = s;
    	mBee.setState(s);
    	mBoard.setState(s);
    }
}
