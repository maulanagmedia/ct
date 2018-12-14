package com.maulana.custommodul;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class SessionManager {
	// Shared Preferences
	SharedPreferences pref;
	
	// Editor for Shared preferences
	Editor editor;
	
	// Context
	Context context;
	
	// Shared pref mode
	int PRIVATE_MODE = 0;
	
	// Sharedpref file name
	private static final String PREF_NAME = "GmediaClientTools";
	
	// All Shared Preferences Keys
	public static final String TAG_USERNAME = "username";
	public static final String TAG_ID_SITE = "ID_SITE";
	public static final String TAG_CUSTOMER_NAME = "customer_name";
	public static final String TAG_SITE_NAME = "site_name";
	public static final String TAG_CUSTOMER_ID = "customer_id";
	public static final String TAG_SERVICE_ID = "service_id";

	// Constructor
	public SessionManager(Context context){
		this.context = context;
		pref = this.context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}
	
	/**
	 * Create login session
	 * */
	public void createLoginSession(String username, String idSite, String customerName, String siteName, String customerID, String serviceID){

		editor.putString(TAG_USERNAME, username);

		editor.putString(TAG_ID_SITE, idSite);

		editor.putString(TAG_CUSTOMER_NAME, customerName);

		editor.putString(TAG_SITE_NAME, siteName);

		editor.putString(TAG_CUSTOMER_ID, customerID);

		editor.putString(TAG_SERVICE_ID, serviceID);

		editor.commit();
	}
	
	/**
	 * Get stored session data
	 * */

	public String getUserInfo(String key){
		return pref.getString(key, "");
	}

	public String getUsername(){
		return pref.getString(TAG_USERNAME, "");
	}

	public String getIdSite(){
		return pref.getString(TAG_ID_SITE, "");
	}

	public String getCustomerName(){
		return pref.getString(TAG_CUSTOMER_NAME, "");
	}

	public String getSiteName(){
		return pref.getString(TAG_SITE_NAME, "");
	}

	public void logoutUser(Intent logoutIntent){

		// Clearing all data from Shared Preferences
		try {
			editor.clear();
			editor.commit();
		}catch (Exception e){
			e.printStackTrace();
		}

		logoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(logoutIntent);
		((Activity)context).finish();
		((Activity)context).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	
	/**
	 * Quick check for login
	 * **/
	// Get Login State
	public boolean isLoggedIn(){
		if(getUsername().isEmpty()){
			return false;
		}else{
			return true;
		}
		/*return pref.getBoolean(IS_LOGIN, false);*/
	}
}
