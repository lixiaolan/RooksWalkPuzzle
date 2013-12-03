package com.seventhharmonic.android.freebeeline.analytics;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Logger.LogLevel;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.StandardExceptionParser;
import com.seventhharmonic.android.freebeeline.GlobalApplication;

import android.app.Application;
import android.content.Context;

public class AnalyticsServer {
	private Context context; 
	private static GoogleAnalytics mGa;
	private static EasyTracker mTracker;
	
	
	public AnalyticsServer(GlobalApplication a) {    
		mGa = GoogleAnalytics.getInstance(a);
		//hello world 2
		//mGa.setDryRun(true);
		//GoogleAnalytics.getInstance(a).getLogger().setLogLevel(LogLevel.VERBOSE);
		mGa.setAppOptOut(true);
		mTracker = EasyTracker.getInstance(a);//mGa.getTracker(GA_PROPERTY_ID);
		context = a.getContext();
	}
	
	//Timing
	/**
	 * @param id Id of Puzzle_Par
	 * @param time Time to completion
	 * @param moves Moves
	 */
	
	public void sendPuzzleFirstTimeCompleteTiming(long id, int par, long time, int moves) {
		mTracker.send(MapBuilder
			      .createTiming("puzzles_first_run",    // Timing category (required)
			                    time,       // Timing interval in milliseconds (required)
			                    Long.toString(id)+"_"+Integer.toString(par),  // Timing name
			                    Integer.toString(moves))           // Timing label
			      .build()
			  );
	}
	
	
	/**
	 * @param id Id of Puzzle_Par
	 * @param time Time to completion
	 * @param moves Moves
	 */
	public void sendPuzzleReplayedTiming(long id, int par, long time, int moves){
		mTracker.send(MapBuilder
			      .createTiming("puzzles_replay",    // Timing category (required)
			                    time,       // Timing interval in milliseconds (required)
			                    Long.toString(id)+"_"+Integer.toString(par),  // Timing name
			                    Integer.toString(moves))           // Timing label
			      .build()
			  );	
	}
	
	
	//Events
	/** Puzzles Showing on ChapterDisplay
	 * Category: art_event
	 * Action: button_press
	 * Label: toggle_puzzle_view
	 * Value: "hide" or "show"
	 * @param boolean: true is show, false is hide.
	 */
	public void sendPuzzleShow(boolean show) {
		mTracker.send(MapBuilder
			      .createEvent("art_event",     // Event category (required)
			                   "button_press",  // Event action (required)
			                   "toggle_puzzle_view",   // Event label
			                   null)            // Event value
			      .build()
			  );
	}
	
	
	/** Toggle the Sound
	 * Category: art_event
	 * Action: button_press
	 * Label: toggle_music
	 * Value: "mute" or "liberate"
	 * @param boolean: true is mute, false is liberate.
	 */
	public void sendMuteSound(boolean mute){
		mTracker.send(MapBuilder
			      .createEvent("art_event",     // Event category (required)
			                   "button_press",  // Event action (required)
			                   "toggle_music",   // Event label
			                   null)            // Event value
			      .build()
			  );
	}
	
	
	/** Toggle the Lines
	 * Category: ui_event
	 * Action: button_press
	 * Label: toggle_lines
	 * Value: "show" or "hide"
	 * @param boolean: true is show, false is hide.
	 */
	public void sendToggleLines(boolean show) {
		mTracker.send(MapBuilder
			      .createEvent("ui_event",     // Event category (required)
			                   "button_press",  // Event action (required)
			                   "toggle_lines",   // Event label
			                   null)            // Event value
			      .build()
			  );
	}
	
	
	/** Toggle the Errors
	 * Category: ui_event
	 * Action: button_press
	 * Label: toggle_errors
	 * Value: "show" or "hide"
	 * @param boolean: true is show, false is hide.
	 */
	public void sendToggleErrors(boolean show){
		mTracker.send(MapBuilder
			      .createEvent("ui_event",     // Event category (required)
			                   "button_press",  // Event action (required)
			                   "toggle_errors",   // Event label
			                   null)            // Event value
			      .build()
			  );
	}
	
	
	/**
	 * Category: gameplay_event
	 * Action: use_hint
	 * Label: puzzleId
	 * Value: hints
	 * @param hints Number of hints the user had. I.e., if people had infinite hints, did they use more?
	 * @param puzzleID the id of the puzzle on which the hint was used. 
	 */
	public void sendUsedHint(long puzzleID, long hintNum) {
			mTracker.send(MapBuilder
				      .createEvent("gameplay_event",     // Event category (required)
				                   "use hint",  // Event action (required)
				                   Long.toString(puzzleID),   // Event label
				                   null)            // Event value
				      .build()
				  );	
	}
	
