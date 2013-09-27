package com.example.android.opengl;


class TutorialBoard extends Board {
	enum TutorialState {
		INIT, ONE_TILE, SHOW_PATH, WALKTHROUGH, SUMMARY
	}

	Banner mBanner; 
	Background mBoardBg;
	TutorialState mTutorialState;
	TutorialInfo mTutorialInfo = new TutorialInfo();
	Bee mBee;
	
	public TutorialBoard() {
		//Worse fix ever. Daniel Ross confirms that super() is run by default after.
		super();
		mBanner = new Banner(.7f);
		mBoardBg = new Background("boardbg", .75f);
		mBanner.setCenter(0,.8f);
		mTutorialState = TutorialState.ONE_TILE;
		state = new OneTile(tiles);
		path = TutorialInfo.path;
		mBee = new Bee(this);
		mBee.setState(GameState.GAME_OPENING, TutorialInfo.length);
		restoreBoard(TutorialInfo.solutionNumbers, TutorialInfo.initialNumbers, TutorialInfo.initialArrows, TutorialInfo.solutionArrows, path, null);
	}

	public void setState()	{
		System.out.println("Got called :(");
		boolean b;
		switch(mTutorialState) {
		case ONE_TILE:
			state = new ShowPath(tiles);
			mTutorialState = TutorialState.SHOW_PATH;
			break;
		case SHOW_PATH:
			state = new WalkThrough(tiles, mTutorialInfo);
			mTutorialState = TutorialState.WALKTHROUGH;
			break;
		case WALKTHROUGH: 
			b = ((WalkThrough)state).setCounter();
			if(!b){
			    state = new Summary(tiles);
			    mTutorialState = TutorialState.SUMMARY;
			    setState();
			}
			break;
		case SUMMARY:

			break;
		}
	}

	//The next three methods are all very specific to tutorialinfo and are used to handle input
	public void setBee(Bee mBee){
		this.mBee = mBee;
	}
	
	public void swipeHandler(String direction) {
		state.swipeHandler(direction);
	}

	public void touchHandler(Menu mMenu, float[] pt) {
		state.touchHandler(mMenu, pt);
	}

	class OneTile extends State<BoardTile> {

		BoardTile[] tiles;
		int at = -1;

		public OneTile(BoardTile[] tiles) {
			//Need a way to 
			this.tiles = tiles;
			for(int i=0;i<tiles.length;i++){
				tiles[i].setTextures(TextureManager.CLEAR, TextureManager.CLEAR);
			}
			tiles[15].setColor("blue");
			float[] origin = {0,.3f};
			tiles[15].setCenter2D(origin);
			mBanner.set(mTutorialInfo.OneTileBanner);
		}

		@Override
		public void enterAnimation(BoardTile[] tiles) {
			period = DrawPeriod.DURING;
		}

		@Override
		public void duringAnimation(BoardTile[] tiles) {
			// TODO Auto-generated method stub
		}

		public int touched(float[] pt) {
			for (int i = 0; i < tiles.length; i++) {
				if( tiles[i].touched(pt)) {
					return i;
				}
			}
			return -1;
		}	

		public void touchHandler(Menu mMenu, float[]  pt){
			int val = mMenu.touched(pt);
			if (val != -1) {
				System.out.println("Menu Touched!");
				tiles[at].setUserInput(val);
				//Can I just do this in tile
				tiles[at].setTextures();
				mMenu.menuActive = false;				
			}
			else {
				at  = touched(pt);
				if (at==15) {
					mMenu.activate(pt);
				}
				else {
					mMenu.menuActive = false;
				}
			}
		}

		public void swipeHandler(String direction){
			tiles[15].setArrow(direction);
			tiles[15].setTextures();
		}

		public void draw(BoardTile[] tiles, MyGLRenderer r){
			super.draw(tiles, r);
			mBanner.draw(r);
			mBee.draw(r);
		}
		
	}
	
	class ShowPath extends State<BoardTile> {
		BoardTile[] tiles;
		long refTime;

		public ShowPath(BoardTile[] tiles){
			refTime = System.currentTimeMillis();
			mBanner.set(mTutorialInfo.ShowPathBanner);
			System.out.println("I set the mood");
			mBee.setMood(Mood.HAPPY);
		}
		
		@Override
		public void enterAnimation(BoardTile[] tiles) {
				long time = System.currentTimeMillis() - refTime;
				float totalTime = 2000f;

				if(time < totalTime) {
					for (int i = 0; i < tiles.length; i++) {
						tiles[i].setTextures(TextureManager.CLEAR, tiles[i].flowerTexture);
						float Sx = ( (i/6) - 2.5f )/4.0f;
						float Sy = ( (i%6) - 2.5f )/4.0f;
						tiles[i].setSize(.12f*time/totalTime+(1-time/totalTime)*.15f);
						float newX = tiles[i].center[0]+time/totalTime*(Sx - tiles[i].center[0]);
						float newY = tiles[i].center[1]+time/totalTime*(Sy - tiles[i].center[1]);
						float center[] = { newX, newY, 0.0f};
						tiles[i].center = center;
					}			
				} 
				else if( time < 4000.0f && time > totalTime) {
					float[] pivot = {1,0,0};
					for (int i = 0; i < tiles.length; i++) {
						tiles[i].setPivot(pivot);
						tiles[i].setAngle((time-totalTime)*.045f);
					}
				}	
				else {
					for(int i = 0;i<tiles.length;i++){
						tiles[i].setAngle(0);
						tiles[i].setSize(.12f);
						tiles[i].setColor("transparent");
						tiles[i].setNumber(Integer.toString(tiles[i].getTrueSolution()));
						tiles[i].setArrow(tiles[i].getTrueArrow());
						tiles[i].setTextures();
					}
					period = DrawPeriod.DURING;
				}
				System.out.println("Counter + "+Integer.toString(mTutorialInfo.getCounter()));
			}
		
