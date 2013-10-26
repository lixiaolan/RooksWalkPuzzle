package com.seventhharmonic.android.freebeeline;


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
    	//Deactivate menu!!
    	if(!menuActive) {
    		return -1;
    	}
    	menuActive=false;
    	for (int i = 0; i < menuTiles.length; i++) {
    		if (menuTiles[i].touched(pt)) {	    	
    			return i;
    		}
    	}
    	return -1;
    }

    public void draw(MyGLRenderer r) {
	if (menuActive) {
	    animate();
	    for (int i = 0; i < menuTiles.length; i++) {
		menuTiles[i].draw(r);
	    }
	}
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
}
