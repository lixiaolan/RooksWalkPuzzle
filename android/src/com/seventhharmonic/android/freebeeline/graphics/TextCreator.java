package com.seventhharmonic.android.freebeeline.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;

import com.seventhharmonic.android.freebeeline.GlobalApplication;
import com.seventhharmonic.android.freebeeline.MyGLRenderer;


public class TextCreator {
	
	public enum TextJustification{
		CENTER, LEFT
	}
	
	private static final String TAG = "TEXTCREATOR";

	
	public final static int CHAR_START = 32;           // First Character (ASCII Code)
	public final static int CHAR_END = 126;            // Last Character (ASCII Code)
	public final static int CHAR_CNT = ( ( ( CHAR_END - CHAR_START ) + 1 ) + 1 );  // Character Count (Including Character to use for Unknown)

	public final static int CHAR_NONE = 32;            // Character to Use for Unknown (ASCII Code)
	public final static int CHAR_UNKNOWN = ( CHAR_CNT - 1 );  // Index of the Unknown Character

	public final static int FONT_SIZE_MIN = 6;         // Minumum Font Size (Pixels)
	public final static int FONT_SIZE_MAX = 180;       // Maximum Font Size (Pixels)

	public float fontSize = 0;
	
	int fontPadX=0, fontPadY=0;                            // Font Padding (Pixels; On Each Side, ie. Doubled on Both X+Y Axis)

	float fontHeight = 0;                                  // Font Height (Actual; Pixels)
	float fontAscent = 0;                                  // Font Ascent (Above Baseline; Pixels)
	float fontDescent = 0;                                 // Font Descent (Below Baseline; Pixels)

	int textureId = -1;                                     // Font Texture ID [NOTE: Public for Testing Purposes Only!]
	int textureSize = 0;                                   // Texture Size for Font (Square) [NOTE: Public for Testing Purposes Only!]
	TextureRegion textureRgn;                          // Full Texture Region

	float charWidthMax;                                // Character Width (Maximum; Pixels)
	float charHeight;                                  // Character Height (Maximum; Pixels)
	final float[] charWidths;                         // Width of Each Character (Actual; Pixels)
	TextureRegion[] charRgn;                           // Region of Each Character (Texture Coordinates)
	int cellWidth, cellHeight;                         // Character Cell Width/Height
	int rowCnt, colCnt;                                // Number of Rows/Columns

	//float scaleX = 1.0f;
	//	float scaleY = 1.0f;                              // Font Scale (X,Y Axis)
	float spaceX= 0.0f;                                      // Additional (X,Y Axis) Spacing (Unscaled)
	
