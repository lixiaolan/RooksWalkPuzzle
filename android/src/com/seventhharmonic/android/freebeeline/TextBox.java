package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.android.freebeeline.graphics.TextureManager;

/*TODO:
 * Should a Text Box widget specify it's center or where the text should start?
 * It should specify a center! You should have parameters determining how the text is drawn
 * in this box.
 * 
 */

public class TextBox extends Widget{

	String text;
	TextTile[] chars; 
	
	public TextBox(float centerX, float centerY , float width, String text){
		float[] center = {centerX, centerY, 0.0f};
		this.text = text;
		chars = new TextTile[text.length()];
		setCenter(center);
		setWidth(width);
	}
	
	public void setText(String text){
		this.text = text;
	}
	
	
	@Override
	public void setCenter(float x, float y){
		super.setCenter(x,y);
	}
	
	
	@Override
	public void draw(MyGLRenderer r) {
		r.drawTextBox(center,width,text);
	}

}
