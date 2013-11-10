package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.android.freebeeline.graphics.TextureManager;



class ImageTile extends Tile{

	float w;
	float h;
	String mode = MyGLRenderer.CROPTOP;
	
    public ImageTile(float[] inCenter, float w, float h, String text) {
    	super(inCenter, 1);
    	setTextures(text, TextureManager.CLEAR);
    	this.w = w;
    	this.h = h;
    }
    
    public ImageTile(float x, float y, float w, float h, String text) {
    	super(new float[]{x,y,0}, 1);
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
    
    public void drawSimple(MyGLRenderer r) {
    	r.drawTile(center, w, textures, color, angle, pivot,false);
    }
    
    public void draw(MyGLRenderer r) {
    	r.drawRectangleTile(mode, center, w, h, textures, color, angle, pivot);
    }
}
