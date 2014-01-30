package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.ButtonWidget.ButtonStyle;
import com.seventhharmonic.android.freebeeline.graphics.TextCreator.TextJustification;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
import com.seventhharmonic.android.freebeeline.listeners.GameEventListener;

public class TextToggleButtonWidget extends ButtonWidget{

	String text;
	String toggleText;
	boolean toggle = true;
	
	public TextToggleButtonWidget(float centerX, float centerY, float width, float height, String text, String toggleText){
		super(centerX, centerY, width, height, text);
		this.text = text;
		this.toggleText = toggleText;
	}
	
	public boolean getToggle(){
		return toggle;
	}
	
	public void touchHandler(float[] pt){
		super.touchHandler(pt);
		if(super.isTouched(pt)){
		if(toggle){
		    //mText.setText(toggleText);
		    super.setImage(toggleText);
		    toggle = false;
		} else{
		    super.setImage(text);
		    //mText.setText(text);
		    toggle = true;
		}
		}
	}
	
	@Override
	public void draw(MyGLRenderer r) {
		super.draw(r);
		//mText.draw(r);
	}
}
