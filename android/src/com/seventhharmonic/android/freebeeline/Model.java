package com.seventhharmonic.android.freebeeline;


import java.util.List;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
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
	public Bee mBee;
	MenuManager mMenuManager;
	private int at = -1;
	private Vibrator vibe;
	public Context context;
	private Background mTitle;
	private float[] geometry = new float[3];
	private StoryBoard mStoryBoard;
	private DataServer mDataServer;
	private StatsScreen mStatsScreen;
	public boolean createTextures = false;
	public MediaPlayer mediaPlayer;    
	
	Banner mVersionBanner;
	Store mStore;
	EasyTracker mTracker;

	TableOfContents toc;
	
	public Model(Context c) {
		mediaPlayer = MediaPlayer.create(c, R.raw.themesong);
		mediaPlayer.start();
		initiateMembers(c, new Board());
		mTracker = GlobalApplication.getGaTracker();
		
	}

	public Model(Context c, Board b){
		mediaPlayer = MediaPlayer.create(c, R.raw.themesong);
		mediaPlayer.start();	
		initiateMembers(c, b);
		mTracker = GlobalApplication.getGaTracker();
	}

	public void initiateMembers(Context c, Board b){
		mBoard = b;
		mBee = new Bee(mBoard);
		context = c;
		vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE); 
		state = new GlobalState();
		mMenuManager = new MenuManager(state, this);
		mTitle = new Background("title", .50f);
		float[] titleCenter = {.5f, 0.8f, 0.0f};
		mTitle.setCenter(titleCenter);
		mStatsScreen = new StatsScreen(state);
		mVersionBanner= new Banner(TextureManager.VERSION, .5f);
		mVersionBanner.setCenter(.5f, -1.1f);
	}
	
	public void createPuzzleFromPuzzle(Puzzle p){
		mBoard.createPuzzleFromPuzzle(p);	
		final long id  = p.getId();
		
		mBoard.setCorrectGameEventListener(new GameEventListener() {	
			public void event(int i){
				//state.state = GameState.GAME_M;
				//mMenuManager.updateState();
				//No game to save. No game to resume.
				state.saveCurrGame = false;
				state.resumeGameExists = false;
				mBee.setMood(Mood.HAPPY);
				//TODO: DO THIS DIFFERENT WITH A HARD RESET
				toc.setState();
				toc.setState();
				GlobalApplication.getDB().setPuzzle(id,"true");
		}
			});
		
		mBoard.setIncorrectGameEventListener(new GameEventListener() {
			
			public void event(int i){
			}
		});

		
	}
	
	//This is where difficulties are assigned for the different puzzle lengths:
	public void createPuzzle(int level) {
		int modifyOne = (int)(Math.random()*3);
		if (modifyOne>0) modifyOne = 2;

		int modifyTwo = (int)(Math.random()*5);
		if (modifyTwo>0 && modifyTwo < 4) {
			modifyTwo = 2;
		}
		state.saveCurrGame = true;
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

		mBoard.setCorrectGameEventListener(new GameEventListener() {	
			public void event(int i){
				state.state = GameState.GAME_MENU_END;
				mMenuManager.updateState();
				//No game to save. No game to resume.
				state.saveCurrGame = false;
				state.resumeGameExists = false;
				mDataServer.setGames(state.gametype);
				mDataServer.setFlowers(state.difficulty);
				mBee.setMood(Mood.HAPPY);			
				mTracker.send(MapBuilder
						.createEvent("game_action",     
								"board_event",  
								"game_completed",   
								null)            
								.build()
						);
		}
			});
		
		mBoard.setIncorrectGameEventListener(new GameEventListener() {
		
			public void event(int i){
				mTracker.send(MapBuilder
						.createEvent("game_action",     
								"board_event",  
								"wrong_solution",   
								null)            
								.build()
						);
			}
		});
		setState(GameState.GAME_OPENING);
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

	public void toggleRules(boolean toggle) {
		mBoard.toggleRules(toggle);
	}

	public void touched(float[] pt) {
		switch(state.state){
		case GAME_OPENING: 
		case GAME_MENU_LIST:    
		case GAME_MENU_END:
			if(mBee.touched(pt) == 1){
				//vibe.vibrate(500);
				System.out.println("Bee touched");
				mBoard.showHint();
				mTracker.send(MapBuilder
						.createEvent("ui_action",     
								"button_press",  
								"bee_buzz",   
								null)            
								.build()
						);
			}
			//Internally close menu.    		
			mBoard.touchHandler(pt);
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
			if(mBee.touched(pt) == 1){
				vibe.vibrate(100);
				mTracker.send(MapBuilder
						.createEvent("ui_action",     
								"button_press",  
								"bee_buzz",   
								null)            
								.build()
						);
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
		case GAME_MENU_LIST:
		case GAME_MENU_END:
			mBoard.draw(r);
			mBee.draw(r);
			mMenuManager.draw(r);		
			break;
		case MAIN_MENU_OPENING:
		case MAIN_MENU_LIST:
		case MAIN_MENU_NEW:
		case MAIN_MENU_OPTIONS:
		case MAIN_MENU_GEAR:
			mTitle.draw(r);
			mVersionBanner.draw(r);
			mBoard.draw(r);
			mMenuManager.draw(r);
			mBee.draw(r);
			break;
		case TABLE_OF_CONTENTS:
			toc.draw(r);
			mMenuManager.draw(r);
			mBee.draw(r);
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
		Log.d("Model","g");
		Log.d("Model", Float.toString(g[1]));
		Log.d("Model","global");
		GlobalApplication.getGeometry().setGeometry(g[0], g[1]);
		Log.d("Model", Float.toString(GlobalApplication.getGeometry().getGeometry()[1]));

		toc = new TableOfContents(this);
	}

	public void setState(GameState s){
		state.state = s;
		mBee.setState(s);
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
		case MAIN_MENU_NEW:
		case MAIN_MENU_OPTIONS:
		case MAIN_MENU_GEAR:
		case GAME_MENU_LIST:
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


