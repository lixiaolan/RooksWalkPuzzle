package com.seventhharmonic.android.freebeeline;


import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.graphics.Geometry;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
import com.seventhharmonic.android.freebeeline.listeners.GameEventListener;
import com.seventhharmonic.com.freebeeline.levelresources.Chapter;

public class ChapterWidget extends GraphicWidget {
	enum ChapterState{
		PUZZLE, FINISHED
	}
	
	String TAG = "ChapterWidget";
	GridWidgetLayout mGrid;
	GridWidgetLayout mGrid2;
	Chapter ch;
	GameEventListener listener;
	//ImageWidget mImage;
	//ImageWidget mImageBorder;
	ImageWidget mImageTop;
	//ButtonWidget finishedFlower;
	List<Widget> widgetList = new ArrayList<Widget>();
	Geometry geometry;
	StateWidget state;
	ChapterState mChapterState;
	TextBox mText;
	boolean chEnd;
	
	public ChapterWidget(Chapter ch){
		float height = GlobalApplication.getGeometry().getGeometry()[1];
		
		setCenter(0,0);
		setWidth(1);
		setHeight(height);
		
		this.ch = ch;
		
		/*mImage = new ImageWidget(0,0,.9f, .9f*height, ch.getImage());
		mImage.setRelativeCenter(0,.05f);
		mImage.setBorder(true);
		mImage.setMode(MyGLRenderer.FIXEDWIDTH);
		widgetList.add(mImage);
		*/
		//widgetList.add(mImageBorder);
		
		/*TODO: Place some kind of text here. - OR NOT>>>>>>
		 * Set the text at the top of the screen.
		 */
		mText = new TextBox(0f, 0f, 1f, "Frame 1");
		mText.setFontSize(2);
		mText.setRelativeCenter(-.3f,height-.5f);
		widgetList.add(mText);

		//mImageTop = new ImageWidget(0,0,.8f, .395f, "levelPack1Banner");
		//mImageTop.setRelativeCenter(0,.95f);
		//mImageTop.setBorder(true);
		//mImageTop.setColor("white");
		//mImageTop.setMode(MyGLRenderer.CROPTOP);
		//widgetList.add(mImageTop);
/*		
		/*
		 * If the chapter is completed, a spinning flower will appear at the lower left.
		 
		//if(ch.getCompleted()){
			//float x = ch.getFlowerCoords()[0];
			//float y =ch.getFlowerCoords()[1]; 
			//Log.d(TAG, "coords"+Float.toString(x)+" "+Float.toString(y));
			finishedFlower = new ButtonWidget(0,0,.25f, .25f, ch.getPuzzle(0).getFlower());
			finishedFlower.setRelativeCenter(1-finishedFlower.getWidth(), -1*(height-finishedFlower.getHeight()));
			//finishedFlower.setRelativeCenter(x, y );
			//TODO: Rename button style
			finishedFlower.setBorder(false);
			//finishedFlower.setRotate(true);
			//finishedFlower.setBorderStyle(ButtonWidget.ButtonStyle.SQUARE);
			finishedFlower.setClickListener(new GameEventListener(){
				public void event(int i){
				setState();
			}
				
			});	
			widgetList.add(finishedFlower);
		//}
*/		
		/*
		 * Create a grid of flowers.
		 * Look for the first not completed puzzle. Spin that one.
		 * mGrid2 should draw opaque squares. Without this, we end up 
		 * having some serious framerate issues.
		 */
		mGrid = new GridWidgetLayout(ch.getWidth(), ch.getHeight(), .22f);
		mGrid.setRelativeCenter(0, 0);
		mGrid2 = new GridWidgetLayout(ch.getWidth(), ch.getHeight(), .22f);
		mGrid2.setRelativeCenter(0, 0);
		
		boolean foundCompleted = false;
		for(int i =0;i<ch.getNumberOfPuzzles();i++){
			PuzzleWidget mFlower = new PuzzleWidget(0,0,.15f, .15f, ch.getPuzzle(i));
			ImageWidget overlay = new ImageWidget(0,0,.15f, .15f,TextureManager.CLEAR);
			mFlower.setColor("white");
			overlay.setBorder(true);
			if(!foundCompleted && !ch.getPuzzle(i).isCompleted()){
				mFlower.setPulse(true);
				foundCompleted = true;
			}else if(!ch.getPuzzle(i).isCompleted() && foundCompleted){
				overlay.setColor("opaque");
			} 
			mGrid.addWidget(mFlower);

			mGrid2.addWidget(overlay);
		}
		widgetList.add(mGrid);
		widgetList.add(mGrid2);
	
		state = new Puzzle_State();
		mChapterState = ChapterState.PUZZLE;
	}

