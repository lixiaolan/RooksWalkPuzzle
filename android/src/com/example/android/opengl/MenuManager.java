package com.example.android.opengl;

class MenuManager {
    private GlobalState state;
    public GameMenu mGameMenu;
    public Model mModel;


    public MenuManager(GlobalState s,) {
	state = s;
    }
    //Each case statement should have a declaration of
    //a new menu.

    // Note that we could use closure here to declare funtions (callbacks)
    // Which have access to information model!  Cool!
    public void updateState() {
	switch (state.state) {
	case MAIN_MENU_OPENING:
	    mGameMenu = new GameMenu(float[] pos, float scale, String[] textures, String center); 
	    
	    break;
	case MAIN_MENU_LIST:
	    break;
	case MAIN_MENU_NEW:
	    break;
	case MAIN_MENU_OPTION:
	    break;
	case GAME_OPENING:
	    break;
	case GAME_MENU_LIST:
	    break;
	case GAME_MENU_END:
	    break;
	}
    }

    public void touched(float[] pt) {
	int val = mGameMenu(pt);
    }

    public void onTouched(int val) {
	
    }




    
}
