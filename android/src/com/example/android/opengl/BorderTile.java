package com.example.android.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class BorderTile extends Tile{

   public int value;

    public BorderTile(float[] inCenter,float inSize, int inValue) {
    	super(inCenter, inSize);
    	value = inValue;
    }
  
}