	@Override
	public void setState(){
		setState(false);
	}
	
	public void setState(boolean a) {
		Log.d(TAG,"setState() in ChapterWidget");
		switch(mChapterState){
		case PUZZLE:
			state = new Finished_State(a);
			mChapterState = ChapterState.FINISHED;
			break;
		case FINISHED:
			state = new Puzzle_State();
			mChapterState = ChapterState.PUZZLE;
			break;		
		}
	}

	/*public void setFinishedState(){
		if((mChapterState == ChapterState.PUZZLE || (mChapterState == ChapterState.FINISHED && chEnd)) && ch.getCompleted()){
			state = new Finished_State(false);
			mChapterState = ChapterState.FINISHED;
		}
	}
	
	public void setFinishedChapter() {
		state = new Finished_State(true);
		mChapterState = ChapterState.FINISHED;
	}
	*/
	
	@Override
	public void touchHandler(float[] pt){
		state.touchHandler(pt);
	}
		
	@Override
	public void swipeHandler(String direction) {
	}

	class Puzzle_State extends StateWidget{

		List<Widget> list;
		float[] initialPositionX;
		float[] initialPositionY;
		float[] finalPosition;
		long refTime;		

		public Puzzle_State(){
			mGrid.computeGeometry();
			mGrid2.computeGeometry();
			list = mGrid.widgetList;
			finalPosition = new float[]{1-.15f, -1*GlobalApplication.getGeometry().getGeometry()[1]+.15f};
			refTime = System.currentTimeMillis();
			initialPositionX = new float[list.size()];
			initialPositionY = new float[list.size()];
			for(int i =0;i< list.size();i++){
				initialPositionX[i] = list.get(i).getCenterX();
				initialPositionY[i] = list.get(i).getCenterY();
			}
		}
		
		
		@Override
		public void enterAnimation() {
			long time = (System.currentTimeMillis() - refTime);
			if(time < 500){
				for(int i =0;i< list.size(); i++){
					computeAnimation(i, 500-time, 500);
				}
			} else{
				mGrid.computeGeometry();
				period = DrawPeriod.DURING;
			}
			
		}

		public void computeAnimation(int i, long time, long totalDuration){
			float expDec = 10.0f;	
			float t = delay(i, time, totalDuration);
			float x = initialPositionX[i] + (1-(float)Math.exp(-t/totalDuration*expDec ) )*(finalPosition[0] - initialPositionX[i]);
			float y = initialPositionY[i] + (1-(float)Math.exp(-t/totalDuration*expDec ) )*(finalPosition[1] - initialPositionY[i]);
			
			list.get(i).setCenter(x+mGrid.getCenterX(), y+mGrid.getCenterY());
		}
		
		public float delay(int i, long time, long totalDuration) {
			float div = 2.0f;
			if ( ((float)i)/((float)list.size())*((float)totalDuration)/div > time  ){
				return 0.0f;
			}
			else {
				return time - ((float)i)/((float)list.size())*((float)totalDuration)/div; 
			}
		}

		
		@Override
		public void duringAnimation() {
		}

