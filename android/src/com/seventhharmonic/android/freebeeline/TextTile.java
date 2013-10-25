package com.seventhharmonic.android.freebeeline;



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
    
    public void draw(MyGLRenderer r) {
    	r.drawRectangleTile(mode, center, w, h, textures, color, angle, pivot);
    }
}
