package com.example.android.opengl;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import android.os.SystemClock;

class Border{

    public Tile[] rowTiles = new Tile[6];
    public Tile[] columnTiles = new Tile[6];
    public float[] tilesCenter = new float[3];
    public float tilesSize;
    
    public Border(int[] columnSums, int[] rowSums) {

    	tilesSize = 0.11f;
	for (int i = 0; i < 6; i++) {
		float Sx = ( (i/6) - 2.5f )/4.0f-(-3.5f-2.5f)/4.0f;
		float Sy = ( (i%6) - 2.5f )/4.0f;
	    float[] center1 = { Sx, Sy, 0.0f};
	    columnTiles[i] = new Tile(center1, tilesSize, columnSums[i], 0);
	    
	    
	    Sx = ( (i%6) - 2.5f )/4.0f;
		Sy = (1.0f+2.5f)/4.0f;
		float[] center2 = { Sx, Sy, 0.0f};
	    rowTiles[i] = new Tile(center2, tilesSize, rowSums[i], 0);
	}
    }    
	
 
    
}
