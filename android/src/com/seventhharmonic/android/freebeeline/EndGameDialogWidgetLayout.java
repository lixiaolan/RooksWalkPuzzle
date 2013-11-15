package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.graphics.Geometry;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
import com.seventhharmonic.android.freebeeline.listeners.GameEventListener;

public class EndGameDialogWidgetLayout extends WidgetLayout{

	
	ButtonWidget mBack;
	ButtonWidget mNext;
	boolean draw  = false;
	
	public EndGameDialogWidgetLayout(float width){
		setWidth(width);
		setHeight(width/Geometry.PHI);
		setCenter(0,0);
		mBack = new ButtonWidget(0,0, .20f, .20f, TextureManager.BACK);
		mBack.setRelativeCenter(.7f*width, 0);
		
		mNext = new ButtonWidget(0,0, .20f, .20f, TextureManager.NEXT);
		mNext.setRelativeCenter(-.7f*width, 0);
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


	@Override
	public void swipeHandler(String direction) {
		// TODO Auto-generated method stub
		
	}

}
