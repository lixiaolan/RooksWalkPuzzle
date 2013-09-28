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
import android.text.Html;
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
import android.widget.TextView;
import android.widget.ViewAnimator;
import android.widget.ViewSwitcher;
import android.content.Context;
import android.content.res.Resources;

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
    private TextView mQuoteView;
    
    boolean savedGame = false;
    static final String savefile = "savefile";
    static final String settingsfile = "settingsfile";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	setContentView(R.layout.viewactivity);

	mModel = new Model(this);
	mRenderer = new MyGLRenderer(this, mModel);
	
	mGLView = (GameView)findViewById(R.id.surface_view);
		((GameView)mGLView).setMyRenderer(mRenderer);
		((GameView)mGLView).setModel(mModel);
	mQuoteView = (TextView)findViewById(R.id.QuoteView);
	Resources res = getResources();
	String[] quotes = res.getStringArray(R.array.quotes);
	int sel = (int)(Math.random()*quotes.length);
	mQuoteView.setText(Html.fromHtml(quotes[sel]));
    
    }
    
    
    @Override
    protected void onPause() {
	super.onPause();
	// The following call pauses the rendering thread.
	// If your OpenGL application is memory intensive,
	// you should consider de-allocating objects that
	// consume significant memory here.
	mGLView.onPause();
	System.out.println("Should I dump data: "+Boolean.toString(mModel.state.saveCurrGame));
	if(mModel.state.saveCurrGame){
	    File file = new File(this.getFilesDir(), savefile);
	    int[] solutions = mModel.mBoard.dumpSolution();
	    String[] numbers  = mModel.mBoard.dumpNumbers();
	    String[] arrows  = mModel.mBoard.dumpArrows();
	    String[] trueArrows = mModel.mBoard.dumpTrueArrows();
	    int[][] path = mModel.mBoard.dumpPath();
	    boolean[] clickable = mModel.mBoard.dumpClickable();
	    
	    try {
		PrintWriter outputStream = new PrintWriter(new FileWriter(file));
		for(int i =0; i< solutions.length;i++){
		    outputStream.println(Integer.toString(solutions[i]));
		}
		
		for(int i =0; i< numbers.length;i++){
		    //System.out.println("Saving "+Integer.toString(i)+" "+numbers[i]);
		    outputStream.println(numbers[i]);
		}
		
		for(int i =0; i< arrows.length;i++){
		    //System.out.println("Saving "+Integer.toString(i)+" "+arrows[i]);
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
		SharedPreferences s  = getSharedPreferences(settingsfile, 0);
		SharedPreferences.Editor editor = s.edit();
	    editor.putBoolean("savedGameExists", true);
	    editor.commit();
	    System.out.println("Edited Shared Preferences");
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
		SharedPreferences s  = getSharedPreferences(settingsfile, 0);
		mModel.state.resumeGameExists = s.getBoolean("savedGameExists", false);	
		System.out.println("Is there a resume game "+Boolean.toString( mModel.state.resumeGameExists));
		mModel.reset();
	/*File file = new File(this.getFilesDir(), savefile);
		if(file.exists()){
			savedGame = true;				
		}	
	*/
    }
 
    public void closeQuoteScreen(View v) {
    	System.out.println("I should close now");
    	TextView w = (TextView)findViewById(R.id.QuoteView);
    	w.setVisibility(View.INVISIBLE);
    }
   
}    
    
    //This is called after the constructor of GameView is complete.
    //Otherwise, the positions would not work out correctly :(
       
    // public void newGame(View v) {
    // 	//mButtonManager.manageState(v, mModel.getState());
    // 	//NOTE: menu state is getting changed to GAME_PLAY after the buttons leave the screen.
    // 	if (mMenuState.state == MenuStateEnum.GAME_PLAY && mMenuState.createGame) {
    // 	    switch (mMenuState.difficulty) {
    // 	    case 1:
    // 		mModel.createPuzzle(4,2);
    // 		break;
    // 	    case 2:
    // 		mModel.createPuzzle(10,3);
    // 		break;
    // 	    case 3:
    // 		mModel.createPuzzle(12,4);
    // 		break;
    // 	    case 4:
    // 		mModel.createPuzzle(20,3);
    // 		break;
    // 	    }
    // 	    mMenuState.createGame = false;
    // 	    mModel.setState(GameState.PLAY);
	    
    // 	} 
    // }
    
    // public void resumeGame(View v) {
    // 	manageView(v);
    // 	//mButtonManager.manageState(v, mModel.getState());
    // 	if(mMenuState.state == MenuStateEnum.GAME_RESUME && mMenuState.createGame){
    // 	    if(savedGame) {
    // 		restoreGameUtil();
    // 	    }
    // 	    mMenuState.createGame = false;
    // 	    mModel.setState(GameState.PLAY);
    // 	}	
    // }
    
     
    // public void resetGame(View v){
    // 	manageView(v);
    // 	try{
    // 	    File file = new File(this.getFilesDir(), savefile);
    // 	    if(file.delete()){
    // 		System.out.println(file.getName() + " is deleted!");
    // 	    }else{
    // 		System.out.println("Delete operation failed.");
    // 	    }
    // 	}catch(Exception e){
    // 	    e.printStackTrace();
    // 	}
	
    // 	savedGame = false;
    // 	mModel.setState(GameState.MAIN_MENU);
	
    // }
    
    // public void clearBoard(View v){
    // 	manageView(v);
    // 	mModel.clearBoard();
    // }
    
    // public void toggleHints(View v){
    // 	manageView(v);
    // 	mModel.toggleHints(mMenuState.hints);
    // }
    
    //This function peforms animatinos on the array of buttons allButtons.
    //It works by sequentially activating the various animatinos
    //needed based on any change of state in the StateButtons.
    //This provides a flexible way for the menu to have smooth
    //animations between its different layouts.
    

