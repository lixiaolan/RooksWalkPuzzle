package com.seventhharmonic.android.freebeeline;

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
		String[] texturesGear = {TextureManager.OPTIONS, TextureManager.TUTORIAL,  TextureManager.STORY};
		mGameMenu = new GameMenu(pos1,scale1, texturesGear, TextureManager.BACK); 
		mCallback = new Callback_MAIN_MENU_GEAR();
		break;
	    case MAIN_MENU_LIST:
		/*if(state.resumeGameExists){
		    String[] textures2 = {TextureManager.RESUME, TextureManager.MENU, TextureManager.PLAY};
		    mGameMenu = new GameMenu(pos1,scale1, textures2, TextureManager.BACK); 
		    mCallback = new Callback_MAIN_MENU_LIST_RESUME();
		} else {*/
		    String[] textures2 = {TextureManager.MENU, TextureManager.PLAY};
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
		updateState();
		break;
	    case 2: 
	    	mModel.setModelToTutorial();
	    	updateState();
		break;
	    case 3:
	    	mModel.setModelToStory();
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
	    case 1: state.state = GameState.MAIN_MENU_GEAR;
	    updateState();
		break;
	    case 2:
		mModel.setModelToLevelPack();
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
		if (state.linesOn)
		    mGameMenu.setTexture(0,TextureManager.LINES_ON);
		else
		    mGameMenu.setTexture(0,TextureManager.LINES_OFF);
		mModel.toggleLines(state.linesOn);
		break;
	    case 2:
	    	state.ruleCheck = !state.ruleCheck;
		if (state.ruleCheck)
		    mGameMenu.setTexture(1,TextureManager.RULE_CHECK_ON);
		else
		    mGameMenu.setTexture(1,TextureManager.RULE_CHECK_OFF);
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
	    	updateState();   
		}
    }
    
    
    
    class Callback_TUTORIAL extends Callback {
	
	@Override
		public void callback(int val) {
	    	switch(val) {
	    	case 1:
	    		mModel.setState(GameState.GAME_OPENING);
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
    	    	mModel.setModelToLevelPack();
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

