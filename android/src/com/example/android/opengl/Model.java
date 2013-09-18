package com.example.android.opengl;

import android.content.Context;
import android.os.Vibrator;
import android.widget.Toast;


class Model{
    public GlobalState state;
    
    public Board mBoard;
    private Menu mMenu;
    private Border mBorder;
    private Bee mBee;
    private MenuManager mMenuManager;
    private Background mBg;
    private Background mBoardBg;
    private Background mCheck;
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
	mBee = new Bee(mBoard);
	//mBg = new Background("paperbg", 2f);
	mCheck  = new Background("check",.11f);
	float[] center = {-.7f,1f, 0f};
	mCheck.setCenter(center);
	context = c;
	vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE); 
	state = new GlobalState();
	mMenuManager = new MenuManager(state, this);
    }
    
    
    public void createPuzzle(int length, int hints) {
	mBoard.createPuzzleFromJNI(length, hints);
	mBorder = new Border(mBoard.columnSums, mBoard.rowSums);
	mBoardBg = new Background("boardbg", .75f);
	mMenu = new Menu();
    }
    
    public void restorePuzzle(int[] solution,String[] numbers, String[] arrows, String[] trueArrows){
	mBoard.restoreBoard(solution, numbers, arrows, trueArrows);
	mBorder = new Border(mBoard.columnSums, mBoard.rowSums);
	mBoardBg = new Background("boardbg", .75f);
	mMenu = new Menu();
    }
    
    public void toggleHints(boolean toggle) {
	mBoard.toggleHints(toggle);
    }
    
    public void touched(float[] pt) {
	int val = -1;
    switch(state.state){
	case GAME_OPENING: 
	    //Internally close menu.    		
	    val = mMenu.touched(pt);
	    if(val == -1){
		if (at != -1) {
		    mBoard.tiles[at].setColor("transparent");
		}
		at = mBoard.touched(pt);
		if(at != -1 ) {
		    if(mBoard.tiles[at].isBlack() == false) {
			mBoard.tiles[at].setColor("blue");
			if(mBoard.tiles[at].isClickable())
			    mMenu.activate(pt);
		    }
		}
	    } else {
		if (at != -1)
		    mBoard.tiles[at].setUserInput(val);
	    }
	    
	    if(mCheck.touched(pt)){
		if(mBoard.checkSolution()){
		    state.state = GameState.GAME_MENU_END;
		    mBee.setMood(Mood.HAPPY);
		} else {
		    vibe.vibrate(500);
		}
	    }
	    
	    break;
	case MAIN_MENU_OPENING:    
	case MAIN_MENU_LIST:
	case MAIN_MENU_NEW:
	case MAIN_MENU_OPTION:
	    at = mBoard.touched(pt);
	    if(at != -1) {
		float[] pivot = {0,0,1};
		mBoard.tiles[at].setPivot(pivot);
		mBoard.tiles[at].setRotate(true);
	    }
	    
	    val = mMenuManager.touched(pt);
	    if(val != -1){
	    	mMenuManager.onTouched(val);
	    }
	    
	    break;
	default: break;
	}

	if(mBee.touched(pt)){
	    vibe.vibrate(500);
	}

    }
    
    public void swiped(float[] pt, String direction) {
	switch(state.state){
	case GAME_OPENING:
	    if (at != -1 && mBoard.tiles[at].isClickable()) {
		mBoard.tiles[at].arrow = direction; 
		mMenu.menuActive = false;
	    }
	    break;
	default: break;
	}
    }
    
    public void draw(MyGLRenderer r) {
	
	//mBg.draw(r);
	mBoard.draw(r);
	mBee.draw(r);
	mMenuManager.draw(r);
	
	switch(state.state) {
	case GAME_OPENING:
	case GAME_MENU_LIST:
	case GAME_MENU_END:
	    mBoardBg.draw(r);
	    mBorder.draw(r);
	    mMenu.draw(r);
	    mCheck.draw(r);
	    break;
	default: break;
	}
    }
    
    public void setState(GameState s){
	state.state = s;
	mBee.setState(s);
	mBoard.setState(s);
    }
    
    public GameState getState() {
	return state.state;
    }
    
    public void clearBoard() {
	mBoard.clearBoard();
    }
    
}
