package com.example.android.opengl;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ViewAnimator;
import android.widget.ViewSwitcher;
import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.io.File;
public class ViewActivity extends Activity {

	private GLSurfaceView mGLView;
	private Model mModel;
	private MyGLRenderer mRenderer;    
	
	private MenuState mMenuState = new MenuState();
	private ButtonManager mButtonManager;
	boolean savedGame = false;
	String savefile = "savefile";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewactivity);

		// Create a GLSurfaceView instance and set it
		// as the ContentView for this Activity
		if(savedInstanceState != null){
			System.out.println("Restored some shit");
			mModel = new Model(this, (Board)savedInstanceState.getParcelable("board"));        	
		} else {
			mModel = new Model(this);
		}
		mButtonManager = new ButtonManager(mMenuState, this);

		mRenderer = new MyGLRenderer(this, mModel);

		mGLView = (GameView)findViewById(R.id.surface_view);
		((GameView)mGLView).setMyRenderer(mRenderer);
		((GameView)mGLView).setModel(mModel);

		//Store all buttons in one array for now.  Change to a map later for clarity.
				View yourLayout = findViewById(R.id.surface_view);
				final ViewTreeObserver vto = yourLayout.getViewTreeObserver();
				vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						float h = (float) mGLView.getHeight()-mGLView.getPaddingBottom()-mGLView.getPaddingTop();
						float w = (float) mGLView.getWidth();
						mButtonManager.setHW(h, w);
					}
				});
				
				// String fontPath = "MileyTwerk.ttf";
				// Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
				// bee_puzzled.mButton.setTypeface(tf);
				// short_puz.mButton.setTypeface(tf);
				// medium_puz.mButton.setTypeface(tf);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putParcelable("board", mModel.mBoard);
		super.onSaveInstanceState(outState);
	}


	@Override
	protected void onPause() {
		super.onPause();
		// The following call pauses the rendering thread.
		// If your OpenGL application is memory intensive,
		// you should consider de-allocating objects that
		// consume significant memory here.
		mGLView.onPause();
		
		if(mModel.getState() ==GameState.PLAY){
			File file = new File(this.getFilesDir(), savefile);
			int[] solutions = mModel.mBoard.dumpSolution();
			String[] arrows  = mModel.mBoard.dumpArrows();
			String[] numbers  = mModel.mBoard.dumpNumbers();
			String[] trueArrows = mModel.mBoard.dumpTrueArrows();
			try {
				PrintWriter outputStream = new PrintWriter(new FileWriter(file));
				for(int i =0; i< solutions.length;i++){
					outputStream.println(Integer.toString(solutions[i]));
				}
				for(int i =0; i< numbers.length;i++){
					System.out.println("Saving "+Integer.toString(i)+" "+numbers[i]);
					outputStream.println(numbers[i]);
				}
				for(int i =0; i< arrows.length;i++){
					System.out.println("Saving "+Integer.toString(i)+" "+arrows[i]);
					outputStream.println(arrows[i]);
				}
				for(int i =0; i< trueArrows.length;i++){
					outputStream.println(trueArrows[i]);
				}
				outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// The following call resumes a paused rendering thread.
		// If you de-allocated graphic objects for onPause()
		// this is a good place to re-allocate them.
		mGLView.onResume();
		// AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.anim.shrink_dance_button_anim);
		// set.setTarget((Button)findViewById(R.id.bee_puzzled));
		// set.end();       
	}

	@Override
	protected void onStart() {
		super.onStart();
		mModel.setState(GameState.MAIN_MENU);
		mModel.setButtonManager(mButtonManager);
		mButtonManager.reset();
		File file = new File(this.getFilesDir(), savefile);
		if(file.exists()){
				savedGame = true;				
		}	
		
		Button resume = (Button)findViewById(R.id.resume);
		resume.setVisibility(View.VISIBLE);
	}

	//This is called after the constructor of GameView is complete.
	//Otherwise, the positions would not work out correctly :(

	public void newGame(View v) {
		manageView(v);
		//mButtonManager.manageState(v, mModel.getState());
		//NOTE: menu state is getting changed to GAME_PLAY after the buttons leave the screen.
		if (mMenuState.state == MenuStateEnum.GAME_PLAY && mMenuState.createGame) {
				switch (mMenuState.difficulty) {
				case 1:
					mModel.createPuzzle(4,2);
					break;
				case 2:
					mModel.createPuzzle(6,5);
					break;
				case 3:
					mModel.createPuzzle(12,4);
					break;
				case 4:
					mModel.createPuzzle(16,5);
					break;
				}
				mMenuState.createGame = false;
				mModel.setState(GameState.PLAY);

			} 
	}

	public void resumeGame(View v) {
		manageView(v);
		//mButtonManager.manageState(v, mModel.getState());
		if(mMenuState.state == MenuStateEnum.GAME_RESUME && mMenuState.createGame){
			if(savedGame) {
				restoreGameUtil();
			}
			mMenuState.createGame = false;
			mModel.setState(GameState.PLAY);
		}	
	}
	
	public void restoreGameUtil(){
		try{
		int[] solution = new int[36];
		String[] numbers = new String[36];
		String[] arrows = new String[36];
		String[] trueArrows = new String[36];
		File file = new File(this.getFilesDir(), "savefile");
		
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
			mModel.restorePuzzle(solution, numbers, arrows, trueArrows);
			savedGame = false;
		} 
		}
		
		catch (FileNotFoundException e) {
		    Log.e("Exception", "File not found: " + e.toString());
		} 
		catch (InputMismatchException e) {
		    System.out.print(e.getMessage()); //try to find out specific reason.
		    
		}
		
	}
    
	public void resetGame(View v){
		manageView(v);
		try{
    		File file = new File(this.getFilesDir(), savefile);
    		if(file.delete()){
    			System.out.println(file.getName() + " is deleted!");
    		}else{
    			System.out.println("Delete operation is failed.");
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		
		savedGame = false;
		mModel.setState(GameState.MAIN_MENU);

	}
	
	public void manageView(View v){
		mButtonManager.manageState(v, mModel.getState());
	}
	//This function peforms animatinos on the array of buttons allButtons.
	//It works by sequentially activating the various animatinos
	//needed based on any change of state in the StateButtons.
	//This provides a flexible way for the menu to have smooth
	//animations between its different layouts.

}
