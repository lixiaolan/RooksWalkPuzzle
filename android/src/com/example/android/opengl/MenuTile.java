package com.example.android.opengl;


class MenuTile extends Tile{

    public String number;
    public String BGTexture;
    public boolean touched_flag;
    public String color;
  
    public MenuTile(float[] inCenter,float inSize, int inVal) {
	super(inCenter, inSize);
	touched_flag = false;
	number = "menu_"+Integer.toString(inVal+1);
	BGTexture = "menu_circle";
	color = "transparent";
    }
    
    public void draw(MyGLRenderer r) {
	String[] textures = new String[2];
	textures[0] = BGTexture;
	textures[1] = number;
	r.drawTile(center, size, textures, color, angle);
    }
}
