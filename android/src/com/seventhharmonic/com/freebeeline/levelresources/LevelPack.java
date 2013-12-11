package com.seventhharmonic.com.freebeeline.levelresources;

import java.util.ArrayList;
import java.util.List;

import com.seventhharmonic.android.freebeeline.graphics.TextureManager;


public class LevelPack {
	List<Chapter> chapters = new ArrayList<Chapter>();
	String title;
	String purchaseId;
	String banner;
    String style;
    String purchaseTag;
    String purchaseBanner;
    String id;
    int kickback;
    
    
    
	
	public int getKickback() {
		return kickback;
	}

	public void setKickback(int kickback) {
		this.kickback = kickback;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPurchaseBanner() {
		return purchaseBanner;
	}

	public void setPurchaseBanner(String purchaseBanner) {
		this.purchaseBanner = purchaseBanner;
	}

	public String getPurchaseTag() {
		return purchaseTag;
	}

	public void setPurchaseTag(String tag) {
		this.purchaseTag = tag;
	}

	public LevelPack(){
	}
	
	public String getPurchaseId(){
		return purchaseId;
	}

	public void setPurchaseId(String id){
		this.purchaseId = id;
	}

	
	public String getStyle(){
		return style;
	}

	public void setStyle(String style){
		this.style = style;
	}



    public String getSong() {
	//TODO: Make this read the song from the XML!!!!
	return "default";
    }
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	/**Returns the first chapter with an unfinished puzzle.
	 * @return
	 */
	public int getCurrChapter() {
	    for (int i = 0; i < chapters.size(); i++) {
		if (!chapters.get(i).getCompleted())
		    return i;
	    }
	    return 0;
	}

	public void addChapter(Chapter c){
		chapters.add(c);
		c.setLp(this);
		if(chapters.size() > 1)
			chapters.get(chapters.size()-2).setNextChapter(c);
		if(chapters.size() >= 2)
			c.setPrevChapter(chapters.get(chapters.size()-2));
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
	
}
