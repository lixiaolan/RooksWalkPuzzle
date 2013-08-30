package com.example.android.opengl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.os.SystemClock;

import com.example.android.opengl.R;
import com.example.android.opengl.common.RawResourceReader;
import com.example.android.opengl.common.ShaderHelper;
import com.example.android.opengl.common.TextureHelper;

import com.example.android.opengl.Board;
import com.example.android.opengl.Tile;

public class MyGLRenderer implements GLSurfaceView.Renderer {
    
    private static final String TAG = "MyGLRenderer";
    

    //The game board!
    private Board mBoard;
    
    //Don't know what it does
    private final Context mActivityContext;

  /**
     * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
     * of being located at the center of the universe) to world space.
     */
    private float[] mModelMatrix = new float[16];
    
    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private float[] mVMatrix = new float[16];
    
    /** Store the projection matrix. This is used to project the scene onto a 2D viewport. */
    private float[] mProjMatrix = new float[16];
    
    /** Allocate storage for the final combined matrix. This will be passed into the shader program. */
    private float[] mMVPMatrix = new float[16];
    
    
    /** Store our model data in a float buffer. */
    private final FloatBuffer mSquarePositions;
    private final FloatBuffer mSquareColors;
    private final FloatBuffer mSquareTextureCoordinates;
    
    /** This will be used to pass in the transformation matrix. */
    private int mMVPMatrixHandle;
    
    /** This will be used to pass in the modelview matrix. */
    private int mMVMatrixHandle;
        
    /** This will be used to pass in the texture. */
    private int mTextureUniformHandle;
    
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
    
    /** This is a handle to our texture data. */
    private int mTextureDataHandle;
    
    private int mTextureDataHandle2;
  
    
    // private final float[] mMVPMatrix = new float[16];
    private final float[] mMVPMatrixInv = new float[16];
    // private final float[] mProjMatrix = new float[16];
    // private final float[] mVMatrix = new float[16];
    // private final float[] mRotationMatrix = new float[16];
    
    // Declare as volatile because we are updating it from another thread
    public volatile float mAngle;
    
    
    
    
    public MyGLRenderer(final Context activityContext) {
	
	mActivityContext = activityContext;
	
	// Define points for a square.		
	
	// X, Y, Z
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
	
	// R, G, B, A
	final float[] squareColorData =
	    {
		// Left face (white)
		1.0f, 1.0f, 1.0f, 1.0f,				
		1.0f, 1.0f, 1.0f, 1.0f,
		1.0f, 1.0f, 1.0f, 1.0f,
		1.0f, 1.0f, 1.0f, 1.0f,				
		1.0f, 1.0f, 1.0f, 1.0f,
		1.0f, 1.0f, 1.0f, 1.0f,
		
	    };
	
	
	final float[] squareTextureCoordinateData =
	    {												
		// Front face
		0.0f, 0.0f, 				
		0.0f, 1.0f,
		1.0f, 0.0f,
		0.0f, 1.0f,
		1.0f, 1.0f,
		1.0f, 0.0f,				
	    };
	
	// Initialize the buffers.
	mSquarePositions = ByteBuffer.allocateDirect(squarePositionData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
	
	mSquarePositions.put(squarePositionData).position(0);		
	
	mSquareColors = ByteBuffer.allocateDirect(squareColorData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
	
	mSquareColors.put(squareColorData).position(0);
	
	
	mSquareTextureCoordinates = ByteBuffer.allocateDirect(squareTextureCoordinateData.length * mBytesPerFloat).order(ByteOrder.nativeOrder()).asFloatBuffer();
	
	mSquareTextureCoordinates.put(squareTextureCoordinateData).position(0);
	
	
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
	

	System.out.println("ONE!!!!!");

        //Set the background frame col// or
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
	
	
	final String vertexShader = getVertexShader();   		
	final String fragmentShader = getFragmentShader();			
	
	final int vertexShaderHandle = ShaderHelper.compileShader(GLES20.GL_VERTEX_SHADER, vertexShader);		
	final int fragmentShaderHandle = ShaderHelper.compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);
	
	//Handle for the program.
	final int mProgramHandle = ShaderHelper.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, new String[] {"a_Position",  "a_Color", "a_TexCoordinate"});								               
	
	// Load the texture
        mTextureDataHandle = TextureHelper.loadTexture(mActivityContext, R.drawable.test);

	System.out.println("TWO!!!!!");

    }
    
    
    
    
    
    
    
    
    
    
    @Override
    public void onDrawFrame(GL10 unused) {
	System.out.println("THREE!!!!!");

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
	
	GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);			        
        



