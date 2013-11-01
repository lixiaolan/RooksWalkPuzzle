package com.seventhharmonic.android.freebeeline;

import android.app.Activity;
import android.graphics.Typeface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
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
	private DataServer mDataServer;
	public static Store mStore;
	
	boolean savedGame = false;
	static final String savefile = "savefile";
	static final String settingsfile = "settingsfile";
	static final String TAG = "ViewActivity";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewactivity);

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
		mStore = new Store(this);		
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
	}

	@Override
	protected void onResume() {
		super.onResume();
		// The following call resumes a paused rendering thread.
		// If you de-allocated graphic objects for onPause()
		// this is a good place to re-allocate them.
		mGLView.onResume();
		GlobalApplication.getPuzzleDB().open();
		GlobalApplication.getHintDB().open();
		// AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.anim.shrink_dance_button_anim);
		// set.setTarget((Button)findViewById(R.id.bee_puzzled));
		// set.end();       
	}

	@Override
	protected void onStart() {
		super.onStart();	
		mModel.state.resumeGameExists = mDataServer.resumeExists();
		mModel.state.firstRun = mDataServer.firstRun();

		GlobalApplication.getPuzzleDB().open();
		GlobalApplication.getHintDB().open();

		if(mModel.state.firstRun){
			mQuoteView.setVisibility(View.INVISIBLE);
			mModel.firstRun();
		} else {
			mModel.reset();
		}

		
		//TODO: This sucks balls. Do this elsewhere.
		LevelPackProvider mLPP = GlobalApplication.getLevelPackProvider();
		LevelPack mLP = mLPP.getLevelPack(0);
		Log.d(TAG, mLP.getTitle());
		Log.d(TAG, Integer.toString(mLP.getAllChapters().size()));
		SQLPuzzle q;
		for(Chapter c: mLP.getAllChapters()){
			for(Puzzle p: c.getAllPuzzles()){
			    q = GlobalApplication.getPuzzleDB().getPuzzle(p.getId());
			    String result = q.getCompleted();
			    System.out.println("db result "+result+" "+p.getId());
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
		TextView w = (TextView)findViewById(R.id.QuoteView);
		w.setVisibility(View.INVISIBLE);
	}
	
	public static void hintClick(View v) {
		//mStore.onBuyHints();
	}
	
	public void onBackPressed() {
		   mModel.onBack();
		}
	
	
}    



