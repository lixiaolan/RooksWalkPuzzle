package com.seventhharmonic.com.freebeeline.levelresources;

import java.util.ArrayList;
import java.util.List;

import com.seventhharmonic.android.freebeeline.TextureManager;

public class LevelPack {
	List<Chapter> chapters = new ArrayList<Chapter>();
	String title;
	
	public LevelPack(String name){
		this.title = name;
	}
	
	public LevelPack(){
	}

	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	int currChapter = 0;
		
	public int getCurrChapter() {
		return currChapter;
	}

	public void setCurrChapter(int currChapter) {
		this.currChapter = currChapter;
	}

	public void addChapter(Chapter c){
		chapters.add(c);
	}
	
	public List<Chapter> getAllChapters(){
		return chapters;
	}
	
	
	
	public Chapter getChapter(int i){
		return chapters.get(i);
	}

	public int getNumberOfChapters(){
		return chapters.size();
	}
	
	public String getCurrTitleImage() {
		return TextureManager.getFlowerTexture();
	}
	
	
	
}
