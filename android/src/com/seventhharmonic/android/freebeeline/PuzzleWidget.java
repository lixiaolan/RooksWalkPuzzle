package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.com.freebeeline.levelresources.Puzzle;

public class PuzzleWidget extends Widget{
    
    TextTile mFlower;
    TextTile mBox;
    boolean rotateToggle = false;
    int currAngle = 0;
    
    public PuzzleWidget(float centerX, float centerY , float width, float height, Puzzle p){
	float[] center = {centerX, centerY, 0.0f}; 
	mFlower = new TextTile(center, width, height, p.getImage());
	mFlower.setPivot(new float[] {0.0f, 0.0f, 1.0f});
	
	mBox = new TextTile(center, width, height, TextureManager.BOX);
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
	mBox.draw(r);
	if(rotateToggle){
	    currAngle += 2 % 360;
	    mFlower.setAngle(currAngle);
	}
	mFlower.draw(r);	
    }
    
    public void setCenter(float x, float y){
	super.setCenter(x,y);
	mFlower.setCenter(x, y);
	mBox.setCenter(x, y);
    }
    
    public boolean isTouched(float[] pt){
	return mFlower.touched(pt);
    }    
}
