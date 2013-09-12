package com.example.android.opengl;

public class BackgroundTile extends Tile{
	public String background;
	
	public BackgroundTile(float[] center,float size, String bg){
		super(center,  size);
		this.background = bg;
		textures[0] = bg;
		textures[1] = "clear";
		color = "transparent";
	}
	
	@Override
	public void draw(MyGLRenderer r) {
		r.drawTile(center,size,textures,color,0);
	}

}
