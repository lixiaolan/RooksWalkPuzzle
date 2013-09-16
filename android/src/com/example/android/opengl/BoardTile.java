package com.example.android.opengl;
//In the future. Expose this class to state as well.

class BoardTile extends Tile{


    public int true_solution;
    public String true_arrow;

    public String number = "clear";
    public String arrow = "clear";
    private boolean clickable = true;
    protected boolean rotate = false;

    public void setTrueArrow(String arrow) {
    	this.true_arrow = arrow;
    }
    
    
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
    	true_arrow = "clear";
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
   
   public String getTrueArrow(){
	   return true_arrow;
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
    
    public boolean checkArrows() {
    	if(isBlack()) 
    		return true;
    	else {
    		return arrow==true_arrow;
    	}
    	
    }
    
    public boolean checkSolutions(){	
    	if(isBlack()){
    		return true;
    	} else {
    		if(number == "clear"){
    			if(true_solution == 0){
    				return true;
    			}
    		} else {
    			if(Integer.toString(true_solution) == number)
    				return true;
    		}
    	}
    	return false;
    }
    
}
