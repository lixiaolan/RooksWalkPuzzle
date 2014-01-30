package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.graphics.Geometry;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
import com.seventhharmonic.android.freebeeline.listeners.GameEventListener;
import com.seventhharmonic.com.freebeeline.levelresources.Puzzle;

public class EndGameDialogWidgetLayout extends WidgetLayout{

    ButtonWidget mBack;
    ButtonWidget mNext;
    boolean draw  = false;
    Model mModel;
    Puzzle currPuzzle;
    boolean firstCompleted;
    
    public EndGameDialogWidgetLayout(Model m){
	mModel = m;
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

	//This also kind of sucks :(
	currPuzzle = mModel.mBoard.currPuzzle;
	firstCompleted = mModel.mBoard.firstCompleted;
	//This sucks. There should be a more direct way to get this :(
	float number = mModel.mBoard.mBoardBg.getHeight();	
        setCenter(0,(-1*number-geometry.getGeometry()[1])/2.0f);
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
