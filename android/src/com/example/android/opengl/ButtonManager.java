package com.example.android.opengl;


import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ViewAnimator;
import android.widget.ViewSwitcher;
import java.util.ArrayList;


//Manages transitions between MenuStates.

class ButtonManager{
    public StateButton bee_puzzled;
    public StateButton short_puz;
    public StateButton medium_puz;
    public MenuState mMenuState;
    
    private float h;
    private float w;
    private long[] pOne = {0,1000,1000};
    private long[] pTwo = {1000, 1000, 0};
    private long[] pThree = {0,0,1000};

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
	    bee_puzzled.setState(0.1f*w,0.5f*h, 1.0f, true,pThree);
	    short_puz.setState(0.1f*w,0.5f*h, 1.0f, false,pThree);
	    medium_puz.setState(0.1f*w,0.6f*h, 1.0f, false,pThree);
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
    
    
