package com.seventhharmonic.android.freebeeline;

public abstract class Widget {

	public abstract void draw(MyGLRenderer r);
	
	public abstract void touchHandler(float[] pt);
	
	public abstract void swipeHandler(String direction);
	
	
}
