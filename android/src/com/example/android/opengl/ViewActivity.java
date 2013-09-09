package com.example.android.opengl;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
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
	
	public enum State {MAIN_MENU, GAME, STATS };

	private State state = State.MAIN_MENU;
	
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

		String fontPath = "Archistico_Simple.ttf";
        Button main_menu_dance_button = (Button) findViewById(R.id.main_menu_dance_button);
        // Loading Font Face
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        // Applying font
        main_menu_dance_button.setTypeface(tf);
		
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
		//mGLView.onResume();
		AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.anim.shrink_dance_button_anim);
	  	set.setTarget((Button)findViewById(R.id.main_menu_dance_button));
	  	set.end();
	}
	
	  public void manageView(View v) {
		  	if(state==State.MAIN_MENU){
		  		AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.anim.shrink_dance_button_anim);
		  		set.setTarget((Button)findViewById(R.id.main_menu_dance_button));
		  		set.start();
		  	
		  		Chronometer ch  = (Chronometer)findViewById(R.id.game_timer);
		  		ch.setVisibility(View.VISIBLE);
		  			  	
		  		Button footsteps  = (Button)findViewById(R.id.footsteps);
		  		footsteps.setVisibility(View.VISIBLE);
		  	
		  		Button stats  = (Button)findViewById(R.id.stats);
		  		stats.setVisibility(View.INVISIBLE);
		  		
		  		state=State.GAME;
		  	}
		  	
		  	
		  	//ViewAnimator main = (ViewAnimator)findViewById(R.id.ViewAnimator1);
	    	//main.showNext();
	    }
}


