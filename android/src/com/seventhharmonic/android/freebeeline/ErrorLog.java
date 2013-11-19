package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.android.freebeeline.graphics.TextureManager;

public class ErrorLog {
    
    public ErrorMap errorMap;
    public boolean pathNotClosed = false;
    public boolean spaceNotFilled = false;
    
    //Set this to check orthogonality or not:
    public boolean checkOrthogonality = false;
    
    public Board mBoard;
    
    public ErrorLog(Board b) {
	mBoard = b;
	setLog();
    }
    
    public void setLog() {
	errorMap = new ErrorMap();
	logErrors();
    }

    public boolean hasError() {
	setLog();
	ErrorBundle e;
	for (int i = 0; i < mBoard.tiles.length; i++ ) {
	    if (errorMap.containsKey(i)) {
		e = errorMap.get(i);
		if (e.hasError()) {
		    return true;
		}
	    }
	}
	return false;
    }

    public boolean hasError(int index) {
	setLog();
	ErrorBundle e;
	if (errorMap.containsKey(index)) {
	    e = errorMap.get(index);
	    if (e.hasError()) {
		return true;
	    }
	}
	return false;
    }

    
    public String getError(int index) {
	if (!errorMap.containsKey(index)) return "";
	
	ErrorBundle e = errorMap.get(index);
	
	if (e.pointsAtSame || e.pointedAtBySame) {
	    return TextureManager.MATCHINGNUMBERRULE;
	}
	else if (e.pointsPast || e.pointedPast) {
	    return TextureManager.PASSTHROUGHRULE;
	}
	else if (e.pointsAtBadDir || e.pointedAtByBadDir) {
	    return TextureManager.TURNINGRULE;
	} 
	else if (e.pointsOffBoard){
	    return TextureManager.OFFBOARD;
	}
	else if (e.pointsAtMultiPointedTo || e.multiPointedTo) {
	    return TextureManager.MULTIPOINTRULE;
	}
	else {
	    return TextureManager.CLEAR;
	}
    } 
    
