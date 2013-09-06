package com.example.android.opengl;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.RelativeLayout;
import android.widget.Chronometer;

public class ViewActivity extends Activity {

    private GLSurfaceView mGLView;
    private Model mModel;
    private MyGLRenderer mRenderer;
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
        
        Chronometer ch = (Chronometer)findViewById(R.id.puzzle_time);
        ch.start();
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
      getMenuInflater().inflate(R.menu.viewactivity_actions, menu);
      getActionBar().setDisplayShowTitleEnabled(false);
      getActionBar().setDisplayShowHomeEnabled(false);
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
    
    
}


