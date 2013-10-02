package com.example.android.opengl;

import android.app.Activity;

class MenuManager {
    private GlobalState state;
    public GameMenu mGameMenu;
    public Model mModel;
    private Callback mCallback;
    private float[] geometry = new float[3];
    private float[] bottomPos1 = new float[3];
    private float[] bottomPos2 = new float[3];
    private float scale1;
    private float scale2;


    public MenuManager(GlobalState s, Model m) {
    	state = s;
    	mModel = m;
    	updateState();
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
	float[] pos2 = {0f,-1.1f, 0f};
	float[] pos3 = {0f,-1.3f, 0f};
	scale1 = .25f;
	scale2  = .2f;

	float tiltAngle = -1.0f*(float)Math.PI/2;
	
	switch (state.state) {
	case MAIN_MENU_OPENING:
	    String[] textures1  = {};
	    mGameMenu = new GameMenu(pos1, scale1, textures1, TextureManager.START);
	    mCallback = new Callback_MAIN_MENU_OPENING();
	    break;
	case MAIN_MENU_LIST:
		if(state.resumeGameExists){
			String[] textures2 = {TextureManager.NEW, TextureManager.RESUME, TextureManager.TUTORIAL, TextureManager.OPTIONS, TextureManager.STORY};
			mGameMenu = new GameMenu(pos1,scale1, textures2, TextureManager.BACK); 
			mCallback = new Callback_MAIN_MENU_LIST_RESUME();
		} else {
			String[] textures2 = {TextureManager.NEW, TextureManager.TUTORIAL, TextureManager.OPTIONS, TextureManager.STORY};
			mGameMenu = new GameMenu(pos1,scale1, textures2, TextureManager.BACK); 
			mCallback = new Callback_MAIN_MENU_LIST_NORESUME();
		}
	    break;
	case MAIN_MENU_NEW:
	    String[] textures3 = {TextureManager.SHORT, TextureManager.MEDIUM, TextureManager.LONGER,  TextureManager.LONGEST};
	    mGameMenu = new GameMenu(pos1, scale1, textures3, TextureManager.BACK); 
	    mCallback = new Callback_MAIN_MENU_NEW();
	    break;
	case MAIN_MENU_OPTIONS:
	    String[] textures4 = new String[1];
	    if(state.hintsOn){
		textures4[0] = TextureManager.HINTS_ON;
	    } else {
		textures4[0] = TextureManager.HINTS_OFF;
	    }
	    
	    mGameMenu = new GameMenu(pos1, scale1, textures4, TextureManager.BACK); 
	    mCallback = new Callback_MAIN_MENU_OPTIONS();
	    break;
	case GAME_OPENING:
	    String[] textures5 = {};
	    mGameMenu = new GameMenu(bottomPos2, scale2, textures5, TextureManager.CLEAR, tiltAngle); 
	    mCallback = new Callback_GAME_OPENING();
	    break;
	case GAME_MENU_LIST:
	    String[] textures6 = {TextureManager.CLEAR_BOARD, TextureManager.SHOW_SOLUTION, TextureManager.QUIT};
	    mGameMenu = new GameMenu(bottomPos2, scale2, textures6, TextureManager.BACK, tiltAngle); 
	    mCallback = new Callback_GAME_MENU_LIST();
	    break;
	case GAME_MENU_END:
	    String[] textures7 = {TextureManager.QUIT, TextureManager.SHARE};
	    mGameMenu = new SelectTwoMenu(bottomPos2, scale2, textures7); 
	    mCallback = new Callback_GAME_MENU_END();
	    break;
	case TUTORIAL:
	    String[] textures8 = {TextureManager.QUIT};
	    mGameMenu = new SelectOneMenu(bottomPos2, scale2, textures8); 
	    mCallback = new Callback_TUTORIAL();
	    break;
	}
    }
    
    public int touched(float[] pt) {
    	return mGameMenu.touched(pt);
    }
    
    public void onTouched(int val) {
    	mCallback.callback(val);
    }
    
    public void draw(MyGLRenderer r){
    	mGameMenu.draw(r);
    }

    public void setGeometry(float[] g) {
	geometry = g;

	bottomPos1[0] = 0.0f;
	bottomPos1[1] = -geometry[1]+scale1;
	bottomPos1[2] = 0.0f;

	bottomPos2[0] = 0.0f;
	bottomPos2[1] = -geometry[1]+scale2;
	bottomPos2[2] = 0.0f;
    }
   
    
    class Callback_MAIN_MENU_OPENING extends Callback {
	@Override
	public void callback(int val) {
	    state.state = GameState.MAIN_MENU_LIST;
	    updateState();
	}	
    }
    
