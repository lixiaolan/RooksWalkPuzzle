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
import android.graphics.Rect;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.opengl.GLUtils;


public class TextureManager {
    public static final String START = "start";
    public static final String NEW = "new";
    public static final String RESUME = "resume";
    public static final String OPTIONS = "options";
    public static final String BACK = "back";
    public static final String SHORT = "short";
    public static final String MEDIUM = "medium";
    public static final String LONGER = "longer";
    public static final String LONGEST = "longest";
    public static final String HINTS_ON = "hints on";
    public static final String HINTS_OFF = "hints off";
    public static final String CLEAR_BOARD = "reset";
    public static final String QUIT = "quit";
    public static final String YES = "yes";
    public static final String NO = "no";
    public static final String SHARE = "share";
    public static final String CLEAR = "clear";
    public static final String BEE = "bee";
    public static final String MENUCIRCLE = "menu_circle";
    public static final String UPARROW = "up_arrow";
    public static final String DOWNARROW = "down_arrow";
    public static final String LEFTARROW = "left_arrow";
    public static final String RIGHTARROW = "right_arrow";
    public static final String TUTORIAL = "tutorial";
    public static final String NEXT = "next";
    public static final String PREVIOUS = "previous";
    public static final String LONGSTRING = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Duis sapien erat, interdum quis libero ultrices, scelerisque ullamcorper enim.";
    public static final String SHOW_SOLUTION = "solution";
    public static final String FOOT = "+";
    public static final String GOOD_JOB = "Good job!";
    public static final String TRY_AGAIN = "Keep trying...";
    
