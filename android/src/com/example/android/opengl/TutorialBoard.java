package com.example.android.opengl;

import android.content.Context;

class TutorialBoard extends Board {
	enum TutorialState {
		INIT, ONE_TILE, SHOW_PATH, WALKTHROUGH
	}

	Banner mBanner; 
	Banner mBottomBanner;
	Background mBoardBg;
	Border mBorder;
	TutorialState mTutorialState;
	TutorialInfo mTutorialInfo = new TutorialInfo();
	Bee mBee;

	public TutorialBoard() {
		//Worse fix ever. Daniel Ross confirms that super() is run by default after.
		super();
		mBanner = new Banner(.75f);
		//mBanner.setPosition("TOPCENTER");
		mBottomBanner = new Banner(.75f);
		//mBottomBanner.setPosition("BANNERBOTTOM");
		mBoardBg = new Background("boardbg", .75f);
		path = TutorialInfo.path;
		mBee = new Bee(this);
		mBee.setState(GameState.GAME_OPENING, TutorialInfo.length);
		restoreBoard(TutorialInfo.solutionNumbers, TutorialInfo.initialNumbers, TutorialInfo.initialArrows, TutorialInfo.solutionArrows, path, null);
		state = new ShowPath(tiles,true);
		mTutorialState = TutorialState.SHOW_PATH;
	}

