package com.seventhharmonic.android.freebeeline;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.test.*;

import com.seventhharmonic.android.freebeeline.util.IabException;
import com.seventhharmonic.android.freebeeline.util.Inventory;
import com.seventhharmonic.android.freebeeline.util.Purchase;
import com.seventhharmonic.android.freebeeline.util.IabResult;
import com.seventhharmonic.android.freebeeline.util.IabHelper;

public class Store {

	static final String TAG = "Store";
	// SKUs for our products: the premium upgrade (non-consumable) and gas (consumable)
	static final String SKU_HINT = "hint";
	static final int RC_REQUEST = 10001;
	// The helper object
	public IabHelper mHelper;
	public Inventory mInventory;

	public int PURCHASE_OK = 0;
	public int PURCHASE_FAILED = -1;

	int hintsAdded = 0;
	TextWidget hintWidget;
	
	Activity mContext;

	public Store(Activity c){
		this.mContext = c;
		initializeIab();
	}

	void initializeIab() {
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAocHERvmpvt+dCcMh2R8GnAS8scYLWLnPDC7KFw4qadzDw5iv7rPcgAzvGcwkPjN/nUHamJ/eHRvYhMJiekFGtOn/zFKTLOUU+JmTUHrQuvE7cQ8P30fej7GB4htm1h6FfOjJ9ZQRgyR78LMa9cMQnSY3BSxY3qhAPP4vmlj0ruTIPN7Selepc8ybP0RQtpyGSDfHAZ6v2B8Wnh23lqBg87JWyyvqD4bsIJeMr79WT7BD20dt3IsGKZ72I9XAH86S4CKb4TvaDqmWRU2qXmYq9QrqvJGNBdAwg3Wf4nAZfnVpeliF4y6ryq/lKvPCOcAsajREczSQGdNaLyYbRRAhHwIDAQAB";

		// Create the helper, passing it our context and the public key to verify signatures w
		mHelper = new IabHelper(mContext, base64EncodedPublicKey);

		// enable debug logging (for a production application, you should set this to false).
		mHelper.enableDebugLogging(true);
		Log.d(TAG, "Starting setup.");
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");

				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					complain("Problem setting up in-app billing: " + result);
					return;
				}

				// Have we been disposed of in the meantime? If so, quit.
				if (mHelper == null) return;
				// IAB is fully set up. Now, let's get an inventory of stuff we own.
				Log.d(TAG, "Setup successful. Querying inventory.");
				/*
				//The point of this next piece of code is to fix fuckups.
				ArrayList<String> moreSkus = new ArrayList<String>();
				moreSkus.add("android.test.purchase");
				try{
					mInventory = mHelper.queryInventory(false, moreSkus);
					Log.d(TAG, "Got inventory");
				} catch(IabException e){
					Log.d(TAG, "EXCEPTED "+e.getMessage());
				}
				mHelper.consumeAsync(mInventory.getPurchase("android.test.purchased"),mConsumeHintsFinishedListener);
				/**/
				
