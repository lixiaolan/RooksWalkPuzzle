package com.example.android.opengl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
		buildBoardFromSolution(5);
		state = new BoardPlay(tiles);
	}

	public void restoreBoard(int[] solution, String[] numbers, String[] arrows, String[] trueArrows){
		columnSums = new int[36];
		rowSums = new int[36];
		for(int i=0;i<36;i++){
			tiles[i].setTrueSolution(solution[i]);
			tiles[i].setArrow(arrows[i]);
			tiles[i].setNumber(numbers[i]);
			tiles[i].setTrueArrow(trueArrows[i]);
			columnSums[i%6] += Math.max(solution[i],0);
			rowSums[i/6] += Math.max(solution[i],0);
		}
	}

	public int[] dumpSolution() {
		int[] solution = new int[36];
		for(int i =0;i<36;i++){
			solution[i] = tiles[i].getTrueSolution();
		}
		return solution;
	}

	public String[] dumpArrows() {
		String[] arrows = new String[36];
		for(int i =0;i<36;i++){
			arrows[i] = tiles[i].getArrow();
		}
		return arrows;
	}

	public String[] dumpNumbers() {
		String[] numbers = new String[36];
		for(int i =0; i<36; i++){
			numbers[i] = tiles[i].getNumber();
		}
		return numbers;
	}
	
	public String[] dumpTrueArrows() {
		String[] arrows = new String[36];
		for(int i =0; i<36; i++){
			arrows[i] = tiles[i].getTrueArrow();
		}
		return arrows;
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
		List<Integer> numbers = new ArrayList<Integer>();
		for (int i = 0; i < tiles.length; i++) {	
			tiles[i].number = "clear";
			tiles[i].arrow = "clear";
			tiles[i].setTrueSolution(0);
			tiles[i].setTrueArrow("clear");
			if(solution [i] > 0){
				columnSums[i%6] += Math.max(solution[i],0);
				rowSums[i/6] += Math.max(solution[i],0);
				numbers.add(i);
				tiles[i].setNumber(Integer.toString(solution[i]));
			}
			tiles[i].setTrueSolution(solution[i]);
			//This does a hard reset on the board.
			//tiles[i].number = "clear";
			//tiles[i].arrow = "clear";
		} 
		int dx;
		int dy;
		for (int i = 0; i < path.length-1; i++) {
			dx = path[i+1][0]-path[i][0];
			dy = path[i+1][1]-path[i][1];

			if (dx > 0) {
				tiles[6*path[i+1][0] + path[i+1][1]].setTrueArrow("right_arrow");
				tiles[6*path[i+1][0] + path[i+1][1]].arrow = "right_arrow";
			}
			if (dx < 0) {
				tiles[6*path[i+1][0] + path[i+1][1]].setTrueArrow("left_arrow");
				tiles[6*path[i+1][0] + path[i+1][1]].arrow = "left_arrow";
			}
			if (dy > 0) {
				tiles[6*path[i+1][0] + path[i+1][1]].setTrueArrow("down_arrow");
				tiles[6*path[i+1][0] + path[i+1][1]].arrow = "down_arrow";
			}
			if (dy < 0) {
				tiles[6*path[i+1][0] + path[i+1][1]].setTrueArrow("up_arrow");
				tiles[6*path[i+1][0] + path[i+1][1]].arrow = "up_arrow";
			}
		}
		Collections.shuffle(numbers);
		//Note we are assuming that hints<numbers
		for(int i=0;i<hints;i++){
			int r = numbers.get(i);
			tiles[r].setHint();
		}
	}
	
	public void createPuzzleFromJNI(int length, int hints){
		try {
			readSolutionFromJni(stringFromJNI(6, 6, length) );	
		} catch (IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());
		}
		buildBoardFromSolution(hints);
	}

	public native String stringFromJNI(int rows, int cols, int length);

	public void readSolutionFromJni(String puzzleString) throws IOException{
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
		case GAME_MENU: state  = new BoardPlay(tiles); break;
		}
	}

	public boolean checkSolution(){
		for(int i =0; i < 36; i++){
			if(!( tiles[i].checkArrows() && tiles[i].checkSolutions() ) ){ 
				System.out.println(Integer.toString(i));
				System.out.println(tiles[i].getArrow()+" "+tiles[i].getTrueArrow());
				System.out.println(tiles[i].getNumber()+" "+Integer.toString(tiles[i].getTrueSolution()));
				return false;
			}
		}
		return true;
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
				refTime[i] = System.currentTimeMillis();
				rotateTiles[i] = true;
				tiles[i].rotate = false;
			}
			long time = System.currentTimeMillis()-refTime[i];
			if(time < 2000f && rotateTiles[i]==true){
				tiles[i].setAngle(time*.4f);
				if(time<1000f) {
					tiles[i].textures[0] = Integer.toString((int)(10*Math.random()));
					tiles[i].textures[1] = "up_arrow";
				}
			} else{
				rotateTiles[i] = false;
				tiles[i].setAngle(0);
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
				tiles[i].setTextures("flower","clear");
				float Sx = ( (i/6) - 2.5f )/4.0f;
				float Sy = ( (i%6) - 2.5f )/4.0f;
				float newX = originalTiles[i].center[0]+time/totalTime*(Sx - originalTiles[i].center[0]);
				float newY = originalTiles[i].center[1]+time/totalTime*(Sy - originalTiles[i].center[1]);
				float center[] = { newX, newY, 0.0f};
				tiles[i].center = center;
			}			
		} 
		else if( time < 4000.0f && time > totalTime) {
			float[] pivot = {1,0,0};
			for (int i = 0; i < tiles.length; i++) {
				tiles[i].setPivot(pivot);
				tiles[i].setAngle((time-totalTime)*.045f);
			}
		}	
		else {
			for(int i = 0;i<tiles.length;i++){
				tiles[i].setAngle(0);
				///tiles[i].setTextures();
				tiles[i].setColor("transparent");
			}
			period = DrawPeriod.DURING;
		}
	}

	public void duringAnimation(BoardTile[] tiles) {
		for (int i = 0; i < tiles.length; i++) {
			((BoardTile)tiles[i]).setTextures();
		}
	}
}


