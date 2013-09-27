package com.example.android.opengl;

import android.app.Activity;

class MenuManager {
    private GlobalState state;
    public GameMenu mGameMenu;
    public Model mModel;
    private Callback mCallback;
    
    public MenuManager(GlobalState s, Model m) {
	state = s;
	mModel = m;
	updateState();
    }
    //Each case statement should have a declaration of
    //a new menu.
    
    // Note that we could use closure here to declare funtions (callbacks)
    // Which have access to information model!  Cool!
    public void updateState() {
	float[] pos1 = {-.75f, 0f, 0f};
	float[] pos2 = {0f,-1.1f, 0f};
	float scale1 = .3f;
	float scale2  = .2f;
	float tiltAngle = -1.0f*(float)Math.PI/2;
	
	switch (state.state) {
	case MAIN_MENU_OPENING:
	    String[] textures1  = {};
	    mGameMenu = new GameMenu(pos1, scale1, textures1, TextureManager.START); 
	    mCallback = new Callback_MAIN_MENU_OPENING();
	    break;
	case MAIN_MENU_LIST:
		if(state.resumeGameExists){
			String[] textures2 = {TextureManager.NEW, TextureManager.RESUME, TextureManager.OPTIONS};
			mGameMenu = new GameMenu(pos1,scale1, textures2, TextureManager.CLEAR); 
			mCallback = new Callback_MAIN_MENU_LIST_RESUME();
		} else {
			String[] textures2 = {TextureManager.NEW, TextureManager.OPTIONS};
			mGameMenu = new GameMenu(pos1,scale1, textures2, TextureManager.CLEAR); 
			mCallback = new Callback_MAIN_MENU_LIST_NORESUME();
		}
	    break;
	case MAIN_MENU_NEW:
	    String[] textures3 = {TextureManager.TUTORIAL, TextureManager.SHORT, TextureManager.MEDIUM, TextureManager.LONGER,  TextureManager.LONGEST};
	    mGameMenu = new GameMenu(pos1, scale1, textures3, TextureManager.NEW); 
	    mCallback = new Callback_MAIN_MENU_NEW();
	    break;
	case MAIN_MENU_OPTIONS:
	    String[] textures4 = new String[1];
	    if(state.hintsOn){
		textures4[0] = TextureManager.HINTS_ON;
	    } else {
		textures4[0] = TextureManager.HINTS_OFF;
	    }
	    
	    mGameMenu = new GameMenu(pos1, scale1, textures4, TextureManager.OPTIONS); 
	    mCallback = new Callback_MAIN_MENU_OPTIONS();
	    break;
	case GAME_OPENING:
	    String[] textures5 = {};
	    mGameMenu = new GameMenu(pos2, scale2, textures5, TextureManager.CLEAR, tiltAngle); 
	    mCallback = new Callback_GAME_OPENING();
	    break;
	case GAME_MENU_LIST:
	    String[] textures6 = {TextureManager.CLEAR_BOARD, TextureManager.QUIT};
	    mGameMenu = new GameMenu(pos2, scale2, textures6, TextureManager.CLEAR, tiltAngle); 
	    mCallback = new Callback_GAME_MENU_LIST();
	    break;
	case GAME_MENU_END:
	    String[] textures7 = {TextureManager.QUIT, TextureManager.SHARE};
	    mGameMenu = new GameMenu(pos2, scale2, textures7, TextureManager.CLEAR, tiltAngle); 
	    mCallback = new Callback_GAME_MENU_END();
	    break;
	case TUTORIAL:
	    String[] textures8 = {TextureManager.NEXT, TextureManager.QUIT, TextureManager.PREVIOUS};
	    mGameMenu = new GameMenu(pos2, scale2, textures8, TextureManager.CLEAR, tiltAngle); 
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
    	    case 3: state.state = GameState.MAIN_MENU_OPTIONS;
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
	    case 2: state.state = GameState.MAIN_MENU_OPTIONS;
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
	    case 1: 
		mModel.setState(GameState.TUTORIAL);
		state.state = GameState.TUTORIAL;
		updateState();
	    break;
	    case 2: mModel.createPuzzle(4,2);
	    state.saveCurrGame = true;
	    mModel.setState(GameState.GAME_OPENING);
		state.state = GameState.GAME_OPENING;
		updateState();
		break;
	    case 3: mModel.createPuzzle(12,2);
	    state.saveCurrGame = true;
		mModel.setState(GameState.GAME_OPENING);
		state.state = GameState.GAME_OPENING;
		updateState();
		break;
	    case 4: mModel.createPuzzle(16,3);
	    state.saveCurrGame = true;
		mModel.setState(GameState.GAME_OPENING);
		state.state = GameState.GAME_OPENING;
		updateState();
		break;
	    case 5: mModel.createPuzzle(20,3);
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
		System.out.println("Game Menu opening");
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
	    case 2: state.state = GameState.MAIN_MENU_OPENING;
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
		mModel.mTutorialBoard.setState();
		break;
	    case 2:
		mModel.setState(GameState.MAIN_MENU_OPENING);
		updateState();
		break;
	    case 3:
		mModel.mTutorialBoard.setState();
		break;
	    }
	}
    }
    
    
    
}
