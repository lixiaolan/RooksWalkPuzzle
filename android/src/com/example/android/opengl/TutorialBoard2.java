package com.example.android.opengl;

import com.example.android.opengl.State.DrawPeriod;

import android.content.Context;

class TutorialBoard2 extends Board {
	enum TutorialState {
		SHOW_PATH, SLIDE0, SLIDE1, SLIDE2, SLIDE3, SLIDE4
	}

	Banner mBanner; 
	Banner mBottomBanner;
	Background mBoardBg;
	TutorialState mTutorialState;
	TutorialInfo2 mTutorialInfo = new TutorialInfo2();
	Bee mBee;
	Menu mMenu;
	float[] g;
	
	public TutorialBoard2() {
		//Worse fix ever. Daniel Ross confirms that super() is run by default after.
		super();
		mBanner = new Banner(.75f);
		mBoardBg = new Background("boardbg", .75f);
		path = TutorialInfo2.path;
		mBee = new Bee(this);
		mBee.setState(GameState.GAME_OPENING, TutorialInfo2.length);
		state = new ShowPath(tiles,true);
		mTutorialState = TutorialState.SHOW_PATH;
		mMenu = new Menu();
	}
	
	public void setGeometry(float[] g){
		this.g = g;
		mBanner.setCenter(0, g[1] - mBanner.getSize());

	}
	
	public void setState()	{
		switch(mTutorialState) {
		case SHOW_PATH:
			state = new SLIDE1();
			mTutorialState = TutorialState.SLIDE1;
			break;
		case SLIDE1:
			break;
		case SLIDE2:
			break;
		case SLIDE3:
			break;
		case SLIDE4:
			break;
		}
	}

	//The next three methods are all very specific to tutorialinfo and are used to handle input
	public void setBee(Bee mBee){
		this.mBee = mBee;
	}

	public void swipeHandler(String direction) {
	}

	public void touchHandler(Menu mMenu, float[] pt) {
	}    

	class ShowPath extends State<BoardTile> {
		public ShowPath(BoardTile[] tiles, boolean intro) {
			//System.out.println("Entered ShowPath");
			restoreBoard(TutorialInfo2.solutionNumbers, TutorialInfo2.initialNumbers, TutorialInfo2.initialArrows, TutorialInfo2.solutionArrows, path, null);
			showSolution();

			for (int i = 0; i < tiles.length; i++) {
					tiles[i].rotate = false;
					tiles[i].setTextures();
					tiles[i].setSize(tileSize);
				}
			mBanner.set("banner_0");
			mBee.setMood(Mood.HAPPY);			
			drawLines();

		}

		public void enterAnimation(BoardTile[] tiles) {
				// This does a simultaneous snap to position and shrink of tiles.
					for (int i = 0; i < tiles.length; i++) {
						float Sx = ( (i/6) - 2.5f )/4.0f;
						float Sy = ( (i%6) - 2.5f )/4.0f;
						float newX = Sx;
						float newY = Sy;
						float center[] = {newX, newY};
						tiles[i].setCenter2D(center);
				}
					period = DrawPeriod.DURING;

		}

		public void duringAnimation(BoardTile[] tiles) {
		}

		@Override	    
		public void draw(BoardTile[] tiles, MyGLRenderer r){
			mBoardBg.draw(r);
			super.draw(tiles, r);
			mBanner.draw(r);
			mBee.draw(r);
		}

	}    

	class SLIDE1  extends State<BoardTile>{
		
		Banner hand;
		long refTime;
		float[] pt0 = {.23f,.12f};

		float[] pt1 = {-.11f, 0};
		float[] pt2 = {0,.22f};
		float[] pt3 = {.35f,-.22f};
		float[] pt4 = {-.35f, -.22f};
		boolean lines = true;
		
