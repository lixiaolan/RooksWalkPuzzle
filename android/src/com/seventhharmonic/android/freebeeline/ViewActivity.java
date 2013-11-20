package com.seventhharmonic.android.freebeeline;

import android.app.Activity;
import android.graphics.Typeface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.text.Html;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;
import android.content.res.Resources;
import com.seventhharmonic.android.freebeeline.db.SQLPuzzle;
import com.seventhharmonic.com.freebeeline.levelresources.*;


public class ViewActivity extends Activity {

	private GLSurfaceView mGLView;
	private Model mModel;
	private MyGLRenderer mRenderer;    
	private TextView mQuoteView;
	private TextView mWaitView;
	
	private DataServer mDataServer;
	private static LinearLayout loadingScreen;
	public static Store mStore;
	
	boolean savedGame = false;
	static final String savefile = "savefile";
	static final String settingsfile = "settingsfile";
	static final String TAG = "ViewActivity";
	
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
	    
	    Typeface font = Typeface.createFromAsset(getAssets(), "font3.ttf");  
	    mQuoteView.setTypeface(font);
	    mQuoteView.setText(Html.fromHtml(quotes[sel]));
	    
	    mWaitView = (TextView)findViewById(R.id.wait_view);
	    mWaitView.setTypeface(font);
	    
	    mStore = new Store(this, mModel);		
	    loadingScreen = (LinearLayout)findViewById(R.id.loadingScreen);
	    //texturesLoadedEventHandler();
	}
    
    
    @Override
    protected void onPause() {
	super.onPause();
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

    }
    
	@Override
	protected void onResume() {
	    super.onResume();
	    // The following call resumes a paused rendering thread.
	    // If you de-allocated graphic objects for onPause()
	    // this is a good place to re-allocate them.
	    GlobalApplication.getPuzzleDB().open();
	    GlobalApplication.getHintDB().open();
	    GlobalApplication.getPurchasedDB().open();

	    mGLView.onResume();
	}
    
    @Override
    protected void onStart() {
	super.onStart();	
		mModel.state.resumeGameExists = mDataServer.resumeExists();
		mModel.state.firstRun = mDataServer.firstRun();

		GlobalApplication.getPuzzleDB().open();
		GlobalApplication.getHintDB().open();
		GlobalApplication.getPurchasedDB().open();

		/*
		if(mModel.state.firstRun){
			mQuoteView.setVisibility(View.INVISIBLE);
			mModel.firstRun();
		} else {
			//mModel.reset();
		}*/

		
		//TODO: Loading puzzles with their completion value. This sucks balls. Do this elsewhere.
		LevelPackProvider mLPP = GlobalApplication.getLevelPackProvider();
		LevelPack mLP = mLPP.getLevelPack(0);
	//	Log.d(TAG, mLP.getTitle());
	//	Log.d(TAG, Integer.toString(mLP.getAllChapters().size()));
		SQLPuzzle q;
		for(Chapter c: mLP.getAllChapters()){
			for(Puzzle p: c.getAllPuzzles()){
			    q = GlobalApplication.getPuzzleDB().getPuzzle(p.getId());
			    String result = q.getCompleted();
			    Log.d(TAG,"db result "+result+" "+p.getId());
			    if(result.equals("true"))
				p.setCompleted(true);
			}
		}

		
	}

	
	
	protected void onStop() {
		super.onStop();
	}
	
	public void onDestroy() {
        super.onDestroy();
        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mStore.mHelper != null) {
            mStore.mHelper.dispose();
            mStore.mHelper = null;
        }
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



