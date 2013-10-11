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
    public int boardWidth = 6;
    public int boardHeight = 6;
    private boolean toggleHints = true;
    private boolean toggleLines = true;	
    protected float flowerSize = .15f;
    protected float tileSize = .11f;    
    private long lastTouchTime;
    private float[] lastTouchPos = new float[2];
    
    private Background mBoardBg;
    public Banner mGameBanner;
    public Board() {
	buildEmptyBoard();
	state = new BoardMainMenu(tiles);
	mBoardBg = new Background("boardbg", .75f);
	mGameBanner = new Banner(.8f);
	
    }
    
    public void restoreBoard(int[] solution, String[] numbers, String[] arrows, String[] trueArrows, int[][] path, boolean[] clickable ){
	for(int i=0;i<36;i++){
	    tiles[i].setTrueSolution(solution[i]);
	    tiles[i].setArrow(arrows[i]);
			tiles[i].setNumber(numbers[i]);
			tiles[i].setTrueArrow(trueArrows[i]);
			if(clickable!=null)
			    if(!clickable[i])
				tiles[i].setHint();
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
	List<Integer> numbers = new ArrayList<Integer>();
	for (int i = 0; i < tiles.length; i++) {	
	    //This does a hard reset on the board.
	    tiles[i].number = TextureManager.CLEAR;
	    tiles[i].arrow = TextureManager.CLEAR;
	    tiles[i].setTrueSolution(0);
	    tiles[i].setTrueArrow(TextureManager.CLEAR);
	    if(solution [i] > 0){
		numbers.add(i);
		//tiles[i].setNumber(Integer.toString(solution[i]));
	    }
	    //Set the solution in the tile
	    tiles[i].setTrueSolution(solution[i]);
	} 
	int dx;
	int dy;
	int length = (path.length)/2;
	for (int i = 0; i < length-1; i++) {
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
		clearLines();
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
    
    static {
	System.loadLibrary("GeneratePuzzle");
    }
    
    public int touched(float[] pt) {
	lastTouchPos = pt;
	lastTouchTime = System.currentTimeMillis();
	for (int i = 0; i < tiles.length; i++) {
	    if( tiles[i].touched(pt) && tiles[i].isClickable()) {
		return i;
	    }
	}
	return -1;   
    }
    
    public void touchHandler(float[] pt){
	state.touchHandler(pt);
    }
    
    public void swipeHandler(String direction) {
	state.swipeHandler(direction);
    }
    
    public void setState(GameState s){
	switch(s) {
	    
	case MAIN_MENU_OPENING: state = new BoardMainMenu(tiles); break;
	case GAME_OPENING: state  = new BoardPlay(tiles); break;
	case GAME_MENU_END: state = new BoardGameEnd(tiles); break;
	default: break;
	}
    }
    
    public boolean checkSolution(){
	for(int i =0; i < 36; i++){
	    if(!( tiles[i].checkArrows() && tiles[i].checkSolutions() ) ){ 
		return false;
	    }
	}
	return true;
    }
    
    public void toggleLines(boolean toggle){
    	toggleLines = toggle;
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
    
    public void setGeometry(float[] g) {
		super.setGeometry(g);
		float top = mGameBanner.getSize();
		mGameBanner.setCenter(0, g[1]-top);
	}
    
    public void animatePlay(int at) {
	//Now go though each tile and fill in pointed at tiles.
	float duration = .3f;
	float delay = .15f;
	clearLines();
	if (tiles[at].hasNumber() && tiles[at].hasArrow()) {
	    int num = Integer.parseInt(tiles[at].number);
	    if (tiles[at].getArrow() == TextureManager.UPARROW) {
		for (int j = 1; j < num; j++) {
		    if (at%boardHeight + j < boardHeight) {
			if (!tiles[at+j].isBlack() && tiles[at+j].isBlank()) {
			    String[] s1 = new String[2];
			    s1[0] = TextureManager.VERTDOTS;
			    s1[1] = tiles[at+j].textures[1];
			    float[] pivot = {1.0f,0.0f,0.0f};
			    tiles[at+j].setFlipper(geometry[1], pivot, duration, j*delay ,s1);
			}
			else {
			    tiles[at+j].setAngryGlow(duration, j*delay);
			}
		    }
		}
	    }
	    if (tiles[at].getArrow() == TextureManager.DOWNARROW) {
		for (int j = 1; j < num; j++) {
		    if (at%boardHeight - j >= 0) {
			if (!tiles[at-j].isBlack() && tiles[at-j].isBlank()) { 
			    String[] s2 = new String[2];
			    s2[0] = TextureManager.VERTDOTS;
			    s2[1] = tiles[at-j].textures[1];
			    float[] pivot = {1.0f,0.0f,0.0f};
			    tiles[at-j].setFlipper(geometry[1], pivot, duration, j*delay ,s2);
			}
			else {
			    tiles[at-j].setAngryGlow(duration, j*delay);
			}
		    }
		}
	    }
	    if (tiles[at].getArrow() == TextureManager.LEFTARROW) {
		for (int j = 1; j < num; j++) {
		    if (at/boardHeight + j < boardWidth) { 
			if (!tiles[at+j*boardHeight].isBlack() && tiles[at+j*boardHeight].isBlank()) {
			    String[] s3 = new String[2];
			    s3[1] = TextureManager.HORZDOTS;
			    s3[0] = tiles[at+j*boardHeight].textures[0];
			    float[] pivot = {0.0f,1.0f,0.0f};
			    tiles[at+j*boardHeight].setFlipper(geometry[1], pivot, duration, j*delay ,s3);
			}
			else {
			    tiles[at+j*boardHeight].setAngryGlow(duration, j*delay);
			}
		    }
		}
	    }
	    if (tiles[at].getArrow() == TextureManager.RIGHTARROW) {
		for (int j = 1; j < num; j++) {
		    if (at/boardHeight - j >= 0) {
			if (!tiles[at-j*boardHeight].isBlack() && tiles[at-j*boardHeight].isBlank()) {
			    String[] s4 = new String[2];
			    s4[1] = TextureManager.HORZDOTS;
			    s4[0] = tiles[at-j*boardHeight].textures[0];
			    float[] pivot = {0.0f,1.0f,0.0f};
			    tiles[at-j*boardHeight].setFlipper(geometry[1], pivot, duration, j*delay ,s4);
			}
			else {
			    tiles[at-j*boardHeight].setAngryGlow(duration, j*delay);
			}
		    }
		}
	    }
	}
	else {
	    drawLines();
	}
    }
    // Class to draw the dotted lines on the board.  Called after every input
    public void drawLines() {
	//First remove the lines on tiles which are no longer pointed at.
	markPointedAt();
	for (int i = 0; i < tiles.length; i++ ) {
	    if (!tiles[i].hasNumber() && !tiles[i].hasArrow() && !tiles[i].isBlack()) {
		if (!tiles[i].vPointedAt) {
		    tiles[i].setTextures(TextureManager.CLEAR, tiles[i].textures[1]);
		}
		else {
		    tiles[i].setTextures(TextureManager.VERTDOTS, tiles[i].textures[1]);
		}
		if (!tiles[i].hPointedAt) {
		    tiles[i].setTextures(tiles[i].textures[0], TextureManager.CLEAR);
		}
		else {
		    tiles[i].setTextures(tiles[i].textures[0], TextureManager.HORZDOTS);
		}
	    }
	}	
    }
    
    public void clearLines() {
	//First remove the lines on tiles which are no longer pointed at.
	markPointedAt();
	for (int i = 0; i < tiles.length; i++ ) {
	    if (!tiles[i].hasNumber() && !tiles[i].hasArrow() && !tiles[i].isBlack()) {
		if (!tiles[i].vPointedAt) {
		    tiles[i].setTextures(TextureManager.CLEAR, tiles[i].textures[1]);
		}
		if (!tiles[i].hPointedAt) {
		    tiles[i].setTextures(tiles[i].textures[0], TextureManager.CLEAR);
		}
	    }
	}	
    }
    
    public void markPointedAt() {
	//Marks all tiles pointed at either V or H
	for (int i = 0; i < tiles.length; i++) {
	    tiles[i].vPointedAt = false;
	    tiles[i].hPointedAt = false;
	}
	for (int i = 0; i < tiles.length; i++ ) {
	    if (tiles[i].hasNumber() && tiles[i].hasArrow()) {
		int num = Integer.parseInt(tiles[i].number);
		if (tiles[i].getArrow() == TextureManager.UPARROW) {
		    for (int j = 1; j < num; j++) {
			if (i%boardHeight + j < boardHeight) {
			    if (!tiles[i+j].isBlack() && tiles[i+j].isBlank()) 
				tiles[i+j].vPointedAt = true;
			}
		    }
		}
		if (tiles[i].getArrow() == TextureManager.DOWNARROW) {
		    for (int j = 1; j < num; j++) {
			if (i%boardHeight - j >= 0) {
			    if (!tiles[i-j].isBlack() && tiles[i-j].isBlank()) 
				tiles[i-j].vPointedAt = true;
			}
		    }
		}
		if (tiles[i].getArrow() == TextureManager.LEFTARROW) {
		    for (int j = 1; j < num; j++) {
			if (i/boardHeight + j < boardWidth) {
			    if (!tiles[i+j*boardHeight].isBlack() && tiles[i+j*boardHeight].isBlank()) 
				tiles[i+j*boardHeight].hPointedAt = true;
			}
		    }
		}
		if (tiles[i].getArrow() == TextureManager.RIGHTARROW) {
		    for (int j = 1; j < num; j++) {
			if (i/boardHeight - j >= 0) {
			    if (!tiles[i-j*boardHeight].isBlack() && tiles[i-j*boardHeight].isBlank()) 
				tiles[i-j*boardHeight].hPointedAt = true;
			}
		    }
		}
	    }
	}
    }
    
    public boolean checkRuleCollision(int at) {
	//Now go though each tile and fill in pointed at tiles.
	float duration = .3f;
	float delay = 0.0f;
	boolean satisfied = true;
	if (tiles[at].hasNumber() && tiles[at].hasArrow()) {
	    int num = Integer.parseInt(tiles[at].number);
	    if (tiles[at].getArrow() == TextureManager.UPARROW) {
		for (int j = 1; j < num; j++) {
		    if (at%boardHeight + j < boardHeight) {
			if (!tiles[at+j].isBlack() && tiles[at+j].isBlank()) {
			}
			else {
			    tiles[at+j].setAngryGlow(duration, j*delay);
			    satisfied = false;
			}
		    }
		}
	    }
	    if (tiles[at].getArrow() == TextureManager.DOWNARROW) {
		for (int j = 1; j < num; j++) {
		    if (at%boardHeight - j >= 0) {
			if (!tiles[at-j].isBlack() && tiles[at-j].isBlank()) { 
			}
			else {
			    tiles[at-j].setAngryGlow(duration, j*delay);
			    satisfied = false;
			}
		}
		}
	    }
	    if (tiles[at].getArrow() == TextureManager.LEFTARROW) {
		for (int j = 1; j < num; j++) {
		    if (at/boardHeight + j < boardWidth) { 
			if (!tiles[at+j*boardHeight].isBlack() && tiles[at+j*boardHeight].isBlank()) {
			}
			else {
			    tiles[at+j*boardHeight].setAngryGlow(duration, j*delay);
			    satisfied = false;
			}
		    }
		}
	    }
	    if (tiles[at].getArrow() == TextureManager.RIGHTARROW) {
		for (int j = 1; j < num; j++) {
		    if (at/boardHeight - j >= 0) {
			if (!tiles[at-j*boardHeight].isBlack() && tiles[at-j*boardHeight].isBlank()) {
			}
			else {
			    tiles[at-j*boardHeight].setAngryGlow(duration, j*delay);
			    satisfied = false;
			}
		    }
		}
	    }
		}
	    return false;
    }
    
    public boolean checkRuleCollision() {
	//Now go though each tile and fill in pointed at tiles.
	float duration = .3f;
	float delay = 0.0f;
	boolean satisfied = true;
	for (int i = 1; i < tiles.length; i++ ) {
	    if (tiles[i].hasNumber() && tiles[i].hasArrow()) {
		int num = Integer.parseInt(tiles[i].number);
		if (tiles[i].getArrow() == TextureManager.UPARROW) {
		    for (int j = 1; j < num; j++) {
			if (i%boardHeight + j < boardHeight) {
			    if (!tiles[i+j].isBlack() && tiles[i+j].isBlank()) {
			    }
			    else {
				tiles[i+j].setAngryGlow(duration, j*delay);
				satisfied = false;
			    }
			}
		    }
		}
		if (tiles[i].getArrow() == TextureManager.DOWNARROW) {
		    for (int j = 1; j < num; j++) {
			if (i%boardHeight - j >= 0) {
			    if (!tiles[i-j].isBlack() && tiles[i-j].isBlank()) { 
			    }
			    else {
				tiles[i-j].setAngryGlow(duration, j*delay);
				satisfied = false;
			    }
			}
		    }
		}
		if (tiles[i].getArrow() == TextureManager.LEFTARROW) {
		    for (int j = 1; j < num; j++) {
			if (i/boardHeight + j < boardWidth) { 
			    if (!tiles[i+j*boardHeight].isBlack() && tiles[i+j*boardHeight].isBlank()) {
			    }
			    else {
				tiles[i+j*boardHeight].setAngryGlow(duration, j*delay);
				satisfied = false;
			    }
			}
		    }
		}
		if (tiles[i].getArrow() == TextureManager.RIGHTARROW) {
		    for (int j = 1; j < num; j++) {
			if (i/boardHeight - j >= 0) {
			    if (!tiles[i-j*boardHeight].isBlack() && tiles[i-j*boardHeight].isBlank()) {
			    }
			    else {
				tiles[i-j*boardHeight].setAngryGlow(duration, j*delay);
				satisfied = false;
			    }
			}
		    }
		}
	    }
	}
	return false;
    }
    
    public boolean checkRuleRightAngles(int at) {
	return true;
    }
    
    public boolean checkRuleRightAngles() {
	return true;
    }    
    
    public boolean checkRulePoint(int at) {
	return true;
    }
    
    public boolean checkRulePoint() {
    	return true;
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
	    float[] temp = new float[2];
	    float[] mid = {centers[2*i],centers[2*i+1],0.0f};
	    force = vSProd(-2.0f,vDiff(tiles[i].center, mid)); 
	    force = vSum(force, vSProd(-1.2f,tiles[i].velocity));
	    //Compute wave of forces due to touch
	    float time = 2*(System.currentTimeMillis()-lastTouchTime)/1000f;
	    temp = vDiff(tiles[i].center, lastTouchPos);
	    float sTemp = abs(temp);
	    if (sTemp<time && sTemp > time - .2f && sTemp > .00001f)
		force = vSum(force, vSProd(5f/((float)Math.pow(sTemp,1)), temp));
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
    //This state defines board behavior during game play.
    class BoardPlay extends State<BoardTile> {
	
	public Menu mMenu;
	public Tile[] originalTiles;
	public long refTime;
	public boolean swap = false;
	public float initSize;
	public float finalSize = .12f;
	public float[] oldX;
	public float[] oldY;
	int at = -1;
	
	
	public BoardPlay(BoardTile[] tiles) {
	    originalTiles = tiles;
	    refTime = System.currentTimeMillis();
	    oldX = new float[tiles.length];
	    oldY = new float[tiles.length];
	    mMenu = new Menu();
	    for (int i = 0; i < tiles.length; i++) {
		tiles[i].rotate = false;
		tiles[i].setTextures(TextureManager.CLEAR, tiles[i].flowerTexture);
		oldX[i] = tiles[i].center[0];
		oldY[i] = tiles[i].center[1];
		tiles[i].setColor("transparent");
		tiles[i].vPointedAt = false;
		tiles[i].hPointedAt = false;
		tiles[i].isHint = false;
	    }
	    mGameBanner.set(TextureManager.CLEAR);
	    initSize = tiles[0].getSize();
	}
	
	public void enterAnimation(BoardTile[] tiles) {
	    long time = System.currentTimeMillis() - refTime;
	    float totalTime = 2000f;
	    // This does a simultaneous snap to position and shrink of tiles.
	    if(time < totalTime/3) {
		for (int i = 0; i < tiles.length; i++) {
		    float Sx = ( (i/6) - 2.5f )/4.0f;
		    float Sy = ( (i%6) - 2.5f )/4.0f;
		    float newX = Sx + ((float)Math.pow(.5,3*time/totalTime*10f))*(oldX[i]-Sx);
		    float newY = Sy + ((float)Math.pow(.5,3*time/totalTime*10f))*(oldY[i]-Sy);
		    float center[] = {newX, newY, 0.0f};
		    tiles[i].center = center;
		}			
	    }
	    //Tiles shrink to zero then blow up with the
	    //the correct game tiles.
	    else if( time < 2*totalTime/3 && time > totalTime/3) {
		for (int i = 0; i < tiles.length; i++) {
		    tiles[i].setSize(initSize*(2-3*time/totalTime ) );
		}
	    }
	    else if( time < totalTime && time > 2*totalTime/3) {
		float[] pivot = {1,0,0};
		if (!swap) {
		    for (int i = 0; i < tiles.length; i++) {
			tiles[i].setTextures();
			float Sx = ( (i/6) - 2.5f )/4.0f;
			float Sy = ( (i%6) - 2.5f )/4.0f;
			float center[] = {Sx, Sy, 0.0f};
			tiles[i].center = center;
			if(!tiles[i].isClickable())
			    tiles[i].setHint();
		    }
		}
		for (int i = 0; i < tiles.length; i++) {
		    tiles[i].setSize(finalSize*(3*time/totalTime-2));
		}
	    }
	    
	    else {
		for(int i = 0;i<tiles.length;i++){
		    tiles[i].setAngle(0);
		    tiles[i].setSize(finalSize);
		    
		}
		if (toggleLines) {
		    drawLines();
		}
		period = DrawPeriod.DURING;
	    }
	}
	
	public void duringAnimation(BoardTile[] tiles) {
	    for (int i = 0; i < tiles.length; i++) {
		((BoardTile)tiles[i]).setTextures();
	    }
	}
	
	public void draw(BoardTile[] tiles, MyGLRenderer r){
	    mBoardBg.draw(r);
	    super.draw(tiles, r);
	    mGameBanner.draw(r);
	    mMenu.draw(r);
	}
	
	@Override
	    public void touchHandler(float[] pt){
	    int val = mMenu.touched(pt);
	    if(val == -1){
		if (at != -1) {
		    tiles[at].setColor("transparent");
		}
		at = touched(pt);
		if(at != -1 ) {
		    if(tiles[at].isBlack() == false) {
			tiles[at].setColor("blue");
			if(tiles[at].isClickable())
			    mMenu.activate(pt);
		    }
		}
	    } else {
		if (at != -1) {
		    tiles[at].setUserInput(val);
		    if (toggleLines) {
			animatePlay(at);
		    }
		}
	    }
	    
	}
	
	public void swipeHandler(String direction){
	    if (at != -1 && tiles[at].isClickable()) {
		tiles[at].setArrow(direction);
		if (toggleLines) {
		    animatePlay(at); 
		}
		mMenu.menuActive = false;
	    }
	}
	
    }
    //This is the board after completing the game.
    class BoardGameEnd extends State<BoardTile> {
	boolean[] rotateTiles = new boolean[36];
	boolean[] flipped = new boolean[36];
	long[] refTime = new long[36];
	public BoardGameEnd(BoardTile[] tiles) {
	    for (int i = 0; i < tiles.length; i++) {
		flipped[i] = false;
		rotateTiles[i] = false;
		float Sx = ( (i/6) - 2.5f )/4.0f;
		float Sy = ( (i%6) - 2.5f )/4.0f;
		tiles[i].setSize(.12f);
		float center[] = { Sx, Sy, 0.0f};
		tiles[i].center = center;
		tiles[i].setColor("transparent");
		refTime[i] = System.currentTimeMillis();
		tiles[i].setPivot(tiles[i].getCenter());
	    }
	    if (toggleLines) {
		drawLines();
	    }
	}
	
	
	//The new animation:
	public void enterAnimation(BoardTile[] tiles) {
	    //	    long time = System.currentTimeMillis()-refTime[0];
	    for (int i = 0; i < tiles.length ; i++) {
		if (tiles[i].true_solution >0) {
		    float[] pivot = {1.0f,1.0f,0.0f};
		    String[] s = new String[2];
		    s[0] = TextureManager.CLEAR;
		    s[1] = tiles[i].flowerTexture;
		    tiles[i].setFlipper(geometry[1], pivot, 1.5f, 0.0f, s);
		}
		else {
		    tiles[i].setTextures();
		}
	    }
	    period = DrawPeriod.DURING;
	}
	
	public void duringAnimation(BoardTile[] tiles) {
	    for(int i=0;i<tiles.length;i++){
		if(tiles[i].rotate && !rotateTiles[i]){
		    refTime[i] = System.currentTimeMillis();
		    rotateTiles[i] = true;
		    float[] pivot = {1.0f,1.0f,0.0f};
		    String[] s = new String[2];
		    s[0] = tiles[i].arrow;
		    s[1] = tiles[i].number;
		    tiles[i].setFlipper(geometry[1], pivot, 1.5f, 0.0f, s);
		    //tiles[i].rotate = false;
		}
	    }
	}
	public void draw(BoardTile[] tiles, MyGLRenderer r){
	    mBoardBg.draw(r);
	    super.draw(tiles, r);
	    mGameBanner.draw(r);
	}
    }        
}
