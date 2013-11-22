package com.seventhharmonic.android.freebeeline.physics;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.*;
import com.seventhharmonic.android.freebeeline.StateWidget.DrawPeriod;
import com.seventhharmonic.android.freebeeline.util.LATools;
import com.seventhharmonic.com.freebeeline.levelresources.*;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
import com.seventhharmonic.android.freebeeline.listeners.*;
import com.seventhharmonic.com.freebeeline.levelresources.LevelPack;
import com.seventhharmonic.com.freebeeline.levelresources.LevelPackProvider;
import java.util.HashMap;
import java.util.Map;

class LevelPack1 extends StateWidget {
    float t;
    private long startTime;
    private long refTime;
    private float[] centers;
    private float[] fixedCenters;
    private FlowerTile[] tiles; 
    private long lastTouchTime = 0;
    private float[] lastTouchPos = new float[2];
    
    
    public LevelPack1(FlowerTile[] in) {	
	startTime = System.currentTimeMillis();
	
	tiles = in;
	//mBee.setMood(Mood.HAPPY);
	//Initialize tiles to have a random velocity.
	centers = new float[2*tiles.length];
	fixedCenters = new float[2*tiles.length];
	
	for (int i = 0; i < tiles.length; i++) {
	    double r = Math.random();
	    tiles[i].velocity[0] = (float)(-1*r+(1-r)*1);
	    r = Math.random();
	    tiles[i].velocity[1] = (float)(-1*r+(1-r)*1);
	    tiles[i].setTextures(TextureManager.CLEAR, TextureManager.getFlowerTexture());
	}
	
	//Initiate the centers array.
	refTime = System.currentTimeMillis();
	float tt = ((float)(refTime - startTime))/2000.0f;
	for (int i = 0; i<tiles.length; i++ ) {
	    float ii = (float)i;
	    float r = (ii + 10*(1-1/(ii+1)))/25;
	    t = ii/1.0f; 
	    fixedCenters[2*i] =  r*((float)Math.sin(t));
	    fixedCenters[2*i+1] = r*((float)Math.cos(t));
	    centers[2*i]   = ((float)Math.cos(tt))*fixedCenters[2*i] 
		+ ((float)Math.sin(tt))*fixedCenters[2*i + 1];
	    centers[2*i+1]   = -((float)Math.sin(tt))*fixedCenters[2*i] 
		+ ((float)Math.cos(tt))*fixedCenters[2*i + 1];	
	}
    }
    
    @Override
    public void reset() {
	refTime = System.currentTimeMillis();
    }
    
    @Override
    public void enterAnimation() {
	period = DrawPeriod.DURING;
	refTime = System.currentTimeMillis();
    }
    
    float[] force = new float[2];
    long time;	
    float fTime;
    float dt;	
    
    @Override
    public void duringAnimation() {
	time = System.currentTimeMillis()-refTime;
	dt = Math.min(((float)time)/1000.0f, .0333f);
	refTime = System.currentTimeMillis();
	t = ((float)(refTime - startTime))/2000.0f;
	for (int i = 0; i<tiles.length; i++ ) {
	    centers[2*i]   = ((float)Math.cos(t))*fixedCenters[2*i] 
		+ ((float)Math.sin(t))*fixedCenters[2*i + 1];
	    centers[2*i+1]   = -((float)Math.sin(t))*fixedCenters[2*i] 
		+ ((float)Math.cos(t))*fixedCenters[2*i + 1];	
	}
	
	for(int i=0;i<tiles.length;i++){
	    getForce(tiles, i);
	    LATools.vSProd(dt, tiles[i].velocity, temp);
	    LATools.vSum(tiles[i].getCenter(), temp ,tiles[i].getCenter());
	    LATools.vSProd(dt, force, temp);
	    LATools.vSum(tiles[i].velocity, temp,tiles[i].velocity);
	}
	
    }
    
    float[] temp = new float[2];
    float[] mid = new float[3];    
    float sTemp;
    public void getForce(FlowerTile[] tiles, int i) {
	mid[0] = centers[2*i]; mid[1] = centers[2*i+1]; 
	LATools.vDiff(tiles[i].center, mid, temp);
	LATools.vSProd(-2.0f,temp, force); 
	LATools.vSProd(-1.2f,tiles[i].velocity, temp);
	LATools.vSum(force, temp, force);
	//Compute wave of forces due to touch
	fTime =2*((float)(System.currentTimeMillis()-lastTouchTime))/1000f;
	LATools.vDiff(tiles[i].center, lastTouchPos, temp);
	sTemp = LATools.abs(temp);
	if (sTemp<fTime && sTemp > fTime - .2f && sTemp > .00001f){
	    LATools.vSProd(5f/((float)Math.pow(sTemp,1)), temp, temp);
	    LATools.vSum(force, temp, force);
	}
    }
    
    @Override	    
    public void draw(MyGLRenderer r){	   
	super.draw(r);
	//mBee.draw(r);
	//mCPB.draw(r);
	for(int i =0;i< tiles.length;i++){
	    tiles[i].draw(r);
	}
	//mBanner.draw(r);
	//slider.draw(r);	    
    }
    
    @Override
    public void swipeHandler(String direction) {
	return;	
    }
    
    @Override
    public void touchHandler(float[] pt) {
	lastTouchPos = pt;
	lastTouchTime = System.currentTimeMillis();
    }	
}
