package com.seventhharmonic.android.freebeeline;

public abstract class GraphicWidget extends Widget{
    
    StateWidget state;
    float[] geometry;
    
    public void draw(MyGLRenderer r){
    	state.draw(r);
    }
    
    public abstract void setState();
    
    public void setGeometry(float[] g){
    	geometry = g;
    }
    
    public StateWidget currState(){
    	return state;
    }

    public abstract void touchHandler(float[] pt);
    public abstract void swipeHandler(String direction);
    
}
