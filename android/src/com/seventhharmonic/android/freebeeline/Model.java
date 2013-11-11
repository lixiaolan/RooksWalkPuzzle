package com.seventhharmonic.android.freebeeline;

import java.util.List;

import com.seventhharmonic.android.freebeeline.graphics.Geometry;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
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
    private ImageWidget mTitle;
    private Geometry geometry;
    private StoryBoard mStoryBoard;
    private DataServer mDataServer;
    public boolean createTextures = false;
    public MediaPlayer mediaPlayer;    
    public FlowerMenu mFlowerMenu;
    private boolean initializeToggle = false;
    
    TextBox mVersionBanner;
    Store mStore;
    //TextBox testBox = new TextBox(0,0,0.8f,"Create a loop and fill the board. The arrows tell you where to go and the numbers indicate how far.");
    
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
	mTitle = new ImageWidget(.5f,.8f,.5f,.5f,"title");
	mVersionBanner= new TextBox(0,0,.8f,TextureManager.VERSION);
	mVersionBanner.setCenter(0.0f, 0.0f);
    }
    
    public void createPuzzleFromPuzzle(Puzzle p){
	mBoard.createPuzzleFromPuzzle(p);
	final long id  = p.getId();		
    }
    
    public void createTutorial(){
	mTutorialBoard = new TutorialBoard2();
	mTutorialBoard.setGeometry(geometry.getGeometry());
	
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
		mMenuManager.touchHandler(pt);
	case GAME_MENU_END:
	    //Internally close menu.    		
	    mBoard.touchHandler(pt);
	    
	    break;
	case MAIN_MENU_OPENING:  
	    mMenuManager.touchHandler(pt);
	    //TODO: This may not exist yet but still be touched. We should be careful about this.
	    if(mFlowerMenu != null){
	    mFlowerMenu.touchHandler(pt);
	    }
	    break;
	case FLOWER_MENU:
	    mFlowerMenu.touchHandler(pt);
	    mMenuManager.touchHandler(pt);
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
	    //The order of these is important:
	    mFlowerMenu.touchHandler(pt);
	    mMenuManager.touchHandler(pt);
	    break;
	case TUTORIAL_MAIN_MENU:
	case TUTORIAL:
	    mTutorialBoard.touchHandler(pt);
	    mMenuManager.touchHandler(pt);
	    break;
	case STATS:
	    mMenuManager.touchHandler(pt);
	    break;
	case STORY:
	    mStoryBoard.touchHandler(pt);
	    mMenuManager.touchHandler(pt);
	    break;
	default: break;
	}

    }
    
    public void swiped(float[] pt, String direction) {
	switch(state.state){
	case MAIN_MENU_OPENING:
	    break;
	case FLOWER_MENU:
	    mFlowerMenu.swipeHandler(direction);
	    break;	
	case GAME_OPENING:
	    mBoard.swipeHandler(direction);
	    break;
	case TUTORIAL_MAIN_MENU:
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
	    mMenuManager.draw(r);
	    break;
	case TUTORIAL_MAIN_MENU:
	case TUTORIAL:
	    mTutorialBoard.draw(r);
	    mMenuManager.draw(r);
	    break;
	default: break;
	}
    }
    
    public void setGeometry(Geometry g) {
    //TODO: Should probably get the geometry statically anyways???
    geometry = g;
	mMenuManager.setGeometry(g);
	mBoard.setGeometry(g.getGeometry());
	//Log.d("Model","g");
	//Log.d("Model", Float.toString(g[1]));
	//Log.d("Model","global");
	//	Log.d("Model", Float.toString(GlobalApplication.getGeometry().getGeometry()[1]));	
	/*TODO: This is a bit of hack to make sure these classes are not initialized too early.
	*the initialize Toggle is to fix the fact that this function is called from the Renderer.
	*Can the renderer initialize before the DB????
	*/
	if(!initializeToggle){
		mFlowerMenu = new FlowerMenu(this);
		initializeToggle = true;
	}
    }
    
    public void setState(GameState s){
    	mBoard.setState(s);
    	state.state = s;
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


   //INTERFACE CLASSES FOR FLOWERMENU:   
    public void setModelToMainMenuOpening() {
	setState(GameState.MAIN_MENU_OPENING);
	System.out.println("GETTING HERE?A?A?A?A......NOPE!");
    }

    public void setModelToGameOpening(Puzzle p) {
	createPuzzleFromPuzzle(p);
	//Resets the chapter display information.
	mFlowerMenu.enterChapterSelect();
	setState(GameState.GAME_OPENING);
    }  

    public void setModelToChapterEnd(){
    	mFlowerMenu.enterChapterSelect();
    	mFlowerMenu.enterChapterEnd();
    	setState(GameState.FLOWER_MENU);
    }

    public void setModelToLevelPack() {
    	setState(GameState.FLOWER_MENU);
    	mFlowerMenu.enterLevelPack();
        }
    
    public void setModelToChapterSelect() {
    	setState(GameState.FLOWER_MENU);
    	mFlowerMenu.enterChapterSelect();
    }	
    
    
}


