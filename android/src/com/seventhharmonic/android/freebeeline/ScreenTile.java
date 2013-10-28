package com.seventhharmonic.android.freebeeline;



class ScreenTile extends Tile{

    public ScreenTile(float[] inCenter,float inSize) {
    	super(inCenter, inSize);
    	setTextures(TextureManager.MATCHINGNUMBERRULE, TextureManager.CLEAR);
    	setColor("blue");
    }
            
    
    public void draw(MyGLRenderer r) {
    	r.drawRectangleTile(MyGLRenderer.CROPTOP, center, 1.0f,.5f, textures, color, angle, pivot);
    }
}