	public void setState()	{
		switch(mTutorialState) {
		case SHOW_PATH:
			state = new WalkThrough(tiles);
			mTutorialState = TutorialState.WALKTHROUGH;
			break;
		case WALKTHROUGH:
			state = new ShowPath(tiles, false);
			mTutorialState = TutorialState.SHOW_PATH;
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

	class ShowPath extends State<BoardTile> {

		boolean[] rotateTiles = new boolean[36];
		boolean[] flipped = new boolean[36];
		long[] refTime = new long[36];

		public Tile[] originalTiles;
		public long refTimeIntro;
		public boolean swap = false;
		public float initSize;
		public float finalSize = .12f;
		public float[] oldX;
		public float[] oldY;

		public boolean doIntro; 

		public ShowPath(BoardTile[] tiles, boolean intro) {
			//System.out.println("Entered ShowPath");
			doIntro  = intro;
			originalTiles = tiles;
			if (doIntro) {
				refTimeIntro = System.currentTimeMillis();
				oldX = new float[tiles.length];
				oldY = new float[tiles.length];
				for (int i = 0; i < tiles.length; i++) {
					tiles[i].rotate = false;
					tiles[i].setTextures(TextureManager.CLEAR, tiles[i].flowerTexture);
					oldX[i] = tiles[i].center[0];
					oldY[i] = tiles[i].center[1];
				}
				initSize = tiles[0].getSize();
				setCounter();
			}
			for (int i = 0; i < tiles.length; i++)
				tiles[mTutorialInfo.getActiveTile()].setColor("white");

			showSolution();
			//    	    mBanner.set(mTutorialInfo.ShowPathBanner);
			mBee.setMood(Mood.HAPPY);
			//mBorder = new Border(getColumnSums(), getRowSums());
		}

		public void enterAnimation(BoardTile[] tiles) {
			if (doIntro) {	
				long time = System.currentTimeMillis() - refTimeIntro;
				float totalTime = 2000f;
				// This does a simultaneous snap to position and shrink of tiles.
				if(time < totalTime/3) {
					for (int i = 0; i < tiles.length; i++) {
						float Sx = ( (i/6) - 2.5f )/4.0f;
						float Sy = ( (i%6) - 2.5f )/4.0f;
						float newX = Sx + ((float)Math.pow(.5,3*time/totalTime*10f))*(oldX[i]-Sx);
						float newY = Sy + ((float)Math.pow(.5,3*time/totalTime*10f))*(oldY[i]-Sy);
						float center[] = {newX, newY, 0.0f};
						tiles[i].center = center;
					}			
				}
				//Tiles shrink to zero then blow up with the
				//the correct game tiles.
				else if( time < 2*totalTime/3 && time > totalTime/3) {
					for (int i = 0; i < tiles.length; i++) {
						tiles[i].setSize(initSize*(2-3*time/totalTime ) );
					}
				}
				else if( time < totalTime && time > 2*totalTime/3) {
					float[] pivot = {1,0,0};
					if (!swap) {
						for (int i = 0; i < tiles.length; i++) {
							tiles[i].setTextures();
							float Sx = ( (i/6) - 2.5f )/4.0f;
							float Sy = ( (i%6) - 2.5f )/4.0f;
							float center[] = {Sx, Sy, 0.0f};
							tiles[i].center = center;
						}
					}
					for (int i = 0; i < tiles.length; i++) {
						tiles[i].setSize(finalSize*(3*time/totalTime-2));
					}
				}

				else {
					for(int i = 0;i<tiles.length;i++){
						tiles[i].setAngle(0);
						tiles[i].setSize(finalSize);
						//tiles[i].setColor("transparent");
					}
					period = DrawPeriod.DURING;
				}
			}
			else {
				period = DrawPeriod.DURING;
			}
		}

		public void duringAnimation(BoardTile[] tiles) {

		}

		@Override	    
		public void draw(BoardTile[] tiles, MyGLRenderer r){
			super.draw(tiles, r);
			mBoardBg.draw(r);
			mBanner.draw(r);
			mBee.draw(r);
			mBottomBanner.draw(r);
			mBorder.draw(r);		    
		}
		public void touchHandler(Menu menu, float[] pt) {
			setCounter();
			if (mTutorialInfo.getCounter() == 3)
				setState();
		}

		public void swipeHandler(String direction) {

		}

		public boolean setCounter() {
			if (mTutorialInfo.getCounter() >= 0) {
				tiles[mTutorialInfo.getActiveTile()].setColor("white");
			} 
			mTutorialInfo.incrementCounter();
			mBanner.set("banner_"+Integer.toString(mTutorialInfo.getCounter()));
			mBottomBanner.set("bottom_banner_"+Integer.toString(mTutorialInfo.getCounter()));
			if(mTutorialInfo.getCounter() >= mTutorialInfo.activeTile.length){
				return false;
			}
			if (mTutorialInfo.getCounter() == 3)
				tiles[mTutorialInfo.getActiveTile()].setColor("blue"); 
			return true;
		}


	}    


	class WalkThrough extends State<BoardTile> {

		BoardTile[] tiles;

		public WalkThrough(BoardTile[] tiles){
			System.out.println("Entered Walkthrough");
			this.tiles = tiles;
			mBee.setMood(Mood.HIDDEN);
		}
		public void enterAnimation(BoardTile[] tiles) {			
			period = DrawPeriod.DURING;
			for(int i = 0;i<tiles.length;i++){
				tiles[i].setAngle(0);
				tiles[i].setSize(.12f);
				if(!mTutorialInfo.initialNumbers[i].equals("clear")){
					tiles[i].setColor("dullyellow");
				} else if(mTutorialInfo.getActiveTile() != i){
					tiles[i].setColor("transparent");
				}

				tiles[i].setNumber(mTutorialInfo.initialNumbers[i]);
				tiles[i].setArrow(mTutorialInfo.initialArrows[i]);
				tiles[i].setTextures();
			}
		}

		public void duringAnimation(BoardTile[] tiles) {
			for (int i = 0; i < tiles.length; i++) {
				((BoardTile)tiles[i]).setTextures();
			}
		}	

		@Override
		public void draw(BoardTile[] tiles, MyGLRenderer r){
			super.draw(tiles, r);
			mBoardBg.draw(r);
			mBanner.draw(r);
			mBottomBanner.draw(r);
			mBee.draw(r);
			mBorder.draw(r);		    
		}

		public boolean setCounter() {
			if (mTutorialInfo.getCounter() >= 0) {
				tiles[mTutorialInfo.getActiveTile()].setColor("white");
			} 
			mTutorialInfo.incrementCounter();
			mBanner.set("banner_"+Integer.toString(mTutorialInfo.getCounter()));
			mBottomBanner.set("bottom_banner_"+Integer.toString(mTutorialInfo.getCounter()));
			if(mTutorialInfo.getCounter() >= mTutorialInfo.activeTile.length){
				return false;
			}
			tiles[mTutorialInfo.getActiveTile()].setColor("blue"); 
			if(mTutorialInfo.getCounter() > 0 ){
				updateStep();
			}
			return true;
		}

		public int touched(float[] pt) {
			int in = -1;
			for (int i = 0; i < tiles.length; i++) {
				if( tiles[i].touched(pt)) {
					in =  i;
					break;
				}
			}
			if (mTutorialInfo.getCounter() < mTutorialInfo.activeTile.length)
				if (in == mTutorialInfo.getActiveTile()) 
					return in;
			return -1;   
		}	

		public void touchHandler(Menu mMenu, float[] pt) {
			int val = mMenu.touched(pt);
			if (val != -1) {
				//System.out.println("Menu Touched!");
				if ( userNumberInput(val) ) {
					//System.out.println("Passed the if");
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
			if(checkInput()){
				setCounter();
			}
			if (mTutorialInfo.getCounter() == 29)
				setState();
		}

		private boolean checkInput() {
			boolean correctNumber = true;
			boolean correctArrow  = true;
			String number = mTutorialInfo.getNumber();
			String arrow = mTutorialInfo.getArrow();
			if(!number.equals("none")){
				if(!number.equals(tiles[mTutorialInfo.getActiveTile()].getNumber())){
					correctNumber = false;
				}
			} 

			if(!arrow.equals("none")){
				if(!arrow.equals(tiles[mTutorialInfo.getActiveTile()].getArrow())){
					correctArrow = false;
				}
			}	    
			return correctNumber && correctArrow;

		}

		public void swipeHandler(String direction) {
			if(direction.equals(mTutorialInfo.getArrow())){
				tiles[mTutorialInfo.getActiveTile()].setArrow(direction);
			}
			if(checkInput()){
				setCounter();
			}	    
			if (mTutorialInfo.getCounter() == 29)//
				setState();
		}

		private boolean userNumberInput(int val) {
			if(mTutorialState == TutorialState.WALKTHROUGH){
				if( Integer.toString(val).equals(mTutorialInfo.getNumber())){
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

	// class Summary extends State<BoardTile> {

	// 	BoardTile[] tiles;

	// 	public Summary(BoardTile[] tiles){
	// 	    this.tiles = tiles;
	// 	}

	// 	public void enterAnimation(BoardTile[] tiles) {
	// 	    period = DrawPeriod.DURING;
	// 	}

	// 	public void duringAnimation(BoardTile[] tiles) {

	// 	    for (int i = 0; i < tiles.length; i++) {
	// 		((BoardTile)tiles[i]).setTextures();
	// 	    }
	// 	}

	// 	public void draw(BoardTile[] tiles, MyGLRenderer r){
	// 	    super.draw(tiles,r);
	// 	    //mBanner.draw(r);   
	// 	}

	// 	@Override
	// 	    public int touched(float[] pt) {
	// 	    return -1;
	// 	}	


	// }

}
