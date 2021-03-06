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
import android.view.View;
import android.app.Activity;


class Model implements ModelBoardInterface{

    public GlobalState state;
    public TutorialBoard2 mTutorialBoard;
    public Board mBoard;
    public MenuManager mMenuManager;
    private int at = -1;
    private Vibrator vibe;
    public Activity context;
    private ImageWidget mTitle;
    private TextToggleButtonWidget muteButton;
    private Geometry geometry;
    private StoryBoard mStoryBoard;
    private AboutScreenWidget mAboutScreen;
    private DataServer mDataServer;
    public boolean createTextures = false;
    public MediaPlayer mediaPlayer;    
    public FlowerMenu mFlowerMenu;
    private boolean initializeToggle = false;
    private DailyPuzzleLoadWidget mDailyPuzzleLoadWidget;
    private EndGameDialogWidgetLayout mDialog;
    
    //TextBox mVersionBanner;
    
    Store mStore;
    
    public Model(Activity c) {
    	initiateMembers(c, new Board(this));		
    }
    
    
    public void initiateMembers(Activity c, Board b){
    	mBoard = b;
    	context = c;
    	vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE); 
	state = new GlobalState();
	mMenuManager = new MenuManager(state, this);
	mTitle = new ImageWidget(.5f,.8f,.5f,.5f,"title");
	mDailyPuzzleLoadWidget = new DailyPuzzleLoadWidget(this);
	
	//mVersionBanner= new TextBox(0,0,.8f,TextureManager.VERSION);
	//mVersionBanner.setCenter(0.0f, 0.0f);
	muteButton = new TextToggleButtonWidget(10.0f,.1f,.1f, .1f, TextureManager.SPEAKER_ON, TextureManager.SPEAKER_OFF);
	muteButton.setBorder(false);
	muteButton.setClickListener(new GameEventListener() {
		@Override
		public void event(int i) {
		    GlobalApplication.getAnalytics().sendMuteSound(muteButton.getToggle());
		    GlobalApplication.getMyMusic().toggleMusic();
		    ViewActivity.mDataServer.saveMusicToggle(GlobalApplication.getMyMusic().isPlaying());
		}
	    });
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
    
    public void toggleLines(boolean toggle) {
	mBoard.toggleLines(toggle);
	//This used to be the secrete way to get hints in game :)  Cute!
	//GlobalApplication.getHintDB().addHints(10);
    }
    
    public void toggleRules(boolean toggle) {
    	mBoard.toggleRules(toggle);
    }
    
    public void touched(float[] pt) {
	switch(state.state){
	case DAILY_PUZZLE_GAME: 
	case GAME_OPENING: 
	    mMenuManager.touchHandler(pt);
	    mBoard.touchHandler(pt);
	    break;
	case GAME_MENU_END:
	    //Internally close menu.    		
	    //mBoard.touchHandler(pt);
	    mDialog.touchHandler(pt);
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
	    //The order of these is important:
	    mFlowerMenu.touchHandler(pt);
	    mMenuManager.touchHandler(pt);
	    break;
	case TUTORIAL_MAIN_MENU:
	case TUTORIAL:
	    mTutorialBoard.touchHandler(pt);
	    mMenuManager.touchHandler(pt);
	    break;
	case STORY:
	    mStoryBoard.touchHandler(pt);
	    mMenuManager.touchHandler(pt);
	    break;
	case DAILY_PUZZLE_LOADER:
	case ABOUT:
	    mMenuManager.touchHandler(pt);
	    break;
	default: break;
	}
	muteButton.touchHandler(pt);
    }
    
    public void swiped(float[] pt, String direction) {
	switch(state.state){
	case MAIN_MENU_OPENING:
	    break;
	case FLOWER_MENU:
	    mFlowerMenu.swipeHandler(direction);
	    break;	
	case DAILY_PUZZLE_GAME:
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
	case DAILY_PUZZLE_GAME:
	case GAME_OPENING:
	    mBoard.draw(r);   
	    mMenuManager.draw(r);		
	    break;
	case GAME_MENU_END:
	    mBoard.draw(r);
	    mDialog.draw(r);
	    break;
	case MAIN_MENU_OPENING:
	case MAIN_MENU_LIST:
	case MAIN_MENU_OPTIONS:
	case MAIN_MENU_GEAR:
	    mFlowerMenu.draw(r);
	    mTitle.draw(r);
	    mMenuManager.draw(r);
	    break;
	case FLOWER_MENU:	
	    mFlowerMenu.draw(r);
	    if(!mFlowerMenu.inChapterEndState())
	    	mMenuManager.draw(r);
	    break;
	case TUTORIAL_MAIN_MENU:
	case TUTORIAL:
	    mTutorialBoard.draw(r);
	    mMenuManager.draw(r);
	    break;
	case DAILY_PUZZLE_LOADER:
		mDailyPuzzleLoadWidget.draw(r);
		mMenuManager.draw(r);
		break;
	case ABOUT:
	    mAboutScreen.draw(r);
	    mMenuManager.draw(r);
	    break;
	default: break;
	}
	muteButton.draw(r);
    }
    
