package com.seventhharmonic.com.freebeeline.levelresources;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.GlobalApplication;

public class SAXLevelPackProvider implements LevelPackProvider{
	String TAG = "SAXLevelPackProvider";
	List<LevelPack> levelPacks = new ArrayList<LevelPack>();
	LevelPackParser mLevelPackParser = new LevelPackParser();
			
	public SAXLevelPackProvider(){
	}
	
	public void initialize() {
		levelPacks.add(mLevelPackParser.parse());
		Log.d(TAG,"Level Pack parsed");
		GlobalApplication.getTextureBridge().startTextureCreation();
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
