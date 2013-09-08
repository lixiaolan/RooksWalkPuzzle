package com.example.android.opengl;
 
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
 
public class LineGraphView extends View {
		int cx;
		int cy;
		int radius;
		float[] data = new float[1];
		
         public LineGraphView(Context context, AttributeSet attrs) {
                  super(context,attrs);
         }
 
         public void setData(float[] x){
        	 data = x;
        	 float m = 0;
        	 for(int i =0; i< x.length;i++){
        		 m = Math.max(m,x[i]);
        	 }
        	 for(int i=0;i < data.length;i++){
        		 data[i]  = data[i]/m;
        	 }
         }
         
         @SuppressLint("DrawAllocation") protected void onDraw(Canvas canvas) {
        	    Paint paint = new Paint();
        	    paint.setAntiAlias(true);                         // set anti alias so it smooths
        	    
        	    //Draw Outer circle
        	    paint.setStyle(Style.STROKE);
        	    paint.setColor(0xFF33B5E5);
        	    paint.setStrokeWidth(4);
        	   
        	    float xtick = canvas.getWidth()/data.length;
        	    float left = getPaddingLeft();
        	    float right = getPaddingRight();
        	    float top = getPaddingTop();
        	    float bottom = getPaddingBottom();
        	    float height = canvas.getHeight()-top - bottom;
        	    float width = canvas.getWidth()-left-right;
        	    Path graph = new Path();
        	    graph.moveTo(left,height*(1 - data[0]));
        	    for(int i = 1; i< data.length; i++){
        	    	graph.lineTo(left+xtick*i,height*(1-data[i]));
        	    	}
        	 
        	    canvas.drawPath(graph, paint);
        	    
        	    //Draw a grid
        	    paint.setColor(0x11FFFFFF);
        	    for (int i=0 ; i<5; i++) {
        	    	//Vertical Lines
        	    	canvas.drawLine(i*width/5, top, i*width/5, height, paint);
        	    	//Horizontal Lines
        	    	canvas.drawLine(0.0f, i*height/5, width, i*height/5, paint);
        	    }
        	    
        	    
        	 
         }
         
}