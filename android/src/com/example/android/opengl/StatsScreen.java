package com.example.android.opengl;

public class StatsScreen extends Graphic<BackgroundTile, State<BackgroundTile>>{

	
	float[] geometry;
	GlobalState mGlobalState;
	
	public StatsScreen(GlobalState g){
		tiles = new BackgroundTile[5];
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
		tiles[0].setTextures(TextureManager.TOTALLINES, TextureManager.CLEAR);
		
		//Short
		center[0] = 0; center[1] = .4f;
		tiles[1] = new BackgroundTile(center, scale);
		tiles[1].setTextures(TextureManager.SHORTSTATS, TextureManager.CLEAR);
		
		
		//Medium
		
		center[0] = 0; center[1] = 0f;
		tiles[2] = new BackgroundTile(center, scale);
		tiles[2].setTextures(TextureManager.MEDIUMSTATS, TextureManager.CLEAR);
	
		
		//Long
		center[0] = 0; center[1] = -.4f;
		tiles[3] = new BackgroundTile(center, scale);
		tiles[3].setTextures(TextureManager.LONGERSTATS, TextureManager.CLEAR);
		
		
		//Longest
		center[0] = 0; center[1] = -1f;
		tiles[4] = new BackgroundTile(center, scale);
		tiles[4].setTextures(TextureManager.LONGESTSTATS, TextureManager.CLEAR);
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
