package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
import com.seventhharmonic.com.freebeeline.levelresources.Puzzle;

public class PuzzleWidget extends Widget{
    
    private static final String TAG = "PuzzleWidget";
	ImageWidget mFlower;
    boolean rotateToggle = false;
    int currAngle = 0;
    boolean pulseToggle = false;
    
    public PuzzleWidget(float centerX, float centerY , float width, float height, Puzzle p){
    	float[] center = {centerX, centerY, 0.0f}; 
    	this.center = center;
    	this.width = width;
    	this.height = height;
    	mFlower = new ImageWidget(0,0, width, height, p.getFlower());
		mFlower.setPivot(new float[] {0.0f, 0.0f, 1.0f});
    }
    
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
    
    public void setPulse(boolean t){
    	pulseToggle = t;
    }
        
    float pulseAmount = 0;
    @Override
    public void draw(MyGLRenderer r) {
	//It is important that the box is drawn after the flower to get the right opaqueness. 
    	if(rotateToggle){
    		currAngle += 2 % 360;
    		mFlower.setAngle(currAngle);
    	}
    	
    	if(pulseToggle){
    		pulseAmount = (pulseAmount +5)%360; 
    		mFlower.setHeight(height*(Math.abs(.2f*(float)Math.sin((pulseAmount)*3.14f/180f))+.8f));
    		mFlower.setWidth(width*(Math.abs(.2f*(float)Math.sin((pulseAmount)*3.14f/180f))+.8f));
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

	@Override
	public void touchHandler(float[] pt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void swipeHandler(String direction) {
		// TODO Auto-generated method stub
		
	}    


}
