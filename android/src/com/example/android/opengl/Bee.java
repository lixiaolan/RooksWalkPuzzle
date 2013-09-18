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
	
	switch(s.state){
	case MAIN_MENU_OPENING: state = new BeeWander(mBoard, Mood.ASLEEP); break;
	case GAME_OPENING: state = new BeeFixed(mBoard, Mood.ASLEEP); break;
	}
    }
    
    
    public boolean touched(float[] pt) {
	if(pt[0]< bee.center[0]+0.25f && pt[0] > bee.center[0]-.25f 
	   && pt[1]< bee.center[1]+0.25f && pt[1] > bee.center[1]-.25f ){
	    return true;
	}
	return false;
    }    

    public void setMood(Mood m) {
	state.setMood(m);
    }
}

class BeeWander extends BeeState<BeeTile> {
    public long refTime;
    
    private float targetX = 0;
    private float targetY = 0;
    private float startX = 0;
    private float startY = 0;
    
    boolean flipped = true;
    int r = 0;
    float[] pivot = {1,0,1};
    
    public BeeWander(Board b, Mood m) {
	setBoard(b);
	setMood(m);
    }
    
    public void enterAnimation(BeeTile[] tiles){
	period = DrawPeriod.DURING;
    }    
    
    public void duringAnimation(BeeTile[] tiles) {
	BeeTile bee = (BeeTile)tiles[0];
	long time = System.currentTimeMillis() - refTime;
	float interval = 5000f;
	if(time < interval){
	    bee.center[0] = startX + time/interval*(targetX-startX);
	    bee.center[1] = startY + time/interval*(targetY-startY);
	} else if(time<interval+200f){
	    if(!flipped){
		mBoard.tiles[r].setPivot(pivot);
		mBoard.tiles[r].setRotate(true);
	    }
	}
	else {
	    startX = bee.center[0];
	    startY = bee.center[1];
	    refTime = System.currentTimeMillis();
	    r = ((int)(Math.random()*36));
	    targetX = mBoard.tiles[r].center[0];
	    targetY = mBoard.tiles[r].center[1];
	    flipped = false;
	}
    }
    
    public void exitAnimation(BeeTile[] tiles){
	
    }    
}

class BeeFixed extends BeeState<BeeTile> {
    
    public long refTime = 0;    
    private float startX = 0.0f;
    private float startY = 0.0f;

    private float dX = 0.0f;
    private float dY = 0.0f;
    

    private float oldAngle = 0.0f;
    private float newAngle = 0.0f;

    private float flyInterval = 0.001f;
    private float rotateInterval = 800.0f;

    boolean flipped = true;
    int index = 0;
    int r = 0;
    int length;
    
    float[] pivot = {1,0,1};
    float[] fixedPos = {-.75f,-1.0f,0.0f};
    
    
    public BeeFixed(Board b, Mood m) {
	setBoard(b);
	setMood(m);
    }   
    
    public void enterAnimation(BeeTile[] tiles){
	period = DrawPeriod.DURING;
    }
    
    public void duringAnimation(BeeTile[] tiles) {
	BeeTile bee = (BeeTile)tiles[0];
	long time = System.currentTimeMillis() - refTime;

	length = (mBoard.path.length-1)/2;

	switch (mood) {
	case HAPPY:
	    if (time < rotateInterval) {
		bee.setAngle(oldAngle + time/rotateInterval*(newAngle - oldAngle));
	    }

	    else if(time < flyInterval + rotateInterval){
		bee.center[0] = startX + (time - rotateInterval)/flyInterval*(dX);
		bee.center[1] = startY + (time - rotateInterval)/flyInterval*(dY);
		//} else if(time<interval+200f){
		// if(!flipped){
		//     mBoard.tiles[r].setPivot(pivot);
		//     mBoard.tiles[r].setRotate(true);
		//}
	    }
	    else {
		oldAngle = newAngle;
		startX = bee.center[0];
		startY = bee.center[1];
		dX = mBoard.tiles[r].center[0] - startX;
		dY = mBoard.tiles[r].center[1] - startY;

		if (dX > 0.0f) {
		    newAngle = 45.0f*((float)(Math.atan(dY/dX)/Math.atan(1)));
		}
		else if (dX < 0.0f)
		    newAngle = 180.0f + 45.0f*((float)(Math.atan(dY/dX)/Math.atan(1)));
		else {
		    if (dY > 0.0f) {
			newAngle = 90.0f;
		    }
		    else {
			newAngle = -90.0f;
		    }
		}
		newAngle = newAngle - 90.0f;
			
		r = 6*mBoard.path[index][0] + mBoard.path[index][1];
		index = ((index-1)%length + length)%length;
		flyInterval = 500f*(Math.abs(dX)+Math.abs(dY));
		flipped = false;
		refTime = System.currentTimeMillis();
	    }
	    break;
	case ASLEEP:
	    bee.center = fixedPos;
	    break;
	}
    }
    
    public void exitAnimation(BeeTile[] tiles){
	
    }
}
