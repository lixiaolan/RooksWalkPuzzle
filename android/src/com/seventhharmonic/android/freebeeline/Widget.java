package com.seventhharmonic.android.freebeeline;
/*
 * This is an abstract base class for a Widget. The center here is relative to screen coordinates!
 *  Relative center is relative to a more global layout.
 *  Not sure what the right way to organize this is - I think we might insist that every widget should be 
 *  stored in a layout. 
 */


/*
 * Every widget has an absolute center and a relative center. The absolute center
 * is computed in Widget layout and passed in. The relative center is specified by the user
 * You have to override the setCenter method to pass down the center data to your own squares.
 * 
 */


public abstract class Widget {

	float width;
	float height;
	float[] center = new float[3];
	float[] relativeCenter  = new float[3];
	
	public Widget(){
		center[2] = 0;
		relativeCenter[2] = 0; 
	}
	
	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float[] getCenter() {
		return center;
	}

	public void setCenter(float[] center) {
		this.center = center;
	}
	
	public float getCenterX() {
		return center[0];
	}
	
	public float getCenterY(){
		return center[1];
	}

	public void setCenter(float center0, float center1) {
		this.center[0] = center0;
		this.center[1] = center1;
		this.center[2] = 0.0f;
	}
	
	/*
	* Relative center, is a private notion that can be used by a WidgetLayout to organize position.
	* Actial center is where things get drawn.
	*
	*/
	protected float[] getRelativeCenter() {
		return relativeCenter;
	}

	protected void setRelativeCenter(float[] center) {
		this.relativeCenter = center;
	}
	
	protected float getRelativeCenterX() {
		return relativeCenter[0];
	}
	
	protected float getRelativeCenterY(){
		return relativeCenter[1];
	}

	protected void setRelativeCenter(float center0, float center1) {
		this.relativeCenter[0] = center0;
		this.relativeCenter[1] = center1;
		this.relativeCenter[2] = 0.0f;
	}
	
		
	public abstract void draw(MyGLRenderer r);
		
	public void touchHandler(float[] pt){};
	
	public void swipeHandler(String direction){};
	
	public boolean isTouched(float[] pt){
		System.out.println("Widget Touched");
		boolean b = ((pt[0] < center[0]+width)&(pt[0] > center[0]-width)&(pt[1] < center[1]+height)&(pt[1] > center[1]-height));
    	if(b){
    		pt[0] = center[0];
    		pt[1] = center[1];
    	}
    	System.out.println(b);
    	return b;
		
	}	
	
	public void setBorder(boolean border){}
	
	public void setColor(String color){} 
	
}
