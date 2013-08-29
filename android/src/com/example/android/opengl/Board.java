package com.example.android.opengl;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Scanner;
import java.io.IOException;

import com.example.android.opengl.geometry.*;

class Board{

    private Square   BGSquare;
    private Tile[] puzzleTiles = new Tile[36];

    private int[] solution ;
    private int[][] path;
    
    public Board() {
	try {
	    readBoard("/sdcard/iotest.txt");	
	} catch (IOException e) {
	    System.err.println("Caught IOException: " + e.getMessage());
	}

	float h = .75f;
	float loc[]                = { -h,  h, 0.0f,   // top left
				       -h, -h, 0.0f,   // bottom left
				       h, -h, 0.0f,    // bottom right
				       h,  h, 0.0f };  // top right
	BGSquare   = new Square(loc, Colors.col);
	
	for (int i = 0; i < puzzleTiles.length; i++) {
	    float H = .11f;
	    float Sx = ( (i/6) - 2.5f )/4.0f;
	    float Sy = ( (i%6) - 2.5f )/4.0f;
	    
	    float center[]         = { -H + Sx,  H + Sy, 0.0f,    // top left
				       -H + Sx, -H + Sy, 0.0f,    // bottom left
				       H + Sx, -H + Sy, 0.0f,    // bottom right
				       H + Sx,  H + Sy, 0.0f };  // top right
	    
	    puzzleTiles[i] = new Tile(center, solution[i]);
	}
    }

    
    public void readBoard(String board_file) throws IOException{
	Scanner scanner = null;
	try {	    
            scanner = new Scanner(new BufferedReader(new FileReader(board_file)));
	    
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

    public void draw(float[] mvpMatrix) {
	BGSquare.draw(mvpMatrix);
	for (int i = 0; i < puzzleTiles.length; i++) {
	    puzzleTiles[i].draw(mvpMatrix);
	}
    }
}
