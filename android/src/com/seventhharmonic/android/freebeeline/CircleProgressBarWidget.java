package com.seventhharmonic.android.freebeeline;

public class CircleProgressBarWidget extends Widget {

	CircleTile[] circleTiles;
	GridWidgetLayout g;
	
	private int activeCircle;
	float[] tilesCenter = new float[3];
	float tilesSize;
	int length;

	
	public CircleProgressBarWidget(int length, float x, float y, float size) {
		this.length = length;
		tilesSize = size;	
		tilesCenter = center;
		circleTiles = new CircleTile[length];
		g = new GridWidgetLayout(length, 1, size);
		g.setCenter(x, y);
		for(int i = 0;i< length;i++){
			circleTiles[i] = new CircleTile(tilesSize);	
			g.addWidget(circleTiles[i]);
		}
	}
	
	public void setActiveCircle(int a){
		for(int i = 0;i<length;i++ ){
			if(i == a) {
				circleTiles[i].setFull();
			} else{
			circleTiles[i].setEmpty();
			}
		}
	}

	public void draw(MyGLRenderer r) {
		/*	for (int i = 0; i < circleTiles.length; i++) {
				circleTiles[i].draw(r);
			}
		*/
		g.draw(r);
		}

	@Override
	public void touchHandler(float[] pt) {
		for(int i =0;i<circleTiles.length;i++){
			if(circleTiles[i].isTouched(pt)){
				setActiveCircle(i);
				return;
			}
		}
	}
	
	@Override
	public void swipeHandler(String direction) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	public void setLength(int l){
		this.length = l;
		circleTiles = new CircleTile[length];
		for(int i = 0;i< length;i++){
			float[] centerp = new float[3];
			centerp[0] = tilesCenter[0]-2*tilesSize*((float)i);
			centerp[1] = tilesCenter[1];
			centerp[2] = 0.0f;
			//circleTiles[i] = new CircleTile(centerp, tilesSize);	
			
		}
	}*/


}

