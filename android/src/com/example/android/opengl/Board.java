package com.example.android.opengl;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;
import java.io.IOException;

import android.os.Parcel;
import android.os.Parcelable;

class Board implements Parcelable {
    
    public BoardTile[] puzzleTiles = new BoardTile[36];
    public int[] solution ;
    public int[][] path;
    public int[] columnSums;
    public int[] rowSums;
    public int activeTile;
    
    public Board() {
	
	try {
	    readBoard(stringFromJNI(6,6, 16) );	
	} catch (IOException e) {
	    System.err.println("Caught IOException: " + e.getMessage());
	}
	
	buildBoardFromSolution();
	
    }
    
    
    
    public Board(Parcel in) {
    	in.readIntArray(solution);
    	buildBoardFromSolution();
    }
    
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeIntArray(this.solution);
     }
     
    /*
     * Format of solution array must match specs laid out in README.txt 
     */
    public void buildBoardFromSolution() {
    	columnSums = new int[6];
    	rowSums = new int[6];
    	
    	for (int i = 0; i < puzzleTiles.length; i++) {
    	    float size = .11f;
    	    //2.5, why? Also, somehow these constants should come down from model? Needs a restructuring.
    	    //Are they spiraling out?
    	    float Sx = ( (i/6) - 2.5f )/4.0f;
    	    float Sy = ( (i%6) - 2.5f )/4.0f;
    	    columnSums[i%6] += Math.max(solution[i],0);
    	    rowSums[i/6] += Math.max(solution[i],0);
    	    System.out.println("Column Sums "+Integer.toString(columnSums[i/6]));
    	    float center[] = { Sx, Sy, 0.0f};
    	    if (solution[i] == -1) {
    		puzzleTiles[i] = new BoardTile(center, size, solution[i]);
    	    }
    	    else {
    		puzzleTiles[i] = new BoardTile(center, size, solution[i]);
    	    }
    	    
    	}
    }
    
    public native String stringFromJNI(int rows, int cols, int length);
    
    public void readBoard(String puzzleString) throws IOException{
	Scanner scanner = null;
	try {	    
            scanner = new Scanner(puzzleString);
	    
	    //Get dimensions
	    int m = scanner.nextInt();
	    int n = scanner.nextInt();
	    System.out.println("m is "+Integer.toString(m));
	    solution = new int[m*n];
	    int a  = 0;
	    //Update all the solutions
	    for(int i = 0; i<m*n; i++){
	    	a = scanner.nextInt(); 
		solution[i] = a;
		System.out.println(Integer.toString(a));
	    }
	    
	    //Get path length
	    int l = scanner.nextInt();
	    path = new int[2*l][2];
	    
	    //Update path
	    for(int i = 0; i < l; i++){
	    	path[i][0] = scanner.nextInt();
	    	path[i][1] = scanner.nextInt();
		System.out.println("which l"+Integer.toString(i));
	    }
	    
        } finally {
	    scanner.close();	    
        }
    }
    
    public boolean touched(float[] pt) {
    	for (int i = 0; i < puzzleTiles.length; i++) {
    	    if( puzzleTiles[i].touched(pt) ) {
		puzzleTiles[i].touched_flag = !puzzleTiles[i].touched_flag;
		puzzleTiles[i].texture = 4;
		activeTile = i;
		return true;
	    }
    	}
	return false;
    }
    
    public void swiped(float[] pt, int direction) {
	for (int i = 0; i < puzzleTiles.length; i++) {
    	    if( puzzleTiles[i].touched(pt) ) {
		puzzleTiles[i].arrow = direction;
	    }
    	}
    }
    
    public void clearFlags() {
	for (int i = 0; i < puzzleTiles.length; i++) {
	    puzzleTiles[i].touched_flag = false;
    	}
    }
    
    static {
	System.loadLibrary("GeneratePuzzle");
    }

    public static final Parcelable.Creator<Board> CREATOR = new Parcelable.Creator<Board>() {
    	 
        @Override
        public Board createFromParcel(Parcel source) {
            return new Board(source); // RECREATE VENUE GIVEN SOURCE
        }
 
        @Override
        public Board[] newArray(int size) {
            return new Board[size]; // CREATING AN ARRAY OF VENUES
        }
 
    };

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
