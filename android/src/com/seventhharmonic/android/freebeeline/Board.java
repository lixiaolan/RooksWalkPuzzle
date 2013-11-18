package com.seventhharmonic.android.freebeeline;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.db.HintsDataSource;
import com.seventhharmonic.android.freebeeline.db.PurchasedDataSource;
import com.seventhharmonic.android.freebeeline.graphics.TextCreator;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
import com.seventhharmonic.android.freebeeline.graphics.TextCreator.TextJustification;
import com.seventhharmonic.android.freebeeline.listeners.GameEventListener;
import com.seventhharmonic.com.freebeeline.levelresources.Hint;
import com.seventhharmonic.com.freebeeline.levelresources.Puzzle;

class Board extends Graphic<BoardTile, State<BoardTile> > implements BeeBoardInterface{
	String TAG = "board";
	public int hints;
	public int[] solution ;
	public int[][] path;
	public int boardWidth = 6;
	public int boardHeight = 6;

	private boolean toggleHints = true;
	private boolean toggleLines = true;	
	private boolean toggleError = true;
	protected float flowerSize = .15f;
	protected float tileSize = .11f;    
	private ErrorLog mErrorLog;
	private TextBox mGameBanner;
	private ImageWidget mBoardBg;
	private Puzzle currPuzzle;
	Model mModel;
	private NewBee mBee;
	private BoardBeeController mBeeController;
	private PurchasedDataSource PDS;
	protected BoardLineManager mBoardLineManager;
	//Number of moves in a puzzle.
	int moves = 0;
	//Par number of moves
	int par  = 0;


	public Board(Model mModel) {
		buildEmptyBoard();
		state = null;//new BoardPlay(tiles);
		mGameBanner = new TextBox(0.0f, 0.0f, .9f, "");
		mGameBanner.setFontSize(TextCreator.font1);
		mBoardBg = new ImageWidget(0,0,1.0f, 1.0f, "boardbg");
		mErrorLog = new ErrorLog(this);
		this.mModel = mModel;

		mBeeController = new BoardBeeController(this);
		mBee = new NewBee(mBeeController);
		mBeeController.setBee(mBee);
		mBee.setModeFast();
		mBoardLineManager = new BoardLineManager(this);

	}    

	public void restoreBoard(int[] solution, String[] numbers, String[] arrows, String[] trueArrows, int[][] path, boolean[] clickable){
		for(int i=0;i<boardWidth*boardHeight;i++){
			tiles[i].setTrueSolution(solution[i]);
			tiles[i].setArrow(arrows[i]);
			tiles[i].setNumber(numbers[i]);
			tiles[i].setTrueArrow(trueArrows[i]);
			if(clickable!=null)
				if(!clickable[i] && tiles[i].getTrueSolution()!=-1)
					tiles[i].setHint();
			tiles[i].setTextures();
		}
		this.path = path;
	}

	public void buildXMLBoard(int[] solution, int[][] path, int height, int width, int[] hints){
		this.path = path;
		int tilesLength = height*width;
		Log.d(TAG,Integer.toString(tilesLength));

		for (int i = 0; i < tilesLength; i++) {	
			//This does a hard reset on the board.			
			tiles[i].number = TextureManager.CLEAR;
			tiles[i].arrow = TextureManager.CLEAR;
			tiles[i].setTrueArrow(TextureManager.CLEAR);
			tiles[i].setTrueSolution(solution[i]);
		} 

		int dx;
		int dy;
		int length = (path.length);

		for (int i = 0; i < length-1; i++) {
			dx = path[i+1][0]-path[i][0];
			dy = path[i+1][1]-path[i][1];
			Log.d("path","path creation");
			Log.d("path", "length "+Float.toString(length));
			Log.d("path", "dx "+Float.toString(dx));
			Log.d("path", "dy "+Float.toString(dy));
			if (dx > 0) {
				tiles[boardHeight*path[i+1][0] + path[i+1][1]].setTrueArrow("right_arrow");
				//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "right_arrow";
			}

			if (dx < 0) {
				tiles[boardHeight*path[i+1][0] + path[i+1][1]].setTrueArrow("left_arrow");
				//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "left_arrow";
			}

			if (dy > 0) {
				tiles[boardHeight*path[i+1][0] + path[i+1][1]].setTrueArrow("down_arrow");
				//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "down_arrow";
			}
			if (dy < 0) {
				tiles[boardHeight*path[i+1][0] + path[i+1][1]].setTrueArrow("up_arrow");
				//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "up_arrow";
			}
			Log.d("path", tiles[boardHeight*path[i][0] + path[i][1]].getTrueArrow());
		}

		if(toggleHints){
			for(int i=0;i<hints.length;i++){
				int r = hints[i];
				Log.d(TAG, "hint");
				Log.d(TAG, Integer.toString(r));
				tiles[r].setHint();
			}
		}

	}

