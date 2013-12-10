package com.seventhharmonic.android.freebeeline;

public class ErrorBundle {
    
    public boolean pointsPast = false;
    public boolean pointedPast = false;
    public boolean pointsOnTo = false;
    public boolean pointsAtSame = false; //(points at same number)
    public boolean pointedAtBySame = false; //(pointed at by same number)
    public boolean pointsAtBadDir = false; //(right angle rule broken!)
    public boolean pointedAtByBadDir = false;
    public boolean pointsOffBoard = false;
    public boolean pointsAtMultiPointedTo = false; //
    public boolean multiPointedTo= false;
    public boolean sudokuRule= false;
    
    public boolean hasError() {
    	return (pointsOnTo || pointsPast || pointedPast || sudokuRule || pointsAtBadDir || pointedAtByBadDir || pointsOffBoard || pointsAtMultiPointedTo || multiPointedTo );
    	//return ( pointsPast || pointedPast || pointsAtSame || pointedAtBySame || pointsAtBadDir || pointedAtByBadDir || pointsOffBoard || pointsAtMultiPointedTo || multiPointedTo );
    }
}
