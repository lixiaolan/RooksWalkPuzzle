package com.example.android.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.example.android.opengl.geometry.*;

class Tile {
    private Square square;
    private boolean touched_flag;
    private int true_solution;
    private int true_arrow;

    public Tile(float[] center, int solution) {
	touched_flag = false;
	true_solution = solution;
	if (true_solution==-1) {	    
	    square = new Square(center, Colors.black);
	} else {	    
	    square = new Square(center, Colors.white);
	}
	    
    }
    
    public void draw(float[] mvpMatrix) {
	square.draw(mvpMatrix);
    }

    public void touched(float[] pt) {
	touched_flag = square.touched(pt);
	if(true_solution != -1){
	    if (touched_flag == true) {
		square.color = Colors.col;
	    } else {
		square.color = Colors.white;
	    }
	}
    }
}
