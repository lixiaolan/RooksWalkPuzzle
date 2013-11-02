package com.seventhharmonic.android.freebeeline.db;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;


public class SQLPurchased {    
    private Map<String, Boolean> mSkuMap = new HashMap<String, Boolean>();

    public SQLPurchased(Map<String, Boolean> in) {
	mSkuMap = in;
    }
    
    public Map<String, Boolean> getSkuMap() {
	return mSkuMap;
    }
} 
