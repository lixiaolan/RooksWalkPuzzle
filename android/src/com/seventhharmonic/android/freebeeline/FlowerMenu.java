package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.State.DrawPeriod;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
import com.seventhharmonic.android.freebeeline.util.LATools;
import com.seventhharmonic.com.freebeeline.levelresources.*;
import com.seventhharmonic.android.freebeeline.listeners.*;
import com.seventhharmonic.android.freebeeline.physics.*;
import com.seventhharmonic.com.freebeeline.levelresources.LevelPack;
import com.seventhharmonic.com.freebeeline.levelresources.LevelPackProvider;
import java.util.HashMap;
import java.util.Map;

class FlowerMenu extends GraphicWidget implements BeeBoardInterface {
    
    //This is the internal state system this class will use:
    //MAIN_MENU: Opening game menu
    //BOOK_SELECT: Selecting a puzzle book (uses two overlayed widgits);
    //CHAPTER_SELECT: The state that used to be handled by Table of Contents.

    //The enum and its member
    enum FlowerState {
	MAIN_MENU, BOOK_SELECT, CHAPTER_SELECT 
    }
    private FlowerState mFlowerState;

    //Tag used for logs
    private String TAG = "FlowerMenu";
    
    // "slider" used for placing a screen slide widget on top of stat system.
    // This is a bit of a hack... fair warning.
    //private StateWidget flowerState;

    //The bee that lives in the flowers
    private Bee mBee;

    private LevelPack currLevelPack;
    private int currLevelPackIndex;

    protected float flowerSize = .15f;
    private static final int flowerCount = 25; 
    private FlowerTile[] tiles = new FlowerTile[flowerCount];

    private long lastTouchTime;
    private float[] lastTouchPos = new float[2];
    
    //do I really need these? Yes, I do!
    private int savedLevelPack = 0;
    private int savedChapter = 0;

    private LevelPackProvider LPP;
    private Model mModel;

    private PhysicsInterface physics;

    public FlowerMenu(Model model) {

	mModel = model;

	float[] center = {.20f,-1.0f, 0.0f };
	//mBee = new Bee(this);
	//	mCPB = new CircleProgressBarWidget(lpCount, center, .05f);
	//mBee.setState(GameState.MAIN_MENU_OPENING, TutorialInfo2.length);

	currLevelPackIndex = -1;	

	//Initiliaze the flowers randomly on the screen.
	for(int i =0;i<tiles.length;i++){
	    double r = Math.random();
	    float Sx = (float)(-1.5*r+(1-r)*1.5);
	    r = Math.random();
	    float Sy = (float)(-1.5*r+(1-r)*1.5);
	    tiles[i] = new FlowerTile(Sx,Sy, flowerSize);
	}
	
	physics = new Physics(tiles);

	//The state for MAIN_MENU:
	mFlowerState = FlowerState.MAIN_MENU;
	state = new MainMenu();		

	LPP = GlobalApplication.getLevelPackProvider();

    }
    
    public void setGeometry(float[] g){
	super.setGeometry(g);
    }

    //Update Flowers to have the approprieate colors!
    private void updateFlowers() {
    	for(int i =0;i<tiles.length;i++){
    	    tiles[i].setTextures(TextureManager.CLEAR, TextureManager.getFlowerTexture());
    	}	
    }
        
    //Update the state according to the internal state system
    private void updateState() {
	switch(mFlowerState) {
	case MAIN_MENU:
	    state = new MainMenu();			 
	    break;
	case BOOK_SELECT:
	    state = new LevelPackDisplay();
	    break;
	case CHAPTER_SELECT:
	    state = new ChapterDisplay();
	    break;
	default:
	    break;
	}
    }
    
    //A method which backs up the state of this class
    //This method also can set the state of Model and is
    //the only method that can do this.
    public void backState() {
	System.out.println("In backState");
	switch(mFlowerState) {
	case MAIN_MENU:
	    break;
	case BOOK_SELECT:
	    System.out.println("In backState: BOOK_SELECT");
	    mFlowerState = FlowerState.MAIN_MENU;
	    
	    mModel.setModelToMainMenuOpening();
	    break;
	case CHAPTER_SELECT:
	    mFlowerState = FlowerState.BOOK_SELECT;
	    break;
	default:
	    break;
	}
	updateState();
    }

    public void enterLevelPack() {
	mFlowerState = FlowerState.BOOK_SELECT;
	updateState();
    }

    public void enterChapterSelect() {
    	mFlowerState = FlowerState.CHAPTER_SELECT;
    	updateState();
    }
/*
    public void enterChapterEnd(){
    	if(mFlowerState == FlowerState.CHAPTER_SELECT)
    		((ChapterDisplay)state).chapterEnd();
    }
  */  
    public void setState(int index) {
    }
    
    /* 
     *(non-Javadoc) Swipe Handler for this class.
     * @see com.seventhharmonic.android.freebeeline.GraphicWidget#swipeHandler(java.lang.String)
     */ 

    public void swipeHandler(String direction) {
 
    	state.swipeHandler(direction);
    }
    
    public void touchHandler(float[] pt) {
	state.touchHandler(pt);
    }

    class MainMenu extends StateWidget {
	
