package com.seventhharmonic.android.freebeeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.seventhharmonic.android.freebeeline.db.PurchasedDataSource;
import com.seventhharmonic.android.freebeeline.graphics.TextureManager;
import com.seventhharmonic.android.freebeeline.util.IabException;
import com.seventhharmonic.android.freebeeline.util.Inventory;
import com.seventhharmonic.android.freebeeline.util.Purchase;
import com.seventhharmonic.android.freebeeline.util.IabResult;
import com.seventhharmonic.android.freebeeline.util.IabHelper;
import com.seventhharmonic.android.freebeeline.util.SkuDetails;
import com.seventhharmonic.com.freebeeline.levelresources.LevelPack;

public class Store {

	static final String TAG = "Store";

	static final int RC_REQUEST = 10001;

	public IabHelper mHelper;
	public Inventory mInventory = null;
	PurchasedDataSource PDS;

	Model mModel;
	List<String> purchasables;
	HashMap<String, Integer> levelPackToChapterLimit;
	
	public int PURCHASE_OK = 0;
	public int PURCHASE_FAILED = -1;

	int hintsAdded = 0;
	TextBox hintWidget;

	Activity mContext;

	public Store(Activity c, Model mModel){
		this.mContext = c;
		this.mModel = mModel;
		PDS = GlobalApplication.getPurchasedDB();
		purchasables = new ArrayList<String>(Arrays.asList(
				new String[]{"android.test.purchased","levelPack1","levelPack2"}));
		levelPackToChapterLimit = new HashMap<String, Integer>();
		levelPackToChapterLimit.put("android.test.purchased", Integer.valueOf(1));
		levelPackToChapterLimit.put("levelPack1", Integer.valueOf(1));
		levelPackToChapterLimit.put("levelPack2", Integer.valueOf(3));
		
		initializeIab();
		
	}

	
	void initializeIab() {
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAocHERvmpvt+dCcMh2R8GnAS8scYLWLnPDC7KFw4qadzDw5iv7rPcgAzvGcwkPjN/nUHamJ/eHRvYhMJiekFGtOn/zFKTLOUU+JmTUHrQuvE7cQ8P30fej7GB4htm1h6FfOjJ9ZQRgyR78LMa9cMQnSY3BSxY3qhAPP4vmlj0ruTIPN7Selepc8ybP0RQtpyGSDfHAZ6v2B8Wnh23lqBg87JWyyvqD4bsIJeMr79WT7BD20dt3IsGKZ72I9XAH86S4CKb4TvaDqmWRU2qXmYq9QrqvJGNBdAwg3Wf4nAZfnVpeliF4y6ryq/lKvPCOcAsajREczSQGdNaLyYbRRAhHwIDAQAB";

		// Create the helper, passing it our context and the public key to verify signatures w
		mHelper = new IabHelper(mContext, base64EncodedPublicKey);

		//TODO: enable debug logging (for a production application, you should set this to false).
		mHelper.enableDebugLogging(true);
		Log.d(TAG, "Starting setup.");
	
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");

				if (!result.isSuccess()) {
					complain("Problem setting up in-app billing: " + result);
					return;
				}

				// Have we been disposed of in the meantime? If so, quit.
				if (mHelper == null) return;
				// IAB is fully set up. Now, let's get an inventory of stuff we own.

				Log.d(TAG, "Setup successful. Querying inventory.");
				
				/*TODO:
				 * Have completely commented out Security. There should be a newer version of the code
				 * which does the necessary verification of the security.
				 */
				mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
	}

	/**Pass it a standard android static response and it will consume it. This is for testing purposes only!
	 * @param sku
	 */
	private void consumeStaticResponse(String sku) throws Exception{
		//The point of this next piece of code is to fix fuckups.
		ArrayList<String> moreSkus = new ArrayList<String>();
		moreSkus.add(sku);
		try{
			mHelper.consumeAsync(mInventory.getPurchase(sku),mConsumeHintsFinishedListener);
			//mInventory = mHelper.queryInventory(false, moreSkus);
			Log.d(TAG, "Got inventory");
		} catch(Exception e){
			Log.d(TAG, "EXCEPTED "+e.getMessage());
			throw e;
		}
		
		
	}

	
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
			Log.d(TAG, "Query inventory finished.");
			if(inventory == null){
				Log.d(TAG, "Null inventory");
				return;
			}
			mInventory = inventory;

			if (mHelper == null) return;
		
			//TODO: Is it a failure? - FIX HOW SECURITY IS BEING DONE - on static purchases, the signature could be null
			if (result.isFailure()) {
				complain("Failed to query inventory: " + result);
				return;
			}
			
			/*try{
				consumeStaticResponse("android.test.purchased");
			} catch(Exception e){
				Log.d(TAG, "Failed to consume the test purchase");
				Log.d(TAG, e.getLocalizedMessage());
			}*/
			
