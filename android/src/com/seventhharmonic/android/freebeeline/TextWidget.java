package com.seventhharmonic.android.freebeeline;

/*
 * Every widget has an absolute center and a relative center. The absolute center
 * is computed in Widget layout and passed in. The relative center is specified by the user
 * You have to override the setCenter method to pass down the center data to your own squares.
 * 
 */

public class TextWidget extends Widget{

	TextTile a;
	String mode = MyGLRenderer.CROPTOP;
	
	public TextWidget(float[] center , float width, float height, String text){
		a = new TextTile(center, width, height, text);
		a.setMode(mode);
		setCenter(center);
		setWidth(width);
		setHeight(height);
	}
	
	public TextWidget(float centerX, float centerY , float width, float height, String text){
		float[] center = {centerX, centerY, 0.0f};
		a = new TextTile(center, width, height, text);
		a.setMode(mode);
		setCenter(center);
		setWidth(width);
		setHeight(height);
	}
	
	public void setText(String text){
		a.setTextures(text, TextureManager.CLEAR);
	}
	
	public void setColor(String color){
		a.setColor(color);
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
