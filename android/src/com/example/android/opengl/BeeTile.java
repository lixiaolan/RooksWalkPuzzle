package com.example.android.opengl;

public class BeeTile extends Tile{
	    public boolean touched_flag;
	   
	    public BeeTile(float[] inCenter,float inSize) {
	    	super(inCenter, inSize);
	    	touched_flag = false;
	    	color = "transparent";
	    	textures[0] = "bee";
	    }
	    
	    public void draw(MyGLRenderer r) {
	    	r.drawTile(center, size, textures, color, angle, pivot);
	    }
}

