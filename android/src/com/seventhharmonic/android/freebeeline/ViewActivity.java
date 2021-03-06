package com.seventhharmonic.android.freebeeline;

import android.app.Activity;
import android.graphics.Typeface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;
import android.content.res.Resources;
import com.seventhharmonic.android.freebeeline.db.SQLPuzzle;
import com.seventhharmonic.com.freebeeline.levelresources.*;

import com.google.android.gms.ads.*;
import com.google.analytics.tracking.android.EasyTracker;


import android.media.MediaPlayer;
import android.media.AudioManager;
import android.content.*;
import android.app.Activity;
import android.app.Service;
import android.os.Binder;
import android.os.IBinder;
import android.os.Build;
import android.content.Intent;
import android.view.WindowManager;

/**
   An "Activity" Represents (generally) a unit of user interface. It
   must be able to handle Creation and destruction, Start and Stop,
   Pause and Resume. Usually a program will have many
   activities. Bee-Line only uses one (this one) since all our UI is
   though OpenGL|ES and not the Android framework.
 */

public class ViewActivity extends Activity {    
    
    boolean savedGame = false;
    static final String savefile = "savefile";
    static final String settingsfile = "settingsfile";
    static final String TAG = "ViewActivity";
    
    private GLSurfaceView mGLView;
    private Model mModel;
    private MyGLRenderer mRenderer;    
    private TextView mQuoteView;
    private TextView mWaitView;

    private AdView adView;
    

        

    private static LinearLayout loadingScreen;

    public static Store mStore;
    public static DataServer mDataServer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	setContentView(R.layout.viewactivity);
	Log.d(TAG, "Run View Activity");
	
	mModel = new Model(this);
	mRenderer = new MyGLRenderer(this, mModel);
	
	GlobalApplication.getLevelPackProvider().initialize();
	mDataServer = new DataServer();
	mDataServer.setContext(this);
	
	mModel.setDataServer(mDataServer);
	
	mGLView = (GameView)findViewById(R.id.surface_view);
	((GameView)mGLView).setMyRenderer(mRenderer);
	((GameView)mGLView).setModel(mModel);
	
	mQuoteView = (TextView)findViewById(R.id.QuoteView);
	Resources res = getResources();
	String[] quotes = res.getStringArray(R.array.quotes);
	int sel = (int)(Math.random()*quotes.length);
	
	Typeface font = Typeface.createFromAsset(getAssets(), "Scribblz.ttf");  
	mQuoteView.setTypeface(font);
	mQuoteView.setText(Html.fromHtml(quotes[sel]));
	
	mWaitView = (TextView)findViewById(R.id.wait_view);
	mWaitView.setTypeface(font);
	
	mStore = new Store(this, mModel);		
	loadingScreen = (LinearLayout)findViewById(R.id.loadingScreen);
	
	//Create AdView
	adView = (AdView)findViewById(R.id.adView);
 
    // Initiate a generic request.
    AdRequest adRequest = new AdRequest.Builder()
    								   .build();
    // Load the adView with the ad request.
    adView.loadAd(adRequest);
	adView.setVisibility(View.INVISIBLE);
    
    }
    
    
    @Override
    protected void onPause() {
	super.onPause();
	adView.pause();
	// The following call pauses the rendering thread.
	// If your OpenGL application is memory intensive,
	// you should consider de-allocating objects that
	// consume significant memory here.
	mGLView.onPause();
	if(mModel.state.saveCurrGame){
	    mDataServer.saveGame(mModel.mBoard);
	}
	GlobalApplication.getPuzzleDB().close();
	GlobalApplication.getHintDB().close();
	GlobalApplication.getPurchasedDB().close();
	GlobalApplication.getMyMusic().pauseMusic();
    }
    
    @Override
    protected void onResume() {
	super.onResume();
	adView.resume();
	// The following call resumes a paused rendering thread.
	// If you de-allocated graphic objects for onPause()
	// this is a good place to re-allocate them.
	mGLView.onResume();
	GlobalApplication.getPuzzleDB().open();
	GlobalApplication.getHintDB().open();
	GlobalApplication.getPurchasedDB().open();	
	//TODO:This also sucks. Why is music hard???
	if(mDataServer.getMusicToggle()) {
	    GlobalApplication.getMyMusic().resumeMusic();
	}
    }
    
    @Override
    protected void onStart() {
	super.onStart();	
	mModel.state.resumeGameExists = mDataServer.resumeExists();
	mModel.state.firstRun = mDataServer.firstRun();
	
	GlobalApplication.getPuzzleDB().open();
	GlobalApplication.getHintDB().open();
	GlobalApplication.getPurchasedDB().open();
	
	
	  if(mModel.state.firstRun){
		GlobalApplication.getHintDB().addHints(15);
	  }
	
	
	//TODO: Loading puzzles with their completion value. This sucks balls. Do this elsewhere.
	LevelPackProvider mLPP = GlobalApplication.getLevelPackProvider();
	for(int i =0;i<mLPP.getNumberOfLevelPacks();i++){
	LevelPack mLP = mLPP.getLevelPack(i);
	//	Log.d(TAG, mLP.getTitle());
	//	Log.d(TAG, Integer.toString(mLP.getAllChapters().size()));
	SQLPuzzle q;
	for(Chapter c: mLP.getAllChapters()){
	    for(Puzzle p: c.getAllPuzzles()){
		q = GlobalApplication.getPuzzleDB().getPuzzle(p.getId());
		String result = q.getCompleted();
		Log.d(TAG,"db result "+result+" "+p.getId());
		//TODO: BOOGIE:Uncomment to unmark all puzzles as completed
		if(result.equals("true")){
		    p.setCompleted(true);
		}
			p.setMoves((int)q.getMovesUsed());
	    }
	}
	}
	EasyTracker.getInstance(this).activityStart(this);
    }	
    
    protected void onStop() {
    	super.onStop();
	//	GlobalApplication.getMyMusic().stopMusic();
    	EasyTracker.getInstance(this).activityStop(this);
    	mGLView.onPause();
    }
    
    public void onDestroy() {
	super.onDestroy();
	adView.destroy();
	// very important:
	Log.d(TAG, "Destroying helper.");
	if (mStore.mHelper != null) {
	    mStore.mHelper.dispose();
	    mStore.mHelper = null;
	}
	//	GlobalApplication.getMyMusic().onDestroy();	
	EasyTracker.getInstance(this).activityStop(this);
	mGLView.onPause();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	
	// Pass on the activity result to the helper for handling
	// NOTE: handleActivityResult() will update the state of the helper,
	// allowing you to make further calls without having it exception on you
	if (mStore.mHelper.handleActivityResult(requestCode, resultCode, data)) {
	    Log.d(TAG, "onActivityResult handled by IABUtil.");
	    //handlePurchaseResult(requestCode, resultCode, data);
	    return;
	}
	
	// What you would normally do
	// ...
    }
    
    public void closeQuoteScreen(View v) {
    	loadingScreen.setVisibility(View.INVISIBLE);
    }    
    
    public void onBackPressed() {
    	mModel.onBack();
    }	
}    



//texturesLoadedEventHandler();
	/*
	if (Build.VERSION.SDK_INT < 16) {
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				 WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}
	else{
	    View decorView = getWindow().getDecorView();
	    int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
	    decorView.setSystemUiVisibility(uiOptions);
	}*/
