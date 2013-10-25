package com.seventhharmonic.android.freebeeline;



class TutorialBoard2 extends Board {
	enum TutorialState {
		SHOW_PATH, SLIDE0, SLIDE1, SLIDE2, SLIDE3, PlayGame, PlayGameEnd
	}

	Banner mBanner; 
	Banner mBottomBanner;
	Background mBoardBg;
	TutorialState mTutorialState;
	TutorialInfo2 mTutorialInfo = new TutorialInfo2();
	CircleProgressBarWidget mCPB;
	Bee mBee;
	Menu mMenu;
	
	public TutorialBoard2() {
		//Worse fix ever. Daniel Ross confirms that super() is run by default after.
		super();
		float[] center = {.20f,-1.0f, 0.0f };
		mCPB = new CircleProgressBarWidget(5, center, .05f);
		mBanner = new Banner(.8f);
		mBoardBg = new Background("boardbg", .75f);
		mBee = new Bee(this);
		mBee.setState(GameState.GAME_OPENING, TutorialInfo2.length);
		state = new ShowPath(tiles,true);
		mTutorialState = TutorialState.SHOW_PATH;
		mMenu = new Menu();
	}
	
	public void setGeometry(float[] g){
		super.setGeometry(g);
		mBanner.setCenter(0, g[1] - mBanner.getSize() -.1f);
	}
	
