package com.seventhharmonic.android.freebeeline;



class ScreenTile extends Tile{

    
    
    public ScreenTile(float[] inCenter,float inSize) {
    	super(inCenter, inSize);
    	setTextures(TextureManager.CLEAR, TextureManager.CLEAR);
    	setColor("blue");
    }
            
    public void draw(MyGLRenderer r) {
    	r.drawTile(center, size, textures, color, angle, pivot);
    }
}