	Map <String, Integer> library = new HashMap<String, Integer>();
	Typeface tf;
	Context context;
	
	
	public TextureManager(Context context) {
		tf = Typeface.createFromAsset(context.getAssets(), "font3.ttf");
		this.context = context; 
	}
	
	
	public void buildTextures() {
		int[] x_coords = {96,96,96,96,96,96,96,96,96,96};
		int[] y_coords = {64, 64, 64, 64, 64, 64, 64, 64, 64, 64};
		buildTextures("0123456789", x_coords, y_coords,64);
		buildTextures(context, R.drawable.up_arrow, UPARROW);
		buildTextures(context, R.drawable.down_arrow, DOWNARROW);
		buildTextures(context, R.drawable.left_arrow, LEFTARROW);
		buildTextures(context, R.drawable.right_arrow, RIGHTARROW);
		buildTextures(context, R.drawable.circle, MENUCIRCLE);
		buildTextures(context, R.drawable.beecolor, BEE);
		
		buildTextures(context, R.drawable.flower1,"flower0");
		buildTextures(context, R.drawable.flower2,"flower1");
		buildTextures(context, R.drawable.flower3,"flower2");
		buildTextures(context, R.drawable.flower4,"flower3");
		buildTextures(context, R.drawable.flower5,"flower4");
		buildTextures(context, R.drawable.flower6,"flower5");
		buildTextures(context, R.drawable.flower7,"flower6");
		buildTextures(context, R.drawable.flower8,"flower7");
		buildTextures(context, R.drawable.flower9,"flower8");
		
		buildTextures(context, R.drawable.grass1,"grass0");
		
		
		//TM.buildTextures(context, R.drawable.papertexture2, "paperbg");
		buildTextures(context, R.drawable.boardbg, "boardbg");
		buildTextures(context, R.drawable.check2, "check");
		buildTextures(context, R.drawable.share, SHARE);
		buildTextures(context, R.drawable.title, "title");
		//Create Menu Textures

		buildTextures(context, R.drawable.red_x, "menu_1");
		for(int i=1;i<6;i++){
		    buildTextures(Integer.toString(i),64,80,"menu_"+Integer.toString(i+1),50);
		}
		//Create Border Textures
		for(int i=0;i<15;i++){
		    buildTextures(Integer.toString(i),60,90,"border_"+Integer.toString(i),60);
		}
		int fontSize = 25; 
		int xpos = 64;
		int ypos = 70;
	    buildTextures(START, xpos, ypos, START, fontSize);
	    buildTextures(NEW, xpos, ypos, NEW, fontSize);
	    buildTextures(RESUME, xpos, ypos, RESUME, fontSize);
	    buildTextures(OPTIONS, xpos, ypos, OPTIONS, fontSize);
	    buildTextures(BACK, xpos, ypos, BACK, fontSize);
	    buildTextures(SHORT, xpos, ypos, SHORT, fontSize);
	    buildTextures(MEDIUM, xpos, ypos, MEDIUM, fontSize);
	    buildTextures(LONGER, xpos, ypos, LONGER, fontSize);
	    buildTextures(LONGEST, xpos, ypos, LONGEST, fontSize);
	    buildTextures(HINTS_ON, xpos, ypos, HINTS_ON, fontSize);
	    buildTextures(HINTS_OFF, xpos, ypos, HINTS_OFF, fontSize);
	    buildTextures(CLEAR_BOARD, xpos, ypos, CLEAR_BOARD, fontSize);
	    buildTextures(QUIT, xpos, ypos, QUIT, fontSize);
	    buildTextures(YES, xpos, ypos, YES, fontSize);
	    buildTextures(NO, xpos, ypos, NO, fontSize);
	    //buildTextures(SHARE, xpos, ypos, R.drawable.share, fontSize);
	    buildTextures(TUTORIAL, xpos, ypos, TUTORIAL, fontSize);
	    buildTextures(NEXT, xpos, ypos, NEXT, fontSize);
	    buildTextures(PREVIOUS, xpos, ypos, PREVIOUS, fontSize);
	    buildTextures(SHOW_SOLUTION, xpos, ypos, SHOW_SOLUTION, fontSize);
	    buildLongTextures(TRY_AGAIN, xpos, ypos, TRY_AGAIN, fontSize, 256);
	    buildLongTextures(GOOD_JOB, xpos, ypos, GOOD_JOB, fontSize, 256);
	    
	    buildTextures("", xpos, ypos, CLEAR, fontSize);

	    for(int i =0;i< TutorialInfo.banners.length;i++){
	    	buildLongTextures(TutorialInfo.banners[i],2,30,"banner_"+Integer.toString(i),20, 256);
	    }

	    for(int i =0;i< TutorialInfo.bottomBanners.length;i++){
	    	buildLongTextures(TutorialInfo.bottomBanners[i],2,30,"bottom_banner_"+Integer.toString(i), 20, 256);
	    }

	    
	    buildLongTextures(TutorialInfo.OneTileBanner, 2, 30, TutorialInfo.OneTileBanner, 20, 256);
	    buildLongTextures(TutorialInfo.ShowPathBanner, 2, 30, TutorialInfo.ShowPathBanner, 20, 256);
	    
	    }
	
	
	public static String getFlowerTexture() {
		int r = (int)(Math.random()*9.0f);
		return "flower"+Integer.toString(r);
	}
	
	public static String getGrassTexture() {
		int r = (int)(Math.random()*1.0f);
		return "grass"+Integer.toString(r);
	}
	
	public void buildTextures(String a, int[] x, int[] y, int font){
		String curr;
		for(int i=0;i<a.length();i++){
			curr = String.valueOf(a.charAt(i));
			library.put(curr, textureFromBitmap(bitmapFromShortString(curr,x[i],y[i],font)));
		}
	}

	public void buildTextures(String a, int x, int y, String key , int font){
		library.put(key, textureFromBitmap(bitmapFromShortString(a,x,y, font)));
	}