	// SHOULD THESE BE IN THE CONSTRUCTOR???
	//////////////////////////////////
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
	
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
	////////////////////////////////////////

        
        // Set our per-vertex lighting program.
        GLES20.glUseProgram(mProgramHandle);
        
        // Set program handles for square drawing.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVPMatrix");
        mMVMatrixHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_MVMatrix"); 
        mTextureUniformHandle = GLES20.glGetUniformLocation(mProgramHandle, "u_Texture");
        mPositionHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_Color");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mProgramHandle, "a_TexCoordinate");
        
        // Set the active texture unit to texture unit 0.
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);
	
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);        
	
	
	//Draw the Board!
	System.out.println("FOUR!!!!!");	
	
	drawBoard();
    }
    
    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);
	
        float ratio = (float) width / height;
	
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
	
    }
    
    
    
    // public static int loadShader(int type, String shaderCode){
	
    //     // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
    //     // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
    //     int shader = GLES20.glCreateShader(type);
	
    //     // add the source code to the shader and compile it
    //     GLES20.glShaderSource(shader, shaderCode);
    //     GLES20.glCompileShader(shader);
	
    //     return shader;
    // }
    
    
    
        
    
    
    
    
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }
    
    
    
    
    
    
    
    
    public void touched(float[] pt) {
	float[] inPt = new float[4];
	float[] outPt = new float[4];
	inPt[0] = pt[0];
	inPt[1] = pt[1];
	inPt[2] = -1.0f;
	inPt[3] = 1.0f;
	
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
	
        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);
	Matrix.invertM(mMVPMatrixInv, 0, mMVPMatrix, 0);   
	
	Matrix.multiplyMV(outPt, 0,mMVPMatrixInv, 0,inPt, 0);
	
	pt[0] = outPt[0]/outPt[3];
	pt[1] = outPt[1]/outPt[3];
	
	//mBoard.touched(pt);
    }
   


    private void drawBoard(){
	// Loop though tiles and draw them
	float[] center;
	float size;
	System.out.println("IN DRAWBOARD!!!!!");
	int j = mBoard.puzzleTiles.length;
	for (int i = 0; i < mBoard.puzzleTiles.length; i++) {
	    System.out.println("IN LOOP!!!!!");
	    center = mBoard.puzzleTiles[i].center;
	    System.out.println("IN GET CENTER");
	    size = mBoard.puzzleTiles[i].size;
	    System.out.println("FIVE!!!!!");
	    drawTile(center, size);
	    System.out.println("SIX!!!!!");
	}	
    }

    private void drawTile(float[] center, float size){
	
	System.out.println("DRAWTILE...");
	// Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);
        
        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);        
	
	// Draw some squares.        
        Matrix.setIdentityM(mModelMatrix, 0);

	//Maybe?
	for (int i = 0; i < mModelMatrix.length; i++) {
	    mModelMatrix[i] =  mModelMatrix[i]*size;
	}


	Matrix.translateM(mModelMatrix, 0, center[0], center[1], center[2]);

	Matrix.rotateM(mModelMatrix, 0, mAngle, 1.0f, 0.0f, 0.0f);  
	
	
	
	
	// Pass in the position information
	mSquarePositions.position(0);		
	GLES20.glVertexAttribPointer(mPositionHandle, mPositionDataSize, GLES20.GL_FLOAT, false,0, mSquarePositions);        
        
	GLES20.glEnableVertexAttribArray(mPositionHandle);        
	
	// Pass in the color information
	mSquareColors.position(0);
	GLES20.glVertexAttribPointer(mColorHandle, mColorDataSize, GLES20.GL_FLOAT, false, 0, mSquareColors);        
	
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
	
	// // Pass in the light position in eye space.        
	// GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);
	
	// Draw the square.
	GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);      
	System.out.println("DONE DRAWING TILE!!!!!");
    }
 
}

