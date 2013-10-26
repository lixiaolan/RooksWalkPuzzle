package com.seventhharmonic.android.freebeeline;
import java.util.HashMap;
import java.util.Map;


/*
 * Philosophy of the state: State's have a period. A beginning, middle and end. The initial period
 *  of every State is ENTER. It is the job of enterAnimation to change the period to DURING. I am not sure how
 *  to deal with exit yet, as this should be triggered by a global state change. However this class should NOT 
 *  be exposed to global state changes. It might make sense to only allow entry and during animations. This needs to be
 *  thought about. Actually I think this might be forced upon us... Think more about this.
 */

public class ErrorMap extends HashMap<Integer, ErrorBundle> {

    public ErrorBundle get(Integer k) {
	ErrorBundle v = super.get(k);
	if (v == null) {
	    ErrorBundle vv = new ErrorBundle();
	    super.put(k, vv);
	    return vv;
	}
	else {
	    return v;
	}
    }
}

