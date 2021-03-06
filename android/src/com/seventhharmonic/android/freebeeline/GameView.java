package com.seventhharmonic.android.freebeeline;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class GameView extends GLSurfaceView {

    public MyGLRenderer mRenderer;
    public Model mModel;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);        
        // Set the Renderer for drawing on the GLSurfaceView
        // Render the view only when there is a change in the drawing data
        //
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        setPreserveEGLContextOnPause(true);
    }

    
    
    private float mDownX;
    private float mDownY;
    private float mUpX;
    private float mUpY;
    float swipeTol = 20.0f;

    public void setMyRenderer(MyGLRenderer mR) {
    		mRenderer = mR;
    		setRenderer(mRenderer);
    }
    
    public void setModel(Model m) {
	mModel = m;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

	//Adjust the "Swipe Tollerance"
	
	float[] pt = new float[2];
	float x = e.getX();
	float y = e.getY();

	// If one does not reuturn true in the cases
	// The .ACTION_UP will not get correctly registered.

	switch (e.getAction() & MotionEvent.ACTION_MASK) {

	case (MotionEvent.ACTION_DOWN) :
	    
	    // pt[0] = 2.0f*(x/getWidth())-1;
	    // pt[1] = -2.0f*(y/getHeight())+1;
	    // mRenderer.touched(pt);
	    // requestRender();
	    
	    mDownX = x;
	    mDownY = y;
	    
	    return true;

	case (MotionEvent.ACTION_UP) :

	    mUpX = x;
	    mUpY = y;
	    
	    x = mUpX - mDownX;
	    y = mUpY - mDownY;

	    pt[0] = 2.0f*(mDownX/getWidth())-1;
	    pt[1] = -2.0f*(mDownY/getHeight())+1;
	    
	    String direction; //0 east, 1 north, 2 west, 3 south
	    if ( Math.pow(x,2) + Math.pow(y,2)  > Math.pow(swipeTol,2) ) {
		if (y >= x) {
		    if ( y >= -x ) {
			direction = "down_arrow"; //Note the coordinate system is reflected about the x-axis
		    }
		    else {
			direction = "left_arrow";
		    }
		}
		else {
		    if ( y >= -x) {
			direction = "right_arrow";
		    }
		    else {
			direction = "up_arrow"; //Note the coordinate system is reflected about the x-axis
		    }
		}
		pt = mRenderer.project(pt);
		mModel.swiped(pt, direction);
		requestRender();
	    }
	    else {
	    	pt = mRenderer.project(pt);
			mModel.touched(pt);
			requestRender();
	    }
	    return true;
	}	
        return false;
    }
}
