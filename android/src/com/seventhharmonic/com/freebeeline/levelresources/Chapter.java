package com.seventhharmonic.com.freebeeline.levelresources;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.graphics.TextureManager;

public class Chapter {
	String title;
	List<Puzzle> puzzles =  new ArrayList<Puzzle>();
	int height = 0;
	int width = 0;
	boolean completed = true;
	String beforeImage;
	String afterImage;
	float[] flowerCoords;
	
	public void setFlowerCoords(float x, float y){
		//TODO: Get a more correct ratio for the height here!
		flowerCoords = new float[2];
		Log.d("Chapter", Float.toString(x)+" "+Float.toString(y));
		float h = 1.57f;
		flowerCoords[0] = -1f/512f*x+1f-.125f; 
		flowerCoords[1] = 2f*h/((512f*1.57f)*2)*y-h+.25f; 
		Log.d("Chapter", "chapter "+Float.toString(flowerCoords[0])+" "+Float.toString(flowerCoords[1]));
	}
	
	public float[] getFlowerCoords(){
		return flowerCoords;
	}

	protected String getBeforeImage() {
		return beforeImage;
	}

	protected void setBeforeImage(String beforeImage) {
		this.beforeImage = beforeImage;
	}
	
	protected String getAfterImage() {
		return afterImage;
	}

	protected void setAfterImage(String afterImage) {
		this.afterImage = afterImage;
	}

	public boolean getCompleted(){
		//TODO: Put this code back!
		//for(Puzzle p: puzzles){
		// 	if(!p.isCompleted()){
		// 		completed  = false;
		// 		return false;
		// 	}
		 //}
		
		completed = true;
		return true;
	}
	
	public int getNumberOfPuzzles(){
		return puzzles.size();
	}
	
	public int getNumberPuzzlesIncomplete() {
		int a  = 0;
		Log.d("Chapter","Puzzle id's in this chapter");
		for(Puzzle p: puzzles){
			Log.d("Chapter", Long.toString(p.id));
			if(!p.isCompleted())
				a++;
		}
		return a;
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

	public Chapter(String title){
		this.title = title;
	}
	
	private Chapter(List<Puzzle> puzzles, String title){
		this.puzzles = puzzles;
		this.title = title;
	}
	
	public Chapter(){
	}
	
	public void addPuzzle(Puzzle p){
		puzzles.add(p);
		if(puzzles.size() > 1)
			puzzles.get(puzzles.size()-2).setNextPuzzle(p);
	}
	
	public Puzzle getPuzzle(int i){
		return puzzles.get(i);
	}
	
	public List<Puzzle> getAllPuzzles(){
		return puzzles;
	}
	
	public void reset(){
		puzzles = new ArrayList<Puzzle>();
	}
	
	public Chapter copy(){
		Chapter ch  = new Chapter(puzzles, title);
		for(Puzzle p:puzzles){
			p.setChapter(ch);
		}
		ch.setHeight(height);
		ch.setWidth(width);
		ch.setAfterImage(afterImage);
		ch.setBeforeImage(beforeImage);
		ch.flowerCoords = flowerCoords;		
		return ch;
	}

	public String getTitle() {
		System.out.println("Got title "+title);
		return title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getImage() {
			return getAfterImage();
	}
	
	
	
}
