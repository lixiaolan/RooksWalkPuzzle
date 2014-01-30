package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.graphics.Geometry;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
import com.seventhharmonic.android.freebeeline.listeners.GameEventListener;
import com.seventhharmonic.com.freebeeline.levelresources.Puzzle;

public class DailyPuzzleEndGameDialogWidgetLayout extends EndGameDialogWidgetLayout{

    ButtonWidget mBack;
    boolean draw  = false;
    Puzzle currPuzzle;
    boolean firstCompleted;
    
    public DailyPuzzleEndGameDialogWidgetLayout(Model m){
    super(m);
	setWidth(.8f);
	setHeight(width/Geometry.PHI);
	setCenter(0,0);
	mBack = new ButtonWidget(0,0, .20f, .20f, TextureManager.BACK);
	mBack.setRelativeCenter(.7f*width, 0);
	
	widgetList.add(mBack);
	computeGeometry();

	float number = mModel.mBoard.mBoardBg.getHeight();	
        setCenter(0,(-1*number-GlobalApplication.getGeometry().getGeometry()[1])/2.0f);
        
	setBackClickListener(new GameEventListener() {
		public void event(int i){
		    deactivate();
		    mModel.setModelToMainMenuOpening();
		    }
	    });
	activate();
    }
    
    @Override
    public void touchHandler(float[] pt){
	mBack.touchHandler(pt);
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
