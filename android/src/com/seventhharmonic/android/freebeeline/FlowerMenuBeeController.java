package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.android.freebeeline.util.LATools;
import android.util.Log;

public class FlowerMenuBeeController extends ControllerBase implements BeeInterface {
    public NewBee bee;
    private BeeFlowerMenuInterface mFlowerMenuInterface;
    private boolean beeTouchedBoolean = false;

    public FlowerMenuBeeController(BeeFlowerMenuInterface b) {
	mFlowerMenuInterface = b;
	setControllerUnit(new MainMenuController());
    }
    
    public void setBee(NewBee inBee) {
	bee = inBee;
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
    }
    
    public class MainMenuController extends ControllerUnitBase {

	public long globalRefTime;
	public long relativeRefTime;
	
	protected long time;
	protected float interval = 6000f;
	protected int r;
	
	@Override
	public void control() {
	    time = System.currentTimeMillis() - globalRefTime;
	    if(time < interval){
	    }
	    else {
		r = ((int)(Math.random()*mFlowerMenuInterface.getFlowerCount()));
		bee.setTarget(mFlowerMenuInterface.getTile(r).getCenter());
		globalRefTime = System.currentTimeMillis();
	    }
	}
    }
}