	private int mColorHandle;						   // Shader color handle	
	private int mTextureUniformHandle;                 // Shader texture handle

	
	public TextCreator(){
		charWidths = new float[CHAR_CNT];               // Create the Array of Character Widths
		charRgn = new TextureRegion[CHAR_CNT];          // Create the Array of Character Regions
	}
	
	
	//--Load Font--//
	// description
	//    this will load the specified font file, create a texture for the defined
	//    character range, and setup all required values used to render with it.
	// arguments:
	//    file - Filename of the font (.ttf, .otf) to use. In 'Assets' folder.
	//    size - Requested pixel size of font (height)
	//    padX, padY - Extra padding per character (X+Y Axis); to prevent overlapping characters.
	public int load(String file, int size, int padX, int padY) {
		// setup requested values
		fontPadX = padX;                                // Set Requested X Axis Padding
		fontPadY = padY;                                // Set Requested Y Axis Padding
		fontSize = size;
		// load the font and setup paint instance for drawing
		Typeface tf = Typeface.createFromAsset( GlobalApplication.getContext().getAssets(), file );  // Create the Typeface from Font File
		Paint paint = new Paint();                      // Create Android Paint Instance
		paint.setAntiAlias( true );                     // Enable Anti Alias
		paint.setTextSize( size );                      // Set Text Size
		//paint.setColor( 0x00000000 );                   // Set ARGB (White, Opaque)
		paint.setARGB(0xFF, 0x00, 0x00, 0x00);
		paint.setTypeface( tf );                        // Set Typeface

		// get font metrics
		Paint.FontMetrics fm = paint.getFontMetrics();  // Get Font Metrics
		fontHeight = (float)Math.ceil( Math.abs( fm.bottom ) + Math.abs( fm.top ) );  // Calculate Font Height
		fontAscent = (float)Math.ceil( Math.abs( fm.ascent ) );  // Save Font Ascent
		fontDescent = (float)Math.ceil( Math.abs( fm.descent ) );  // Save Font Descent

		// determine the width of each character (including unknown character)
		// also determine the maximum character width
		char[] s = new char[2];                         // Create Character Array
		charWidthMax = charHeight = 0;                  // Reset Character Width/Height Maximums
		float[] w = new float[2];                       // Working Width Value
		int cnt = 0;                                    // Array Counter
		for ( char c = CHAR_START; c <= CHAR_END; c++ )  {  // FOR Each Character
			s[0] = c;                                    // Set Character
			paint.getTextWidths( s, 0, 1, w );           // Get Character Bounds
			charWidths[cnt] = w[0];                      // Get Width
			if ( charWidths[cnt] > charWidthMax )        // IF Width Larger Than Max Width
				charWidthMax = charWidths[cnt];           // Save New Max Width
			cnt++;                                       // Advance Array Counter
		}
		s[0] = CHAR_NONE;                               // Set Unknown Character
		paint.getTextWidths( s, 0, 1, w );              // Get Character Bounds
		charWidths[cnt] = w[0];                         // Get Width
		if ( charWidths[cnt] > charWidthMax )           // IF Width Larger Than Max Width
			charWidthMax = charWidths[cnt];              // Save New Max Width
		cnt++;                                          // Advance Array Counter

		// set character height to font height
		charHeight = fontHeight;                        // Set Character Height

		// find the maximum size, validate, and setup cell sizes
		cellWidth = (int)charWidthMax + ( 2 * fontPadX );  // Set Cell Width
		cellHeight = (int)charHeight + ( 2 * fontPadY );  // Set Cell Height
		int maxSize = cellWidth > cellHeight ? cellWidth : cellHeight;  // Save Max Size (Width/Height)
		if ( maxSize < FONT_SIZE_MIN || maxSize > FONT_SIZE_MAX )  // IF Maximum Size Outside Valid Bounds
			return -1;                                // Return Error

		// set texture size based on max font size (width or height)
		// NOTE: these values are fixed, based on the defined characters. when
		// changing start/end characters (CHAR_START/CHAR_END) this will need adjustment too!
		if ( maxSize <= 24 )                            // IF Max Size is 18 or Less
			textureSize = 256;                           // Set 256 Texture Size
		else if ( maxSize <= 40 )                       // ELSE IF Max Size is 40 or Less
			textureSize = 512;                           // Set 512 Texture Size
		else if ( maxSize <= 80 )                       // ELSE IF Max Size is 80 or Less
			textureSize = 1024;                          // Set 1024 Texture Size
		else                                            // ELSE IF Max Size is Larger Than 80 (and Less than FONT_SIZE_MAX)
			textureSize = 2048;                          // Set 2048 Texture Size

		// create an empty bitmap (alpha only)
		Bitmap bitmap = Bitmap.createBitmap( textureSize, textureSize, Bitmap.Config.ARGB_8888 );  // Create Bitmap
		Canvas canvas = new Canvas( bitmap );           // Create Canvas for Rendering to Bitmap
		bitmap.eraseColor( 0x00000000 );                // Set Transparent Background (ARGB)

		// calculate rows/columns
		// NOTE: while not required for anything, these may be useful to have :)
		colCnt = textureSize / cellWidth;               // Calculate Number of Columns
		rowCnt = (int)Math.ceil( (float)CHAR_CNT / (float)colCnt );  // Calculate Number of Rows

		// render each of the characters to the canvas (ie. build the font map)
		float x = fontPadX;                             // Set Start Position (X)
		float y = ( cellHeight - 1 ) - fontDescent - fontPadY;  // Set Start Position (Y)
		for ( char c = CHAR_START; c <= CHAR_END; c++ )  {  // FOR Each Character
			s[0] = c;                                    // Set Character to Draw
			canvas.drawText( s, 0, 1, x, y, paint );     // Draw Character
			x += cellWidth;                              // Move to Next Character
			if ( ( x + cellWidth - fontPadX ) > textureSize )  {  // IF End of Line Reached
				x = fontPadX;                             // Set X for New Row
				y += cellHeight;                          // Move Down a Row
			}
		}
		s[0] = CHAR_NONE;                               // Set Character to Use for NONE
		canvas.drawText( s, 0, 1, x, y, paint );        // Draw Character

		/*
		//OLDCODE: save the bitmap in a texture
		//textureId = TextureHelper.loadTexture(bitmap);
		 */
		textureId = textureFromBitmap(bitmap);
		Log.d(TAG, "finished creating font texture");
		// setup the array of character texture regions
		x = 0;                                          // Initialize X
		y = 0;                                          // Initialize Y
		for ( int c = 0; c < CHAR_CNT; c++ )  {         // FOR Each Character (On Texture)
			charRgn[c] = new TextureRegion( textureSize, textureSize, x, y, cellWidth-1, cellHeight-1 );  // Create Region for Character
			x += cellWidth;                              // Move to Next Char (Cell)
			if ( x + cellWidth > textureSize )  {
				x = 0;                                    // Reset X Position to Start
				y += cellHeight;                          // Move to Next Row (Cell)
			}
		}

		// create full texture region
		textureRgn = new TextureRegion( textureSize, textureSize, 0, 0, textureSize, textureSize );  // Create Full Texture Region

		// return success
		return textureId;                                    // Return Success
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
		return texture[0];
	}
	
