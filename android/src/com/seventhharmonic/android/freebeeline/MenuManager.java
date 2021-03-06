package com.seventhharmonic.android.freebeeline;

import com.google.analytics.tracking.android.EasyTracker;
import com.seventhharmonic.android.freebeeline.graphics.Geometry;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;

class MenuManager {
	private GlobalState state;
	public GameMenu mGameMenu;
	public Model mModel;
	private Callback mCallback;
	private float[] bottomPos1 = new float[3];
	private float[] bottomPos2 = new float[3];
	private float[] bottomRight = new float[3];
	private float scale1;
	private float scale2;
    //EasyTracker mTracker;


	public MenuManager(GlobalState s, Model m) {
		state = s;
		mModel = m;
		updateState();
        //mTracker = GlobalApplication.getGaTracker();
	}
	//Each case statement should have a declaration of
	//a new menu.

	public void callCallback(int val) {
		mCallback.callback(val);
	}


	// Note that we could use closure here to declare funtions (callbacks)
	// Which have access to information model!  Cool!
	public void updateState() {
	    float[] pos1 = {-.75f, 0f, 0f};
	    scale1 = .25f;
	    scale2  = .15f;
	    
	    float tiltAngle = -1.0f*(float)Math.PI/2;

	    switch (state.state) {
	    
	    case MAIN_MENU_OPENING:
		String[] textures1  = {};
		mGameMenu = new GameMenu(pos1, scale1, textures1, TextureManager.START);
		mCallback = new Callback_MAIN_MENU_OPENING();
		break;
		
	    case MAIN_MENU_GEAR:
		String[] texturesGear = {TextureManager.OPTIONS, TextureManager.TUTORIAL, TextureManager.ABOUT};
		mGameMenu = new GameMenu(pos1,scale1, texturesGear, TextureManager.BACK); 
		mCallback = new Callback_MAIN_MENU_GEAR();
		break;
	    case MAIN_MENU_LIST:
		/*if(state.resumeGameExists){
		    String[] textures2 = {TextureManager.RESUME, TextureManager.MENU, TextureManager.PLAY};
		    mGameMenu = new GameMenu(pos1,scale1, textures2, TextureManager.BACK); 
		    mCallback = new Callback_MAIN_MENU_LIST_RESUME();
		} else {*/
		    String[] textures2 = {TextureManager.PLAY, TextureManager.DAILY_PUZZLE,TextureManager.MENU};
		    mGameMenu = new GameMenu(pos1,scale1, textures2, TextureManager.BACK); 
		    mCallback = new Callback_MAIN_MENU_LIST();
		break;
	    case MAIN_MENU_OPTIONS:
		String[] textures4 = new String[2];
		
		if(state.linesOn){
		    textures4[0] = TextureManager.LINES_ON;
		} else {
		    textures4[0] = TextureManager.LINES_OFF;
		}
		if(state.ruleCheck){
		    textures4[1] = TextureManager.RULE_CHECK_ON;
		} else {
		    textures4[1] = TextureManager.RULE_CHECK_OFF;
		}
		mGameMenu = new GameMenu(pos1, scale1, textures4, TextureManager.BACK); 
		mCallback = new Callback_MAIN_MENU_OPTIONS();
		break;
	    case GAME_OPENING:
		String[] textures5 = {};
		mGameMenu = new GameMenu(bottomRight, scale2, textures5, TextureManager.BACK, tiltAngle); 
		mCallback = new Callback_GAME_OPENING();
		break;
	    case DAILY_PUZZLE_GAME:
			String[] textures55 = {};
			mGameMenu = new GameMenu(bottomRight, scale2, textures55, TextureManager.BACK, tiltAngle); 
			mCallback = new Callback_DAILY_PUZZLE_GAME();
			break;
/*	    case GAME_MENU_END:
		String[] textures7 = {TextureManager.QUIT, TextureManager.SHARE};
		mGameMenu = new SelectTwoMenu(bottomPos2, scale2, textures7); 
		mCallback = new Callback_GAME_MENU_END();
		break;    */
	    
	    case TUTORIAL:
		String[] texturesTUTORIAL = {TextureManager.BACK};
		//String[] textures8 = {TextureManager.NEXT, TextureManager.PREVIOUS};
		mGameMenu = new SelectOneMenu(bottomRight, scale2, texturesTUTORIAL); 
		mCallback = new Callback_TUTORIAL();
		break;
		
	    case TUTORIAL_MAIN_MENU:
		String[] texturesTUTORIAL_MAIN_MENU = {TextureManager.DONE};
		mGameMenu = new SelectOneMenu(bottomRight, scale2, texturesTUTORIAL_MAIN_MENU); 
		mCallback = new Callback_TUTORIAL_MAIN_MENU();
		break;	
	    
	    case STORY:
		String[] textures10 = {TextureManager.BACK};
		mGameMenu = new SelectOneMenu(bottomPos2, scale2, textures10); 
		mCallback = new Callback_STORY();
		break;
		
	    case FLOWER_MENU:
		String[] texturesFM = {TextureManager.BACK};
		mGameMenu = new SelectOneMenu(bottomRight, scale2, texturesFM); 
		mCallback = new Callback_FLOWER_MENU();
		break;
		
	    case ABOUT:
			String[] texturesAbout = {TextureManager.BACK};
			mGameMenu = new SelectOneMenu(bottomRight, scale2, texturesAbout); 
			mCallback = new Callback_ABOUT();
			break;
	    
	    case DAILY_PUZZLE_LOADER:
			String[] texturesDP = {TextureManager.BACK};
			mGameMenu = new SelectOneMenu(bottomRight, scale2, texturesDP); 
			mCallback = new Callback_DAILY_PUZZLE();
			break;
		
	    }

	}
    
