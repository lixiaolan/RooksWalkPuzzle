package com.example.android.opengl;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;
import java.io.IOException;

class Board{

    public Tile[] puzzleTiles = new Tile[36];
    public int[] solution ;
    public int[][] path;
    
    public Board() {


	try {
	    readBoard(stringFromJNI() );	
	} catch (IOException e) {
	    System.err.println("Caught IOException: " + e.getMessage());
	}


	for (int i = 0; i < puzzleTiles.length; i++) {
	    float size = .11f;
	    float Sx = ( (i/6) - 2.5f )/4.0f;
	    float Sy = ( (i%6) - 2.5f )/4.0f;
	    
	    float center[] = { Sx, Sy, 0.0f};
	    
	    puzzleTiles[i] = new Tile(center, size, solution[i], 0);
	}
    }

    public native String stringFromJNI();
    
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
    
	
    public void touched(float[] pt) {
    	for (int i = 0; i < puzzleTiles.length; i++) {
    	    puzzleTiles[i].touched(pt);
    	}
    }


    static {
	System.loadLibrary("GeneratePuzzle");
    }

}
