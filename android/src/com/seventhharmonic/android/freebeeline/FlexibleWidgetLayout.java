package com.seventhharmonic.android.freebeeline;

public class FlexibleWidgetLayout extends WidgetLayout{

	@Override
	protected void computeGeometry() {
		for(int i =0;i< widgetList.size();i++){
			Widget w = widgetList.get(i);
			w.setCenter(getCenterX()+w.getRelativeCenterX(), getCenterY()+w.getRelativeCenterY());
		}
	}

	@Override
	public void draw(MyGLRenderer r) {
		for(Widget w: widgetList){
			w.draw(r);
		}
	}

	@Override
	public void touchHandler(float[] pt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void swipeHandler(String direction) {
		// TODO Auto-generated method stub
		
	}
	
}
