package com.seventhharmonic.android.freebeeline;



class ScreenSlideWidgetLayout extends WidgetLayout{

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
    
    
    public ScreenSlideWidgetLayout(float gap) {
    	this.gap = gap;  
    	//mCPW.setActiveCircle(0);
    }
    
    @Override
    public boolean isTouched(float[] pt){
    	return widgetList.get(activeWidget).isTouched(pt);
    }
    
    public int getActiveWidget(){
    	return activeWidget;
    }
    
    public void setDrawProgressBar(boolean a){
    	displayProgressBar = a;
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
    	mCPW = new CircleProgressBarWidget(widgetList.size(), center, .08f);
    	mCPW.setActiveCircle(0);
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
    	}
}


