package com.seventhharmonic.android.freebeeline;

public class ErrorBundle {
    
    public boolean pointsPast;
    public boolean pointedPast;
    public boolean pointsAtSame; //(points at same number)
    public boolean pointedAtBySame; //(pointed at by same number)
    public boolean pointsAtBadDir; //(right angle rule broken!)
    public boolean pointedAtByBadDir;
    public boolean pointsOffBoard;
    public boolean pointsAtMultiPointedTo; //
    public boolean multiPointedTo;

    public boolean hasError() {
	return ( pointsPast || pointedPast || pointsAtSame || pointedAtBySame || pointsAtBadDir || pointedAtByBadDir || pointsOffBoard || pointsAtMultiPointedTo || multiPointedTo );
    }
}
