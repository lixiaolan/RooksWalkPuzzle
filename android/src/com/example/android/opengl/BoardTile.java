package com.example.android.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class BoardTile extends Tile{

	public String[] textures = {"clear", "clear"};
	
    public boolean touched_flag;
    public int true_solution;
    public int true_arrow;
   
    public String number = "clear";
    public String arrow = "clear";

    public String color;

    public BoardTile(float[] inCenter,float inSize, int solution) {
	super(inCenter, inSize);
	touched_flag = false;
	true_solution = solution;
	center = inCenter;
	size = inSize;
    }

   public void setTextures(String texture0, String texture1) {
	   textures[0] = texture0;
	   textures[1] = texture1;
   }
    
   public void setTextures(){
	   textures[0]  = arrow;
	   textures[1]  = number;
   }
   
   
   public void setColor() {
		if (true_solution == -1)
		    color = "black";
		else
		    color = "white";

   }
   
   public void setColor(String c) {
	   color = c;
   }
   
   
    public void draw(MyGLRenderer r) {
    	String tColor;
    	if (touched_flag)
    		tColor = "blue";
    	else
    		tColor = color;

    	r.drawTile(center, size, textures, tColor, angle);
    }    
}
