package com.example.android.opengl;

abstract class Tile {

 
    public float[] center;
    public float size;
    float angle;
    String[] textures = {"clear","clear"};;
    String color = "transparent";
    float[] pivot = {0,0,1};
    
    public Tile(float[] inCenter,float inSize) {
    	center = inCenter;
    	size = inSize;
    	angle = 0.0f;
    }

    public boolean touched(float[] pt) {
    	return ((pt[0] < center[0]+size)&(pt[0] > center[0]-size)&(pt[1] < center[1]+size)&(pt[1] > center[1]-size));
        }
    
    public abstract void draw(MyGLRenderer r);
    
    public void setPivot(float[] pivot){
    	this.pivot = pivot;
    }
    
    public void setAngle(float angle){
    	this.angle = angle;
    }
 
    public void setTextures(String texture0, String texture1) {
 	   textures[0] = texture0;
 	   textures[1] = texture1;
    }
    
    
    public void setColor(String c) {
 	   color = c;
    }
    
    
    
}

