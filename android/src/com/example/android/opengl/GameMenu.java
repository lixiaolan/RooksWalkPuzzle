package com.example.android.opengl;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import android.os.SystemClock;

class GameMenu{

    public MenuTile[] menuTiles;
    public MenuTile centerTile;
    public float radius;
    public float tiltAngle;
    public float[] tilesCenter = new float[3];
    public float tilesSize;
    public long refTime;
    
    public GameMenu(float[] pos, float size, String[] textures, String centerTexture, float tAngle) {
    	tiltAngle = tAngle;
    	initialize(pos, size, textures, centerTexture);
    }
	
    public GameMenu(float[] pos, float size, String[] textures, String centerTexture){
    	tiltAngle = -((float)(textures.length-1))*((float)Math.PI)/5.0f+((float)Math.PI)/2.2f;
    	initialize(pos, size, textures, centerTexture);
    }
    
    private void initialize(float[] pos, float size, String[] textures, String centerTexture) {
    	tilesSize = size;
    	tilesCenter = pos;
    	radius = size*4.0f;
    	
    	menuTiles = new MenuTile[textures.length];
    	for (int i = 0; i < menuTiles.length; i++) {
    	    float tmpsin = (float)Math.sin(tiltAngle+i*Math.PI/5.0f);
    	    float tmpcos = (float)Math.cos(tiltAngle+i*Math.PI/5.0f);
    	    float Sx = tilesCenter[0] + radius*tmpcos;
    	    float Sy = tilesCenter[1] - radius*tmpsin;
    	    float center[] = { Sx, Sy, 0.0f};
    	    menuTiles[i] = new MenuTile(center, tilesSize, textures[i]);
    	}
    	centerTile = new MenuTile(tilesCenter, tilesSize, centerTexture);	
    	refTime = System.currentTimeMillis();
    }
    

    public int touched(float[] pt) {
    	
    	for (int i = 0; i < menuTiles.length; i++) {
    		if (menuTiles[i].touched(pt)) {	    	
    			return i+1;
    		}
    	}
    	
    	if (centerTile.touched(pt)) {
    		return 0; 
    	}
    	return -1;
    }

    public void draw(MyGLRenderer r) {
	
	    animate();
	    for (int i = 0; i < menuTiles.length; i++) {
		menuTiles[i].draw(r);
	    }
	    centerTile.draw(r);
    }

    public void animate() {
	long time = System.currentTimeMillis() - refTime;  	
	float tRadius = Math.min( radius, (radius / 250.0f) * ((int) time) ); 
	    for (int i = 0; i < menuTiles.length; i++) {     
		float tmpsin = (float)Math.sin(tiltAngle+i*Math.PI/5.0f);
		float tmpcos = (float)Math.cos(tiltAngle+i*Math.PI/5.0f);
		float Sx = tilesCenter[0] + tRadius*tmpcos;
		float Sy = tilesCenter[1] - tRadius*tmpsin;
		float center[] = { Sx, Sy, 0.0f};
		menuTiles[i].center = center;
	
	}
    }
    
    public void setTexture(int loc, String texture) {
	if (loc < menuTiles.length) {
	    float[] center = menuTiles[loc].center;
	    float size = menuTiles[loc].size;
	    menuTiles[loc] = new MenuTile(center, size, texture);
	}
    }

}
