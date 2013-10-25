package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.com.freebeeline.levelresources.*;
import com.seventhharmonic.android.freebeeline.listeners.*;

public class TableOfContents extends GraphicWidget{

	private String TAG = "TOC";
	
	enum Contents {
		LEVELPACKDISPLAY, CHAPTERDISPLAY, PUZZLEDISPLAY
	}
	Contents mContents = Contents.LEVELPACKDISPLAY;
	LevelPackProvider mLPP;	
	LevelPack currLevelPack;
	Model mModel;
	
	public TableOfContents(Model model){
		mLPP = GlobalApplication.getLevelPackProvider();
		state = new LevelPackDisplay();
		mModel = model;
	}
	
	public void setState() {
		switch(mContents){
			case LEVELPACKDISPLAY: 
				mContents = Contents.CHAPTERDISPLAY;
				state = new ChapterDisplay();
			break;
			default:
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
				m.addWidget(new LevelPackWidget(mLPP.getLevelPack(i).getTitle(),"forest"));
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
			currLevelPack = mLPP.getLevelPack(m.getActiveWidget());
			Log.d(TAG,"touched LevelPackDisplay");
			setState();
		}
		
	}

	class ChapterDisplay extends StateWidget{
		ScreenSlideWidgetLayout m;
		ChapterWidget currChapterWidget;
		
		public ChapterDisplay(){
			m = new ScreenSlideWidgetLayout(2.0f);
			for(int i =0;i<currLevelPack.getNumberOfChapters();i++){
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
			currChapterWidget = (ChapterWidget)m.getWidget(m.getActiveWidget());
			currChapterWidget.touchHandler(pt);
		}
	}
	
}
