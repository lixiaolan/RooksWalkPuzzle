package com.example.android.opengl;

/*
 * Philosophy of the state: State's have a period. A beginning, middle and end. The initial period
 *  of every State is ENTER. It is the job of enterAnimation to change the period to DURING. I am not sure how
 *  to deal with exit yet, as this should be triggered by a global state change. However this class should NOT 
 *  be exposed to global state changes. It might make sense to only allow entry and during animations. This needs to be
 *  thought about. Actually I think this might be forced upon us... Think more about this.
 */
public abstract class BeeState<T extends Tile> extends State<T> {

    protected Mood mood;
    protected Board mBoard;

    protected void setMood(Mood m) {
    	mood = m;
    }

    protected void setBoard(Board b) {
    	mBoard = b;
    }

}
