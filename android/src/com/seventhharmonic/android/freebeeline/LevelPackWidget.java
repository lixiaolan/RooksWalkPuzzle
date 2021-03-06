package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.com.freebeeline.levelresources.LevelPack;

import android.util.Log;

/*
 * Every widget has an absolute center and a relative center. The absolute center
 * is computed in Widget layout and passed in. The relative center is specified by the user
 * You have to override the setCenter method to pass down the center data to your own squares.
 * 
 */

public class LevelPackWidget extends WidgetLayout{
    //TextWidget mText;
    ImageWidget mImage;
    String TAG = "LevelPackWidget";

    public LevelPackWidget(LevelPack lp){

	float height = Math.abs(GlobalApplication.getGeometry().getGeometry()[1]);
	
	Log.d(TAG, Float.toString(height));
	//mText = new TextWidget(0,0,1,.5f,text);
	//mText.setRelativeCenter(0,height-mText.getHeight());
	
	mImage = new ImageWidget(0,0,.8f, .5f, lp.getTitle());
	mImage.setRelativeCenter(0,0);
	mImage.setBorder(true);
	mImage.setColor("white");
	mImage.setMode(MyGLRenderer.CROPTOP);
	widgetList.add(mImage);
	
	setCenter(0,0);
	setWidth(mImage.getWidth());
	setHeight(mImage.getHeight());
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

    public boolean isTouched(float[] pt){
	return mImage.isTouched(pt);
    }

	@Override
	public void touchHandler(float[] pt) {
		return;
	}

	@Override
	public void swipeHandler(String direction) {
		return;
	}    
}
