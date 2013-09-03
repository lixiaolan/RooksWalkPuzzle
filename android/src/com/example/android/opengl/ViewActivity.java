package com.example.android.opengl;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.RelativeLayout;


public class ViewActivity extends Activity {

    private GLSurfaceView mGLView;
    private TileMenuView mCMView;
    private int stateTileMenu = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        mGLView = new GameView(this);
        mCMView = new TileMenuView(this);
        
        RelativeLayout relativeLayout = new RelativeLayout(this);

        // Defining the RelativeLayout layout parameters.
        // In this case I want to fill its parent
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);

        // Defining the layout parameters of the GameView
        RelativeLayout.LayoutParams mGLViewlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        mGLViewlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        mGLView.setLayoutParams(mGLViewlp);
        relativeLayout.addView(mGLView);
        
     // Defining the layout parameters of the GameView
        RelativeLayout.LayoutParams mCMViewlp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        //mCMViewlp.addRule(RelativeLayout.CENTER_IN_PARENT);
        mCMViewlp.addRule(RelativeLayout.BELOW, mGLView.getId());
        mGLView.setLayoutParams(mCMViewlp);
        relativeLayout.addView(mCMView);
        //Initially this will not be visible!
        mCMView.setVisibility(mCMView.GONE);
        
        
        setContentView(relativeLayout,rlp);
        //setContentView(mGLView);
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
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
    	super.dispatchTouchEvent(e);
    	return true;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent e){
    	if (e.getActionMasked() == 0) {
    		toggleTileMenu();
    	}
    	return true;
    }
    
    void toggleTileMenu(){
    	stateTileMenu = (stateTileMenu+1) % 2;
    	
    	if(stateTileMenu==1){
    		mCMView.setVisibility(mCMView.VISIBLE);
    	} else {
    		mCMView.setVisibility(mCMView.INVISIBLE);
    	}	
    }
}


