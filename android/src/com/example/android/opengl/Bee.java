package com.example.android.opengl;

public class Bee extends Graphic<BeeTile, BeeState<BeeTile>>{
    public BeeTile bee;
    private Board mBoard;    
    
    public Bee(Board b) {
	float[] center= {0.0f,0.0f,0.0f};
	tiles = new BeeTile[1];
	tiles[0] = new BeeTile(center,0.13f);
	bee = tiles[0];
	mBoard = b;
	setState(GameState.MAIN_MENU_OPENING); 
    }
    
    public void setState(GameState s){	
	switch(s){
		case MAIN_MENU_OPENING: state = new BeeWander(mBoard, Mood.ASLEEP); break;
		case GAME_OPENING: state = new BeeFixed(mBoard, Mood.ASLEEP); break;
		default: break; 
	}
    }

    public void setState(GameState s, int l){	
	switch(s){
		case MAIN_MENU_OPENING: state = new BeeWander(mBoard, Mood.ASLEEP); break;
	case GAME_OPENING: state = new BeeFixed(mBoard, Mood.ASLEEP, l); break;
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
}

class BeeWander extends BeeState<BeeTile> {
    public long globalRefTime;
    public long relativeRefTime;

    BeeTile bee;

    float interval = 10000f;
    
    boolean flipped = true;
    int r = 0;
    float[] pivot = {1,0,1};
    
    public BeeWander(Board b, Mood m) {
	setBoard(b);
	setMood(m);
    }
    
    public void enterAnimation(BeeTile[] tiles){
	period = DrawPeriod.DURING;
	globalRefTime = System.currentTimeMillis();
	relativeRefTime = System.currentTimeMillis();
    }    
    
    public void duringAnimation(BeeTile[] tiles) {
	bee = (BeeTile)tiles[0];
	long time = System.currentTimeMillis() - globalRefTime;
	float dt = ((float)(System.currentTimeMillis() - relativeRefTime))/1000f;
	relativeRefTime = System.currentTimeMillis();

	float[] force = new float[2];
	if(time < interval){
	    force = getForce(mBoard.tiles[r]);
	    bee.setCenter2D(vSum(bee.getCenter2D(), vSProd(dt, bee.velocity)));
	    bee.velocity = vSum(bee.velocity, vSProd(dt, force));
	}
	// } else if(time<interval+200f){
	//     if(!flipped){
	// 	mBoard.tiles[r].setPivot(pivot);
	// 	mBoard.tiles[r].setRotate(true);
	//     }
	// }


	else {
	    globalRefTime = System.currentTimeMillis();
	    bee.velocity = vSProd(1/abs(bee.velocity),bee.velocity);
	    r = ((int)(Math.random()*36));
	    // flipped = false;
	}
    }

    public float[] getForce(BoardTile tile) {
	float[] force = {0.0f, 0.0f};

	force = vSProd(-0.7f,vDiff(bee.center, tile.center)); 
	force = vSum(force, vSProd(-1.2f,bee.velocity));

	return force;
    }
    
    public float[] vDiff(float[] left, float[] right) {
	float[] ret = new float[2];
	ret[0] = left[0] - right[0];
	ret[1] = left[1] - right[1];
	return ret;
    }
    
    public float[] vSum(float[] left, float[] right) {
	float[] ret = new float[2];
	for (int i = 0; i < left.length; i++)
	    ret[i] = left[i] + right[i];
	return ret;
    }
    
    public float[] vSProd(float scalar, float[] vec) {
	float[] ret = new float[2];
	for (int i = 0; i < vec.length; i++)
	    ret[i] = vec[i]*scalar;
	return ret;
    }
    
    public float abs(float[] vec) {
	float ret = 0.0f;
	for (int i = 0; i < vec.length; i++)
	    ret += vec[i]*vec[i];
	ret = (float)Math.sqrt(ret);
	return ret;
    }    
}

class BeeFixed extends BeeState<BeeTile> {
    
    public long globalRefTime = 0;    
    public long relativeRefTime = 0;
    public BeeTile bee;

    boolean firstFlower = true;
    boolean flipped = true;
    int index = 0;
    int r = 0;
    int length;
    
    float[] pivot = {1,0,1};
    float[] fixedPos = {0.75f,-1.0f,0.0f};
    
    float[] fixedPosHidden = {-2.0f, 0.0f, 0.0f};
    
    public BeeFixed(Board b, Mood m) {
	setBoard(b);
	setMood(m);
	length = (mBoard.path.length-2)/2;
	System.out.println("length: "+Integer.toString(length));
    }

    public BeeFixed(Board b, Mood m, int l) {
	setBoard(b);
	setMood(m);
	length = l;
	System.out.println("length: "+Integer.toString(length));
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
	    if(abs(bee.velocity) > .01f){
		force = getForce(mBoard.tiles[r]);
		bee.setCenter2D(vSum(bee.getCenter2D(), vSProd(dt, bee.velocity)));
		bee.velocity = vSum(bee.velocity, vSProd(dt, force));
	    }
	    else {
		if (abs(bee.velocity) == 0.0f) {
		    bee.velocity[0] = .00001f;
		}
		bee.velocity = vSProd(.2f/abs(bee.velocity),bee.velocity);
		if (firstFlower == true) {
		    firstFlower = false;
		}
		else{
		    mBoard.tiles[r].rotate = true;
		}
		r = 6*mBoard.path[index][0] + mBoard.path[index][1];
		index = ((index-1)%length + length)%length;
	    }
	    break;

	case ASLEEP:
	    if(abs(bee.velocity) > .0001f){
		force = getForce(fixedPos);
		bee.setCenter2D(vSum(bee.getCenter2D(), vSProd(dt, bee.velocity)));
		bee.velocity = vSum(bee.velocity, vSProd(dt, force));
	    } else {
	    	float[] velocity = {0.0f, .00001f};
	    	bee.velocity = velocity;
	    }
	    break;
	    
	case HIDDEN:
	    if(abs(bee.velocity) > .0001f){
		force = getForce(fixedPosHidden);
		bee.setCenter2D(vSum(bee.getCenter2D(), vSProd(dt, bee.velocity)));
		bee.velocity = vSum(bee.velocity, vSProd(dt, force));
	    }
	    break;
	}
    }

    public float[] getForce(float[] in) {
	float[] force = {0.0f, 0.0f};
	force = vSProd(-2.0f,vDiff(bee.center, in)); 
	float abs = abs(force);
	if (abs > .1) {
	    force[0] = force[0]/abs;
	    force[1] = force[1]/abs;
	    force = vSum(force, vSProd(-2.0f,bee.velocity));
	}
	else {
	    force[0] = 0.0f;
	    force[1] = 0.0f;
	    force = vSum(force, vSProd(-7.0f,bee.velocity));
	}
	return force;
    }


    public float[] getForce(BoardTile tile) {
	float[] force = {0.0f, 0.0f};

	force = vSProd(-2.0f,vDiff(bee.center, tile.center)); 
	float abs = abs(force);
	if (abs > .1) {
	    force[0] = force[0]/abs;
	    force[1] = force[1]/abs;
	    force = vSum(force, vSProd(-2.0f,bee.velocity));
	}
	else {
	    force[0] = 0.0f;
	    force[1] = 0.0f;
	    force = vSum(force, vSProd(-7.0f,bee.velocity));
	}

	return force;
    }
    
    public float[] vDiff(float[] left, float[] right) {
	float[] ret = new float[2];
	ret[0] = left[0] - right[0];
	ret[1] = left[1] - right[1];
	return ret;
    }
    
    public float[] vSum(float[] left, float[] right) {
	float[] ret = new float[2];
	for (int i = 0; i < left.length; i++)
	    ret[i] = left[i] + right[i];
	return ret;
    }
    
    public float[] vSProd(float scalar, float[] vec) {
	float[] ret = new float[2];
	for (int i = 0; i < vec.length; i++)
	    ret[i] = vec[i]*scalar;
	return ret;
    }
    
    public float abs(float[] vec) {
	float ret = 0.0f;
	for (int i = 0; i < vec.length; i++)
	    ret += vec[i]*vec[i];
	ret = (float)Math.sqrt(ret);
	return ret;
    }
}

