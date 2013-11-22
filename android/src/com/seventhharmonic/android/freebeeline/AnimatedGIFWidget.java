package com.seventhharmonic.android.freebeeline;

import java.util.ArrayList;
import java.util.List;

import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
import com.seventhharmonic.com.freebeeline.levelresources.*;

import android.util.Log;


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
    
    public AnimatedGIFWidget(LevelPack LP, int kickback) {
	
	List<Chapter> chapterList;
	chapterList = LP.getAllChapters();
	int i = 0;
	for (int j =0; j<chapterList.size(); j++) {
	    Chapter ch  = chapterList.get(j);
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
    //Add the last frame as a keyframe and a kickback frame that can be optionally used for final looping.
	keyFrames.add(i-1-kickback);
	keyFrames.add(i-1);
	mImage = new ImageWidget(0, 0, 1.0f, 1.0f*height, frameList.get(keyFrames.get(LP.getCurrChapter())));
	mImage.setMode(MyGLRenderer.FIXEDWIDTH);
	mImage.setBorder(true);
    }    

    public long getSpeed() {
		return speed;
	}


	public void setSpeed(long speed) {
		this.speed = speed;
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
    
    public void setSpeedMultiplier(long speed){
    	this.speed = speed;
    }
    
    @Override
    public void swipeHandler(String direction){
	return;
    }
    
    @Override
    public void touchHandler(float[] pt){
	return;
    } 
   
    public boolean onLastFrame() {
    	/*time = System.currentTimeMillis() - refTime;
    	Log.d(TAG, Long.toString(time)+" "+Long.toString(refTime));
    	if(time > speed && )
    		return true;
    	return false;
    	*/
    	return currFrame == frameList.size()-1;
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
    
    public boolean nonTimedAnimationFinished(){
    	return currFrame == targetFrame;
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
    
    public void setCurrFrame(int i){
    	mImage.setImage(frameList.get(keyFrames.get(i)));
    	currFrame = keyFrames.get(i);
    	//targetFrame = keyFrames.get(i);
    	//Log.d(TAG,"setkeyFrame currFrame "+Integer.toString(currFrame)+" "+"targetFrame "+Integer.toString(targetFrame));
    }    
    
    public void draw(MyGLRenderer r) {
    	animate();
    	//Log.d(TAG,"draw currFrame "+Integer.toString(currFrame)+" "+"targetFrame "+Integer.toString(targetFrame));
    	mImage.draw(r);
    }
}


