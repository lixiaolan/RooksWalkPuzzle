package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.graphics.Geometry;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
import com.seventhharmonic.android.freebeeline.listeners.GameEventListener;
import com.seventhharmonic.com.freebeeline.levelresources.Puzzle;

public abstract class EndGameDialogWidgetLayout extends WidgetLayout{

    boolean draw  = false;
    Model mModel;
    
    
    public EndGameDialogWidgetLayout(Model m){
	mModel = m;
	}
    
    @Override
    public void touchHandler(float[] pt){
    	return;
    }
    
    public void activate(){
	draw = true;
    }
    
    public void deactivate(){
	draw = false;
    }
    
    /*@Override
    public void draw(MyGLRenderer r) {
	if(draw){
	    for(Widget w: widgetList){
	    	w.draw(r);
	    }	
	}
    }*/
    
    
    @Override
    public void swipeHandler(String direction) {
	// TODO Auto-generated method stub	
    }
    
}