    public int touched(float[] pt) {
	return mGameMenu.touched(pt);
    }
    
    public void touchHandler(float[] pt) {
	int val = touched(pt);
	if(val != -1){
	    mCallback.callback(val);
	}
    }
    
    public void draw(MyGLRenderer r){
	mGameMenu.draw(r);
    }
    
    public void setGeometry(Geometry g) {
	float[] geometry = g.getGeometry();
	bottomPos1[0] = 0.0f;
	bottomPos1[1] = -geometry[1]+scale1;
	bottomPos1[2] = 0.0f;
	
	bottomPos2[0] = 0.0f;
	bottomPos2[1] = -geometry[1]+scale2;
	bottomPos2[2] = 0.0f;
	
	bottomRight[0] = -1*(geometry[0]-scale2);
	bottomRight[1] = -geometry[1]+scale2;
	bottomRight[2] = 0.0f;
    }
    
    class Callback_MAIN_MENU_OPENING extends Callback {
	@Override
	public void callback(int val) {
	    state.state = GameState.MAIN_MENU_LIST;
	    updateState();
	}	
    }
    
    class Callback_MAIN_MENU_GEAR extends Callback {
	@Override
	public void callback(int val) {
	    switch(val) {
	    case 1: state.state = GameState.MAIN_MENU_OPTIONS;
	    GlobalApplication.getAnalytics().sendScreen("main_options");
		updateState();
		break;
	    case 2: 
	    	mModel.setModelToTutorial();
	    	updateState();
		break;
	    case 3:
	    	mModel.setModelToAbout();
	    	updateState();
		break;
	    case 0: state.state = GameState.MAIN_MENU_LIST;
		updateState();
		break;
		
	    }
	    
	}
    }

    class Callback_MAIN_MENU_LIST extends Callback {
	public void callback(int val) {
	    switch(val) {
	    case 3: state.state = GameState.MAIN_MENU_GEAR;
	    	GlobalApplication.getAnalytics().sendScreen("main_gear");
	    	updateState();
		break;
	    case 1:
	    	mModel.setModelToLevelPack();
	    	updateState();
		break;
	    case 2:
	    	mModel.setModelToLoadDailyPuzzle();
			updateState();
			break;
	    case 0: state.state = GameState.MAIN_MENU_OPENING;
		updateState();
		break;
	    }
	}
    }
        
    class Callback_MAIN_MENU_OPTIONS extends Callback {
	
