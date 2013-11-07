package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.listeners.GameEventListener;
import com.seventhharmonic.com.freebeeline.levelresources.Chapter;
import com.seventhharmonic.com.freebeeline.levelresources.LevelPack;

public class ChapterWidget extends WidgetLayout{
	String TAG = "ChapterWidget";
	TextWidget mText;	
	GridWidgetLayout mGrid;
	Chapter ch;
	GameEventListener listener;
	ImageWidget mImage;
	ImageWidget finishedFlower;
	
	public ChapterWidget(Chapter ch){
		float height = GlobalApplication.getGeometry().getGeometry()[1];
		/*TODO:
		 *In the future, forest should be replaced by ch.getImage.
		 *This sets the background image. 
		 */
		mImage = new ImageWidget(0,0,.8f, .8f*height, ch.getImage());
		mImage.setRelativeCenter(0,0);
		mImage.setMode(MyGLRenderer.FIXEDWIDTH);
		widgetList.add(mImage);
		
		/*TODO: Place some kind of text here.
		 * Set the text at the top of the screen.
		 */
		mText = new TextWidget(0, 0,1,.5f,ch.getTitle());
		mText.setRelativeCenter(0,height-mText.getHeight());
		setCenter(0,0);
		setWidth(1);
		setHeight(height);
		
		//widgetList.add(mText);
		/*
		 * If the chapter is completed, a spinning flower will appear at the lower left.
		 * TODO: Set Event handler of this flower.
		 */
		if(ch.getCompleted()){
			finishedFlower = new ImageWidget(0,0,.125f, .125f, ch.getPuzzle(0).getFlower());
			finishedFlower.setColor("blue");
			finishedFlower.setRelativeCenter(1-finishedFlower.getWidth(), -1*(height-finishedFlower.getHeight()));
			widgetList.add(finishedFlower);
		}
		
		/*
		 * Create a grid of flowers.
		 * Look for the first not completed puzzle. Spin that one.
		 */
		mGrid = new GridWidgetLayout(ch.getWidth(), ch.getHeight(), .15f);
		mGrid.setRelativeCenter(0, 0);
		boolean foundCompleted = false;
		for(int i =0;i<ch.getNumberOfPuzzles();i++){
			PuzzleWidget mFlower = new PuzzleWidget(0,0,.125f, .125f, ch.getPuzzle(i));
			if(!foundCompleted && !ch.getPuzzle(i).isCompleted()){
				mFlower.setRotate(true);
				foundCompleted = true;
			}
			mGrid.addWidget(mFlower);
		}
		widgetList.add(mGrid);
	}

	@Override
	public void touchHandler(float[] pt){
		int puzzTouched = mGrid.widgetTouched(pt);
		if(puzzTouched !=-1){
			listener.event(puzzTouched);
		}
	}
	
	public void setTouchListener(GameEventListener g){
		listener = g;
	}
	
	@Override
	protected void computeGeometry() {
		for(int i =0;i< widgetList.size();i++){
			Widget w = widgetList.get(i);
			w.setCenter(getCenterX()+w.getRelativeCenterX(), getCenterY()+w.getRelativeCenterY());
		}
	}
	
	@Override
	public void setCenter(float x, float y){
			super.setCenter(x,y);
			computeGeometry();
	}
	
	@Override
	public void draw(MyGLRenderer r) {
		for(Widget w: widgetList)
			w.draw(r);
	}

	

}
