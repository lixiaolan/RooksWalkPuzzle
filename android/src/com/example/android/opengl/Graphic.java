package com.example.android.opengl;

public abstract class Graphic<T extends Tile, S extends State<T> > {
    T[] tiles;
    S state;
    
    public void draw(MyGLRenderer r){
    	state.draw(tiles,r);
    }
    
 /*   public int touched(float[] pt) {
    	return state.touched(pt);
    }
*/
    public abstract void setState(GameState s);
    
    public State<T> currState(){
	return state;
    }

    
}