	public void buildEmptyBoard() {
		tiles = new BoardTile[boardHeight*boardWidth];
		float size = .15f;
		for (int i = 0; i < tiles.length; i++) {
			double r = Math.random();
			float Sx = (float)(-1.5*r+(1-r)*1.5);
			r = Math.random();
			float Sy = (float)(-1.5*r+(1-r)*1.5);
			float center[] = { Sx, Sy, 0.0f};
			tiles[i] = new BoardTile(center, size);
		}
	}

	public void buildBoardFromSolution(int hints) {
		List<Integer> numbers = new ArrayList<Integer>();
		for (int i = 0; i < tiles.length; i++) {	
			//This does a hard reset on the board.
			tiles[i].number = TextureManager.CLEAR;
			tiles[i].arrow = TextureManager.CLEAR;
			tiles[i].setTrueSolution(0);
			tiles[i].setTrueArrow(TextureManager.CLEAR);
			tiles[i].rotate = false;
			if(solution [i] > 0){
				numbers.add(i);
				//tiles[i].setNumber(Integer.toString(solution[i]));
			}
			//Set the solution in the tile
			tiles[i].setTrueSolution(solution[i]);
		} 
		int dx;
		int dy;
		int length = (path.length)/2;
		for (int i = 0; i < length-1; i++) {
			dx = path[i+1][0]-path[i][0];
			dy = path[i+1][1]-path[i][1];

			if (dx > 0) {
				tiles[boardHeight*path[i+1][0] + path[i+1][1]].setTrueArrow("right_arrow");
				//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "right_arrow";
			}
			if (dx < 0) {
				tiles[boardHeight*path[i+1][0] + path[i+1][1]].setTrueArrow("left_arrow");
				//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "left_arrow";
			}
			if (dy > 0) {
				tiles[boardHeight*path[i+1][0] + path[i+1][1]].setTrueArrow("down_arrow");
				//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "down_arrow";
			}
			if (dy < 0) {
				tiles[boardHeight*path[i+1][0] + path[i+1][1]].setTrueArrow("up_arrow");
				//tiles[6*path[i+1][0] + path[i+1][1]].arrow = "up_arrow";
			}
		}
		//Note we are assuming that hints<numbers and that we have a unique solution
		if(toggleHints){
			Collections.shuffle(numbers);
			for(int i=0;i<hints;i++){
				int r = numbers.get(i);
				tiles[r].setHint();
			}
		}
	}

	public void resetBoard() {
		for(int i=0;i<tiles.length;i++){
			if(tiles[i].isClickable()){
				tiles[i].setNumber(TextureManager.CLEAR);
				tiles[i].setArrow(TextureManager.CLEAR);
				tiles[i].setColor("transparent");
			}
		}
		if(toggleLines){
			mBoardLineManager.drawLines();
		}
	}

	public void createPuzzleFromPuzzle(Puzzle p){
		boardHeight = p.getHeight();
		boardWidth = p.getWidth();
		System.out.println("boardHeight: " + Integer.toString(boardHeight));
		System.out.println("boardWidth: " + Integer.toString(boardWidth));
		currPuzzle = p;
		buildEmptyBoard();
		mBoardLineManager.boardHeight = boardHeight;
		mBoardLineManager.boardWidth = boardWidth;
		mBoardLineManager.tiles = tiles;
		int[] solution = p.getBoard();
		int[][] path = p.getPath();

		List<Hint> hints = p.getAllHints();
		int[] hintList = new int[hints.size()];
		for(int i = 0; i< hints.size();i++){
			hintList[i] = hints.get(i).getIndex();
		}
		buildXMLBoard(solution, path, p.getHeight(), p.getWidth(), hintList);
	}

	public int touched(float[] pt) {

		for (int i = 0; i < tiles.length; i++) {
			if( tiles[i].touched(pt) && tiles[i].isClickable() && !tiles[i].isBlack()) {
				return i;
			}
		}
		return -1;
	}

