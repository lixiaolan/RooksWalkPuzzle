package com.seventhharmonic.android.freebeeline;


public class BoardBeeController extends ControllerBase implements BeeInterface {
    public NewBee bee;
    private BeeBoardInterface mBoardInterface;
    private boolean beeTouchedBoolean = false;

    public BoardBeeController(BeeBoardInterface b) {
	mBoardInterface = b;
    }
    
    public void setMood(Mood m) {
	switch(m) {
	case ASLEEP: setControllerUnit(new StayStillController()); break;
	case HAPPY: setControllerUnit(new FollowPuzzlePathController()); break;
	default: break;
	}
    }


    public void setBee(NewBee inBee) {
	//Perhaps this needs to be in the order so that we don't fuck
	//up the bee that the old controller is trying to work with?
	bee = inBee;
	setControllerUnit(new StayStillController());
    }

    public int touched(float[] pt) {
	bee.touched(pt);
	return (beeTouchedBoolean) ? 1 : 0;
    }

    @Override
    public void beeTouched(boolean b) {
	beeTouchedBoolean = b;
    }

    @Override
    public void targetReached() {
	controllerUnit.targetReached();
    }


    //This may be a bad idea..............
    
    // public class visitHintTileController extends ControllerUnitBase {
    // 	private float[] fixedPos = {0.8f,-1.0f,0.0f};
	
    // 	public visitHintTileController() {
    // 	    bee.setMood(Mood.FAST);
    // 	}

    // 	@Override
    // }

    public class StayStillController extends ControllerUnitBase {

	private float[] fixedPos = {0.8f,-1.0f,0.0f};
	
	public StayStillController() {
	    bee.setMood(Mood.FAST);
	}
	
	@Override
	public void control() {
	    bee.setTarget(mBoardInterface.getBeeBoxCenter());
	}
	
	@Override
	public void targetReached() {
	    bee.setCenter(mBoardInterface.getBeeBoxCenter());
	    bee.setMood(Mood.ASLEEP);
	    //input method to make the bee face the right way!
	}
	
    }

    public class FollowPuzzlePathController extends ControllerUnitBase {
	
	private int index = 0;
	private int r;
	private int length;
	
	private BoardTile t;
	private float[] f;

	public FollowPuzzlePathController() {
	    r = mBoardInterface.getPathToArray(index);
	    length = mBoardInterface.getPathLength()-1;
	    bee.setMood(Mood.FAST);
	}
	
	@Override
	public void control() {
	    try {
		t = mBoardInterface.getTile(r);
		f = t.getCenter();
		bee.setTarget(f);
	    }
	    catch (NullPointerException e) {
		System.out.println("Null Pointer Exception Caught in BoardBeeController::controll()");
	    }
	}
	
	@Override
	public void targetReached() {
	    r = mBoardInterface.getPathToArray(index);
	    index = ((index-1)%length + length)%length;
	}
    }


}
