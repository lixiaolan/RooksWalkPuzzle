package com.seventhharmonic.android.freebeeline;


public abstract class Widget {

	public abstract void draw(MyGLRenderer r);
	
	public void touchHandler(float[] pt){};
	
	public void swipeHandler(String direction){};
	
	
}
