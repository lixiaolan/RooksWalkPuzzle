package com.example.android.opengl;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.RelativeLayout;


public class ViewActivity extends Activity {

    private GLSurfaceView mGLView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MyGLRenderer mRenderer;
        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        if(savedInstanceState != null){
        	Model mModel = new Model((Board)savedInstanceState.getParcelable("board"));
        	mRenderer = new MyGLRenderer(this, mModel);
        } else {
        	mRenderer = new MyGLRenderer(this, new Model());
        }
        
        mGL
        
        setContentView(mGLView);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("board", ((GameView)mGLView).mRenderer.mModel.mBoard);
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
    }
    
    
    
    
}


