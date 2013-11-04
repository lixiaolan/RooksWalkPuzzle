package com.seventhharmonic.android.freebeeline;


class SelectOneMenu extends GameMenu{

   
    
    public SelectOneMenu(float[] pos, float size, String[] textures) {
    	initialize(pos, size, textures);
    }
	
    
    private void initialize(float[] pos, float size, String[] textures) {
    	tilesSize = size;
    	tilesCenter = pos;
    	radius = 0.0f;
    	
    	menuTiles = new MenuTile[textures.length];
    	for (int i = 0; i < menuTiles.length; i++) {
    	    float Sx = tilesCenter[0];
    	    float Sy = tilesCenter[1];
    	    float center[] = { Sx, Sy, 0.0f};
    	    menuTiles[i] = new MenuTile(center, tilesSize, textures[i]);
    	}
    	centerTile = new MenuTile(tilesCenter, tilesSize, TextureManager.CLEAR);	
    	centerTile.setTextures(TextureManager.CLEAR, TextureManager.CLEAR);
    }
    
    public void animate() {
	    for (int i = 0; i < menuTiles.length; i++) {     
		float Sx = tilesCenter[0];
		float Sy = tilesCenter[1];
		menuTiles[i].setCenter(Sx, Sy);	
	}
    }
    
   

}
