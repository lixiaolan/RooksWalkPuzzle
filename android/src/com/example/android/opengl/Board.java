package com.example.android.opengl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;



class Board extends Graphic<BoardTile, State<BoardTile> > {
    
    public int hints;
    public int[] solution ;
    public int[][] path;
    public int[] columnSums;
    public int[] rowSums;
    private boolean toggleHints = true;
    private float flowerSize = .15f;
    private float tileSize = .11f;
    
    
    public Board() {
    	buildEmptyBoard();
    	state = new BoardMainMenu(tiles);
    }
    
    public void restoreBoard(int[] solution, String[] numbers, String[] arrows, String[] trueArrows, int[][] path, boolean[] clickable ){
	columnSums = new int[36];
	rowSums = new int[36];
	for(int i=0;i<36;i++){
	    tiles[i].setTrueSolution(solution[i]);
	    tiles[i].setArrow(arrows[i]);
	    tiles[i].setNumber(numbers[i]);
	    tiles[i].setTrueArrow(trueArrows[i]);
	    if(clickable!=null)
	    	if(!clickable[i])
	    		tiles[i].setHint();
	    columnSums[i%6] += Math.max(solution[i],0);
	    rowSums[i/6] += Math.max(solution[i],0);
	}
	this.path = path;
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
    
    public boolean[] dumpClickable() {
    	boolean[] clickable = new boolean[36];
    	for(int i =0; i<36; i++){
    	    clickable[i] = tiles[i].isClickable();
    	}
    	return clickable;
    }
    
    public int[][] dumpPath() {
    	return path;
    }
    
    public void buildEmptyBoard() {
	tiles = new BoardTile[36];
	float size = .15f;
	for (int i = 0; i < tiles.length; i++) {
	    double r = Math.random();
	    float Sx = (float)(-1.5*r+(1-r)*1.5);
	    r = Math.random();
	    float Sy = (float)(-1.5*r+(1-r)*1.5);
	    float center[] = { Sx, Sy, 0.0f};
	    tiles[i] = new BoardTile(center, size);    	    
	}
    }
    
    public void buildBoardFromSolution(int hints) {
	columnSums = new int[6];
	rowSums = new int[6];
	List<Integer> numbers = new ArrayList<Integer>();
	for (int i = 0; i < tiles.length; i++) {	
	    //This does a hard reset on the board.
	    tiles[i].number = TextureManager.CLEAR;
	    tiles[i].arrow = TextureManager.CLEAR;
	    tiles[i].setTrueSolution(0);
	    tiles[i].setTrueArrow(TextureManager.CLEAR);
	    if(solution [i] > 0){
	    	columnSums[i%6] += Math.max(solution[i],0);
			rowSums[i/6] += Math.max(solution[i],0);
			numbers.add(i);
			//tiles[i].setNumber(Integer.toString(solution[i]));
	    }
	    //Set the solution in the tile
	    tiles[i].setTrueSolution(solution[i]);
	} 
	int dx;
	int dy;
	for (int i = 0; i < path.length-1; i++) {
	    dx = path[i+1][0]-path[i][0];
	    dy = path[i+1][1]-path[i][1];
	    
	    if (dx > 0) {
		tiles[6*path[i+1][0] + path[i+1][1]].setTrueArrow("right_arrow");
		//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "right_arrow";
	    }
	    if (dx < 0) {
		tiles[6*path[i+1][0] + path[i+1][1]].setTrueArrow("left_arrow");
		//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "left_arrow";
	    }
	    if (dy > 0) {
		tiles[6*path[i+1][0] + path[i+1][1]].setTrueArrow("down_arrow");
		//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "down_arrow";
	    }
	    if (dy < 0) {
		tiles[6*path[i+1][0] + path[i+1][1]].setTrueArrow("up_arrow");
		//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "up_arrow";
	    }
	}
	//Note we are assuming that hints<numbers and that we have a unique solution
	if(toggleHints){
	    Collections.shuffle(numbers);
	    for(int i=0;i<hints;i++){
	    	int r = numbers.get(i);
			tiles[r].setHint();
	    }
	}
    }
    
    public void resetBoard() {
	for(int i=0;i<tiles.length;i++){
	    if(tiles[i].isClickable()){
	    	tiles[i].setNumber(TextureManager.CLEAR);
	    	tiles[i].setArrow(TextureManager.CLEAR);
	    }
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
	System.out.println(puzzleString);
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
	    if( tiles[i].touched(pt) && tiles[i].isClickable()) {
	    	return i;
	    }
	}
	return -1;   
    }
    
    public int[] getColumnSums(){
    	return columnSums;
    }
   
    public int[] getRowSums(){
    	return rowSums;
    }
    static {
	System.loadLibrary("GeneratePuzzle");
    }
    
    public void setState(GameState s){
    	switch(s) {
    		case MAIN_MENU_OPENING: state = new BoardMainMenu(tiles); break;
    		case GAME_OPENING: state  = new BoardPlay(tiles); break;
    	}
    }
    
    public boolean checkSolution(){
	for(int i =0; i < 36; i++){
	    if(!( tiles[i].checkArrows() && tiles[i].checkSolutions() ) ){ 
		System.out.println(Integer.toString(i));
		System.out.println(tiles[i].getArrow()+" "+tiles[i].getTrueArrow()+Boolean.toString(tiles[i].checkArrows()));
		System.out.println(tiles[i].getNumber()+" "+Integer.toString(tiles[i].getTrueSolution())+Boolean.toString(tiles[i].checkSolutions()));
		return false;
	    }
	}
	return true;
    }
    
    public void toggleHints(boolean toggle){
	toggleHints = toggle;
    }
    
    public void showSolution(){
    	String sol = "";
    	for(int i =0;i<tiles.length;i++){
    		if(tiles[i].getTrueSolution() == 0)
    			sol  = "clear";
    		else
    			sol = Integer.toString(tiles[i].getTrueSolution());
    		tiles[i].setNumber(sol);
    		tiles[i].setArrow(tiles[i].getTrueArrow());
    	}
    }

    public void showPath() {
    	//This will be a hard thing to actually accomplish. Leaving it on the side for now.
    	List<Integer> markedTiles = new ArrayList<Integer>();
    	for(int i=0;i< 36;i++){
    		if(tiles[i].hasNumber() && tiles[i].hasArrow()){
    			markedTiles.add(i);
    		}
    	}
    }
    
class BoardMainMenu extends State<BoardTile> {
    
    long refTime;

    float[] centers;
    
    public BoardMainMenu(BoardTile[] tiles) {
	centers = new float[2*tiles.length];
	
	for (int i = 0; i < tiles.length; i++) {
	    double r = Math.random();
	    tiles[i].velocity[0] = (float)(-1*r+(1-r)*1);
	    r = Math.random();
	    tiles[i].velocity[1] = (float)(-1*r+(1-r)*1);
	    
	}
	float[] pivot = {0.0f,0.0f,1.0f};
	//Set textures	
	for (int i = 0;i<tiles.length;i++){
	    tiles[i].setTextures(TextureManager.CLEAR, tiles[i].flowerTexture);
	    tiles[i].setColor("transparent");
	    tiles[i].setSize(flowerSize);
	    tiles[i].setAngle(0f);
	    tiles[i].setPivot(pivot);
	}
	
	for (int i = 0; i<tiles.length; i++ ) {
	    float ii = (float)i;
	    float r = (ii + 10*(1-1/(ii+1)))/25;
	    float t = ii/1.5f; 
	    centers[2*i] = -.75f + r*((float)Math.sin(t));
	    centers[2*i+1] = r*((float)Math.cos(t));
	}
	
    }
    
    public void enterAnimation(BoardTile[] tiles) {
	period = DrawPeriod.DURING;
	refTime = System.currentTimeMillis();
    }
    
    public void duringAnimation(BoardTile[] tiles) {
	long time = System.currentTimeMillis()-refTime;
	float dt =((float)time)/1000.0f;
	refTime = System.currentTimeMillis();
	float[] force = new float[2];
	for(int i=0;i<tiles.length;i++){
	    force = getForce(tiles, i);
	    tiles[i].setCenter2D(vSum(tiles[i].getCenter2D(), vSProd(dt, tiles[i].velocity)));
	    tiles[i].velocity = vSum(tiles[i].velocity, vSProd(dt, force));
	}
    }
    
    public float[] getForce(BoardTile[] tiles, int i) {
	float[] force = {0.0f, 0.0f};
	float[] mid = {centers[2*i],centers[2*i+1],0.0f};
	force = vSProd(-2.0f,vDiff(tiles[i].center, mid)); 
	force = vSum(force, vSProd(-1.2f,tiles[i].velocity));
	return force;
    }
    
    public float[] vDiff(float[] left, float[] right) {
	float[] ret = new float[2];
	ret[0] = left[0] - right[0];
	ret[1] = left[1] - right[1];
	return ret;
    }
    
    public float[] vSum(float[] left, float[] right) {
	float[] ret = new float[2];
	for (int i = 0; i < left.length; i++)
	    ret[i] = left[i] + right[i];
	return ret;
    }
    
    public float[] vSProd(float scalar, float[] vec) {
	float[] ret = new float[2];
	for (int i = 0; i < vec.length; i++)
	    ret[i] = vec[i]*scalar;
	return ret;
    }
    
    public float abs(float[] vec) {
	float ret = 0.0f;
	for (int i = 0; i < vec.length; i++)
	    ret += vec[i]*vec[i];
	ret = (float)Math.sqrt(ret);
	return ret;
    }
}

// class BoardMainMenu extends State<BoardTile> {
//     boolean[] rotateTiles = new boolean[36];
//     long[] refTime = new long[36];
//     public BoardMainMenu(BoardTile[] tiles) {
// 	for (int i = 0; i < tiles.length; i++) {
// 	    double r = Math.random();
// 	    float Sx = (float)(-1.5*r+(1-r)*1.5);
// 	    r = Math.random();
// 	    float Sy = (float)(-1.5*r+(1-r)*1.5);
// 	    float center[] = { Sx, Sy, 0.0f};
// 	    tiles[i].center = center;
// 	}
// 	//Set textures	
// 	for(int i = 0;i<tiles.length;i++){
// 	    tiles[i].setTextures(TextureManager.CLEAR, tiles[i].flowerTexture);
// 	    tiles[i].setColor("transparent");
// 	}
//     }
//     public void enterAnimation(BoardTile[] tiles) {
// 	period = DrawPeriod.DURING;
//     }
//     public void duringAnimation(BoardTile[] tiles) {
// 	for(int i=0;i<tiles.length;i++){
// 	    if(tiles[i].rotate){
// 		refTime[i] = System.currentTimeMillis();
// 		rotateTiles[i] = true;
// 		tiles[i].rotate = false;
// 	    }
// 	    long time = System.currentTimeMillis()-refTime[i];
// 	    if(time < 2000f && rotateTiles[i]==true){
// 		tiles[i].setAngle(time*.4f);
// 		if(time<1000f) {
// 		    tiles[i].textures[0] = Integer.toString((int)(10*Math.random()));
// 		    tiles[i].textures[1] = "up_arrow";
// 		}
// 	    } else{
// 		rotateTiles[i] = false;
// 		tiles[i].setAngle(0);
// 		tiles[i].setTextures(TextureManager.CLEAR, tiles[i].flowerTexture);
// 	    }
// 	}
//     }
// }


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
		tiles[i].setTextures(TextureManager.CLEAR, tiles[i].flowerTexture);
		float Sx = ( (i/6) - 2.5f )/4.0f;
		float Sy = ( (i%6) - 2.5f )/4.0f;
		tiles[i].setSize(.12f*time/totalTime+(1-time/totalTime)*.15f);
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
		tiles[i].setSize(.12f);
		//tiles[i].setColor("transparent");
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




}
