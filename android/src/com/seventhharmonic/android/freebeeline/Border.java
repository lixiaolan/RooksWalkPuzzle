package com.seventhharmonic.android.freebeeline;

//Width of a square is always fixed at .125! Maybe change this decision later.
//Size of a square is half an edge!
class Border extends Widget{
    

	int boardWidth;
    int boardHeight;
    int tileCount;
    float tileSize = .125f;
    BorderTile[] tiles;
    BorderTile[] north;
    BorderTile[] east;
    BorderTile[] west;   
    BorderTile[] south;
    BorderTile ne; BorderTile nw; BorderTile se; BorderTile sw;
    
    public Border(int boardWidth, int boardHeight) {
    	this.boardWidth = boardWidth;
    	this.boardHeight = boardHeight;
    	this.tileCount = boardWidth*boardHeight;
    	tiles = new BorderTile[tileCount];
    	south = new BorderTile[boardWidth];
    	north = new BorderTile[boardWidth];
    	east = new BorderTile[boardHeight];
    	west = new BorderTile[boardHeight];
    	initialize();
    }
    
    void initialize(){
    	for (int i = 0; i < tiles.length; i++) {
            float Sx = ( (i/boardWidth) - ((float)boardWidth-1.0f)/2 )/4.0f;
            float Sy = ( (i%boardHeight) - ((float)boardHeight-1.0f)/2 )/4.0f;
            System.out.println(Sx);
            System.out.println(Sy);
            float[] center = {Sx, Sy,0};
            tiles[i] = new BorderTile(center, tileSize);
            tiles[i].setTextures(TextureManager.BOX, TextureManager.CLEAR);
        }   
    	
    	for(int i = 0; i < boardHeight ; i++){
    		float Sx =  (- ((float)boardWidth-1.0f)/2 )/4.0f-.25f;
            float Sy = ( (i%boardHeight) - ((float)boardHeight-1.0f)/2 )/4.0f;
            float[] center1 = {Sx, Sy,0};
            east[i] = new BorderTile(center1, tileSize);
            east[i].setTextures(TextureManager.BORDERE, TextureManager.CLEAR);
            Sx = (boardWidth- ((float)boardWidth-1.0f)/2 )/4.0f;
            float[] center2 = {Sx, Sy,0};
            west[i] = new BorderTile(center2, tileSize);
            west[i].setTextures(TextureManager.BORDERW, TextureManager.CLEAR);
    	}
    	
    	for(int i = 0 ; i < boardWidth ; i++){
    		float Sx =  ( i - ((float)boardWidth-1.0f)/2 )/4.0f;
            float Sy = (boardHeight - ((float)boardHeight-1.0f)/2 )/4.0f;
            
            float[] center3 = {Sx, Sy,0};
            north[i] = new BorderTile(center3, tileSize);
            north[i].setTextures(TextureManager.BORDERN, TextureManager.CLEAR);
            
            Sy = (- ((float)boardHeight-1.0f)/2 )/4.0f-.25f;
            
            float[] center4 = {Sx, Sy,0};
            south[i] = new BorderTile(center4, tileSize);
            south[i].setTextures(TextureManager.BORDERS, TextureManager.CLEAR);
    	}
    	float Sx = (- ((float)boardWidth-1.0f)/2 )/4.0f-.25f;
    	float Sy = (boardHeight - ((float)boardHeight-1.0f)/2 )/4.0f;
    	ne = new BorderTile(Sx, Sy, tileSize);
    	ne.setTextures(TextureManager.BORDERNE, TextureManager.CLEAR);

    	sw = new BorderTile(-Sx,-Sy, tileSize );
    	sw.setTextures(TextureManager.BORDERSW, TextureManager.CLEAR);
    	
    	Sy = (- ((float)boardHeight-1.0f)/2 )/4.0f-.25f;
    	se = new BorderTile(Sx,Sy, tileSize);
    	se.setTextures(TextureManager.BORDERSE, TextureManager.CLEAR);
    	
    	nw = new BorderTile(-Sx,-Sy, tileSize);
    	nw.setTextures(TextureManager.BORDERNW, TextureManager.CLEAR);
    	
    	
    	
    	
    }
    
    public void draw(MyGLRenderer r) {
    	for (int i = 0; i < tiles.length; i++) {
    		tiles[i].draw(r);
    	}
    	for (int i = 0; i < boardHeight; i++) {
    		east[i].draw(r);
    		west[i].draw(r);
    	}
    	for (int i = 0; i < boardHeight; i++) {
    		north[i].draw(r);
    		south[i].draw(r);
    	}
    	
    	ne.draw(r);
    	sw.draw(r);
    }

}