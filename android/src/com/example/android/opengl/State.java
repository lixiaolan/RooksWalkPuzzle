package com.example.android.opengl;

/*
 * Philosophy of the state: State's have a period. A beginning, middle and end. The initial period
 *  of every State is ENTER. It is the job of enterAnimation to change the period to DURING. I am not sure how
 *  to deal with exit yet, as this should be triggered by a global state change. However this class should NOT 
 *  be exposed to global state changes. It might make sense to only allow entry and during animations. This needs to be
 *  thought about. Actually I think this might be forced upon us... Think more about this.
 */
public abstract class State<T extends Tile> {
    
    enum DrawPeriod {
	ENTER, DURING
    }
    
    DrawPeriod period = DrawPeriod.ENTER;
    
    public abstract void enterAnimation(T[] tiles);
    public abstract void duringAnimation(T[] tiles);
    
    
    public void draw(T[] tiles, MyGLRenderer r){
	switch(period) {
	case ENTER: enterAnimation(tiles); break;
	case DURING: duringAnimation(tiles); break;
	}
	
	for (int i = 0; i < tiles.length; i++) {
	    tiles[i].draw(r);
	}
    }    

    public int touched(float[] pt) { return 0;};
    
    public void touchHandler(Menu mMenu, float[] pt){};
    public void swipeHandler(String direction){};
    public void touchHandler() {};
}
