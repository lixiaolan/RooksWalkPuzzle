package com.example.android.opengl;

public class BeeTile extends Tile{
	    public String BGTexture;
	    public boolean touched_flag;
	    public String color;
	  
	    public BeeTile(float[] inCenter,float inSize) {
	    	super(inCenter, inSize);
	    	touched_flag = false;
	    	BGTexture = "bee";
	    	color = "transparent";
	    }
	    
	    public void draw(MyGLRenderer r) {
	    	String[] textures = new String[2];
	    	textures[0] = "clear";
	    	textures[1] = BGTexture;
	    	r.drawTile(center, size, textures, color);
	    }
}

