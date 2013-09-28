package com.example.android.opengl;


class MenuTile extends Tile{

    public String number;
    public boolean touched_flag;
    public String color;
    public String[] textures;
    
    public MenuTile(float[] inCenter,float inSize, int inVal) {
	super(inCenter, inSize);
	touched_flag = false;
	number = "menu_"+Integer.toString(inVal+1);
	color = "transparent";
	textures = new String[2];
	textures[0] = "menu_circle";
	textures[1] = number;
    }
    
    public MenuTile(float[] inCenter,float inSize, String inVal) {
    	super(inCenter, inSize);
    	touched_flag = false;
    	number = inVal;
    	color = "transparent";
    	textures = new String[2];
    	textures[0] = "menu_circle";
    	textures[1] = number;
    }
        
    public void draw(MyGLRenderer r) {
    	r.drawTile(center, size, textures, color, angle, pivot);
    }
}
