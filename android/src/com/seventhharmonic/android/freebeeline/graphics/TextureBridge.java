package com.seventhharmonic.android.freebeeline.graphics;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.seventhharmonic.android.freebeeline.TextureManager;

public class TextureBridge {
	String TAG = "TextureBridge"
;	List<String> textTextures = new ArrayList<String>();
	public boolean hasTexture = true;
	
	
	public void createTextures(TextureManager TM){
		Log.d(TAG, "Making Text textures");
		for(String t: textTextures){
			Log.d(TAG, t);			
			TM.buildLongTextures(t, 0, 30, t, 25,  256);
		}
		textTextures.clear();
		hasTexture = false;
	}

	public void populateTextTextures(List<String> textures){
		for(String t: textures){
			textTextures.add(t);
		}
		hasTexture = true;
	}
	
	public void addTextTexture(String text){
		textTextures.add(text);
		hasTexture = true;
	}
	
	public void startTextureCreation(){
		hasTexture = true;
	}
	
	public void finishTextureCreation(){
		hasTexture = false;
	}
}
