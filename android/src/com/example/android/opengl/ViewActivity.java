package com.example.android.opengl;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewactivity);

		// Create a GLSurfaceView instance and set it
		// as the ContentView for this Activity
		if(savedInstanceState != null){
			System.out.println("Restored some shit");
			mModel = new Model(this, (Board)savedInstanceState.getParcelable("board"));        	
			mButtonManager = new ButtonManager((MenuState)savedInstanceState.getParcelable("menu"), this);

		} else {
			mModel = new Model(this);
			mButtonManager = new ButtonManager(mMenuState, this);

		}

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
		outState.putParcelable("menu", mMenuState);
		super.onSaveInstanceState(outState);
	}


	@Override
	protected void onPause() {
		super.onPause();
		// The following call pauses the rendering thread.
		// If your OpenGL application is memory intensive,
		// you should consider de-allocating objects that
		// consume significant memory here.
		System.out.println("paused");
		mGLView.onPause();
		
		if(mModel.getState() ==GameState.PLAY){
			String filename = "savefile";
			File file = new File(this.getFilesDir(),filename);
			int[] solutions = mModel.mBoard.dumpSolution();
			String[] arrows  = mModel.mBoard.dumpArrows();
			String[] numbers  = mModel.mBoard.dumpNumbers();
			
			for(int i=0; i<36; i++){
				System.out.println("Why am I empty? "+numbers[i]);
			}
			
			System.out.println("lenght solution+ "+Integer.toString(solutions.length));
			System.out.println("lenght arrows+ "+Integer.toString(arrows.length));
			System.out.println("lenght numbers+ "+Integer.toString(numbers.length));

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
		
	
		File file = new File(this.getFilesDir(), "savefile");
		if(file.exists()){
				savedGame = true;				
		}	
		
		Button resume = (Button)findViewById(R.id.resume);
		resume.setVisibility(View.VISIBLE);
	}

	//This is called after the constructor of GameView is complete.
	//Otherwise, the positions would not work out correctly :(

	public void manageView(View v) {
		mButtonManager.manageState(v);
		if (mMenuState.state == MenuStateEnum.GAME_PLAY) {
			switch (mMenuState.difficulty) {
			case 1:
				
				mModel.createPuzzle(4,1);
				mModel.setState(GameState.PLAY);
				break;
			case 2:
				
				mModel.createPuzzle(14,5);
				mModel.setState(GameState.PLAY);
				break;
			}
		}
	}

	
	public void RestoreGame(View v){
		try{
		int[] solution = new int[36];
		String[] numbers = new String[36];
		String[] arrows = new String[36];
		File file = new File(this.getFilesDir(), "savefile");
		
		if(file.exists()){
			savedGame = true;
			Scanner scanner = new Scanner(new BufferedReader(new FileReader(file)));
			for(int i=0; i<36;i++){
				int m = scanner.nextInt();
				System.out.println(Integer.toString(i)+": "+Integer.toString(m));
				solution[i] = m;
			}
			for(int i=0; i<36;i++){
				String m = scanner.next();
				System.out.println(Integer.toString(i)+": "+m);
				numbers[i] = m;
			}
			for(int i=0; i<36;i++){
				String m = scanner.next();
				System.out.println(Integer.toString(i)+": "+m);
				arrows[i] = m;
			}
			mModel.restorePuzzle(solution, numbers, arrows);
			mModel.setState(GameState.PLAY);
		} 
		}
		
		catch (FileNotFoundException e) {
		    Log.e("Exception", "File not found: " + e.toString());
		} 
		catch (InputMismatchException e) {
		    System.out.print(e.getMessage()); //try to find out specific reason.
		    
		}
		
	}
    
	//This function peforms animatinos on the array of buttons allButtons.
	//It works by sequentially activating the various animatinos
	//needed based on any change of state in the StateButtons.
	//This provides a flexible way for the menu to have smooth
	//animations between its different layouts.

}
