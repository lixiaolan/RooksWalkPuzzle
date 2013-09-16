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

public class ViewActivity extends Activity {

    private GLSurfaceView mGLView;
    private Model mModel;
    private MyGLRenderer mRenderer;    
    private GameState state = GameState.MAIN_MENU;
    private MenuState mMenuState = new MenuState();
    private ButtonManager mButtonManager;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {

	super.onCreate(savedInstanceState);
	setContentView(R.layout.viewactivity);
       
	// Create a GLSurfaceView instance and set it
	// as the ContentView for this Activity
	if(savedInstanceState != null){
	    mModel = new Model(this, (Board)savedInstanceState.getParcelable("board"));        	
	} else {
	    mModel = new Model(this);
	}
	mRenderer = new MyGLRenderer(this, mModel);
	
	mGLView = (GameView)findViewById(R.id.surface_view);
	((GameView)mGLView).setMyRenderer(mRenderer);
	((GameView)mGLView).setModel(mModel);
	
	mButtonManager = new ButtonManager(mMenuState, this);

	//Store all buttons in one array for now.  Change to a map later for clarity.
	View yourLayout = findViewById(R.id.surface_view);
	final ViewTreeObserver vto = yourLayout.getViewTreeObserver();
	vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		@Override
		public void onGlobalLayout() {
		    float h = (float) mGLView.getHeight()-mGLView.getPaddingBottom()-mGLView.getPaddingTop();
		    float w = (float) mGLView.getWidth();
		    mButtonManager.setHW(h, w);
		}
	    });
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
	//outState.putParcelable("board", mModel.mBoard);
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
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	//Store all buttons in one array for now.  Change to a map later for clarity.
    	// bee_puzzled = new StateButton((Button)findViewById(R.id.bee_puzzled));
    	// short_puz = new StateButton((Button)findViewById(R.id.short_puz));
    	// medium_puz = new StateButton((Button)findViewById(R.id.medium_puz));
    	
    	
    }
    
    //This is called after the constructor of GameView is complete.
    //Otherwise, the positinos would not work out correctly :(
    
    public void manageView(View v) {
	mButtonManager.manageState(v);
	if (mMenuState.state == MenuStateEnum.GAME_PLAY) {
	    switch (mMenuState.difficulty) {
	    case 1:
		state = GameState.PLAY;
		mModel.createPuzzle(10,3);
		mModel.setState(GameState.PLAY);
		break;
	    case 2:
		state = GameState.PLAY;
		mModel.createPuzzle(12,4);
		mModel.setState(GameState.PLAY);
		break;
	    case 3:
		state = GameState.PLAY;
		mModel.createPuzzle(14,4);
		mModel.setState(GameState.PLAY);
		break;
	    case 4:
		state = GameState.PLAY;
		mModel.createPuzzle(16,5);
		mModel.setState(GameState.PLAY);
		break;
	    }
	}
    }
    
    //This function peforms animatinos on the array of buttons allButtons.
    //It works by sequentially activating the various animatinos
    //needed based on any change of state in the StateButtons.
    //This provides a flexible way for the menu to have smooth
    //animations between its different layouts.

}
