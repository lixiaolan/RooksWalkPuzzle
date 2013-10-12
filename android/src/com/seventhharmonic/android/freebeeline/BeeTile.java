package com.seventhharmonic.android.freebeeline;

public class BeeTile extends Tile{
    public boolean touched_flag;
    public float[] velocity = new float[2];
    
    
    public BeeTile(float[] inCenter,float inSize) {
	super(inCenter, inSize);
	touched_flag = false;
	color = "transparent";
	textures[0] = TextureManager.BEE;
	velocity[0] = 0.0f;
	velocity[1] = 0.0f;
    }
    
    public void draw(MyGLRenderer r) {

	if (velocity[0] > 0.0f) {
	    angle = 45.0f*((float)(Math.atan(velocity[1]/velocity[0])/Math.atan(1)));
	}
	else if (velocity[0] < 0.0f)
	    angle = 180.0f + 45.0f*((float)(Math.atan(velocity[1]/velocity[0])/Math.atan(1)));
	else {
	    if (velocity[1] > 0.0f) {
		angle = 90.0f;
	    }
	    else {
		angle = -90.0f;
	    }
	}
	angle = angle - 90.0f;
	r.drawTile(center, size, textures, color, angle, pivot);
    }
    
    public void setVelocity2D(float[] in) {
	velocity[0] = in[0];
	velocity[1] = in[1];
    }
}

