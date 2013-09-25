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
    public static final String HINTS_ON = "hints_on";
    public static final String HINTS_OFF = "hints_off";
    public static final String CLEAR_BOARD = "clear_board";
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


	Map <String, Integer> library = new HashMap<String, Integer>();
	Typeface tf;
	Context context;
	
	
	public TextureManager(Context context) {
		tf = Typeface.createFromAsset(context.getAssets(), "MileyTwerk.ttf");
		this.context = context; 
	}
	
	
	public void buildTextures() {
		int[] x_coords = {96,96,96,96,96,96,96,96,96,96};
		int[] y_coords = {64,64,64,64,64,64,64,64,64,64};
		buildTextures("0123456789", x_coords, y_coords,64);
		buildTextures(context, R.drawable.up_arrow, UPARROW);
		buildTextures(context, R.drawable.down_arrow, DOWNARROW);
		buildTextures(context, R.drawable.left_arrow, LEFTARROW);
		buildTextures(context, R.drawable.right_arrow, RIGHTARROW);
		buildTextures(context, R.drawable.menu_circle, MENUCIRCLE);
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
		
		
		buildTextures(context, R.drawable.crayonsquare, "crayonbg");
		//TM.buildTextures(context, R.drawable.papertexture2, "paperbg");
		buildTextures(context, R.drawable.boardbg, "boardbg");
		buildTextures(context, R.drawable.blacksquare, "blacksquare");
		buildTextures(context, R.drawable.check, "check");
		//Create Menu Textures

		for(int i=0;i<6;i++){
		    buildTextures(Integer.toString(i),64,64,"menu_"+Integer.toString(i+1),64);
		}
		//Create Border Textures
		for(int i=0;i<15;i++){
		    buildTextures(Integer.toString(i),64,128,"border_"+Integer.toString(i),60);
		}
		int fontSize = 30; 
	    buildTextures(START, 64,64, START, fontSize);
	    buildTextures(NEW, 64,64, NEW, fontSize);
	    buildTextures(RESUME, 64,64, RESUME, fontSize);
	    buildTextures(OPTIONS, 64,64, OPTIONS, fontSize);
	    buildTextures(BACK, 64,64, BACK, fontSize);
	    buildTextures(SHORT, 64,64, SHORT, fontSize);
	    buildTextures(MEDIUM, 64,64, MEDIUM, fontSize);
	    buildTextures(LONGER, 64,64, LONGER, fontSize);
	    buildTextures(LONGEST, 64,64, LONGEST, fontSize);
	    buildTextures(HINTS_ON, 64,64, HINTS_ON, fontSize);
	    buildTextures(HINTS_OFF, 64,64, HINTS_OFF, fontSize);
	    buildTextures(CLEAR_BOARD, 64,64, CLEAR_BOARD, fontSize);
	    buildTextures(QUIT, 64,64, QUIT, fontSize);
	    buildTextures(YES, 64,64, YES, fontSize);
	    buildTextures(NO, 64,64, NO, fontSize);
	    buildTextures(SHARE, 64,64, SHARE, fontSize);
	    buildTextures(TUTORIAL, 64,64, TUTORIAL, fontSize);
	    buildTextures(NEXT, 64,64, NEXT, fontSize);
	    buildTextures(PREVIOUS, 64,64, PREVIOUS, fontSize);
	    buildTextures("", 64,64, CLEAR, fontSize);

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
			library.put(curr, textureFromBitmap(bitmapFromString(curr,x[i],y[i],font)));
		}
	}

	public void buildTextures(String a, int x, int y, String key , int font){
		library.put(key, textureFromBitmap(bitmapFromString(a,x,y, font)));
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
	
	Bitmap bitmapFromString(String text, int x, int y, int font){
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
int[] y_coords = {64,64,64,64,64,64,64,64,64,64};
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
    buildTextures(Integer.toString(i),64,64,"menu_"+Integer.toString(i+1),64);
}
//Create Border Textures
for(int i=0;i<rowSums.length;i++){
    buildTextures(Integer.toString(rowSums[i]),64,128,"border_row_"+Integer.toString(rowSums[i]),50);
}
for(int i=0;i<columnSums.length;i++){
	buildTextures(Integer.toString(columnSums[i]),105,64,"border_col_"+Integer.toString(columnSums[i]),50);
}
}*/
