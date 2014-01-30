package com.seventhharmonic.android.freebeeline;

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

class Board extends Graphic<BoardTile, State<BoardTile> > implements BeeBoardInterface {
	String TAG = "board";
	public int[] solution ;
	public int[][] path;
	public int boardWidth = 6;
	public int boardHeight = 6;
	public float[] beeBoxCenter = {0.0f, 0.0f, 0.0f};

	private boolean toggleLines = true;	
	private boolean toggleError = true;

	private ErrorLog mErrorLog;
	private TextBox mGameBanner;	
	
	public ImageWidget mBoardBg;	
	public Puzzle currPuzzle;
	
	private Model mModel;
	private NewBee mBee;
	private BoardBeeController mBeeController;
	private PurchasedDataSource PDS;
	protected BoardLineManager mBoardLineManager;
	public long analyticsTime;

	//The following variables need to be global because they are accessed by several states.
	//Number of moves in a puzzle.
	int moves = 0;
	//Par number of moves
	int par  = 0;
	
	public Board(Model mModel) {
		buildEmptyBoard();
		state = null;

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
		Log.d("Board", "Restored");
		for(int i=0;i<boardWidth*boardHeight;i++){
			Log.d("Board","Restored "+Integer.toString(i));
			tiles[i].setTrueSolution(solution[i]);
			tiles[i].setArrow(arrows[i]);
			tiles[i].setNumber(numbers[i]);
			tiles[i].setTrueArrow(trueArrows[i]);
			tiles[i].vPointedAt = false;
			tiles[i].hPointedAt = false;
			tiles[i].mEndDirectionType = EndDirectionType.NONE;
			tiles[i].setColor("transparent");
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
			}

			if (dx < 0) {
				tiles[boardHeight*path[i+1][0] + path[i+1][1]].setTrueArrow("left_arrow");
			}

			if (dy > 0) {
				tiles[boardHeight*path[i+1][0] + path[i+1][1]].setTrueArrow("down_arrow");
			}
			if (dy < 0) {
				tiles[boardHeight*path[i+1][0] + path[i+1][1]].setTrueArrow("up_arrow");
			}
			Log.d("path", tiles[boardHeight*path[i][0] + path[i][1]].getTrueArrow());
		}

