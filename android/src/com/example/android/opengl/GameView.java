package com.example.android.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

class GameView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    public GameView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
        setEGLContextClientVersion(2);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer(context);
        setRenderer(mRenderer);

        // Render the view only when there is a change in the drawing data
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.
    	
	if (e.getActionMasked() == 0) {
	    float x = e.getX();
	    float y = e.getY();


	    float[] pt = new float[2];
	    pt[0] = 2.0f*(x/getWidth())-1;
	    pt[1] = -2.0f*(y/getHeight())+1;

	    mRenderer.touched(pt);
	    requestRender();
	    mPreviousX = x;
	    mPreviousY = y;
	}
		//A hack for right now to ensure menu appears...
        return false;
    }
    
    

}