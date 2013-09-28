package com.example.android.opengl;

public class Bee extends Graphic<BeeTile, BeeState<BeeTile>>{
    public BeeTile bee;
    private Board mBoard;    
    
    public Bee(Board b) {
	float[] center= {0.0f,0.0f,0.0f};
	tiles = new BeeTile[1];
	tiles[0] = new BeeTile(center,0.2f);
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

    boolean flipped = true;
    int index = 0;
    int r = 0;
    int length;
    
    float[] pivot = {1,0,1};
    float[] fixedPos = {-.75f,-1.0f,0.0f};
    
    
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
	    if( abs(bee.velocity) > .01f ){
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
	    System.out.println("Got into the else");
	    //	    globalRefTime = System.currentTimeMillis();
	    if (abs(bee.velocity) == 0.0f) {
		bee.velocity[0] = .00001f;
	    }
	    bee.velocity = vSProd(.2f/abs(bee.velocity),bee.velocity);
	    r = 6*mBoard.path[index][0] + mBoard.path[index][1];
	    index = ((index-1)%length + length)%length;
	    //	    globalRefTime = System.currentTimeMillis();
	    // flipped = false;
	}
	break;

	case ASLEEP:
	    bee.center = fixedPos;
	    break;
	}
    }



    public float[] getForce(BoardTile tile) {
	float[] force = {0.0f, 0.0f};

	force = vSProd(-2.0f,vDiff(bee.center, tile.center)); 
	float abs = abs(force);
	if (abs > .1) {
	    force[0] = force[0]/abs;
	    force[1] = force[1]/abs;
	}
	else {
	    force[0] = 0.0f;
	    force[1] = 0.0f;
	}
	force = vSum(force, vSProd(-4.0f,bee.velocity));
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



	    // if (time < rotateInterval) {
	    // 	bee.setAngle(oldAngle + time/rotateInterval*(newAngle - oldAngle));
	    // }

	    // else if(time < flyInterval + rotateInterval){
	    // 	bee.center[0] = startX + (time - rotateInterval)/flyInterval*(dX);
	    // 	bee.center[1] = startY + (time - rotateInterval)/flyInterval*(dY);
	    // 	//} else if(time<interval+200f){
	    // 	// if(!flipped){
	    // 	//     mBoard.tiles[r].setPivot(pivot);
	    // 	//     mBoard.tiles[r].setRotate(true);
	    // 	//}
	    // }
	    // else {
	    // 	oldAngle = newAngle;
	    // 	startX = bee.center[0];
	    // 	startY = bee.center[1];
	    // 	dX = mBoard.tiles[r].center[0] - startX;
	    // 	dY = mBoard.tiles[r].center[1] - startY;

	    // 	if (dX > 0.0f) {
	    // 	    newAngle = 45.0f*((float)(Math.atan(dY/dX)/Math.atan(1)));
	    // 	}
	    // 	else if (dX < 0.0f)
	    // 	    newAngle = 180.0f + 45.0f*((float)(Math.atan(dY/dX)/Math.atan(1)));
	    // 	else {
	    // 	    if (dY > 0.0f) {
	    // 		newAngle = 90.0f;
	    // 	    }
	    // 	    else {
	    // 		newAngle = -90.0f;
	    // 	    }
	    // 	}
	    // 	newAngle = newAngle - 90.0f;
			
	    // 	r = 6*mBoard.path[index][0] + mBoard.path[index][1];
	    // 	index = ((index-1)%length + length)%length;
	    // 	flyInterval = 500f*(Math.abs(dX)+Math.abs(dY));
	    // 	flipped = false;
	    // 	refTime = System.currentTimeMillis();
	    // }
	    // break;
	// case ASLEEP:
	//     bee.center = fixedPos;
	//     break;
	// }
}


