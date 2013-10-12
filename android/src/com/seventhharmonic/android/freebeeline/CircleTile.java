package com.seventhharmonic.android.freebeeline;


class CircleTile extends Tile{

    
    
    public CircleTile(float[] inCenter,float inSize) {
    	super(inCenter, inSize);
    	setTextures(TextureManager.CLEAR, TextureManager.OPENCIRCLE);
	}
    
    public void setFull(){
    	setTextures(TextureManager.CLEAR, TextureManager.CLOSEDCIRCLE);
    }
    
    public void setEmpty() {
    	setTextures(TextureManager.CLEAR, TextureManager.OPENCIRCLE);
    }
    
        
    public void draw(MyGLRenderer r) {
    	r.drawTile(center, size, textures, color, angle, pivot);
    }
}
