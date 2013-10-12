package com.seventhharmonic.android.freebeeline;



class GameMenu{

    public MenuTile[] menuTiles;
    public MenuTile centerTile;
    public float radius;
    public float tiltAngle;
    public float[] tilesCenter = new float[3];
    public float tilesSize;
    public long refTime;
    // public boolean useGeometry = false;//This indicates if we a using a custom GLRenderer draw function.
    // public String geometry;//Stores the goemetry string used in the draw function to select draw style.
    // public boolean gotGeometry = false;//Used to make sure we get screen info from GLRenderer only once.

    public GameMenu(float[] pos, float size, String[] textures, String centerTexture, float tAngle) {
    	tiltAngle = tAngle;
    	initialize(pos, size, textures, centerTexture);
    }
	
    public GameMenu(float[] pos, float size, String[] textures, String centerTexture){
    	tiltAngle = -((float)(textures.length-1))*((float)Math.PI)/5.0f+((float)Math.PI)/2.2f;
    	initialize(pos, size, textures, centerTexture);
    }
    // //Constructors for useing GLRenderer draw function
    // public GameMenu(float[] pos, float size, String[] textures, String centerTexture, float tAngle, String geo) {
    // 	tiltAngle = tAngle;
    // 	initialize(pos, size, textures, centerTexture);
    // 	geometry = geo;
    // 	useGeometry = true;
    // }
	
    // public GameMenu(float[] pos, float size, String[] textures, String centerTexture, String geo){
    // 	tiltAngle = -((float)(textures.length-1))*((float)Math.PI)/5.0f+((float)Math.PI)/2.2f;
    // 	initialize(pos, size, textures, centerTexture);
    // 	geometry = geo;
    // 	useGeometry = true;
    // }

    
    public GameMenu(){
    	
    }

    private void initialize(float[] pos, float size, String[] textures, String centerTexture) {
    	tilesSize = size;
    	tilesCenter = pos;
    	radius = 3.6f*size;//.9f;
    	
    	menuTiles = new MenuTile[textures.length];
    	for (int i = 0; i < menuTiles.length; i++) {
    	    float tmpsin = (float)Math.sin(tiltAngle+i*Math.PI/4.0f);
    	    float tmpcos = (float)Math.cos(tiltAngle+i*Math.PI/4.0f);
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
	// if (!gotGeometry) {
	//     if (geometry == "BOTTOMCENTER") {
	// 	float[] c = new float[3];
	// 	c = r.getTopLeft();
	// 	tilesCenter[1] = -c[1]+tilesSize;
	// 	tilesCenter[0] = 0.0f;
	//     }
	//     gotGeometry = true;
	// }
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
