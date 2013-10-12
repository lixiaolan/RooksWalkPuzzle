package com.seventhharmonic.android.freebeeline;



class Border{

    public BorderTile[] rowTiles = new BorderTile[6];
    public BorderTile[] columnTiles = new BorderTile[6];
    public float[] tilesCenter = new float[3];
    public float tilesSize;
    
    
    public Border(int[] columnSums, int[] rowSums) {
    	tilesSize = 0.11f;
	String tString;
	for (int i = 0; i < 6; i++) {
	    float Sx = ( (i/6) - 2.5f )/4.0f-(-3.5f-2.5f)/4.0f;
	    float Sy = ( (i%6) - 2.5f )/4.0f;
	    float[] center1 = { Sx, Sy, 0.0f};
	    tString = "border_"+Integer.toString(columnSums[i]);
	    columnTiles[i] = new BorderTile(center1, tilesSize, tString);
	    
	    Sx = ( (i%6) - 2.5f )/4.0f;
	    Sy = (1.0f+2.5f)/4.0f;
	    float[] center2 = { Sx, Sy, 0.0f};
	    tString = "border_"+Integer.toString(rowSums[i]);
	    rowTiles[i] = new BorderTile(center2, tilesSize, tString);
	}
    }
    
    public void draw(MyGLRenderer r) {
    	
    		for (int i = 0; i < rowTiles.length; i++) {
    			columnTiles[i].draw(r);
    			rowTiles[i].draw(r);
    		}
    	
    }
}
