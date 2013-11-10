package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
import com.seventhharmonic.com.freebeeline.levelresources.Puzzle;

public class PuzzleWidget extends Widget{
    
    ImageWidget mFlower;
    boolean rotateToggle = false;
    int currAngle = 0;
    
    public PuzzleWidget(float centerX, float centerY , float width, float height, Puzzle p){
	float[] center = {centerX, centerY, 0.0f}; 
		mFlower = new ImageWidget(0,0, width, height, p.getFlower());
		mFlower.setPivot(new float[] {0.0f, 0.0f, 1.0f});

    }
    
    @Override
    public void setColor(String color){
    	mFlower.setColor(color);
    }
    
    public void setRotate(boolean t){
	rotateToggle = t;
	if(t == false){
	    currAngle = 0;
	    mFlower.setAngle(currAngle);
	}
    }
    
    @Override
    public void draw(MyGLRenderer r) {
	//It is important that the box is drawn after the flower to get the right opaqueness. 
    	if(rotateToggle){
	    currAngle += 2 % 360;
	    mFlower.setAngle(currAngle);
	}
	mFlower.draw(r);	
    }
    
    public void setCenter(float x, float y){
	super.setCenter(x,y);
	mFlower.setCenter(x, y);
    }
    
    public boolean isTouched(float[] pt){
    	return mFlower.isTouched(pt);
    }    
}
