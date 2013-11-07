package com.seventhharmonic.android.freebeeline;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.seventhharmonic.android.freebeeline.graphics.TextureObject;


import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Environment;
import android.util.Log;


public class TextureManager {
	static final String TAG = "TextureManager";
	static final int CHAR_PER_LINE = 30;
	public static boolean texturesLoaded = false;
	public static final String START = "start";
	public static final String PLAY = "play";
	public static final String NEW = "new";
	public static final String RESUME = "resume";
	public static final String OPTIONS = "options";
	public static final String BACK = "back";
	public static final String HINTS_ON = "hints on";
	public static final String HINTS_OFF = "hints off";
	public static final String LINES_ON = "lines on";
	public static final String LINES_OFF = "lines off";
	public static final String RULE_CHECK_ON = "help on";
	public static final String RULE_CHECK_OFF = "help off";

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
	public static final String SHOW_SOLUTION = "solution";
	public static final String GOOD_JOB = "Good job! You helped Beatrice collect all the flowers in this puzzle!";
	public static final String TRY_AGAIN = "Keep trying...";
	public static final String LP1 = "Level Pack 1";
	public static final String STORY = "story";
	public static final String HORZDOTS = "horz_dots";
	public static final String VERTDOTS = "vert_dots";
	public static final String HAND = "hand";
	public static final String GEAR = "gear";
	public static final String OPENCIRCLE = "opencircle";
	public static final String CLOSEDCIRCLE = "closedcircle";
	public static final String MENU = "menu";
	public static final String LWEDGE = "lwedge";
	public static final String RWEDGE = "rwedge";
	public static final String FIVEHINTS = "5 Hints";
	public static final String TWENTYHINTS = "20 Hints";
	public static final String UNLIMITEDHINTS = "Unlimited Hints";
	public static final String HIVE = "hive";
	public static final String PUZZLESLEFT = "Puzzles left in Chapter: ";

	public static final String TURNINGRULE = " Oops! The path should turn at each step.";
	public static final String MATCHINGNUMBERRULE = "Watch out there! You can't point in the direction of a square with the same number.";
	public static final String PASSTHROUGHRULE = "Your path can only go through empty squares.";
	public static final String OFFBOARD = "Oh no! Your path went off the board!";

	public static final String VERSION = "alpha-0.2";
	public static final String BOX = "box";
	public static final String ERASER = "eraser";
	public static final String TABLE_OF_CONTENTS = "Table of Contents";
	public static final String BOARD5 = "board5";
	public static final String QUESTIONMARK = "?";

	Map <String, Integer> library = new HashMap<String, Integer>();
	Map <String, TextureObject> sheetLibrary = new HashMap<String, TextureObject>();
	Typeface tf;
	Context context;


	public TextureManager(Context context) {
		tf = Typeface.createFromAsset(context.getAssets(), "font3.ttf");
		this.context = context; 
	}


	public void buildTextures() {

		buildTextures(context, R.drawable.menu_circle_light_grey2, MENUCIRCLE);

		buildTextures(context, R.drawable.flower1,"flower0");
		buildTextures(context, R.drawable.flower2,"flower1");
		buildTextures(context, R.drawable.flower3,"flower2");
		buildTextures(context, R.drawable.flower4,"flower3");
		buildTextures(context, R.drawable.flower5,"flower4");
		buildTextures(context, R.drawable.flower6,"flower5");
		buildTextures(context, R.drawable.flower7,"flower6");
		buildTextures(context, R.drawable.flower8,"flower7");
		buildTextures(context, R.drawable.flower9,"flower8");
		 
		buildTextures(context, R.drawable.board2, "boardbg");
		buildTextures(context, R.drawable.check2, "check");
		buildTextures(context, R.drawable.share, SHARE);
		buildTextures(context, R.drawable.title_compact, "title");
		buildTextures(context, R.drawable.hand, HAND);
		buildTextures(context, R.drawable.gear, GEAR);
		buildTextures(context, R.drawable.opencircle, OPENCIRCLE);
		buildTextures(context, R.drawable.closedcircle, CLOSEDCIRCLE);
		buildTextures(context, R.drawable.lwedge, LWEDGE);
		buildTextures(context, R.drawable.rwedge, RWEDGE);
		buildTextures(context, R.drawable.box2, BOX);
		buildTextures(context, R.drawable.eraser, ERASER);
		buildTextures(context, R.drawable.board3, BOARD5);
		buildTextures(context, R.drawable.hive, HIVE);
		buildTextures("", 128, 140, CLEAR, 50);

		buildMenuBanners();
		buildGameBanners();
		buildStoryBanners();
		buildHintTextures();
		loadBitmapFromAssets();
		texturesLoaded = true;
		buildSheet();
		/*context.sendBroadcast(new Intent(
				Intent.ACTION_MEDIA_MOUNTED,
				            Uri.parse("file://" + Environment.getExternalStorageDirectory())));*/
		//ViewActivity.texturesLoadedEventHandler();
	}

