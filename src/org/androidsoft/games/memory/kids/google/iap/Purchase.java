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
 * Represents an in-app billing purchase.
 */
public class Purchase {
	String mItemType; // ITEM_TYPE_INAPP or ITEM_TYPE_SUBS
	String mOrderId;
	String mPackageName;
	String mSku;
	long mPurchaseTime;
	int mPurchaseState;
	String mDeveloperPayload;
	String mToken;
	String mOriginalJson;
	String mSignature;

	public Purchase(String itemType, String jsonPurchaseInfo, String signature)
			throws JSONException {
		mItemType = itemType;
		mOriginalJson = jsonPurchaseInfo;
		JSONObject o = new JSONObject(mOriginalJson);
		mOrderId = o.optString("orderId");
		mPackageName = o.optString("packageName");
		mSku = o.optString("productId");
		mPurchaseTime = o.optLong("purchaseTime");
		mPurchaseState = o.optInt("purchaseState");
		mDeveloperPayload = o.optString("developerPayload");
		mToken = o.optString("token", o.optString("purchaseToken"));
		mSignature = signature;
	}

	public Purchase() {

	}

	public String getItemType() {
		return mItemType;
	}

	public String getOrderId() {
		return mOrderId;
	}

	public String getPackageName() {
		return mPackageName;
	}

	public String getSku() {
		return mSku;
	}

	public long getPurchaseTime() {
		return mPurchaseTime;
	}

	public int getPurchaseState() {
		return mPurchaseState;
	}

	public String getDeveloperPayload() {
		return mDeveloperPayload;
	}

	public String getToken() {
		return mToken;
	}

	public String getOriginalJson() {
		return mOriginalJson;
	}

	public String getSignature() {
		return mSignature;
	}

	/**
	 * @param mItemType
	 *            the mItemType to set
	 */
	public void setItemType(String mItemType) {
		this.mItemType = mItemType;
	}

	/**
	 * @param mOrderId
	 *            the mOrderId to set
	 */
	public void setOrderId(String mOrderId) {
		this.mOrderId = mOrderId;
	}

	/**
	 * @param mPackageName
	 *            the mPackageName to set
	 */
	public void setPackageName(String mPackageName) {
		this.mPackageName = mPackageName;
	}

	/**
	 * @param mSku
	 *            the mSku to set
	 */
	public void setSku(String mSku) {
		this.mSku = mSku;
	}

	/**
	 * @param mPurchaseTime
	 *            the mPurchaseTime to set
	 */
	public void setPurchaseTime(long mPurchaseTime) {
		this.mPurchaseTime = mPurchaseTime;
	}

	/**
	 * @param mPurchaseState
	 *            the mPurchaseState to set
	 */
	public void setPurchaseState(int mPurchaseState) {
		this.mPurchaseState = mPurchaseState;
	}

	/**
	 * @param mDeveloperPayload
	 *            the mDeveloperPayload to set
	 */
	public void setDeveloperPayload(String mDeveloperPayload) {
		this.mDeveloperPayload = mDeveloperPayload;
	}

	/**
	 * @param mToken
	 *            the mToken to set
	 */
	public void setToken(String token) {
		this.mToken = token;
	}

	/**
	 * @param originalJson
	 *            the mOriginalJson to set
	 */
	public void setOriginalJson(String originalJson) {
		this.mOriginalJson = originalJson;
	}

	/**
	 * @param mSignature
	 *            the signature to set
	 */
	public void setSignature(String signature) {
		this.mSignature = signature;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Purchase [mItemType=" + mItemType + ", mOrderId=" + mOrderId
				+ ", mPackageName=" + mPackageName + ", mSku=" + mSku
				+ ", mPurchaseTime=" + mPurchaseTime + ", mPurchaseState="
				+ mPurchaseState + ", mDeveloperPayload=" + mDeveloperPayload
				+ ", mToken=" + mToken + ", mOriginalJson=" + mOriginalJson
				+ ", mSignature=" + mSignature + "]";
	}

}