	public void touchHandler(float[] pt){
		state.touchHandler(pt);
	}

	public void swipeHandler(String direction) {
		state.swipeHandler(direction);
	}

	public void setState(GameState s){

		switch(s) {
		case GAME_OPENING: 
			state  = new BoardPlay(tiles); 
			mBeeController.setMood(Mood.ASLEEP); 
			break;
		case GAME_MENU_END: 
			state = new BoardGameEnd(tiles); 
			mBeeController.setMood(Mood.HAPPY); 
			break;
		default: break;
		}
	}

	public boolean checkSolution(){
		for(int i =0; i < boardHeight*boardWidth; i++){
			if(!( tiles[i].checkArrows() && tiles[i].checkSolutions() ) ){ 
				return false;
			}
		}
		return true;
	}

	public void toggleLines(boolean toggle){
		toggleLines = toggle;
	}

	public void toggleHints(boolean toggle){
		toggleHints = toggle;
	}

	public void toggleRules(boolean toggle){
		toggleError = toggle;
	}

	public int beeTouched(float[] pt) {
		return mBeeController.touched(pt);
	}

	/**
	 * Show a hint on the board. This should be triggered when the bee is clicked.
	 */
	public void showHint() {
		for(int i =0; i< tiles.length; i++){
			if(tiles[i].getTrueSolution() > 0 && !tiles[i].isHint()){
				System.out.println("Found a potential tile");
				if(tiles[i].textures[0].equals(TextureManager.CLEAR) || tiles[i].textures[1].equals(TextureManager.CLEAR)){
					//tiles[i].setSolution();
					//tiles[i].setTextures();
					tiles[i].setHint();
					mBoardLineManager.drawLines();

					break;
				}
			}
		}
	}

	/*
	 * Show solution at the end of the game. Currently just a utility function.
	 */
	public void showSolution(){
		String sol = "";
		for(int i =0;i <tiles.length;i++){
			if(tiles[i].getTrueSolution() == 0)
				sol  = "clear";
			else
				sol = Integer.toString(tiles[i].getTrueSolution());
			tiles[i].setNumber(sol);
			tiles[i].setArrow(tiles[i].getTrueArrow());
		}
	}

	/*
	 *	Get's called by Model's set geometry.
	 */
	public void setGeometry(float[] g) {
		super.setGeometry(g);
		mBoardLineManager.setGeometry(g);

		//Place the banner halfway between the board and the top of the screen.
		mGameBanner.setCenter(0, (g[1]+1)/2);
	}

	/////////////////////////////////////////////////////////////////////////////	

	////////////////////////////////////////////////////////////////////////////////
	//Bee Board Interface Methods:

	public BoardTile getTile(int index) {
		if (index >= tiles.length) {
			return tiles[0];
		}
		return tiles[index];
	}

	public int getBoardHeight() {
		return boardHeight;
	}

	public int getBoardWidth() {
		return boardWidth;
	}

	public int getPathLength() {
		if (path != null) {
			return path.length;
		}
		return 0;
	}

	public void setTileRotate(int index) {
		if (index < tiles.length) {
			tiles[index].rotate = true;	    
		}
	}

	public int getPathToArray(int index) {
		if (path != null) {
			return boardHeight*path[index][0] + path[index][1];
		}
		return 0;
	}
	////////////////////////////////////////////////////////////////////////////////


	//This state defines board behavior during game play.
	class BoardPlay extends State<BoardTile> {

		public Menu mMenu;
		public Tile[] originalTiles;
		public long refTime;
		public boolean swap = false;
		public float initSize;
		public float finalSize = .12f;
		public float[] oldX;
		public float[] oldY;
		int at = -1;
		int lt = -1;
		ButtonWidget reset;
		ButtonWidget tutorial;
		HintsDataSource DB;
		TextBox mHints;
		TextBox mMoves;
		HintDialogWidgetLayout mHintDialog;
		Store mStore;
		GridWidgetLayout buttonGrid;
		//ButtonWidget mCheck;

