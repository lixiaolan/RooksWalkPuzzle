
package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.android.freebeeline.util.LATools;

public class StoryBoard extends Board{

	public enum StoryState {
	    SLIDE_0, SLIDE_1, SLIDE_2, SLIDE_3
	};

	StoryState mStoryState = StoryState.SLIDE_0;

	Bee mBee = new Bee(this);
	Banner mStoryBanner;
	Model mModel;
	
	public StoryBoard(Model mModel){
		super();
		mStoryBanner = new Banner(.6f);
		state = new SLIDE0(tiles);
		this.mModel = mModel; 
		//state = new SLIDE2(tiles);
		
	}
	
	public void setState() {
		switch(mStoryState){
			case SLIDE_0: 
				state = new SLIDE1(tiles);
				mStoryState = StoryState.SLIDE_1;
				break;
			case SLIDE_1:
				state = new SLIDE2(tiles);
				mStoryState = StoryState.SLIDE_2;
				break;
			case SLIDE_2:
				state = new SLIDE3(tiles);
				mStoryState = StoryState.SLIDE_3;
			default:
				break;
			}
	}

	public void touchHandler(float[] pt) {
		if(mStoryState == StoryState.SLIDE_3){
			mModel.reset();
		}
		setState();
	}
	
	public void draw() {}
	
	
	class SLIDE0 extends State<BoardTile>{
		
		BoardTile[] tiles;
		float[] centers;
		long refTime;
		
		public SLIDE0(BoardTile[] tiles){
			this.tiles = tiles;
			refTime = System.currentTimeMillis();
			mStoryBanner.set("story_banner_0");
			centers = new float[2*tiles.length];
		    
		    for (int i = 0; i < tiles.length; i++) {
		    	double r = Math.random();
		    	tiles[i].velocity[0] = (float)(-1*r+(1-r)*1);
		    	r = Math.random();
		    	tiles[i].velocity[1] = (float)(-1*r+(1-r)*1);
		    }
		    float[] pivot = {0.0f,0.0f,1.0f};
		    //Set textures	
		    for (int i = 0;i<tiles.length;i++){
		    	tiles[i].setTextures(TextureManager.CLEAR, tiles[i].flowerTexture);
				tiles[i].setColor("transparent");
				tiles[i].setSize(flowerSize);
				tiles[i].setAngle(0f);
				tiles[i].setPivot(pivot);
		    }
		    for (int i = 0; i<tiles.length; i++ ) {
		    	float ii = (float)i;
		    	float r = (ii)/25;
		    	float t = ii/1.5f; 
		    	centers[2*i] =  (r+0.65f)*((float)Math.sin(t));
		    	centers[2*i+1] = (r+0.65f)*((float)Math.cos(t));
		    	float[] mcenter = {centers[2*i], centers[2*i+1]}; 
		    	tiles[i].setCenter2D(mcenter);
		    }
		    mBee.setState(GameState.MAIN_MENU_OPENING);
		}
		
		public void enterAnimation(BoardTile[] tiles) {
			state.period = DrawPeriod.DURING;
		}	
		@Override
		public void duringAnimation(BoardTile[] tiles) {
			   	/*long time = System.currentTimeMillis()-refTime;
			    float dt =((float)time)/1000.0f;
			    refTime = System.currentTimeMillis();
			    float[] force = new float[2];
			    for(int i=0;i<tiles.length;i++){
			    	//force = LATools.getForce(centers, tiles, i);
			    	//tiles[i].setCenter2D(LATools.vSum(centers, tiles[i].getCenter2D(), LATools.vSProd(dt, tiles[i].velocity)));
			    	//tiles[i].velocity = LATools.vSum(centers, tiles[i].velocity, LATools.vSProd(dt, force));
			    }*/
		}
		@Override
		public void draw(BoardTile[] tiles, MyGLRenderer r){
			super.draw(tiles, r);
			mStoryBanner.draw(r);
			mBee.draw(r);
		}
		
	}
	
	class SLIDE1  extends State<BoardTile>{
		//Beatrice was a happy bee
		
		BoardTile[] tiles;
		
		public SLIDE1(BoardTile[] tiles){
			this.tiles = tiles;
			
		}
			@Override
		public void enterAnimation(BoardTile[] tiles) {
			state.period = DrawPeriod.DURING; 
			mStoryBanner.set("story_banner_1");
		}

		@Override
		public void duringAnimation(BoardTile[] tiles) {
		}
		
		public void draw(BoardTile[] tiles, MyGLRenderer r){
			super.draw(tiles, r);
			mStoryBanner.draw(r);
			mBee.draw(r);
		}
		
	}
	
	class SLIDE2  extends State<BoardTile> {
		//Beatrice was a happy bee
		
				BoardTile[] tiles;
				
				public SLIDE2(BoardTile[] tiles){
					this.tiles = tiles;
					
				}
					@Override
				public void enterAnimation(BoardTile[] tiles) {
					state.period = DrawPeriod.DURING; 
					mStoryBanner.set("story_banner_2");
				}

				@Override
				public void duringAnimation(BoardTile[] tiles) {
				}
				
				public void draw(BoardTile[] tiles, MyGLRenderer r){
					super.draw(tiles, r);
					mStoryBanner.draw(r);
					mBee.draw(r);
				}
				
	}

	class SLIDE3  extends State<BoardTile> {
		//Beatrice was a happy bee
		
				BoardTile[] tiles;
				Background mTitleBanner;
				public SLIDE3(BoardTile[] tiles){
					this.tiles = tiles;
					mTitleBanner = new Background("title", 1.0f);
				}
					@Override
				public void enterAnimation(BoardTile[] tiles) {
					state.period = DrawPeriod.DURING;
				}

				@Override
				public void duringAnimation(BoardTile[] tiles) {
				}
				