		@Override
		public void swipeHandler(String direction) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void touchHandler(float[] pt) {
			int puzzTouched = mGrid.widgetTouched(pt);
			if(puzzTouched !=-1){
				listener.event(puzzTouched);
				return;
			}	
		}
		
		public void draw(MyGLRenderer r){
			super.draw(r);
			for(Widget p: list){
				p.draw(r);
			}
			if(period == DrawPeriod.DURING)
				mGrid2.draw(r);
		}
		
	}
	
	class Finished_State extends StateWidget{

		List<Widget> list;
		float[] initialPositionX;
		float[] initialPositionY;
		float[] finalPosition;
		long refTime;
		boolean imageToggle;
		TextBox mBanner;
		
		public Finished_State(boolean in){
			chEnd = in;
			list = mGrid.widgetList;
			finalPosition = new float[]{1-.15f, -1*GlobalApplication.getGeometry().getGeometry()[1]+.15f};
			refTime = System.currentTimeMillis();
			initialPositionX = new float[list.size()];
			initialPositionY = new float[list.size()];
			for(int i =0;i< list.size();i++){
				initialPositionX[i] = list.get(i).getCenterX();
				initialPositionY[i] = list.get(i).getCenterY();
			}
			mBanner = new TextBox(0,0,1,"HO HO HO");
			mBanner.setFontSize(3);
			widgetList.add(mBanner);
		}
		
		@Override
		public void enterAnimation() {
			long time = (System.currentTimeMillis() - refTime);
			if(time < 500){
				for(int i =0;i< list.size(); i++){
					computeAnimation(i, time, 500);
				}
			} else{
				imageToggle = true;
				period = DrawPeriod.DURING;
			}
			
		}

		public void computeAnimation(int i, long time, long totalDuration){
			float expDec = 10.0f;	
			float t = delay(i, time, totalDuration);
			float x = initialPositionX[i] + (1-(float)Math.exp(-t/totalDuration*expDec ) )*(finalPosition[0] - initialPositionX[i]);
			float y = initialPositionY[i] + (1-(float)Math.exp(-t/totalDuration*expDec ) )*(finalPosition[1] - initialPositionY[i]);
			
			list.get(i).setCenter(x+mGrid.getCenterX(), y+mGrid.getCenterY());
		}
		
		public float delay(int i, long time, long totalDuration) {
			float div = 2.0f;
			if ( ((float)i)/((float)list.size())*((float)totalDuration)/div > time  ){
				return 0.0f;
			}
			else {
				return time - ((float)i)/((float)list.size())*((float)totalDuration)/div; 
			}
		}
		
		@Override
		public void duringAnimation() {
			// TODO Auto-generated method stub	
		}
		
		@Override
		public void swipeHandler(String direction) {
			// TODO Auto-generated method stub
		}

		@Override
		public void touchHandler(float[] pt) {
			if (chEnd) {
				chEnd = false;	
			}
			/*else {
				setState();
			}*/
		}

		public void draw(MyGLRenderer r){
			super.draw(r);
			if(imageToggle){
				//mImage.draw(r);
				if (chEnd){
					mBanner.draw(r);		
				}
			}
			else{
				for(Widget p: list){
					p.draw(r);
				}
			}
		}

	}
	
	public void setTouchListener(GameEventListener g){
		listener = g;
	}
		
	protected void computeGeometry() {
		for(int i =0;i< widgetList.size();i++){
			Widget w = widgetList.get(i);
			w.setCenter(getCenterX()+w.getRelativeCenterX(), getCenterY()+w.getRelativeCenterY());
		}
	}
	
	public void setCenter(float x, float y){
			super.setCenter(x,y);
			computeGeometry();
	}
	
	@Override
	public void draw(MyGLRenderer r) {
		//mImageBorder.draw(r);
		//mImage.draw(r);
		//mImageTop.draw(r);

		state.draw(r);

		//mText.draw(r);
		/*
		if (ch.getCompleted()) {
			finishedFlower.draw(r);
		}
		*/
	}

}
