package com.example.android.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class BorderTile extends Tile{

    public String number;
    public String BGTexture;
    public boolean touched_flag;
    public String color;

    public BorderTile(float[] inCenter,float inSize, String inValue) {
    	super(inCenter, inSize);
    	number = inValue;
	color = "transparent";
	BGTexture = "clear";
    }

    public void draw(MyGLRenderer r) {
	String[] textures = new String[2];
	textures[0] = BGTexture;
	textures[1] = number;
	r.drawTile(center, size, textures, color, angle);
    }
}