	public void buildHintTextures(){
		int xpos = 50;
		int ypos = 50;
		int fontSize = 50;
		for(int i =0;i<20;i++){
			buildLongTextures(Integer.toString(i), xpos, ypos, "hints"+Integer.toString(i), fontSize, 128);
		}
	}


	/**Returns the string "hintsi" given a long i. Useful abstraction wherever hint textures need to be accessed.
	 * Used by Store and Board for example.
	 * @param i
	 * @return
	 */
	public static String buildHint(long i){
		return "hints"+Long.toString(i);
	}
	
	public void loadBitmapFromAssets() {
		// load text
		String BASE = "images";
		try {
			// get input stream for text
			String[] imageList = context.getAssets().list(BASE);
			for(String s: imageList){
				InputStream is = context.getAssets().open(BASE+"/"+s);
				Bitmap b = BitmapFactory.decodeStream(is); 
				library.put(s,textureFromBitmap(b));
			}
		}
		catch (IOException ex) {
			Log.d("TextureManager", ex.getMessage());

		}
	}

	public void buildMenuBanners() {
		buildTextures(context, R.drawable.red_x, "menu_1");
		for(int i=1;i<7;i++){
			buildTextures(Integer.toString(i),2*64,2*80,"menu_"+Integer.toString(i+1),2*50);
		}


		int fontSize = 50; 
		int xpos = 128;
		int ypos = 140;

		buildTextures(FIVEHINTS, xpos, ypos, FIVEHINTS, fontSize);
		buildTextures(TWENTYHINTS, xpos, ypos, TWENTYHINTS, fontSize);
		buildTextures(UNLIMITEDHINTS, xpos, ypos, UNLIMITEDHINTS, fontSize);

		buildTextures(QUESTIONMARK, xpos, ypos, QUESTIONMARK, fontSize);
		buildTextures(MENU, xpos, ypos, MENU, fontSize);
		buildTextures(START, xpos, ypos, START, fontSize);
		buildTextures(PLAY, xpos, ypos, PLAY, fontSize);
		buildTextures(NEW, xpos, ypos, NEW, fontSize);
		buildTextures(RESUME, xpos, ypos, RESUME, fontSize);
		buildTextures(OPTIONS, xpos, ypos, OPTIONS, fontSize);
		buildTextures(BACK, xpos, ypos, BACK, fontSize);
		buildTextures(HINTS_ON, xpos, ypos, HINTS_ON, fontSize);
		buildTextures(HINTS_OFF, xpos, ypos, HINTS_OFF, fontSize);
		buildTextures(LINES_ON, xpos, ypos, LINES_ON, fontSize);
		buildTextures(LINES_OFF, xpos, ypos, LINES_OFF, fontSize);
		buildTextures(TABLE_OF_CONTENTS, xpos, ypos, TABLE_OF_CONTENTS, fontSize);
		buildTextures(LP1, xpos, ypos, LP1, fontSize);

		buildTextures(RULE_CHECK_ON, xpos, ypos, RULE_CHECK_ON, fontSize);
		buildTextures(RULE_CHECK_OFF, xpos, ypos, RULE_CHECK_OFF, fontSize);
		buildTextures(CLEAR_BOARD, xpos, ypos, CLEAR_BOARD, fontSize);
		buildTextures(QUIT, xpos, ypos, QUIT, fontSize);
		buildTextures(YES, xpos, ypos, YES, fontSize);
		buildTextures(STORY, xpos, ypos, STORY, fontSize);
		buildTextures(NO, xpos, ypos, NO, fontSize);
		//buildTextures(SHARE, xpos, ypos, R.drawable.share, fontSize);
		buildTextures(TUTORIAL, xpos, ypos, TUTORIAL, fontSize);
		buildTextures(NEXT, xpos, ypos, NEXT, fontSize);
		buildTextures(PREVIOUS, xpos, ypos, PREVIOUS, fontSize);
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
		//float dpi = context.getResources().getDisplayMetrics().density;
		int screenWidth = context.getResources().getDisplayMetrics().widthPixels;	
		int bannerSize = closestPower((int)((.8*screenWidth)));
		// Assume that banner size is .8 here
		//.6 is the ratio of text width to height
		int fontWidth =  (int)(10*bannerSize/(6*CHAR_PER_LINE));
		int fontHeight = fontWidth;

		for(int i =0;i< TutorialInfo2.banners.length;i++){
			buildLongTextures(TutorialInfo2.banners[i],0,fontHeight,"tutorial_banner_"+Integer.toString(i),fontWidth, bannerSize);
		}

		buildLongTextures(TRY_AGAIN, 0, fontHeight, TRY_AGAIN, fontWidth, bannerSize);
		buildLongTextures(GOOD_JOB, 0, fontHeight, GOOD_JOB, fontWidth, bannerSize);
		buildLongTextures(TURNINGRULE, 0, fontHeight, TURNINGRULE, fontWidth, bannerSize);
		buildLongTextures(MATCHINGNUMBERRULE, 0, fontHeight, MATCHINGNUMBERRULE, fontWidth, bannerSize);
		buildLongTextures(PASSTHROUGHRULE, 0, fontHeight, PASSTHROUGHRULE, fontWidth, bannerSize);
		buildLongTextures(OFFBOARD, 0, fontHeight, OFFBOARD, fontWidth, bannerSize);
		buildLongTextures(VERSION, 0, fontHeight, VERSION, fontWidth, bannerSize);
		buildLongTextures(PUZZLESLEFT, 0, fontHeight, PUZZLESLEFT, fontWidth, bannerSize);
		for(int i = 0;i< 17;i++){
			buildLongTextures(PUZZLESLEFT+Integer.toString(i), 0, fontHeight, PUZZLESLEFT+Integer.toString(i), fontWidth, 128);
		}

	}

