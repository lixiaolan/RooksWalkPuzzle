package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.State.DrawPeriod;
import com.seventhharmonic.android.freebeeline.graphics.TextCreator;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
import com.seventhharmonic.android.freebeeline.util.LATools;
import com.seventhharmonic.com.freebeeline.levelresources.*;
import com.seventhharmonic.android.freebeeline.listeners.*;
import com.seventhharmonic.android.freebeeline.physics.*;
import com.seventhharmonic.com.freebeeline.levelresources.LevelPack;
import com.seventhharmonic.com.freebeeline.levelresources.LevelPackProvider;
import java.util.HashMap;
import java.util.Map;

class FlowerMenu extends GraphicWidget implements BeeFlowerMenuInterface {
    
    //This is the internal state system this class will use:
    //MAIN_MENU: Opening game menu
    //BOOK_SELECT: Selecting a puzzle book (uses two overlayed widgits);
    //CHAPTER_SELECT: The state that used to be handled by Table of Contents.

    //The enum and its member
    enum FlowerState {
	MAIN_MENU, BOOK_SELECT, CHAPTER_SELECT, CHAPTER_END
    }
    private FlowerState mFlowerState;

    //Tag used for logs
    private String TAG = "FlowerMenu";
    
    // "slider" used for placing a screen slide widget on top of stat system.
    // This is a bit of a hack... fair warning.
    //private StateWidget flowerState;

    //The bee that lives in the flowers
    private NewBee mBee;
    private FlowerMenuBeeController mBeeController;

    private LevelPack currLevelPack;
    private int currLevelPackIndex;

    protected float flowerSize = .15f;
    private static final int flowerCount = 25; 
    private FlowerTile[] tiles = new FlowerTile[flowerCount];

    private MyMusic myMusic = GlobalApplication.getMyMusic();

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
	
	//Note that the bee gets constructed inside of the controller...
	//That feels a bit strange

	mBeeController = new FlowerMenuBeeController(this);
	mBee = new NewBee(mBeeController);
	mBeeController.setBee(mBee);

	currLevelPackIndex = -1;	

	//Initialize the flowers randomly on the screen.
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
	case CHAPTER_END:
	    state = new ChapterEnd();
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
	    //myMusic.stopMusic();
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

    /**If the chapter has ended. This triggers a series of wonderful events at the state level.
     * 
     */
    public void enterChapterEnd(){
    	mFlowerState = FlowerState.CHAPTER_END;
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
	    mBee.draw(r);
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

	    //	    myMusic.playSong(currLevelPack.getSong());
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
	    mBee.draw(r);
	}
	
	@Override
	public void swipeHandler(String direction) {
	    physics.swipeHandler(direction);
	    m.swipeHandler(direction);
	    physics.resetPhysics();
	    physics.setPhysics(currLevelPack.getStyle());
	    //myMusic.playSong(currLevelPack.getSong());
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
	ScreenSlideWidgetLayout m;	//Widget to hold all the chapters
	Widget currChapterWidget;	//The current chapter that the screenslide widget is on.
	ButtonWidget gridToggle;	//Toggles the showing of puzzles
	AnimatedGIFWidget mGIF;		//Displays the backgrounds and handles their animation

	
	public ChapterDisplay(){
		//Set up the grid toggle
		gridToggle = new TextToggleButtonWidget(1f-.15f,-1*GlobalApplication.getGeometry().getGeometry()[1]+.15f, .15f, .15f, "hide", "show" );
	    gridToggle.setBorderStyle(ButtonWidget.ButtonStyle.CIRCLE);
	    //Yes - we do toggle all the grids on ALL the puzzles.
	    gridToggle.setClickListener(new GameEventListener(){ 
		    public void event(int i){
			for(Widget w: m.getWidgetList())
			    ((ChapterWidget)w).setState();
		    }
		});
	    
	    //Now populate the widget that holds onto all the chapters.
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
	    
	    //Widget that shows our backgrounds.
	    mGIF = new AnimatedGIFWidget(currLevelPack);
	    
	    //Say that a user was staring at chapter 4 - then we should move the slider ahead to that
	    //if the saved chapter was 0, then we jump to the most current chapter.
	    if(savedChapter != 0){
	    	m.setActiveWidget(savedChapter);
	    	mGIF.setKeyFrame(savedChapter);
	    } else {
	    	m.setActiveWidget(currLevelPack.getCurrChapter());
	    	mGIF.setKeyFrame(currLevelPack.getCurrChapter());
	    }
	    
	    //Set the current chapter appropriately.
	    currChapterWidget = m.getWidget(m.getActiveWidget());
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

	/** This carries out a series of wonderful events when the chapter is ended.
	 *  1. Display a congratulations.
	 *  2. Play an animation on the GIF
	 *  3. On Click, move the chapter widget forward.
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

    class ChapterEnd extends StateWidget{
    	AnimatedGIFWidget mGIF;		//Displays the backgrounds and handles their animation
    	TextBox mText;
    	float h = GlobalApplication.getGeometry().getGeometry()[1];
    	
    	public ChapterEnd(){
    		mGIF = new AnimatedGIFWidget(currLevelPack);
    		mGIF.setSpeedMultiplier(500);
    		mGIF.setKeyFrame(savedChapter);
    		mGIF.setTargetFrame(savedChapter+1);
    		String text = "          Congratulations! ^You helped Beatrice finish Chapter "+Integer.toString(savedChapter+1)+" .^ Click to move onto the next chapter.";
    	
    		mText = new TextBox(0, h -.2f,.9f, text);
    		mText.setFontSize(TextCreator.font1);
    		//TODO: This sucks. There must be a more flexible way to figure out the gometry.
    		mText.setBorder(true, 0, h-.4f, .9f,.3f);
    		mText.setColor("opaque");
    	}

		@Override
		public void enterAnimation() {
			if(mGIF.animationFinished()) {
				mGIF.setKeyFrame(savedChapter);
				mGIF.setTargetFrame(savedChapter+1);
			}
		}

		@Override
		public void duringAnimation() {}

		@Override
		public void swipeHandler(String direction) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void touchHandler(float[] pt) {
			savedChapter = savedChapter+1;
			mFlowerState  = FlowerState.CHAPTER_SELECT;
			updateState();
		}
		
		@Override
		public void draw(MyGLRenderer r){
			super.draw(r);
			mGIF.draw(r);			
			mText.draw(r);
		}
    	
    }

    @Override
    public FlowerTile getTile(int i){
	return (i < 0 || i >= flowerCount) ? tiles[0] : tiles[i]; 
    }

    @Override
    public int getFlowerCount() {
	return flowerCount;
    }
    @Override
    public void setTileRotate(int i) {
	return;
    }


    @Override
    public void setState() {
	return;
    }
}
