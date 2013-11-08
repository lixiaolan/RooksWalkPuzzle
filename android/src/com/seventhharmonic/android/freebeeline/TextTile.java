package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.android.freebeeline.graphics.TextureManager;



class TextTile extends Tile{

	float w;
	float h;
	String mode = MyGLRenderer.CROPTOP;
	
    public TextTile(float[] inCenter, float w, float h, String text) {
    	super(inCenter, 1);
    	setTextures(text, TextureManager.CLEAR);
    	this.w = w;
    	this.h = h;
    }
    
    public void setMode(String m){
    	mode = m;
    }
    
    public boolean touched(float[] pt) {
    	boolean b = ((pt[0] < center[0]+w)&(pt[0] > center[0]-w)&(pt[1] < center[1]+h)&(pt[1] > center[1]-h));
    	if(b){
    		pt[0] = center[0];
    		pt[1] = center[1];
    	}
    	return b;
        }
    
    public void draw(MyGLRenderer r) {
    	r.drawRectangleTile(mode, center, w, h, textures, color, angle, pivot);
    }
}
