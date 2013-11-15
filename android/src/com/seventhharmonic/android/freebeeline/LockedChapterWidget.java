package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.listeners.GameEventListener;
import com.seventhharmonic.com.freebeeline.levelresources.Chapter;
import com.seventhharmonic.com.freebeeline.levelresources.LevelPack;

public class LockedChapterWidget extends WidgetLayout{
	String TAG = "LockedChapterWidget";
	TextBox mText;
	//TextWidget mText;
	public LockedChapterWidget(){
		
		/*
		 * TODO: Post chapter title and some text being like - you need to finish the previous
		 * chapters to unlock this
		 */ 
		
		
		/*
		 * TODO: In the future, check should be replaced by a lock
		 *
		 */
		
	
		//widgetList.add(mImage);
	}

	@Override
	public void touchHandler(float[] pt){
	}
	
	@Override
	protected void computeGeometry() {
		for(int i =0;i< widgetList.size();i++){
			Widget w = widgetList.get(i);
			w.setCenter(getCenterX()+w.getRelativeCenterX(), getCenterY()+w.getRelativeCenterY());
		}
	}
	
	@Override
	public void setCenter(float x, float y){
			super.setCenter(x,y);
			computeGeometry();
	}
	
	@Override
	public void draw(MyGLRenderer r) {
		for(Widget w: widgetList)
			w.draw(r);
	}

	@Override
	public void swipeHandler(String direction) {
		// TODO Auto-generated method stub
		
	}

	

}
