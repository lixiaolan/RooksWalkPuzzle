package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.android.freebeeline.graphics.TextureManager;


class SelectTwoMenu extends GameMenu{

   
    
    public SelectTwoMenu(float[] pos, float size, String[] textures) {
    	initialize(pos, size, textures);
    }
	
    
    
    private void initialize(float[] pos, float size, String[] textures) {
    	tilesSize = size;
    	tilesCenter = pos;
    	radius = size*2.0f;
    	
    	menuTiles = new MenuTile[textures.length];
    	for (int i = 0; i < menuTiles.length; i++) {
    	    float tmpsin = (float)Math.sin(i*Math.PI);
    	    float tmpcos = (float)Math.cos(i*Math.PI);
    	    float Sx = tilesCenter[0] + radius*tmpcos;
    	    float Sy = tilesCenter[1] - radius*tmpsin;
    	    float center[] = { Sx, Sy, 0.0f};
    	    menuTiles[i] = new MenuTile(center, tilesSize, textures[i]);
    	}
    	centerTile = new MenuTile(tilesCenter, tilesSize, TextureManager.CLEAR);	
    	centerTile.setTextures(TextureManager.CLEAR, TextureManager.CLEAR);
    	refTime = System.currentTimeMillis();
    }
    
    public void animate() {
	long time = System.currentTimeMillis() - refTime;  	
	float tRadius = Math.min( radius, (radius / 250.0f) * ((int) time) ); 
	    for (int i = 0; i < menuTiles.length; i++) {     
		float tmpsin = (float)Math.sin(i*Math.PI);
		float tmpcos = (float)Math.cos(i*Math.PI);
		float Sx = tilesCenter[0] + tRadius*tmpcos;
		float Sy = tilesCenter[1] - tRadius*tmpsin;
		float center[] = { Sx, Sy, 0.0f};
		menuTiles[i].center = center;
	
	}
    }
    
   

}
