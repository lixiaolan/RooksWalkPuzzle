package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.listeners.GameEventListener;
import com.seventhharmonic.com.freebeeline.levelresources.Chapter;

/*
 * Every widget has an absolute center and a relative center. The absolute center
 * is computed in Widget layout and passed in. The relative center is specified by the user
 * You have to override the setCenter method to pass down the center data to your own squares.
 * 
 */

public class ChapterWidget extends WidgetLayout{
	String TAG = "ChapterWidget";
	TextWidget mText;	
	GridWidgetLayout mGrid;
	Chapter ch;
	GameEventListener listener;
	
	public ChapterWidget(Chapter ch){
		
		float height = GlobalApplication.getGeometry().getGeometry()[1];
		mText = new TextWidget(0, 0,1,.5f,ch.getTitle());
		mText.setRelativeCenter(0,height-mText.getHeight());
		setCenter(0,0);
		setWidth(1);
		setHeight(height);
		widgetList.add(mText);
		Log.d(TAG,ch.getTitle());
		Log.d(TAG,Float.toString(ch.getWidth()));
		mGrid = new GridWidgetLayout(ch.getWidth(), ch.getHeight());
		mGrid.setRelativeCenter(0, 0);
		for(int i =0;i<ch.getNumberOfPuzzles();i++){
			ImageWidget mImage = new ImageWidget(0,0,.125f, .125f, ch.getPuzzle(i).getImage());
			mGrid.addWidget(mImage);
		}
		widgetList.add(mGrid);		
	}

	@Override
	public void touchHandler(float[] pt){
		int puzzTouched = mGrid.widgetTouched(pt);
		if(puzzTouched !=-1){
			listener.event(puzzTouched);
		}
	}
	
	public void setTouchListener(GameEventListener g){
		listener = g;
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

	

}
