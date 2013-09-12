package com.example.android.opengl;

public class BackgroundTile extends Tile{
	public String bg;
	public String[] textures = new String[2];
	
	public BackgroundTile(float[] center,float size, String bg){
		super(center,  size);
		this.bg = bg;
		textures[0] = bg;
		textures[1] = "clear";
	}
	
	
	@Override
	public void draw(MyGLRenderer r) {
		r.drawTile(center,size,textures,"transparent",0);
		
	}

}
