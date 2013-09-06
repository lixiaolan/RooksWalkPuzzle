package com.example.android.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class MenuTile extends Tile{

	public int value;
    public boolean touched_flag;
  
    
    public MenuTile(float[] inCenter,float inSize, int inValue) {
    super(inCenter, inSize);
	touched_flag = false;
	value = inValue;
    }
    
   
}
