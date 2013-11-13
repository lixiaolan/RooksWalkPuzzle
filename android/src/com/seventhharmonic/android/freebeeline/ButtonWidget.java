package com.seventhharmonic.android.freebeeline;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.listeners.GameEventListener;

public class ButtonWidget extends ImageWidget{

	public enum ButtonStyle{
		NONE, SQUARE, CIRCLE
	}
	
	//ImageTile mText;
	ImageTile mBackground;
	GameEventListener listener;
	ButtonStyle style =ButtonStyle.CIRCLE;
	private boolean rotateToggle;
	private int currAngle;
	
	public ButtonWidget(float centerX, float centerY, float width, float height, String text){
		super(centerX, centerY, width, height, text);
		/*float[] center = {centerX, centerY, 0.0f};
		mText = new ImageTile(center, width, height, text);

		
		*/
		mBackground  = new ImageTile(center, width, height, "menu_circle");
		mBackground.setMode(MyGLRenderer.STRETCH);
	}
		
	public void setBorder(boolean border){
		this.border = border;
		if(border == false){
			style = ButtonStyle.NONE;
		}
	}
	
	public void setRotate(boolean t){
		rotateToggle = t;
		if(t == false){
		    currAngle = 0;
		    a.setAngle(currAngle);
		}
	    }
	
	public void setBorderStyle(ButtonStyle style){
		this.style = style;
		if(style==ButtonStyle.SQUARE){
			super.setBorder(true);
		} else if(style==ButtonStyle.CIRCLE){
			super.setBorder(false);
		}
	}
	
	public void setClickListener(GameEventListener l){
		listener = l;
	}
	
	@Override
	public void setCenter(float centerX, float centerY){
		//mText.setCenter(centerX, centerY);
		super.setCenter(centerX,centerY);
		mBackground.setCenter(centerX, centerY);
	}
	
	@Override
	public void touchHandler(float[] pt){
		if(super.isTouched(pt) & listener != null){
			Log.d("mText", "touched");
			listener.event(0);
		}
	}
	
	@Override
	public void draw(MyGLRenderer r) {
		if(style == ButtonStyle.CIRCLE){
			mBackground.draw(r);
		} 
		
		if(rotateToggle){
		    currAngle += 1 % 360;
		   a.setAngle(currAngle);
		}
		super.draw(r);
	}
}
