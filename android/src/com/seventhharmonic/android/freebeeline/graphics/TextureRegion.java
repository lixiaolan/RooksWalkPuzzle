package com.seventhharmonic.android.freebeeline.graphics;


class TextureRegion {

   //--Members--//
   public float u1, v1;                               // Top/Left U,V Coordinates
   public float u2, v2;                               // Bottom/Right U,V Coordinates
   float[] coords;
   //--Constructor--//
   // D: calculate U,V coordinates from specified texture coordinates
   // A: texWidth, texHeight - the width and height of the texture the region is for
   //    x, y - the top/left (x,y) of the region on the texture (in pixels)
   //    width, height - the width and height of the region on the texture (in pixels)
   public TextureRegion(float texWidth, float texHeight, float x, float y, float width, float height)  {
      this.u1 = x / texWidth;                         // Calculate U1
      this.v1 = y / texHeight;                        // Calculate V1
      this.u2 = this.u1 + ( width / texWidth );       // Calculate U2
      this.v2 = this.v1 + ( height / texHeight );     // Calculate V2
      
      coords = new float[] {
    		u2, v1,	
  			u2, v2,
  			u1, v1,
  			u2, v2,
  			u1, v2,
  			u1, v1	  
      };
      /*
      c[1], 1-c[3],	
		c[1], 1-c[2],
		c[0], 1-c[3],
		c[1], 1-c[2],
		c[0], 1-c[2],
		c[0], 1-c[3]
		*/
   }
   
   public float[] getCoords(){
	   return coords;
   }
   
}
