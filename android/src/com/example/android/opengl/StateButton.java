package com.example.android.opengl;
import android.widget.Button;

class StateButton {
    public Button mButton;
    public float[] position = new float[2];
    public float size;
    public boolean visible;

    //For animation purposes
    public boolean moved;
    public boolean fadeIn;
    public boolean fadeOut;


    public StateButton(android.widget.Button b) {
	mButton = b;
	position[0] = -1000.0f;
	position[1] = 0.0f;
	size = 1.0f;
	visible = false;
	moved = true;
	fadeOut = true;
	fadeIn = false;
    }

    // public StateButton(android.widget.Button b, boolean viz) {
    // 	mButton = b;
    // 	position[0] = -1000.0f;
    // 	position[1] = 0.0f;
    // 	size = 1.0f;
    // 	visible = viz;
    // }

    public void setState(float x, float y, float siz, boolean vis) {
	if ((x == position[0])&(y == position[1]))
	    moved = false;
	else
	    moved = true;

	if ((visible == true)&(vis == false)){
	    fadeOut = true;
	    System.out.println("SET FADE OUT");
	}
	else
	    fadeOut = false;

	if ((visible == false)&(vis == true))
	    fadeIn = true;
	else 
	    fadeIn = false;

    	size = siz;
	visible = vis;

	if (visible) {
	    position[0] = x;
	    position[1] = y;
	}
	else {
	    position[0] = -1000.0f;
	    position[1] = -1000.0f;
	}

    }
}

