package com.example.android.opengl;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public class TextureManager {
	Map <String, Integer> library = new HashMap<String, Integer>();
	
	
	public void buildTextures(String a, int[] x, int[] y){
		String curr;
		for(int i=0;i<a.length();i++){
			curr = String.valueOf(a.charAt(i));
			library.put(curr, textureFromBitmap(bitmapFromString(curr,x[i],y[i])));
		}
	}
	
	public void buildTextures(final Context context, final int resourceId, String key){
		final int[] textureHandle = new int[1];
		
		GLES20.glGenTextures(1, textureHandle, 0);
		
		if (textureHandle[0] != 0)
		{
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;	// No pre-scaling

			// Read in the resource
			final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);
						
			// Bind to the texture in OpenGL
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);
			
			// Set filtering
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
			
			// Load the bitmap into the bound texture.
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
			// Recycle the bitmap, since its data has been loaded into OpenGL.
			bitmap.recycle();						
		}
		
		if (textureHandle[0] == 0)
		{
			throw new RuntimeException("Error loading texture.");
		}
		
		library.put(key,textureHandle[0]);
	}
	
	public void buildTextures(final Bitmap bmp, final String id){
		
	}
	
	int textureFromBitmap(Bitmap bmp){
		int[] texture = new int[1];
		GLES20.glGenTextures(1, texture, 0);
		System.out.println("Texture");
		System.out.println(texture[0]);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bmp,0); 
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		System.out.println(texture[0]);
		bmp.recycle();
		return texture[0];
	}
	
	Bitmap bitmapFromString(String text, int x, int y){
		Bitmap bitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
        // get a canvas to paint over the bitmap
        Canvas canvas = new Canvas(bitmap);
        //bitmap.eraseColor(Color.TRANSPARENT);

        // Draw the text
        Paint textPaint = new Paint();
        textPaint.setTextSize(64);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStyle(Style.FILL);
        textPaint.setStrokeWidth(4);
        textPaint.setAntiAlias(true);
        textPaint.setARGB(0xFF, 0x00, 0x00, 0x00);
        // draw the text centered
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawText(text, x , y, textPaint);
        return bitmap;
	}
	
	
}
