package com.example.android.opengl;


class BannerPlay extends State<BackgroundTile> {

	@Override
	public void enterAnimation(BackgroundTile[] tiles) {
	}

	@Override
	public void duringAnimation(BackgroundTile[] tiles) {
	}
	
}


public class Banner extends Graphic<BackgroundTile, State<BackgroundTile>> {
	
	public Banner(String bg, float size) {
		state = new BackgroundPlay();
		float[] center = {0,0,0};
		tiles = new BackgroundTile[1];
		tiles[0] = new BackgroundTile(center,size,bg);
	}
	
	public Banner(float size) {
		state = new BackgroundPlay();
		float[] center = {0,0,0};
		tiles = new BackgroundTile[1];
		tiles[0] = new BackgroundTile(center,size);
	}
	
	@Override
	public void setState(GameState s) {
	
	}

	public void setCenter(float a, float b){
		tiles[0].center[0] = a;
		tiles[0].center[1] = b;
	}
	
	public void set(String string) {
		tiles[0].setTextures(TextureManager.CLEAR, string);
	}
	
	
}


