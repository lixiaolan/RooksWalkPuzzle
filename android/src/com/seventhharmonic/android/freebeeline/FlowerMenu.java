package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.State.DrawPeriod;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
import com.seventhharmonic.android.freebeeline.util.LATools;
import com.seventhharmonic.com.freebeeline.levelresources.*;
import com.seventhharmonic.android.freebeeline.listeners.*;
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

    private LevelPackProvider LPP;

    protected float flowerSize = .15f;
    private static final int flowerCount = 25; 
    private FlowerTile[] tiles = new FlowerTile[flowerCount];

    private long lastTouchTime;
    private float[] lastTouchPos = new float[2];


    
    //do I really need these? Yes, I do!
    private int savedLevelPack = 0;
    private int savedChapter = 0;

    private LevelPackProvider mLPP;	        
    private Model mModel;

    private Map <String, StateWidget> str2physics = new HashMap<String, StateWidget>();

    public FlowerMenu(Model model) {

	mModel = model;

	float[] center = {.20f,-1.0f, 0.0f };
	//mBee = new Bee(this);
	//	mCPB = new CircleProgressBarWidget(lpCount, center, .05f);
	//mBee.setState(GameState.MAIN_MENU_OPENING, TutorialInfo2.length);

	currLevelPackIndex = -1;
	
	//mBanner = new ImageWidget(0,0,.4f,.4f,TextureManager.LP1);
	//mBanner.setColor("white");
	//mBanner.setBackground(TextureManager.BOX);
	

	//Initiliaze the flowers randomly on the screen.
	for(int i =0;i<tiles.length;i++){
	    double r = Math.random();
	    float Sx = (float)(-1.5*r+(1-r)*1.5);
	    r = Math.random();
	    float Sy = (float)(-1.5*r+(1-r)*1.5);
	    tiles[i] = new FlowerTile(Sx,Sy, flowerSize);
	}
	

	//These need to go here:
	str2physics.put("main_menu", new FlowerMainMenu());
	str2physics.put("default", new LevelPack1());

	//The state for MAIN_MENU:
	mFlowerState = FlowerState.MAIN_MENU;
	state = new MainMenu();		

	LPP = GlobalApplication.getLevelPackProvider();

    }
    
    public void setGeometry(float[] g){
	super.setGeometry(g);
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
	System.out.println("enterLevelPack");
	mFlowerState = FlowerState.BOOK_SELECT;
	updateState();
    }

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

    //This maps the proper physics to the proper string.
        
    //TODO: Add the flower drawing to this class:
    class MainMenu extends StateWidget {
	StateWidget flowerState;
	
	public MainMenu(){
	    str2physics.get("main_menu").reset();
	    flowerState = str2physics.get("main_menu");
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
	    flowerState.draw(r);
	}
	
	@Override
	public void swipeHandler(String direction) {
	    flowerState.swipeHandler(direction);
	    return;
	}
	
	@Override
	public void touchHandler(float[] pt) {
	    flowerState.touchHandler(pt);
	    return;
	}
    }    

    class LevelPackDisplay extends StateWidget {
	ScreenSlideWidgetLayout m;
	StateWidget flowerState;
	public LevelPackDisplay(){
	    m = new ScreenSlideWidgetLayout(2.0f);
	    m.setDrawProgressBar(true);
	    for(int i =0;i<LPP.getNumberOfLevelPacks();i++){
		m.addWidget(new LevelPackWidget(TextureManager.CLEAR, "book1chapter0"));
		//m.addWidget(new LevelPackWidget(mLPP.getLevelPack(i).getTitle(),"forest"));
	    }
	    m.setActiveWidget(savedLevelPack);
	    currLevelPack = LPP.getLevelPack(savedLevelPack);
	    str2physics.get(currLevelPack.getStyle()).reset();
	    flowerState = str2physics.get(currLevelPack.getStyle());
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
	    flowerState.draw(r);
	    m.draw(r);
    
	}
	
	@Override
	public void swipeHandler(String direction) {
	    flowerState.swipeHandler(direction);
	    m.swipeHandler(direction);
	    str2physics.get(currLevelPack.getStyle()).reset();
	    flowerState = str2physics.get(currLevelPack.getStyle());
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
	    flowerState.touchHandler(pt);	    
	}
    }

    class ChapterDisplay extends StateWidget{
	ScreenSlideWidgetLayout m;
	Widget currChapterWidget;
	
	public ChapterDisplay(){
	    m = new ScreenSlideWidgetLayout(2.0f);

	    for(int i =0;i<currLevelPack.getNumberOfChapters();i++){
		
		//If the previous chapter is completed, launch a normal chapter widget
		if(i==0 || currLevelPack.getChapter(i-1).getCompleted()){
		    final Chapter c = currLevelPack.getChapter(i);
		    ChapterWidget ch  = new ChapterWidget(c);
		    ch.setTouchListener(new GameEventListener() {
			    public void event(int puzz){
				Puzzle p = c.getPuzzle(puzz);
				mModel.setModelToGamePlayOpening(p);
			    }
			});
		    m.addWidget(ch);
		}
		
		//Otherwise lock the user out!
		else {
		    m.addWidget(new LockedChapterWidget());
		}
	    }
	    m.setActiveWidget(savedChapter);
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
	    m.draw(r);
	}
	
	@Override
	public void swipeHandler(String direction) {
	    m.swipeHandler(direction);
	    
	}
	
	@Override
	public void touchHandler(float[] pt) {	
	    m.touchHandler(pt);
	    currChapterWidget = m.getWidget(m.getActiveWidget());
	    savedChapter = m.activeWidget;
	    currChapterWidget.touchHandler(pt);
	}
    }

    //Used for physics of flowers.
    class FlowerMainMenu extends StateWidget {
	long refTime;
	float[] centers;
	
	public FlowerMainMenu() {
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
	public void reset() {
	    refTime = System.currentTimeMillis();
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
	long createdTime;
	long refTime;
	float[] centers;
	float[] fixedCenters;
	
	public LevelPack1() {
	    //mBee.setMood(Mood.HAPPY);
	    //Initialize tiles to have a random velocity.
	    centers = new float[2*tiles.length];
	    fixedCenters = new float[2*tiles.length];

	    for (int i = 0; i < tiles.length; i++) {
		double r = Math.random();
		tiles[i].velocity[0] = (float)(-1*r+(1-r)*1);
		r = Math.random();
		tiles[i].velocity[1] = (float)(-1*r+(1-r)*1);
		tiles[i].setTextures(TextureManager.CLEAR, TextureManager.getFlowerTexture());
		tiles[i].setSize(flowerSize);
	    }

	    //Initiate the centers array.
	    createdTime = System.currentTimeMillis();
	    refTime = System.currentTimeMillis();
	    float GlobalTime = ((float)(createdTime - refTime))/1000.0f;
	    System.out.println("HSDF:LKJSDLKFJLS:DFJK:LSDFJ" + Float.toString(GlobalTime));
	    for (int i = 0; i<tiles.length; i++ ) {
		float ii = (float)i;
		float r = (ii + 10*(1-1/(ii+1)))/25;
		float t = ii/1.0f; 
		fixedCenters[2*i] =  r*((float)Math.sin(t));
		fixedCenters[2*i+1] = r*((float)Math.cos(t));
		centers[2*i]   = ((float)Math.cos(GlobalTime))*fixedCenters[2*i] 
		    + ((float)Math.sin(GlobalTime))*fixedCenters[2*i + 1];
		centers[2*i+1]   = -((float)Math.sin(GlobalTime))*fixedCenters[2*i] 
		    + ((float)Math.cos(GlobalTime))*fixedCenters[2*i + 1];	
	    }
	}
	
	@Override
	public void reset() {
	    refTime = System.currentTimeMillis();
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
	    float GlobalTime = ((float)(createdTime - refTime))/1000.0f;

	    for (int i = 0; i<tiles.length; i++ ) {
		centers[2*i]   = ((float)Math.cos(GlobalTime))*fixedCenters[2*i] 
		    + ((float)Math.sin(GlobalTime))*fixedCenters[2*i + 1];
		centers[2*i+1]   = -((float)Math.sin(GlobalTime))*fixedCenters[2*i] 
		    + ((float)Math.cos(GlobalTime))*fixedCenters[2*i + 1];	
	    }
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
	    //mCPB.draw(r);
	    for(int i =0;i< tiles.length;i++){
		tiles[i].draw(r);
	    }
	    //mBanner.draw(r);
	    //slider.draw(r);	    
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
