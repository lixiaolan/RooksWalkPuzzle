package com.seventhharmonic.android.freebeeline;

import java.util.List;

import com.seventhharmonic.android.freebeeline.listeners.GameEventListener;
import com.seventhharmonic.com.freebeeline.levelresources.Hint;
import com.seventhharmonic.com.freebeeline.levelresources.Puzzle;

import android.media.MediaPlayer;
import android.media.AudioManager;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;
import android.app.Activity;


class Model {

    public GlobalState state;
    public TutorialBoard2 mTutorialBoard;
    public Board mBoard;
    public MenuManager mMenuManager;
    private int at = -1;
    private Vibrator vibe;
    public Context context;
    private Background mTitle;
    private float[] geometry = new float[3];
    private StoryBoard mStoryBoard;
    private DataServer mDataServer;
    public boolean createTextures = false;
    public MediaPlayer mediaPlayer;    
    public FlowerMenu mFlowerMenu = new FlowerMenu(this);
    
    Banner mVersionBanner;
    Store mStore;
    
    TableOfContents toc;
    
    public Model(Context c) {
	mediaPlayer = MediaPlayer.create(c, R.raw.themesong);
	mediaPlayer.setLooping(true);
	mediaPlayer.start();
	initiateMembers(c, new Board(this));		
    }
    
    public Model(Context c, Board b){
	mediaPlayer = MediaPlayer.create(c, R.raw.themesong);
	mediaPlayer.start();	
	initiateMembers(c, b);
    }
    
    public void initiateMembers(Context c, Board b){
	mBoard = b;
	context = c;
	vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE); 
	state = new GlobalState();
	mMenuManager = new MenuManager(state, this);
	mTitle = new Background("title", .50f);
	float[] titleCenter = {.5f, 0.8f, 0.0f};
	mTitle.setCenter(titleCenter);
	mVersionBanner= new Banner(TextureManager.VERSION, .5f);
	mVersionBanner.setCenter(.5f, -1.1f);
    }
    
    public void createPuzzleFromPuzzle(Puzzle p){
	mBoard.createPuzzleFromPuzzle(p);	
	final long id  = p.getId();		
    }
    
    public void createTutorial(){
	mTutorialBoard = new TutorialBoard2();
	mTutorialBoard.setGeometry(geometry);
	
    }
    
    public void createStory(){
	mStoryBoard = new StoryBoard(this);
    }
    
    public void toggleHints(boolean toggle) {
	mBoard.toggleHints(toggle);
    }
    
    public void toggleLines(boolean toggle) {
	mBoard.toggleLines(toggle);
	GlobalApplication.getHintDB().addHints(10);
    }
    
    public void toggleRules(boolean toggle) {
	mBoard.toggleRules(toggle);
    }
    
    public void touched(float[] pt) {
	switch(state.state){
	case GAME_OPENING: 
	case GAME_MENU_END:
	    //Internally close menu.    		
	    mBoard.touchHandler(pt);
	    break;
	case MAIN_MENU_OPENING:  
	case FLOWER_MENU:
		mFlowerMenu.touchHandler(pt);
		break;
	case MAIN_MENU_LIST:
	case MAIN_MENU_OPTIONS:
	case MAIN_MENU_GEAR:
/*	TODO:Delete this code    at = mBoard.touched(pt);
	    if(at != -1) {
		float[] pivot = {0,0,1};
		mBoard.tiles[at].setPivot(pivot);
		mBoard.tiles[at].setRotate(true);
	    }*/
	    if(mBoard.beeTouched(pt) == 1){
		vibe.vibrate(100);
	    }
	    break;
	case TUTORIAL:
	    //Game Menu
	    mTutorialBoard.touchHandler(pt);
	    break;
	case STATS:
	    break;
	case STORY:
	    mStoryBoard.touchHandler(pt);
	    break;
	case TABLE_OF_CONTENTS:
	    toc.touchHandler(pt);
	    break;
	    
	default: break;
	}
	mMenuManager.touchHandler(pt);
    }
    
    public void swiped(float[] pt, String direction) {
	switch(state.state){
	case MAIN_MENU_OPENING:
	case FLOWER_MENU:
		mFlowerMenu.swipeHandler(direction);
		break;	
	case TABLE_OF_CONTENTS:
	    toc.swipeHandler(direction);
	    break;
	case GAME_OPENING:
	    mBoard.swipeHandler(direction);
	    break;
	    
	case TUTORIAL:
	    mTutorialBoard.swipeHandler(direction); 
	    break;
	default: break;
	}
    }
    
    public void draw(MyGLRenderer r) {
	
	switch(state.state) {
	    
	case STORY:
	    mStoryBoard.draw(r);
	    break;
	case GAME_OPENING:
	    mBoard.draw(r);   
	    mMenuManager.draw(r);		
	    break;
	case GAME_MENU_END:
	    mBoard.draw(r);
	    break;
	case MAIN_MENU_OPENING:
	case MAIN_MENU_LIST:
	case MAIN_MENU_OPTIONS:
	case MAIN_MENU_GEAR:
		mFlowerMenu.draw(r);
		mTitle.draw(r);
	    mVersionBanner.draw(r);
	    mMenuManager.draw(r);
	    break;
	case FLOWER_MENU:	
		mFlowerMenu.draw(r);
	    break;
	case TABLE_OF_CONTENTS:
	    toc.draw(r);
	    mMenuManager.draw(r);
	    break;
	case TUTORIAL:
	    mTutorialBoard.draw(r);
	    mMenuManager.draw(r);
	    break;
	default: break;
	}
    }
    
    public void setGeometry(float[] g) {
	geometry = g;
	mMenuManager.setGeometry(g);
	mBoard.setGeometry(g);
	Log.d("Model","g");
	Log.d("Model", Float.toString(g[1]));
	Log.d("Model","global");
	GlobalApplication.getGeometry().setGeometry(g[0], g[1]);
	Log.d("Model", Float.toString(GlobalApplication.getGeometry().getGeometry()[1]));	
	toc = new TableOfContents(this);
    }
    
    public void setState(GameState s){
	state.state = s;
	mBoard.setState(s);
	mMenuManager.updateState();
    }
    
    public void setDataServer(DataServer d){
	mDataServer = d;
    }
    
    public void setStore(Store s){
	mStore = s; 
    }
    
    public GameState getState() {
	return state.state;
    }
    
    public void saveGame() {
	mDataServer.saveGame(mBoard);
	state.resumeGameExists  = true;
    }
    
    public void resumeGame(){
	mDataServer.restoreGame(mBoard);
    }
    
    public void reset() {
	setState(GameState.MAIN_MENU_OPENING);
	mMenuManager.updateState();
    }
    
    public void firstRun() {
	setState(GameState.STORY);
	createStory();
	mMenuManager.updateState();
    }
    
    public void clearBoard() {
	mBoard.resetBoard();
    }    
    
    public void onBack(){
	switch(state.state){
	case MAIN_MENU_LIST:
	case MAIN_MENU_OPTIONS:
	case MAIN_MENU_GEAR:
	case STATS:
	case GAME_OPENING:
	    mMenuManager.callCallback(0);
	    break;
	case MAIN_MENU_OPENING:
	    ((Activity)context).finish();
	    break;
	default:
	    break;
	}
    }
    
  
}


