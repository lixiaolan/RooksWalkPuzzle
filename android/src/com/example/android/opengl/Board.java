package com.example.android.opengl;
import java.util.Scanner;
import java.io.IOException;


import android.os.Parcel;
import android.os.Parcelable;

class Board extends Graphic<BoardTile> implements Parcelable {
    
    public int hints;
    public int[] solution ;
    public int[][] path;
    public int[] columnSums;
    public int[] rowSums;
    
    public Board() {
    	buildEmptyBoard();
    	state = new BoardMainMenu(tiles);
    }
    
    public Board(Parcel in) {
    	in.readIntArray(solution);
    	//NEED TO TRACK DIFFICULTY OF PUZZLE
    	//buildBoardFromSolution();
    	state = new BoardPlay(tiles);
    }
        
    public void createPuzzle(int length, int hints){
    	try {
    		readSolution(stringFromJNI(6, 6, length) );	
    	} catch (IOException e) {
    		System.err.println("Caught IOException: " + e.getMessage());
    	}

    	buildBoardFromSolution(hints);
    }

    public void buildEmptyBoard() {
    	tiles = new BoardTile[36];
    	float size = .12f;
    	for (int i = 0; i < tiles.length; i++) {
    	    float center[] = { 0, 0, 0.0f};    	   
    	    tiles[i] = new BoardTile(center, size);    	    
    	}
    }
    
    public void buildBoardFromSolution(int hints) {
    	columnSums = new int[6];
    	rowSums = new int[6];
    	int hintsCreated = 0;
	for (int i = 0; i < tiles.length; i++) {
    	    columnSums[i%6] += Math.max(solution[i],0);
    	    rowSums[i/6] += Math.max(solution[i],0);
	    System.out.println(Integer.toString(solution[i]));
	    //tiles[i].setTrueSolution(0);
	    tiles[i].setTrueSolution(solution[i]);
	}

    	int dx;
	int dy;
	for (int i = 0; i < path.length-1; i++) {
	    dx = path[i+1][0]-path[i][0];
	    dy = path[i+1][1]-path[i][1];

	    if (dx > 0) {
		tiles[6*path[i+1][0] + path[i+1][1]].true_direction = 0;
		//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "right_arrow";
	    }
	    if (dx < 0) {
		tiles[6*path[i+1][0] + path[i+1][1]].true_direction = 2;
		//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "left_arrow";
	    }
	    if (dy > 0) {
		tiles[6*path[i+1][0] + path[i+1][1]].true_direction = 3;
		//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "down_arrow";
	    }
	    if (dy < 0) {
		tiles[6*path[i+1][0] + path[i+1][1]].true_direction = 1;
		//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "up_arrow";
	    }
	}
    	
    	while(hintsCreated < hints) {
    		int r = (int)(36*Math.random());
    		if(tiles[r].isClickable() != false){
    			tiles[r].setHint();
    			hintsCreated +=1;
    		}
    	}
    	
    }
    
    public native String stringFromJNI(int rows, int cols, int length);
    
    public void readSolution(String puzzleString) throws IOException{
    	Scanner scanner = null;
    	try {	    
    		scanner = new Scanner(puzzleString);
    		int m = scanner.nextInt();
    		int n = scanner.nextInt();
    		solution = new int[m*n];
    		int a  = 0;
    		for(int i = 0; i<m*n; i++){
    			a = scanner.nextInt(); 
    			solution[i] = a;
    		}
    		int l = scanner.nextInt();
    		path = new int[2*l][2];
    		for(int i = 0; i < l; i++){
    			path[i][0] = scanner.nextInt();
    			path[i][1] = scanner.nextInt();
    		}
    	} finally {
    		scanner.close();	    
    	}
    }
    
    public int touched(float[] pt) {
    	for (int i = 0; i < tiles.length; i++) {
    	    if( tiles[i].touched(pt)) {
    	    	return i;
    	    }
    	}
    	return -1;
    }
    
    public void swiped(float[] pt, String direction) {
	for (int i = 0; i < tiles.length; i++) {
    	    if( tiles[i].touched(pt) ) {
    	    	tiles[i].arrow = direction;
    	    }
    	}
    }
    ;
    public void setRotate(int i, float[] pivot){
    	tiles[i].rotate = true;
    	tiles[i].pivot = pivot;
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
			case PLAY: state  = new BoardPlay(tiles); 
			break;
			case GAME_MENU: state  = new BoardPlay(tiles); break;
		}
	}
}


class BoardMainMenu extends State<BoardTile> {
			
	boolean[] rotateTiles = new boolean[36];
	long[] refTime = new long[36];
	
	public BoardMainMenu(BoardTile[] tiles) {
		for (int i = 0; i < tiles.length; i++) {
    	    double r = Math.random();
    	    float Sx = (float)(-1.5*r+(1-r)*1.5);
    	    r = Math.random();
    	    float Sy = (float)(-1.5*r+(1-r)*1.5);
    	    float center[] = { Sx, Sy, 0.0f};
    	    tiles[i].center = center;
    	}
		
		//Set textures	
		for(int i = 0;i<tiles.length;i++){
			tiles[i].setTextures("clear", "flower");
			tiles[i].setColor("transparent");
		}
		
	}
	
	public void enterAnimation(BoardTile[] tiles) {
		period = DrawPeriod.DURING;
	}
	
	public void duringAnimation(BoardTile[] tiles) {
		for(int i=0;i<tiles.length;i++){
			if(tiles[i].rotate){
				System.out.println("I need to rotate bad.");
				refTime[i] = System.currentTimeMillis();
				rotateTiles[i] = true;
				tiles[i].rotate = false;
			}
			long time = System.currentTimeMillis()-refTime[i];
			if(time < 2000f && rotateTiles[i]==true){
				System.out.println("ROTATE ME");
				tiles[i].angle = time*.4f;
				if(time<1000f) {
					tiles[i].textures[0] = Integer.toString((int)(10*Math.random()));
					tiles[i].textures[1] = "up_arrow";
				}
			} else{
				rotateTiles[i] = false;
				tiles[i].angle = 0;
				tiles[i].textures[0]="flower";
				tiles[i].textures[1] ="clear";
			}
			
		}
	}
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
				tiles[i].setColor("transparent");
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
	
	
}
