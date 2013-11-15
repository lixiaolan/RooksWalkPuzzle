package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.android.freebeeline.graphics.TextCreator;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;

import android.util.Log;

/*
 * Every widget has an absolute center and a relative center. The absolute center
 * is computed in Widget layout and passed in. The relative center is specified by the user
 * You have to override the setCenter method to pass down the center data to your own squares.
 * 
 */

public class ImageWidget extends Widget{

	ImageTile a;
	String color = "transparent";
	String text;
	Boolean border = false;
	

	ImageTile[] borderTiles;

	public ImageWidget(float centerX, float centerY , float width, float height, String text){
		this.text = text;
		float[] center = {centerX, centerY, 0.0f};
		a = new ImageTile(center, width, height, text);
		setCenter(center);
		setWidth(width);
		setHeight(height);
	}

	private void createBorderTiles(){
		borderTiles = new ImageTile[4];
		float s = TextCreator.pxToB(2);
		//top
		borderTiles[0] = new ImageTile(center[0],center[1]+height+s,width+s,s,TextureManager.CLEAR);
		borderTiles[0].setColor("black");
		//bottom
		borderTiles[1] = new ImageTile(center[0],center[1]-height-s,width+s,s,TextureManager.CLEAR);
		borderTiles[1].setColor("black");
		//left
		borderTiles[2] = new ImageTile(center[0]+width+s,center[1],s,s+height,TextureManager.CLEAR);
		borderTiles[2].setColor("black");
		//top
		borderTiles[3] = new ImageTile(center[0]-width-s,center[1],s,s+height,TextureManager.CLEAR);
		borderTiles[3].setColor("black");

	}
	

	private void recenterBorderTiles(){
		if(borderTiles != null){
			float s = TextCreator.pxToB(2);
			//top
			borderTiles[0].setCenter(center[0],center[1]+height+s);
			//bottom
			borderTiles[1].setCenter(center[0],center[1]-height-s);
			//left
			borderTiles[2].setCenter(center[0]+width+s,center[1]);
			//top
			borderTiles[3].setCenter(center[0]-width-s,center[1]);

		}
	}

	
	public void setImage(String image){
		a.setTextures(image, TextureManager.CLEAR);
	}
	
	public void setMode(String mode){
		a.setMode(mode);
	}
	
	public void setColor(String color){
		this.color = color;
		a.setColor(color);
	}
	
	public void setBackground(String image){
		if(image != null){
			a.setTextures(text, image);
		} 
	}

	
	public void setBorder(boolean border){
		this.border = border;
		//Create the booder tiles if they do not yet exist.
		if(borderTiles == null && border == true){
			createBorderTiles();
		//If they do exist, we should probably refresh their position.
		} else if(border == true){
			recenterBorderTiles();
		}
	}
	
	public Boolean getBorder() {
		return border;
	}
	
	@Override
	public void setCenter(float x, float y){
		super.setCenter(x,y);
		a.setCenter(x, y);
		recenterBorderTiles();
	}

	public void setPivot(float[] pivot){
		a.setPivot(pivot);
	}
	
	public void setAngle(float angle){
		a.setAngle(angle);
	}
	
	public void setWidth(float width){
		a.setWidth(width);
		this.width = width;
	}
	
	public void setHeight(float height){
		a.setHeight(height);
		this.height = height;
	}
	
	@Override
	public boolean isTouched(float[] pt){
		Log.d("ImageWidget", text+" "+Boolean.toString(a.touched(pt)));
		return a.touched(pt);
	}
	
	@Override
	public void draw(MyGLRenderer r) {
		a.draw(r);
		if(border){
			for(int i =0;i<borderTiles.length;i++){
				borderTiles[i].draw(r);
			}
		}
	}

	@Override
	public void touchHandler(float[] pt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void swipeHandler(String direction) {
		// TODO Auto-generated method stub
		
	}

}
