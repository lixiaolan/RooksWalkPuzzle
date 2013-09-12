package com.example.android.opengl;


class BorderTile extends Tile{

    public String number;
    public boolean touched_flag;
    public String color;

    public BorderTile(float[] inCenter, float inSize, String inValue) {
    	super(inCenter, inSize);
    	number = inValue;
    	color = "transparent";
    }

    public void draw(MyGLRenderer r) {
    	String[] textures = new String[2];
    	System.out.println(number);
    	textures[0] = number;
    	textures[1] = "clear";
    	r.drawTile(center, size, textures, color, 0);
    }
}
