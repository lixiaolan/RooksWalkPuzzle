package com.seventhharmonic.android.freebeeline;



class FlowerMenu extends GraphicWidget implements BeeBoardInterface{
    enum FlowerState {
	MAIN_MENU, LP_1, LP_2,
    }

    private FlowerState mFlowerState;
    private CircleProgressBarWidget mCPB;
    private Bee mBee;
    private ImageWidget mBanner;
    private currentLP;

    public static final int lpCount; 
    public static final int flowerCount = 25; 

    private FlowerTile[] tiles = new FlowerTile[flowerCount];
    
    public FlowerMenu() {
	float[] center = {.20f,-1.0f, 0.0f };
	mCPB = new CircleProgressBarWidget(lpCount, center, .05f);
	mBee = new Bee(this);

	mBee.setState(GameState.MAIN_MENU, TutorialInfo2.length);
	state = new MainMenu();
	mFlowerState = FlowerState.MAIN_MENU;
	currentLPIndex = -1;
    }
    
    public void setGeometry(float[] g){
	super.setGeometry(g);
    }

    /* 
     * This class reads the physics style and pack title from the Level
     * Pack Provider class. Then it updates its state accordingly.
     */    

    public void setState(int index) {
	
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
    

    
    class MainMenu extends StateWidget {
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
    
   
}
