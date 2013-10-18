package com.seventhharmonic.android.freebeeline;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Logger.LogLevel;

import android.app.Application;


/*
 * An advanced Google Analytics implementation may be initialized
 * in a subclass of Application. Note that this example assumes data
 * only needs to be sent to a single Google Analytics property ID.
 */
public class MyTracker extends Application {

  private static GoogleAnalytics mGa;
  private static EasyTracker mTracker;

  /*
   * Google Analytics configuration values.
   */
  // Placeholder property ID.
  private static final String GA_PROPERTY_ID = "UA-44910433-1";

  // Dispatch period in seconds.
  private static final int GA_DISPATCH_PERIOD = 5;

  // Prevent hits from being sent to reports, i.e. during testing.
  private static final boolean GA_IS_DRY_RUN = false;

  // GA Logger verbosity.
  private static final LogLevel GA_LOG_VERBOSITY = LogLevel.INFO;

  // Key used to store a user's tracking preferences in SharedPreferences.
  private static final String TRACKING_PREF_KEY = "trackingPreference";


  /*
   * Method to handle basic Google Analytics initialization. This call will not
   * block as all Google Analytics work occurs off the main thread.
   */
  private void initializeGa() {
    mGa = GoogleAnalytics.getInstance(this);
    mTracker = EasyTracker.getInstance(this);//mGa.getTracker(GA_PROPERTY_ID);

  }

  @Override
  public void onCreate() {
    super.onCreate();
    initializeGa();
  }

  /*
   * Returns the Google Analytics tracker.
   */
  public static EasyTracker getGaTracker() {
    return mTracker;
  }

  /*
   * Returns the Google Analytics instance.
   */
  public static GoogleAnalytics getGaInstance() {
    return mGa;
  }
}