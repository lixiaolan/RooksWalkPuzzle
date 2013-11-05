package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.State.DrawPeriod;
import com.seventhharmonic.android.freebeeline.util.LATools;
import com.seventhharmonic.com.freebeeline.levelresources.LevelPack;
import com.seventhharmonic.com.freebeeline.levelresources.LevelPackProvider;



class FlowerMenu extends GraphicWidget implements BeeBoardInterface{
	enum FlowerState {
		MAIN_MENU, DEFAULT
	}

	private String TAG = "FlowerMenu";
	
	private FlowerState mFlowerState;
	private CircleProgressBarWidget mCPB;
	private Bee mBee;
	private ImageWidget mBanner;
	private LevelPack currLevelPack;
	private int currLevelPackIndex;
	private LevelPackProvider LPP = GlobalApplication.getLevelPackProvider();
	protected float flowerSize = .15f;
	private static int lpCount; 
	private static final int flowerCount = 25; 
	private long lastTouchTime;
	private float[] lastTouchPos = new float[2];
	private FlowerTile[] tiles = new FlowerTile[flowerCount];
	Model mModel;
	
	public FlowerMenu(Model model) {

		mModel = model;
		float[] center = {.20f,-1.0f, 0.0f };
		//mBee = new Bee(this);
		mCPB = new CircleProgressBarWidget(lpCount, center, .05f);
		//mBee.setState(GameState.MAIN_MENU_OPENING, TutorialInfo2.length);
		mFlowerState = FlowerState.MAIN_MENU;
		currLevelPackIndex = -1;
		mBanner = new ImageWidget(0,0,.4f,.4f,TextureManager.LP1);
		mBanner.setColor("white");
		mBanner.setBackground(TextureManager.BOX);
		for(int i =0;i<tiles.length;i++){
			double r = Math.random();
			float Sx = (float)(-1.5*r+(1-r)*1.5);
			r = Math.random();
			float Sy = (float)(-1.5*r+(1-r)*1.5);
			tiles[i] = new FlowerTile(Sx,Sy, flowerSize);
		}
		state = new MainMenu();
		lpCount = LPP.getNumberOfLevelPacks();

	}

	public void setGeometry(float[] g){
		super.setGeometry(g);
	}

	/* 
	 * This class reads the physics style and pack title from the Level
	 * Pack Provider class. Then it updates its state accordingly.
	 */    

	public void setState(int index) {
		Log.d(TAG, "index"+Integer.toString(index));
		Log.d(TAG, "lpCount"+Integer.toString(lpCount));
		
		currLevelPackIndex = index;
		if(index == -1){
			state = new MainMenu();
			mFlowerState = FlowerState.MAIN_MENU;
			mModel.setState(GameState.MAIN_MENU_OPENING);
			return;
		}

		currLevelPack = LPP.getLevelPack(index);
		//TODO: This sucks. The problem is that LPCount may not exist yet!
		mCPB.setLength(lpCount);
		mCPB.setActiveCircle(index);
		if(currLevelPack.getStyle() == "default"){
			state = new LevelPack1();
			mFlowerState = FlowerState.DEFAULT;
			mModel.setState(GameState.FLOWER_MENU);
		}
	}





	/* (non-Javadoc) Swipe Handler for this class.
	 * @see com.seventhharmonic.android.freebeeline.GraphicWidget#swipeHandler(java.lang.String)
	 */
	public void swipeHandler(String direction) {
		lpCount = LPP.getNumberOfLevelPacks();
		if(direction.equals("left_arrow")){
			setState(Math.min(currLevelPackIndex+1, lpCount-1));
		} else if(direction.equals("right_arrow")){
			setState(Math.max(currLevelPackIndex-1, -1));
		}
	}

	public void touchHandler(float[] pt) {
		/*TODO: This needs to get changed to potentiall trigger Model. Not sure whether to do it here or
		 * in the states.  
		 */
		state.touchHandler(pt);
	}



	/**
	 * MainMenu state for FlowerMenu.
	 * @author jain
	 *
	 */
	class MainMenu extends StateWidget {
		long refTime;
		float[] centers;
		
		public MainMenu() {
			//mBee.setMood(Mood.HAPPY);
			//Initialize tiles to have a random velocity.
			centers = new float[2*tiles.length];
			for (int i = 0; i < tiles.length; i++) {
				double r = Math.random();
				tiles[i].velocity[0] = (float)(-1*r+(1-r)*1);
				r = Math.random();
				tiles[i].velocity[1] = (float)(-1*r+(1-r)*1);
				tiles[i].setTextures(TextureManager.CLEAR, TextureManager.getFlowerTexture());
				tiles[i].setSize(flowerSize);
			}

			//Initiate the centers array.
			for (int i = 0; i<tiles.length; i++ ) {
				float ii = (float)i;
				float r = (ii + 10*(1-1/(ii+1)))/25;
				float t = ii/1.5f; 
				centers[2*i] = -.75f + r*((float)Math.sin(t));
				centers[2*i+1] = r*((float)Math.cos(t));
			}
		}
		@Override
		public void enterAnimation() {
			period = DrawPeriod.DURING;
			refTime = System.currentTimeMillis();
		}

