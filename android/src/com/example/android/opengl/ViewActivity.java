package com.example.android.opengl;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ViewAnimator;
import android.widget.ViewSwitcher;

public class ViewActivity extends Activity {

	private GLSurfaceView mGLView;
	private Model mModel;
	private MyGLRenderer mRenderer;
	public static final String GAME_PREFS = "ScoreFile";
	
    public StateButton[] allButtons = new StateButton[4];

	private GameState state = GameState.MAIN_MENU;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewactivity);
		// Create a GLSurfaceView instance and set it
		// as the ContentView for this Activity
		if(savedInstanceState != null){
			mModel = new Model((Board)savedInstanceState.getParcelable("board"));        	
		} else {
			mModel = new Model();
		}
		mRenderer = new MyGLRenderer(this, mModel);

		mGLView = (GameView)findViewById(R.id.surface_view);
		((GameView)mGLView).setMyRenderer(mRenderer);
		((GameView)mGLView).setModel(mModel);

		// String fontPath = "Archistico_Simple.ttf";
		// Button bee_puzzled = (Button) findViewById(R.id.bee_puzzled);
		// // Loading Font Face
		// Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
		// // Applying font
		// bee_puzzled.setTypeface(tf);
		//Store all buttons in one array for now.  Change to a map later for clarity.
		allButtons[0] = new StateButton((Button)findViewById(R.id.bee_puzzled));
		allButtons[1] = new StateButton((Button)findViewById(R.id.new_game));
		allButtons[2] = new StateButton((Button)findViewById(R.id.easy));
		allButtons[3] = new StateButton((Button)findViewById(R.id.medium));
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable("board", mModel.mBoard);
		super.onSaveInstanceState(outState);
	}


	@Override
	protected void onPause() {
		super.onPause();
		// The following call pauses the rendering thread.
		// If your OpenGL application is memory intensive,
		// you should consider de-allocating objects that
		// consume significant memory here.
		mGLView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// The following call resumes a paused rendering thread.
		// If you de-allocated graphic objects for onPause()
		// this is a good place to re-allocate them.
		mGLView.onResume();
		// AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.anim.shrink_dance_button_anim);
	  	// set.setTarget((Button)findViewById(R.id.bee_puzzled));
	  	// set.end();
		allButtons[0].setState(20.0f, 120.0f, 1.0f, true);
		animateButtons();
	}
	
    public void manageView(View v) {
	//if(state==GameState.MAIN_MENU){
	    switch (v.getId()) {
	    case R.id.bee_puzzled:
	        allButtons[0].setState(20.0f,20.0f, 1.0f, true);
		allButtons[1].setState(20.0f,120.0f, 1.0f, true);
		allButtons[2].setState(20.0f,120.0f, 1.0f, false);
		allButtons[3].setState(20.0f,120.0f, 1.0f, false);
		animateButtons();
	        break;
	    case R.id.new_game:
	        allButtons[0].setState(20.0f,20.0f, 1.0f, true);
		allButtons[1].setState(20.0f,50.0f, 1.0f, true);	
		allButtons[2].setState(20.0f,120.0f,1.0f, true);
		allButtons[3].setState(20.0f,150.0f,1.0f, true);
		animateButtons();
		break;
	    case R.id.easy:
	        allButtons[0].setState(20.0f,250.0f, 1.0f, true);
		allButtons[1].setState(20.0f,40.0f, 1.0f, false);
		allButtons[2].setState(20.0f,120.0f,1.0f, false);
		allButtons[3].setState(20.0f,150.0f,1.0f, false);
		animateButtons();
		state=GameState.PLAY;
		mModel.setState(GameState.PLAY);
		break;
	    }
	    //}
    }

    //This function peforms animatinos on the array of buttons allButtons.
    //It works by sequentially activating the various animatinos
    //needed based on any change of state in the StateButtons.
    //This provides a flexible way for the menu to have smooth
    //animations between its different layouts.
    public void animateButtons() {
	boolean test;
	long duration = 1000;
	long delay = 0;
	int len = (int)allButtons.length;

        AnimatorSet AllAnimSets[] = new AnimatorSet[len];

	ObjectAnimator fadeOutAnim[] = new ObjectAnimator[len];
	ObjectAnimator fadeInAnim[] = new ObjectAnimator[len];
	ObjectAnimator xAnim[] = new ObjectAnimator[len];
	ObjectAnimator yAnim[] = new ObjectAnimator[len];

	boolean outTest = false;
	boolean moveTest = false;
	boolean inTest = false;

	for (int i = 0; i < len; i++) {
	    AllAnimSets[i] = new AnimatorSet();
	}

	for (int i = 0; i < allButtons.length; i++) {
	    outTest |= allButtons[i].fadeOut;
	    moveTest |= allButtons[i].moved;
	    inTest |= allButtons[i].fadeIn;
	}
	
	

	//Fade outs first!
	for (int i = 0; i < allButtons.length; i++) {
	    //fade out
	    if (allButtons[i].fadeOut) {
		fadeOutAnim[i] = ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0.0f);
		fadeOutAnim[i].setTarget(allButtons[i].mButton);
	    }
	    else {  
		if (allButtons[i].fadeIn) {
		    fadeOutAnim[i] = ObjectAnimator.ofFloat(this, "alpha", 0.0f, 0.0f);
		    fadeOutAnim[i].setTarget(allButtons[i].mButton);
		}
		else {
		    if (allButtons[i].visible) {
			fadeOutAnim[i] = ObjectAnimator.ofFloat(this, "alpha", 1.0f, 1.0f);
			fadeOutAnim[i].setTarget(allButtons[i].mButton);
		    }
		    else {
			fadeOutAnim[i] = ObjectAnimator.ofFloat(this, "alpha", 0.0f, 0.0f);
			fadeOutAnim[i].setTarget(allButtons[i].mButton);
		    }
		
		}		
	    }
	    fadeOutAnim[i].setDuration(outTest ? duration : 0);

	    //move
	    xAnim[i] = ObjectAnimator.ofFloat(this, "x", allButtons[i].position[0]);
	    yAnim[i] = ObjectAnimator.ofFloat(this, "y", allButtons[i].position[1]);
	    
	    xAnim[i].setTarget(allButtons[i].mButton);
	    yAnim[i].setTarget(allButtons[i].mButton);
	    
	    xAnim[i].setDuration(moveTest ? duration : 0);
	    yAnim[i].setDuration(moveTest ? duration : 0);
	    
	    //fade in
	    if (allButtons[i].fadeIn) {
		fadeInAnim[i] = ObjectAnimator.ofFloat(this, "alpha", 0.0f, 1.0f);
		fadeInAnim[i].setTarget(allButtons[i].mButton);
		fadeInAnim[i].setDuration(duration);
	    }
	    else {  
		if (allButtons[i].fadeOut) {
		    fadeInAnim[i] = ObjectAnimator.ofFloat(this, "alpha", 0.0f, 0.0f);
		    fadeInAnim[i].setTarget(allButtons[i].mButton);
		    fadeInAnim[i].setDuration(0);
		}
		else {
		    if (allButtons[i].visible) {
			fadeInAnim[i] = ObjectAnimator.ofFloat(this, "alpha", 1.0f, 1.0f);
			fadeInAnim[i].setTarget(allButtons[i].mButton);
			fadeInAnim[i].setDuration(0);
		    }
		    else {
			fadeInAnim[i] = ObjectAnimator.ofFloat(this, "alpha", 0.0f, 0.0f);
			fadeInAnim[i].setTarget(allButtons[i].mButton);
			fadeInAnim[i].setDuration(0);
		    }
		
		}
	    }
	    fadeInAnim[i].setDuration(inTest ? duration : 0);


	    //Build animation sets
	     AllAnimSets[i].play(fadeOutAnim[i]).before(xAnim[i]);
	     AllAnimSets[i].play(xAnim[i]).with(yAnim[i]);
	     AllAnimSets[i].play(fadeInAnim[i]).after(xAnim[i]);
	     AllAnimSets[i].start();
	}
	
	
	



	// if (test)
	//     delay = delay + duration;

	// //Moves next;
	// test = false;
	// for (int i = 0; i < allButtons.length; i++) {
	//     if (allButtons[i].moved) {
	// 	xAnim = ObjectAnimator.ofFloat(this, "x", allButtons[i].position[0]);
	// 	yAnim = ObjectAnimator.ofFloat(this, "y", allButtons[i].position[1]);
		
	// 	xAnim.setTarget(allButtons[i].mButton);
	// 	yAnim.setTarget(allButtons[i].mButton);

	// 	if (allButtons[i].fadeIn) {
	// 	    xAnim.setDuration(0);
	// 	    yAnim.setDuration(0);
	// 	}
	// 	else {
	// 	    xAnim.setDuration(duration);
	// 	    yAnim.setDuration(duration);
	// 	    test = true;
	// 	}
	// 	xAnim.setStartDelay(delay);
	// 	yAnim.setStartDelay(delay);
	// 	xAnim.start();
	// 	yAnim.start();
	//     }
	// }
	// if (test)
	//     delay = delay + duration;

	// //Fade in last
	// for (int i = 0; i < allButtons.length; i++) {
	//     if (allButtons[i].visible) {
	// 	if (allButtons[i].fadeIn) {
	// 	    fadeInAnim.setTarget(allButtons[i].mButton);
	// 	    fadeInAnim.setDuration(duration);
	// 	    fadeInAnim.setStartDelay(delay);
	// 	    fadeInAnim.start();
	// 	}
	//     }
	// }
    }
}
