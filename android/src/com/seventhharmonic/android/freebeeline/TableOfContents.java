package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.com.freebeeline.levelresources.*;
import com.seventhharmonic.android.freebeeline.listeners.*;

public class TableOfContents extends GraphicWidget{

	private String TAG = "TOC";

	public enum Contents {
		LEVELPACKDISPLAY, CHAPTERDISPLAY
	};

	Contents mContents;
	LevelPackProvider mLPP;	
	LevelPack currLevelPack;
	Model mModel;

	public TableOfContents(Model model){

		mLPP = GlobalApplication.getLevelPackProvider();
		mModel = model;
		mContents = Contents.LEVELPACKDISPLAY;
		state = new LevelPackDisplay();
	}

	public void setState() {
		System.out.println("In set state");
		System.out.println(mContents);
		switch(mContents){
		case LEVELPACKDISPLAY: 
			mContents = Contents.CHAPTERDISPLAY;
			state = new ChapterDisplay();
			break;
		case CHAPTERDISPLAY:
			mContents = Contents.LEVELPACKDISPLAY;
			state = new LevelPackDisplay();
			break;
		}
	}

	@Override
	public void touchHandler(float[] pt) {
		state.touchHandler(pt);
	}

	@Override
	public void swipeHandler(String direction) {
		state.swipeHandler(direction);

	}

	class LevelPackDisplay extends StateWidget {

		ScreenSlideWidgetLayout m;
		public LevelPackDisplay(){
			m = new ScreenSlideWidgetLayout(2.0f);
			m.setDrawProgressBar(false);
			for(int i =0;i<mLPP.getNumberOfLevelPacks();i++){
				m.addWidget(new LevelPackWidget(TextureManager.GOOD_JOB,"forest"));
				//m.addWidget(new LevelPackWidget(mLPP.getLevelPack(i).getTitle(),"forest"));
			}
			currLevelPack = mLPP.getLevelPack(0);
		}

		@Override
		public void enterAnimation() {
			period = DrawPeriod.DURING;
		}

		@Override
		public void duringAnimation(){
		}

		@Override
		public void draw(MyGLRenderer r){
			super.draw(r);
			m.draw(r);
		}

		@Override
		public void swipeHandler(String direction) {
			m.swipeHandler(direction);

		}

		@Override
		public void touchHandler(float[] pt) {
			currLevelPack = mLPP.getLevelPack(m.getActiveWidget());
			Log.d(TAG,"touched LevelPackDisplay");
			if(m.isTouched(pt)){
				setState();
			}
		}

	}

	class ChapterDisplay extends StateWidget{
		ScreenSlideWidgetLayout m;
		Widget currChapterWidget;

		public ChapterDisplay(){
			m = new ScreenSlideWidgetLayout(2.0f);
			for(int i =0;i<currLevelPack.getNumberOfChapters();i++){
				
				//If the previous chapter is completed, launch a normal chapter widget
				if(i==0 || currLevelPack.getChapter(i-1).getCompleted()){
					final Chapter c = currLevelPack.getChapter(i);
					ChapterWidget ch  = new ChapterWidget(c);
					ch.setTouchListener(new GameEventListener() {
						public void event(int puzz){
							Puzzle p = c.getPuzzle(puzz);
							mModel.createPuzzleFromPuzzle(p);
							mModel.setState(GameState.GAME_OPENING);
						}

					});
					m.addWidget(ch);
				}

				//Otherwise lock the user out!
				else {
					m.addWidget(new LockedChapterWidget());
				}
			}
		}

		@Override
		public void enterAnimation() {
			period = DrawPeriod.DURING;
		}

		@Override
		public void duringAnimation(){
		}

		@Override
		public void draw(MyGLRenderer r){
			super.draw(r);
			m.draw(r);
		}

		@Override
		public void swipeHandler(String direction) {
			m.swipeHandler(direction);

		}

		@Override
		public void touchHandler(float[] pt) {	
			currChapterWidget = m.getWidget(m.getActiveWidget());
			currChapterWidget.touchHandler(pt);
		}
	}

}