				/*
				 * Have completely commented out Security. There should be a newer version of the code
				 * which does the necessary verification of the security.
				 */
				mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
		
	}

	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			Log.d(TAG, "Query inventory finished.");
			if(inventory == null){
				Log.d(TAG, "Null inventory");
				return;
			}
			// Have we been disposed of in the meantime? If so, quit.
			mInventory = inventory;

			if (mHelper == null) return;
			// Is it a failure? - FIX HOW SECURITY IS BEING DONE - on static purchases, the signature could be null
			if (result.isFailure()) {
				complain("Failed to query inventory: " + result);
				return;
			}
			Log.d(TAG, "Query inventory was successful.");
			Log.d(TAG, "Initial inventory query finished; enabling main UI.");
		}
	};
	/************************************************************************/
	/*
	 * Call this method when you decide to buy 5 hints.
	 */
	public void onBuyFiveHints(TextWidget mHints) {
		Log.d(TAG, "Buy hints button clicked.");
		// launch the gas purchase UI flow.
		// We will be notified of completion via mPurchaseFinishedListener
		//setWaitScreen(true);
		Log.d(TAG, "Launching purchase flow for hints.");
		/* TODO: for security, generate your payload here for verification. See the comments on
		 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
		 *        an empty string, but on a production app you should carefully generate this. */
		String payload = "";
		hintWidget = mHints;
		mHelper.launchPurchaseFlow(mContext, "android.test.purchased", RC_REQUEST,
				mPurchaseFiveHintsFinishedListener, payload);
	}

	IabHelper.OnIabPurchaseFinishedListener mPurchaseFiveHintsFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
			try{
			// if we were disposed of in the meantime, quit.
				if (mHelper == null){
					return ;
				}

				if (result.isFailure()) {
					complain("Error purchasing: " + result);
					//setWaitScreen(false);
					return;
				}
			if (!verifyDeveloperPayload(purchase)) {
					complain("Error purchasing. Authenticity verification failed.");
					//setWaitScreen(false);
					return;
				} 

			//Consume this purchase immediately!!! Can change this in the future.
			mHelper.consumeAsync(purchase,mConsumeHintsFinishedListener);	
			
			//Fill DB
			GlobalApplication.getHintDB().open();
			GlobalApplication.getHintDB().addHints(50);
			hintWidget.setText(TextureManager.buildHint(GlobalApplication.getHintDB().getHints().getNum()));
			GlobalApplication.getHintDB().close();	
			Log.d(TAG,"In Store, how many hints did I get? hints: "+Long.toString(GlobalApplication.getHintDB().getHints().getNum()));
	    	Log.d("Board",Long.toString(GlobalApplication.getHintDB().getHints().getNum()));
			
			}catch(Exception e){
				//Should actually throw an exception here! This is a mess.
				Log.e(TAG, e.getMessage());
			}
		}
	};

	IabHelper.OnConsumeFinishedListener mConsumeHintsFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);
			// if we were disposed of in the meantime, quit.
			if (mHelper == null) return;
			if (result.isSuccess()) {
				// successfully consumed, so we apply the effects of the item in our
				// game world's logic, which in our case means filling the gas tank a bit
				Log.d(TAG, "Consumption successful. Provisioning.");
			}
			else {
				complain("Error while consuming: " + result);
			}
		}
	};

	/************************************************************************/

	/************************************************************************/
	/*
	 * Call this method when you decide to buy hints.
	 */
	public void onBuyUnlimitedHints(TextWidget mHints) {
		Log.d(TAG, "Buy hints button clicked.");
		// launch the gas purchase UI flow.
		// We will be notified of completion via mPurchaseFinishedListener
		//setWaitScreen(true);
		Log.d(TAG, "Launching purchase flow for hints.");
		/* TODO: for security, generate your payload here for verification. See the comments on
		 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
		 *        an empty string, but on a production app you should carefully generate this. */
		String payload = "";
		hintWidget = mHints;
		
		mHelper.launchPurchaseFlow(mContext, "android.test.purchased", RC_REQUEST,
				mPurchaseUnlimitedHintFinishedListener, payload);
	}

	IabHelper.OnIabPurchaseFinishedListener mPurchaseUnlimitedHintFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
			try{
			// if we were disposed of in the meantime, quit.
				if (mHelper == null){
					return ;
				}

				if (result.isFailure()) {
					complain("Error purchasing: " + result);
					//setWaitScreen(false);
					return;
				}
			if (!verifyDeveloperPayload(purchase)) {
					complain("Error purchasing. Authenticity verification failed.");
					//setWaitScreen(false);
					return;
				} 

			//Consume this purchase immediately!!! Can change this in the future.
			mHelper.consumeAsync(purchase,mConsumeHintsFinishedListener);	
			
			//Fill DB
			GlobalApplication.getHintDB().open();
			GlobalApplication.getHintDB().addHints(50);
			hintWidget.setText(TextureManager.buildHint(GlobalApplication.getHintDB().getHints().getNum()));
			GlobalApplication.getHintDB().close();	
			Log.d(TAG,"In Store, how many hints did I get? hints: "+Long.toString(GlobalApplication.getHintDB().getHints().getNum()));
	    	Log.d("Board",Long.toString(GlobalApplication.getHintDB().getHints().getNum()));
			
			}catch(Exception e){
				//Should actually throw an exception here! This is a mess.
				Log.e(TAG, e.getMessage());
			}
		}
	};


	/************************************************************************/
	
	
	void complain(String message) {
		Log.e(TAG, "**** InApp purchase Error: " + message);
	}

	boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();

		/*
		 * TODO: verify that the developer payload of the purchase is correct. It will be
		 * the same one that you sent when initiating the purchase.
		 *
		 * WARNING: Locally generating a random string when starting a purchase and
		 * verifying it here might seem like a good approach, but this will fail in the
		 * case where the user purchases an item on one device and then uses your app on
		 * a different device, because on the other device you will not have access to the
		 * random string you originally generated.
		 *
		 * So a good developer payload has these characteristics:
		 *
		 * 1. If two different users purchase an item, the payload is different between them,
		 *    so that one user's purchase can't be replayed to another user.
		 *
		 * 2. The payload must be such that you can verify it even when the app wasn't the
		 *    one who initiated the purchase flow (so that items purchased by the user on
		 *    one device work on other devices owned by the user).
		 *
		 * Using your own server to store and verify developer payloads across app
		 * installations is recommended.
		 */

		return true;
	}


}
