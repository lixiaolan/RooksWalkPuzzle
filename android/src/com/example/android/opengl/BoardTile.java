package com.example.android.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class BoardTile extends Tile{

    public boolean touched_flag;
    public int true_solution;
    public int true_arrow;
   
    public int number;
    public int arrow;

    public int texture;
    public float[] center;
    public float size;

    public BoardTile(float[] inCenter,float inSize, int solution) {
    super(inCenter, inSize);
    touched_flag = false;
	true_solution = solution;
	arrow = -1;
	number = 0;
	center = inCenter;
	size = inSize;
    }

}
