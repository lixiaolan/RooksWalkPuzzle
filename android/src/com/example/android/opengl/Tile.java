package com.example.android.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

abstract class Tile {

 
    public float[] center;
    public float size;
    public float angle;
    public boolean rotate;
    public String[] textures = {"clear","clear"};;
    public String color;
    public float[] pivot = {0,0,1};
    
    public Tile(float[] inCenter,float inSize) {
    	center = inCenter;
    	size = inSize;
    	angle = 0.0f;
    	rotate = false;
    }

    public boolean touched(float[] pt) {
    	return ((pt[0] < center[0]+size)&(pt[0] > center[0]-size)&(pt[1] < center[1]+size)&(pt[1] > center[1]-size));
        }
    
    public abstract void draw(MyGLRenderer r);
    
}

