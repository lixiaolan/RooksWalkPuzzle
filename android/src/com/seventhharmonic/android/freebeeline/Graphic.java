package com.seventhharmonic.android.freebeeline;

public abstract class Graphic<T extends Tile, S extends State<T> > {
    T[] tiles;
    S state;
    float[] geometry;
    
    public void draw(MyGLRenderer r){
    	state.draw(tiles,r);
    }
    
    public abstract void setState(GameState s);
    
    public void setGeometry(float[] g){
    	geometry = g;
    }
    
    public State<T> currState(){
    	return state;
    }

    public void touchHandler(float[] pt) {}
    public void swipeHandler(String direction) {}
    
}
