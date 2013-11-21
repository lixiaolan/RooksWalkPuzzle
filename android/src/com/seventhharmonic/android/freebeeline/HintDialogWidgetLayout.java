package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.graphics.Geometry;
import com.seventhharmonic.android.freebeeline.graphics.TextCreator.TextJustification;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
import com.seventhharmonic.android.freebeeline.listeners.GameEventListener;

public class HintDialogWidgetLayout extends WidgetLayout{

	
	TextBox mText;
	ButtonWidget mBack;
	TextBox fiveHints;
	TextBox twentyHints;
	TextBox unlimitedHints;
	ImageWidget mBackground;
	TextBox mHints;
	
	boolean draw  = false;
	
	/**
	 * @param width
	 * @param text
	 * @param mHints The hints dialogue in Board that needs to be updated.
	 */
	public HintDialogWidgetLayout(TextBox mHints){
		setWidth(.95f);
		setHeight(width/Geometry.PHI);
		setCenter(0,0);
		this.mHints = mHints;
		String text = TextureManager.HINTPROMPT;
		mBackground = new ImageWidget(0,0,width, height, TextureManager.CLEAR);
		mBackground.setMode(MyGLRenderer.STRETCH);
		mBackground.setRelativeCenter(0, 0);
		mBackground.setBorder(true);
		mBackground.setColor("white");
		
		mText = new TextBox(0,0, .95f*width, text);
		mText.setRelativeCenter(.05f,.9f*height);
		//mText.setJ(TextJustification.CENTER);
				
		mBack = new ButtonWidget(0,0, .15f, .15f, TextureManager.BACK);
		mBack.setRelativeCenter(-width+.15f, -height+.15f);
		mBack.setClickListener(new GameEventListener(){
			public void event(int i){
				deactivate();
			}
		});
		
		/*
		 * Layout the 3 buttons in a nice grid.
		 */
		float size  = 1.5f*width/5;
		float fontSize = 1.2f;	
		GridWidgetLayout mGrid = new GridWidgetLayout(3, 1, width/4);
		
		fiveHints = new TextBox(0, 0, size, "5 Hints^ $0.99");
		//fiveHints.setBorder(true);
		mGrid.addWidget(fiveHints);
		
		twentyHints = new TextBox(0, 0, size, "20 Hints^ $1.99");
		//twentyHints.setBorder(true);
		mGrid.addWidget(twentyHints);
		
		
		unlimitedHints = new TextBox(0, 0, size, "Infinite Hints^ $4.99");
		//unlimitedHints.setBorder(true);
		mGrid.addWidget(unlimitedHints);
		
		mGrid.setRelativeCenter(0, 0);
		
		
		widgetList.add(mBackground);
		widgetList.add(mText);
		widgetList.add(mBack);
		widgetList.add(mGrid);
		computeGeometry();
	}
	/*
	 * Pass in mHints. The appropriate listeners in store will set this dialog.
	 * (non-Javadoc)
	 * @see com.seventhharmonic.android.freebeeline.Widget#touchHandler(float[])
	 */
	@Override
	public void touchHandler(float[] pt){
		Log.d("HintDialog", "Got Touched in Dialog");
		Log.d("HintDialog", Float.toString(pt[0])+" "+Float.toString(pt[1]));
		if(fiveHints.isTouched(pt)){
			Log.d("HintDialog", "You want to buy 5 hints");
			ViewActivity.mStore.onBuyFiveHints(mHints);
			deactivate();
		} 
		
		if(twentyHints.isTouched(pt)){
			Log.d("HintDialog", "You want to buy 20 hints");
			ViewActivity.mStore.onBuyTwentyHints(mHints);
			deactivate();
		} 
		
		if(unlimitedHints.isTouched(pt)){
			Log.d("HintDialog", "You want to buy Unlimited hints");
			ViewActivity.mStore.onBuyUnlimitedHints(mHints);
			deactivate();
		} else {
			mBack.touchHandler(pt);
		}
	}
	

	public boolean isActive(){
		return draw;
	}
	
	public void activate(){
		draw = true;
	}
	
	public void deactivate(){
		draw = false;
	}

	@Override
	public void draw(MyGLRenderer r) {
		if(draw){
		for(Widget w: widgetList){
			w.draw(r);
		}
		}
	}
	@Override
	public void swipeHandler(String direction) {
		// TODO Auto-generated method stub
		
	}

}
