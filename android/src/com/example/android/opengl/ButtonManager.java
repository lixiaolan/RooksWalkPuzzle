package com.example.android.opengl;



import android.app.Activity;
import android.view.View;
import android.widget.Button;



//Manages transitions between MenuStates.
class ButtonManager{
    public StateButton bee_puzzled;

    public StateButton new_game;
    public StateButton resume;
    public StateButton options;
    public StateButton stats;
    
    public StateButton short_puz;
    public StateButton medium_puz;
    public StateButton longer_puz;
    public StateButton longest_puz;

    public StateButton hints;
    public MenuState mMenuState;
    
    private float h;
    private float w;
    
    private float[] offSet = {.1f, .2f, .3f};
    private float[] stack = {.1f, .2f, .3f, .4f, .5f, .6f, .7f, .8f, .9f};

    private long[] MoveFadeIn = {0,1000,1000};
    private long[] FadeOutMove = {1000, 1000, 0};
    private long[] FadeIn = {0,0,1000};
    private long[] Move = {0,1000,0};
    private long[] None = {0,0,0};


    public ButtonManager(MenuState ms, Activity va) {

	mMenuState = ms;
	
	bee_puzzled = new StateButton((Button)va.findViewById(R.id.bee_puzzled));

	new_game = new StateButton((Button)va.findViewById(R.id.new_game));
	resume = new StateButton((Button)va.findViewById(R.id.resume));
	options = new StateButton((Button)va.findViewById(R.id.options));
	stats = new StateButton((Button)va.findViewById(R.id.stats));

	hints = new StateButton((Button)va.findViewById(R.id.hints));

	if (ms.hints)
	    hints.mButton.setText("Hints On");
	else
	    hints.mButton.setText("Hints Off");

	short_puz = new StateButton((Button)va.findViewById(R.id.short_puz));
	medium_puz = new StateButton((Button)va.findViewById(R.id.medium_puz));
	longer_puz = new StateButton((Button)va.findViewById(R.id.longer_puz));
	longest_puz = new StateButton((Button)va.findViewById(R.id.longest_puz));

    }

    public void setHW(float height, float width) {
	h = height;
	w = width;
	switchState();
    }

    public void manageState(View v, GameState state) {	
	 switch (v.getId()) {
	    case R.id.bee_puzzled:
	    	if(mMenuState.open == false){
	    		switch(state){
	    			case MAIN_MENU: 
	    				mMenuState.state = MenuStateEnum.MENU_MAIN_MENU;
	    				break;
	    			case PLAY: 
	    				mMenuState.state = MenuStateEnum.MENU_DURING_GAME;
	    				break;
	    		}
	    		mMenuState.open = true;
	    	} else {
	    		mMenuState.open = false;
	    		switch(state){
    			case MAIN_MENU: 
    				mMenuState.state = MenuStateEnum.OPENING;
    				break;
    			case PLAY: 
    				mMenuState.state = MenuStateEnum.GAME_PLAY;
    				break;
    		}
	    	}
	    	break;

	    case R.id.new_game:
	    	mMenuState.state = MenuStateEnum.NEW;
	    	break;

	    case R.id.resume:
	    	mMenuState.state = MenuStateEnum.GAME_RESUME;
	    	mMenuState.createGame = true;
	    	break;

	    case R.id.options:
	    	mMenuState.state = MenuStateEnum.OPTIONS;
	    	break;

	    case R.id.hints:
		mMenuState.hints = !mMenuState.hints;
		if (mMenuState.hints)
		    hints.mButton.setText("Hints On");
		else
		    hints.mButton.setText("Hints Off");
	    	break;


	    // case R.id.stats:
	    // 	mMenuState.state = MenuStateEnum.STATS;
	    // 	break;
		
	    case R.id.short_puz:
		mMenuState.state = MenuStateEnum.GAME_PLAY;
		mMenuState.difficulty = 1;
		mMenuState.createGame = true;
		break;
		
	    case R.id.medium_puz:
		mMenuState.state = MenuStateEnum.GAME_PLAY;
		mMenuState.difficulty = 2;
		mMenuState.createGame = true;
		break;

	    case R.id.longer_puz:
		mMenuState.state = MenuStateEnum.GAME_PLAY;
		mMenuState.difficulty = 3;
		mMenuState.createGame = true;
		break;

  	    case R.id.longest_puz:
		mMenuState.state = MenuStateEnum.GAME_PLAY;
		mMenuState.difficulty = 4;
		mMenuState.createGame = true;
		break;

	    }
	 switchState();
    }
    
