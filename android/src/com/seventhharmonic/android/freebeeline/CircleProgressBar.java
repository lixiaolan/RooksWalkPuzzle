package com.seventhharmonic.android.freebeeline;

public class CircleProgressBar {

	CircleTile[] circleTiles;
	private int activeCircle;
	float[] tilesCenter = new float[3];
	float tilesSize;
	int length;

	public CircleProgressBar(int length, float[] center, float size) {
		this.length = length;
		tilesSize = size;	
		tilesCenter = center;
		circleTiles = new CircleTile[length];
		for(int i = 0;i< length;i++){
			float[] centerp = new float[3];
			centerp[0] = tilesCenter[0]-2*tilesSize*((float)i);
			centerp[1] = tilesCenter[1];
			centerp[2] = 0.0f;
			circleTiles[i] = new CircleTile(centerp, tilesSize);	
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
			for (int i = 0; i < circleTiles.length; i++) {
				circleTiles[i].draw(r);
			}
		}
	}

