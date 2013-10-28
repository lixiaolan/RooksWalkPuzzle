package com.seventhharmonic.com.freebeeline.levelresources;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.GlobalApplication;
import com.seventhharmonic.android.freebeeline.TextureManager;
import com.seventhharmonic.android.freebeeline.db.PuzzleDataSource;
import com.seventhharmonic.android.freebeeline.db.SQLPuzzle;

public class Puzzle {
	public String board;
	String image = TextureManager.getFlowerTexture();
	public String path;
	int width;
	List<Hint> hints = new ArrayList<Hint>();
	Puzzle nextPuzzle;
	boolean completed = false;
	long id;
	
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
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
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

	int height;
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

	
	public String getImage(){
		if(completed)
			return "check";
		return image;
	}
	
	public void setImage(String image){
		this.image = image;
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
	
	public Puzzle copy(){
		Puzzle p = new Puzzle();
		p.setBoard(board);
		p.setImage(image);
		p.hints = hints;
		p.setWidth(width);
		p.setHeight(height);
		p.setPath(path);
		p.setId(id);
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
