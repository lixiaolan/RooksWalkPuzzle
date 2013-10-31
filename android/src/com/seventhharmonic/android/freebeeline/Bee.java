package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.android.freebeeline.util.LATools;

public class Bee extends Graphic<BeeTile, BeeState<BeeTile>> {
	public BeeTile bee;
	private BeeBoardInterface mBoard;

	public Bee(BeeBoardInterface b) {
		float[] center= {0.0f,0.0f,0.0f};
		tiles = new BeeTile[1];
		tiles[0] = new BeeTile(center,0.13f);
		bee = tiles[0];
		mBoard = b;
		setState(GameState.MAIN_MENU_OPENING); 
	}

	public void setState(GameState s){
		switch(s){
		case MAIN_MENU_OPENING: state = new BeeWander( Mood.ASLEEP); break;
		case GAME_OPENING: state = new BeeFixed( Mood.ASLEEP); break;
		default: break; 
		}
	}

	public void setState(GameState s, int l){
		switch(s){
		case MAIN_MENU_OPENING: state = new BeeWander( Mood.ASLEEP); break;
		case GAME_OPENING: state = new BeeFixed( Mood.ASLEEP, l); 
		break;
		default: break;
		}
	}

	public int touched(float[] pt) {
		if(pt[0]< bee.center[0]+0.25f && pt[0] > bee.center[0]-.25f 
				&& pt[1]< bee.center[1]+0.25f && pt[1] > bee.center[1]-.25f ){
			return 1;
		}
		return 0;
	}

	public void setMood(Mood m) {
		state.setMood(m);
	}

	class BeeWander extends BeeState<BeeTile> {
	    public long globalRefTime;
	    public long relativeRefTime;
	    
	    BeeTile bee;

	float interval = 10000f;

	boolean flipped = true;
	int r = 0;
	float[] pivot = {1,0,1};

	public BeeWander(Mood m) {
	    setMood(m);
	}

	public void enterAnimation(BeeTile[] tiles){
	    bee = (BeeTile)tiles[0];
	    globalRefTime = 0;
	    relativeRefTime = System.currentTimeMillis();
	    float[] vel = {.01f, .01f};
	    bee.setVelocity2D(vel);
	    period = DrawPeriod.DURING;
	}    

	public void duringAnimation(BeeTile[] tiles) {
	    long time = System.currentTimeMillis() - globalRefTime;
	    float dt = ((float)(System.currentTimeMillis() - relativeRefTime))/1000f;
	    relativeRefTime = System.currentTimeMillis();
	    
	    float[] force = new float[2];
	    if(time < interval){
		force = getForce(mBoard.getTile(r));
		bee.setCenter2D(LATools.vSum(bee.getCenter2D(), LATools.vSProd(dt, bee.velocity)));
		bee.velocity = LATools.vSum(bee.velocity, LATools.vSProd(dt, force));
	    }
	    else {
		globalRefTime = System.currentTimeMillis();
		bee.velocity = LATools.vSProd(1/LATools.abs(bee.velocity),bee.velocity);
		r = ((int)(Math.random()*mBoard.getBoardHeight()*mBoard.getBoardWidth()));
	    }
	}

	public float[] getForce(BoardTile tile) {
		float[] force = {0.0f, 0.0f};

		force = LATools.vSProd(-0.7f,LATools.vDiff(bee.center, tile.center)); 
		force = LATools.vSum(force, LATools.vSProd(-0.8f,bee.velocity));

		return force;
	}

}

class BeeFixed extends BeeState<BeeTile> {

	public long globalRefTime = 0;    
	public long relativeRefTime = 0;
	public BeeTile bee;
	final float[] origin = {0.0f, 0.0f};
	boolean firstFlower = true;
	boolean flipped = true;
	int index = 0;
	int r = 0;
	int length;

	float[] pivot = {1,0,1};
	float[] fixedPos = {0.75f,-1.0f,0.0f};

	float[] fixedPosHidden = {-2.0f, 0.0f, 0.0f};

	public BeeFixed(Mood m) {
		setMood(m);
		length = (mBoard.getPathLength()-2)/2;
	}

	public BeeFixed(Mood m, int l) {
		setMood(m);
		length = l;
	}   

	public void enterAnimation(BeeTile[] tiles){
		globalRefTime = System.currentTimeMillis();
		relativeRefTime = System.currentTimeMillis();
		period = DrawPeriod.DURING;
	}

