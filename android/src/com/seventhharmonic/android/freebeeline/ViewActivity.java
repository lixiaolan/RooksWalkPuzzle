package com.seventhharmonic.android.freebeeline;

import android.app.Activity;
import android.graphics.Typeface;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.content.res.Resources;

import com.example.android.opengl.R;

public class ViewActivity extends Activity {

	private GLSurfaceView mGLView;
	private Model mModel;
	private MyGLRenderer mRenderer;    
	private TextView mQuoteView;
	private DataServer mDataServer;
	
	
	boolean savedGame = false;
	static final String savefile = "savefile";
	static final String settingsfile = "settingsfile";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewactivity);

		mModel = new Model(this);
		mRenderer = new MyGLRenderer(this, mModel);
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
		mModel.state.resumeGameExists = mDataServer.resumeExists();
		mModel.state.firstRun = mDataServer.firstRun();
		
		if(mModel.state.firstRun){
			mQuoteView.setVisibility(View.INVISIBLE);
			mModel.firstRun();
		} else {
			mModel.reset();
		}
	}

	public void closeQuoteScreen(View v) {
		TextView w = (TextView)findViewById(R.id.QuoteView);
		w.setVisibility(View.INVISIBLE);
	}
	public void onBackPressed() {
		   mModel.onBack();
		}
	
	
}    


