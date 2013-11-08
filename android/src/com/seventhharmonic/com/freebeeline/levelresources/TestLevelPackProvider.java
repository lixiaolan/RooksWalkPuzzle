package com.seventhharmonic.com.freebeeline.levelresources;

import com.seventhharmonic.android.freebeeline.graphics.TextureManager;

public class TestLevelPackProvider implements LevelPackProvider{

	@Override
	public int getNumberOfLevelPacks() {
		return 5;
	}

	@Override
	public LevelPack getLevelPack(int i) {
		return new LevelPack(TextureManager.GOOD_JOB);
	}

	@Override
	public void unlockLevelPack(int i) {
		// TODO Auto-generated method stub
	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}

}
