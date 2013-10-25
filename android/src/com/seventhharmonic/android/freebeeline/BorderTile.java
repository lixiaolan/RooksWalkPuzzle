package com.seventhharmonic.android.freebeeline;


class BorderTile extends Tile{

    public String number;

    public BorderTile(float[] inCenter, float inSize) {
    	super(inCenter, inSize);
    }
    
    public BorderTile(float x, float y, float inSize) {
    	super(x,y, inSize);
    }
    
    public void draw(MyGLRenderer r) {
    	r.drawTile(center, size, textures, color, 0,   pivot, false);
    }
}