		//TODO: Get rid of tiles as inputs to these functions.
		public BoardPlay(BoardTile[] tiles) {
			
			par = (int)(2*(path.length-currPuzzle.getNumberOfHints()));
			moves = 0;
			
			DB = GlobalApplication.getHintDB();
			mStore = ViewActivity.mStore;

			originalTiles = tiles;
			refTime = System.currentTimeMillis();
			oldX = new float[tiles.length];
			oldY = new float[tiles.length];

			mMenu = new Menu(boardHeight);

			mGameBanner.setJ(TextJustification.LEFT);
			mGameBanner.setFontSize(TextCreator.font1);

			if(boardWidth == 6){
				mBoardBg = new ImageWidget(0,0, 1.0f,1.0f,"boardbg");
			} else if(boardWidth == 5){
				float mBoardBgScale = 5.0f/6.0f;
				mBoardBg = new ImageWidget(0,0,mBoardBgScale,mBoardBgScale,TextureManager.BOARD5);
			}

			reset = new ButtonWidget(0, -1.0f, .11f, .11f, TextureManager.ERASER);
			reset.setBorderStyle(ButtonWidget.ButtonStyle.SQUARE);
			reset.setClickListener(new GameEventListener(){
				public void event(int i){
					resetBoard();
				}
			});

			tutorial = new ButtonWidget(-.7f, 1.0f, .11f, .11f, TextureManager.QUESTIONMARK);
			tutorial.setBorderStyle(ButtonWidget.ButtonStyle.SQUARE);
			tutorial.setClickListener(new GameEventListener(){
				public void event(int i){
					mModel.createTutorial();
					mModel.setState(GameState.TUTORIAL);
				}
			});


			/*
			 * The hints text widget next to the bee.
			 * Note: If we can't open the DB we will crash out the program!! That is not a bad thing.
			 */
			mHints = new TextBox(0, 0, .12f, "");
			mHints.setJ(TextJustification.CENTER);
			if(mStore.hasUnlimitedHints()){
				mHints.setText(TextureManager.HIVE);
			} else {
				mHints.setText(TextureManager.buildHint(DB.getHints().getNum()));
			}

			//mCheck  = new ButtonWidget(0, 0, .11f, .11f, TextureManager.CHECK);
			//mCheck.setBorderStyle(ButtonWidget.ButtonStyle.SQUARE);

			//Text Box with number of moves 
			mMoves = new TextBox(.33f, mBoardBg.getCenterY()-mBoardBg.getHeight()-.3f, .22f, "moves:^ 0");
			updateMoves();
			mMoves.setJ(TextJustification.CENTER);
			
			
			//Grid Widget to store all these wonderful buttons.
		
			buttonGrid = new GridWidgetLayout(4,1, .18f);
			buttonGrid.setCenter(0, mBoardBg.getCenterY()-mBoardBg.getHeight()-.1f);
			buttonGrid.addWidget(mHints);
			buttonGrid.addWidget(mMoves);
			buttonGrid.addWidget(reset);
			buttonGrid.addWidget(tutorial);

			
			 // The dialog box that appears when you run out of hints.
			 
			mHintDialog = new HintDialogWidgetLayout(mHints);
			mGameBanner.setText(currPuzzle.getText());
			initSize = tiles[0].getSize();

			for (int i = 0; i < tiles.length; i++) {
				tiles[i].setRotate(false);// = false;
				tiles[i].setTextures(TextureManager.CLEAR, tiles[i].flowerTexture);
				oldX[i] = tiles[i].center[0];
				oldY[i] = tiles[i].center[1];
				tiles[i].setColor("transparent");
				tiles[i].vPointedAt = false;
				tiles[i].hPointedAt = false;
			}	    
			
			
		}

		public void enterAnimation(BoardTile[] tiles) {
			long time = System.currentTimeMillis() - refTime;
			float totalTime = 2000f;
			// This does a simultaneous snap to position and shrink of tiles.
			if(time < totalTime/3) {
				for (int i = 0; i < tiles.length; i++) {
					float Sx = ( (i/boardHeight) - boardWidth/2.0f + 0.5f )/4.0f;
					float Sy = ( (i%boardHeight) - boardWidth/2.0f + 0.5f )/4.0f;
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
						float Sx = ( (i/boardHeight) - boardWidth/2.0f + 0.5f )/4.0f;
						float Sy = ( (i%boardHeight) - boardWidth/2.0f + 0.5f )/4.0f;
						float center[] = {Sx, Sy, 0.0f};
						tiles[i].center = center;
						if(!tiles[i].isClickable() && !tiles[i].isBlack())
							tiles[i].setHint();
						else 
							tiles[i].nativeColor = "transparent";
					}
				}
				for (int i = 0; i < tiles.length; i++) {
					tiles[i].setSize(finalSize*(3*time/totalTime-2));
				}
			}

