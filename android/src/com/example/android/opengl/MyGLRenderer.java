package com.example.android.opengl;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;
import android.os.SystemClock;

import com.example.android.opengl.R;
import com.example.android.opengl.common.RawResourceReader;
import com.example.android.opengl.common.ShaderHelper;
import com.example.android.opengl.common.TextureHelper;


public class MyGLRenderer implements GLSurfaceView.Renderer {
    
    private static final String TAG = "MyGLRenderer";
    
    //This is the model
    private Model mModel;
    
    //Don't know what it does
    private final Context mActivityContext;
    
    /**
       Store the model matrix. This matrix is used to move 
       models from object space (where each model can be thought
       of being located at the center of the universe) to world space.
    */
    private float[] mModelMatrix = new float[16];
    
    /**
       Store the view matrix. This can be thought of as our camera. 
       This matrix transforms world space to eye space;
       it positions things relative to our eye.
    */
    private float[] mVMatrix = new float[16];
    
    /** Store the projection matrix. This is used to
	project the scene onto a 2D viewport. */
    private float[] mProjMatrix = new float[16];
    
    /** Allocate storage for the final combined matrix. 
	This will be passed into the shader program. */
    private float[] mMVPMatrix = new float[16];
    
    private float[] mRotationMatrix = new float[16];
    
    /** Store our model data in a float buffer. */
    private final FloatBuffer mSquarePositions;
    private final FloatBuffer mSquareTextureCoordinates;
    
    /** Colors. This way sucks. */
    private final FloatBuffer mSquareWhiteColor;
    private final FloatBuffer mSquareBlueColor;
    private final FloatBuffer mSquareBlackColor;
    
    
    /** This will be used to pass in the transformation matrix. */
    private int mMVPMatrixHandle;
    /** This will be used to pass in the modelview matrix. */
    private int mMVMatrixHandle;
    /** This will be used to pass in the texture. */
    private int mTextureArrowHandle;
    private int mTextureTextHandle;	
    /** This will be used to pass in model position information. */
    private int mPositionHandle;
    /** This will be used to pass in model color information. */
    private int mColorHandle;
    /** This will be used to pass in model texture coordinate information. */
    private int mTextureCoordinateHandle;
    
    /** How many bytes per float. */
    private final int mBytesPerFloat = 4;	
    
    /** Size of the position data in elements. */
    private final int mPositionDataSize = 3;	
    
    /** Size of the color data in elements. */
    private final int mColorDataSize = 4;	
    
    /** Size of the texture coordinate data in elements. */
    private final int mTextureCoordinateDataSize = 2;
    
    /** This is a handle to our square shading program. */
    private int mProgramHandle;
    
    /** This is a handle to our light point program. */
    private int mPointProgramHandle;
    
    private TextureManager TM;
    /** Used only in "touched" */
    private final float[] mMVPMatrixInv = new float[16];
    
    
    //These are used in our custom projection.

