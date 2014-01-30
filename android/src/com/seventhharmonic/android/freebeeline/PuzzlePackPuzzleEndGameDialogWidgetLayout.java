package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.graphics.Geometry;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
import com.seventhharmonic.android.freebeeline.listeners.GameEventListener;
import com.seventhharmonic.com.freebeeline.levelresources.Puzzle;

public class PuzzlePackPuzzleEndGameDialogWidgetLayout extends EndGameDialogWidgetLayout{

    ButtonWidget mBack;
    ButtonWidget mNext;
    Puzzle currPuzzle;
    boolean firstCompleted;
    
    public PuzzlePackPuzzleEndGameDialogWidgetLayout(Model m){
    super(m);
	setWidth(.8f);
	setHeight(width/Geometry.PHI);
	setCenter(0,0);
	mBack = new ButtonWidget(0,0, .20f, .20f, TextureManager.BACK);
	mBack.setRelativeCenter(.7f*width, 0);
	
	mNext = new ButtonWidget(0,0, .20f, .20f, TextureManager.NEXT);
	mNext.setRelativeCenter(-.7f*width, 0);
	widgetList.add(mNext);
	widgetList.add(mBack);
	computeGeometry();

	//This also kind of sucks :( It is all data that has to be taken from Board
	/***************************************************************************************/
	currPuzzle = mModel.mBoard.currPuzzle;
	boolean beforeChCompleted = currPuzzle.getChapter().getCompleted();
	Log.d("board", "solved a puzzle and set completed to true");
	long time = System.currentTimeMillis() - mModel.mBoard.analyticsTime;
	//Need to see if puzzle was completed for analytics reasons
	if(!currPuzzle.isCompleted())
		GlobalApplication.getAnalytics().sendPuzzleFirstTimeCompleteTiming(currPuzzle.getId(), mModel.mBoard.par, time, mModel.mBoard.moves);
	else
		GlobalApplication.getAnalytics().sendPuzzleReplayedTiming(currPuzzle.getId(), mModel.mBoard.par, time, mModel.mBoard.moves);		
	//Now call something in puzzle, sets the puzzle to be completed in the DB and the associated number of moves
	currPuzzle.completePuzzleListener(mModel.mBoard.moves, true);
	//if the chapter was not completed before, but now it is 
	if(!beforeChCompleted &&  currPuzzle.getChapter().getCompleted()){
		firstCompleted = true;
	}
	/*******************************************************************************************/
	//This sucks. There should be a more direct way to get this :(
	float number = mModel.mBoard.mBoardBg.getHeight();	
    setCenter(0,(-1*number-GlobalApplication.getGeometry().getGeometry()[1])/2.0f);
	setNextClickListener(new GameEventListener(){
		public void event(int i ){
		    Log.d("Dialog","Clicked");
		    deactivate();
		    //If this is the first time you completed a chapter
		    if(firstCompleted == true) {
			mModel.setModelToChapterEnd();
		    } 
		    //If there is a next puzzle and the chapter is not finished
		    else if(currPuzzle.getNextPuzzle() != null) {
			Log.d("next puzzle", "Apparently, there is another puzzle!");
			//TODO: Created a hack here to ensure we recreate the current chapter widget
			mModel.setModelToGameOpening(currPuzzle.getNextPuzzle());
			mModel.setState(GameState.GAME_OPENING);
		    } 
		    //If you are the last puzzle in a chapter and the chapter is now completed
		    else if(currPuzzle.getNextPuzzle() == null && currPuzzle.getChapter().getCompleted()) {
			mModel.setModelToChapterEnd();
		    } 
		    //If you are the last puzzle in a chapter and the chapter is not completed.
		    else {
			mModel.setModelToChapterSelect();
		    }
		}
		
	    });
	
	setBackClickListener(new GameEventListener() {
		public void event(int i){
		    deactivate();
		    if(currPuzzle.getNextPuzzle() == null){
			mModel.setModelToChapterEnd();
		    }
		    else{
			mModel.setModelToChapterSelect();
		    }
		}
		
	    });
	
	activate();
    }
    
    @Override
    public void touchHandler(float[] pt){
	mNext.touchHandler(pt);
	mBack.touchHandler(pt);
    }
    
    public void setNextClickListener(GameEventListener listener){
	mNext.setClickListener(listener);
    }
    
    public void setBackClickListener(GameEventListener listener){
	mBack.setClickListener(listener);
    }
    
    public void activate(){
	draw = true;
    }
    
    public void deactivate(){
	draw = false;
    }
    
    @Override
    public void draw(MyGLRenderer r) {
	if(draw){
	    for(Widget w: widgetList){
	    	w.draw(r);
	    }	
	}
    }
    
    
    @Override
    public void swipeHandler(String direction) {
	// TODO Auto-generated method stub
	
    }
    
}
