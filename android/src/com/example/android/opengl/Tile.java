package com.example.android.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class Tile {

 
    public float[] center;
    public float size;
    
    public Tile(float[] inCenter,float inSize) {
	center = inCenter;
	size = inSize;
    }

    public boolean touched(float[] pt) {
    	return ((pt[0] < center[0]+size)&(pt[0] > center[0]-size)&(pt[1] < center[1]+size)&(pt[1] > center[1]-size));
        }
}

