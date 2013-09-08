package com.example.android.opengl;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ViewSwitcher;

public class ViewActivity extends Activity {

	private GLSurfaceView mGLView;
	private Model mModel;
	private MyGLRenderer mRenderer;
	private SharedPreferences gamePrefs;
	public static final String GAME_PREFS = "ScoreFile";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewactivity);
		gamePrefs = getSharedPreferences(GAME_PREFS, 0);
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
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		
		//Somehow the menu items and the default layout all get merged?
		final ActionBar actionBar = getActionBar();
		getMenuInflater().inflate(R.menu.viewactivity_actions, menu);		
		final ViewGroup actionBarLayout = (ViewGroup) getLayoutInflater().inflate(
				R.layout.viewactivity_actionbar,
				null);

		// Set up your ActionBar
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setCustomView(actionBarLayout);
		Chronometer ch = (Chronometer)findViewById(R.id.puzzle_timer);
		ch.start();
	
		
		return true;
	} 

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.new_game_action:
			System.out.println("CLICKED");
			mModel.resetBoard();    	  
			break;

		default:
			break;
		}

		return true;
	} 

	
	public void checkSolution(View view){
		//THIS IS THE WORST METHOD EVER WRITTEN
		//Fixes include moving share helper into a fragment?
		//Use a fragment for the dialog as well I think
		if(true) {
			AlertDialog.Builder builder;
	        builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
	        builder.setTitle("Finished!");
	        final View v = LayoutInflater.from(this).inflate(R.layout.solved_fragment, null);
	        builder.setView(v);
	        final AlertDialog mDialog = builder.create();
	        mDialog.show();
	        
	        Button share = (Button)v.findViewById(R.id.button1);
	        share.setOnClickListener(new OnClickListener()
	        {
	            @Override
	            public void onClick(View v)
	            {
	            	mDialog.dismiss();
	            	share();
	            }
	        });
	        
		}
	}

	public void share() {
		ShareHelper sh = new ShareHelper(this,"1","2","3","4","http://www.google.com");
		sh.share();
	}
	
	/*private void setTime(){
		SharedPreferences.Editor scoreEdit = gamePrefs.edit();
		float[] scores = gamePrefs.getFloat("times", "");
		if(scores.length()>0){
		    //we have existing scores
		}
		else{
			
		    //no existing scores
			
		}
	}*/
	
	  public void switchView(View v) {
	    	System.out.println("Click me please");
	    	ViewSwitcher main = (ViewSwitcher)findViewById(R.id.ViewSwitcher1);
	    	main.showNext();
	    }
}


