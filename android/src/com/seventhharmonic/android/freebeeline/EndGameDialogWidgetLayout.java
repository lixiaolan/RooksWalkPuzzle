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
		String a = Integer.toString(puzzLeft);		
		Log.d("EndGame", a);
		
		mText = new TextBox(0,0,.90f*width, text+"^"+TextureManager.PUZZLESLEFT+a);
		mText.setRelativeCenter(.05f*width,height*.9f);
		
		//String a = Integer.toString(puzzLeft);		
		//Log.d("EndGame", a);
		//mPuzzText = new TextBox(0,0,width, TextureManager.PUZZLESLEFT+a);
		//mPuzzText.setRelativeCenter(0,height*.7f);
		
		mBack = new ButtonWidget(0,0, .15f, .15f, TextureManager.BACK);
		mBack.setRelativeCenter(.7f*width, -.6f*height);
		
		mNext = new ButtonWidget(0,0, .15f, .15f, TextureManager.NEXT);
		mNext.setRelativeCenter(-.7f*width, -.6f*height);
		
		mBackground = new ImageWidget(0,0,width, height, TextureManager.CLEAR);
		mBackground.setMode(MyGLRenderer.STRETCH);
		mBackground.setRelativeCenter(0, 0);
		mBackground.setBorder(true);
		mBackground.setColor("opaque");
		
		widgetList.add(mBackground);
		widgetList.add(mText);
		//widgetList.add(mPuzzText);
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
