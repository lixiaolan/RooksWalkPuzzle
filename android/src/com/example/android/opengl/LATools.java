package com.example.android.opengl;

public class LATools {

    public static float[] getForce(float[] centers, BoardTile[] tiles, int i, float lastTouchTime, float[] lastTouchPos) {
	float[] force = {0.0f, 0.0f};
	float[] temp = new float[2];
	float[] mid = {centers[2*i],centers[2*i+1],0.0f};
	force = vSProd(-2.0f,vDiff(tiles[i].center, mid)); 
	force = vSum(force, vSProd(1.2f,tiles[i].velocity));
	//Compute wave of forces due to touch
	float time = (System.currentTimeMillis()-lastTouchTime)/1000f;
	temp = vDiff(tiles[i].center, lastTouchPos);
	float sTemp = abs(temp);
	if (sTemp<time && sTemp > time - .1f && sTemp > .00001f)
	    force = vSum(force, vSProd(5f/((float)Math.pow(sTemp,1)), temp));
	return force;
    }
    
    public static float[] vDiff(float[] left, float[] right) {
	float[] ret = new float[2];
	ret[0] = left[0] - right[0];
	ret[1] = left[1] - right[1];
	return ret;
	}
    
    public static float[] vSum(float[] left, float[] right) {
	float[] ret = new float[2];
	for (int i = 0; i < left.length; i++)
	    ret[i] = left[i] + right[i];
	return ret;
    }
    
    public static float[] vSProd(float scalar, float[] vec) {
	float[] ret = new float[vec.length];
	for (int i = 0; i < vec.length; i++)
	    ret[i] = vec[i]*scalar;
	return ret;
    }
    
    public static float vDot(float[] l, float[] r) {
	float ret = 0.0f;
	for (int i = 0; i < Math.min(l.length,r.length); i++)
	    ret += l[i]*r[i];
	return ret;
    }
    
    public static float abs(float[] vec) {
	float ret = 0.0f;
	for (int i = 0; i < vec.length; i++)
	    ret += vec[i]*vec[i];
	ret = (float)Math.sqrt(ret);
	return ret;
    }

    public static float vCross(float[] l, float[] r) {
	return (l[0]*r[1] - l[1]*r[0]);
    }
}
