package com.seventhharmonic.android.freebeeline;

public class StaticScreenSlideWidgetLayout extends ScreenSlideWidgetLayout{

	public StaticScreenSlideWidgetLayout(float gap) {
		super(gap);
	}

	@Override 
	public void draw(MyGLRenderer r){
		widgetList.get(activeWidget).setCenter(0, 0);
		widgetList.get(activeWidget).draw(r);
	}
	
}