		public SLIDE1(){
			mBee.setMood(Mood.HIDDEN);
			hand = new Banner(.5f);
			hand.set(TextureManager.HAND);
			hand.setCenter(.23f, .12f);
			refTime = System.currentTimeMillis();
			tiles[27].setNumber(TextureManager.CLEAR);
			tiles[27].setArrow(TextureManager.CLEAR);
			drawLines();
		}
			@Override
		public void enterAnimation(BoardTile[] tiles) {
			mBanner.set("banner_1");
			state.period = DrawPeriod.DURING; 
		}

		@Override
		public void duringAnimation(BoardTile[] tiles) {
			float time = ((float)(System.currentTimeMillis()-refTime))/1000.0f; 
			if(time < 1) {
				hand.setCenter(pt1[0]*(time)+(1-(time))*pt0[0], pt1[1]*(time)+(1-(time))*pt0[1]);
			} else if(time < 2){
				if(!mMenu.menuActive)
					mMenu.activate(pt0);
			} else if(time < 3){
				hand.setCenter(pt2[0]*(time-2)+(1-(time-2))*pt1[0], pt2[1]*(time-2)+(1-(time-2))*pt1[1]);
			} else if (time <4) {
				mMenu.menuActive = false;
				tiles[27].setNumber("3");
				tiles[27].setTextures();
				hand.setCenter(pt3[0]*(time-3)+(1-(time-3))*pt2[0], pt3[1]*(time-3)+(1-(time-3))*pt2[1]);
			} else if(time <5){
				hand.setCenter(pt4[0]*(time-4)+(1-(time-4))*pt3[0], pt4[1]*(time-4)+(1-(time-4))*pt3[1]);
			} else if(time < 7){
				tiles[27].setArrow(TextureManager.RIGHTARROW);
				tiles[27].setTextures();
				if(lines) {
					drawLines();
					lines = false;
				}
			} else {
				lines = true;
				tiles[27].setNumber(TextureManager.CLEAR);
				tiles[27].setArrow(TextureManager.CLEAR);
				tiles[27].setTextures();
				drawLines();
				refTime = System.currentTimeMillis();
			}
		}
		
		public void draw(BoardTile[] tiles, MyGLRenderer r){
			mBoardBg.draw(r);
			super.draw(tiles, r);
			mBanner.draw(r);
			hand.draw(r);
			mMenu.draw(r);
		}
		
	}
	
	class SLIDE2 extends State<BoardTile>{
		
	}
		long refTime;
		float[] pt0 = {.23f,.12f};
		float[] pt1 = {-.11f, 0};
		float[] pt2 = {0,.22f};
		float[] pt3 = {.35f,-.22f};
		float[] pt4 = {-.35f, -.22f};
		boolean lines = true;
		
		public SLIDE2(){
			mBee.setMood(Mood.HIDDEN);
			refTime = System.currentTimeMillis();
			tiles[27].setNumber(TextureManager.CLEAR);
			tiles[27].setArrow(TextureManager.CLEAR);
			drawLines();
		}
			@Override
		public void enterAnimation(BoardTile[] tiles) {
			mBanner.set("banner_2");
			state.period = DrawPeriod.DURING; 
		}

		@Override
		public void duringAnimation(BoardTile[] tiles) {
			float time = ((float)(System.currentTimeMillis()-refTime))/1000.0f; 
			if(time < 1) {
			} else if(time < 2){
				if(!mMenu.menuActive)
					mMenu.activate(pt0);
			} else if(time < 3){
			} else if (time <4) {
				mMenu.menuActive = false;
				tiles[27].setNumber("3");
				tiles[27].setTextures();
			} else if(time <5){
			} else if(time < 7){
				tiles[27].setArrow(TextureManager.RIGHTARROW);
				tiles[27].setTextures();
				if(lines) {
					drawLines();
					lines = false;
				}
			} else {
				lines = true;
				tiles[27].setNumber(TextureManager.CLEAR);
				tiles[27].setArrow(TextureManager.CLEAR);
				tiles[27].setTextures();
				drawLines();
				refTime = System.currentTimeMillis();
			}
		}
		
		public void draw(BoardTile[] tiles, MyGLRenderer r){
		}
}
	
/*
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

		@Override
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
*/
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
