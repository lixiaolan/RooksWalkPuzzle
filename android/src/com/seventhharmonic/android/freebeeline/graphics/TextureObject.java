package com.seventhharmonic.android.freebeeline.graphics;

import android.util.Log;

public class TextureObject {
	
	String TAG = "TextureObject";
	public float[] coords;
	String texture;
	
	public TextureObject(String texture, float[] c){
		this.texture = texture;
		for(int j =0;j<4;j++){
			Log.d(TAG, texture+" in object curr float "+Float.toString(c[j]));
		}
		coords  = /*new float[]{
				1,0,
				1,1,
				0,0,
				1,1,
				0,1,
				0,0
			};*/
				
				
				
				new float[]{
			c[1], 1-c[3],	
			c[1], 1-c[2],
			c[0], 1-c[3],
			c[1], 1-c[2],
			c[0], 1-c[2],
			c[0], 1-c[3]
		};

		for(int j =0;j<12;j++){
			Log.d(TAG, texture+" in object coord built "+Float.toString(coords[j]));
		}

		
	}
	
	
}
