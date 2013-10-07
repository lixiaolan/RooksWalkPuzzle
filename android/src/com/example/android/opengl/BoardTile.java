package com.example.android.opengl;
//In the future. Expose this class to state as well.

class BoardTile extends Tile{


    public float[] velocity = new float[2];
    public int true_solution;
    public String true_arrow;

    public String flowerTexture;
    public String grassTexture;
    
    public String number = TextureManager.CLEAR;
    public String arrow = TextureManager.CLEAR;
    private boolean clickable = true;
    protected boolean rotate = false;
    public boolean vPointedAt = false;
    public boolean hPointedAt = false;
    private Flipper flipper;
    private AngryGlow angryGlow;
    public boolean isHint = false;
    //Define an inner class to take care of flip animations
    class Flipper {
	
    	private long refTime;
    	private float duration;
    	private float swapTime;
	private float delayTime;
    	private boolean active = false;
	private boolean swapped = false;
	private String[] textures;

    	public Flipper() {
	    active = false;
    	}


    	public Flipper(float eyeDist, float[] piv, float durationTime, float delayT, String[] newTextures) {
	    //Compute swap angle from geometry
	    textures = newTextures;
	    duration = durationTime;
	    delayTime = delayT;
	    setPivot(piv);
	    float [] normPiv = LATools.vSProd(1/LATools.abs(piv), piv);
	    float orthDist = LATools.abs(LATools.vDiff(center, LATools.vSProd(LATools.vDot(center, normPiv), normPiv) ) );
	    if (LATools.vCross(center, normPiv) < 0) {
		swapTime = duration/((float)Math.PI)*(float)Math.atan(eyeDist/orthDist);
	    }
	    else {
		swapTime = duration/((float)Math.PI)*(((float)Math.PI)-(float)Math.atan(eyeDist/orthDist));
	    }
	    swapped = false;
	    refTime = System.currentTimeMillis();
	    active = true;
    	}
	

    	public void animate() {
	    float time = (System.currentTimeMillis()-refTime)/1000.0f;
    	    if (active) {
		//System.out.println("time: " + Float.toString(time));
		if (time < delayTime) {}
		else if (time - delayTime < swapTime) {
		    setAngle((time - delayTime)/duration*180);
		}
		else if (time - delayTime < duration) {
		    if (!swapped) {
			setTextures(textures[0], textures[1]);
			swapped = true;
		    }
		    setAngle(180 + (time - delayTime)/duration*180);
		}
		else {
		    setAngle(0);
		    setTextures(textures[0], textures[1]);
		    active = false;
		}
    	    }
    	}
    }

    class AngryGlow {
	
    	private long refTime;
    	private float duration;
	private float delay;
	private String finalColor;
    	private boolean active = false;
	private boolean hasGlowed = false;

    	public AngryGlow() {
	    active = false;
	    hasGlowed = false;
    	}

    	public AngryGlow(float durationTime, float delayTime, String fColor) {
	    //Compute swap angle from geometry
	    duration = durationTime;
	    delay = delayTime;
	    refTime = System.currentTimeMillis();
	    active = true;
	    finalColor = fColor;
    	}
	
    	public void animate() {
    	    if (active) {
		float time = (System.currentTimeMillis()-refTime)/1000.0f;
		if (time < delay) {}
		else if (time - delay < duration) {
		    color = "dullyellow";
		}
		else {
		    color = finalColor;
		    active = false;
		}
    	    }
    	}
    }

    public void setFlipper(float eyeDist, float[] piv, float durationTime, float delayTime, String[] newTextures) {
	if (flipper.active) {
	    if (newTextures[0] != TextureManager.CLEAR) {
		flipper.textures[0] = newTextures[0];
	    }
	    if (newTextures[1] != TextureManager.CLEAR) {
		flipper.textures[1] = newTextures[1];
	    }

	}
	else {
	    flipper = new Flipper(eyeDist, piv, durationTime, delayTime, newTextures);
	}
    }

    public void setAngryGlow(float durationTime, float delayTime, String fColor) {
	angryGlow = new AngryGlow(durationTime, delayTime, fColor);
    }

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

    public boolean isPointedAt() {
	return vPointedAt || hPointedAt;
    }

    public boolean isBlank() {
	return (number == TextureManager.CLEAR) && (arrow == TextureManager.CLEAR);
    }

    public BoardTile(float[] inCenter,float inSize) {
    	super(inCenter, inSize);
    	color = "transparent";
    	true_arrow = TextureManager.CLEAR;
    	flowerTexture  = TextureManager.getFlowerTexture();
    	grassTexture  = TextureManager.getGrassTexture();
    	velocity[0] = 0.0f;
    	velocity[1] = 0.0f;
	flipper = new Flipper();
	angryGlow = new AngryGlow();
    }

    public void setHint(){
    	number  = Integer.toString(true_solution);
    	arrow = true_arrow;
    	color = "dullyellow";
    	clickable = false;
	isHint = true;
    }
    
    public void setTrueSolution(int solution){
    	this.true_solution = solution;
    	if(true_solution != -1){
    		clickable = true;
    	}

    }
    
   public void setTextures(){
       if (!isPointedAt()) {
	   if(!isBlack()) {
	       textures[0]  = arrow;
	       textures[1]  = number;
	   } else {
	       textures[0] = TextureManager.CLEAR;
	       textures[1] = grassTexture;
	   }
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
		   textures[0] = TextureManager.CLEAR;
		   textures[1] = TextureManager.CLEAR;
	   } else {
		   number = Integer.toString(val);
	   }
   }
   
   public void draw(MyGLRenderer r) {
       flipper.animate();
       angryGlow.animate();
       r.drawTile(center, size, textures, color, angle, pivot);    
    }    
    
   public boolean checkArrows() {
    	if(isBlack()) 
    		return true;
    	else {
    		return arrow.equals(true_arrow);
    	}
    	
    }
    
   public void setSize(float s) {
    	size = s;
    }
    
    public float getSize() {
	return size;
    }
    
    
   public boolean hasNumber() {
	   if(!number.equals("clear") )
		   return true;
	   return false;
   }
    
   public boolean hasArrow() {
	   if(!arrow.equals("clear") )
		   return true;
	   return false;
   }
   
    public boolean checkSolutions(){	
    	if(isBlack()){
	    return true;
    	} else {
	    if(number.equals("clear")){
		if(true_solution == 0){
		    return true;
		}   
	    } else {
		if(Integer.toString(true_solution).equals(number))
		    return true;
	    }
    	}
    	return false;
    }
    
}
