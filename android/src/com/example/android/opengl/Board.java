package com.example.android.opengl;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;
import java.io.IOException;

class Board{

	public Tile[] puzzleTiles = new Tile[36];
	public int[] solution ;
	public int[][] path;
	public int[] columnSums;
	public int[] rowSums;

	public Board() {

	try {
	    readBoard(stringFromJNI(6,6, 8) );	
	} catch (IOException e) {
	    System.err.println("Caught IOException: " + e.getMessage());
	}

	columnSums = new int[6];
	rowSums = new int[6];


	for (int i = 0; i < puzzleTiles.length; i++) {
	    float size = .11f;
	    float Sx = ( (i/6) - 2.5f )/4.0f;
	    float Sy = ( (i%6) - 2.5f )/4.0f;
	    columnSums[i%6] += Math.max(solution[i],0);
	    rowSums[i/6] += Math.max(solution[i],0);
	    float center[] = { Sx, Sy, 0.0f};
	    if (solution[i] == -1) {
		puzzleTiles[i] = new Tile(center, size, solution[i], 4);
	    }
	    else {
		puzzleTiles[i] = new Tile(center, size, solution[i], 4);
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
    	
    public float[] touched(float[] pt) {
    	for (int i = 0; i < puzzleTiles.length; i++) {
    	    if( puzzleTiles[i].touched(pt) ) {
		puzzleTiles[i].touched_flag = !puzzleTiles[i].touched_flag;
		puzzleTiles[i].texture = 4;
		pt[0] = puzzleTiles[i].center[0];
		pt[1] = puzzleTiles[i].center[1];
	    }
    	}
	return pt;
    }
    
    public void swiped(float[] pt, int direction) {
	for (int i = 0; i < puzzleTiles.length; i++) {
    	    if( puzzleTiles[i].touched(pt) ) {
		puzzleTiles[i].texture = direction;
	    }
    	}


	// if (direction == 0) {
	//     System.out.println("East");
	// }
	// if (direction == 1) {
	//     System.out.println("North");
	// }
	// if (direction == 2) {
	//     System.out.println("West");
	// }
	// if (direction == 3) {
	//     System.out.println("South");
	// }
    }


    static {
	System.loadLibrary("GeneratePuzzle");
    }
}
