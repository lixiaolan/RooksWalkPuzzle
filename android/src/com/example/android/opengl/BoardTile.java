package com.example.android.opengl;
//In the future. Expose this class to state as well.

class BoardTile extends Tile{


    public int true_solution;
    public String true_arrow;

    public String flowerTexture;
    public String grassTexture;
    
    public String number = TextureManager.CLEAR;
    public String arrow = TextureManager.CLEAR;
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
    	color = "transparent";
    	true_arrow = TextureManager.CLEAR;
    	flowerTexture  = TextureManager.getFlowerTexture();
    	grassTexture  = TextureManager.getGrassTexture();
    }

    public void setHint(){
    	number  = Integer.toString(true_solution);
    	arrow = true_arrow;
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
   			textures[0] = TextureManager.CLEAR;
   			textures[1] = grassTexture;
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
	   if(val == 0 && isClickable()){
		   number = TextureManager.CLEAR;
		   arrow = TextureManager.CLEAR;
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
    
    public void setSize(float s) {
    	size = s;
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
