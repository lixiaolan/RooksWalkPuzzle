package com.example.android.opengl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.util.Log;

class Model{
	public GlobalState state;
	public TutorialBoard mTutorialBoard;
	public Board mBoard;
	private Menu mMenu;
	private Border mBorder;
	public Bee mBee;
	private MenuManager mMenuManager;
	private Background mBoardBg;
	private Background mCheck;
	private int at = -1;
	private Vibrator vibe;
	public Context context;
	private Banner mGameBanner;
	private Background mTitle;
	public Model(Context c) {
		initiateMembers(c, new Board());
	}

	public Model(Context c, Board b){
		initiateMembers(c, b);
	}

	public void initiateMembers(Context c, Board b){
		mBoard = b;
		mBee = new Bee(mBoard);
		mCheck  = new Background("check",.11f);
		float[] center = {-.7f,-1f, 0f};
		mCheck.setCenter(center);
		context = c;
		vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE); 
		state = new GlobalState();
		mMenuManager = new MenuManager(state, this);
		mMenu = new Menu();
		mBoardBg = new Background("boardbg", .75f);
		mGameBanner = new Banner(.75f);
		mGameBanner.setPosition("TOPCENTER");
		mTitle = new Background("title", .75f);
		float[] titleCenter = {0.0f, 1.0f, 0.0f};
		mTitle.setCenter(titleCenter);
	}    

	public void createPuzzle(int length, int hints) {
		state.showGameBanner = false;
		mBoard.createPuzzleFromJNI(length, hints);
		mBorder = new Border(mBoard.getColumnSums(), mBoard.getRowSums());	
	}

	public void restorePuzzle(int[] solution,String[] numbers, String[] arrows, String[] trueArrows, int[][] path, boolean[] clickable){
		state.showGameBanner = false;
		mBoard.restoreBoard(solution, numbers, arrows, trueArrows, path, clickable);
		mBorder = new Border(mBoard.getColumnSums(), mBoard.getRowSums());
	}

	public void createTutorial(){
		mTutorialBoard = new TutorialBoard();
	}
	
	public void toggleHints(boolean toggle) {
		mBoard.toggleHints(toggle);
	}

	public void touched(float[] pt) {
		int val = -1;
		switch(state.state){
		case GAME_OPENING: 
			//Internally close menu.    		
			val = mMenu.touched(pt);
			if(val == -1){
				if (at != -1) {
					mBoard.tiles[at].setColor("transparent");
				}
				at = mBoard.touched(pt);
				if(at != -1 ) {
					if(mBoard.tiles[at].isBlack() == false) {
						mBoard.tiles[at].setColor("blue");
						if(mBoard.tiles[at].isClickable())
							mMenu.activate(pt);
					}
				}
			} else {
				if (at != -1)
					mBoard.tiles[at].setUserInput(val);
			}

			if(mCheck.touched(pt) == 1){
				if(mBoard.checkSolution()){
					state.showGameBanner = true;
					mGameBanner.set(TextureManager.GOOD_JOB);
					state.state = GameState.GAME_MENU_END;
					mMenuManager.updateState();
					//No game to save. No game to resume.
					state.saveCurrGame = false;
					state.resumeGameExists = false;
					mBee.setMood(Mood.HAPPY);
					mBoard.setState(GameState.GAME_MENU_END);
				       
				} else {
					mGameBanner.set(TextureManager.TRY_AGAIN);
					state.showGameBanner = true;
					vibe.vibrate(500);
				}
			}
			
			
		case GAME_MENU_LIST:    
		case GAME_MENU_END:
			val = mMenuManager.touched(pt);
			if(val != -1){
				mMenuManager.onTouched(val);
			}
			if(mBee.touched(pt) == 1){
				vibe.vibrate(500);
			}
			break;
		case MAIN_MENU_OPENING:    
		case MAIN_MENU_LIST:
		case MAIN_MENU_NEW:
		case MAIN_MENU_OPTIONS:
			at = mBoard.touched(pt);
			if(at != -1) {
				float[] pivot = {0,0,1};
				mBoard.tiles[at].setPivot(pivot);
				mBoard.tiles[at].setRotate(true);
			}

			val = mMenuManager.touched(pt);
			if(val != -1){
				mMenuManager.onTouched(val);
			}	    
			if(mBee.touched(pt) == 1){
				vibe.vibrate(500);
			}
			break;
		
		case TUTORIAL:
			//Game Menu
			val = mMenuManager.touched(pt);
			if(val != -1){
				mMenuManager.onTouched(val);
			}
			mTutorialBoard.touchHandler(mMenu, pt);
			break;
		default: break;
		}

		




	}

	public void swiped(float[] pt, String direction) {
		switch(state.state){
		case GAME_OPENING:
			if (at != -1 && mBoard.tiles[at].isClickable()) {
				mBoard.tiles[at].setArrow(direction); 
				mMenu.menuActive = false;
			}
			break;
		
		case TUTORIAL:
				mTutorialBoard.swipeHandler(direction); 
				break;
		default: break;
		}
	}

	public void draw(MyGLRenderer r) {

		switch(state.state) {
		case GAME_OPENING:
		case GAME_MENU_LIST:
			mCheck.draw(r);
		case GAME_MENU_END:
			mBoardBg.draw(r);
			mBoard.draw(r);
			mBee.draw(r);
			mBoardBg.draw(r);
			mBorder.draw(r);
			mMenu.draw(r);
			if(state.showGameBanner){
				mGameBanner.draw(r);
			}
			break;
		case MAIN_MENU_OPENING:
		case MAIN_MENU_LIST:
		case MAIN_MENU_NEW:
		case MAIN_MENU_OPTIONS:
			mBoard.draw(r);
			mBee.draw(r);
			mTitle.draw(r);
			break;
		case TUTORIAL:
			mTutorialBoard.draw(r);
			mMenu.draw(r);
		default: break;
		}
		mMenuManager.draw(r);
	}

	public void setState(GameState s){
		state.state = s;
		mBee.setState(s);
		mBoard.setState(s);
	}

	public GameState getState() {
		return state.state;
	}

	 public void restoreGameUtil(){
	     	try{
	     	    int[] solution = new int[36];
	     	    String[] numbers = new String[36];
	     	    String[] arrows = new String[36];
	     	    String[] trueArrows = new String[36];
	     	    int[][] path;
	     	    boolean[] clickable = new boolean[36];
	     	    
	     	    File file = new File(context.getFilesDir(), "savefile");
		    
	     	    if(file.exists()){
	     		Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)));
	     		for(int i=0; i<36;i++){
	     		    int m = scanner.nextInt();
	     		    solution[i] = m;
	     		}
	     		for(int i=0; i<36;i++){
	     		    String m = scanner.next();
	     		    numbers[i] = m;
	     		}
	     		for(int i=0; i<36;i++){
	     		    String m = scanner.next();
	     		    arrows[i] = m;
	     		}
	     		for(int i=0; i<36;i++){
	     		    String m = scanner.next();
	     		    trueArrows[i] = m;
	     		}
	     		
	     		for(int i =0; i< clickable.length;i++){
	     			boolean m = scanner.nextBoolean();
	    		    clickable[i] = m;
	    		}
	     		
	     		int path_length = scanner.nextInt();
	     		int twiddle = scanner.nextInt();
	     		path = new int[path_length][twiddle];
	     		for(int i=0; i< path_length;i++){
	     			for(int j = 0;j< twiddle;j++){
	     				path[i][j] = scanner.nextInt();
	     			}
	     		}
	     		scanner.close();
	     		restorePuzzle(solution, numbers, arrows, trueArrows, path, clickable);
	     	    } 
	     	}
		
	     	catch (FileNotFoundException e) {
	     	    Log.e("Exception", "File not found: " + e.toString());
	     	} 
	     	catch (InputMismatchException e) {
	     	    System.out.print(e.getMessage()); //try to find out specific reason.
	     	}
		
	     }

	public void reset() {
		setState(GameState.MAIN_MENU_OPENING);
		mMenuManager.updateState();
	}
	 
	public void saveGame() {
		File file = new File(context.getFilesDir(), "savefile");
		int[] solutions = mBoard.dumpSolution();
		String[] numbers  = mBoard.dumpNumbers();
		String[] arrows  = mBoard.dumpArrows();
		String[] trueArrows = mBoard.dumpTrueArrows();
		int[][] path  = mBoard.dumpPath();
		boolean[] clickable = mBoard.dumpClickable();

		try {
			PrintWriter outputStream = new PrintWriter(new FileWriter(file));
			for(int i =0; i< solutions.length;i++){
				outputStream.println(Integer.toString(solutions[i]));
			}

			for(int i =0; i< numbers.length;i++){
				outputStream.println(numbers[i]);
			}

			for(int i =0; i< arrows.length;i++){
				outputStream.println(arrows[i]);
			}

			for(int i =0; i< trueArrows.length;i++){
				outputStream.println(trueArrows[i]);
			}

			for(int i =0; i< clickable.length;i++){
				outputStream.println(clickable[i]);
			}

			outputStream.println(path.length);
			outputStream.println(path[0].length);

			for(int i =0;i< path.length ;i++){
				for(int j=0;j<path[0].length;j++){
					outputStream.println(path[i][j]);
				}
			}

			outputStream.close();
			SharedPreferences s  = context.getSharedPreferences("settingsfile", 0);
			SharedPreferences.Editor editor = s.edit();
			editor.putBoolean("savedGameExists", true);
			editor.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void clearBoard() {
		mBoard.resetBoard();
	}    
}
