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

	public void setCenter(float[] center){
		tiles[0].center = center;
	}
	
	public void set(String string) {
		tiles[0].setTextures(TextureManager.CLEAR, string);
	}
	
	public boolean touched(float[] pt){
		for (int i = 0; i < tiles.length; i++) {
			if( tiles[i].touched(pt)) {
				return true;
			}
		}
		return false;
	}
}


