package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.com.freebeeline.levelresources.LevelPack;

import android.util.Log;

/*
 * Every widget has an absolute center and a relative center. The absolute center
 * is computed in Widget layout and passed in. The relative center is specified by the user
 * You have to override the setCenter method to pass down the center data to your own squares.
 * 
 */

public class LockedLevelPackWidget extends WidgetLayout{
    //TextWidget mText;
    ImageWidget mMain;
    ImageWidget mTag;
    String TAG = "LevelPackWidget";
    LevelPack lp;
    
    public LockedLevelPackWidget(LevelPack lp){
    	
	float height = Math.abs(GlobalApplication.getGeometry().getGeometry()[1]);
	
	Log.d(TAG, Float.toString(height));
	//mText = new TextWidget(0,0,1,.5f,text);
	//mText.setRelativeCenter(0,height-mText.getHeight());
	this.lp = lp;
	mMain = new ImageWidget(0,0,.8f, .5f, lp.getPurchaseBanner());
	mMain.setRelativeCenter(0,0);
	mMain.setBorder(true);
	mMain.setColor("white");
	mMain.setMode(MyGLRenderer.CROPTOP);
	widgetList.add(mMain);
	
	mTag = new ImageWidget(0,0,.4f, .4f, lp.getPurchaseTag());
	mTag.setRelativeCenter(0,-.9f);
	//mTag.setBorder(true);
	mTag.setColor("transparent");
	mTag.setMode(MyGLRenderer.CROPTOP);
	widgetList.add(mTag);
	
	setCenter(0,0);
	setWidth(mMain.getWidth());
	setHeight(mMain.getHeight());
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
    	//This is important - not a huge fan of this but basically, FlowerMenu check if this was touched.
    	//This will be touched and will launch the chapter selection iff the main image is touched.
    	return mMain.isTouched(pt);
    }

	@Override
	public void touchHandler(float[] pt) {
		//Going into the Chapter Select logic is in Flower menu.
		//All we need to do is go into the store.
		if(mTag.isTouched(pt)){
				ViewActivity.mStore.onBuyLevelPack(lp);
			}
	}

	@Override
	public void swipeHandler(String direction) {
	}    
}
