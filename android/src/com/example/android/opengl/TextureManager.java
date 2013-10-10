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
    
	static final int CHAR_PER_LINE = 30;
	
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
    public static final String STORY = "story";
    public static final String STATS = "Stats";
    public static final String SHORTSTATS = "shortstats";
    public static final String MEDIUMSTATS = "mediumstats";
    public static final String LONGERSTATS = "longerstats";
    public static final String LONGESTSTATS = "longeststats";
    public static final String TOTALLINES = "Total Lines";
    public static final String HORZDOTS = "horz_dots";
    public static final String VERTDOTS = "vert_dots";
    public static final String HAND = "hand";
    public static final String GEAR = "gear";
    public static final String OPENCIRCLE = "opencircle";
    public static final String CLOSEDCIRCLE = "closedcircle";
    
    
    
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
		
		//Graphics are built here
		buildTextures("0123456789", x_coords, y_coords,64);
		buildTextures(context, R.drawable.up_arrow, UPARROW);
		buildTextures(context, R.drawable.down_arrow, DOWNARROW);
		buildTextures(context, R.drawable.left_arrow, LEFTARROW);
		buildTextures(context, R.drawable.right_arrow, RIGHTARROW);
		buildTextures(context, R.drawable.menu_circle_light_grey2, MENUCIRCLE);
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
		buildTextures(context, R.drawable.grass2,"grass1");
		buildTextures(context, R.drawable.grass3,"grass2");
		buildTextures(context, R.drawable.grass4,"grass3");
		buildTextures(context, R.drawable.grass5,"grass4");
		buildTextures(context, R.drawable.grass6,"grass5");
		//TM.buildTextures(context, R.drawable.papertexture2, "paperbg");
		buildTextures(context, R.drawable.boardbg, "boardbg");
		buildTextures(context, R.drawable.check2, "check");
		buildTextures(context, R.drawable.share, SHARE);
		buildTextures(context, R.drawable.title_compact, "title");
		buildTextures(context, R.drawable.hand, HAND);
		buildTextures(context, R.drawable.gear, GEAR);
		buildTextures(context, R.drawable.vert_dots, VERTDOTS);
		buildTextures(context, R.drawable.horz_dots, HORZDOTS);
		buildTextures(context, R.drawable.opencircle, OPENCIRCLE);
		buildTextures(context, R.drawable.closedcircle, CLOSEDCIRCLE);
		
		//Create Menu Textures and Words needed
			    
	    
	    //The most useful texture ever
	    buildTextures("", 0, 0, CLEAR, 0);
	    
	    buildMenuBanners();
	    buildGameBanners();
	    buildStoryBanners();
	   /* buildTextures("Easy: ", 64, 70, SHORTSTATS, 25);
		buildTextures("Medium: ", 64, 70, MEDIUMSTATS, 25);
		buildTextures("Long: ", 64, 70, LONGERSTATS, 25);
		buildTextures("Longest: ", 70, 64, LONGESTSTATS, 25);
	    */
	    }

	
	public void buildMenuBanners() {
		buildTextures(context, R.drawable.red_x, "menu_1");
		for(int i=1;i<6;i++){
		    buildTextures(Integer.toString(i),64,80,"menu_"+Integer.toString(i+1),50);
		}

		
		int fontSize = 50; 
		int xpos = 128;
		int ypos = 140;
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
	    buildTextures(STORY, xpos, ypos, STORY, fontSize);
	    buildTextures(NO, xpos, ypos, NO, fontSize);
	    //buildTextures(SHARE, xpos, ypos, R.drawable.share, fontSize);
	    buildTextures(TUTORIAL, xpos, ypos, TUTORIAL, fontSize);
	    buildTextures(NEXT, xpos, ypos, NEXT, fontSize);
	    buildTextures(PREVIOUS, xpos, ypos, PREVIOUS, fontSize);
	    buildTextures(SHOW_SOLUTION, xpos, ypos, SHOW_SOLUTION, fontSize);
	    buildTextures(STATS, xpos, ypos, STATS, fontSize);
	    buildLongTextures(TOTALLINES, 0, 30, TOTALLINES, 30, 256);

	}
	
	public void buildGameBanners() {
	    /*
	     * Idea:Tutorial Banners should always be between -.8 and .8.
	     * This is not enforced here - it is just something to be careful about.
	     * Ideally this will be put in a more general geometry class.
	     * Shoot for 30 characters per line. Ideally never more then three lines.
	     * 
	     * Size of banners should be .8 * screenWidth rounded up to the nearest power of 2
	     * Need the dpi info for scaling
	     * 
		*/
		float dpi = context.getResources().getDisplayMetrics().density;
		int screenWidth = context.getResources().getDisplayMetrics().widthPixels;	
		int bannerSize = closestPower((int)((.8*screenWidth)));
		// Assume that banner size is .8 here
		//.6 is the ratio of text width to height
		System.out.println("Text properties");
		System.out.println(bannerSize);
		int fontWidth =  (int)(10*bannerSize/(6*CHAR_PER_LINE));
		int fontHeight = fontWidth;
		
		for(int i =0;i< TutorialInfo2.banners.length;i++){
	    	buildLongTextures(TutorialInfo2.banners[i],0,fontHeight,"tutorial_banner_"+Integer.toString(i),fontWidth, bannerSize);
	    }
		
		buildLongTextures(TRY_AGAIN, 0, 2*fontHeight, TRY_AGAIN, 2*fontWidth, bannerSize);
	    buildLongTextures(GOOD_JOB, 0, 2*fontHeight, GOOD_JOB, 2*fontWidth, bannerSize);
	    
	}
	
	public void buildStoryBanners() {

		/*
		 * Story banners are a bit of a different story.
		 * 
		 */
		float dpi = context.getResources().getDisplayMetrics().density;
		int screenWidth = context.getResources().getDisplayMetrics().widthPixels;	
		int bannerSize = closestPower((int)((.6*screenWidth)));
		// Assume that banner size is .6 here
		//.6 is the ratio of text width to height
		//20 characters per line
		int fontWidth =  (int)(10*bannerSize/(6*20));
		int middle = bannerSize/2;
		for(int i =0;i< StoryBoardInfo.banners.length;i++){
	    	buildLongTextures(StoryBoardInfo.banners[i],0,middle,"story_banner_"+Integer.toString(i), fontWidth, bannerSize);
	    }
		
	}
	
	public int closestPower(int a){
		if(a<=256){
			return 256;
		} else if(a<=512){
			return 512;
		} else if(a<=1024){
			return 1024;
		} else if(a<=2048){
			return 2048;
		} else {
			//default densities
			return 512;
		}
	}
	
	public static String getFlowerTexture() {
		int r = (int)(Math.random()*9.0f);
		return "flower"+Integer.toString(r);
	}
	
	public static String getGrassTexture() {
		int r = (int)(Math.random()*5.0f);
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
	
	public int textureFromBitmap(Bitmap bmp){
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
		Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
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
        	index = textPaint.breakText(text, true, (float)size, null)-1;
        	oldIndex = index;
        	while(text.charAt(index) != ' ' && index > 0 && index != text.length()-1){
        		index--;
        	} 
        	if(index <= 0)
        		index=oldIndex;
        		
        	canvas.drawText(text.substring(0, index+1), x, y, textPaint);
        	textPaint.getTextBounds(text, 0, text.length(), bounds);
        	y += fontSize*1.50;//bounds.height()*1.05;
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