	public void buildLongTextures(String a, int x, int y, String key , int font, int size){
		library.put(key, textureFromBitmap(bitmapFromLongString(a,x,y, font, size)));
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
	
	
	
	int textureFromBitmap(Bitmap bmp){
		int[] texture = new int[1];
		GLES20.glGenTextures(1, texture, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bmp,0); 
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		bmp.recycle();
		return texture[0];
	}
	
	Bitmap bitmapFromShortString(String text, int x, int y, int font){
		Bitmap bitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
        // get a canvas to paint over the bitmap
        Canvas canvas = new Canvas(bitmap);
        //bitmap.eraseColor(Color.TRANSPARENT);

        // Draw the text
        Paint textPaint = new Paint();
        textPaint.setTextSize(font);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStyle(Style.FILL);
        textPaint.setStrokeWidth(4);
        textPaint.setAntiAlias(true);
        textPaint.setARGB(0xFF, 0x00, 0x00, 0x00);
        textPaint.setTypeface(tf);
        // draw the text centered
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawText(text, x , y, textPaint);
        return bitmap;
	}
	
	Bitmap bitmapFromLongString(String text, int x, int y, int fontSize, int size) {
		//int size = 128; //Must be a power of two
		int length = text.length();
		Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        // get a canvas to paint over the bitmap
        Canvas canvas = new Canvas(bitmap);
        //bitmap.eraseColor(Color.TRANSPARENT);

        // Draw the text
        Paint textPaint = new Paint();
        textPaint.setTextSize(fontSize);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setStyle(Style.FILL);
        textPaint.setStrokeWidth(4);
        textPaint.setAntiAlias(true);
        textPaint.setARGB(0xFF, 0x00, 0x00, 0x00);
        textPaint.setSubpixelText(true);
        textPaint.setTypeface(tf);
        // draw the text centered
        canvas.drawColor(Color.TRANSPARENT);
        int index = 0;
        int oldIndex = 0;
        Rect bounds = new Rect();
        while(text.length() != 0) {
        	System.out.println(text);
        	index = textPaint.breakText(text, true, (float)size, null)-1;
        	System.out.println(index);
        	System.out.println(length);
        	oldIndex = index;
        	while(text.charAt(index) != ' ' && index > 0 && index != text.length()-1){
        		index--;
        	} 
        	if(index <= 0)
        		index=oldIndex;
        		
        	canvas.drawText(text.substring(0, index+1), x, y, textPaint);
        	textPaint.getTextBounds(text, 0, text.length(), bounds);
        	y += bounds.height()*1.03;
        	text = text.substring(index+1);
        }
        return bitmap;
	}
}



/*public void setState(GameState s, int[] rowSums, int[] columnSums) {
switch(s) {
case MAIN_MENU: loadMainMenuTextures();
				 break;
case PLAY:  cleanTextures();
			loadPlayTextures(rowSums,columnSums);
			break;
}
}

private void cleanTextures() {
Integer[] values = library.values().toArray(new Integer[0]);
int[] a = new int[values.length];
for(int i=0; i<values.length;i++){
	a[i] = values[i].intValue();
}
	GLES20.glDeleteTextures(a.length, a,0);	
	library.clear();
}

private void loadMainMenuTextures() {
buildTextures(context, R.drawable.bee,"bee");
buildTextures(context, R.drawable.flower,"flower");
buildTextures(context, R.drawable.papertexture2, "paperbg");
//Create Menu Textures
}

private void loadPlayTextures(int[] columnSums, int[] rowSums) {
int[] x_coords = {96,96,96,96,96,96,96,96,96,96};
int[] y_coords = {xpos, ypos,xpos, ypos,xpos, ypos,xpos, ypos,xpos, ypos};
buildTextures("0123456789", x_coords, y_coords,64);
buildTextures(context, R.drawable.up_arrow,"up_arrow");
buildTextures(context, R.drawable.down_arrow,"down_arrow");
buildTextures(context, R.drawable.left_arrow,"left_arrow");
buildTextures(context, R.drawable.right_arrow,"right_arrow");
buildTextures(context, R.drawable.menu_circle,"menu_circle");
buildTextures(context, R.drawable.bee,"bee");
buildTextures(context, R.drawable.flower,"flower");
buildTextures(context, R.drawable.crayonsquare, "crayonbg");
buildTextures(context, R.drawable.papertexture2, "paperbg");
buildTextures(context, R.drawable.boardbg, "boardbg");
buildTextures(context, R.drawable.blacksquare, "blacksquare");
library.put("clear", textureFromBitmap(bitmapFromString("",0,0,64)));
//Create Menu Textures
for(int i=0;i<6;i++){
    buildTextures(Integer.toString(i),xpos, ypos,"menu_"+Integer.toString(i+1),64);
}
//Create Border Textures
for(int i=0;i<rowSums.length;i++){
    buildTextures(Integer.toString(rowSums[i]),64,128,"border_row_"+Integer.toString(rowSums[i]),50);
}
for(int i=0;i<columnSums.length;i++){
	buildTextures(Integer.toString(columnSums[i]),105,64,"border_col_"+Integer.toString(columnSums[i]),50);
}
}*/
