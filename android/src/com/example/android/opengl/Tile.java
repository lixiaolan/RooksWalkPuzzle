package com.example.android.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class Tile {

    public boolean touched_flag;
    public int true_solution;
    public int true_arrow;
   
    public int number;
    public int arrow;

    public int texture;
    public float[] center;
    public float size;

    public Tile(float[] inCenter,float inSize, int solution, int inTexture) {
	touched_flag = false;
	true_solution = solution;
	arrow = -1;
	number = 0;
	center = inCenter;
	size = inSize;
	texture = inTexture;
    }
    
    public boolean touched(float[] pt) {
	return ((pt[0] < center[0]+size)&(pt[0] > center[0]-size)&(pt[1] < center[1]+size)&(pt[1] > center[1]-size));
    }
}
