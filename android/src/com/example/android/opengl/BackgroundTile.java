package com.example.android.opengl;

public class BackgroundTile extends Tile{
    public String background;
    
    public BackgroundTile(float[] center,float size) {
	super(center,  size);
	textures[0] = TextureManager.CLEAR;
	textures[1] = TextureManager.CLEAR;
	color = "transparent";
    }
    
    public BackgroundTile(float[] center,float size, String bg){
	super(center,  size);
	this.background = bg;
	textures[0] = bg;
	textures[1] = "clear";
	color = "transparent";
    }
    
    public void setTextures(String bg1, String bg2) {
	textures[0] = bg1;
	textures[1] = bg2;  
    }
    
    @Override
    public void draw(MyGLRenderer r) {
    	r.drawTile(center,size,textures,color,0, pivot);
    }
    
}