	/**
	 * Category: store_error
	 * Action: query
	 * Label: error
	 * Value: null
	 * @param error Custom error message 
	 */
	public void sendStoreError(String error) {
			mTracker.send(MapBuilder
				      .createEvent("store_error",     // Event category (required)
				                   "query",  // Event action (required)
				                   error,   // Event label
				                   null)            // Event value
				      .build()
				  );	
	}
	
	//Campaigns:
	/**
	 * It appears that we just add a few things to the android xml file and then take care
	 * to pre-build the links so that they tell google play where they came from... 
	 */
	
	//Exceptions:
	/**
	 * Note that the uncaught exceptions will be reported already as long as we add the correct
	 * thing to the xml manifest (check th site).
	 * @param e Exception to be reported
	 */
	public void sendCaughtException(Exception e){
        mTracker.send(MapBuilder
        		.createException(new StandardExceptionParser(context, null)// Context and optional collection of package names
                                                                            // to be used in reporting the exception.
                .getDescription(Thread.currentThread().getName(),           // The name of the thread on which the exception occurred.
                                       e),                                  // The exception.
                       false)                                              // False indicates a fatal exception
                       .build()
        		);
	}
	
	//Screens
	/**
	 * The screen names sent should be in the list below:
	 * 
	 * DONE - main_menu
	 * DONE - main_tutorial
	 * DONE - main_options
	 * DONE - main_about
	 * DONE - main_gear
	 * DONE - level_pack_select
	 * DONE - chapter_select
	 * DONE - puzzle_begin
	 * puzzle_tutorial
	 * DONE - puzzle_end
	 * DONE - chapter_end
	 * 
	 * @param screen This is the current screen:
	 */
	public void sendScreen(String screen) {
		mTracker.send(MapBuilder
				.createAppView()
				.set(Fields.SCREEN_NAME, screen)
				.build()
				);
	}
	
	//Ecommerce
	/**
	 * item should be on of:
	 * 
	 * hints5
	 * hints20
	 * hintsunlimited
	 * levelpack1
	 * 
	 * @param item
	 */
	public void sendPurchase(String item) {
		 	String name;
		 	String SKU = item;
		 	String productCat;
		 	double price;
		 	long quantity = 1;
		 	
		 	
			if (item.equals("hints5")) {
		 		name = "Five Hints";
		 		productCat = "hints";
		 		price = .99d;
		 	}
		 	else if (item.equals("hints20")) {
		 		name = "Twenty Hints";
		 		productCat = "hints";
		 		price = 1.99d;
		 	}
		 	else if (item.equals("hintsunlimited")){
		 		name = "Unlimited Hints";
		 		productCat = "hints";
		 		price = 4.99d;
		 	}
		 	else if(item.equals("levelpack1")){
		 		name = "Level Pack One";
		 		productCat = "levelPacks";
		 		price = .99d;		 		
		 	} else {
		 			return;
		 	}
		
			//mTracker.send(MapBuilder
			//      .createTransaction(item,       // (String) Transaction ID
			//                         "In-app Store",   // (String) Affiliation
			//                         2.16d,            // (Double) Order revenue
			//                         0.17d,            // (Double) Tax
			//                         0.0d,             // (Double) Shipping
			//                         "USD")            // (String) Currency code
			//      .build()
			//);

			  mTracker.send(MapBuilder
			      .createItem(item,               // (String) Transaction ID
			                  name,               // (String) Product name
			                  item,                  // (String) Product SKU
			                  productCat,        // (String) Product category
			                  price,                    // (Double) Product price
			                  1L,                       // (Long) Product quantity
			                  "USD")                    // (String) Currency code
			      .build()
			  );
	}
}

