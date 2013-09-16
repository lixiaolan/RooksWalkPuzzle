package com.example.android.opengl;

import android.content.Context;
import android.os.Vibrator;
import android.widget.Toast;


class Model{
	public Board mBoard;
	private Menu mMenu;
	private Border mBorder;
	private Bee mBee;
	private Background mBg;
	private Background mBoardBg;
	private Background mCheck;
	private GameState state;
	private int at = -1;
	private Vibrator vibe;
	private Context context;

	public Model(Context c) {
		initiateMembers(c, new Board());
	}

	public Model(Context c, Board b){
		initiateMembers(c, b);
	}

	public void initiateMembers(Context c, Board b){
		mBoard = b;
		mBee = new Bee(mBoard);
		mBg = new Background("paperbg", 2f);
		mCheck  = new Background("check",.11f);
		float[] center = {-.7f,1f, 0f};
		mCheck.setCenter(center);
		context = c;
		vibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE); 
		state = GameState.MAIN_MENU;
	}


	public void createPuzzle(int length, int hints) {
		mBoard.createPuzzleFromJNI(length, hints);
		mBorder = new Border(mBoard.columnSums, mBoard.rowSums);
		mBoardBg = new Background("boardbg", .75f);
		mMenu = new Menu();
	}

	public void restorePuzzle(int[] solution,String[] numbers, String[] arrows, String[] trueArrows){
		mBoard.restoreBoard(solution, numbers, arrows, trueArrows);
		mBorder = new Border(mBoard.columnSums, mBoard.rowSums);
		mBoardBg = new Background("boardbg", .75f);
		mMenu = new Menu();
	}

	public void touched(float[] pt) {
		switch(state){
		case PLAY: 
			//Internally close menu.    		
			int val = mMenu.touched(pt);
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

			if(mCheck.touched(pt)){
				if(mBoard.checkSolution()){
					//state = GameState.END; 
					EndDialog ed = new EndDialog(context);
					ed.show();
				} else {
					vibe.vibrate(500);
				}
			}

			break;

		case MAIN_MENU: 
			at = mBoard.touched(pt);
			if(at != -1) {
				float[] pivot = {0,0,1};
				mBoard.tiles[at].setPivot(pivot);
				mBoard.tiles[at].setRotate(true);
			}
			break;


		case GAME_MENU: break;

		}

		if(mBee.touched(pt)){
			vibe.vibrate(500);
		}
	}


	public void swiped(float[] pt, String direction) {
		switch(state){
		case PLAY:
			if (at != -1) {
				mBoard.tiles[at].arrow = direction; 
				mMenu.menuActive = false;
			}
			break;
		default: break;
		}
	}

	public void draw(MyGLRenderer r) {

		mBg.draw(r);
		mBoard.draw(r);
		mBee.draw(r);
		if(state != GameState.MAIN_MENU){
			mBoardBg.draw(r);
			mBorder.draw(r);
			mMenu.draw(r);
			mCheck.draw(r);
		} 	
	}

	public void setState(GameState s){
		state = s;
		mBee.setState(s);
		mBoard.setState(s);
	}

	public GameState getState() {
		return state;
	}

}
