package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.graphics.TextCreator.TextJustification;
import com.seventhharmonic.android.freebeeline.listeners.GameEventListener;
import com.seventhharmonic.com.freebeeline.levelresources.Chapter;
import com.seventhharmonic.com.freebeeline.levelresources.LevelPack;

public class LockedChapterWidget extends WidgetLayout{
	String TAG = "LockedChapterWidget";
	ImageWidget mLocked;
	Chapter ch;
	public LockedChapterWidget(Chapter ch){

		this.ch  = ch;
		//TODO: This needs to be in the xml.
		mLocked = new ImageWidget(0,0,.8f, .8f, "chapterLockedBanner");
		mLocked.setRelativeCenter(0,0);
		mLocked.setBorder(true);
		mLocked.setColor("white");
		mLocked.setMode(MyGLRenderer.CROPTOP);
		widgetList.add(mLocked);

	}

	@Override
	public void touchHandler(float[] pt){
		if(mLocked.isTouched(pt)){
			if(ch.getLp() == null){
				Log.d("LockedChapter", "null levelpack");
			}
			ViewActivity.mStore.onBuyLevelPack(ch.getLp());
		}
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
