package com.seventhharmonic.android.freebeeline.graphics;

public class Geometry {
	public static float PHI = 1.618f;
	
	float[] geometry = {0,0,0};
	public void setGeometry(float x, float y){
		geometry[0] = x;
		geometry[1] = y;
	}
	
	public float[] getGeometry(){
		return geometry;
	}
	
}
