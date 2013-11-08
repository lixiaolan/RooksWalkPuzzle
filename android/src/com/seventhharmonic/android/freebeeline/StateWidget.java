package com.seventhharmonic.android.freebeeline;

/*
 * Philosophy of the state: State's have a period. A beginning, middle and end. The initial period
 *  of every State is ENTER. It is the job of enterAnimation to change the period to DURING. I am not sure how
 *  to deal with exit yet, as this should be triggered by a global state change. However this class should NOT 
 *  be exposed to global state changes. It might make sense to only allow entry and during animations. This needs to be
 *  thought about. Actually I think this might be forced upon us... Think more about this.
 */
public abstract class StateWidget {
    
    enum DrawPeriod {
    	ENTER, DURING
    }
    
    DrawPeriod period = DrawPeriod.ENTER;
    
    public abstract void enterAnimation();
    public abstract void duringAnimation();
    
    public void reset() {
	return;
    }
    
    public void draw(MyGLRenderer r){
    	switch(period) {
    	case ENTER: enterAnimation(); break;
    	case DURING: duringAnimation(); break;
    	}
    }    
  
    public abstract void swipeHandler(String direction);
    public abstract void touchHandler(float[] pt);
    
}