    public void setGeometry(Geometry g) {
    //TODO: Should probably get the geometry statically anyways???
    geometry = g;
	mMenuManager.setGeometry(g);
	mBoard.setGeometry(g.getGeometry());

	/*TODO: This is a bit of hack to make sure these classes are not initialized too early.
	*	the initialize Toggle is to fix the fact that this function is called from the Renderer.
	*	Can the renderer initialize before the DB????
	*/
	if(!initializeToggle){
		mFlowerMenu = new FlowerMenu(this);
		mAboutScreen = new AboutScreenWidget();
		initializeToggle = true;
	}

	mTitle.setCenter(1.0f-0.5f,GlobalApplication.getGeometry().getGeometry()[1]-.6f);
	muteButton.setCenter(-1.0f+0.1f,GlobalApplication.getGeometry().getGeometry()[1]-.1f);
    }
    
    public void setState(GameState s){
    	mBoard.setState(s);
    	state.state = s;
    	mMenuManager.updateState();
    }

    public void setDataServer(DataServer d){
    	mDataServer = d;
    	state.linesOn = d.getLinesOption();
    	state.ruleCheck = d.getErrorCheckingOption();
    	boolean musicToggle = mDataServer.getMusicToggle();
    	GlobalApplication.getMyMusic().playSong("default");
    	//GlobalApplication.getMyMusic().setMusic(musicToggle);
    	//TODO: MAKE THIS BETTER> 
    	if(!musicToggle){
    		muteButton.touchHandler(muteButton.getCenter());
    	}
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
    
   
    //Broken on Game_Menu_End, - this might be okay.
    public void onBack(){
	switch(state.state){
	case MAIN_MENU_LIST:
	case MAIN_MENU_OPTIONS:
	case MAIN_MENU_GEAR:
	case DAILY_PUZZLE_LOADER:
	case ABOUT:
		mMenuManager.callCallback(0);
	    break;
	case MAIN_MENU_OPENING:
	    ((Activity)context).finish();
	    break;
	case TUTORIAL_MAIN_MENU:
	case TUTORIAL:
	    mMenuManager.callCallback(1);
	    break;
	case DAILY_PUZZLE_GAME:
	case GAME_OPENING:
	    mMenuManager.callCallback(0);
	    break;
	case FLOWER_MENU:
	    mMenuManager.callCallback(1);
	    break;
	default:
	    break;
	}
    }

   //INTERFACE CLASSES FOR FLOWERMENU:   
    public void setModelToMainMenuOpening() {
    	GlobalApplication.getAnalytics().sendScreen("main_menu");
	setState(GameState.MAIN_MENU_OPENING);
    }

    public void setModelToGameOpening(Puzzle p) {
	createPuzzleFromPuzzle(p);
	mDialog = new PuzzlePackPuzzleEndGameDialogWidgetLayout(this);
	GlobalApplication.getAnalytics().sendScreen("puzzle_begin");
	//Resets the chapter display information.
	mFlowerMenu.enterChapterSelect();
	setState(GameState.GAME_OPENING);
    }  

    public void setModelToChapterEnd(){
    	mFlowerMenu.enterChapterEnd();
    	GlobalApplication.getAnalytics().sendScreen("chapter_end");
    	setState(GameState.FLOWER_MENU);
    }

    public void setModelToLoadDailyPuzzle(){
    	setState(GameState.DAILY_PUZZLE_LOADER);
    	toggleAdView(true);
    	GlobalApplication.getAnalytics().sendScreen("daily_puzzle_download");
    	mDailyPuzzleLoadWidget.downloadPuzzle();
    	
    }
    
    public void setModelToPlayDailyPuzzle(Puzzle p){
    	createPuzzleFromPuzzle(p);
    	mDialog = new DailyPuzzleEndGameDialogWidgetLayout(this);
    	GlobalApplication.getAnalytics().sendScreen("daily_puzzle_begin");
    	setState(GameState.DAILY_PUZZLE_GAME);
    }
    
    public void setModelToLevelPack() {
    	if(state.firstRun){
    		setModelToTutorial();    		
    		return;
    	}
    	GlobalApplication.getAnalytics().sendScreen("level_pack_select");
    	setState(GameState.FLOWER_MENU);
    	mFlowerMenu.enterLevelPack();
        }
    
    public void setModelToChapterSelect() {
    	setState(GameState.FLOWER_MENU);
    	GlobalApplication.getAnalytics().sendScreen("chapter_select");
    	mFlowerMenu.enterChapterSelect();
    }	
    
    public void setModelToTutorial(){
    	createTutorial();
    	GlobalApplication.getAnalytics().sendScreen("main_tutorial");
		setState(GameState.TUTORIAL_MAIN_MENU);
    }
    
    public void setModelToStory(){
    	createStory();
    	GlobalApplication.getAnalytics().sendScreen("main_story");
    	setState(GameState.STORY);
    }
    
    public void setModelToAbout(){
    	setState(GameState.ABOUT);
    	GlobalApplication.getAnalytics().sendScreen("main_about");

    }


    //INTERFACE FOR BOARD dialog
    // Called by board when a puzzle is completed
    public void launchDialog(){
	//extract needed info from board;
    	//mDialog = new EndGameDialogWidgetLayout(this);
    	return;
    }
    
    // Called by board when touching the '?' icon.
    public void launchTutorial(){
    	GlobalApplication.getAnalytics().sendScreen("puzzle_tutorial");
    	createTutorial();
    	setState(GameState.TUTORIAL);
    	return;
    }
    
    public void toggleAdView(boolean set){
		context.findViewById(R.id.adView).setVisibility((set)? View.VISIBLE: View.INVISIBLE);
    }
    // Called by board when a puzzle solution has been found. 
    public void puzzleSolved(){
        toggleAdView(false);
    	setState(GameState.GAME_MENU_END);
    	return;
    }    
}


