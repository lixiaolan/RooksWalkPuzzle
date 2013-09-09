package com.example.android.opengl;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import android.os.SystemClock;

class Menu{

    public MenuTile[] menuTiles = new MenuTile[6];
    public boolean menuActive;
    public float radius;
    public float tiltAngle;
    public float[] tilesCenter = new float[3];
    public float tilesSize;
    public long refTime;
    
    public Menu() {
	menuActive = false;
	tilesSize = 0.11f;
	tilesCenter[0] = 0.0f;
	tilesCenter[1] = 0.0f;
	tilesCenter[2] = 0.0f;
	tiltAngle = 0.0f;
	radius = 0.4f;
	for (int i = 0; i < menuTiles.length; i++) {
	    float tmpsin = (float)Math.sin(i*Math.PI/5.0f);
	    float tmpcos = (float)Math.cos(i*Math.PI/5.0f);
	    float Sx = tilesCenter[0] + radius*tmpcos;
	    float Sy = tilesCenter[1] + radius*tmpsin;
	    float center[] = { Sx, Sy, 0.0f};
	    menuTiles[i] = new MenuTile(center, tilesSize, i);
	}
    }    
	
    public void activate(float[] pt) {
	menuActive = true;
	refTime = System.currentTimeMillis();
	if (menuActive) {	    
	    tilesCenter[0] = pt[0];
	    tilesCenter[1] = pt[1];		
	    tilesCenter[2] = 0.0f;
	    tiltAngle = (float) Math.PI/2.0f*pt[0];
	    for (int i = 0; i < menuTiles.length; i++) {
		float tmpsin = (float)Math.sin(tiltAngle+i*Math.PI/5.0f);
		float tmpcos = (float)Math.cos(tiltAngle+i*Math.PI/5.0f);
		float Sx = tilesCenter[0] + radius*tmpcos;
		float Sy = tilesCenter[1] + radius*tmpsin;
		float center[] = { Sx, Sy, 0.0f};
		menuTiles[i].center = center;
	    }   
	}
    }

    public int touched(float[] pt) {
	for (int i = 0; i < menuTiles.length; i++) {
	    if (menuTiles[i].touched(pt)) {
		return i;
	    }
	}
	return -1;
    }

    public void animate() {
	long time = System.currentTimeMillis() - refTime;  	
	float tRadius = Math.min( radius, (radius / 250.0f) * ((int) time) ); 
 
	if (menuActive) {	    
	    for (int i = 0; i < menuTiles.length; i++) {     
		float tmpsin = (float)Math.sin(tiltAngle+i*Math.PI/5.0f);
		float tmpcos = (float)Math.cos(tiltAngle+i*Math.PI/5.0f);
		float Sx = tilesCenter[0] + tRadius*tmpcos;
		float Sy = tilesCenter[1] + tRadius*tmpsin;
		float center[] = { Sx, Sy, 0.0f};
		menuTiles[i].center = center;
	    }   
	}
    }

    public void draw(MyGLRenderer r) {
	if (menuActive) {
	    for (int i = 0; i < menuTiles.length; i++) {
		menuTiles[i].draw(r);
	    }
	}
    }
    // public void swiped(float[] pt, int direction) {
    // 	if (direction == 0) {
    // 	    System.out.println("East");
    // 	}
    // 	if (direction == 1) {
    // 	    System.out.println("North");
    // 	}
    // 	if (direction == 2) {
    // 	    System.out.println("West");
    // 	}
    // 	if (direction == 3) {
    // 	    System.out.println("South");
    // 	}
    // }

}
