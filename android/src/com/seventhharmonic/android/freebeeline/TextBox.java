package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.graphics.TextCreator;
import com.seventhharmonic.android.freebeeline.graphics.TextCreator.TextJustification;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;

/*TODO:
 * Should a Text Box widget specify it's center or where the text should start?
 * It should specify a center! You should have parameters determining how the text is drawn
 * in this box.
 * 
 */

public class TextBox extends Widget{
	String TAG = "TextBox";
	String text;
	//Font size in b's. See Text Creator for more on this unit.
	//1bf  = .05b. Ie
	float fontSize = 1;
	TextJustification j = TextJustification.LEFT;
	ImageWidget background;
	boolean shakeToggle = false;
	long timer = 0;
	public void setJ(TextJustification j) {
		this.j = j;
	}

	public float getFontSize() {
		return fontSize;
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

	public void setShakeToggle(boolean a){
		shakeToggle = a;
	}
	
	/**
	 * Constructor for a textBox. The x, y coordinates where you want the center of the first row of the text box.
	 * The width specifies where to jump to the next line.
	 * 
	 * 
	 * 
	 * @param centerX
	 * @param centerY
	 * @param width
	 * @param text
	 */
	public TextBox(float centerX, float centerY , float width, String text){
		float[] center = {centerX, centerY, 0.0f};
		this.text = text;
		background = new ImageWidget(centerX, centerY, width, 0,  TextureManager.CLEAR);
		setCenter(center);
		setWidth(width);
		setHeight(width);
	}
	
	public void setText(String t){
		if(!text.equals(t)){
			timer = System.currentTimeMillis();
		}
		this.text = t;
	}
	
	/**Currently, this gives no guarantee on properly toggling a border. The text could end up outside of it.
	 * Need to query the text creator for an appropriate height and width.
	 * 
	 * Modified: Now accepts a height parameter. This sucks. Should be able to dynamically determine height of text.
	 * 
	 * @param b
	 */
	public void setBorder(boolean b, float x, float y, float width, float height){
		background.setCenter(x, y);
		background.setHeight(height);
		background.setWidth(width);
		background.setBorder(b);
	}
	
	public void setColor(String color){
		background.setColor(color);
	}
	
	@Override
	public void setCenter(float x, float y){
		super.setCenter(x,y);
		background.setCenter(x, y);
	}
	
	float[] tempCenter = {0,0};
	
	@Override
	public void draw(MyGLRenderer r) {
		
		if(background.getBorder()){
			background.draw(r);
		}
		long time = System.currentTimeMillis();
		if(shakeToggle && time-timer<500){
			tempCenter[0] = center[0]+.02f*(float)Math.sin(6.28f*(float)((time-timer)/500f));
			tempCenter[1] = center[1];//+.05f*(float)Math.sin(6.28f*(float)(time-timer));
			
			Log.d(TAG, "Shake it up baby "+Float.toString(.1f*(float)Math.sin(6.28f*(float)(time-timer))));
			r.drawTextBox(tempCenter, width, fontSize, text, j);
		} else{
			r.drawTextBox(center,width,fontSize,text, j);
		}
		
	}

	@Override
	public void touchHandler(float[] pt) {
	}

	@Override
	public void swipeHandler(String direction) {
		
	}

}
