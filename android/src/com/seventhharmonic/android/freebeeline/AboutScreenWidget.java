package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.android.freebeeline.graphics.TextureManager;

import android.util.Log;

/*
 * Every widget has an absolute center and a relative center. The absolute center
 * is computed in Widget layout and passed in. The relative center is specified by the user
 * You have to override the setCenter method to pass down the center data to your own squares.
 * 
 */

public class AboutScreenWidget extends WidgetLayout{
    TextBox mText;
	ImageWidget mLogo;
    String TAG = "AboutScreenWidget";

    public AboutScreenWidget(){
	float height = Math.abs(GlobalApplication.getGeometry().getGeometry()[1]);
	
	Log.d(TAG, Float.toString(height));
	mText = new TextBox(0,0,.9f,"BeeLine is a Seventh Harmonic production.^^ Version Beta-1.0^^Developed by Lalit Jain, Leland Jefferis^ Art by Nathan Clement, Music by Leland Jefferis^^ For questions, comments, or to submit a bug report, visit www.seventhharmonic.com^^ Copyright 2013 Seventh Harmonic.");
	mText.setRelativeCenter(0,0);
	//mText.setFontSize(fontSize);
	widgetList.add(mText);
	
	mLogo = new ImageWidget(0,0,.8f, .6f, TextureManager.LOGO);
	mLogo.setRelativeCenter(0,height-.7f);
	mLogo.setColor("white");
	mLogo.setMode(MyGLRenderer.CROPTOP); 
	widgetList.add(mLogo);
	
	mText = new TextBox(0,0,1,"");
	mText.setRelativeCenter(0,height-mText.getHeight());
	widgetList.add(mText);
	
	setCenter(0,0);
	setWidth(mLogo.getWidth());
	setHeight(mLogo.getHeight());
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
    	return false;
    }

	@Override
	public void touchHandler(float[] pt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void swipeHandler(String direction) {
		// TODO Auto-generated method stub
		
	}    
}
