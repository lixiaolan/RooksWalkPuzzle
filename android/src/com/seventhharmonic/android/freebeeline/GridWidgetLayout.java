package com.seventhharmonic.android.freebeeline;

import android.util.Log;

public class GridWidgetLayout extends WidgetLayout{
	String TAG = "GridWidgetList";
	int width;
	int height;
	float[] xcoords;
	float[] ycoords;
	
	public GridWidgetLayout(int width, int height){
		this.width = width;
		this.height = height;
		initiateGeometry();
	}
	
	public int widgetTouched(float[] pt){
		for(int i =0;i<widgetList.size();i++){
			if(widgetList.get(i).isTouched(pt)){
				return i;
			}
		}
		return -1;
	}
		
	@Override
	protected void computeGeometry() {
		for(int i =0; i< Math.min(widgetList.size(), width*height); i++){
			Widget w = widgetList.get(i);
			w.setRelativeCenter(xcoords[i], ycoords[i]);
			w.setCenter(getCenterX()+w.getRelativeCenterX(), getCenterY()+w.getRelativeCenterY());
		}
	}

	public void initiateGeometry(){
		xcoords = new float[width*height];
		ycoords = new float[width*height];
		int length = width*height;
		Log.d(TAG, "Width height ");
		Log.d(TAG, Integer.toString(width));
		// Unlike the rest of the program, we are taking the care here to perform a rotation
		// Hence index 0 is a the upper left.
		for(int i =0;i< length; i++){
			xcoords[i] = -( (i%width) - ((float)width-1.0f)/2 )/4.0f;
			ycoords[i] = -( (i/height) - ((float)height-1.0f)/2 )/4.0f;
		}
	}
	
	@Override
	public void draw(MyGLRenderer r) {
		computeGeometry();
		for(int i =0;i< Math.min(widgetList.size(), width*height) ; i++){
			widgetList.get(i).draw(r);
		}
	}
	
}
