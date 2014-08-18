/* Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.androidsoft.games.memory.kids.google.iap;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents an in-app product's listing details.
 */
public class SkuDetails {
	String mItemType;
	String mSku;
	String mType;
	float mPrice;
	String mConsumerCurrencyCode;
	String mTitle;
	String mDescription;
	String mJson;
	String mPriceAmountMicros;
	String mDeveloperCurrencyCode;

	public SkuDetails(String jsonSkuDetails) throws JSONException {
		this(IabHelper.ITEM_TYPE_INAPP, jsonSkuDetails);
	}

	public SkuDetails(String itemType, String jsonSkuDetails)
			throws JSONException {
		mItemType = itemType;
		mJson = jsonSkuDetails;
		JSONObject o = new JSONObject(mJson);
		mSku = o.optString("productId");
		mType = o.optString("type");
		mTitle = o.optString("title");
		mDescription = o.optString("description");
		mPriceAmountMicros = o.optString("price_amount_micros");
		mPrice = Float.parseFloat(mPriceAmountMicros) / 1000000;
		setConsumerCurrencyCode(o.optString("price"));
		mDeveloperCurrencyCode = o.optString("price_currency_code");

	}

	private void setConsumerCurrencyCode(String price) {

		if (price.startsWith("Rs")) {
			mConsumerCurrencyCode = "INR";
		}

	}

	public String getSku() {
		return mSku;
	}

	public String getType() {
		return mType;
	}

	public float getPrice() {
		return mPrice;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getDescription() {
		return mDescription;
	}

	public String getPriceAmountMictors() {
		return mPriceAmountMicros;
	}

	public String getDeveloperCurrencyCode() {
		return mDeveloperCurrencyCode;
	}

	public String getConsumerCurrencyCode() {
		return mConsumerCurrencyCode;
	}

	@Override
	public String toString() {
		return "SkuDetails:" + mJson;
	}
}
