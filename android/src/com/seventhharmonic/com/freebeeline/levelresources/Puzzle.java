package com.seventhharmonic.com.freebeeline.levelresources;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.GlobalApplication;
import com.seventhharmonic.android.freebeeline.db.PuzzleDataSource;
import com.seventhharmonic.android.freebeeline.db.SQLPuzzle;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;

public class Puzzle {
	public String board;
	public String path;
	
	int pathLength;
	int width;
	int height;
	List<Hint> hints = new ArrayList<Hint>();
	Puzzle nextPuzzle;
	Puzzle prevPuzzle;
	
	public Puzzle getPrevPuzzle() {
		return prevPuzzle;
	}

	public void setPrevPuzzle(Puzzle prevPuzzle) {
		this.prevPuzzle = prevPuzzle;
	}

	boolean completed = false;
	long id;
	Chapter ch;
	String beforeFlower;
	String afterFlower;
	String text;
	int moves;
	int par;
	
	
	
	public String getText() {
		return text;
	}

	public boolean isUnlocked(){
		//First puzzle in a chapter
		if(prevPuzzle == null){
			//First puzzle in the game!
			if(ch.getPrevChapter() == null){
				return true;
			} else{
				return ch.getPrevChapter().getCompleted();
			}
		} else{
			return prevPuzzle.isCompleted();
		}
		
		//New logic. A puzzle is unlocked when the whole previous chapter is unlocked.
	/*	if(prevPuzzle == null || ch.getPrevChapter() == null){
			return true;
		} else if(ch.getPrevChapter().getCompleted()){
			return true;
		} else{
			return false;
		}*/
	}
	
	public void setText(String text) {
		if(text == null)
			this.text="";
		else
			this.text = text;
	}

	protected String getBeforeFlower() {
		return beforeFlower;
	}

	public void setBeforeFlower(String beforeFlower) {
		this.beforeFlower = beforeFlower;
	}

	private String getAfterFlower() {
		Log.d("Puzzle", "moves + "+Integer.toString(moves)+" "+Integer.toString(getPar()));
		if(moves <= getPar()){
			return afterFlower;
		}
		
		return beforeFlower;
	}

	protected void setAfterFlower(String afterFlower) {
		this.afterFlower = afterFlower;
	}

	public int getPar(){
		//TODO: move par into XML?
		par = CSVIntParser(path).length-2*getNumberOfHints()-2;
		return par;
	
	}
	
	public String getFlower(){
		if(isCompleted()){
			return getAfterFlower();
		}
		return getBeforeFlower();
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isCompleted() {
		 //SQLPuzzle q = GlobalApplication.getDB().getPuzzle(getId());
		 //String result = q.getCompleted();
		 //if(result.equals(true))
		//	 return true;//p.setCompleted(true);
		return completed;
		//return true;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public void completePuzzleListener(int moves, boolean completed){
		setMoves(moves);
		setCompleted(completed);
		GlobalApplication.getPuzzleDB().setPuzzle(this.getId(),"true",moves);

	}
	
	public int getMoves(){
		return moves;
	}
	
	public void setMoves(int moves){
		if(moves < this.moves && this.moves > 0){
			this.moves = moves;
		} else if(this.moves == 0){
			this.moves = moves;
		}
	}
	
	public void setChapter(Chapter ch){
		this.ch  = ch;
	}
	
	public Chapter getChapter(){
		return ch;
	}
	
	public Puzzle getNextPuzzle() {
		return nextPuzzle;
	}

	public void setNextPuzzle(Puzzle nextPuzzle) {
		this.nextPuzzle = nextPuzzle;
	}

	public void setPath(String path) {
		this.path = path;
	}

	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}	
	
	public Puzzle(){
	}
	
	public void setBoard(String board){
		this.board = board;
	}
	
	public int[] getBoard(){
		return CSVIntParser(board);
	}

	public int[][] getPath(){
		int[] p = CSVIntParser(path);
		Log.d("puzzpath", Integer.toString(p.length));
		int[][] sol = new int[p.length/2][2]; 
		for(int i = 0; i<p.length/2; i++){
			sol[i][0] = p[2*i];
			sol[i][1] = p[2*i+1];
		}
		return sol;
	}
	
	public void reset(){
		hints = new ArrayList<Hint>();
	}
	
	public void addHint(Hint h){
		hints.add(h);
	}
	
	public Hint getHint(int i) {
		return hints.get(i);
	}
	
	public List<Hint> getAllHints(){
		return hints;
	}
	
	public int getNumberOfHints(){
		return hints.size();
	}
	
	public Puzzle copy(){
		Puzzle p = new Puzzle();
		p.setBoard(board);
		p.hints = hints;
		p.setWidth(width);
		p.setHeight(height);
		p.setPath(path);
		p.setId(id);
		p.setChapter(ch);
		p.setBeforeFlower(beforeFlower);
		p.setAfterFlower(afterFlower);
		p.setText(text);
		return p;
	}
	
	private int[] CSVIntParser(String b){
		String[] ba = b.split(",", -1);
		int[] solution = new int[ba.length];
		for(int i =0;i< ba.length;i++){
			solution[i] = Integer.parseInt(ba[i]);
		}
		return solution;
	}
	
	
	
}