		float[] force = new float[2];
		@Override
		public void duringAnimation() {
			long time = System.currentTimeMillis()-refTime;
			float dt = Math.min(((float)time)/1000.0f, 33.3f);
			refTime = System.currentTimeMillis();
			for(int i=0;i<tiles.length;i++){
				getForce(tiles, i);
				LATools.vSProd(dt, tiles[i].velocity, temp);
				LATools.vSum(tiles[i].getCenter(), temp ,tiles[i].getCenter());
				LATools.vSProd(dt, force, temp);
				LATools.vSum(tiles[i].velocity, temp,tiles[i].velocity);
				//LATools.vSum(tiles[i].getCenter2D(), LATools.vSProd(dt, tiles[i].velocity),tiles[i].getCenter());
				//LATools.vSum(tiles[i].velocity, LATools.vSProd(dt, force),tiles[i].velocity);
			}
		}

		float[] temp = new float[2];
		float[] mid = new float[3];
		
		public void getForce(FlowerTile[] tiles, int i) {
			mid[0] = centers[2*i]; mid[1] = centers[2*i+1]; 
			LATools.vDiff(tiles[i].center, mid, temp);
			LATools.vSProd(-2.0f,temp, force); 
			LATools.vSProd(-1.2f,tiles[i].velocity, temp);
			LATools.vSum(force, temp, force);
			//Compute wave of forces due to touch
			float time = 2*(System.currentTimeMillis()-lastTouchTime)/1000f;
			LATools.vDiff(tiles[i].center, lastTouchPos, temp);
			float sTemp = LATools.abs(temp);
			if (sTemp<time && sTemp > time - .2f && sTemp > .00001f){
				LATools.vSProd(5f/((float)Math.pow(sTemp,1)), temp, temp);
				LATools.vSum(force, temp, force);
			}
		}


		@Override	    
		public void draw(MyGLRenderer r){	   
			super.draw(r);
			//mBee.draw(r);
			for(int i =0;i< tiles.length;i++){
				tiles[i].draw(r);
			}
		}

		@Override
		public void swipeHandler(String direction) {
			return;	
		}

		@Override
		public void touchHandler(float[] pt) {
			lastTouchPos = pt;
			lastTouchTime = System.currentTimeMillis();
		}
	}

	class LevelPack1 extends StateWidget {
		long refTime;
		float[] centers;
		
		public LevelPack1() {
			//mBee.setMood(Mood.HAPPY);
			//Initialize tiles to have a random velocity.
			centers = new float[2*tiles.length];
			for (int i = 0; i < tiles.length; i++) {
				double r = Math.random();
				tiles[i].velocity[0] = (float)(-1*r+(1-r)*1);
				r = Math.random();
				tiles[i].velocity[1] = (float)(-1*r+(1-r)*1);
				tiles[i].setTextures(TextureManager.CLEAR, TextureManager.getFlowerTexture());
				tiles[i].setSize(flowerSize);
			}

			//Initiate the centers array.
			for (int i = 0; i<tiles.length; i++ ) {
				float ii = (float)i;
				float r = (ii + 10*(1-1/(ii+1)))/25;
				float t = ii/1.0f; 
				centers[2*i] =  r*((float)Math.sin(t));
				centers[2*i+1] = r*((float)Math.cos(t));
			}
		}

		@Override
		public void enterAnimation() {
			period = DrawPeriod.DURING;
			refTime = System.currentTimeMillis();
		}

		
		float[] force = new float[2];
		@Override
		public void duringAnimation() {
			long time = System.currentTimeMillis()-refTime;
			float dt = Math.min(((float)time)/1000.0f, 33.3f);
			refTime = System.currentTimeMillis();
			for(int i=0;i<tiles.length;i++){
				getForce(tiles, i);
				LATools.vSProd(dt, tiles[i].velocity, temp);
				LATools.vSum(tiles[i].getCenter(), temp ,tiles[i].getCenter());
				LATools.vSProd(dt, force, temp);
				LATools.vSum(tiles[i].velocity, temp,tiles[i].velocity);
			}

		}

		float[] temp = new float[2];
		float[] mid = new float[3];
		
		public void getForce(FlowerTile[] tiles, int i) {
			mid[0] = centers[2*i]; mid[1] = centers[2*i+1]; 
			LATools.vDiff(tiles[i].center, mid, temp);
			LATools.vSProd(-2.0f,temp, force); 
			LATools.vSProd(-1.2f,tiles[i].velocity, temp);
			LATools.vSum(force, temp, force);
			//Compute wave of forces due to touch
			float time = 2*(System.currentTimeMillis()-lastTouchTime)/1000f;
			LATools.vDiff(tiles[i].center, lastTouchPos, temp);
			float sTemp = LATools.abs(temp);
			if (sTemp<time && sTemp > time - .2f && sTemp > .00001f){
				LATools.vSProd(5f/((float)Math.pow(sTemp,1)), temp, temp);
				LATools.vSum(force, temp, force);
			}
		}


		@Override	    
		public void draw(MyGLRenderer r){	   
			super.draw(r);
			//mBee.draw(r);
			mCPB.draw(r);
			for(int i =0;i< tiles.length;i++){
				tiles[i].draw(r);
			}
			mBanner.draw(r);

		}

		@Override
		public void swipeHandler(String direction) {
			return;	
		}

		@Override
		public void touchHandler(float[] pt) {
			lastTouchPos = pt;
			lastTouchTime = System.currentTimeMillis();
			mModel.setState(GameState.TABLE_OF_CONTENTS);
			mModel.toc.currLevelPack = currLevelPack;
			mModel.toc.setState();
		}

	}

	

	@Override
	public BoardTile getTile(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBoardHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBoardWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPathLength() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setTileRotate(int i) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getPathToArray(int i) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setState() {
		// TODO Auto-generated method stub

	}    


}