		for(int i=0;i<hints.length;i++){
			int r = hints[i];
			Log.d(TAG, "hint");
			Log.d(TAG, Integer.toString(r));
			tiles[r].setHint();
		}

	}

	public void buildEmptyBoard() {
		tiles = new BoardTile[boardHeight*boardWidth];
		//Boardtiles start bigger as flowers and then shrink down.
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

	public void resetBoard() {
		mErrorLog.setLog();
		moves = 0;
		for(int i=0;i<tiles.length;i++){
			if(tiles[i].isChangeable()){
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
		case DAILY_PUZZLE_GAME:
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
					tiles[i].setHint();
					mBoardLineManager.drawLines();
					GlobalApplication.getAnalytics().sendUsedHint(currPuzzle.getId(), getHints());

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
		if (tiles == null) {
			System.out.println("TILES WAS NULL IN BOARD: getTile");
		}
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

	public float[] getBeeBoxCenter() {
		return beeBoxCenter;
	}

	public long getHints() {
		if(ViewActivity.mStore.hasUnlimitedHints()){
			return -1;
		} else {
			return GlobalApplication.getHintDB().getHints().getNum();
		}
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
		//These are used as temporary place holders in the touched function
		int val;
		int temp;

		ButtonWidget reset;
		ButtonWidget tutorial;
		ButtonWidget beeBox;
		HintsDataSource DB;
		TextBox mHints;
		TextBox mMoves;
		TextBox mPar;
		HintDialogWidgetLayout mHintDialog;
		Store mStore;
		GridWidgetLayout buttonGrid;
		GridWidgetLayout textGrid;

		//TODO: Get rid of tiles as inputs to these functions.
		public BoardPlay(BoardTile[] tiles) {
			analyticsTime = System.currentTimeMillis();
			par = currPuzzle.getPar();
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
			//All text shown will shake a bit
			mGameBanner.setShakeToggle(true);
			if(boardWidth == 6){
				mBoardBg = new ImageWidget(0,0, 1.0f,1.0f,TextureManager.BOARD6);
			} else if(boardWidth == 5){
				float mBoardBgScale = 1.0f;
				mBoardBg = new ImageWidget(0,0,mBoardBgScale,mBoardBgScale,TextureManager.BOARD5);
			}

			reset = new ButtonWidget(0, -1.0f, .11f, .11f, TextureManager.ERASER);
			reset.setBorderStyle(ButtonWidget.ButtonStyle.SQUARE);
			reset.setClickListener(new GameEventListener(){
				public void event(int i){
					//moves = 0;
					resetBoard();
					updateMoves();
					updateErrors();
					if(toggleError){
						mGameBanner.setText(currPuzzle.getText());
					} else{
						mGameBanner.setText("");
					}

				}
			});

			tutorial = new ButtonWidget(-.7f, 1.0f, .11f, .11f, TextureManager.QUESTIONMARK);
			tutorial.setBorderStyle(ButtonWidget.ButtonStyle.SQUARE);
			tutorial.setClickListener(new GameEventListener(){
				public void event(int i){
				    mModel.launchTutorial();
				}
			    });


			/*
			 * The hints text widget next to the bee.
			 * Note: If we can't open the DB we will crash out the program!! That is not a bad thing.
			 */
			mHints = new TextBox(0, 0, .12f, "");
			mHints.setJ(TextJustification.CENTER);
			setHintsText();

			mMoves = new TextBox(0, 0, .22f, "moves:^ 0");
			updateMoves();
			mMoves.setJ(TextJustification.CENTER);

			mPar = new TextBox(0,0,.22f, "goal:^ "+Integer.toString(par));
			mPar.setJ(TextJustification.CENTER);

			beeBox = new ButtonWidget(-.7f, 1.0f, .11f, .11f, TextureManager.CLEAR);
			beeBox.setBorderStyle(ButtonWidget.ButtonStyle.SQUARE);
			beeBox.setClickListener(new GameEventListener(){
				public void event(int i){
					if (GlobalApplication.getHintDB().useHint() || mStore.hasUnlimitedHints()) {
						showHint();
						setHintsText();
						mErrorLog.setLog();
						turnErrorRed(0);
					} else {
						mHintDialog.activate();
					}
				}
			});


			//Grid Widget to store all these wonderful buttons.
			buttonGrid = new GridWidgetLayout(3,2, .18f);
			buttonGrid.setCenter(0, mBoardBg.getCenterY()-mBoardBg.getHeight()-.1f);
			buttonGrid.addWidget(mHints);
			buttonGrid.addWidget(mMoves);
			buttonGrid.addWidget(mPar);
			buttonGrid.addWidget(beeBox);
			buttonGrid.addWidget(reset);
			buttonGrid.addWidget(tutorial);

			beeBoxCenter = buttonGrid.getWidget(3).getCenter();

			// The dialog box that appears when you run out of hints.
			mHintDialog = new HintDialogWidgetLayout(mHints);

			//If rule checking is on
			if(toggleError){
				mGameBanner.setText(currPuzzle.getText());
			} else{
				mGameBanner.setText("");
			}

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
						if(!tiles[i].isChangeable() && !tiles[i].isBlack())
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
					if(!tiles[i].isChangeable() && !tiles[i].isBlack())
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
			mBoardBg.draw(r);
			mGameBanner.draw(r);
			super.draw(tiles, r);
			mBee.draw(r);
			buttonGrid.draw(r);
			mMenu.draw(r);
			mHintDialog.draw(r);

		}	

		@Override
		public void touchHandler(float[] pt){
			reset.touchHandler(pt);
			tutorial.touchHandler(pt);

			// this checks that the errors have been resolved before one moves on.  Comment this out
			// to turn off this feature.
			if (mErrorLog.hasError() && toggleError) {
				float[] f = new float[2];
				f[0] = pt[0];
				f[1] = pt[1];
				temp = touched(f);
				val = mMenu.testTouch(pt);
				if (temp == -1 && val == -1) {
					mGameBanner.setText(TextureManager.PLEASEFIXERROR);
					return;
				}
				else if (!mErrorLog.hasError(temp) && (val == -1) ){
					mGameBanner.setText(TextureManager.PLEASEFIXERROR);
					return;
				}
			}


			//onto the main logic of the game
			val = mMenu.touched(pt);
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
						if(tiles[at].isClickable()) {
							mMenu.activate(pt);
						}
					}
				}
			} else {
				if (at != -1) {
					//Set the user input since we get values
					tiles[at].setUserInput(val);
					if(val != 0){
						moves+=1;
					}
					updateMoves();

					if (toggleLines) {
						//if (!mErrorLog.getError(at).equals(""))
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
				checkIfPuzzleSolved();
				return;
			}

			
			if(mHints.isTouched(pt)){
				if (GlobalApplication.getHintDB().useHint() || mStore.hasUnlimitedHints()) {
					showHint();
					setHintsText();
					mErrorLog.setLog();
					turnErrorRed(0);
				} else {
					mHintDialog.activate();
				}	
			}	
			beeBox.touchHandler(pt);

			if(mHintDialog.isActive()){
				mHintDialog.touchHandler(pt);
				return;
			}

			checkIfPuzzleSolved();
		}

		public void updateMoves(){
			mMoves.setText("moves:^"+Integer.toString(moves));
		}

		public void checkIfPuzzleSolved() {
			if(checkSolution()) {	
				if(currPuzzle!=null){
					/*//Trouble: Check if chapter is completed
					boolean beforeChCompleted = currPuzzle.getChapter().getCompleted();
					Log.d("board", "solved a puzzle and set completed to true");
					long time = System.currentTimeMillis() - analyticsTime;
					//Need to see if puzzle was completed for analytics reasons
					if(!currPuzzle.isCompleted())
						GlobalApplication.getAnalytics().sendPuzzleFirstTimeCompleteTiming(currPuzzle.getId(), par, time, moves);
					else
						GlobalApplication.getAnalytics().sendPuzzleReplayedTiming(currPuzzle.getId(), par, time, moves);		
					//Now call something in puzzle, sets the puzzle to be completed in the DB and the associated number of moves
					currPuzzle.completePuzzleListener(moves, true);
					//if the chapter was not completed before, but now it is 
					if(!beforeChCompleted &&  currPuzzle.getChapter().getCompleted()){
						firstCompleted = true;
					}
					*/
					mBeeController.setMood(Mood.HAPPY);
					
					
					mModel.puzzleSolved();
				}
			}
		}

		public void setHintsText(){
			if(mStore.hasUnlimitedHints()){
				mHints.setText(TextureManager.buildHint("%"));
			} else {
				mHints.setText(TextureManager.buildHint(GlobalApplication.getHintDB().getHints().getNum()));
			}
		}



		public void updateErrors(){
			mErrorLog.setLog();
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
			for (int i = 0; i < tiles.length; i++) {

				String error = mErrorLog.getError(i); 
				if(!error.equals("") && !tiles[i].isHint()){
					//tiles[at].setAngryGlow(1,0);
					tiles[i].setColor("red");
				}
			}
			// String error = mErrorLog.getError(at); 
			// if(!error.equals("")){
			//     //tiles[at].setAngryGlow(1,0);
			//     tiles[at].setColor("red");
			// }

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
		ImageWidget mFlower;

		public BoardGameEnd(BoardTile[] tiles) {
			//Top game banner
			mFlower = new ImageWidget(0,mBoardBg.getCenterY()+mBoardBg.getHeight(),.15f,.15f,currPuzzle.getFlower());
			mGameBanner.setShakeToggle(false);
			//Note: this is game moves! If this number is less then the puzzle moves, the puzzle will not change the number of moves.
			if(moves <= par){
				mGameBanner.setText("Perfect Finish!");	
			} else {
				mGameBanner.setText("Puzzle Completed");
			}

			mGameBanner.setFontSize(2);
			mGameBanner.setJ(TextJustification.CENTER);

			mModel.launchDialog();
			//Buttons at the bottom.

			//Initiate tiles
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

			//We should redraw the lines to be on the safe side
			mBoardLineManager.drawLines();
			GlobalApplication.getAnalytics().sendScreen("puzzle_end");
		}

		public void enterAnimation(BoardTile[] tiles) {
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
					}
				}
			}
		}

		public void touchHandler(float[] pt){
		    return;
		}

		public void draw(BoardTile[] tiles, MyGLRenderer r){
			try{
				super.draw(tiles, r); //TODO: The code once died on this line :(
				for(int i =0;i<tiles.length;i++){
					if(tiles[i].getTrueSolution() != -1 && tiles[i] !=null){
						tiles[i].draw(r);
					}
				}
			}
			catch(Exception e){
				GlobalApplication.getAnalytics().sendCaughtException(e);
			}
			mBee.draw(r);
			mGameBanner.draw(r);
			mFlower.draw(r);
		}
	}        

}

////+"^^"+TextureManager.PUZZLESLEFT+Integer.toString(currPuzzle.getChapter().getNumberPuzzlesIncomplete()));