	public void setStateForward()	{
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
			state = new PlayGameEnd();
			mTutorialState = TutorialState.PlayGameEnd;
		default:
			break;
		}
	}

	public void setStateBackward()	{
		switch(mTutorialState) {
		case SLIDE1:
			state = new ShowPath(tiles,true);
			mTutorialState = TutorialState.SHOW_PATH;
			break;
		case SLIDE2:
			state = new SLIDE1();
			mTutorialState = TutorialState.SLIDE1;
			break;
		case SLIDE3:
			state = new SLIDE2();
			mTutorialState = TutorialState.SLIDE2;
			break;
		default:
			break;
		}
	}
	
	//The next three methods are all very specific to tutorialinfo and are used to handle input
	public void setBee(Bee mBee){
		this.mBee = mBee;
	}

	public void swipeHandler(String direction) {
		if(mTutorialState == TutorialState.PlayGame){
			state.swipeHandler(direction);
		} else {
			if(direction.equals("left_arrow")){
				setStateForward();
			} else if(direction.equals("right_arrow")){
				setStateBackward();
			}
		}
	}

	public void touchHandler(float[] pt) {
		state.touchHandler(pt);
	}
	

	class ShowPath extends State<BoardTile> {
		public ShowPath(BoardTile[] tiles, boolean intro) {
			path = TutorialInfo2.path;
			mCPB.setActiveCircle(0);
			restoreBoard(TutorialInfo2.solutionNumbers, TutorialInfo2.initialNumbers, TutorialInfo2.initialArrows, TutorialInfo2.solutionArrows, path, null);
			showSolution();

			for (int i = 0; i < tiles.length; i++) {
					tiles[i].rotate = false;
					tiles[i].setTextures();
					tiles[i].setSize(tileSize);
				}
			mBanner.set("tutorial_banner_0");
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
			mCPB.draw(r);
		}

	}    

	class SLIDE1  extends State<BoardTile>{
		
		Banner hand;
		long refTime;
		float[] pt0 = {.22f,-.33f};
		float[] pt1 = {.22f, -.05f};
		float[] pt2 = {-.20f,0f};
		float[] pt3 = {.35f,-.22f};
		float[] pt4 = {-.35f, -.22f};
		int time1 = 1;
		int time2 = 2;
		int time3 = 3;
		int time4 = 4;
		int time5 = 5;
		boolean lines = true;
		
		public SLIDE1(){
			mBee.setMood(Mood.HIDDEN);
			hand = new Banner(.5f);
			hand.set(TextureManager.HAND);
			hand.setCenter(.23f, .12f);
			refTime = System.currentTimeMillis();
			prepBoard();
			mCPB.setActiveCircle(1);
		}
		
		public void prepBoard() {
			//Active Tile in this animation
			tiles[27].setNumber(TextureManager.CLEAR);
			tiles[27].setArrow(TextureManager.CLEAR);
			tiles[27].setColor("blue");
			
			//Toggle off some tiles
			tiles[9].setNumber(TextureManager.CLEAR);
			tiles[9].setArrow(TextureManager.CLEAR);
			
			tiles[28].setNumber(TextureManager.CLEAR);
			tiles[28].setArrow(TextureManager.CLEAR);

			tiles[16].setNumber(TextureManager.CLEAR);
			tiles[16].setArrow(TextureManager.CLEAR);
			
			tiles[7].setNumber(TextureManager.CLEAR);
			tiles[7].setArrow(TextureManager.CLEAR);
			
			tiles[13].setNumber(TextureManager.CLEAR);
			tiles[13].setArrow(TextureManager.CLEAR);
			
			drawLines();

		}
		
			@Override
		public void enterAnimation(BoardTile[] tiles) {
			mBanner.set("tutorial_banner_1");
			state.period = DrawPeriod.DURING; 
		}

		@Override
		public void duringAnimation(BoardTile[] tiles) {
			float time = ((float)(System.currentTimeMillis()-refTime))/1000.0f; 
			if(time < time1) {
				//Move To initial position
				hand.setCenter(pt1[0]*(time)+(1-(time))*pt0[0], pt1[1]*(time)+(1-(time))*pt0[1]);
			} else if(time < time2){
				//Activate Menu
				if(!mMenu.menuActive)
					mMenu.activate(pt1);
			} else if(time < time3){
				//Move to correct bubble
				hand.setCenter(pt2[0]*(time-2)+(1-(time-2))*pt1[0], pt2[1]*(time-2)+(1-(time-2))*pt1[1]);
			} else if (time < time4) {
				mMenu.menuActive = false;
				tiles[27].setNumber("3");
				tiles[27].setTextures();
				//Move back down
				hand.setCenter(pt3[0]*(time-3)+(1-(time-3))*pt2[0], pt3[1]*(time-3)+(1-(time-3))*pt2[1]);
			} else if(time < time5){
				//Swipe across
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
			mMenu.draw(r);
			hand.draw(r);
			mCPB.draw(r);
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
			mCPB.setActiveCircle(2);
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
			mBanner.set("tutorial_banner_2");
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
			mCPB.draw(r);
		}
}
	
	class SLIDE3 extends State<BoardTile> {
		long refTime;
		private Banner mCheck;
		boolean lines = true;
		public SLIDE3(){
			mBee.setMood(Mood.HIDDEN);
			refTime = System.currentTimeMillis();
			
			prepBoard();
			
			mCheck  = new Banner(.22f);
			mCheck.setCenter(-.68f, .11f);
			mCheck.setColor("transparent");
			mCheck.set("check");
			mBanner.set("tutorial_banner_3");
			mCPB.setActiveCircle(3);
		}

		private void prepBoard() {
			//restoreBoard(TutorialInfo2.solutionNumbersSlide3, TutorialInfo2.initialNumbersSlide3, TutorialInfo2.initialArrowsSlide3, TutorialInfo2.solutionArrowsSlide3, path, null);
			//showSolution();
			tiles[7].setNumber("2");
			tiles[7].setArrow(TextureManager.UPARROW);
			tiles[7].setTextures();
			
			tiles[27].setNumber(TextureManager.CLEAR);
			tiles[27].setArrow(TextureManager.CLEAR);
			tiles[27].setTextures();
			drawLines();

		}
		
		@Override
		public void enterAnimation(BoardTile[] tiles) {
			state.period = DrawPeriod.DURING; 
		}

		@Override
		public void duringAnimation(BoardTile[] tiles) {
			float time = ((float)(System.currentTimeMillis()-refTime))/1000.0f; 
			if(time < 2) {
				mCheck.set("menu_1");
				tiles[9].setNumber("2");
				tiles[9].setArrow("left_arrow");
				tiles[9].setTextures();
			} else if(time < 4){
				tiles[9].setNumber("3");
				tiles[9].setArrow("left_arrow");
				tiles[9].setTextures();
				mCheck.set("check");
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
			mCPB.draw(r);
		}
	}
	
	class PlayGame extends State<BoardTile> {
		long refTime;
		private Background mCheck;
		Menu mMenu;
		private int at = -1;
		
		
		public PlayGame(){
			mBee.setMood(Mood.ASLEEP);
			refTime = System.currentTimeMillis();
			prepBoard();		
			mCheck  = new Background("check",.11f);
			float[] center = {-.7f,-1f, 0f};
			mCheck.setCenter(center);
			mBanner.set("tutorial_banner_4");
			mMenu = new Menu();
		}

		private void prepBoard() {
			restoreBoard(TutorialInfo2.solutionNumbersPlayGame, TutorialInfo2.initialNumbersPlayGame, TutorialInfo2.initialArrowsPlayGame, TutorialInfo2.solutionArrowsPlayGame, TutorialInfo2.pathPlayGame, TutorialInfo2.clickablePlayGame);
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
		}
		
		public void draw(BoardTile[] tiles, MyGLRenderer r){
			mBoardBg.draw(r);
			super.draw(tiles, r);
			mBanner.draw(r);
			mMenu.draw(r);
			mCheck.draw(r);
			mBee.draw(r);
			
		}
					
		public void touchHandler(float[] pt){
			int val = mMenu.touched(pt);			
			if(val == -1){
				if (at != -1) {
					tiles[at].setColor("transparent");
				}
				at = touched(pt);
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
	
			if(mCheck.touched(pt) == 1){
				if(checkSolution()){
					setStateForward();      
				} else {
					mBanner.set(TextureManager.TRY_AGAIN);
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
	
	class PlayGameEnd extends State<BoardTile> {
		boolean[] rotateTiles = new boolean[36];
		boolean[] flipped = new boolean[36];
		long[] refTime = new long[36];
		public PlayGameEnd() {
			mBanner.set("tutorial_banner_5");
			mBee.setState(GameState.GAME_OPENING,TutorialInfo2.lengthPlayGame);
			mBee.setMood(Mood.HAPPY);
			for (int i = 0; i < tiles.length; i++) {
				flipped[i] = false;
				rotateTiles[i] = false;
				float Sx = ( (i/6) - 2.5f )/4.0f;
				float Sy = ( (i%6) - 2.5f )/4.0f;
				tiles[i].setSize(.12f);
				float center[] = { Sx, Sy, 0.0f};
				tiles[i].center = center;
				tiles[i].setColor("transparent");
				refTime[i] = System.currentTimeMillis();
				tiles[i].setPivot(tiles[i].getCenter());
			}
		}


		//The new animation:
		public void enterAnimation(BoardTile[] tiles) {
			//	    long time = System.currentTimeMillis()-refTime[0];
			for (int i = 0; i < tiles.length ; i++) {
				if (tiles[i].true_solution >0) {
					float[] pivot = {1.0f,1.0f,0.0f};
					String[] s = new String[2];
					s[0] = TextureManager.CLEAR;
					s[1] = tiles[i].flowerTexture;
					tiles[i].setFlipper(geometry[1], pivot, 1.5f, 0.0f, s);
				}
			}
			period = DrawPeriod.DURING;
		}

		public void duringAnimation(BoardTile[] tiles) {
			for(int i=0;i<tiles.length;i++){
				if(tiles[i].rotate && !rotateTiles[i]){
					refTime[i] = System.currentTimeMillis();
					rotateTiles[i] = true;
					float[] pivot = {1.0f,1.0f,0.0f};
					String[] s = new String[2];
					s[0] = tiles[i].arrow;
					s[1] = tiles[i].number;
					tiles[i].setFlipper(geometry[1], pivot, 1.5f, 0.0f, s);
					//tiles[i].rotate = false;
				}
			}
		}
		public void draw(BoardTile[] tiles, MyGLRenderer r){
			mBoardBg.draw(r);
			super.draw(tiles, r);
			mBanner.draw(r);
			mBee.draw(r);
		}


	}    

	
	
}