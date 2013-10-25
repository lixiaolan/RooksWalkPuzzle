package com.seventhharmonic.android.freebeeline;

/*
 * Every widget has an absolute center and a relative center. The absolute center
 * is computed in Widget layout and passed in. The relative center is specified by the user
 * You have to override the setCenter method to pass down the center data to your own squares.
 * 
 */

public class SquareWidget extends Widget{

	ScreenTile a;
	
	public SquareWidget(){
		float[] c = {0.0f, 0.0f, 0};
		a = new ScreenTile(c, .11f);
		setRelativeCenter(0.0f, 0.0f);
	}
	
	@Override
	public void setCenter(float x, float y){
		super.setCenter(x,y);
		a.setCenter(x, y);
	}
	
	@Override
	public void draw(MyGLRenderer r) {
		a.draw(r);
	}

}
