package com.seventhharmonic.com.freebeeline.levelresources;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.seventhharmonic.android.freebeeline.GlobalApplication;

public class SAXLevelPackProvider implements LevelPackProvider{
	String TAG = "SAXLevelPackProvider";
	String BASE = "levels";
	List<LevelPack> levelPacks = new ArrayList<LevelPack>();
	LevelPackParser mLevelPackParser = new LevelPackParser();
	
	public SAXLevelPackProvider(){
	}

	public void initialize() {
	    Context context = GlobalApplication.getContext();
	    try{
	    levelPacks.clear();
	    //Notice that this ordering is very important!
	    //TODO: Change this when we add more level packs
	    String[] levelPackList = {"StoryPackOne.xml","StoryPackTwo.xml","ChallengePackOne.xml"};
		for(String s: levelPackList){
		    levelPacks.add(mLevelPackParser.parse(BASE+"/"+s));	    
		}
	    } catch(Exception e){
		throw new RuntimeException(e);
	    }
	    Log.d(TAG,"Level Pack parsed");
	    //GlobalApplication.getTextureBridge().startTextureCreation();
	}
	
	@Override
	public int getNumberOfLevelPacks() {
		return levelPacks.size();
	}

	@Override
	public LevelPack getLevelPack(int i) {
		return levelPacks.get(i);
	}

	@Override
	public void unlockLevelPack(int i) {
		// TODO Auto-generated method stub
		
	}

}