	public int getTextureId(){
		return textureId;
	}
	
	
	/**
	 * @param r
	 * @param text
	 * @param x
	 * @param y
	 * @param width
	 * @param scaleX
	 * @param scaleY
	 */
	public void draw(MyGLRenderer r, String text, float x, float y, float width, float scaleX, float scaleY)  {
		float chrHeight = cellHeight * scaleY;          // Calculate Scaled Character Height
		float chrWidth = cellWidth * scaleX;            // Calculate Scaled Character Width
		int len = text.length();                        // Get String Length
		//x += ( chrWidth / 2.0f ) - ( fontPadX * scaleX );  // Adjust Start X
		//y += ( chrHeight / 2.0f ) - ( fontPadY * scaleY );  // Adjust Start Y
		
	
		float letterX, letterY; 
		letterX = letterY = 0;
		
		for (int i = 0; i < len; i++)  {              // FOR Each Character in String
			int c = (int)text.charAt(i) - CHAR_START;  // Calculate Character Index (Offset by First Char in Font)
			if (c < 0 || c >= CHAR_CNT)                // IF Character Not In Font
				c = CHAR_UNKNOWN;                         // Set to Unknown Character Index
			/*Notes:
			 * Recall that a tile is 2*width across. 
			 * width-letterX+x is the correct translation to align the text at the top of a TextBox
			 * TODO: Set it up so we don't truncate words at the end of line. Need a DFA
			*/
			//
			if(text.charAt(i)=='^'){
				letterX = 0;
				letterY -= 2.1*chrHeight;
			} 
			else if(text.charAt(i)==' '){
				int j = 1;					//index to track relative position to i
				float tempWidth = 0	;		//track 
				while(text.charAt(i+j) != ' ' && i+j<text.length()-1){
					tempWidth += chrWidth;	
					//If we ever exceed the width of a line, jump to the next line and keep going. 
					if(tempWidth +letterX> 2*width){
						letterX = 0;
						letterY -= 2.1*chrHeight;
						break;
					} 		
					j = Math.min(j+1, text.length()-i-1);
				}
				//Now draw our character, and let's move on our merry way.
				r.drawTextChar(width-letterX+x-chrWidth, y+letterY, chrWidth, chrHeight, charRgn[c].getCoords());
				letterX += 2*(charWidths[c] + spaceX ) * scaleX;    // Advance X Position by Scaled Character Width
			}
			
			else {
				r.drawTextChar(width-letterX+x-chrWidth, y+letterY, chrWidth, chrHeight, charRgn[c].getCoords());
				letterX += 2*(charWidths[c] + spaceX ) * scaleX;    // Advance X Position by Scaled Character Width
			}
		}
	}

	public void drawCenter(MyGLRenderer r, String text, float x, float y, float width, float scaleX, float scaleY){
		String[] lines = text.split("\\^");
		
		for(int i =0;i<lines.length;i++){
			//Log.d(TAG, "lines "+lines[i]);
			float len = getLength(lines[i], scaleX, scaleY);
			draw(r, lines[i], x, y, len, scaleX, scaleY);
			y-=2.1f*charHeight*scaleY;
		}
		
	}
	
	public float getLength(String text, float scaleX, float scaleY) {
		float len = 0.0f;                               // Working Length
		int strLen = text.length();                     // Get String Length (Characters)
		for ( int i = 0; i < strLen; i++ )  {           // For Each Character in String (Except Last
			int c = (int)text.charAt( i ) - CHAR_START;  // Calculate Character Index (Offset by First Char in Font)
			len += ( charWidths[c] * scaleX );           // Add Scaled Character Width to Total Length
		}
		len += ( strLen > 1 ? ( ( strLen - 1 ) * spaceX ) * scaleX : 0 );  // Add Space Length
		return len;                                     // Return Total Length
	}
		
		
	/**The board unit is know as a b. A bf is .04 B's. 
	 * That means a square on the board (.22 across) will consist of 5 to 6 such characters.
	 * @param b
	 * @return
	 */
	public float bfToB(float bf){
		//Extra multiple of 2 due to fucked-upness in how we draw.
		return bf*.08f;
	}
	
	/**Convert bf units to pixels. 
	 * @param bf
	 * @return
	 */
	public float bToPixels(float b){
		float width = GlobalApplication.getContext().getResources().getDisplayMetrics().widthPixels;
		return b*width/2;
	}
	
	public static float pxToB(float px){
		float width = GlobalApplication.getContext().getResources().getDisplayMetrics().widthPixels;
		return px*2/width;
	}
	
	public float getCharHeight(){
		return charHeight; 
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

