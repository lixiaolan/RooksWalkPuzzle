package com.seventhharmonic.android.freebeeline;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.listeners.GameEventListener;
import com.seventhharmonic.android.freebeeline.util.LATools;
import com.seventhharmonic.com.freebeeline.levelresources.Hint;
import com.seventhharmonic.com.freebeeline.levelresources.Puzzle;


class Board extends Graphic<BoardTile, State<BoardTile> > implements BeeBoardInterface{
    String TAG = "board";
    public int hints;
    public int[] solution ;
    public int[][] path;
    public int boardWidth = 6;
    public int boardHeight = 6;
    private boolean toggleHints = true;
    private boolean toggleLines = true;	
    private boolean toggleError = true;
    protected float flowerSize = .15f;
    protected float tileSize = .11f;    
    private long lastTouchTime;
    private float[] lastTouchPos = new float[2];
    ErrorLog mErrorLog;
    Border mBorder = new Border(boardWidth, boardHeight);
    Background mCheck;
    public TextWidget mGameBanner;
    private Background mBoardBg;
    Puzzle currPuzzle;
    Model mModel;
    Bee mBee;
    
    public Board(Model mModel) {
	buildEmptyBoard();
	state = new BoardMainMenu(tiles);
	mGameBanner = new TextWidget(0.0f, 0.0f, .9f, .4f, TextureManager.CLEAR);
	mBoardBg = new Background("boardbg", 1.0f);
	mErrorLog = new ErrorLog(this);
	this.mModel = mModel;
	mBee = new Bee(this);
    }    
    
    public void restoreBoard(int[] solution, String[] numbers, String[] arrows, String[] trueArrows, int[][] path, boolean[] clickable){
	for(int i=0;i<boardWidth*boardHeight;i++){
	    tiles[i].setTrueSolution(solution[i]);
	    tiles[i].setArrow(arrows[i]);
	    tiles[i].setNumber(numbers[i]);
	    tiles[i].setTrueArrow(trueArrows[i]);
	    if(clickable!=null)
		if(!clickable[i] && tiles[i].getTrueSolution()!=-1)
		    tiles[i].setHint();
	    tiles[i].setTextures();
	}
	this.path = path;
    }
    
    public void buildXMLBoard(int[] solution, int[][] path, int height, int width, int[] hints){
	this.path = path;
	int tilesLength = height*width;
	Log.d(TAG,Integer.toString(tilesLength));
	
	for (int i = 0; i < tilesLength; i++) {	
	    //This does a hard reset on the board.			
	    tiles[i].number = TextureManager.CLEAR;
	    tiles[i].arrow = TextureManager.CLEAR;
	    tiles[i].setTrueArrow(TextureManager.CLEAR);
	    tiles[i].setTrueSolution(solution[i]);
	} 
	
	int dx;
	int dy;
	int length = (path.length);
	
	for (int i = 0; i < length-1; i++) {
	    dx = path[i+1][0]-path[i][0];
	    dy = path[i+1][1]-path[i][1];
	    Log.d("path","path creation");
	    Log.d("path", "length "+Float.toString(length));
	    Log.d("path", "dx "+Float.toString(dx));
	    Log.d("path", "dy "+Float.toString(dy));
	    if (dx > 0) {
		tiles[boardHeight*path[i+1][0] + path[i+1][1]].setTrueArrow("right_arrow");
		//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "right_arrow";
	    }
	    
	    if (dx < 0) {
		tiles[boardHeight*path[i+1][0] + path[i+1][1]].setTrueArrow("left_arrow");
		//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "left_arrow";
	    }
	    
	    if (dy > 0) {
		tiles[boardHeight*path[i+1][0] + path[i+1][1]].setTrueArrow("down_arrow");
		//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "down_arrow";
	    }
	    if (dy < 0) {
		tiles[boardHeight*path[i+1][0] + path[i+1][1]].setTrueArrow("up_arrow");
		//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "up_arrow";
	    }
	    Log.d("path", tiles[boardHeight*path[i][0] + path[i][1]].getTrueArrow());
	}
	
	if(toggleHints){
	    for(int i=0;i<hints.length;i++){
		int r = hints[i];
		Log.d(TAG, "hint");
		Log.d(TAG, Integer.toString(r));
		tiles[r].setHint();
	    }
	}
	
    }
    
