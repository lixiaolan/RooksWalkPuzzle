package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.android.freebeeline.graphics.TextCreator;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;



class TutorialBoard2 extends Board {
	enum TutorialState {
		SHOW_PATH, SLIDE1, SLIDE3
	}

	TextBox mBanner; 
	ImageWidget mBoardBg;
	TutorialState mTutorialState;
	TutorialInfo2 mTutorialInfo = new TutorialInfo2();
	CircleProgressBarWidget mCPB;
	Bee mBee;
	Menu mMenu;

	public TutorialBoard2() {
		//Worse fix ever. Daniel Ross confirms that super() is run by default after.
		super(null);
		float[] center = {.20f,-1.0f, 0.0f };
		mCPB = new CircleProgressBarWidget(3, 0, -1, .05f);
		mBanner = new TextBox(0,0,.9f,"");
		mBanner.setFontSize(TextCreator.font1);
		mBoardBg = new ImageWidget(0,0,1,1,"boardbg");
		mBee = new Bee(this);
		mBee.setState(GameState.GAME_OPENING, TutorialInfo2.length);
		state = new ShowPath(tiles,true);
		mTutorialState = TutorialState.SHOW_PATH;
		mMenu = new Menu(5);
		mBoardLineManager = new BoardLineManager(this);
	}

	public void setGeometry(float[] g){
		super.setGeometry(g);
		mBanner.setCenter(0, (g[1]+1)/2);
	}

	public void setStateForward()	{
		switch(mTutorialState) {
		case SHOW_PATH:
			state = new SLIDE1();
			mTutorialState = TutorialState.SLIDE1;
			break;
		case SLIDE1:
			state = new SLIDE3();
			mTutorialState = TutorialState.SLIDE3;
			break;
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
		case SLIDE3:
			state = new SLIDE1();
			mTutorialState = TutorialState.SLIDE1;
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
			if(direction.equals("left_arrow")){
				setStateForward();
			} else if(direction.equals("right_arrow")){
				setStateBackward();
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
			
			//This gets changed on slide 3, so we have to reset it.
			tiles[10].setTrueSolution(-1);
			
			for (int i = 0; i < tiles.length; i++) {
				tiles[i].rotate = false;
				tiles[i].setTextures();
				tiles[i].setSize(tileSize);
			}
			mBanner.setText(mTutorialInfo.banners[0]);
			mBee.setMood(Mood.HAPPY);			
			mBoardLineManager.drawLines();
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

		ImageWidget hand;
		long refTime;
		float[] pt0 = {.22f,-.33f};
		float[] pt1 = {.22f, -.05f};
		float[] pt2 = {-.25f,-.05f};
		float[] pt3 = {.35f,-.19f};
		float[] pt4 = {-.35f, -.19f};
		int time1 = 1;
		int time2 = 2;
		int time3 = 3;
		int time3halves = 4;
		int time4 = 5;
		int time5 = 6;
		boolean lines = true;
		boolean handToggle = true;
		
		public SLIDE1(){
			mBee.setMood(Mood.HIDDEN);
			hand = new ImageWidget(.23f,.12f,.5f,.5f,TextureManager.HAND);
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

			//Reset tile 10
			tiles[10].setTrueSolution(-1);
			tiles[10].setTextures();
			
			mBoardLineManager.drawLines();

		}

		@Override
		public void enterAnimation(BoardTile[] tiles) {
			mBanner.setText(mTutorialInfo.banners[1]);
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
				hand.setWidth(.5f*(time-time1)+(1-(time-time1))*.4f);
				hand.setHeight(.5f*(time-time1)+(1-(time-time1))*.4f);
				if(!mMenu.menuActive)
					mMenu.activate(pt1);
			} else if(time < time3){
				//Move to correct bubble
				hand.setWidth(.5f);
				hand.setHeight(.5f);
				hand.setCenter(pt2[0]*(time-2)+(1-(time-2))*pt1[0], pt2[1]*(time-2)+(1-(time-2))*pt1[1]);
			} else if (time < time3halves){
				//Wait at this bubble. Shrink the hand.
				hand.setWidth(.5f*(time-time3)+(1-(time-time3))*.4f);
				hand.setHeight(.5f*(time-time3)+(1-(time-time3))*.4f);
			}
			else if (time < time4) {
				mMenu.menuActive = false;
				tiles[27].setNumber("3");
				tiles[27].setTextures();
				handToggle = false;
				//Move back down
				hand.setCenter(pt3[0], pt3[1]);
				hand.setWidth(.5f);
				hand.setHeight(.5f);
			} else if(time < time5){
				//Swipe across
				handToggle = true;
				hand.setCenter(pt4[0]*(time-time4)+(1-(time-time4))*pt3[0], pt4[1]*(time-time4)+(1-(time-time4))*pt3[1]);
			} else if(time < 7){
				tiles[27].setArrow(TextureManager.RIGHTARROW);
				tiles[27].setTextures();
					if(lines) {
						mBoardLineManager.drawLines();
						lines = false;
					}
				} else {
					lines = true;
					tiles[27].setNumber(TextureManager.CLEAR);
					tiles[27].setArrow(TextureManager.CLEAR);
					tiles[27].setTextures();
					refTime = System.currentTimeMillis();
					mBoardLineManager.clearLines();
				}
		}

		public void draw(BoardTile[] tiles, MyGLRenderer r){
			mBoardBg.draw(r);
			super.draw(tiles, r);
			mBanner.draw(r);
			mMenu.draw(r);
			if(handToggle)
				hand.draw(r);
			mCPB.draw(r);
		}

	}


	class SLIDE3 extends State<BoardTile> {
		long refTime;
		private ImageWidget mCheck;
		boolean lines = true;
		public SLIDE3(){
			mBee.setMood(Mood.HIDDEN);
			refTime = System.currentTimeMillis();
			prepBoard();
			mCheck  = new ImageWidget(-.68f, .22f, .22f, .22f, TextureManager.CHECK);
			mBanner.setText(mTutorialInfo.banners[3]);
			mCPB.setActiveCircle(2);
		}

		private void prepBoard() {
			tiles[7].setNumber("2");
			tiles[7].setArrow(TextureManager.UPARROW);
			tiles[7].setTextures();
			
			tiles[9].setNumber("1");
			tiles[9].setArrow(TextureManager.UPARROW);
			tiles[9].setTextures();
			//Kill the 3
			tiles[27].setNumber(TextureManager.CLEAR);
			tiles[27].setArrow(TextureManager.CLEAR);
			tiles[27].setTextures();
			
			tiles[10].setTrueSolution(0);
			tiles[10].setNumber(TextureManager.CLEAR);
			tiles[10].setArrow(TextureManager.CLEAR);
			tiles[10].setTextures();
			mBoardLineManager.drawLines();

		}

		@Override
		public void enterAnimation(BoardTile[] tiles) {
			state.period = DrawPeriod.DURING; 
		}

		@Override
		public void duringAnimation(BoardTile[] tiles) {
			float time = ((float)(System.currentTimeMillis()-refTime))/1000.0f; 
			if(time < 2) {
				mCheck.setImage("menu_1");
				tiles[10].setNumber("2");
				tiles[10].setArrow("left_arrow");
				tiles[10].setTextures();
			} else if(time < 4){
				tiles[10].setNumber("3");
				tiles[10].setArrow("left_arrow");
				tiles[10].setTextures();
				mCheck.setImage(TextureManager.CHECK);
				if(lines){
					//drawLines();
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

	/*
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
	    mMenu = new Menu(5);
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
    } */       
}