    public void switchState() {	

	switch (mMenuState.state) {
	case OPENING:
	    bee_puzzled.setState(offSet[0]*w,0.5f*h, true,FadeIn);

	    new_game.setState(offSet[1]*w,0.0f*h, false,None);
	    resume.setState(offSet[1]*w,0.0f*h, false,None);
	    options.setState(offSet[1]*w,0.0f*h, false,None);
	    stats.setState(offSet[1]*w,0.0f*h, false,None);

	    hints.setState(offSet[2]*w,0.0f*h, false,None);

	    short_puz.setState(offSet[2]*w,0.0f*h, false,None);
	    medium_puz.setState(offSet[2]*w,0.0f*h, false,None);
	    longer_puz.setState(offSet[2]*w,0.0f*h, false,None);
	    longest_puz.setState(offSet[2]*w,0.0f*h, false,None);
	    break;

	case MENU_MAIN_MENU:

	    bee_puzzled.setState(offSet[0]*w,stack[0]*h, true,MoveFadeIn);

	    new_game.setState(offSet[1]*w,stack[1]*h, true,MoveFadeIn);
	    resume.setState(offSet[1]*w,stack[2]*h, true,MoveFadeIn);
	    options.setState(offSet[1]*w,stack[3]*h, true,MoveFadeIn);
	    stats.setState(offSet[1]*w,stack[4]*h, true,MoveFadeIn);

	    hints.setState(offSet[2]*w,0.0f*h, false, None);

	    short_puz.setState(offSet[2]*w,0.0f*h, false,None);
	    medium_puz.setState(offSet[2]*w,0.0f*h, false,None);
	    longer_puz.setState(offSet[2]*w,0.0f*h, false,None);
	    longest_puz.setState(offSet[2]*w,0.0f*h, false,None);
	    break;

	 case MENU_DURING_GAME:
		    bee_puzzled.setState(offSet[0]*w,stack[0]*h, true,Move);

		    new_game.setState(offSet[1]*w,stack[1]*h, true,None);
		    resume.setState(offSet[1]*w,0.0f*h, false,None);
		    options.setState(offSet[1]*w,0.0f*h, false,None);
		    stats.setState(offSet[1]*w,0.0f*h, false,None);

		    hints.setState(offSet[2]*w,0.0f*h, false,None);

		    short_puz.setState(offSet[2]*w,0.5f*h, false,None);
		    medium_puz.setState(offSet[2]*w,0.6f*h, false,None);
		    longer_puz.setState(offSet[2]*w,0.7f*h, false,None);
		    longest_puz.setState(offSet[2]*w,0.8f*h, false,None);

		    break;    
	    
	    
	case NEW:
	    bee_puzzled.setState(offSet[0]*w,stack[0]*h, true,MoveFadeIn);

	    new_game.setState(offSet[1]*w,stack[1]*h, true,MoveFadeIn);
	    resume.setState(offSet[1]*w,0.0f*h, false,None);
	    options.setState(offSet[1]*w,0.0f*h, false,None);
	    stats.setState(offSet[1]*w,0.0f*h, false,None);

	    hints.setState(offSet[2]*w,0.5f*h, false,None);

	    short_puz.setState(offSet[2]*w,stack[2]*h, true,MoveFadeIn);
	    medium_puz.setState(offSet[2]*w,stack[3]*h, true,MoveFadeIn);
	    longer_puz.setState(offSet[2]*w,stack[4]*h, true,MoveFadeIn);
	    longest_puz.setState(offSet[2]*w,stack[5]*h, true,MoveFadeIn);
	    break;

	case OPTIONS:

	    bee_puzzled.setState(offSet[0]*w,stack[0]*h, true,MoveFadeIn);

	    new_game.setState(offSet[1]*w,0.0f*h, false,None);
	    resume.setState(offSet[1]*w,0.0f*h, false,None);
	    options.setState(offSet[1]*w,stack[1]*h, true,MoveFadeIn);
	    stats.setState(offSet[1]*w,0.0f*h, false,None);

	    hints.setState(offSet[2]*w,stack[2]*h, true,MoveFadeIn);

	    short_puz.setState(offSet[2]*w,0.0f*h, false,None);
	    medium_puz.setState(offSet[2]*w,0.0f*h, false,None);
	    longer_puz.setState(offSet[2]*w,0.0f*h, false,None);
	    longest_puz.setState(offSet[2]*w,0.0f*h, false,None);

	    break;


	case GAME_PLAY:
	    bee_puzzled.setState(offSet[0]*w,stack[0]*h, true,Move);

	    new_game.setState(offSet[1]*w,0.0f*h, false,None);
	    resume.setState(offSet[1]*w,0.0f*h, false,None);
	    options.setState(offSet[1]*w,0.0f*h, false,None);
	    stats.setState(offSet[1]*w,0.0f*h, false,None);

	    hints.setState(offSet[2]*w,0.0f*h, false,None);

	    short_puz.setState(offSet[2]*w,0.5f*h, false,None);
	    medium_puz.setState(offSet[2]*w,0.6f*h, false,None);
	    longer_puz.setState(offSet[2]*w,0.7f*h, false,None);
	    longest_puz.setState(offSet[2]*w,0.8f*h, false,None);

	    break;
	    
	case GAME_RESUME:
	    bee_puzzled.setState(offSet[0]*w,stack[0]*h, true,Move);

	    new_game.setState(offSet[1]*w,0.0f*h, false,None);
	    resume.setState(offSet[1]*w,0.0f*h, false,None);
	    options.setState(offSet[1]*w,0.0f*h, false,None);
	    stats.setState(offSet[1]*w,0.0f*h, false,None);

	    hints.setState(offSet[2]*w,0.0f*h, false,None);

	    short_puz.setState(offSet[2]*w,0.5f*h, false,None);
	    medium_puz.setState(offSet[2]*w,0.6f*h, false,None);
	    longer_puz.setState(offSet[2]*w,0.7f*h, false,None);
	    longest_puz.setState(offSet[2]*w,0.8f*h, false,None);

	    break;    

   
	}
	}
	
    public void reset() {
    	mMenuState.state = MenuStateEnum.OPENING;
    	mMenuState.difficulty = -1;
    }
    
}

    
    