    private float screenHeight;
    private float screenWidth;
    private float cameraDistance;
    private float frustumNear;
        
    
    public MyGLRenderer(final Context activityContext) {
	
	mActivityContext = activityContext;
	
	// Define all info for a square.		
	
	final float[] squarePositionData =
	    {
		// Front face
		-1.0f, 1.0f, 0.0f,				
		-1.0f, -1.0f, 0.0f,
		1.0f, 1.0f, 0.0f, 
		-1.0f, -1.0f, 0.0f, 				
		1.0f, -1.0f, 0.0f,
		1.0f, 1.0f, 0.0f,
	    };	
	
	
	final float[] squareTextureCoordinateData =
	    {							 // Front face
		1.0f, 0.0f,                             
		1.0f, 1.0f,
		0.0f, 0.0f,
		1.0f, 1.0f,
		0.0f, 1.0f,
		0.0f, 0.0f,   					
	    };
	// Initialize the buffers.
	mSquarePositions = ByteBuffer.allocateDirect(squarePositionData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
	mSquarePositions.put(squarePositionData).position(0);		
	
	mSquareTextureCoordinates = ByteBuffer.allocateDirect(squareTextureCoordinateData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
	mSquareTextureCoordinates.put(squareTextureCoordinateData).position(0);
	
	final float[] squareWhiteColorData = Colors.squareWhite;
	final float[] squareBlueColorData = Colors.squareBlue;	
	final float[] squareBlackColorData = Colors.squareBlack;
	
	mSquareWhiteColor = ByteBuffer.allocateDirect(squareWhiteColorData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
	mSquareWhiteColor.put(squareWhiteColorData).position(0);
	
	mSquareBlueColor = ByteBuffer.allocateDirect(squareBlueColorData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
	mSquareBlueColor.put(squareBlueColorData).position(0);
	
	mSquareBlackColor = ByteBuffer.allocateDirect(squareBlackColorData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
	mSquareBlackColor.put(squareBlackColorData).position(0);
	
    }
    
    protected String getVertexShader()
    {
	return RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_vertex_shader);
    }
    
    protected String getFragmentShader()
    {
	return RawResourceReader.readTextFileFromRawResource(mActivityContext, R.raw.per_pixel_fragment_shader);
    }
    
    
    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
	mModel = new Model();
	
	//Set the background frame col// or
	GLES20.glClearColor(0.5f, 0.5f, 0.0f, 1.0f);
	GLES20.glDisable(GLES20.GL_DEPTH_TEST);
	
	// Position the eye in front of the origin.
	final float eyeX = 0.0f;
	final float eyeY = 0.0f;
	cameraDistance = 1.5f;
	
	// We are looking toward the origin?
	final float lookX = 0.0f;
	final float lookY = 0.0f;
	final float lookZ = 0.0f;
	
	// Set our up vector. This is where our head would be pointing were we holding the camera.
	final float upX = 0.0f;
	final float upY = 1.0f;
	final float upZ = 0.0f;
	
	// Set the view matrix. This matrix can be said to represent the camera position.
	// NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
	// view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
	Matrix.setLookAtM(mVMatrix, 0, eyeX, eyeY, -cameraDistance, lookX, lookY, lookZ, upX, upY, upZ);		
	//Matrix.setIdentityM(mVMatrix, 0);
	
	
	// Calculate the projection and view transformation
	//Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
	
	final String vertexShader = getVertexShader();   		
	final String fragmentShader = getFragmentShader();			
	
	final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);		
	
	final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
	
	//Handle for the program.
	mProgramHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, new String[] {"a_Position",  "a_Color", "a_TexCoordinate"});								               
	
	buildTextures();
    }
    
    
    public void buildTextures() {
	TM = new TextureManager();
	int[] x_coords = {96,96,96,96,96,96,96,96,96,96};
	int[] y_coords = {64,64,64,64,64,64,64,64,64,64};
	TM.buildTextures("0123456789", x_coords, y_coords);
	TM.buildTextures(mActivityContext, R.drawable.up_arrow,"up_arrow");
	TM.buildTextures(mActivityContext, R.drawable.down_arrow,"down_arrow");
	TM.buildTextures(mActivityContext, R.drawable.left_arrow,"left_arrow");
	TM.buildTextures(mActivityContext, R.drawable.right_arrow,"right_arrow");
    }
    
    @Override
    public void onDrawFrame(GL10 unused) {
	// Draw background color
	GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);			        
	// Set our per-vertex lighting program.
	GLES20.glUseProgram(mProgramHandle);
	// Set program handles for square drawing.
	mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
	mMVMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVMatrix"); 
	mTextureArrowHandle = GLES20.glGetUniformLocation(mProgramHandle, "arrow_Texture");
	mTextureTextHandle = GLES20.glGetUniformLocation(mProgramHandle, "text_Texture");
	mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
	mColorHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Color");
	mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoordinate");        
	//Draw the Board!	
	drawModel();
    }
    
    
    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
	
	// Set the OpenGL viewport to the same size as the surface.
	GLES20.glViewport(0, 0, width, height);
	