	public void buildStoryBanners() {

		/*
		 * Story banners are a bit of a different story.
		 * 
		 */
		//float dpi = context.getResources().getDisplayMetrics().density;
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
		int r = (int)(Math.random()*8.0f);
		return "flower"+Integer.toString(r);
	}

	public static String getGrassTexture() {
		int r = (int)(Math.random()*5.0f);
		return "grass"+Integer.toString(r);
	}

	public void buildSheet(){
		buildTextures(context, R.drawable.boardsheet, "sheet");
		try{
		BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open("sheetdata")));
		String name;
		for (int i = 0; i < 30; i++) {
			name = br.readLine();
			Log.d(TAG, name);
			// Split the lines using comma as delimiter
			String[] coordStrings = br.readLine().split(",");
			Log.d(TAG, coordStrings[0]+" "+coordStrings[1]);
			float[] coords = new float[4];
			for(int j =0;j<4;j++){
				coords[j] = Float.parseFloat(coordStrings[j]);
				Log.d(TAG, name+" curr float "+Float.toString(coords[j]));
			}
			TextureObject t = new TextureObject(name, coords);
			sheetLibrary.put(name, t);
		}
	} catch (Exception e){
		Log.d(TAG, e.getMessage());
	}		
	}

	public int getSheet(String t){
		if(sheetLibrary.containsKey(t)){
			return library.get("sheet");
		} else{
			try{
			return library.get(t);
			
			} catch(Exception e){
				Log.d(TAG,t);
				return library.get("sheet");
			}
		}
	}
	

	final float[] squareTextureCoordinateData =
		{							 // Front face
			1.0f, 0.0f,                             
			1.0f, 1.0f,
			0.0f, 0.0f,
			1.0f, 1.0f,
			0.0f, 1.0f,
			0.0f, 0.0f,   					
		};
	
	public float[] getSheetCoord(String t){
		if(sheetLibrary.containsKey(t)){
			/*if(togg == 0)
				return sheetLibrary.get("up_arrow").coords;
			else if (togg == 1)
				return sheetLibrary.get("down_arrow").coords;
		Log.d(TAG, Integer.toString(togg));
			togg=(togg+1)%2;
			 */			
			return sheetLibrary.get(t).coords;
		}
		
		return sheetLibrary.get("up_arrow").coords;
		//return squareTextureCoordinateData;
		
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
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inScaled = false;	// No pre-scaling
			//options.inPreferredConfig = Bitmap.Config.RGB_565;
			// Read in the resource
			Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

			// Bind to the texture in OpenGL
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandle[0]);

			// Set filtering
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
			GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);

			// Load the bitmap into the bound texture.
			GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
			// Recycle the bitmap, since its data has been loaded into OpenGL.
			
			//SaveImage(bitmap);
			
			bitmap.recycle();		
			//bitmap = null;
			//System.gc();
		}

		if (textureHandle[0] == 0)
		{
			throw new RuntimeException("Error loading texture.");
		}

		library.put(key,textureHandle[0]);
	}

	
	private void SaveImage(Bitmap finalBitmap) {
	    String root = Environment.getExternalStorageDirectory().toString();
	    File myDir = new File(root + "/beeline");    
	    myDir.mkdirs();
	    Random generator = new Random();
	    int n = 10000;
	    n = generator.nextInt(n);
	    String fname = "Image-"+ n +".jpg";
	    File file = new File (myDir, fname);
	    if (file.exists ()) file.delete (); 
	    try {
	           FileOutputStream out = new FileOutputStream(file);
	           finalBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
	           out.flush();
	           out.close();

	    } catch (Exception e) {
	           e.printStackTrace();
	    }
	}
	
	public int textureFromBitmap(Bitmap bmp){
		int[] texture = new int[1];
		GLES20.glGenTextures(1, texture, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bmp,0); 
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		//SaveImage(bmp);
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



