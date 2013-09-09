package com.example.android.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class BoardTile extends Tile{

    public boolean touched_flag;
    public int true_solution;
    public int true_arrow;
   
    public String number;
    public String arrow;

    public String color;

    public BoardTile(float[] inCenter,float inSize, int solution) {
	super(inCenter, inSize);
	touched_flag = false;
	true_solution = solution;
	arrow = "clear";
	number = "clear";
	center = inCenter;
	size = inSize;
	if (solution == -1)
	    color = "black";
	else
	    color = "white";
    }

    public boolean check() {
    	//return true_solution == number;
	return false;
    }

    
    public void draw(MyGLRenderer r) {
	String[] textures = new String[2];
	textures[0] = arrow;
	textures[1] = number;
	String tColor;
	if (touched_flag)
	    tColor = "blue";
	else
	    tColor = color;

	r.drawTile(center, size, textures, tColor);
    }    
}
