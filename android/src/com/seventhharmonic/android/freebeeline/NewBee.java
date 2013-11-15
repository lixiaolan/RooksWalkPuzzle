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

    public void setModeFast() {
	state = new BeeFast();
    }

    public void setTarget(float[] in) {
	state.setTarget(in);
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
	private float friction = 0.8f;
	
	public BeeLazy() {
	}
	
	public void enterAnimation(BeeTile[] tiles){
	    globalRefTime = 0;
	    relativeRefTime = System.currentTimeMillis();
	    bee.setVelocity(.0f, .0f);
	    period = DrawPeriod.DURING;
	}    

	float[] force = new float[2];
    
	public void duringAnimation(BeeTile[] tiles) {

	    mBeeInterface.control();

	    dt = Math.min(((float)(System.currentTimeMillis() - relativeRefTime))/1000f, .0333f);
	    relativeRefTime = System.currentTimeMillis();
   
	    force = getForce();
	    bee.setCenter2D(LATools.vSum(bee.getCenter2D(), LATools.vSProd(dt, bee.velocity)));
	    bee.velocity = LATools.vSum(bee.velocity, LATools.vSProd(dt, force));
	    
	    if(LATools.abs(bee.velocity) < speedTol && LATools.abs(LATools.vDiff(bee.getCenter2D(),target)) < positionTol){
		mBeeInterface.targetReached();
	    }
	}
	
	public float[] getForce() {
	    
	    force = LATools.vSProd(-attraction,LATools.vDiff(bee.center, target)); 
	    force = LATools.vSum(force, LATools.vSProd(-friction, bee.velocity));

	    return force;
	}	
    }

    class BeeFast extends NewBeeState<BeeTile> {
	//Used to animate
	private long globalRefTime;
        private long relativeRefTime;
	private float dt;

	//Used to measure target aquired.
	private float speedTol = .01f;
	private float positionTol = .1f;

	//Used in the movement of the bee;
	private float attraction = 2.5f;
	private float friction = 2.6f;
	
	public BeeFast() {
	}
	
	public void enterAnimation(BeeTile[] tiles){
	    globalRefTime = 0;
	    relativeRefTime = System.currentTimeMillis();
	    bee.setVelocity(.0f, .0f);
	    period = DrawPeriod.DURING;
	}    

	float[] force = new float[2];
    
	public void duringAnimation(BeeTile[] tiles) {

	    mBeeInterface.control();

	    dt = Math.min(((float)(System.currentTimeMillis() - relativeRefTime))/1000f, .0333f);
	    relativeRefTime = System.currentTimeMillis();
   
	    force = getForce();
	    bee.setCenter2D(LATools.vSum(bee.getCenter2D(), LATools.vSProd(dt, bee.velocity)));
	    bee.velocity = LATools.vSum(bee.velocity, LATools.vSProd(dt, force));
	    
	    if(LATools.abs(bee.velocity) < speedTol && LATools.abs(LATools.vDiff(bee.getCenter2D(),target)) < positionTol){
		mBeeInterface.targetReached();
	    }
	}
	
	public float[] getForce() {
	    
	    force = LATools.vSProd(-attraction,LATools.vDiff(bee.center, target)); 
	    force = LATools.vSum(force, LATools.vSProd(-friction, bee.velocity));

	    return force;
	}	
    }
}
