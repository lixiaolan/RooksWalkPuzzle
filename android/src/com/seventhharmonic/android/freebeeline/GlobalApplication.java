package com.seventhharmonic.android.freebeeline;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Logger.LogLevel;
import com.seventhharmonic.android.freebeeline.analytics.AnalyticsServer;
import com.seventhharmonic.android.freebeeline.db.*;
import com.seventhharmonic.android.freebeeline.graphics.Geometry;
import com.seventhharmonic.android.freebeeline.graphics.TextureBridge;
import com.seventhharmonic.com.freebeeline.levelresources.*;

import android.app.Application;
import android.content.Context;


/*
 * An advanced Google Analytics implementation may be initialized
 * in a subclass of Application. Note that this example assumes data
 * only needs to be sent to a single Google Analytics property ID.
 */
public class GlobalApplication extends Application {
    
	
    private static Context context;
    private static LevelPackProvider mLPP;
    private static Geometry geo;
    private static TextureBridge mTextureBridge;

    private static PuzzleDataSource mDB;
    private static HintsDataSource hDB;
    private static PurchasedDataSource PDS;
    private static MySQLiteHelper MSQLH;
    private static MyMusic myMusic;
    private static AnalyticsServer mASS;
    /*
     * Google Analytics configuration values.
     */
    
    // Placeholder property ID.
//    private static final String GA_PROPERTY_ID = "UA-44910433-2";
    
    // Dispatch period in seconds.
//    private static final int GA_DISPATCH_PERIOD = 5;
    
    // Prevent hits from being sent to reports, i.e. during testing.
//    private static final boolean GA_IS_DRY_RUN = false;
    
    // Key used to store a user's tracking preferences in SharedPreferences.
//    private static final String TRACKING_PREF_KEY = "trackingPreference";    
    
    /*
     * Method to handle basic Google Analytics initialization. This call will not
     * block as all Google Analytics work occurs off the main thread.
     */
    
    private void initializeGa() {
	context = getApplicationContext();
	mASS = new AnalyticsServer(this);
	
	geo = new Geometry();
	mTextureBridge = new TextureBridge();
	mLPP = new SAXLevelPackProvider();
	MSQLH = new MySQLiteHelper(this);
	
	mDB = new PuzzleDataSource(this, MSQLH);
	mDB.open();
	hDB = new HintsDataSource(this, MSQLH);
	hDB.open();
	PDS = new PurchasedDataSource(this, MSQLH);
	PDS.open();
	
	myMusic = new MyMusic(context); 
    }
    
    @Override
    public void onCreate() {
	super.onCreate();
	initializeGa();
    }
    
    
    /*
     * Returns the Google Analytics tracker.
     */
   /* public static EasyTracker getGaTracker() {
      return mTracker; 
    }

    public static GoogleAnalytics getGaInstance() {
      return mGa;
    }
    */

    public static AnalyticsServer getAnalytics() {
    	return mASS;
        }
        
    
    public static LevelPackProvider getLevelPackProvider() {
	return mLPP;
    }
    
    public static Geometry getGeometry() {
	return geo;
    }
    
    
    public static Context getContext(){
	return context;
    }
    
    public static PuzzleDataSource getPuzzleDB(){
	return mDB;
    }

    public static HintsDataSource getHintDB(){
	return hDB;
    }

    public static PurchasedDataSource getPurchasedDB(){
	return PDS;
    }

    public static MyMusic getMyMusic(){
	return myMusic;
    }

    
    public static TextureBridge getTextureBridge(){
	return mTextureBridge;
    }    
}