	// Create a new perspective projection matrix. The height will stay the same
	// while the width will vary as per aspect ratio.
	screenWidth = (float) width;
	screenHeight = (float) height;
	final float ratio = screenWidth/screenHeight;
	final float left = -ratio;
	final float right = ratio;
	final float bottom = -1.0f;
	final float top = 1.0f;
	frustumNear = 1.0f;
	final float far = 5.0f;
	
	Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, frustumNear, far);	
    }
    
    public void touched(float[] pt) {
        pt = project(pt);
	mModel.touched(pt);
    }
    
    public void swiped(float[] pt, int direction) {
        pt = project(pt);
	mModel.swiped(pt, direction);
    }

    public float[] project(float[] pt) {	
	pt[0] = -pt[0]*cameraDistance*(screenWidth/screenHeight)/frustumNear;
	pt[1] = pt[1]*cameraDistance/frustumNear;
	return pt;
    }
    
    private void drawModel() {
	//Animate the Model
	mModel.animate();
	FloatBuffer color;
	// Draw the board
	float[] center;
	float size;
	int[] texture = new int[2];
	int texture_toggle = 1;
	for (int i = 0; i < mModel.mBoard.puzzleTiles.length; i++) {
	    center = mModel.mBoard.puzzleTiles[i].center;
	    size =  mModel.mBoard.puzzleTiles[i].size;
	    if(mModel.mBoard.puzzleTiles[i].true_solution > 0) {
		texture[0] = TM.library.get(Integer.toString(mModel.mBoard.puzzleTiles[i].true_solution));
	    }
	    
	    String[] arrows  = {"up_arrow","down_arrow","left_arrow","right_arrow"};
	    texture[1] = TM.library.get(arrows[i%4]);
	    
	    if(mModel.mBoard.puzzleTiles[i].touched_flag){
		color = mSquareBlueColor;
	    } else {
		color = mSquareWhiteColor;
	    }
	    
	    if(mModel.mBoard.puzzleTiles[i].true_solution == -1){
		color = mSquareBlackColor;
	    }
	    drawTile(center, size, texture, color);
	}
	
	// Draw the Menu
	if (mModel.mMenu.menuActive == true) {
	    for (int i = 0; i < mModel.mMenu.menuTiles.length; i++) {
		center =  mModel.mMenu.menuTiles[i].center;
		size =  mModel.mMenu.menuTiles[i].size;
		texture[0] = TM.library.get(Integer.toString((i+1)));
		String[] arrows  = {"up_arrow","down_arrow","left_arrow","right_arrow"};
		texture[1] = TM.library.get(arrows[i%4]);
		drawTile(center, size, texture, mSquareWhiteColor);
	    }
	}
    }
    
    
    private void drawTile(float[] center, float size,int[] texture, FloatBuffer mColor)
    {
	
	//Apply Textures
	GLES20.glActiveTexture(GLES20.GL_TEXTURE0);        
	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[0]);
	GLES20.glUniform1i(mTextureTextHandle, 0); 
	
	GLES20.glActiveTexture(GLES20.GL_TEXTURE1);        
	GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture[1]);
	GLES20.glUniform1i(mTextureArrowHandle, 1); 
	
	
	// Determine position and size
	Matrix.setIdentityM(mModelMatrix, 0);
	Matrix.translateM(mModelMatrix, 0, center[0], center[1], center[2]);
	Matrix.scaleM(mModelMatrix, 0, size, size, size);

	// long time = SystemClock.uptimeMillis() % 12000L;
	// float angle = 0.030f * ((int) time);
	// Matrix.rotateM(mModelMatrix, 0, angle, 1.0f, 1.0f, 0.0f);	    
	
	// Pass in the position information
	mSquarePositions.position(0);		
	GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,0, mSquarePositions);        
	GLES20.glEnableVertexAttribArray(mPositionHandle);        
	
	// Pass in the position information
	mSquarePositions.position(0);		
	GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,0, mSquarePositions);        
	
	GLES20.glEnableVertexAttribArray(mPositionHandle);        
	
	// Pass in the color information
	mColor.position(0);
	GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false, 0, mColor);        
	GLES20.glEnableVertexAttribArray(mColorHandle);
	
	// Pass in the texture coordinate information
	mSquareTextureCoordinates.position(0);
	GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mTextureCoordinateDataSize, GLES20.GL_FLOAT, false, 0, mSquareTextureCoordinates);
	GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);
	
	
	// This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
	// (which currently contains model * view).
	Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mModelMatrix, 0);   
	
	// Pass in the modelview matrix.
	GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);                
	
	// This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
	// (which now contains model * view * projection).
	Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
	
	// Pass in the combined matrix.
	GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
	GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6); 
	
    }   
}