			else {
				for(int i = 0;i<tiles.length;i++){
					tiles[i].setTextures();
					tiles[i].setAngle(0);
					tiles[i].setSize(finalSize);
					float Sx = ( (i/boardHeight) - boardWidth/2.0f + 0.5f )/4.0f;
					float Sy = ( (i%boardHeight) - boardWidth/2.0f + 0.5f )/4.0f;
					tiles[i].setCenter(Sx, Sy);
					tiles[i].setTextures();
					if(!tiles[i].isClickable() && !tiles[i].isBlack())
						tiles[i].setHint();
					else
						tiles[i].nativeColor = "transparent";
					tiles[i].setAlpha(true);
				}

				if (toggleLines) {
					mBoardLineManager.drawLines();
				}
				period = DrawPeriod.DURING;
			}
		}

		public void duringAnimation(BoardTile[] tiles) {
			for (int i = 0; i < tiles.length; i++) {
				((BoardTile)tiles[i]).setTextures();
			}
		}

		public void draw(BoardTile[] tiles, MyGLRenderer r){
			mGameBanner.draw(r);
			mBoardBg.draw(r);
			super.draw(tiles, r);
			mBee.draw(r);
			mMenu.draw(r);
			mHintDialog.draw(r);
			buttonGrid.draw(r);
			mMoves.draw(r);
		}	

		@Override
		public void touchHandler(float[] pt){
			if(mHints.isTouched(pt)){
					if (GlobalApplication.getHintDB().useHint() || mStore.hasUnlimitedHints()) {
						showHint();
						setHintsText();
					} else {
						mHintDialog.activate();
					}
			}
			
				reset.touchHandler(pt);
				tutorial.touchHandler(pt);
				if(mHintDialog.isActive()){
					mHintDialog.touchHandler(pt);
					return;
				}
			
			int val = mMenu.touched(pt);
			if(val == -1){
				//This case corresponds to the menu being closed
				if (at != -1) {
					//If a tile was active close it. 
					tiles[at].setColor("transparent");
					//If this tile should be red, make sure it stays that way. Reset the banner
					if(toggleError){
						turnErrorRed(at);
						mGameBanner.setText("");
					}
				}

				at = touched(pt);
				if(at != -1 ) {
					//Open the menu at the tile that was touched. Display a banner if there is an error
					if(toggleError){
						turnErrorRed(at);
						if(lt == at){
							mGameBanner.setText(mErrorLog.getError(at));
						}
					}
					if(tiles[at].isBlack() == false) {
						tiles[at].setColor("blue");
						if(tiles[at].isClickable())
							mMenu.activate(pt);
					}
				}
			} else {
				if (at != -1) {
					//Set the user input since we get values
					tiles[at].setUserInput(val);
					moves+=1;
					updateMoves();
					if (toggleLines) {
						mBoardLineManager.animatePlay(at);
					}
					if(toggleError){
						mErrorLog.setLog();
						updateErrors();
						turnErrorRed(at);
						mGameBanner.setText(mErrorLog.getError(at));
						lt = at;
					}
				}
			}
			checkIfPuzzleSolved();
		}

		public void updateMoves(){
			mMoves.setText("moves:^"+Integer.toString(moves));
		}
		
		public void checkIfPuzzleSolved() {
		    if(checkSolution()) {	
		    	if(currPuzzle!=null){
			    Log.d("board", "solved a puzzle and set completed to true");
			    currPuzzle.setCompleted(true);
			    if(moves <= par){
			    	currPuzzle.setAward(TextureManager.GOLDSTAR);
			    }
			    mModel.state.saveCurrGame = false;
			    mModel.state.resumeGameExists = false;
			    mBeeController.setMood(Mood.HAPPY);
			    mModel.setState(GameState.GAME_MENU_END);
			    GlobalApplication.getPuzzleDB().setPuzzle(currPuzzle.getId(),"true");
			    
			}
		    }
		}

		public void setHintsText(){
			if(mStore.hasUnlimitedHints()){
				mHints.setText(TextureManager.HIVE);
			} else {
				mHints.setText(TextureManager.buildHint(GlobalApplication.getHintDB().getHints().getNum()));
			}
		}