	public MainMenu(){
	    physics.resetPhysics();
	    physics.setPhysics("main_menu");
	    
	}
	
	@Override
	public void enterAnimation() {
	    period = DrawPeriod.DURING;
	}
	
	@Override
	public void duringAnimation(){
	}
	
	@Override
	public void draw(MyGLRenderer r){
	    super.draw(r);
	    physics.draw(r);
	}
	
	@Override
	public void swipeHandler(String direction) {
	    physics.swipeHandler(direction);
	    return;
	}
	
	@Override
	public void touchHandler(float[] pt) {
	    physics.touchHandler(pt);
	    return;
	}
    }    

    class LevelPackDisplay extends StateWidget {
	ScreenSlideWidgetLayout m;
	public LevelPackDisplay(){
	    m = new ScreenSlideWidgetLayout(2.0f);
	    m.setDrawProgressBar(true);
	    for(int i =0;i<LPP.getNumberOfLevelPacks();i++){
	    	m.addWidget(new LevelPackWidget(TextureManager.CLEAR, LPP.getLevelPack(i).getCurrTitleImage()));
	    }
	    m.setActiveWidget(savedLevelPack);
	    currLevelPack = LPP.getLevelPack(savedLevelPack);

	    physics.resetPhysics();
	    physics.setPhysics(currLevelPack.getStyle());
	}
	
	@Override
	public void enterAnimation() {
	    period = DrawPeriod.DURING;
	}
	
	@Override
	public void duringAnimation(){
	}
	
	@Override
	public void draw(MyGLRenderer r){
	    super.draw(r);
	    physics.draw(r);
	    m.draw(r);

	}
	
	@Override
	public void swipeHandler(String direction) {
	    physics.swipeHandler(direction);
	    m.swipeHandler(direction);
	    physics.resetPhysics();
	    physics.setPhysics(currLevelPack.getStyle());
	}
	
	@Override
	public void touchHandler(float[] pt) {
	    currLevelPack = LPP.getLevelPack(m.getActiveWidget());
	    if(m.getWidget(m.getActiveWidget()).isTouched(pt)){
		if(savedLevelPack != m.activeWidget){
		    savedLevelPack = m.activeWidget;
		    savedChapter = 0;
		}
		Log.d(TAG,"touched LevelPackDisplay");
		if(m.isTouched(pt)){
		    mFlowerState = FlowerState.CHAPTER_SELECT;
		    currLevelPack = currLevelPack;
		    updateState();
		}	
	    }
	    m.touchHandler(pt);
	    physics.touchHandler(pt);	    
	}
    }

    class ChapterDisplay extends StateWidget{
	ScreenSlideWidgetLayout m;
	Widget currChapterWidget;
	ButtonWidget gridToggle;
	AnimatedGIFWidget mGIF;

	
	public ChapterDisplay(){
	    gridToggle = new ButtonWidget(-.85f,-1,.15f, .15f, TextureManager.CLOSEDCIRCLE );
	    //1-.15f,GlobalApplication.getGeometry().getGeometry()[1]+.15f
	    gridToggle.setBorderStyle(ButtonWidget.ButtonStyle.SQUARE);
	    gridToggle.setClickListener(new GameEventListener(){ 
		    public void event(int i){
			for(Widget w: m.getWidgetList())
			    ((ChapterWidget)w).setState();
		    }
		});
	    m = new ScreenSlideWidgetLayout(2.0f);
	    for(int i =0;i<currLevelPack.getNumberOfChapters();i++){
		final Chapter c = currLevelPack.getChapter(i);
		ChapterWidget ch  = new ChapterWidget(c);
		ch.setTouchListener(new GameEventListener() {
			public void event(int puzz){
			    Puzzle p = c.getPuzzle(puzz);
			    mModel.setModelToGameOpening(p);
			}
		    });
		m.addWidget(ch);
	    }
	    m.setActiveWidget(savedChapter);
	    currChapterWidget = m.getWidget(m.getActiveWidget());
	    mGIF = new AnimatedGIFWidget(currLevelPack);
	}
	
	@Override
	public void enterAnimation() {
	    period = DrawPeriod.DURING;
	}
	
	@Override
	public void duringAnimation(){
	}
	
	@Override
	public void draw(MyGLRenderer r){
		//physics.draw(r);
	    mGIF.draw(r);

	    super.draw(r);
	    m.draw(r);
	    gridToggle.draw(r);
	}
	
	@Override
	public void swipeHandler(String direction) {
		/* TODO: Can do this by inheriting from ScreenSlideWidget and having that call a method
		 * which sets a flag in a chapter widget.
		*/
		m.swipeHandler(direction);
		mGIF.setTargetFrame(m.getActiveWidget());
		Log.d(TAG, Integer.toString(m.getActiveWidget()));
	}
/*
	public void chapterEnd(){
		((ChapterWidget)currChapterWidget).setFinishedChapter();
	}
*/	
	@Override
	public void touchHandler(float[] pt) {	
	    m.touchHandler(pt);
	    currChapterWidget = m.getWidget(m.getActiveWidget());
	    savedChapter = m.activeWidget;
	    currChapterWidget.touchHandler(pt);
	    gridToggle.touchHandler(pt);
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
