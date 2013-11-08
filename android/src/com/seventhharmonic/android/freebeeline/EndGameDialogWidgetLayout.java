package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.graphics.Geometry;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
import com.seventhharmonic.android.freebeeline.listeners.GameEventListener;

public class EndGameDialogWidgetLayout extends WidgetLayout{

	
	TextBox mText;
	TextBox mPuzzText;
	ButtonWidget mBack;
	ButtonWidget mNext;
	ImageWidget mBackground;
	boolean draw  = false;
	
	public EndGameDialogWidgetLayout(float width, int puzzLeft){
		String text = TextureManager.GOOD_JOB;
		setWidth(width);
		setHeight(width/Geometry.PHI);
		setCenter(0,0);
		
		mText = new TextBox(0,getHeight()*.9f,width, text);
		mText.setRelativeCenter(0,0);
		
		//TODO: This solution sucks. Have a more general function to draw text at a certain position.
		String a = Integer.toString(puzzLeft);		
		Log.d("EndGame", a);
		mPuzzText = new TextBox(0,getHeight()*.85f,width, TextureManager.PUZZLESLEFT+a);
		mPuzzText.setRelativeCenter(0,0);
		
		mBack = new ButtonWidget(0,0, width/4, width/4, TextureManager.BACK);
		mBack.setRelativeCenter(.7f*width, -.6f*height);
		
		mNext = new ButtonWidget(0,0, width/4, width/4, TextureManager.NEXT);
		mNext.setRelativeCenter(-.7f*width, -.6f*height);
		
		mBackground = new ImageWidget(0,0,width, height, TextureManager.CLEAR);
		mBackground.setMode(MyGLRenderer.STRETCH);
		mBackground.setRelativeCenter(0, 0);
		mBackground.setColor("opaque");
		
		widgetList.add(mBackground);
		widgetList.add(mText);
		widgetList.add(mPuzzText);
		widgetList.add(mNext);
		widgetList.add(mBack);
		computeGeometry();
	}
	
	@Override
	public void touchHandler(float[] pt){
		mNext.touchHandler(pt);
		mBack.touchHandler(pt);
	}
	
	public void setNextClickListener(GameEventListener listener){
		mNext.setClickListener(listener);
	}
	
	public void setBackClickListener(GameEventListener listener){
		mBack.setClickListener(listener);
	}
	
	public void activate(){
		draw = true;
	}
	
	public void deactivate(){
		draw = false;
	}

	@Override
	public void draw(MyGLRenderer r) {
		if(draw){
		for(Widget w: widgetList){
			w.draw(r);
		}
		}
	}

}