		@Override
		public void duringAnimation(BoardTile[] tiles) {
		}
		
		@Override
		public void draw(BoardTile[] tiles, MyGLRenderer r){
			super.draw(tiles, r);
			mBanner.draw(r);
			mBoardBg.draw(r);
			mBee.draw(r);
		}
	}
	
	class WalkThrough extends State<BoardTile> {

		BoardTile[] tiles;
		
		public WalkThrough(BoardTile[] tiles, TutorialInfo inTI){
			this.tiles = tiles;
			setCounter();
			mBee.setMood(Mood.ASLEEP);
		}

		public void enterAnimation(BoardTile[] tiles) {			
				period = DrawPeriod.DURING;
		}

		public void duringAnimation(BoardTile[] tiles) {
			for (int i = 0; i < tiles.length; i++) {
				((BoardTile)tiles[i]).setTextures();
			}
		}	

		@Override
		public void draw(BoardTile[] tiles, MyGLRenderer r){
			super.draw(tiles, r);
			mBanner.draw(r);
			mBoardBg.draw(r);
			mBee.draw(r);
		}

		public boolean setCounter() {
			if (mTutorialInfo.getCounter() >= 0) {
				tiles[mTutorialInfo.getActiveTile()].setColor("white");
			} 
			mTutorialInfo.incrementCounter();
			mBanner.set("banner_"+Integer.toString(mTutorialInfo.getCounter()));
			if(mTutorialInfo.getCounter() >= mTutorialInfo.activeTile.length){
				return false;
			}
			tiles[mTutorialInfo.getActiveTile()].setColor("blue"); 
			if(mTutorialInfo.getCounter() > 0 ){
				updateStep();
			}
			return true;
		}

		@Override
		public int touched(float[] pt) {
			int in = -1;
			for (int i = 0; i < tiles.length; i++) {
				if( tiles[i].touched(pt)) {
					in =  i;
					break;
				}
			}
			System.out.println("in: " + Integer.toString(in)); 
			if (mTutorialInfo.getCounter() < mTutorialInfo.activeTile.length)
				if (in == mTutorialInfo.getActiveTile()) 
					return in;
			return -1;   
		}	

		public void touchHandler(Menu mMenu, float[] pt) {
			int val = mMenu.touched(pt);
			if (val != -1) {
				System.out.println("Menu Touched!");
				if ( userNumberInput(val) ) {
					System.out.println("Passed the if");
					mMenu.menuActive = false;
				}
			}
			else {
				int at  = touched(pt);
				if (at != -1 && isMenuOn()) {
					mMenu.activate(pt);
				}
				else {
					mMenu.menuActive = false;
				}
			}
		}

		public void swipeHandler(String direction) {
			if(mTutorialState == TutorialState.WALKTHROUGH){
				if(direction.equals(mTutorialInfo.getArrow())){
					tiles[mTutorialInfo.getActiveTile()].setArrow(direction);
				}
			}
		}

		private boolean userNumberInput(int val) {
			System.out.println("I got some input! ");
			if(mTutorialState == TutorialState.WALKTHROUGH){

				if( Integer.toString(val).equals(mTutorialInfo.getNumber())){
					System.out.println("Found some correct input");
					tiles[mTutorialInfo.getActiveTile()].setUserInput(val);
					return true;
				}
			}  	
			return false;
		}

		private boolean isMenuOn() {
			if(mTutorialState==TutorialState.WALKTHROUGH){
				if(!mTutorialInfo.getNumber().equals("none")){
					return true;
				}
			}  	
			return false;
		}

		public void updateStep(){
			int m = mTutorialInfo.getPreviousActiveTile();
			if(!mTutorialInfo.getPreviousArrow().equals("none"))
				tiles[m].setArrow(mTutorialInfo.getPreviousArrow());
			if(!mTutorialInfo.getPreviousNumber().equals("none"))
				tiles[m].setNumber(mTutorialInfo.getPreviousNumber());
		}
		
	}

	class Summary extends State<BoardTile> {

		BoardTile[] tiles;

		public Summary(BoardTile[] tiles){
			this.tiles = tiles;
		}

		public void enterAnimation(BoardTile[] tiles) {
			period = DrawPeriod.DURING;
		}

		public void duringAnimation(BoardTile[] tiles) {

			for (int i = 0; i < tiles.length; i++) {
				((BoardTile)tiles[i]).setTextures();
			}
		}

		public void draw(BoardTile[] tiles, MyGLRenderer r){
			super.draw(tiles,r);
			//mBanner.draw(r);   
		}

		@Override
		public int touched(float[] pt) {
			return -1;
		}	


	}

}
