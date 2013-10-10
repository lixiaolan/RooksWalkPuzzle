package com.example.android.opengl;



import android.content.Context;
import android.os.Vibrator;
import android.app.Activity;

class Model{
       
    public GlobalState state;
    public TutorialBoard2 mTutorialBoard;
    public Board mBoard;
    public Bee mBee;
    private MenuManager mMenuManager;
    private Background mCheck;
    private int at = -1;
    private Vibrator vibe;
    public Context context;
    private Background mTitle;
    private float[] geometry = new float[3];
    private StoryBoard mStoryBoard;
    private DataServer mDataServer;
    private StatsScreen mStatsScreen;
    public boolean createTextures = false;
    
    
    public Model(Context c) {
		initiateMembers(c, new Board());
	}
    
    public Model(Context c, Board b){
		initiateMembers(c, b);
	}

    public void initiateMembers(Context c, Board b){
	mBoard = b;
	mBee = new Bee(mBoard);
	mCheck  = new Background("check",.11f);
	float[] center = {-.7f,-1f, 0f};
	mCheck.setCenter(center);
	context = c;
	vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE); 
	state = new GlobalState();
	mMenuManager = new MenuManager(state, this);
	mTitle = new Background("title", .50f);
	float[] titleCenter = {.5f, 0.8f, 0.0f};
	mTitle.setCenter(titleCenter);
	mStatsScreen = new StatsScreen(state);
    }    
    
    //This is where difficulties are assigned for the different puzzle lengths:
    public void createPuzzle(int level) {
	int modifyOne = (int)(Math.random()*3);
	if (modifyOne>0) modifyOne = 2;
	
	int modifyTwo = (int)(Math.random()*5);
	if (modifyTwo>0 && modifyTwo < 4) {
	    modifyTwo = 2;
	}
	    
	switch (level) {
	case 1:
		state.gametype = GameType.SHORT;
		state.difficulty = 6+(2-modifyOne);
	    mBoard.createPuzzleFromJNI(state.difficulty, 1);
	    break;
	case 2:
		state.gametype = GameType.MEDIUM;
		state.difficulty = 8+modifyTwo;
	    mBoard.createPuzzleFromJNI(state.difficulty, 2);
	    break;
	case 3:
		state.gametype = GameType.LONGER;
		state.difficulty = 12 + modifyTwo;
	    mBoard.createPuzzleFromJNI(state.difficulty, 2);
	    break;
	case 4:
		state.gametype = GameType.LONGEST;
		state.difficulty  = 14 + modifyOne;
	    mBoard.createPuzzleFromJNI(state.difficulty, 3);
	    break;
	}

	state.showGameBanner = false;
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
    }
    
    public void touched(float[] pt) {
	int val = -1;
	switch(state.state){
	case GAME_OPENING: 
	    //Internally close menu.    		
	    mBoard.touchHandler(pt);
	    
	    if(mCheck.touched(pt) == 1){
		if(mBoard.checkSolution()){
		    state.showGameBanner = true;
		    state.state = GameState.GAME_MENU_END;
		    mMenuManager.updateState();
		    //No game to save. No game to resume.
		    state.saveCurrGame = false;
		    state.resumeGameExists = false;
		    mDataServer.setGames(state.gametype);
		    System.out.println("What difficulty am I at?");
		    System.out.println(state.difficulty);
		    mDataServer.setFlowers(state.difficulty);
		    mBee.setMood(Mood.HAPPY);
		    mBoard.setState(GameState.GAME_MENU_END);
		    
		} else {
		    mBoard.mGameBanner.set(TextureManager.TRY_AGAIN);
		    state.showGameBanner = true;
		    vibe.vibrate(500);
		}
	    }
	    
	    
	case GAME_MENU_LIST:    
	case GAME_MENU_END:
	    val = mMenuManager.touched(pt);
	    if(val != -1){
		mMenuManager.onTouched(val);
	    }
	    if(mBee.touched(pt) == 1){
		vibe.vibrate(500);
	    }
	    break;
	case MAIN_MENU_OPENING:    
	case MAIN_MENU_LIST:
	case MAIN_MENU_NEW:
	case MAIN_MENU_OPTIONS:
	case MAIN_MENU_GEAR:
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
	    if(mBee.touched(pt) == 1){
		vibe.vibrate(500);
	    }
	    break;
	case TUTORIAL:
	    //Game Menu
	    val = mMenuManager.touched(pt);
	    if(val != -1){
		mMenuManager.onTouched(val);
	    }
	    mTutorialBoard.touchHandler(pt);
	    break;
	    
	case STATS:
	    val = mMenuManager.touched(pt);
	    if(val != -1){
		mMenuManager.onTouched(val);
	    }
	    break;
	case STORY:
	    val = mMenuManager.touched(pt);
	    if(val != -1){
		mMenuManager.onTouched(val);
	    }
	    mStoryBoard.touchHandler(pt);
	default: break;
	}
    }
    
    public void swiped(float[] pt, String direction) {
		switch(state.state){
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
		case GAME_MENU_LIST:
		case GAME_MENU_END:
			mCheck.draw(r);
			mBoard.draw(r);
			mBee.draw(r);
			mMenuManager.draw(r);		
			break;
		case MAIN_MENU_OPENING:
		case MAIN_MENU_LIST:
		case MAIN_MENU_NEW:
		case MAIN_MENU_OPTIONS:
		case MAIN_MENU_GEAR:
			mBoard.draw(r);
			mBee.draw(r);
			mTitle.draw(r);
			mMenuManager.draw(r);
			break;
		case TUTORIAL:
			mTutorialBoard.draw(r);
			mMenuManager.draw(r);
			break;
		case STATS:
			mStatsScreen.draw(r);
			mMenuManager.draw(r);
			break;
		default: break;
		}
		
	}
    
    public void setGeometry(float[] g) {
    	geometry = g;
    	mMenuManager.setGeometry(g);
    	mStatsScreen.setGeometry(g);
    	mBoard.setGeometry(g);
    }

    public void setState(GameState s){
		state.state = s;
		mBee.setState(s);
		mBoard.setState(s);
	}

	public void setDataServer(DataServer d){
		mDataServer = d;
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
	     	state.showGameBanner = false;
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
		case MAIN_MENU_NEW:
		case MAIN_MENU_OPTIONS:
		case GAME_MENU_LIST:
		case STATS:
		   mMenuManager.callCallback(0);
		   break;
		   
		case MAIN_MENU_OPENING:
			((Activity)context).finish();
			break;
		default:
			break;
		}
		}

	public void updateStats(TextureManager TM) {
		int shortPuzz = mDataServer.getShortGames();
		int medPuzz = mDataServer.getMediumGames();
		int longerPuzz = mDataServer.getLongerGames();
		int longestPuzz = mDataServer.getLongestGames();		
		int flowersVisited = mDataServer.getFlowersVisited();
		
		TM.buildLongTextures("Short Games: "+Integer.toString(shortPuzz), 0, 30, TextureManager.SHORTSTATS, 25,  256);
		TM.buildLongTextures("Medium Games: "+Integer.toString(medPuzz), 0, 30, TextureManager.MEDIUMSTATS, 25, 256);
		TM.buildLongTextures("Longer Games: "+Integer.toString(longerPuzz), 0, 30, TextureManager.LONGERSTATS, 25, 256);
		TM.buildLongTextures("Longest Games: "+Integer.toString(longestPuzz), 0, 30, TextureManager.LONGESTSTATS, 25, 256);
		TM.buildLongTextures("Flowers Visited: "+Integer.toString(flowersVisited), 0, 30, TextureManager.FLOWERSVISITED, 25, 256);
	}

	


}
	