	public void duringAnimation(BeeTile[] tiles) {
	    bee = (BeeTile)tiles[0];
	    long time = System.currentTimeMillis() - globalRefTime;
	    float dt = ((float)(System.currentTimeMillis() - relativeRefTime))/1000f;
	    relativeRefTime = System.currentTimeMillis();
	    float[] force = new float[2];
	    switch (mood) {
	    case HAPPY:
		if(LATools.abs(bee.velocity) > .01f){
		    force = getForce(mBoard.getTile(r));
		    bee.setCenter2D(LATools.vSum(bee.getCenter2D(), LATools.vSProd(dt, bee.velocity)));
		    bee.velocity = LATools.vSum(bee.velocity, LATools.vSProd(dt, force));
		}
		else {
		    if (LATools.abs(bee.velocity) == 0.0f) {
			bee.velocity[0] = .00001f;
			index = 0;
		    }
		    bee.velocity = LATools.vSProd(.2f/LATools.abs(bee.velocity),bee.velocity);
		    if (firstFlower == true) {
			firstFlower = false;
		    }
		    else{
			mBoard.setTileRotate(r);
		    }
		    r = mBoard.getPathToArray(index);
		    index = ((index-1)%length + length)%length;
		}
		break;
		
	    case ASLEEP:
		if(LATools.abs(bee.velocity) > .0001f){
		    force = getForce(fixedPos);
		    bee.setCenter2D(LATools.vSum(bee.getCenter2D(), LATools.vSProd(dt, bee.velocity)));
		    bee.velocity = LATools.vSum(bee.velocity, LATools.vSProd(dt, force));
		} else {
		    float[] velocity = {0.0f, .00001f};
		    bee.velocity = velocity;
		}
		break;
		
	    case HIDDEN:
		if(LATools.abs(bee.velocity) > .0001f){
		    force = getForce(fixedPosHidden);
		    bee.setCenter2D(LATools.vSum(bee.getCenter2D(), LATools.vSProd(dt, bee.velocity)));
		    bee.velocity = LATools.vSum(bee.velocity, LATools.vSProd(dt, force));
		}
		break;
		
	    case DIZZY:
		force = getForceCentripetal(origin, bee.getCenter2D(), bee.velocity, 1.0f, 0.1f);
		bee.setCenter2D(LATools.vSum(bee.getCenter2D(), LATools.vSProd(dt, bee.velocity)));
		bee.velocity = LATools.vSum(bee.velocity, LATools.vSProd(dt, force));
		break;
	    }
	}

	public float[] getForceCentripetal(float[] dest, float[] initial, float[] velocity, float acc, float friction) {
		float[] force = {0.0f, 0.0f};
		force = LATools.vSProd(acc,LATools.vDiff(dest, initial)); 
		float[] dist = LATools.vDiff(dest, initial);
		float v = LATools.abs(velocity);
		float scale = v*v/(LATools.abs(dist));
		force = LATools.vSProd(scale, force);
		force = LATools.vSum(force, LATools.vSProd(friction,velocity));
		return force;
	}

	public float[] getForce(float[] in) {
		float[] force = {0.0f, 0.0f};
		force = LATools.vSProd(-2.0f,LATools.vDiff(bee.center, in)); 
		float abs = LATools.abs(force);
		if (abs > .1) {
			force[0] = force[0]/abs;
			force[1] = force[1]/abs;
			force = LATools.vSum(force, LATools.vSProd(-2.0f,bee.velocity));
		}
		else {
			force[0] = 0.0f;
			force[1] = 0.0f;
			force = LATools.vSum(force, LATools.vSProd(-7.0f,bee.velocity));
		}
		return force;
	}

	public float[] getForce(BoardTile tile) {
		float[] force = {0.0f, 0.0f};

		force = LATools.vSProd(-2.0f,LATools.vDiff(bee.center, tile.center)); 
		float abs = LATools.abs(force);
		if (abs > .1) {
			force[0] = force[0]/abs;
			force[1] = force[1]/abs;
			force = LATools.vSum(force, LATools.vSProd(-2.0f,bee.velocity));
		}
		else {
			force[0] = 0.0f;
			force[1] = 0.0f;
			force = LATools.vSum(force, LATools.vSProd(-7.0f,bee.velocity));
		}

		return force;
	}

}


}


