package com.seventhharmonic.android.freebeeline;

import com.seventhharmonic.android.freebeeline.util.LATools;
import android.util.Log;

public class ControllerBase {
    
    protected ControllerUnitBase controllerUnit;

    public void setControllerUnit(ControllerUnitBase CUB) {
	controllerUnit = CUB;
    }

    public void control() {
	controllerUnit.control();
    }

}
