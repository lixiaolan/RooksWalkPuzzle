package com.seventhharmonic.com.freebeeline.levelresources;

public interface LevelPackProvider {
	public int getNumberOfLevelPacks();
	public LevelPack getLevelPack(int i);
	public void unlockLevelPack(int i);
	public void initialize();
}