		public void updateErrors(){
			for(int i =0;i<tiles.length;i++){
				String error = mErrorLog.getError(i); 
				if(error.equals("")){
					tiles[i].setColor(tiles[i].nativeColor);
					if(i == at){
						tiles[i].setColor("blue");
					}
				}
			}
		}

		public void turnErrorRed(int at){
			String error = mErrorLog.getError(at); 
			if(!error.equals("")){
				tiles[at].setColor("red");
			} 
		}

		public void swipeHandler(String direction){
			if (at != -1 && tiles[at].isClickable()) {
				tiles[at].setArrow(direction);
				tiles[at].setTextures();
				moves += 1;
				updateMoves();
				if (toggleLines) {
					mBoardLineManager.animatePlay(at); 
				}
				if(toggleError){
					mErrorLog.setLog();
					turnErrorRed(at);
					mGameBanner.setText(mErrorLog.getError(at));
					updateErrors();
				}
				mMenu.menuActive = false;
			}
			checkIfPuzzleSolved();
		}

	}
	//This is the board after completing the game.
	class BoardGameEnd extends State<BoardTile> {
		boolean[] rotateTiles = new boolean[boardHeight*boardWidth];
		boolean[] flipped = new boolean[boardHeight*boardWidth];
		long[] refTime = new long[boardHeight*boardWidth];
		EndGameDialogWidgetLayout mDialog;

		public BoardGameEnd(BoardTile[] tiles) {
			if(moves <= par){
				mGameBanner.setText("Perfect Puzzle Solution!");//+"^^"+TextureManager.PUZZLESLEFT+Integer.toString(currPuzzle.getChapter().getNumberPuzzlesIncomplete()));
			} else {
				mGameBanner.setText("Puzzle Completed");
			}
			mGameBanner.setFontSize(2);
			mGameBanner.setJ(TextJustification.CENTER);
			mDialog = new EndGameDialogWidgetLayout(.8f);
			mDialog.setCenter(0,(-1*mBoardBg.getHeight()-geometry[1])/2.0f);
			mDialog.setNextClickListener(new GameEventListener(){
				public void event(int i ){
					Log.d("Dialog","Clicked");
					mDialog.deactivate();
					if(currPuzzle.getNextPuzzle() != null) {
						Log.d("next puzzle", "Apparently, there is another puzzle!");

						//TODO: Created a hack here to ensure we recreate the current chapter widget
						mModel.setModelToGameOpening(currPuzzle.getNextPuzzle());
						setState(GameState.GAME_OPENING);

					} else {
						mModel.setModelToChapterEnd();
					}
				}

			});

			mDialog.setBackClickListener(new GameEventListener() {
				public void event(int i){
					mDialog.deactivate();
					if(currPuzzle.getNextPuzzle() == null){
						mModel.setModelToChapterEnd();
					}
					else{
						mModel.setModelToChapterSelect();
					}
				}

			});


			mDialog.activate();

			for (int i = 0; i < tiles.length; i++) {
				flipped[i] = false;
				rotateTiles[i] = false;
				float Sx = ( (i/boardHeight) - boardWidth/2.0f + 0.5f )/4.0f;
				float Sy = ( (i%boardHeight) - boardWidth/2.0f + 0.5f )/4.0f;
				tiles[i].setSize(.12f);
				tiles[i].setCenter(Sx, Sy);
				tiles[i].setColor("transparent");
				refTime[i] = System.currentTimeMillis();
				tiles[i].setAlpha(true);
			}
			if (toggleLines) {
				mBoardLineManager.drawLines();
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
				else {
					tiles[i].setTextures();
				}
			}
			period = DrawPeriod.DURING;
		}

		public void duringAnimation(BoardTile[] tiles) {
			for(int i=0;i<tiles.length;i++){
				if(tiles[i] != null){
					if(tiles[i].rotate && !rotateTiles[i] ){
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
		}

		public void touchHandler(float[] pt){
			mDialog.touchHandler(pt);
		}

		public void draw(BoardTile[] tiles, MyGLRenderer r){
			super.draw(tiles, r);
			for(int i =0;i<tiles.length;i++){
				if(tiles[i].getTrueSolution() != -1 && tiles[i] !=null){
					tiles[i].draw(r);
				}
			}
			mBee.draw(r);
			mDialog.draw(r);
			mGameBanner.draw(r);
		}
	}        


}
