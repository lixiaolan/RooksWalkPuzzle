package com.seventhharmonic.com.freebeeline.levelresources;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.graphics.TextureManager;

public class Chapter {
	String TAG = "Chapter";
	String title;
	String end_text;
	public String getEnd_text() {
		return end_text;
	}


	public void setEnd_text(String end_text) {
		this.end_text = end_text;
	}

	List<Puzzle> puzzles =  new ArrayList<Puzzle>();
	int height = 0;
	int width = 0;
	List<String> beforeImageList = new ArrayList<String>();
	List<String> afterImageList = new ArrayList<String>();
	Chapter prevChapter;
	Chapter nextChapter;
	LevelPack lp;
	
	public LevelPack getLp() {
		return lp;
	}


	public void setLp(LevelPack lp) {
		this.lp = lp;
	}


	public Chapter getPrevChapter() {
		return prevChapter;
	}


	public void setPrevChapter(Chapter prevChapter) {
		this.prevChapter = prevChapter;
	}


	public Chapter getNextChapter() {
		return nextChapter;
	}


	public void setNextChapter(Chapter nextChapter) {
		this.nextChapter = nextChapter;
	}

	
	protected String getBeforeImage() {
		//If there is no previous chapter, then we are the first chapter.
		if(prevChapter == null){
			Log.d(TAG, title+" prev chapter is null"+beforeImageList.get(0));
			return afterImageList.get(0);
		} 
		//If there is a previous chapter AND it is completed return our image
		else if(prevChapter.getCompleted()){
			Log.d(TAG, title+" previous chapter is completed"+beforeImageList.get(0));
			return afterImageList.get(0);
		} 
		//Other wise we are locked out :(
		Log.d(TAG, title+" locked out"+beforeImageList.get(0));
		return beforeImageList.get(0);
	}

	
	protected String getAfterImage() {
	    return afterImageList.get(0);
	}

	//Temp list for use in getBreforeCompletionImageList
	List<String> images = new ArrayList<String>();

	/** This returns a list of images that will display before completion of the chapter as the "placeholder" animation.
	 * In the current iteration, this is guaranteed to return a list of size 1. It will be a "locked" image (blank) if the 
	 * previous chapter is not finished, otherwise it will be the first "after-image" ie the keyframe associated to this chapter.
	 * @return
	 */
	public List<String> getBeforeCompletionImageList() {
		images.clear();
		images.add(getBeforeImage());
		//return beforeImageList;
		return images;
	}

	
	public List<String> getAfterCompletionImageList() {
	    return afterImageList;
	}

	public boolean getCompleted(){
		
		for(int i =0;i<puzzles.size();i++){
		 	if(!puzzles.get(i).isCompleted()){
		 		return false;
		 	}
		 }
		
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

	public Chapter(String title, String end_text){
		this.title = title;
		this.end_text = end_text;
	}
	
	private Chapter(List<Puzzle> puzzles, String title, String end_text){
		this.puzzles = puzzles;
		this.title = title;
		this.end_text = end_text;
	}
	
	public Chapter(){
	}
	
	public void addPuzzle(Puzzle p){
		puzzles.add(p);
		if(puzzles.size() > 1)
			puzzles.get(puzzles.size()-2).setNextPuzzle(p);
		if(puzzles.size() >= 2)
			p.setPrevPuzzle(puzzles.get(puzzles.size()-2));
	}
	
	public Puzzle getPuzzle(int i){
		return puzzles.get(i);
	}
	
	public List<Puzzle> getAllPuzzles(){
		return puzzles;
	}
	
	public void reset(){
		puzzles = new ArrayList<Puzzle>();
		afterImageList = new ArrayList<String>();
		beforeImageList = new ArrayList<String>();
	}
	
	public Chapter copy(){
		Chapter ch  = new Chapter(puzzles, title, end_text);
		for(Puzzle p:puzzles){
			p.setChapter(ch);
		}
		ch.setHeight(height);
		ch.setWidth(width);
		ch.afterImageList = afterImageList;
		ch.beforeImageList = beforeImageList;
		ch.images = images;
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
