package com.seventhharmonic.android.freebeeline;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.test.*;
import com.seventhharmonic.android.freebeeline.util.Inventory;
import com.seventhharmonic.android.freebeeline.util.Purchase;
import com.seventhharmonic.android.freebeeline.util.IabResult;
import com.seventhharmonic.android.freebeeline.util.IabHelper;

public class Store {

	static final String TAG = "ViewActivity";
	// SKUs for our products: the premium upgrade (non-consumable) and gas (consumable)
	static final String SKU_HINT = "hint";
	static final int RC_REQUEST = 10001;
	// The helper object
	public IabHelper mHelper;
	public Inventory mInventory;
	
	Activity mContext;

	public Store(Activity c){
		this.mContext = c;
		initializeIab();
	}

	void initializeIab() {
		String base64EncodedPublicKey = "CONSTRUCT_YOUR_KEY_AND_PLACE_IT_HERE";
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
				mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
	}

	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			Log.d(TAG, "Query inventory finished.");
			// Have we been disposed of in the meantime? If so, quit.
			if (mHelper == null) return;
			// Is it a failure?
			if (result.isFailure()) {
				complain("Failed to query inventory: " + result);
				return;
			}
			mInventory = inventory;
			Log.d(TAG, "Query inventory was successful.");
			Log.d(TAG, "Initial inventory query finished; enabling main UI.");
		}
	};
/************************************************************************/

	/*
	 * Call this method when you decide to buy hints.
	 */
	public void onBuyHints(View arg0) {
		Log.d(TAG, "Buy hints button clicked.");
		// launch the gas purchase UI flow.
		// We will be notified of completion via mPurchaseFinishedListener
		//setWaitScreen(true);
		Log.d(TAG, "Launching purchase flow for hints.");
		/* TODO: for security, generate your payload here for verification. See the comments on
		 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
		 *        an empty string, but on a production app you should carefully generate this. */
		String payload = "";
		mHelper.launchPurchaseFlow(mContext, "android.test.purchased", RC_REQUEST,
				mPurchaseHintFinishedListener, payload);
	}

	IabHelper.OnIabPurchaseFinishedListener mPurchaseHintFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
	        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
	            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
	            // if we were disposed of in the meantime, quit.
	            if (mHelper == null) return;
	            
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
	            
	            Log.d(TAG, "Purchase successful.");

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
