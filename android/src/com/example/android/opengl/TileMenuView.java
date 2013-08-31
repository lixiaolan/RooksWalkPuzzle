package com.example.android.opengl;
 
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
 
public class TileMenuView extends View {
		int cx;
		int cy;
		int radius;
	
         public TileMenuView(Context context) {
                  super(context);
         }
 
         @SuppressLint("DrawAllocation") protected void onDraw(Canvas canvas) {
        	    Paint paint = new Paint();
        	    paint.setAntiAlias(true);                         // set anti alias so it smooths
        	    
        	    //Draw Outer circle
        	    paint.setStyle(Style.FILL);
        	    paint.setColor(0xEEFFFFFF);
        	    paint.setStrokeWidth(4);
        	    radius  = getWidth()/2;
        	    cx = getWidth()/2;
        	    cy = getHeight();
        	    canvas.drawCircle(cx, cy, radius, paint);
        	    
        	    //Draw Spokes       	   
        	    paint.setStyle(Style.STROKE);        	   
        	    paint.setColor(0x3F000000);
        	    paint.setStrokeWidth(4);
        	    paint.setDither(true);                    // set the dither to true
        	    paint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        	    paint.setStrokeCap(Paint.Cap.ROUND);      // set the paint cap to round too
        	   
        	    float[] pts = createSpokes();
        	    canvas.drawLines(pts, paint);
        	    
        	    //Draw Text
        	    
        	    paint.setStyle(Style.FILL);
        	    paint.setColor(0xFF000000);
        	    paint.setTextSize(70);
        	    paint.setTextAlign(Paint.Align.CENTER);
        	    pts = createTextCoords();
        	    canvas.drawPosText("54321",pts,paint);
        	    
         }
         
         float[] createSpokes() {
        	 float [] pts =  new float[4*4];
        	 double alpha = 36;
        	 System.out.println(alpha);
        	 float bottom_offset = .4f;
        	 float top_offset = 1.0f;
        	 for(int i=0;i<4;i++){
        		 double x0 = cx+Math.cos(Math.toRadians(alpha*(i+1)))*radius*bottom_offset;
        		 double y0 = cy-Math.sin(Math.toRadians(alpha*(i+1)))*radius*bottom_offset;
        		 double x1 = cx+Math.cos(Math.toRadians(alpha*(i+1)))*radius*top_offset;
        		 double y1 = cy-Math.sin(Math.toRadians(alpha*(i+1)))*radius*top_offset;
        		 
        		 pts[4*i+0] = (float) x0;
        		 pts[4*i+1] = (float) y0;
        		 pts[4*i+2] = (float) x1;
        		 pts[4*i+3] = (float) y1;
        	 }
        	 
        	 return pts;
        	 
        	 }

         float[] createTextCoords() {
        	 float[] pts = new float[2*5];
        	 double alpha = 36;
        	 float bottom_offset = .6f;
        	 for(int i=0;i<5;i++){
        		 double x0 = cx+Math.cos(Math.toRadians(alpha*(2*i+1)/2))*radius*bottom_offset;
        		 double y0 = cy-Math.sin(Math.toRadians(alpha*(2*i+1)/2))*radius*bottom_offset;
        		 pts[2*i+0] = (float)x0;
        		 pts[2*i+1] = (float)y0;
        	 }
        	 return pts;
         }

         @Override
         public boolean onTouchEvent(MotionEvent e){
        	 if (e.getActionMasked() == 0) {
        		    float x = e.getX();
        		    float y = e.getY();

        		    float currAngle = (float) Math.atan((cy-y)/(x-cx));
        		    if(Math.pow(x-cx, 2)+Math.pow(cy-y, 2) < getWidth()/2){
        		    }
        		  
        		}
        			//A hack for right now to ensure menu appears...
        	 return false;
        	
         }
         
}