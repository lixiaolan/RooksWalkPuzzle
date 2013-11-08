package com.seventhharmonic.android.freebeeline;

import android.util.Log;

/**
 * @author jain
 *
 */
/**
 * @author jain
 *
 */
/**
 * @author jain
 *
 */
/**
 * @author jain
 *
 */
/**
 * @author jain
 *
 */
class ScreenSlideWidgetLayout extends WidgetLayout{
	String TAG = "SSW";
	int activeWidget = 0;
	int length;
	long refTime = 0;
	String currDirection;
	boolean active = false;

	//By default, you are moving the whole screen.
	float gap = 2.5f;
	float size;
	CircleProgressBarWidget mCPW;
	boolean displayProgressBar = true;
	boolean displayArrows = true;

	ImageWidget leftArrow;
	ImageWidget rightArrow;

	/**
	 * @param gap The gap between widgets.  
	 */
	public ScreenSlideWidgetLayout(float gap) {
		this.gap = gap;
		leftArrow = new ImageWidget(.7f, -1.0f, .10f, .10f, TextureManager.LWEDGE);
		rightArrow = new ImageWidget(-.7f, -1.0f, .10f, .10f, TextureManager.RWEDGE);
	}

	@Override
	public boolean isTouched(float[] pt){
		return widgetList.get(activeWidget).isTouched(pt);
	}


	public int getActiveWidget(){
		return activeWidget;
	}


	/**
	 * Toggle the circular progress bar.
	 * 
	 * @param a A boolean value to toggle the showing of a circular progress bar.
	 */
	public void setDrawProgressBar(boolean toggle){
		displayProgressBar = toggle;
	}

	/**
	 * Toggle the navigational arrows.
	 * 
	 * @param a A boolean value to toggle the showing the navigational arrows.
	 */
	public void setDrawArrow(boolean toggle){
		displayArrows = toggle;
	}



	@Override
	public void addWidget(Widget w){
		widgetList.add(w);
		computeGeometry();
		float h = GlobalApplication.getGeometry().getGeometry()[1];
		/* Assume that each circle is .08. This should probably be moved into 
		 * CircleProgressBarWidget.
		 */
		float xCPW = .08f*widgetList.size()+.04f*((widgetList.size() %2)-1);
		float yCPW = -1*(h-.08f);
		float[] center = {xCPW, yCPW, 0.0f};
		mCPW = new CircleProgressBarWidget(widgetList.size(), center, .05f);
		mCPW.setActiveCircle(0);
		leftArrow.setCenter(xCPW+.1f, yCPW);
		rightArrow.setCenter(xCPW-.05f*widgetList.size()+.1f, yCPW);
	}

	@Override
	public void computeGeometry() {
		for(int i =0;i< widgetList.size();i++){
			Widget w = widgetList.get(i);
			w.setRelativeCenter(-1*i*(2*size+gap), 0);
			w.setCenter(getCenterX()+w.getRelativeCenterX(), getCenterY()+w.getRelativeCenterY());
		}
	}

	@Override
	public void swipeHandler(String direction){
		System.out.println("got swiped");
		if(direction.equals("right_arrow") || direction.equals("left_arrow")){
			refTime = System.currentTimeMillis();
			currDirection = direction;
			initialize();
		}
	}

	@Override
	public void touchHandler(float[] pt){
		Log.d(TAG, "Got touched");
		if(displayArrows){
			if(leftArrow.isTouched(pt)){	
				swipeHandler("right_arrow");
			}

			if(rightArrow.isTouched(pt)){
				swipeHandler("left_arrow");
			}
		}
	}

	private void initialize(){
		if(currDirection.equals("right_arrow") && activeWidget !=0){
			activeWidget = activeWidget -1;
			active = true;
		} else if(currDirection.equals("left_arrow") && activeWidget !=widgetList.size()-1){
			activeWidget = activeWidget + 1;
			active = true;
		}
		mCPW.setActiveCircle(activeWidget);
	}

	private void animate(){
		float t = (float)(System.currentTimeMillis() -refTime)/500.0f;
		if(t < 1) {
			for(int i=0;i<widgetList.size();i++){
				float centerX = widgetList.get(i).getCenterX();
				widgetList.get(i).setCenter((1-t)*centerX + -1*t*(i-activeWidget)*(2*size+gap), 0);
			}
		} else {
			active = false;
		}
	}

	/**
	 * Set the current widget to the appropriate widget. Indexing starts at 0.
	 * 
	 * @param newActiveWidget 
	 */
	public void setActiveWidget(int newActiveWidget){
		activeWidget = newActiveWidget;
		if (mCPW == null) {
		    System.out.println("mCPW is null");
		}

		mCPW.setActiveCircle(activeWidget);
		for(int i=0; i<widgetList.size();i++){
			float centerX = widgetList.get(i).getCenterX();
			widgetList.get(i).setCenter(-1*(i-activeWidget)*(2*size+gap), 0);
		}
	}


	public void draw(MyGLRenderer r) {
		for(int i = 0 ;i < widgetList.size(); i++){
			if(active){
				animate();
			} 
			widgetList.get(i).draw(r);
		}
		if(displayProgressBar){
			mCPW.draw(r);

		}

		if(displayArrows){
			leftArrow.draw(r);
			rightArrow.draw(r);
		}

	}
}


