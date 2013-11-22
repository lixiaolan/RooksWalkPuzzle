package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.android.freebeeline.util.LATools;


//This class is intended to work in concert with a beeController class
//The bee just follows simple instructions given to it and does nothing more.
public class NewBee extends Graphic<BeeTile, NewBeeState<BeeTile> > {
    public BeeTile bee;
    private BeeInterface mBeeInterface;
    
    public NewBee(BeeInterface b) {
	float[] center = {0.0f,0.0f,0.0f};
	tiles = new BeeTile[1];
	tiles[0] = new BeeTile(center, 0.13f);
	bee = tiles[0];

	mBeeInterface = b;
	state = new BeeLazy();
    }

    @Override
    public void setState(GameState s){
	// switch(s){
	    
	// case LAZY: state = new BeeLazy(); break;
	// case FAST: state = new BeeFast(); break;
	// default: break; 
	// }
	state = new BeeLazy();
    }

    public void setMood(Mood m) {
	switch(m) {
	case LAZY: state = new BeeLazy(); break;
	case FAST: state = new BeeFast(); break;
	case ASLEEP: state = new BeeAsleep(); break;
	default: break;
	}
    }

    public void setModeFast() {
	state = new BeeFast();
    }

    public void setTarget(float[] in) {
	state.setTarget(in, bee.getCenter());
    }
        
    public void touched(float[] pt) {
	if(pt[0]< bee.center[0]+0.25f && pt[0] > bee.center[0]-.25f 
	   && pt[1]< bee.center[1]+0.25f && pt[1] > bee.center[1]-.25f ){
	    mBeeInterface.beeTouched(true);
	}
	else {
	    mBeeInterface.beeTouched(false);
	}
    }
  
    class BeeLazy extends NewBeeState<BeeTile> {
	//Used to animate
	private long globalRefTime;
        private long relativeRefTime;
	private float dt;
	
	//Used to measure target aquired.
	private float speedTol = .01f;
	private float positionTol = .1f;
	
	//Used in the movement of the bee;
	private float attraction = 1.5f;
	private float friction = 1.4f;

	// //These are for the size computations:
	// private float maxSize = .18f;
	// private float minSize = 0.08f;
	// private float sizeScale = 1.0f;
	// private float speed;
	
	public BeeLazy() {
	}

	float[] force = new float[2];
	float[] temp = new float[2];
	float[] temp2 = new float[2];
	
	@Override
	    protected void setTarget(float[] tar, float[] last) {
	    target = tar;
	    lastPos = last;
	    if (LATools.abs(bee.velocity) != 0) {
		LATools.vSProd(1.0f/LATools.abs(bee.velocity),bee.velocity, bee.velocity); 
	    }
	}

	public void enterAnimation(BeeTile[] tiles){
	    globalRefTime = 0;
	    relativeRefTime = System.currentTimeMillis();
	    bee.setVelocity(.0f, .0f);
	    period = DrawPeriod.DURING;
	}    
	
	
	public void duringAnimation(BeeTile[] tiles) {
	    
	    mBeeInterface.control();
	    
	    dt = Math.min(((float)(System.currentTimeMillis() - relativeRefTime))/1000f, .0333f);
	    relativeRefTime = System.currentTimeMillis();
	    
	    getForce();

	    LATools.vSProd(dt, bee.velocity, temp2);
	    LATools.vSum(bee.getCenter(), temp2, temp);
	    bee.setCenter2D(temp);
	    LATools.vSProd(dt, force, temp2);
	    LATools.vSum(bee.velocity, temp2, bee.velocity);
	    LATools.vDiff(bee.getCenter(),target, temp);
	    if(LATools.abs(bee.velocity) < speedTol && LATools.abs(temp) < positionTol){
	    	mBeeInterface.targetReached();
	    }

	}
	
	public void getForce() {
	    
	    LATools.vDiff(bee.center, target, temp);
	    LATools.vSProd(-attraction,temp, force);
	    LATools.vSProd(-friction, bee.velocity, temp2);
	    LATools.vSum(force, temp2, force);	    

	}	

	// public void getSize() {
	//     speed = LATools.abs(bee.velocity);
	//     bee.setSize((float)(minSize + 2.0f/Math.PI*Math.atan(speed*sizeScale)*(maxSize - minSize) ));
	// }

    }
    
    class BeeFast extends NewBeeState<BeeTile> {
	//Used to animate
	private long globalRefTime;
        private long relativeRefTime;
	private float dt;
	
	//Used to measure target aquired.
	private float speedTol = .1f;
	private float positionTol = .1f;
	
	//Used in the movement of the bee;
	private float attraction = 2.5f;
	private float friction = 2.6f;

	//These are for the size computations:
	private float maxSize = .18f;
	private float minSize = 0.08f;
	private float sizeScale = 1.0f;
	private float speed;
	public BeeFast() {
	}
	
	
	public void enterAnimation(BeeTile[] tiles){
	    globalRefTime = 0;
	    relativeRefTime = System.currentTimeMillis();
	    bee.setVelocity(.0f, .0f);
	    period = DrawPeriod.DURING;
	}    
	
	float[] force = new float[2];
	float[] temp = new float[2];
	float[] temp2 = new float[2];
       

	public void duringAnimation(BeeTile[] tiles) {
	    mBeeInterface.control();
	    
	    dt = Math.min(((float)(System.currentTimeMillis() - relativeRefTime))/1000f, .0333f);
	    relativeRefTime = System.currentTimeMillis();
	    
	    getForce();
	    getSize();
	    LATools.vSProd(dt, bee.velocity, temp2);
	    LATools.vSum(bee.getCenter(), temp2, temp);
	    bee.setCenter2D(temp);
	    LATools.vSProd(dt, force, temp2);
	    LATools.vSum(bee.velocity, temp2, bee.velocity);
	    LATools.vDiff(bee.getCenter(),target, temp);
	    if(LATools.abs(bee.velocity) < speedTol && LATools.abs(temp) < positionTol){
	    	mBeeInterface.targetReached();
	    }
	}
	
	public void getForce() {

	    LATools.vDiff(bee.center, target, temp);

	    if (LATools.abs(temp) < positionTol*3 ) {
		LATools.vSProd(-attraction,temp, force);
		LATools.vSProd(-friction, bee.velocity, temp2);
	    }
	    else {
		LATools.vSProd(1/LATools.abs(temp), temp, temp2);
		LATools.vSProd(-attraction, temp2, force);
		LATools.vSProd(-friction, bee.velocity, temp2);
	    }
	    LATools.vSum(force, temp2, force);

	}	

	public void getSize() {
	    speed = LATools.abs(bee.velocity);
	    bee.setSize((float)(minSize + 2.0f/Math.PI*Math.atan(speed*sizeScale)*(maxSize - minSize) ));
	}
	
    }

    class BeeAsleep extends NewBeeState<BeeTile> {
		
	public BeeAsleep() {
	    bee.setVelocity(0.0f,.01f);
        }
	
	public void enterAnimation(BeeTile[] tiles){
	    period = DrawPeriod.DURING;
	}
	
	public void duringAnimation(BeeTile[] tiles) {
	}	
    }
}
