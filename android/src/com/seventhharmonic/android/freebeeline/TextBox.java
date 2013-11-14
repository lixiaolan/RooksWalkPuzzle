package com.seventhharmonic.android.freebeeline;

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

	String text;
	//Font size in b's. See Text Creator for more on this unit.
	//1bf  = .05b. Ie
	float fontSize = 1;
	TextJustification j = TextJustification.LEFT;
	
	
	public void setJ(TextJustification j) {
		this.j = j;
	}

	public float getFontSize() {
		return fontSize;
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

	/**
	 * Constructor for a textBox. The x, y coordinates where you want the center of the first row of the text box.
	 * The width specifies where to jump to the next line.
	 * 
	 * @param centerX
	 * @param centerY
	 * @param width
	 * @param text
	 */
	public TextBox(float centerX, float centerY , float width, String text){
		float[] center = {centerX, centerY, 0.0f};
		this.text = text;
		setCenter(center);
		setWidth(width);
		setHeight(width);
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
		r.drawTextBox(center,width,fontSize,text, j);
	}

	@Override
	public void touchHandler(float[] pt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void swipeHandler(String direction) {
		// TODO Auto-generated method stub
		
	}

}
