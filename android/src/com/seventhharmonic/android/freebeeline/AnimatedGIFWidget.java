package com.seventhharmonic.android.freebeeline;

import java.util.ArrayList;
import java.util.List;

import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
import com.seventhharmonic.com.freebeeline.levelresources.*;

import android.util.Log;

/**
 **
 * @author jain-a-tron
 *
 */
class AnimatedGIFWidget extends Widget{

    //This is the frame the widget is animating towards
    int targetFrame;
    
    //This is the frame that the widget is currently showing
    int currFrame;

    String TAG = "GIF";
    int length;
    long time;	
    long refTime = 0;
    long speed = 250;

    List<Integer> keyFrames = new ArrayList<Integer>();
    List<String> frameList = new ArrayList<String>();
    
    //This is the image being drawn.
    ImageWidget mImage;
    ImageWidget mImageBorder;
    
    float height = GlobalApplication.getGeometry().getGeometry()[1];
    
    public AnimatedGIFWidget(LevelPack LP) {

	List<Chapter> chapterList;
	chapterList = LP.getAllChapters();
	int i = 0;
	for (Chapter ch : chapterList) {
	    keyFrames.add(i);
	    if (ch.getCompleted()) {
		for (String s : ch.getAfterCompletionImageList()) {
		    frameList.add(s);
		    i++;
		}
	    }
	    else {
		for (String s : ch.getBeforeCompletionImageList()) {
			Log.d(TAG,s);
		    frameList.add(s);
		    i++;
		}
	    }
	}
		mImage = new ImageWidget(0, 0.05f, .9f, .9f*height, frameList.get(keyFrames.get(LP.getCurrChapter())));
		mImage.setMode(MyGLRenderer.FIXEDWIDTH);
		mImage.setBorder(true);
    }    
    
    public void setTargetFrame(int i) {
	if ( i < 0 || i >= keyFrames.size()) {
	    targetFrame = 0;
	    Log.d(TAG, "Index out of bounds in setTargetFrame");
	}
	else {
	    targetFrame = keyFrames.get(i);
	}
	//Log.d(TAG,"setTargetFrame currFrame "+Integer.toString(currFrame)+" "+"targetFrame "+Integer.toString(targetFrame));
	refTime = System.currentTimeMillis();
    }

    @Override
    public void swipeHandler(String direction){
	return;
    }
    
    @Override
    public void touchHandler(float[] pt){
	return;
    }
    
    protected void animate(){
	time = System.currentTimeMillis() - refTime;
    if (time > speed) {
	    if (currFrame < targetFrame) {
		currFrame++;
		setFrame(currFrame);
		refTime = System.currentTimeMillis();
	    }
	    else if (currFrame > targetFrame) {
		currFrame--;
		setFrame(currFrame);
		refTime = System.currentTimeMillis();
	    } else if(currFrame == targetFrame){
	    	refTime = System.currentTimeMillis();
	    }
	}
    }
    
    public boolean animationFinished(){
    	time = System.currentTimeMillis() - refTime;
    	if(time > speed && currFrame == targetFrame)
    		return true;
    	return false;
    }
    
    protected void setFrame(int i) {
    	mImage.setImage(frameList.get(i));
    }

    //Set the whole widget to a certain frame.
    public void setKeyFrame(int i){
    	mImage.setImage(frameList.get(keyFrames.get(i)));
    	currFrame = keyFrames.get(i);
    	targetFrame = keyFrames.get(i);
    	//Log.d(TAG,"setkeyFrame currFrame "+Integer.toString(currFrame)+" "+"targetFrame "+Integer.toString(targetFrame));
    }    
    
    public void draw(MyGLRenderer r) {
    	animate();
    	mImage.draw(r);
	}
}


