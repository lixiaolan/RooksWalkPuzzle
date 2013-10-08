package com.example.android.opengl;



import android.content.Context;
import android.os.Vibrator;
import android.app.Activity;

class Model{
	
	public static final int SHORT = 6;
	public static final int MEDIUM = 8;
	public static final int LONGER = 12;
	public static final int LONGEST = 14;
	
    public GlobalState state;
    public TutorialBoard2 mTutorialBoard;
    public Board mBoard;
    private Menu mMenu;
    public Bee mBee;
    private MenuManager mMenuManager;
    private Background mBoardBg;
    private Background mCheck;
    private int at = -1;
    private Vibrator vibe;
    public Context context;
    private Banner mGameBanner;
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
		mMenu = new Menu();
		mBoardBg = new Background("boardbg", .75f);
		mGameBanner = new Banner(.75f);
		mTitle = new Background("title", .50f);
		float[] titleCenter = {.5f, 0.8f, 0.0f};
		mTitle.setCenter(titleCenter);
		mStoryBoard = new StoryBoard();
		mStatsScreen = new StatsScreen(state);
		
	}    
    
    public void createPuzzle(int length, int hints) {
		state.showGameBanner = false;
		mBoard.createPuzzleFromJNI(length, hints);
	}

	public void createTutorial(){
		mTutorialBoard = new TutorialBoard2();
		mTutorialBoard.setGeometry(geometry);
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
			    if (at != -1) {
			    	mBoard.tiles[at].setUserInput(val);
			    	mBoard.drawLines();
			    }
			}

			if(mCheck.touched(pt) == 1){
				if(mBoard.checkSolution()){
					state.showGameBanner = true;
					mGameBanner.set(TextureManager.GOOD_JOB);
					state.state = GameState.GAME_MENU_END;
					mMenuManager.updateState();
					//No game to save. No game to resume.
					state.saveCurrGame = false;
					state.resumeGameExists = false;
					mDataServer.setLines(state.difficulty);
					mBee.setMood(Mood.HAPPY);
					mBoard.setState(GameState.GAME_MENU_END);
				       
				} else {
					mGameBanner.set(TextureManager.TRY_AGAIN);
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
	
		default: break;
		}
	}
    
    public void swiped(float[] pt, String direction) {
		switch(state.state){
		case GAME_OPENING:
		    if (at != -1 && mBoard.tiles[at].isClickable()) {
			mBoard.tiles[at].setArrow(direction);
			mBoard.drawLines(); 
			mMenu.menuActive = false;
		    }
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
			mCheck.draw(r);
		case GAME_MENU_END:
			mBoard.draw(r);
			mBee.draw(r);
			mBoardBg.draw(r);
			mMenu.draw(r);
			if(state.showGameBanner){
				mGameBanner.draw(r);
			}
			mMenuManager.draw(r);
			
			break;
		case MAIN_MENU_OPENING:
		case MAIN_MENU_LIST:
		case MAIN_MENU_NEW:
		case MAIN_MENU_OPTIONS:
			mBoard.draw(r);
			mBee.draw(r);
			mTitle.draw(r);
			mMenuManager.draw(r);
			break;
		case TUTORIAL:
			mTutorialBoard.draw(r);
			mMenu.draw(r);
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
		int shortPuzz = mDataServer.getShortLines();
		int medPuzz = mDataServer.getMediumLines();
		int longerPuzz = mDataServer.getLongerLines();
		int longestPuzz = mDataServer.getLongestLines();		
		TM.buildLongTextures("Short Lines: "+Integer.toString(shortPuzz), 0, 30, TextureManager.SHORTSTATS, 25,  256);
		TM.buildLongTextures("Medium Lines: "+Integer.toString(medPuzz), 0, 30, TextureManager.MEDIUMSTATS, 25, 256);
		TM.buildLongTextures("Long Lines: "+Integer.toString(longerPuzz), 0, 30, TextureManager.LONGERSTATS, 25, 256);
		TM.buildLongTextures("Longest Lines: "+Integer.toString(longestPuzz), 0, 30, TextureManager.LONGESTSTATS, 25, 256);
	}
}
	

