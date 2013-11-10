package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.android.freebeeline.graphics.TextureManager;


class CircleTile extends ImageWidget{

    
    
    public CircleTile(float inSize) {
    	super(0,0, inSize, inSize,TextureManager.OPENCIRCLE);
    	//setTextures(TextureManager.CLEAR, TextureManager.OPENCIRCLE);
	}
    
    public void setFull(){
    	setImage(TextureManager.CLOSEDCIRCLE);
    }
    
    public void setEmpty() {
    	setImage(TextureManager.OPENCIRCLE);
    }
 
}