				public void draw(BoardTile[] tiles, MyGLRenderer r){
					mTitleBanner.draw(r);
					mBee.draw(r);
				}
				
	}
	
	
	class WIND1 extends State<BoardTile>{
		
		BoardTile[] tiles;
		float[] centers;
		long refTime;
		long periodTime;
		long beginTime;
		float[] tails;
		float[] swarmCenter = new float[2];
	    float[] swarmVelocity = new float[2];
	    float[] newSwarmCenter  = new float[2];
		float[] weights = new float[36];

		public WIND1(BoardTile[] tiles){
			this.tiles = tiles;
			refTime = System.currentTimeMillis();
			periodTime = refTime;
			mStoryBanner.set("story_banner_2");
			tails = new float[36*2];
			beginTime = refTime;
		}
		
		public void enterAnimation(BoardTile[] tiles) {
			centers = new float[2*tiles.length];	    
			setVelocities(.4f,.4f); 
		    setCenters(0,0);
		    mBee.setState(GameState.MAIN_MENU_OPENING, 36);
			state.period = DrawPeriod.DURING;
		}
		
		private void setCenters(float x, float y){
			for (int i = 0; i<tiles.length; i++ ) {
		    	centers[2*i] =  x;
		    	centers[2*i+1] = y;
		    }
		}
		
		private void setRandomCenters(){
			for (int i = 0; i<tiles.length; i++ ) {
				float r = (float)Math.random();
		    	centers[2*i] =  -1*r+(1-r)*1;
		    	r = (float)Math.random();
		    	centers[2*i+1] = -1*r+(1-r)*1;
		    }
		}
		
		private void setVelocities(float x, float y){
			for (int i = 0; i < tiles.length; i++) {
		    	tiles[i].velocity[0] = x;//(float)(-1*r+(1-r)*1);
		    	tiles[i].velocity[1] = y;//(float)(-1*r+(1-r)*1);
		    }
		}
		@Override
		public void duringAnimation(BoardTile[] tiles) {
				
				long currTime = System.currentTimeMillis();
			   	long time = currTime-refTime;
			    float dt =((float)time)/1000.0f;
			    refTime = System.currentTimeMillis();
			    float[] force = new float[2];
			    float[] swarmForce = new float[2];
			    
			    if(currTime - periodTime > 1000){
			    	float r = (float)Math.random();
			    	newSwarmCenter[0] = -.7f*r+(1-r)*.7f;
			    	r = (float)Math.random();
			    	newSwarmCenter[1] = -1.0f*r+(1-r)*1.0f;
			    	periodTime =  currTime;
			    	setRandomCenters();
			    }
			    
		    	swarmForce = getForce(newSwarmCenter, swarmCenter, 2f, 0);
			    swarmCenter = LATools.vSum(swarmCenter, LATools.vSProd(dt, swarmVelocity));
			    swarmVelocity = LATools.vSum(swarmVelocity, LATools.vSProd(dt, swarmForce));
			    
			    //setTails();
			    for(int i=0;i<tiles.length;i++){
			    	
			    	tiles[i].setCenter2D(LATools.vSum(tiles[i].getCenter2D(), LATools.vSProd(dt, tiles[i].velocity)));	    	
			    	float[] mid = {centers[2*i],centers[2*i+1]};
			    	force = getForceCentripetal(mid, tiles[i].getCenter2D(), tiles[i].velocity, 5.0f, -.6f);
			    	tiles[i].velocity = LATools.vSum(tiles[i].velocity, LATools.vSProd(dt, force));
			    	tiles[i].velocity = LATools.vSum(tiles[i].velocity, LATools.vSProd(dt, swarmForce));
			    }
			    
			    
			    if(currTime - beginTime  > 5000){
			    	float[] center = {-10,10};
			    	for(int i =0;i<tiles.length;i++){
			    		tiles[i].setCenter2D(center);
			    	}
			    	setState();
			    }
			    
		}
		
		public float[] getForce(float[] dest, float[] initial, float acc, float friction) {
		    float[] force = {0.0f, 0.0f};
		    force = LATools.vSProd(acc,LATools.vDiff(dest, initial)); 
		    //force = LATools.vSum(force, LATools.vSProd(friction,tiles[i].velocity));
		    return force;
		}
		
		public float[] getForceCentripetal(float[] dest, float[] initial, float[] velocity, float acc, float friction) {
		    float[] force = {0.0f, 0.0f};
		    force = LATools.vSProd(acc,LATools.vDiff(dest, initial)); 
		    float[] dist = LATools.vDiff(dest, initial);
		    float v = LATools.abs(velocity);
		    float scale = v*v/(LATools.abs(dist)+.2f);
		    force = LATools.vSProd(scale, force);
		    force = LATools.vSum(force, LATools.vSProd(friction,velocity));
		    return force;
		}
		
		@Override
		public void draw(BoardTile[] tiles, MyGLRenderer r){
			super.draw(tiles, r);
			mStoryBanner.draw(r);
			mBee.draw(r);
		}
		
	}
	
	class WIND2 extends State<BoardTile> {

		BoardTile[] tiles;
		
		public WIND2(BoardTile[] tiles){
			this.tiles = tiles;
			mStoryBanner.set("story_banner_3");
			mBee.setState(GameState.GAME_OPENING, tiles.length);
			mBee.setMood(Mood.DIZZY);
		}
		
		@Override
		public void enterAnimation(BoardTile[] tiles) {
		}

		@Override
		public void duringAnimation(BoardTile[] tiles) {
		}

		@Override
		public void draw(BoardTile[] tiles, MyGLRenderer r){
			super.draw(tiles, r);
			mStoryBanner.draw(r);
			mBee.draw(r);
		}

		
		
	}
	
	
}
