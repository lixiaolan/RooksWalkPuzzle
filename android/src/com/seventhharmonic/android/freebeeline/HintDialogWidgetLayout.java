package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.graphics.Geometry;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
import com.seventhharmonic.android.freebeeline.listeners.GameEventListener;

public class HintDialogWidgetLayout extends WidgetLayout{

	
	TextBox mText;
	ButtonWidget mBack;
	ImageWidget fiveHints;
	ImageWidget twentyHints;
	ImageWidget unlimitedHints;
	ImageWidget mBackground;
	TextBox mHints;
	
	boolean draw  = false;
	
	/**
	 * @param width
	 * @param text
	 * @param mHints The hints dialogue in Board that needs to be updated.
	 */
	public HintDialogWidgetLayout(float width, String text, TextBox mHints){
		setWidth(width);
		setHeight(width/Geometry.PHI);
		setCenter(0,0);
		this.mHints = mHints;
		
		mBackground = new ImageWidget(0,0,width, height, TextureManager.CLEAR);
		mBackground.setMode(MyGLRenderer.STRETCH);
		mBackground.setRelativeCenter(0, 0);
		mBackground.setBorder(true);
		mBackground.setColor("opaque");
		
		mText = new TextBox(0,0,.95f*width, text);
		mText.setRelativeCenter(.05f,.9f*height);
				
		mBack = new ButtonWidget(0,0, width/5, width/5, TextureManager.BACK);
		mBack.setRelativeCenter(-.7f*width, -.6f*height);
		mBack.setClickListener(new GameEventListener(){
			public void event(int i){
				deactivate();
			}
		});
		
		/*
		 * Layout the 3 buttons in a nice grid.
		 */
		float size  = width/5;
		
		GridWidgetLayout mGrid = new GridWidgetLayout(3, 1, width/4);
		
		fiveHints = new ImageWidget(0, 0, size, size, TextureManager.FIVEHINTS);
		fiveHints.setBorder(true);
		mGrid.addWidget(fiveHints);
		
		twentyHints = new ImageWidget(0, 0, size, size, TextureManager.TWENTYHINTS);
		twentyHints.setBorder(true);
		mGrid.addWidget(twentyHints);
		
		unlimitedHints = new ImageWidget(0, 0, size, size, TextureManager.UNLIMITEDHINTS);
		unlimitedHints.setBorder(true);
		mGrid.addWidget(unlimitedHints);
		
		mGrid.setRelativeCenter(width/3f, -height+1.1f*size);
		
		
		widgetList.add(mBackground);
		widgetList.add(mText);
		widgetList.add(mBack);
		widgetList.add(mGrid);
	//	widgetList.add(fiveHints);
	//	widgetList.add(twentyHints);
	//	widgetList.add(unlimitedHints);		
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
		if(fiveHints.a.touched(pt)){
			Log.d("HintDialog", "You want to buy 5 hints");
			ViewActivity.mStore.onBuyFiveHints(mHints);
			deactivate();
		} 
		
		if(twentyHints.isTouched(pt)){
			Log.d("HintDialog", "You want to buy 20 hints");
			//ViewActivity.mStore.onBuyTwentyHints(mHints);
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

}
