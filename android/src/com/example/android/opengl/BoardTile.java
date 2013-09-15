package com.example.android.opengl;
//In the future. Expose this class to state as well.

class BoardTile extends Tile{

    private int true_solution;
    public String number = "clear";
    public String arrow = "clear";
    private boolean clickable = true;
    protected boolean rotate = false;
    
    public boolean isClickable() {
		return clickable;
	}

    public boolean isBlack() {
		if(true_solution == -1)
			return true;
		else
			return false;
	}
    
    public BoardTile(float[] inCenter,float inSize) {
    	super(inCenter, inSize);
    	center = inCenter;
    	size = inSize;
    	color = "transparent";
    }

    public void setHint(){
    	number  = Integer.toString(true_solution);
    	clickable = false;
    }
    
    public void setTrueSolution(int solution){
    	this.true_solution = solution;
    	if(true_solution != -1){
    		clickable = true;
    	}

    }
    
   public void setTextures(){
	   if(!isBlack()) {
		   textures[0]  = arrow;
		   textures[1]  = number;
   		} else {
   			textures[0] = "clear";
   			textures[1] = "blacksquare";
   		}
   }
   
   public void setRotate(boolean rotate){
	   this.rotate = rotate;
   }
   
   public void setArrow(String arrow){
	   this.arrow = arrow;
   }

   public String getArrow(){
	   return arrow;
   }

   
   public void setNumber(String number){
	   this.number = number;
   }

   public String getNumber(){
	   return number;
   }

   
   public int getTrueSolution(){
	   return true_solution;
   }
   
   public void setUserInput(int val){
	   if(val == 0 && !isBlack()){
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
