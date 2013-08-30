package com.example.android.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.android.opengl.geometry.*;

class Tile {

    private boolean touched_flag;
    private int true_solution;
    private int true_arrow;
    private float[] center;
    private float size;

    public Tile(float[] inCenter,float inSize, int solution) {
	touched_flag = false;
	true_solution = solution;
	center = inCenter;
	size = inSize;
    }
    

    // public void touched(float[] pt) {
    // 	touched_flag = square.touched(pt);
    // 	if(true_solution != -1){
    // 	    if (touched_flag == true) {
    // 		square.color = Colors.col;
    // 	    } else {
    // 		square.color = Colors.white;
    // 	    }
    // 	}
    // }
}
