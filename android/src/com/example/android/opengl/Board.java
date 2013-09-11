package com.example.android.opengl;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;
import java.io.IOException;

import com.example.android.opengl.State.DrawPeriod;

import android.os.Parcel;
import android.os.Parcelable;

class Board extends Graphic<BoardTile> implements Parcelable {
    
    public int[] solution ;
    public int[][] path;
    public int[] columnSums;
    public int[] rowSums;
    public int activeTile;
    
    public Board() {

    	try {
    		readBoard(stringFromJNI(6, 6, 4) );	
    	} catch (IOException e) {
    		System.err.println("Caught IOException: " + e.getMessage());
    	}
    	
    	buildBoardFromSolution();
    	state = new BoardMainMenu(tiles);
	activeTile = -1;
    }
    
    public Board(Parcel in) {
    	in.readIntArray(solution);
    	buildBoardFromSolution();
    	state = new BoardMainMenu(tiles);
    }
        
    public void buildBoardFromSolution() {
    	tiles = new BoardTile[36];
    	columnSums = new int[6];
    	rowSums = new int[6];
    	
    	for (int i = 0; i < tiles.length; i++) {
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
    		tiles[i] = new BoardTile(center, size, solution[i]);
    	    }
    	    else {
    	    tiles[i] = new BoardTile(center, size, solution[i]);
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
    	for (int i = 0; i < tiles.length; i++) {
    	    if( tiles[i].touched(pt) ) {
		//Can't touch black squares (can't touch this! duuuun dun dun dun...)
		if ( tiles[i].true_solution != -1) {
		    tiles[i].touched_flag = true;
		    activeTile = i;
		    return true;
		}
	    }
    	}
	return false;
    }
    
    public void swiped(float[] pt, String direction) {
	for (int i = 0; i < tiles.length; i++) {
    	    if( tiles[i].touched(pt) ) {
		tiles[i].arrow = direction;
	    }
    	}
    }
    
    public void clearFlags() {
	for (int i = 0; i < tiles.length; i++) {
	    tiles[i].touched_flag = false;
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

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeIntArray(this.solution);
	}
	
	public void setState(GameState s){
		switch(s) {
			case MAIN_MENU: state = new BoardMainMenu(tiles); break;
			case PLAY: state  = new BoardPlay(tiles); break;
		}
	}
}


class BoardMainMenu extends State<BoardTile> {
			
	
	public BoardMainMenu(BoardTile[] tiles) {
		for (int i = 0; i < tiles.length; i++) {
    	    double r = Math.random();
    	    float Sx = (float)(-2*r+(1-r)*2);
    	    r = Math.random();
    	    float Sy = (float)(-2*r+(1-r)*2);
    	    float center[] = { Sx, Sy, 0.0f};
    	    tiles[i].center = center;
    	}
		
		//Set textures
		
		for(int i = 0;i<tiles.length;i++){
			tiles[i].setTextures("clear", "flower");
			tiles[i].setColor("transparent");
		}
		
	}
	
	public void enterAnimation(BoardTile[] tiles) {}
	
	public void duringAnimation(BoardTile[] tiles) {}
	
	public void exitAnimation(BoardTile[] tiles) {}
	
}


class BoardPlay extends State<BoardTile> {
	
	public Tile[] originalTiles;
	public long refTime;
	
	public BoardPlay(BoardTile[] tiles) {
		originalTiles = tiles;
		refTime = System.currentTimeMillis();
	}
			
	public void enterAnimation(BoardTile[] tiles) {
		long time = System.currentTimeMillis() - refTime;
		float totalTime = 2000f;
		
		if(time < totalTime) {
			for (int i = 0; i < tiles.length; i++) {
	    	    float Sx = ( (i/6) - 2.5f )/4.0f;
	    	    float Sy = ( (i%6) - 2.5f )/4.0f;
	    	    float newX = originalTiles[i].center[0]+time/totalTime*(Sx - originalTiles[i].center[0]);
	    	    float newY = originalTiles[i].center[1]+time/totalTime*(Sy - originalTiles[i].center[1]);
	    	    float center[] = { newX, newY, 0.0f};
	    	    tiles[i].center = center;
	    	}
			
		} 
		else if(time<1000.0f) {
			for (int i = 0; i < tiles.length; i++) {
				tiles[i].angle = (time-totalTime)*2;
			}
		}		
		else {
			for(int i = 0;i<tiles.length;i++){
				tiles[i].angle = 0;
				tiles[i].setTextures();
				tiles[i].setColor();
			}
			period = DrawPeriod.DURING;
		}
	}
	
	public void duringAnimation(BoardTile[] tiles) {
		for (int i = 0; i < tiles.length; i++) {
			((BoardTile)tiles[i]).setTextures();
    	    float Sx = ( (i/6) - 2.5f )/4.0f;
    	    float Sy = ( (i%6) - 2.5f )/4.0f;
    	    float center[] = { Sx, Sy, 0.0f};
    	    tiles[i].center = center;
    	}
	}
	
	public void exitAnimation(BoardTile[] tiles) {
		
	}
	
}
