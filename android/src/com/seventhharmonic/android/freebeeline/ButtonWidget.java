package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.listeners.GameEventListener;

public class ButtonWidget extends Widget{

	TextTile mText;
	TextTile mBackground;
	GameEventListener listener;
	
	public ButtonWidget(float centerX, float centerY, float width, float height, String text){
		float[] center = {centerX, centerY, 0.0f};
		mText = new TextTile(center, width, height, text);

		mBackground  = new TextTile(center, width, height, "menu_circle");
		mBackground.setMode(MyGLRenderer.STRETCH);
	}
	
	public void setClickListener(GameEventListener l){
		listener = l;
	}
	
	@Override
	public void setCenter(float centerX, float centerY){
		mText.setCenter(centerX, centerY);
		mBackground.setCenter(centerX, centerY);
	}
	
	@Override
	public void touchHandler(float[] pt){
		if(mText.touched(pt)){
			Log.d("mText", "touched");
			listener.event(0);
		}
	}
	
	@Override
	public void draw(MyGLRenderer r) {
		mBackground.draw(r);
		mText.draw(r);
	}
	
}
