package com.example.android.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.example.android.opengl.R;
import com.example.android.opengl.common.RawResourceReader;
import com.example.android.opengl.common.ShaderHelper;


public class MyGLRenderer implements GLSurfaceView.Renderer {
    
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
       
    /** Store our model data in a float buffer. */
    private final FloatBuffer mSquarePositions;
    private final FloatBuffer mSquareTextureCoordinates;
    
    /** Colors. This way sucks. */
    private final FloatBuffer mSquareWhiteColor;
    private final FloatBuffer mSquareBlueColor;
    private final FloatBuffer mSquareBlackColor;
    private final FloatBuffer mSquareTransparentColor;
    private final FloatBuffer mSquareDullYellowColor;
    
    
    /** This will be used to pass in the transformation matrix. */
    private int mMVPMatrixHandle;
    /** This will be used to pass in the modelview matrix. */
    private int mMVMatrixHandle;
    /** This will be used to pass in the texture. */
    private List<Integer> mTextureHandle = new ArrayList<Integer>();	
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
    
    
    public TextureManager TM;
    /** Used only in "touched" */
        
    //These are used in our custom projection.
    
    private float screenHeight;
    private float screenWidth;
    private float cameraDistance;
    private float frustumNear;
    
    
    //Used only in draw tile. colorMap is populated in the constructor.
    private FloatBuffer mColor;
    private int[] mTextures = new int[2];
    private Map<String, FloatBuffer> colorMap = new HashMap<String, FloatBuffer>();
    
