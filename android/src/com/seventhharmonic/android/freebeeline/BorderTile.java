package com.seventhharmonic.android.freebeeline;


class BorderTile extends Tile{

    public String number;

    public BorderTile(float[] inCenter, float inSize, String inValue) {
    	super(inCenter, inSize);
    	number = inValue;
    	color = "transparent";
    }

    public void draw(MyGLRenderer r) {
    	textures[0] = number;
    	textures[1] = "clear";
    	r.drawTile(center, size, textures, color, 0,   pivot);
    }
}
