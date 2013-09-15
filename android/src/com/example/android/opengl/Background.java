package com.example.android.opengl;


class BackgroundPlay extends State<BackgroundTile> {

	@Override
	public void enterAnimation(BackgroundTile[] tiles) {
	}

	@Override
	public void duringAnimation(BackgroundTile[] tiles) {
	}
	
}


public class Background extends Graphic<BackgroundTile> {
	
	public Background(String bg, float size) {
		state = new BackgroundPlay();
		float[] center = {0,0,0};
		tiles = new BackgroundTile[1];
		tiles[0] = new BackgroundTile(center,size,bg);
	}
	
	@Override
	public void setState(GameState s) {
	
	}

	public void setCenter(float[] center){
		tiles[0].center = center;
	}
}