    public MyGLRenderer(final Context activityContext, Model m) {
	
	mActivityContext = activityContext;
	mModel = m;
		
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
	final float[] squareTransparentColorData = Colors.squareTransparent;
	final float[] squareDullYellowColorData = Colors.squareDullYellow;
	
	
	mSquareWhiteColor = ByteBuffer.allocateDirect(squareWhiteColorData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
	mSquareWhiteColor.put(squareWhiteColorData).position(0);
	
	mSquareBlueColor = ByteBuffer.allocateDirect(squareBlueColorData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
	mSquareBlueColor.put(squareBlueColorData).position(0);
	
	mSquareBlackColor = ByteBuffer.allocateDirect(squareBlackColorData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
	mSquareBlackColor.put(squareBlackColorData).position(0);
	
	mSquareTransparentColor = ByteBuffer.allocateDirect(squareTransparentColorData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
	mSquareTransparentColor.put(squareTransparentColorData).position(0);
	
	mSquareDullYellowColor = ByteBuffer.allocateDirect(squareDullYellowColorData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
	mSquareDullYellowColor.put(squareDullYellowColorData).position(0);
	
	
	colorMap.put("white", mSquareWhiteColor);
	colorMap.put("black", mSquareBlackColor);
	colorMap.put("blue", mSquareBlueColor);
	colorMap.put("dullyellow", mSquareDullYellowColor);
	colorMap.put("transparent",mSquareTransparentColor);
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
	//Set the background frame col// or
	GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	GLES20.glDisable(GLES20.GL_DEPTH_TEST);

	// Calculate the projection and view transformation
	// Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
	
	final String vertexShader = getVertexShader();   		
	final String fragmentShader = getFragmentShader();			
	
	final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);		
	
	final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
	
	//Handle for the program.
	mProgramHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, new String[] {"a_Position",  "a_Color", "a_TexCoordinate"});
	
	//buildTextures();
	TM = new TextureManager(mActivityContext);
	//TM.setState(GameState.MAIN_MENU,null, null);
	TM.buildTextures();
    }
    
    
    
    
    @Override
    public void onDrawFrame(GL10 unused) {
	// Draw background color
	GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
	GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);			        
	// Set our per-vertex lighting program.
	GLES20.glUseProgram(mProgramHandle);
	// Set program handles for square drawing.
	mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
	mMVMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVMatrix"); 
	mTextureHandle.add(GLES20.glGetUniformLocation(mProgramHandle, "texture_0"));
	mTextureHandle.add(GLES20.glGetUniformLocation(mProgramHandle, "texture_1"));
	mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
	mColorHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Color");
	mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoordinate");   
	//Draw the Board!	
	drawModel();
    }
    
    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
    TM.buildTextures();
	float magicNumber = 1.0f;
	
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

	// Position the eye in front of the origin.
	final float eyeX = 0.0f;
	final float eyeY = 0.0f;
	cameraDistance = magicNumber*frustumNear/ratio;
	
	// We are looking toward the distance
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

	mModel.setGeometry(getGeometry());

    }

    public float[] getTopLeft() {
    	float[] ret = new float[3];
    	ret[0] = 1.0f;
    	ret[1] = cameraDistance;
    	ret[2] = 0.0f; 
    	return ret;
    }
    
    public float[] getGeometry() {
    	float[] ret = new float[3];
    	ret[0] = 1.0f;
    	ret[1] = cameraDistance;
    	ret[2] = 0.0f; 
    	return ret;
    }
 
    
    public float[] project(float[] pt) {	
	pt[0] = -pt[0]*cameraDistance*(screenWidth/screenHeight)/frustumNear;
	pt[1] = pt[1]*cameraDistance/frustumNear;
	return pt;
    }
    
    public void resetTextureArray(int[] textures){
	textures[0] = (TM.library.get("clear"));
	textures[1] = (TM.library.get("clear"));
    }
    
    private void drawModel() {
	mModel.draw(this);
    }
    
    
    public void drawTile(float[] center, float size, String[] textures, String color, float angle, float[] pivot)
    {
    	mTextures[0] = TM.library.get(textures[0]);
    	mTextures[1] = TM.library.get(textures[1]);	
    	mColor = colorMap.get(color);


	//Apply MTextures. Need this to make transparent colors stay transparent.		
	GLES20.glEnable(GLES20.GL_BLEND);
	GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	
	for(int i=0;i<mTextures.length; i++){
	    GLES20.glActiveTexture(GLES20.GL_TEXTURE0+i);        
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[i]);
	    GLES20.glUniform1i(mTextureHandle.get(i), i);
	}
	
	
	// Determine position and size
	Matrix.setIdentityM(mModelMatrix, 0);
	Matrix.translateM(mModelMatrix, 0, center[0], center[1], center[2]);
	Matrix.scaleM(mModelMatrix, 0, size, size, size);
	
	// long time = SystemClock.uptimeMillis() % 12000L;
	// float angle = 0.030f * ((int) time);
	Matrix.rotateM(mModelMatrix, 0, angle, pivot[0], pivot[1], pivot[2]);
	
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

    public void drawTilePos(String pos, float size, String[] textures, String color, float angle, float[] pivot)
    {
	float[] center = new float[3];
	if (pos == "TOPLEFT") {
	    center = getTopLeft();
	    center[0] -= size;
	    center[1] -= size;
	}
	else if (pos == "TOPCENTER") {
	    center = getTopLeft();
	    center[0] = 0.0f;
	    center[1] -= size;
	}
	else if (pos == "MENUBOTTOM") {
	    center = getTopLeft();
	    center[0] = 0.0f;
	    center[1] += size;

	} else if(pos == "BANNERBOTTOM"){
		center = getTopLeft();
	    center[0] = .75f-size;
	    center[1] = -1*(.75f+size);
	}
	
	
	mTextures[0] = TM.library.get(textures[0]);
	mTextures[1] = TM.library.get(textures[1]);	
	mColor = colorMap.get(color);
	
	//Apply MTextures. Need this to make transparent colors stay transparent.		
	GLES20.glEnable(GLES20.GL_BLEND);
	GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	
	for(int i=0;i<mTextures.length; i++){
	    GLES20.glActiveTexture(GLES20.GL_TEXTURE0+i);        
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[i]);
	    GLES20.glUniform1i(mTextureHandle.get(i), i);
	}
	
	
	// Determine position and size
	Matrix.setIdentityM(mModelMatrix, 0);
	Matrix.translateM(mModelMatrix, 0, center[0], center[1], center[2]);
	Matrix.scaleM(mModelMatrix, 0, size, size, size);
	
	// long time = SystemClock.uptimeMillis() % 12000L;
	// float angle = 0.030f * ((int) time);
	Matrix.rotateM(mModelMatrix, 0, angle, pivot[0], pivot[1], pivot[2]);
	
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
