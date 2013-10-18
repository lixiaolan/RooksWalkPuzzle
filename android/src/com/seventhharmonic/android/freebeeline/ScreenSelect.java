package com.seventhharmonic.android.freebeeline;


class ScreenSelect extends Widget{

    public ScreenTile[] screenTiles;
    int activeTile = 0;
    int length;
    long refTime = 0;
    String currDirection;
    boolean active = false;
    float gap = 2.5f;
    float size;
    
    public ScreenSelect(int length, float size, float gap) {
    	this.length = length;
    	this.size = size;
    	this.gap = gap;
    	screenTiles = new ScreenTile[length];
    	for(int i =0;i<length;i++){
    		float[] center = {-1*i*(2*size+gap), 0, 0};
    		screenTiles[i] = new ScreenTile(center, size);
    	}
    
    }
    
    public void setTile(String texture0, String texture1, int tile){
    	screenTiles[tile].setTextures(texture0, texture1);
    }
    
    public void touchHandler(float[] pt) {
   
    }

    public void swipeHandler(String direction){
    	System.out.println("got swiped");
    	if(direction.equals("right_arrow") || direction.equals("left_arrow")){
    		refTime = System.currentTimeMillis();
    		currDirection = direction;
    		initialize();
    	}
    }
    
    private void initialize(){
    	if(currDirection.equals("left_arrow") && activeTile !=0){
    		activeTile = activeTile -1;
    		active = true;
    	} else if(currDirection.equals("right_arrow") && activeTile !=length-1){
    		activeTile = activeTile + 1;
    		active = true;
    	}
    	
    }
    
    private void animate(){
    	float t = (float)(System.currentTimeMillis() -refTime)/500.0f;
    	if(t < 1) {
    		System.out.println(t);
    		for(int i=0;i<length;i++){
    			float centerX = screenTiles[i].getCenter2D()[0];
    			screenTiles[i].setCenter((1-t)*centerX + -1*t*(i-activeTile)*(2*size+gap), 0);
    		}
    	} else {
    		active = false;
    	}
    }
    
    public void draw(MyGLRenderer r) {
    	for(int i=0;i<length;i++){
    		if(active){
    			animate();
    		} 
    		screenTiles[i].draw(r);
    	}
    }



}