	@Override
	public void callback(int val) {
	    switch(val) {
		
	    case 1: state.linesOn = !state.linesOn;
		if (state.linesOn){
		    mGameMenu.setTexture(0,TextureManager.LINES_ON);
			GlobalApplication.getAnalytics().sendToggleLines(true);
		}
		else{
		    mGameMenu.setTexture(0,TextureManager.LINES_OFF);
		    GlobalApplication.getAnalytics().sendToggleErrors(false);
		}
		ViewActivity.mDataServer.saveLinesOption(state.linesOn);
		mModel.toggleLines(state.linesOn);
		break;
	    case 2:
	    	state.ruleCheck = !state.ruleCheck;
		if (state.ruleCheck){
		    mGameMenu.setTexture(1,TextureManager.RULE_CHECK_ON);
			GlobalApplication.getAnalytics().sendPuzzleShow(true);
		}
		else{
		    mGameMenu.setTexture(1,TextureManager.RULE_CHECK_OFF);
		    GlobalApplication.getAnalytics().sendToggleErrors(false);
		}
		ViewActivity.mDataServer.saveErrorCheckingOption(state.ruleCheck);
		mModel.toggleRules(state.ruleCheck);
		break;
	    case 0: state.state = GameState.MAIN_MENU_LIST;
		updateState();
		break;
	    }
	}
	
    }
    
    class Callback_GAME_OPENING extends Callback {
	@Override
		public void callback(int val) {
	    	mModel.setModelToChapterSelect();
		    GlobalApplication.getAnalytics().sendScreen("main_gear");
	    	updateState();   
		}
    }
    
    class Callback_DAILY_PUZZLE_GAME extends Callback {
    	@Override
    		public void callback(int val) {
    	    	mModel.setModelToMainMenuOpening();
    	    	mModel.toggleAdView(false);
    		    GlobalApplication.getAnalytics().sendScreen("main_gear");
    	    	updateState();   
    		}
        }
    
    
    class Callback_ABOUT extends Callback {
    	@Override
    		public void callback(int val) {
    			state.state = GameState.MAIN_MENU_GEAR;
    		    GlobalApplication.getAnalytics().sendScreen("main_gear");
    	    	updateState();   
    		}
        }
    
    class Callback_DAILY_PUZZLE extends Callback {
    	@Override
    		public void callback(int val) {
    			state.state = GameState.MAIN_MENU_LIST;
    			mModel.toggleAdView(false);
    		    GlobalApplication.getAnalytics().sendScreen("main_gear");
    	    	updateState();   
    		}
        }
    
    class Callback_TUTORIAL extends Callback {
	@Override
		public void callback(int val) {
	    	switch(val) {
	    	case 1:
	    		mModel.setState(GameState.GAME_OPENING);
	    	    GlobalApplication.getAnalytics().sendScreen("puzzle_begin");
	    		updateState();
	    		break;
	    	}
		}
    }

    class Callback_TUTORIAL_MAIN_MENU extends Callback {
    	@Override
    	public void callback(int val) {
    	    switch(val) {
    	    case 1:
    	    	if(!state.firstRun){
    	    		state.state = GameState.MAIN_MENU_GEAR;
    	    		GlobalApplication.getAnalytics().sendScreen("main_gear");
    	    	} else if(state.firstRun){
    	    		state.firstRun = false;
    	    		mModel.setModelToLevelPack();
    	    	}
    	    	updateState();
    	    	break;
    	    }
    	}
        }

    
    class Callback_FLOWER_MENU extends Callback {
	@Override
	public void callback(int val) {
	    switch(val) {
	    case 1:
	    	System.out.println("CALLED BACKSTATE");
	    	mModel.mFlowerMenu.backState();
		break;
	    }
	}
    }

    //TODO: DEPRECATED. DELETE THIS
    class Callback_STORY extends Callback { 	
	@Override
	public void callback(int val) {
	    
	    switch(val) {
	    case 1:
	    	state.state = GameState.MAIN_MENU_OPENING;
	    	mModel.setState(GameState.MAIN_MENU_OPENING);
	    	updateState();
		break;
	    }
	}
    }    
}

