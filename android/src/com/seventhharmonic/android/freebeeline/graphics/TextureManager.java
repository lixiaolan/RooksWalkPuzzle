package com.seventhharmonic.android.freebeeline.graphics;

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

import com.seventhharmonic.android.freebeeline.R;


import android.content.Context;

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
import android.view.Display;

/*NOTES: 
 * - A tile of size 1 has each side length 2.
 * - Width/height in onSurfaceChanged are the phone's width and height
 * - 
 * 
 */

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
	public static final String DONE = "done";
	
	public static final String CLEAR_BOARD = "reset";
	//public static final String QUIT = "quit";
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
	public static final String CHECK = "check";
	
	public static final String TURNINGRULE = " Oops! The path should turn at each step.";
	public static final String MATCHINGNUMBERRULE = "You can't point in the direction of a square with the same number.";
	public static final String PASSTHROUGHRULE = "Your path can only go through empty squares.";
	public static final String OFFBOARD = "Oh no! Your path went off the board!";
	public static final String MULTIPOINTRULE = "Uh Oh, multiple numbers are pointing towards the same square";
	public static final String HINTPROMPT = "You are out of hints! Select the amount you would like to purchase below.";
    public static final String PLEASEFIXERROR = "Please correct the red squares before continuing!";
	
	public static final String VERSION = "beta-1.0";
	public static final String BOX = "box";
	public static final String ERASER = "eraser";
	public static final String TABLE_OF_CONTENTS = "Table of Contents";
	public static final String BOARD6 = "board6";

	public static final String BOARD5 = "board5";
	public static final String QUESTIONMARK = "?";
	public static final String IMAGEBORDER = "imageborder";
	public static final String SHOW = "show";
	public static final String HIDE = "hide";
	public static final String UPDOT = "updot";
	public static final String DOWNDOT = "downdot";
	public static final String LEFTDOT = "leftdot";
	public static final String RIGHTDOT = "rightdot";
	public static final String GOLDSTAR = "goldstar";
	public static final String SPEAKER_ON = "speaker_on";
	public static final String SPEAKER_OFF = "speaker_off";
	public static final String LOGO = "logo";
	public static final String ABOUT = "about";
	public static final String LOCK = "lock";
	
	
	TextCreator tC = new TextCreator();
	public Map <String, Integer> library = new HashMap<String, Integer>();
	public Map <String, TextureObject> sheetLibrary = new HashMap<String, TextureObject>();
	Typeface tf;
	Context context;
	
	int screenWidth;        
	private static BitmapFactory.Options options = new BitmapFactory.Options();
	
	public TextureManager(Context context) {
		tf = Typeface.createFromAsset(context.getAssets(), "Scribblz.ttf");
		this.context = context; 
		screenWidth = context.getResources().getDisplayMetrics().widthPixels;
	}

	public void buildTextures() {

			
		buildTextures(context, R.drawable.menu_circle_light_grey2, MENUCIRCLE);
		buildTextures(context, R.drawable.beecolor, BEE);
		
		buildTextures(context, R.drawable.board6, BOARD6);
		buildTextures(context, R.drawable.board5, BOARD5);
		
		buildTextures(context, R.drawable.title_compact, "title");
		buildTextures(context, R.drawable.hand1, HAND);
		
		buildTextures(context, R.drawable.opencircle, OPENCIRCLE);
		buildTextures(context, R.drawable.closedcircle, CLOSEDCIRCLE);
		
		buildTextures(context, R.drawable.lwedge, LWEDGE);
		buildTextures(context, R.drawable.rwedge, RWEDGE);
		
		buildTextures(context, R.drawable.revert, ERASER);
		buildTextures(context, R.drawable.revert, ERASER);
		
		
		buildTextures(context, R.drawable.speaker3, SPEAKER_ON);
		buildTextures(context, R.drawable.speaker3_mute, SPEAKER_OFF);
		
		buildTextures(context, R.drawable.check2, CHECK);
		buildTextures(context, R.drawable.logo, LOGO);
		
		buildTextures(context, R.drawable.lock, LOCK);

		buildMenuBanners();
		loadBitmapFromAssets();
		buildSheet();
	}

	/**Returns the string "hintsi" given a long i. Useful abstraction wherever hint textures need to be accessed.
	 * Used by Store and Board for example.
	 * @param i
	 * @return
	 */
	public static String buildHint(long i){
		return "hints:^"+Long.toString(i);
	}
	
	public static String buildHint(String g){
		return "hints:^"+g;
	}
	
	public int getFactor(int w){
		if(w<= 256){
			return 8;
		} else if(w<= 512){
			return 4;
		} else if (w <= 1024){
			return 1;
		} else 
			return 1;
	}
	
	public String getSize(){
		if(screenWidth > 1024){
			//Should get the 2048 image
			return "large";
		} else {
			//Should get a 1024 image
			return "new_medium";
		} 
		} 

	
	public void loadBitmapFromAssets() {
		// load text
		String BASE = "images/basics";
		Log.d(TAG,"int "+Integer.toString(screenWidth));
		try {
			// get input stream for flower pictures
			String[] imageList = context.getAssets().list(BASE);
			for(String s: imageList){
				InputStream is = context.getAssets().open(BASE+"/"+s);
				Bitmap b = BitmapFactory.decodeStream(is); 
				//Remove the .png
				library.put(s.substring(0, s.length()-4),textureFromBitmap(b));
			}
			
			//Now the larger images
			BASE = "images/murals/"+getSize(); 
			Bitmap b = null;
			imageList = context.getAssets().list(BASE);

			for(String s: imageList){
				InputStream is = context.getAssets().open(BASE+"/"+s);
				Log.d(TAG,"currbmp "+s);
				if(b == null){
					Log.d(TAG,"null bmp "+s);
					b =  BitmapFactory.decodeStream(is);
				}
				b = loadLargeBitmaps(is, b, false);
				library.put(s.substring(0, s.length()-4),textureFromBitmap2(b));
			}
			
			b.recycle();
		}
		catch (IOException ex) {
			Log.d("TAG", "Broke texture manager on large textures");
			Log.d("TextureManager", ex.getMessage());
		}
	}

	private static Bitmap loadLargeBitmaps(InputStream is, Bitmap reuseBitmap, boolean useRGB565) {
        options.inPreferredConfig = useRGB565 ? Bitmap.Config.RGB_565 : Bitmap.Config.ARGB_8888;
        options.inMutable = true;
        options.inSampleSize = 1;
        options.inBitmap = reuseBitmap;
        Bitmap bitmap;
        try{
        	bitmap = BitmapFactory.decodeStream(is, null, options);
        }
        catch(Exception e){
        	Log.d(TAG, "Failed to load bitmap");
        	bitmap = BitmapFactory.decodeStream(is);
        }
        if (options.inBitmap != bitmap && options.inBitmap != null) {
        	Log.d(TAG, "new bitmap");
        }
        return bitmap;
}
	
	Bitmap menuBmp;
	 
	private void buildMenuBanners() {
		buildTextures(context, R.drawable.red_x, "menu_1");
		menuBmp = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);; 
		for(int i=1;i<7;i++){
			buildTextures(Integer.toString(i),2*64,2*80,"menu_"+Integer.toString(i+1),2*50);
		}
		int fontSize = 50; 
		int xpos = 128;
		int ypos = 140;
		buildTextures("", 128, 140, CLEAR, 50);
		buildTextures(QUESTIONMARK, xpos, ypos, QUESTIONMARK, 2*fontSize);
		buildTextures(MENU, xpos, ypos, MENU, fontSize);
		buildTextures(START, xpos, ypos, START, fontSize);
		buildTextures(PLAY, xpos, ypos, PLAY, fontSize);
		buildTextures(NEW, xpos, ypos, NEW, fontSize);
		buildTextures(RESUME, xpos, ypos, RESUME, fontSize);
		buildTextures(OPTIONS, xpos, ypos, OPTIONS, fontSize);
		buildTextures(BACK, xpos, ypos, BACK, fontSize);
		buildTextures(LINES_ON, xpos, ypos, LINES_ON, fontSize);
		buildTextures(LINES_OFF, xpos, ypos, LINES_OFF, fontSize);
		buildTextures(RULE_CHECK_ON, xpos, ypos, RULE_CHECK_ON, fontSize);
		buildTextures(RULE_CHECK_OFF, xpos, ypos, RULE_CHECK_OFF, fontSize);
		buildTextures(STORY, xpos, ypos, STORY, fontSize);
		buildTextures(TUTORIAL, xpos, ypos, TUTORIAL, fontSize);
		buildTextures(NEXT, xpos, ypos, NEXT, fontSize);
		buildTextures(HIDE, xpos, ypos, HIDE, fontSize);
		buildTextures(SHOW, xpos, ypos, SHOW, fontSize);
		buildTextures(DONE, xpos, ypos, DONE, fontSize);
		buildTextures(ABOUT, xpos, ypos, ABOUT, fontSize);
		menuBmp.recycle();
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
		int r = Math.min((int)(Math.random()*8.0f)+1, 7);
		return "flower"+Integer.toString(r);
	}

	public static String getGrassTexture() {
		int r = (int)(Math.random()*5.0f);
		return "grass"+Integer.toString(r);
	}

	public void buildSheet(){
		buildTextures(context, R.drawable.boardsheet2, "sheet");
		try{
		BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open("sheetdata")));
		String name;
		//TODO: More flexible for loop here. Maybe a while loop?
		for (int i = 0; i < 33; i++) {
			name = br.readLine();
			//Log.d(TAG, name);
			// Split the lines using comma as delimiter
			String[] coordStrings = br.readLine().split(",");
			//Log.d(TAG, coordStrings[0]+" "+coordStrings[1]);
			float[] coords = new float[4];
			for(int j =0;j<4;j++){
				coords[j] = Float.parseFloat(coordStrings[j]);
				//Log.d(TAG, name+" curr float "+Float.toString(coords[j]));
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
			return sheetLibrary.get(t).coords;
		}
		
		return sheetLibrary.get("up_arrow").coords;
		//return squareTextureCoordinateData;
		
	}
	
	public void buildTextures(String a, int x, int y, String key , int font){
		library.put(key, textureFromBitmap2(bitmapFromShortString(a,x,y, font)));
	}
	
	public void buildTextures(final Context context, final int resourceId, String key){
		final int[] textureHandle = new int[1];

		GLES20.glGenTextures(1, textureHandle, 0);

		if (textureHandle[0] != 0)
		{
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = getFactor(screenWidth);	// No pre-scaling
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;
			options.inScaled =false;
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
	
	public static int textureFromBitmap(Bitmap bmp){
		int[] texture = new int[1];
		GLES20.glGenTextures(1, texture, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bmp,0); 
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		//SaveImage(bmp);
		bmp.recycle();
		bmp = null;
		return texture[0];
	}

	public static int textureFromBitmap2(Bitmap bmp){
		int[] texture = new int[1];
		GLES20.glGenTextures(1, texture, 0);
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_LINEAR);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
		GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, bmp,0); 
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
		return texture[0];
	}
	
	Bitmap bitmapFromShortString(String text, int x, int y, int font){
		//Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
		// get a canvas to paint over the bitmap
		Canvas canvas = new Canvas(menuBmp);
		menuBmp.eraseColor(Color.TRANSPARENT);

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
		return menuBmp;
	}

}



