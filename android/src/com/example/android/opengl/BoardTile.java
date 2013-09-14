package com.example.android.opengl;
//In the future. Expose this class to state as well.

class BoardTile extends Tile{


    public int true_solution;
    public int true_direction;

    public String number = "clear";
    public String arrow = "clear";
    private boolean clickable = true;
    public String color;
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
	true_direction = -1;
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
    
   public void setTextures(String texture0, String texture1) {
	   textures[0] = texture0;
	   textures[1] = texture1;
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
