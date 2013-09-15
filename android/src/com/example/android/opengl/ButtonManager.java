package com.example.android.opengl;



import android.app.Activity;
import android.view.View;
import android.widget.Button;



//Manages transitions between MenuStates.
class ButtonManager{
    public StateButton bee_puzzled;
    public StateButton short_puz;
    public StateButton medium_puz;
    public StateButton resume;
    public MenuState mMenuState;
    
    private float h;
    private float w;
    private long[] pOne = {0,1000,1000};
    private long[] pTwo = {1000, 1000, 0};


    public ButtonManager(MenuState ms, Activity va) {
	
	bee_puzzled = new StateButton((Button)va.findViewById(R.id.bee_puzzled));
	short_puz = new StateButton((Button)va.findViewById(R.id.short_puz));
	medium_puz = new StateButton((Button)va.findViewById(R.id.medium_puz));
	mMenuState = ms;
    }

    public void setHW(float height, float width) {
	h = height;
	w = width;
	switchState();
    }

    public void manageState(View v) {
	 switch (v.getId()) {
	    case R.id.bee_puzzled:
		mMenuState.state = MenuStateEnum.LIST;
	 	break;
		
	    case R.id.short_puz:
		mMenuState.state = MenuStateEnum.GAME_PLAY;
		mMenuState.difficulty = 1;
		break;
		
	    case R.id.medium_puz:
		mMenuState.state = MenuStateEnum.GAME_PLAY;
		mMenuState.difficulty = 2;
		break;
	    }
	 switchState();
    }
    
    public void switchState() {	
	switch (mMenuState.state) {
	case OPENING:
	    bee_puzzled.setState(0.1f*w,0.5f*h, 1.0f, true,pTwo);
	    short_puz.setState(0.1f*w,0.5f*h, 1.0f, false,pTwo);
	    medium_puz.setState(0.1f*w,0.6f*h, 1.0f, false,pTwo);
	    break;

	case LIST:
	    bee_puzzled.setState(0.1f*w,0.1f*h, 1.0f, true,pOne);
	    short_puz.setState(0.1f*w,0.5f*h, 1.0f, true,pOne);
	    medium_puz.setState(0.1f*w,0.6f*h, 1.0f, true,pOne);
	    break;

	case GAME_PLAY:
	    bee_puzzled.setState(0.1f*w,0.9f*h, 1.0f, true,pTwo);
	    short_puz.setState(0.1f*w,0.5f*h, 1.0f, false,pTwo);
	    medium_puz.setState(0.1f*w,0.6f*h, 1.0f, false,pTwo);
	    break;
	}
    }
}
    
    
