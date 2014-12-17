/* Copyright (c) 2010-2014 Pierre LEVY androidsoft.org
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.androidsoft.games.memory.kids.ui;

import org.androidsoft.games.memory.kids.PreferencesService;
import org.androidsoft.games.memory.kids.google.iap.IabHelper;
import org.androidsoft.games.memory.kids.google.iap.IabResult;
import org.androidsoft.games.memory.kids.google.iap.Inventory;
import org.androidsoft.games.memory.kids.google.iap.Purchase;
import org.androidsoft.utils.ui.BasicActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bhulok.sdk.android.Constants;
import com.fairket.app.memory.kids.R;
import com.fairket.sdk.android.FairketApiClient;
import com.fairket.sdk.android.FairketAppTimeHelper;
import com.fairket.sdk.android.FairketResult;

/**
 * 
 * @author pierre
 */
public class PreferencesActivity extends BasicActivity implements
		OnClickListener {

	private String TAG = "PrefrencesActivity";

	// (arbitrary) request code for the purchase flow
	static final int RC_REQUEST = 10001;

	private TextView mTvHiScore;
	private Button mButtonResetHiScore;
	private Button mButtonSupport;
	private CompoundButton mCbSoundEnabled;
	private Spinner mSpinner;
	private int mIconSet;
	private FairketApiClient mFaiirketApiClient;

	// Does the user have the premium upgrade?
	boolean mIsPremium = false;
	private IabHelper mHelper;

	private Button mButtonPaidOpitons;
	static final String SKU_PREMIUM = "premium";

	/**
	 * {@inheritDoc }
	 */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.preferences);

		mTvHiScore = (TextView) findViewById(R.id.hiscore);
		updateHiScore();

		mButtonResetHiScore = (Button) findViewById(R.id.button_reset_hiscore);
		mButtonResetHiScore.setOnClickListener(this);

		mButtonPaidOpitons = (Button) findViewById(R.id.button_premium);
		mButtonPaidOpitons.setOnClickListener(this);

		mSpinner = (Spinner) findViewById(R.id.spinner_theme);

		mIconSet = PreferencesService.instance().getIconsSet();
		System.out.println("Iconset: " + mIconSet);

		mCbSoundEnabled = (CompoundButton) findViewById(R.id.checkbox_sound);
		mCbSoundEnabled.setOnClickListener(this);
		mCbSoundEnabled.setChecked(PreferencesService.instance()
				.isSoundEnabled());

		mButtonSupport = (Button) findViewById(R.id.button_support);
		mButtonSupport.setOnClickListener(this);

		mFaiirketApiClient = FairketAppTimeHelper.onCreate(this,
				MainActivity.FAIRKET_APP_PUBLIC_KEY,
				MainActivity.FAIRKET_LOG_TAG);

		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhGMzQbOH/3E2cxJuEX/iSgH5wTZHPMAd78vuEr5f9n1pazk/zyeG7db/+dTWUenzVvfTXLKcTpW0aLInI4iVN9TG2HSAJiW96kcmVzdzL53XQeh5xFTFZqMYptPxoJa/Te///BFpdiLLemDgozzD+W95eazeJsqsZG734xpa+ZqhBGaM8yJIBZSiDlePhRhlU+c7gni3DUL9EqS27UbVZ6TuSaD/UMfpkPO1Ed23GCw4VqWwU8EWerql4QWDlaOvAi4pzRsoBIO9CsZhBNiST5LgyJi2XhorpmqF8/BESpICNjdyohM2a2zrKSMNqeST0GQpXzWNc7Si1bkCN62dwwIDAQAB";
		mHelper = new IabHelper(this, base64EncodedPublicKey);

		// enable debug logging (for a production application, you should set
		// this to false).
		mHelper.enableDebugLogging(Constants.LOG_MODE);

		// Start setup. This is asynchronous and the specified listener
		// will be called once setup completes.
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {

			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");

				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					complain("Problem setting up in-app billing: " + result);
					return;
				}

				// Have we been disposed of in the meantime? If so, quit.
				if (mHelper == null)
					return;

				// IAB is fully set up. Now, let's get an inventory of stuff we
				// own.
				Log.d(TAG, "Setup successful. Querying inventory.");

			}
		});

	}

	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
			Log.d(TAG, "Query inventory finished.");

			// Have we been disposed of in the meantime? If so, quit.
			if (mHelper == null)
				return;

			// Is it a failure?
			if (result.isFailure()) {
				complain("Failed to query inventory: " + result);
				return;
			}

			Log.d(TAG, "Query inventory was successful.");

			/*
			 * Check for items we own. Notice that for each purchase, we check
			 * the developer payload to see if it's correct! See
			 * verifyDeveloperPayload().
			 */

			// Do we have the premium upgrade?
			Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
			mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
			Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));

			updateUi();
		}
	};

	private void updateUi() {

		mSpinner.setOnItemSelectedListener(null);
		if (mIsPremium) {
			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(this, R.array.array_themes_premium,
							android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mSpinner.setAdapter(adapter);

			mButtonPaidOpitons.setVisibility(View.GONE);
		} else {
			ArrayAdapter<CharSequence> adapter = ArrayAdapter
					.createFromResource(this, R.array.array_themes,
							android.R.layout.simple_spinner_item);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			mSpinner.setAdapter(adapter);
			mButtonPaidOpitons.setVisibility(View.VISIBLE);
			if (mIconSet > 1) {
				setIconSet(0);
			}

		}

		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("selected: " + position);
				setIconSet(position);
			}

			public void onNothingSelected(AdapterView<?> parent) {
				System.out.println("Onnothing selected");
				setIconSet(0);
			}

		});
		System.out.println("Iconset: " + mIconSet);
		try {
			mSpinner.setSelection(mIconSet, false);
		} catch (Exception e) {
		}

	}

	// User clicked the "Buy Gas" button
	public void googleIAPBuy() {
		Log.d(TAG, "Buy gas button clicked.");

		// launch the gas purchase UI flow.
		// We will be notified of completion via mPurchaseFinishedListener
		Log.d(TAG, "Launching purchase flow for gas.");

		/*
		 * TODO: for security, generate your payload here for verification. See
		 * the comments on verifyDeveloperPayload() for more info. Since this is
		 * a SAMPLE, we just use an empty string, but on a production app you
		 * should carefully generate this.
		 */
		String payload = "";
		mHelper.launchPurchaseFlow(this, SKU_PREMIUM, RC_REQUEST,
				mPurchaseFinishedListener, payload);
	}

	/**
	 * {@inheritDoc }
	 */
	@Override
	public int getMenuResource() {
		return R.menu.menu_close;
	}

	/**
	 * {@inheritDoc }
	 */
	@Override
	public int getMenuCloseId() {
		return R.id.menu_close;
	}

	/**
	 * {@inheritDoc }
	 */
	public void onClick(View view) {
		if (view == mButtonResetHiScore) {
			PreferencesService.instance().resetHiScore();
			updateHiScore();
		} else if (view == mCbSoundEnabled) {
			PreferencesService.instance().saveSoundEnabled(
					mCbSoundEnabled.isChecked());
		} else if (view == mButtonSupport) {
			openGooglePlay();
		} else if (view == mButtonPaidOpitons) {
			showPaidOptions();
		}
	}

	private void updateHiScore() {
		int hiscore = PreferencesService.instance().getHiScore();
		if (hiscore == PreferencesService.HISCORE_DEFAULT) {
			mTvHiScore.setText(" - ");
		} else {
			mTvHiScore.setText(" " + hiscore);
		}
	}

	private void setIconSet(int iconSet) {
		if (iconSet != mIconSet) {
			System.out.println("Saving iconset: " + iconSet);
			PreferencesService.instance().saveIconsSet(iconSet);
			Toast.makeText(this, R.string.message_effect_new_game,
					Toast.LENGTH_LONG).show();
			mIconSet = iconSet;
		}
	}

	private void showPaidOptions() {

		String options[] = { "Rent from Fairket", "Buy from Google" };
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("Choose")
				.setItems(options, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (which == 0) {
							mFaiirketApiClient
									.startPlanSubscribeFlow(
											PreferencesActivity.this,
											FairketApiClient.PRODUCT_APPTIME,
											new FairketApiClient.OnPlanSubscribeListener() {

												@Override
												public void onPlanSubscribeFinished(
														FairketResult result) {
													fairketChkForFreePlan();
												}
											});
						} else if (which == 1) {
							googleIAPBuy();
						}

					}
				}).create();
		dialog.show();
	}

	/**
	 * Open the market
	 */
	private void openGooglePlay() {
		String uri = "market://details?id=org.androidsoft.games.memory.kids";
		Intent intentGoToGooglePlay = new Intent(Intent.ACTION_VIEW,
				Uri.parse(uri));
		startActivity(intentGoToGooglePlay);
	}

	@Override
	protected void onResume() {
		super.onResume();

		FairketAppTimeHelper.onResume(mFaiirketApiClient,
				new FairketApiClient.OnInitializeListener() {

					@Override
					public void onInitializeFinished(FairketResult result) {
						fairketChkForFreePlan();
					}

				});
	}

	@Override
	protected void onPause() {
		super.onPause();
		FairketAppTimeHelper.onPause(mFaiirketApiClient);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		FairketAppTimeHelper.onDestroy(mFaiirketApiClient);

		// very important:
		Log.d(TAG, "Destroying helper.");
		if (mHelper != null) {
			mHelper.dispose();
			mHelper = null;
		}

	}

	void complain(String message) {
		Log.e(TAG, "**** TrivialDrive Error: " + message);
		alert("Error: " + message);
	}

	void alert(String message) {
		// AlertDialog.Builder bld = new AlertDialog.Builder(this);
		// bld.setMessage(message);
		// bld.setNeutralButton("OK", null);
		// Log.d(TAG, "Showing alert dialog: " + message);
		// bld.create().show();
	}

	boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();

		/*
		 * TODO: verify that the developer payload of the purchase is correct.
		 * It will be the same one that you sent when initiating the purchase.
		 * 
		 * WARNING: Locally generating a random string when starting a purchase
		 * and verifying it here might seem like a good approach, but this will
		 * fail in the case where the user purchases an item on one device and
		 * then uses your app on a different device, because on the other device
		 * you will not have access to the random string you originally
		 * generated.
		 * 
		 * So a good developer payload has these characteristics:
		 * 
		 * 1. If two different users purchase an item, the payload is different
		 * between them, so that one user's purchase can't be replayed to
		 * another user.
		 * 
		 * 2. The payload must be such that you can verify it even when the app
		 * wasn't the one who initiated the purchase flow (so that items
		 * purchased by the user on one device work on other devices owned by
		 * the user).
		 * 
		 * Using your own server to store and verify developer payloads across
		 * app installations is recommended.
		 */

		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + ","
				+ data);
		if (mHelper == null)
			return;

		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {

			super.onActivityResult(requestCode, resultCode, data);

			try {
				mFaiirketApiClient.onActivityResult(requestCode, resultCode,
						data);
			} catch (Exception e) {
				Log.wtf(TAG, e);
			}
		} else {
			Log.d(TAG, "onActivityResult handled by IABUtil.");
		}
	}

	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: "
					+ purchase);

			// if we were disposed of in the meantime, quit.
			if (mHelper == null)
				return;

			if (result.isFailure()) {
				complain("Error purchasing: " + result);
				return;
			}
			if (!verifyDeveloperPayload(purchase)) {
				complain("Error purchasing. Authenticity verification failed.");
				return;
			}

			Log.d(TAG, "Purchase successful.");

			if (purchase.getSku().equals(SKU_PREMIUM)) {
				// bought the premium upgrade!
				Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
				alert("Thank you for upgrading to premium!");
				mIsPremium = true;
				updateUi();
			}
		}
	};

	private void fairketChkForFreePlan() {
		// Check if the subscribed to free plan
		mFaiirketApiClient.isFreePlanSubscribedAsync(
				FairketApiClient.PRODUCT_APPTIME,
				new FairketApiClient.OnAsyncOperationListener() {

					@Override
					public void onAsyncOperationFinished(FairketResult result) {
						Log.d(TAG,
								"isFreePlanSubscribedAsync: "
										+ result.isSuccess());
						if (result.isSuccess()) {
							mIsPremium = false;
							// If free plan check with Google IAP
							try {
								mHelper.queryInventoryAsync(mGotInventoryListener);
							} catch (Exception e) {

							}
						} else {
							mIsPremium = true;
						}
						updateUi();
					}
				});

	}
}
