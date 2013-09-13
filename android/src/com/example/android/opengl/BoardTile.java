package com.example.android.opengl;


class BoardTile extends Tile{

    public int true_solution;

    public String number = "clear";
    public String arrow = "clear";

    public String color;

    public BoardTile(float[] inCenter,float inSize, int solution) {
    	super(inCenter, inSize);
    	true_solution = solution;
    	center = inCenter;
    	size = inSize;
    	color = "transparent";
    }

   public void setTextures(String texture0, String texture1) {
	   textures[0] = texture0;
	   textures[1] = texture1;
   }
    
   public void setTextures(){
	   if(true_solution != -1) {
		   textures[0]  = arrow;
		   textures[1]  = number;
   		} else {
   			textures[0] = "clear";
   			textures[1] = "blacksquare";
   		}
   }
   
   
   public void setColor() {
		if (true_solution == -1)
		    color = "transparent";
		else
		    color = "transparent";

   }
   
   public void setColor(String c) {
	   color = c;
   }
   
   
   public void setUserInput(int val){
	   if(val == 0 && true_solution != -1){
		   number = "clear";
		   arrow = "clear";
	   } else {
	   number = Integer.toString(val);
	   }
   }
   
    public void draw(MyGLRenderer r) {
    	r.drawTile(center, size, textures, color, angle, pivot);
    }    
}
