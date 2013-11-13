package com.seventhharmonic.com.freebeeline.levelresources;

import java.util.ArrayList;
import java.util.List;

import com.seventhharmonic.android.freebeeline.graphics.TextureManager;

public class LevelPack {
	List<Chapter> chapters = new ArrayList<Chapter>();
	String title;
	
	public LevelPack(String name){
		this.title = name;
	}
	
	public LevelPack(){
	}
	
	public String getStyle(){
		//TODO: Make this read the style from the XML!!!!
		return "default";
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
		
	public int getCurrChapter() {
	    for (int i = 0; i < chapters.size(); i++) {
		if (!chapters.get(i).getCompleted())
		    return i;
	    }
	    return 0;
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
		return "levelPack1Banner";
	}	
}
