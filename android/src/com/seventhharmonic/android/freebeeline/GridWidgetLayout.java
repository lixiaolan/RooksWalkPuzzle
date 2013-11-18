package com.seventhharmonic.android.freebeeline;

import android.util.Log;

public class GridWidgetLayout extends WidgetLayout{
	String TAG = "GridWidgetList";
	int width;
	int height;
	float size = .125f;
	float[] xcoords;
	float[] ycoords;
	
	
	
	public GridWidgetLayout(int width, int height, float size){
		this.width = width;
		this.height = height;
		this.size = size;
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
	public void setCenter(float x, float y){
		super.setCenter(x, y);
		computeGeometry();
	}
	
	public void addWidget(Widget w){
		widgetList.add(w);
		computeGeometry();
	}
	
	@Override
	protected void computeGeometry() {
		//Log.d("Grid", "Coords "+ Float.toString(getCenterX())+" "+Float.toString(getCenterY()));
		for(int i =0; i< Math.min(widgetList.size(), width*height); i++){
			Widget w = widgetList.get(i);
			w.setRelativeCenter(xcoords[i], ycoords[i]);
			w.setCenter(getCenterX()+w.getRelativeCenterX(), getCenterY()+w.getRelativeCenterY());
		}
	}

	/**
	 * Unlike the rest of the program, we are taking the care here to perform a rotation
	 * Hence index 0 is a the upper left. 
	 */
	public void initiateGeometry(){
		xcoords = new float[width*height];
		ycoords = new float[width*height];
		int length = width*height;
		Log.d(TAG, "Width height ");
		Log.d(TAG, Integer.toString(width));
		for(int i =0;i< length; i++){
			xcoords[i] = -( (i%width) - ((float)width-1.0f)/2)/(1/(2*size));
			//Special case for height of 1. Not sure how to handle this better.
			if(height == 1)
				ycoords[i] = -( - ((float)height-1.0f)/2 )/(1/(size*2));
			else
				ycoords[i] = -( (i/width) - ((float)height-1.0f)/2 )/(1/(size*2));
		}
	}
	
	@Override
	public void draw(MyGLRenderer r) {
		computeGeometry();
		for(int i =0;i< Math.min(widgetList.size(), width*height) ; i++){
			widgetList.get(i).draw(r);
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