    class Callback_MAIN_MENU_LIST_RESUME extends Callback {
    	public void callback(int val) {
    	    switch(val) {
    		
    	    case 1: state.state = GameState.MAIN_MENU_NEW;
    		updateState();
    		break;
    	    case 2: 
    	    state.saveCurrGame = true;
    	    mModel.restoreGameUtil();
    	    mModel.setState(GameState.GAME_OPENING);
    	    state.state = GameState.GAME_OPENING;
    	    updateState();
    		break;
    	    case 3: 
    			mModel.createTutorial();
    		    mModel.setState(GameState.TUTORIAL);
    			state.state = GameState.TUTORIAL;
    			updateState();
    		    break;
    	    case 4: state.state = GameState.MAIN_MENU_OPTIONS;
    		updateState();
    		break;
    	    case 5:
    	    	state.state = GameState.STORY;
    	    	mModel.setState(GameState.STORY);
    	    	updateState();
    	    	break;
    	    case 0: state.state = GameState.MAIN_MENU_OPENING;
    		updateState();
    		break;
    	    }
    	}
        }
    
    class Callback_MAIN_MENU_LIST_NORESUME extends Callback {
	public void callback(int val) {
	    switch(val) {
		
	    case 1: state.state = GameState.MAIN_MENU_NEW;
		updateState();
		break;
	    case 2: 
			mModel.createTutorial();
		    mModel.setState(GameState.TUTORIAL);
			state.state = GameState.TUTORIAL;
			updateState();
		    break;
	    case 3: state.state = GameState.MAIN_MENU_OPTIONS;
		updateState();
		break;
	    case 4:
	    	state.state = GameState.STORY;
	    	mModel.setState(GameState.STORY);
	    	updateState();
	    	break;
	    case 0: state.state = GameState.MAIN_MENU_OPENING;
		updateState();
		break;
	    }
	}
    }
    
    class Callback_MAIN_MENU_NEW extends Callback{
    	//Is this really the right place to ensure that a savedGame file is toggled.
	public void callback(int val) {
		switch(val) {
	    case 1: mModel.createPuzzle(8,2);
	    state.saveCurrGame = true;
	    mModel.setState(GameState.GAME_OPENING);
		state.state = GameState.GAME_OPENING;
		updateState();
		break;
	    case 2: mModel.createPuzzle(10,2);
	    state.saveCurrGame = true;
		mModel.setState(GameState.GAME_OPENING);
		state.state = GameState.GAME_OPENING;
		updateState();
		break;
	    case 3: mModel.createPuzzle(12,3);
	    state.saveCurrGame = true;
		mModel.setState(GameState.GAME_OPENING);
		state.state = GameState.GAME_OPENING;
		updateState();
		break;
	    case 4: mModel.createPuzzle(14,4);
	    state.saveCurrGame = true;
		mModel.setState(GameState.GAME_OPENING);
		state.state = GameState.GAME_OPENING;
		updateState();
		break;
	    case 0: state.state = GameState.MAIN_MENU_LIST;
		updateState();
		break;
	    }
	}
    }
    
    class Callback_MAIN_MENU_OPTIONS extends Callback {
	
	@Override
	public void callback(int val) {
	    switch(val) {
	    case 1: state.hintsOn = !state.hintsOn;
		if (state.hintsOn)
		    mGameMenu.setTexture(0,TextureManager.HINTS_ON);
		else
		    mGameMenu.setTexture(0,TextureManager.HINTS_OFF);
		mModel.toggleHints(state.hintsOn);
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
		state.state = GameState.GAME_MENU_LIST;
	    updateState();
	}
	
    }
    
    class Callback_GAME_MENU_LIST extends Callback {
	
	@Override
	public void callback(int val) {
		switch(val) {
	    case 1: state.state = GameState.GAME_OPENING;
	    mModel.clearBoard();
		updateState();
		break;
	    case 2: state.state = GameState.GAME_MENU_END;		
		//No game to save. No game to resume.
		mModel.mBoard.showSolution();
		mModel.mBoard.setState(GameState.GAME_MENU_END);
		mModel.mBee.setMood(Mood.HAPPY);
		state.saveCurrGame = false;
		state.resumeGameExists = false;
		updateState();
		break;
	    case 3: state.state = GameState.MAIN_MENU_OPENING;
	    mModel.saveGame();
	    mModel.state.resumeGameExists  = true;
	    mModel.setState(GameState.MAIN_MENU_OPENING);
		updateState();
		break;
	    case 0: state.state = GameState.GAME_OPENING;
		updateState();
		break;
	    }
	}
	
    }
    
    class Callback_GAME_MENU_END extends Callback{
	
	@Override
	public void callback(int val) {
		switch(val) {
	    case 1: state.state = GameState.MAIN_MENU_OPENING;
	    mModel.setState(GameState.MAIN_MENU_OPENING);
		updateState();
		break;
	    case 2: 
	    	ShareHelper sh = new ShareHelper((Activity)mModel.context, "1","2","3","4","5");
	    	sh.share();
		break;
	    }
	    
	}
	
    }

    class Callback_TUTORIAL extends Callback {
	
	@Override
	public void callback(int val) {
	    switch(val) {
	    case 1:
		mModel.setState(GameState.MAIN_MENU_OPENING);
		updateState();
		break;
	    }
	}
    }
    
    
    
}
