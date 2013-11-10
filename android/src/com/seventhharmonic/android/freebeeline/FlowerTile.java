package com.seventhharmonic.android.freebeeline;

public class FlowerTile extends Tile {
        
    public float[] velocity = new float[2];
    
    public FlowerTile(float inCenter0, float inCenter1 ,float inSize) {
	super(inCenter0, inCenter1, inSize);
	color = "transparent";
	velocity[0] = 0.0f;
	velocity[1] = 0.0f;
    }

    public void draw(MyGLRenderer r) {
	r.drawTile(center, size, textures, color, angle, pivot,true);    
    } 
}