    public void buildEmptyBoard() {
	tiles = new BoardTile[boardHeight*boardWidth];
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
		tiles[boardHeight*path[i+1][0] + path[i+1][1]].setTrueArrow("right_arrow");
		//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "right_arrow";
	    }
	    if (dx < 0) {
		tiles[boardHeight*path[i+1][0] + path[i+1][1]].setTrueArrow("left_arrow");
		//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "left_arrow";
	    }
	    if (dy > 0) {
		tiles[boardHeight*path[i+1][0] + path[i+1][1]].setTrueArrow("down_arrow");
		//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "down_arrow";
	    }
	    if (dy < 0) {
		tiles[boardHeight*path[i+1][0] + path[i+1][1]].setTrueArrow("up_arrow");
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
    
    public void createPuzzleFromPuzzle(Puzzle p){
	boardHeight = p.getHeight();
	boardWidth = p.getWidth();
	System.out.println("boardHeight: " + Integer.toString(boardHeight));
	System.out.println("boardWidth: " + Integer.toString(boardWidth));
	currPuzzle = p;
	buildEmptyBoard();
	int[] solution = p.getBoard();
	int[][] path = p.getPath();
	
	List<Hint> hints = p.getAllHints();
	int[] hintList = new int[hints.size()];
	for(int i = 0; i< hints.size();i++){
	    hintList[i] = hints.get(i).getIndex();
	}
	buildXMLBoard(solution, path, p.getHeight(), p.getWidth(), hintList);
    }
    
    public int touched(float[] pt) {
	lastTouchPos = pt;
	lastTouchTime = System.currentTimeMillis();
	for (int i = 0; i < tiles.length; i++) {
	    if( tiles[i].touched(pt) && tiles[i].isClickable() && !tiles[i].isBlack()) {
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
	mBee.setState(s);
	switch(s) {
	case MAIN_MENU_OPENING: state = new BoardMainMenu(tiles); break;
	case GAME_OPENING: state  = new BoardPlay(tiles); break;
	case GAME_MENU_END: state = new BoardGameEnd(tiles); break;
	default: break;
	}
    }
    
    public boolean checkSolution(){
	for(int i =0; i < boardHeight*boardWidth; i++){
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

    public void toggleRules(boolean toggle){
	toggleError = toggle;
    }
    
    public int beeTouched(float[] pt) {
	return mBee.touched(pt);
    }

    /**
     * Show a hint on the board. This should be trigered when the bee is clicked.
     */
    public void showHint() {
	for(int i =0; i< tiles.length; i++){
	    if(tiles[i].getTrueSolution() > 0 && !tiles[i].isHint()){
		System.out.println("Found a potential tile");
		if(tiles[i].textures[0].equals(TextureManager.CLEAR) || tiles[i].textures[1].equals(TextureManager.CLEAR)){
		    //tiles[i].setSolution();
		    //tiles[i].setTextures();
		    tiles[i].setHint();
		    drawLines();
		    
		    break;
		}
	    }
	}
    }
    
    /*
     * Show solution at the end of the game. Currently just a utility function.
     */
    public void showSolution(){
	String sol = "";
	for(int i =0;i <tiles.length;i++){
	    if(tiles[i].getTrueSolution() == 0)
		sol  = "clear";
	    else
		sol = Integer.toString(tiles[i].getTrueSolution());
	    tiles[i].setNumber(sol);
	    tiles[i].setArrow(tiles[i].getTrueArrow());
	}
    }
    
    /*
     *	Get's called by Model's set geometry.
     */
    public void setGeometry(float[] g) {
	super.setGeometry(g);
	float top = mGameBanner.getHeight();
	mGameBanner.setCenter(0, g[1]-top);
    }
    
    public void animatePlay(int at) {
	//Now go though each tile and fill in pointed at tiles.
	float duration = .3f;
	float delay = .15f;
	clearLines();
	stopFlips();
	drawLines();
	if (tiles[at].hasNumber() && tiles[at].hasArrow()) {
	    int num = Integer.parseInt(tiles[at].getNumber());
	    if (tiles[at].getArrow().equals(TextureManager.UPARROW)) {
		for (int j = 1; j < num; j++) {
		    if (at%boardHeight + j < boardHeight) {
			if (!tiles[at+j].isBlack() && tiles[at+j].isBlank()) {
			    String[] s1 = new String[2];
			    s1[0] = TextureManager.VERTDOTS;
			    s1[1] = tiles[at+j].textures[1];
			    float[] pivot = {1.0f,0.0f,0.0f};
			    tiles[at+j].setTextures(TextureManager.CLEAR, TextureManager.CLEAR);
			    tiles[at+j].setFlipper(geometry[1], pivot, duration, j*delay ,s1);
			}
			
		    }
		}
	    }
	    if (tiles[at].getArrow().equals(TextureManager.DOWNARROW)) {
		for (int j = 1; j < num; j++) {
		    if (at%boardHeight - j >= 0) {
			if (!tiles[at-j].isBlack() && tiles[at-j].isBlank()) { 
			    String[] s2 = new String[2];
			    s2[0] = TextureManager.VERTDOTS;
			    s2[1] = tiles[at-j].textures[1];
			    float[] pivot = {1.0f,0.0f,0.0f};
			    tiles[at-j].setTextures(TextureManager.CLEAR, TextureManager.CLEAR);
			    tiles[at-j].setFlipper(geometry[1], pivot, duration, j*delay ,s2);
			}
			
		    }
		}
	    }
	    if (tiles[at].getArrow().equals(TextureManager.LEFTARROW)) {
		for (int j = 1; j < num; j++) {
		    if (at/boardHeight + j < boardWidth) { 
			if (!tiles[at+j*boardHeight].isBlack() && tiles[at+j*boardHeight].isBlank()) {
			    String[] s3 = new String[2];
			    s3[1] = TextureManager.HORZDOTS;
			    s3[0] = tiles[at+j*boardHeight].textures[0];
			    float[] pivot = {0.0f,1.0f,0.0f};
			    tiles[at+j*boardHeight].setTextures(TextureManager.CLEAR, TextureManager.CLEAR);
			    tiles[at+j*boardHeight].setFlipper(geometry[1], pivot, duration, j*delay ,s3);
			}
			
		    }
		}
	    }
	    if (tiles[at].getArrow().equals(TextureManager.RIGHTARROW)) {
		for (int j = 1; j < num; j++) {
		    if (at/boardHeight - j >= 0) {
			if (!tiles[at-j*boardHeight].isBlack() && tiles[at-j*boardHeight].isBlank()) {
			    String[] s4 = new String[2];
			    s4[1] = TextureManager.HORZDOTS;
			    s4[0] = tiles[at-j*boardHeight].textures[0];
			    float[] pivot = {0.0f,1.0f,0.0f};
			    tiles[at-j*boardHeight].setTextures(TextureManager.CLEAR, TextureManager.CLEAR);
			    tiles[at-j*boardHeight].setFlipper(geometry[1], pivot, duration, j*delay ,s4);
			}
		    }
		}
	    }
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
    
    public void stopFlips() {
	for (int i = 0; i < tiles.length; i++) {
	    tiles[i].stopFlipper();
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
		if (tiles[i].getArrow().equals(TextureManager.UPARROW)) {
		    for (int j = 1; j < num; j++) {
			if (i%boardHeight + j < boardHeight) {
			    if (!tiles[i+j].isBlack() && tiles[i+j].isBlank()) 
				tiles[i+j].vPointedAt = true;
			}
		    }
		}
		if (tiles[i].getArrow().equals(TextureManager.DOWNARROW)) {
		    for (int j = 1; j < num; j++) {
			if (i%boardHeight - j >= 0) {
			    if (!tiles[i-j].isBlack() && tiles[i-j].isBlank()) 
				tiles[i-j].vPointedAt = true;
			}
		    }
		}
		if (tiles[i].getArrow().equals(TextureManager.LEFTARROW)) {
		    for (int j = 1; j < num; j++) {
			if (i/boardHeight + j < boardWidth) {
			    if (!tiles[i+j*boardHeight].isBlack() && tiles[i+j*boardHeight].isBlank()) 
				tiles[i+j*boardHeight].hPointedAt = true;
			}
		    }
		}
		if (tiles[i].getArrow().equals(TextureManager.RIGHTARROW)) {
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
	    if (tiles[at].getArrow().equals(TextureManager.UPARROW)) {
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
	    if (tiles[at].getArrow().equals(TextureManager.DOWNARROW)) {
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
	    if (tiles[at].getArrow().equals(TextureManager.LEFTARROW)) {
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
	    if (tiles[at].getArrow().equals(TextureManager.RIGHTARROW)) {
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
		if (tiles[i].getArrow().equals(TextureManager.DOWNARROW)) {
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
		if (tiles[i].getArrow().equals(TextureManager.LEFTARROW)) {
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
		if (tiles[i].getArrow().equals(TextureManager.RIGHTARROW)) {
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
    
    //Bee Board Interface Methods:

    public BoardTile getTile(int index) {
	if (index >= tiles.length) {
	    return tiles[0];
	}
	return tiles[index];
    }

    public int getBoardHeight() {
	return boardHeight;
    }

    public int getBoardWidth() {
	return boardWidth;
    }

    public int getPathLength() {
	return path.length;
    }
    
    public void setTileRotate(int index) {
	if (index < tiles.length) {
	    tiles[index].rotate = true;	    
	}
    }

    public int getPathToArray(int index) {
	return boardHeight*path[index][0] + path[index][1];
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
		tiles[i].setCenter2D(LATools.vSum(tiles[i].getCenter2D(), LATools.vSProd(dt, tiles[i].velocity)));
		tiles[i].velocity = LATools.vSum(tiles[i].velocity, LATools.vSProd(dt, force));
	    }
	}
	
	public float[] getForce(BoardTile[] tiles, int i) {
	    float[] force = {0.0f, 0.0f};
	    float[] temp = new float[2];
	    float[] mid = {centers[2*i],centers[2*i+1],0.0f};
	    force = LATools.vSProd(-2.0f,LATools.vDiff(tiles[i].center, mid)); 
	    force = LATools.vSum(force, LATools.vSProd(-1.2f,tiles[i].velocity));
	    //Compute wave of forces due to touch
	    float time = 2*(System.currentTimeMillis()-lastTouchTime)/1000f;
	    temp = LATools.vDiff(tiles[i].center, lastTouchPos);
	    float sTemp = LATools.abs(temp);
	    if (sTemp<time && sTemp > time - .2f && sTemp > .00001f)
		force = LATools.vSum(force, LATools.vSProd(5f/((float)Math.pow(sTemp,1)), temp));
	    return force;
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
	int lt = -1;
	ButtonWidget reset;
	
	public BoardPlay(BoardTile[] tiles) {
	    originalTiles = tiles;
	    refTime = System.currentTimeMillis();
	    oldX = new float[tiles.length];
	    oldY = new float[tiles.length];
	    mMenu = new Menu(boardHeight);
	    reset = new ButtonWidget(0, -1.0f, .1f, .1f, TextureManager.ERASER);
	    reset.setClickListener(new GameEventListener(){
		    public void event(int i){
			resetBoard();
		    }
		});
	    for (int i = 0; i < tiles.length; i++) {
		tiles[i].setRotate(false);// = false;
		tiles[i].setTextures(TextureManager.CLEAR, tiles[i].flowerTexture);
		oldX[i] = tiles[i].center[0];
		oldY[i] = tiles[i].center[1];
		tiles[i].setColor("transparent");
		tiles[i].vPointedAt = false;
		tiles[i].hPointedAt = false;
	    }
	    mGameBanner.setText(TextureManager.CLEAR);
	    initSize = tiles[0].getSize();
	    mCheck  = new Background("check",.11f);
	    float[] center = {-.7f,-1f, 0f};
	    mCheck.setCenter(center);
	    
	}
	
	public void enterAnimation(BoardTile[] tiles) {
	    long time = System.currentTimeMillis() - refTime;
	    float totalTime = 2000f;
	    // This does a simultaneous snap to position and shrink of tiles.
	    if(time < totalTime/3) {
		for (int i = 0; i < tiles.length; i++) {
		    float Sx = ( (i/boardHeight) - boardWidth/2.0f + 0.5f )/4.0f;
		    float Sy = ( (i%boardHeight) - boardWidth/2.0f + 0.5f )/4.0f;
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
			float Sx = ( (i/boardHeight) - boardWidth/2.0f + 0.5f )/4.0f;
			float Sy = ( (i%boardHeight) - boardWidth/2.0f + 0.5f )/4.0f;
			float center[] = {Sx, Sy, 0.0f};
			tiles[i].center = center;
			if(!tiles[i].isClickable() && !tiles[i].isBlack())
			    tiles[i].setHint();
			else 
			    tiles[i].nativeColor = "transparent";
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
	    mGameBanner.draw(r);
	    //	    mBoardBg.draw(r);
	    super.draw(tiles, r);
	    mMenu.draw(r);
	    mCheck.draw(r);
	    reset.draw(r);
	}
	
	
	@Override
	    public void touchHandler(float[] pt){
	    int val = mMenu.touched(pt);
	    if(val == -1){
		//This case corresponds to the menu being closed
		if (at != -1) {
		    //If a tile was active close it. 
		    tiles[at].setColor("transparent");
		    //If this tile should be red, make sure it stays that way. Reset the banner
		    if(toggleError){
			turnErrorRed(at);
			mGameBanner.setText(TextureManager.CLEAR);
		    }
		}
		at = touched(pt);
		if(at != -1 ) {
		    //Open the menu at the tile that was touched. Display a banner if there is an error
		    if(toggleError){
			turnErrorRed(at);
			if(lt == at){
			    mGameBanner.setText(mErrorLog.getError(at));
			}
		    }
		    if(tiles[at].isBlack() == false) {
			tiles[at].setColor("blue");
			if(tiles[at].isClickable())
			    mMenu.activate(pt);
		    }
		}
	    } else {
		if (at != -1) {
		    //Set the user input since we get values
		    tiles[at].setUserInput(val);
		    if (toggleLines) {
			animatePlay(at);
		    }
		    if(toggleError){
			mErrorLog.setLog();
			updateErrors();
			turnErrorRed(at);
			mGameBanner.setText(mErrorLog.getError(at));
			lt = at;
		    }
		}
	    }
	    
	    if(mCheck.touched(pt) == 1){
		if(checkSolution()) {		
		    if(currPuzzle!=null){
			Log.d("board", "solved a puzzle and set completed to true");
			currPuzzle.setCompleted(true);
			mModel.state.saveCurrGame = false;
			mModel.state.resumeGameExists = false;
			mBee.setMood(Mood.HAPPY);
			mModel.setState(GameState.GAME_MENU_END);
			//TODO: DO THIS DIFFERENT WITH A HARD RESET
			mModel.toc.setState();
			mModel.toc.setState();
			GlobalApplication.getPuzzleDB().setPuzzle(currPuzzle.getId(),"true");
		    }
		    mGameBanner.setText(TextureManager.GOOD_JOB);
		} else {
		    mGameBanner.setText(TextureManager.TRY_AGAIN);
		    
		}
	    }
	    
	    reset.touchHandler(pt);
	    
	}
	
	public void updateErrors(){
	    for(int i =0;i<tiles.length;i++){
		String error = mErrorLog.getError(i); 
		if(error.equals(TextureManager.CLEAR)){
		    tiles[i].setColor(tiles[i].nativeColor);
		    if(i == at){
			tiles[i].setColor("blue");
		    }
		}
	    }
	}
	
	public void turnErrorRed(int at){
	    String error = mErrorLog.getError(at); 
	    if(!error.equals(TextureManager.CLEAR)){
		tiles[at].setColor("red");
	    } 
	}
	
	
	public void swipeHandler(String direction){
	    if (at != -1 && tiles[at].isClickable()) {
		tiles[at].setArrow(direction);
		if (toggleLines) {
		    animatePlay(at); 
		}
		if(toggleError){
		    mErrorLog.setLog();
		    turnErrorRed(at);
		    mGameBanner.setText(mErrorLog.getError(at));
		    updateErrors();
		}
		mMenu.menuActive = false;
		
	    }
	}
	
    }
    //This is the board after completing the game.
    class BoardGameEnd extends State<BoardTile> {
	boolean[] rotateTiles = new boolean[boardHeight*boardWidth];
	boolean[] flipped = new boolean[boardHeight*boardWidth];
	long[] refTime = new long[boardHeight*boardWidth];
	EndGameDialogWidgetLayout mDialog;
	
	public BoardGameEnd(BoardTile[] tiles) {
	    mDialog = new EndGameDialogWidgetLayout(.8f, TextureManager.GOOD_JOB);
	    mDialog.setNextClickListener(new GameEventListener(){
		    public void event(int i ){
			Log.d("Dialog","Clicked");
			mDialog.deactivate();
			if(currPuzzle.getNextPuzzle() != null) {
			    Log.d("next puzzle", "Apparently, there is another puzzle!");
			    createPuzzleFromPuzzle(currPuzzle.getNextPuzzle());
			    setState(GameState.GAME_OPENING);
			    mModel.setState(GameState.GAME_OPENING);
			} else {
			    //TODO: PLEASE DONT DO IT THIS WAY. TOC should allow an explicit change of state
			    mModel.toc.setState();
			    mModel.toc.setState();
			    mModel.setState(GameState.TABLE_OF_CONTENTS);
			}
		    }
		    
		});
	    
	    mDialog.setBackClickListener(new GameEventListener() {
		    
		    public void event(int i){
			mDialog.deactivate();
			//TODO: PLEASE DONT DO IT THIS WAY. TOC should allow an explicit change of state
			mModel.toc.setState();
			mModel.toc.setState();
			mModel.setState(GameState.TABLE_OF_CONTENTS);
		    }
		    
		});
	    
	    mDialog.activate();
	    for (int i = 0; i < tiles.length; i++) {
		flipped[i] = false;
		rotateTiles[i] = false;
		float Sx = ( (i/boardHeight) - boardWidth/2.0f + 0.5f )/4.0f;
		float Sy = ( (i%boardHeight) - boardWidth/2.0f + 0.5f )/4.0f;
		tiles[i].setSize(.12f);
		tiles[i].setCenter(Sx, Sy);
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
	
	public void touchHandler(float[] pt){
	    mDialog.touchHandler(pt);
	}
	
	public void draw(BoardTile[] tiles, MyGLRenderer r){
	    mGameBanner.draw(r);
	    mBoardBg.draw(r);
	    super.draw(tiles, r);
	    mDialog.draw(r);
	}
    }        
}
