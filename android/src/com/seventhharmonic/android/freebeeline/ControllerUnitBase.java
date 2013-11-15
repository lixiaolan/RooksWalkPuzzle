package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.android.freebeeline.util.LATools;
import android.util.Log;

public abstract class ControllerUnitBase {
    
    public abstract void control();

    public void targetReached() {
	return;
    }
}
