package com.seventhharmonic.android.freebeeline;

abstract class Tile {

 
    public float[] center;
    public float size;
    float angle;
    String[] textures = {"clear","clear"};
    String color = "transparent";
    float[] pivot = {0,0,1};
    
    public Tile(float[] inCenter,float inSize) {
    	//center = inCenter;
    	center = new float[3];
    	center[0] = inCenter[0];
    	center[1] = inCenter[1];
    	center[2] = inCenter[2];
    	size = inSize;
    	angle = 0.0f;
    }

    public Tile(float x, float y,  float inSize){
    	center = new float[3];
    	center[0] = x;
    	center[1] = y;
    	center[2] = 0.0f;
    	size = inSize;
    	angle = 0.0f;
    }
    
    public boolean touched(float[] pt) {
    	boolean b = ((pt[0] < center[0]+size)&(pt[0] > center[0]-size)&(pt[1] < center[1]+size)&(pt[1] > center[1]-size));
    	if(b){
    		pt[0] = center[0];
    		pt[1] = center[1];
    	}
    	return b;
        }
    
    public abstract void draw(MyGLRenderer r);
    
    public float getSize() {
    	return size;
    }
    
    public void setPivot(float[] pivot){
    	this.pivot = pivot;
    }
    
    public void setAngle(float angle){
    	this.angle = angle;
    }

    public void setCenter2D(float[] in) {
	center[0] = in[0];
	center[1] = in[1];
    }

    public void setCenter(float in0, float in1) {
    	center[0] = in0;
    	center[1] = in1;
        }

    
    public float[] getCenter2D() {
	float[] ret = new float[2];
	ret[0] = center[0];
	ret[1] = center[1];
	return ret;
    }

    public float[] getCenter() {
	return center;
    }
 
    public void setTextures(String texture0, String texture1) {
 	   textures[0] = texture0;
 	   textures[1] = texture1;
    }
    
    
    public void setSize(float size){
    	this.size = size;
    }
    
    public void setColor(String c) {
	color = c;
    }
    
    
    
}

