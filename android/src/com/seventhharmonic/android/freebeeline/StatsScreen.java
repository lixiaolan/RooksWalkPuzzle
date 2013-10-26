package com.seventhharmonic.android.freebeeline;

public class StatsScreen extends Graphic<BackgroundTile, State<BackgroundTile>>{

	
	float[] geometry;
	GlobalState mGlobalState;
	
	public StatsScreen(GlobalState g){
		tiles = new BackgroundTile[6];
		state = new StaticScreen(tiles);
		mGlobalState = g;
		populateTiles();
	}

	@Override
	public void setState(GameState s) {
	}
	
	public void setGeometry(float[] geometry){
		this.geometry = geometry;
	}
	
	public void populateTiles() {
		float[] center = new float[3];
		center[2] = 0.0f;
		float scale  = .5f;
		
		//Title of this screen
		center[0] = 0; center[1] = 1f;
		tiles[0] = new BackgroundTile(center, scale);
		tiles[0].setTextures(TextureManager.TOTALGAMES, TextureManager.CLEAR);
		
		//Short
		float[] centerShort = {0,.4f,0};
		tiles[1] = new BackgroundTile(centerShort, scale);
		tiles[1].setTextures(TextureManager.SHORTSTATS, TextureManager.CLEAR);
		
		
		//Medium
		float[] centerMedium = {0,0f,0};
		tiles[2] = new BackgroundTile(centerMedium, scale);
		tiles[2].setTextures(TextureManager.MEDIUMSTATS, TextureManager.CLEAR);
	
		
		//Long		
		float[] centerLonger = {0,-.4f,0};
		tiles[3] = new BackgroundTile(centerLonger, scale);
		tiles[3].setTextures(TextureManager.LONGERSTATS, TextureManager.CLEAR);
		
		
		//Longest
		float[] centerLongest = {0,-.8f,0};
		tiles[4] = new BackgroundTile(centerLongest, scale);
		tiles[4].setTextures(TextureManager.LONGESTSTATS, TextureManager.CLEAR);
		
		float[] centerFlower = {0,-1.2f,0};
		tiles[5] = new BackgroundTile(centerFlower, scale);
		tiles[5].setTextures(TextureManager.FLOWERSVISITED, TextureManager.CLEAR);
	}
	
	/*@Override 
	public void draw(MyGLRenderer r){
		//if(mGlobalState.textureCreationFinished) {
			state.draw(tiles, r);
		//}
	}*/
	
	
	class StaticScreen extends State<BackgroundTile> {

		
		public StaticScreen(BackgroundTile[] tiles){
		}
		
		@Override
		public void enterAnimation(BackgroundTile[] tiles) {
		}

		@Override
		public void duringAnimation(BackgroundTile[] tiles) {
		}
		
	}
	
	
	

}
