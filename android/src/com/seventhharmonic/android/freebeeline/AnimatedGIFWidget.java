package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.android.freebeeline.graphics.TextureManager;

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
    int activeWidget = 0;
    int length;
    long refTime = 0;
    long speed = 334;

    List<Integer> keyFrames = new ArrayList<Integer>();
    List<String> frameList = new ArrayList<String>();

    //This is the image being drawn.
    ImageWidget mImage;

    float height = GlobalApplication.getGeometry().getGeometry()[1];
    
    public AnimatedGIFWidget(LevelPack LP) {

	List<Chapter> chapterList;
	chapterList = LP.getAllChapters();
	int i = 0;
	for (Chapter ch : chapterList) {
	    keyFrames.put(i);
	    if (ch.getCompleted()) {
		for (String s : ch.getAfterCompletedImageList()) {
		    frameList.put(s);
		    i++;
		}
	    }
	    else {
		for (String s : ch.getBeforeCompletedImageList()) {
		    frameList.put(s);
		    i++;
		}
	    }
	}
	mImage = new ImageWidget(0, 0.05f, .9f, .9f*height, frameList.get(keyFrame.get(LP.getCurrChapter())));
    }    
    
    public void setTargetFrame(int i) {
	if ( i < 0 || i >= keyFrames.size()) {
	    targetFrame = 0;
	    Log.d(TAG, "Index out of bounds in setTargetFrame");
	}
	else {
	    targetFrame = keyFrames.get(i);
	}
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
	if (refTime > speed) {
	    if (currFrame < targetFrame) {
		currFrame++;
		setFrame(currFrame);
		refTime = 0;
	    }
	    else if (currFrame > targetFrame) {
		currFrame--;
		setFrame(currFrame);
		refTime = 0;
	    }
	}
    }
    
    protected void setFrame(int i) {
	mImage.setImage(imageList.get(i));
    }

    //Do we need this?
    public void setKeyFrame(int i){
	mImage.setImage(imageList.get(keyFrame.get(i)));
    }    
    
    public void draw(MyGLRenderer r) {
	animate();
	mImage.draw(r);
    }
}


