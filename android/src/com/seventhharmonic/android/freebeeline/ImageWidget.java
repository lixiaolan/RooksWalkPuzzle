package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.android.freebeeline.graphics.TextureManager;

import android.util.Log;

/*
 * Every widget has an absolute center and a relative center. The absolute center
 * is computed in Widget layout and passed in. The relative center is specified by the user
 * You have to override the setCenter method to pass down the center data to your own squares.
 * 
 */

public class ImageWidget extends Widget{

	TextTile a;
	String color = "transparent";
	String text;
	
	public ImageWidget(float[] center , float width, float height, String text){
		this.text = text;
		a = new TextTile(center, width, height, text);	
		setCenter(center);
		setWidth(width);
		setHeight(height);
	}
	
	
	
	public ImageWidget(float centerX, float centerY , float width, float height, String text){
		this.text = text;
		float[] center = {centerX, centerY, 0.0f};
		a = new TextTile(center, width, height, text);
		setCenter(center);
		setWidth(width);
		setHeight(height);
	}
	
	public void setImage(String image){
		a.setTextures(image, TextureManager.CLEAR);
	}
	
	public void setMode(String mode){
		a.setMode(mode);
	}
	
	public void setColor(String color){
		this.color = color;
		a.setColor(color);
	}
	
	public void setBackground(String image){
		if(image != null){
			a.setTextures(text, image);
		} 
	}
	
	@Override
	public void setCenter(float x, float y){
		super.setCenter(x,y);
		a.setCenter(x, y);
	}
	
	@Override
	public boolean isTouched(float[] pt){
		Log.d("ImageWidget", text+" "+Boolean.toString(a.touched(pt)));
		return a.touched(pt);
	}
	
	@Override
	public void draw(MyGLRenderer r) {
		a.draw(r);
	}

}