			Log.d(TAG, "Query inventory was successful.");
			Log.d(TAG, "Initial inventory query finished; enabling main UI.");

		}
	};

	/***********************************************************************/
	/*
	 * Code run when you decide to buy 5 hints.
	 */
	public void onBuyFiveHints(TextBox mHints) {
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
				GlobalApplication.getHintDB().addHints(5);
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
	 * Code run when you decide to buy infinity hints.
	 */
	String sku = "test5";

	public boolean hasUnlimitedHints(){
		return false;
		/*
		if(mInventory == null){
			return PDS.getPurchased(sku);
		}
		
		//TODO: Compare to how mainActivity in trivialDrive is doing this more safely. You should really verify the purchase here. 
		if(mInventory.hasPurchase(sku)){
			return true;
		} else {
			return false;
		}
		*/
	}

	public void onBuyUnlimitedHints(TextBox mHints) {
		Log.d(TAG, "Buy unlimited hints button clicked.");
		// launch the  purchase UI flow.
		// We will be notified of completion via mPurchaseFinishedListener
		//setWaitScreen(true);
		Log.d(TAG, "Launching purchase flow for unlimited hints.");
		/* TODO: for security, generate your payload here for verification. See the comments on
		 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
		 *        an empty string, but on a production app you should carefully generate this. */
		String payload = "";
		hintWidget = mHints;

		mHelper.launchPurchaseFlow(mContext, sku, RC_REQUEST,
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

				//Note that we should NOT CONSUME since you get unlimited hints.
				//Need to update the inventory object.
				mHelper.queryInventoryAsync(mGotInventoryListener);

				//TODO: Need code here to open the DB and set the fact that we have bought unlimited hints.
				PDS.open();
				PDS.setPurchased(sku, true);
				PDS.close();
				//Update the board test widget.
				hintWidget.setText(TextureManager.HIVE);



			}catch(Exception e){
				//Should actually throw an exception here! This is a mess.
				Log.e(TAG, e.getMessage());
			}
		}
	};

	/************************************************************************/

	/***********************************************************************/
	//Code for level pack purchase
	public boolean hasLevelPack(LevelPack lp){
		String id  = lp.getId();
		if(purchasables.contains(id)){
			if(mInventory == null){
				Log.d(TAG, "Found a null inventory");
				return PDS.getPurchased(id);
			}
			
			//TODO: Compare to how mainActivity is doing this more safely. You should really verify the purchase here. 
			if(mInventory.hasPurchase(id)){
				return true;
			} else {
				return false;
			}
		}
		Log.d(TAG, "Didn't contain id "+id);
		return true;
	}

	public int getLevelPackChapterLimit(LevelPack lp){
		String id  = lp.getId();
		if(levelPackToChapterLimit.containsKey(id)){
			return levelPackToChapterLimit.get(id);
		} 
		
		//If we don't contain this, assume it is free.
		return lp.getNumberOfChapters();
		
	}
	
	public void onBuyLevelPack(LevelPack lp) {
		String id  = lp.getId();
		Log.d(TAG, "Buy Level Pack button clicked.");
		// launch the  purchase UI flow.
		// We will be notified of completion via mPurchaseFinishedListener
		setWaitScreen(true);
		Log.d(TAG, "Launching purchase flow for level pack.");
		/* TODO: for security, generate your payload here for verification. See the comments on
		 *        verifyDeveloperPayload() for more info. Since this is a SAMPLE, we just use
		 *        an empty string, but on a production app you should carefully generate this. */
		String payload = "";

		mHelper.launchPurchaseFlow(mContext, "android.test.purchased", RC_REQUEST,
				mPurchaseLevelPackFinishedListener, payload);
		
	}

	IabHelper.OnIabPurchaseFinishedListener mPurchaseLevelPackFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
			try{
				// if we were disposed of in the meantime, quit.
				if (mHelper == null){
					return ;
				}

				if (result.isFailure()) {
					complain("Error purchasing: " + result);
					setWaitScreen(false);
					return;
				}
				if (!verifyDeveloperPayload(purchase)) {
					complain("Error purchasing. Authenticity verification failed.");
					setWaitScreen(false);
					return;
				} 

				//Note that we should NOT CONSUME since you get unlimited hints.
				//Need to update the inventory object.
				mInventory.addPurchase(purchase);

				//TODO: Need code here to open the DB and set the fact that we have bought the LevelPack.
				PDS.open();
				PDS.setPurchased(purchase.getSku(), true);
				PDS.close();

				//Update the board test widget.
				mModel.setModelToChapterSelect();
				setWaitScreen(false);
			}catch(Exception e){
				//Should actually throw an exception here! This is a mess.
				Log.e(TAG, e.getMessage());
			}
		}
	};	
	
	/***********************************************************************/
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

	void setWaitScreen(boolean set) {
	        mContext.findViewById(R.id.surface_view).setVisibility(set ? View.GONE : View.VISIBLE);
	        mContext.findViewById(R.id.wait_view).setVisibility(set ? View.VISIBLE : View.GONE);
	}

	
}



































/*List<String> moreSkus = new ArrayList<String>();
moreSkus.add("test1");
moreSkus.add("hints5");
mHelper.queryInventoryAsync(true, moreSkus, mGotInventoryListener);
*/
//Test code to see if inventory is communicating with the server.
/*
Log.d(TAG, "What can I purchase?");
SkuDetails p = inventory.getSkuDetails("test1");
Log.d(TAG, p.getSku());
Log.d(TAG, p.getTitle());
Log.d(TAG, p.getType());
Log.d(TAG, p.getDescription());
Log.d(TAG, p.getPrice());

p = inventory.getSkuDetails("hints5");
Log.d(TAG,p.getSku());
Log.d(TAG, p.getTitle());
Log.d(TAG, p.getType());
Log.d(TAG, p.getDescription());
Log.d(TAG, p.getPrice());
*/ 