    public void logErrors() {
	//Now go though each tile and fill in pointed at tiles.
	BoardTile t;
	int index;
	int num;
	for (int i = 0; i < mBoard.tiles.length; i++ ) {
	    if (mBoard.tiles[i].hasNumber() && mBoard.tiles[i].hasArrow()) {
		num = Integer.parseInt(mBoard.tiles[i].number);
		if (mBoard.tiles[i].getArrow().equals(TextureManager.UPARROW)) {
		    for (int j = 1; i%mBoard.boardHeight + j < mBoard.boardHeight; j++) {
			index = i+j;
			t = mBoard.tiles[index];
			//Check if matching number rule
			if (t.getNumber().equals(mBoard.tiles[i].getNumber())) {
			    errorMap.get(i).pointsAtSame = true;
			    errorMap.get(index).pointedAtBySame = true;
			}
			
			//Check for pass through rule
			if ( j < num ) {
			    if (t.isBlack() || !t.isBlank()) {
				errorMap.get(i).pointsPast = true;
				errorMap.get(index).pointedPast = true;
			    }
			}
			
			//Checking orthogonality explicitly
			else if(j == num) {
			    if (checkOrthogonality) {
				if (t.getArrow().equals(TextureManager.UPARROW) || t.getArrow().equals(TextureManager.DOWNARROW)) {
				    errorMap.get(i).pointsAtBadDir = true;
				    errorMap.get(index).pointedAtByBadDir = true;
				}
			    }
			    if (t.isBlack()) {
				errorMap.get(i).pointsPast = true;
				errorMap.get(index).pointedPast = true;
			    }
			    if (t.pointedToCount > 1) {
				errorMap.get(i).pointsAtMultiPointedTo = true;
				errorMap.get(index).multiPointedTo = true;
			    }
			    if (t.isPointedAt()) {
				errorMap.get(i).pointsPast = true;
				errorMap.get(index).pointedPast = true;
			    }
			    
			}
		    }
		    //Checking if we point off the board
		    if (i%mBoard.boardHeight + num >= mBoard.boardHeight) {
			errorMap.get(i).pointsOffBoard = true;
		    }
		}
		
		if (mBoard.tiles[i].getArrow().equals(TextureManager.DOWNARROW)) {
		    for (int j = 1; i%mBoard.boardHeight - j >= 0; j++) {
			index = i-j;
			t = mBoard.tiles[index];
			//Check if matching number rule
			if (t.getNumber().equals(mBoard.tiles[i].getNumber())) {
			    errorMap.get(i).pointsAtSame = true;
			    errorMap.get(index).pointedAtBySame = true;
			}
			
			//Check for pass through rule
			if ( j < num ) {
			    if (t.isBlack() || !t.isBlank()) {
				errorMap.get(i).pointsPast = true;
				errorMap.get(index).pointedPast = true;
			    }
			}
			
			//Checking orthogonality explicitly
			else if(j == num) {
			    if (checkOrthogonality) {
				if (t.getArrow().equals(TextureManager.UPARROW) || t.getArrow().equals(TextureManager.DOWNARROW)) {
				    errorMap.get(i).pointsAtBadDir = true;
				    errorMap.get(index).pointedAtByBadDir = true;
				}
			    }
			    if (t.isBlack()) {
				errorMap.get(i).pointsPast = true;
				errorMap.get(index).pointedPast = true;
			    }
			    if (t.pointedToCount > 1) {
				errorMap.get(i).pointsAtMultiPointedTo = true;
				errorMap.get(index).multiPointedTo = true;
			    }
			    if (t.isPointedAt()) {
				errorMap.get(i).pointsPast = true;
				errorMap.get(index).pointedPast = true;
			    }
			}
		    }
		    //Checking if we point off the board
		    if (i%mBoard.boardHeight - num < 0) {
			errorMap.get(i).pointsOffBoard = true;
		    }
		}
		
		if (mBoard.tiles[i].getArrow().equals(TextureManager.LEFTARROW)) {
		    for (int j = 1; i/mBoard.boardHeight + j < mBoard.boardWidth ; j++) {
			index = i+j*mBoard.boardHeight;
			t = mBoard.tiles[index];
			//Check if matching number rule
			if (t.getNumber().equals(mBoard.tiles[i].getNumber())) {
			    errorMap.get(i).pointsAtSame = true;
			    errorMap.get(index).pointedAtBySame = true;
			}
			
			//Check for pass through rule
			if ( j < num ) {
			    if (t.isBlack() || !t.isBlank()) {
				errorMap.get(i).pointsPast = true;
				errorMap.get(index).pointedPast = true;
			    }
			}
			
			//Checking orthogonality explicitly
			else if(j == num) {
			    if (checkOrthogonality) {
				if (t.getArrow().equals(TextureManager.LEFTARROW) || t.getArrow().equals(TextureManager.RIGHTARROW)) {
				    errorMap.get(i).pointsAtBadDir = true;
				    errorMap.get(index).pointedAtByBadDir = true;
				}
			    }
			    if (t.isBlack()) {
				errorMap.get(i).pointsPast = true;
				errorMap.get(index).pointedPast = true;
			    }
			    if (t.pointedToCount > 1) {
				errorMap.get(i).pointsAtMultiPointedTo = true;
				errorMap.get(index).multiPointedTo = true;
			    }
			    if (t.isPointedAt()) {
				errorMap.get(i).pointsPast = true;
				errorMap.get(index).pointedPast = true;
			    }

			}
		    }
		    //Checking if we point off the board
		    if (i/mBoard.boardHeight + num >= mBoard.boardWidth) {
			errorMap.get(i).pointsOffBoard = true;
		    }	
		}
		
		if (mBoard.tiles[i].getArrow().equals(TextureManager.RIGHTARROW)) {
		    for (int j = 1; i/mBoard.boardHeight - j >= 0; j++) {
			index = i-j*mBoard.boardHeight;
			t = mBoard.tiles[index];
			//Check if matching number rule
			if (t.getNumber().equals(mBoard.tiles[i].getNumber())) {
			    errorMap.get(i).pointsAtSame = true;
			    errorMap.get(index).pointedAtBySame = true;
			}
			
			//Check for pass through rule
			if ( j < num ) {
			    if (t.isBlack() || !t.isBlank()) {
				errorMap.get(i).pointsPast = true;
				errorMap.get(index).pointedPast = true;
			    }
			}
			
			//Checking orthogonality explicitly
			else if(j == num) {
			    if (checkOrthogonality) {
				if (t.getArrow().equals(TextureManager.LEFTARROW) || t.getArrow().equals(TextureManager.RIGHTARROW)) {
				    errorMap.get(i).pointsAtBadDir = true;
				    errorMap.get(index).pointedAtByBadDir = true;
				}
			    }
			    if (t.isBlack()) {
				errorMap.get(i).pointsPast = true;
				errorMap.get(index).pointedPast = true;
			    }
			    if (t.pointedToCount > 1) {
				errorMap.get(i).pointsAtMultiPointedTo = true;
				errorMap.get(index).multiPointedTo = true;
			    }
			    if (t.isPointedAt()) {
				errorMap.get(i).pointsPast = true;
				errorMap.get(index).pointedPast = true;
			    }

			}
		    }
		    //Checking if we point off the board
		    if (i/mBoard.boardHeight - num < 0) {
			errorMap.get(i).pointsOffBoard = true;
		    }
		}
	    }
	}
    }
}

