package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.android.freebeeline.graphics.Geometry;
import com.seventhharmonic.android.freebeeline.listeners.GameEventListener;

public class EndGameDialogWidgetLayout extends WidgetLayout{

	
	TextWidget mText;
	ButtonWidget mNext;
	ImageWidget mBackground;
	boolean draw  = false;
	
	public EndGameDialogWidgetLayout(float width, String text){
		setWidth(width);
		setHeight(width/Geometry.PHI);
		setCenter(0,0);
		
		mText = new TextWidget(0,0,width, height, text);
		mText.setRelativeCenter(0,0);
		mText.setColor("blue");
		
		mNext = new ButtonWidget(0,0, width/4, height/4, TextureManager.NEXT);
		mNext.setRelativeCenter(-.7f*width, -.7f*height);
		
		mBackground = new ImageWidget(0,0,width, height, TextureManager.CLEAR);
		mBackground.setMode(MyGLRenderer.STRETCH);
		mBackground.setRelativeCenter(0, 0);
		mBackground.setColor("white");
		
		widgetList.add(mBackground);
		widgetList.add(mText);
		widgetList.add(mNext);
		computeGeometry();
	}
	
	@Override
	public void touchHandler(float[] pt){
		mNext.touchHandler(pt);
	}
	
	public void setNextClickListener(GameEventListener listener){
		mNext.setClickListener(listener);
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
