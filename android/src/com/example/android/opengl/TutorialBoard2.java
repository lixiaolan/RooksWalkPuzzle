package com.example.android.opengl;

import com.example.android.opengl.State.DrawPeriod;

import android.content.Context;

class TutorialBoard2 extends Board {
	enum TutorialState {
		SHOW_PATH, SLIDE0, SLIDE1, SLIDE2, SLIDE3, PlayGame
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
			state = new SLIDE2();
			mTutorialState = TutorialState.SLIDE2;
			break;
		case SLIDE2:
			state = new SLIDE3();
			mTutorialState = TutorialState.SLIDE3;
			break;
		case SLIDE3:
			state = new PlayGame();
			mTutorialState = TutorialState.PlayGame;
			break;
		case PlayGame:
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

	public void touchHandler(float[] pt) {
		state.touchHandler(pt);
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
			tiles[27].setColor("blue");
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
		long refTime;

		private Banner mCheck;
		private boolean lines  = true;
		
		
		
		public SLIDE2(){
			mBee.setMood(Mood.HIDDEN);
			refTime = System.currentTimeMillis();
			
			prepBoard();
			
			mCheck  = new Banner(.22f);
			mCheck.setCenter(-.68f, .11f);
			mCheck.setColor("transparent");
			mCheck.set("check");
		}
		
		private void prepBoard(){
			tiles[27].setNumber("3");
			tiles[27].setArrow("right_arrow");
			tiles[27].setTextures();
			tiles[27].setColor("transparent");
			tiles[9].setColor("blue");
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
			if(time < 2) {
				mCheck.set("check");
				tiles[9].setNumber("2");
				tiles[9].setArrow("down_arrow");
				tiles[9].setTextures();
			} else if(time < 4){
				tiles[9].setNumber("2");
				tiles[9].setArrow("left_arrow");
				tiles[9].setTextures();
				mCheck.set("menu_1");
				if(lines){
					drawLines();
					lines = false;
				}
			} else {
				lines = true;
				refTime = System.currentTimeMillis();
			}
		}
		
		public void draw(BoardTile[] tiles, MyGLRenderer r){
			mBoardBg.draw(r);
			super.draw(tiles, r);
			mBanner.draw(r);
			mCheck.draw(r);
		}
}
	
	class SLIDE3 extends State<BoardTile> {
		long refTime;
		private Banner mCheck;
		private boolean lines  = true;
		
		public SLIDE3(){
			mBee.setMood(Mood.HIDDEN);
			refTime = System.currentTimeMillis();
			
			prepBoard();
			
			mCheck  = new Banner(.22f);
			mCheck.setCenter(-.68f, .11f);
			mCheck.setColor("transparent");
			mCheck.set("check");
			mBanner.set("banner_3");

		}

		private void prepBoard() {
			restoreBoard(TutorialInfo2.solutionNumbersSlide3, TutorialInfo2.initialNumbersSlide3, TutorialInfo2.initialArrowsSlide3, TutorialInfo2.solutionArrowsSlide3, path, null);
			showSolution();
			drawLines();
			for (int i = 0; i < tiles.length; i++) {
				tiles[i].rotate = false;
				tiles[i].setTextures();
				tiles[i].setSize(tileSize);
			}
			

		}
		
		@Override
		public void enterAnimation(BoardTile[] tiles) {
			state.period = DrawPeriod.DURING; 
		}

		@Override
		public void duringAnimation(BoardTile[] tiles) {
			float time = ((float)(System.currentTimeMillis()-refTime))/1000.0f; 
			if(time < 2) {
			} else if(time < 4){
			} else {
				lines = true;
				refTime = System.currentTimeMillis();
			}
		}
		
		public void draw(BoardTile[] tiles, MyGLRenderer r){
			mBoardBg.draw(r);
			super.draw(tiles, r);
			mBanner.draw(r);
			mCheck.draw(r);
		}
	}
	
	class PlayGame extends State<BoardTile> {
		long refTime;
		private Banner mCheck;
		private boolean lines  = true;
		Menu mMenu;
		private int at = -1;
		
		
		public PlayGame(){
			mBee.setMood(Mood.HIDDEN);
			refTime = System.currentTimeMillis();
			
			prepBoard();
			
			mCheck  = new Banner(.22f);
			mCheck.setCenter(-.68f, .11f);
			mCheck.setColor("transparent");
			mCheck.set("check");
			mBanner.set("banner_3");
			mMenu = new Menu();

		}

		private void prepBoard() {
			restoreBoard(TutorialInfo2.solutionNumbersSlide3, TutorialInfo2.initialNumbersSlide3, TutorialInfo2.initialArrowsSlide3, TutorialInfo2.solutionArrowsSlide3, path, TutorialInfo2.clickable);
			showSolution();
			drawLines();
			for (int i = 0; i < tiles.length; i++) {
				tiles[i].rotate = false;
				tiles[i].setTextures();
				tiles[i].setSize(tileSize);
			}
		}
		
		@Override
		public void enterAnimation(BoardTile[] tiles) {
			state.period = DrawPeriod.DURING; 
		}

		@Override
		public void duringAnimation(BoardTile[] tiles) {
			float time = ((float)(System.currentTimeMillis()-refTime))/1000.0f; 
			if(time < 2) {
			} else if(time < 4){
			} else {
				lines = true;
				refTime = System.currentTimeMillis();
			}
		}
		
		public void draw(BoardTile[] tiles, MyGLRenderer r){
			mBoardBg.draw(r);
			super.draw(tiles, r);
			mMenu.draw(r);
			
		}
					
		public void touchHandler(float[] pt){
			int val = mMenu.touched(pt);			
			if(val == -1){
				if (at != -1) {
					tiles[at].setColor("transparent");
				}
				at = touched(pt);
				System.out.println(at);
				if(at != -1 ) {
					if(tiles[at].isBlack() == false) {
						tiles[at].setColor("blue");
						if(tiles[at].isClickable())
							mMenu.activate(pt);
					}
				}
			} else {
			    if (at != -1) {
			    	tiles[at].setUserInput(val);
			    	drawLines();
			    }
			}
	
		}

		public void swipeHandler(String direction){
		    if (at != -1 && tiles[at].isClickable()) {
		    	tiles[at].setArrow(direction);
		    	tiles[at].setTextures();
		    	drawLines(); 
		    	mMenu.menuActive = false;
		}}
	
	}
zzzzzzzzz}