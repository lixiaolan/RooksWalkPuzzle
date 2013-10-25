package com.seventhharmonic.android.freebeeline;

import java.util.ArrayList;
import java.util.List;

import com.seventhharmonic.android.freebeeline.graphics.Geometry;

public abstract class WidgetLayout extends Widget{

	List<Widget> widgetList;
	Geometry geometry;
	/*
	 * This method set's the geometry of the widgets in this class.
	 * Philosophically the geometry of the widgets should get set relative to the center of this one.
	 * Then when a setCenter command is done, you just update the widgets respectively.
	 * The center of a widget is in a local coordinate  system relative to this widget!
	 * We can now composite widget layouts!
	 */
	
	public WidgetLayout(){
		widgetList = new ArrayList<Widget>();
		setCenter(0,0);
		setRelativeCenter(0,0);
	}
	
	protected void computeGeometry() {
		for(int i =0;i< widgetList.size();i++){
			Widget w = widgetList.get(i);
			w.setCenter(getCenterX()+w.getRelativeCenterX(), getCenterY()+w.getRelativeCenterY());
		}
	}
	
	public void addWidget(Widget w){
		widgetList.add(w);
		computeGeometry();
	}
	
	public Widget getWidget(int i){
		return widgetList.get(i);
	}
	

}
