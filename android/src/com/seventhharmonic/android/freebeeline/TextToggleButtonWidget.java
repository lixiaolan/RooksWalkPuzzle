package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.ButtonWidget.ButtonStyle;
import com.seventhharmonic.android.freebeeline.graphics.TextCreator.TextJustification;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
import com.seventhharmonic.android.freebeeline.listeners.GameEventListener;

public class TextToggleButtonWidget extends ButtonWidget{

	//TextBox mText;
	String text;
	String toggleText;
	boolean toggle = true;
	
	public TextToggleButtonWidget(float centerX, float centerY, float width, float height, String text, String toggleText){
		super(centerX, centerY, width, height, TextureManager.HIDE);
		//mText = new TextBox(centerX,centerY, width, text );
		//mText.setJ(TextJustification.CENTERBOX);
		//a.setColor("white");
		this.text = text;
		this.toggleText = toggleText;
	}
		
	
	public void touchHandler(float[] pt){
		super.touchHandler(pt);
		if(super.isTouched(pt)){
		if(toggle){
			//mText.setText(toggleText);
			super.setImage(TextureManager.SHOW);
			toggle = false;
		} else{
			super.setImage(TextureManager.HIDE);
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
