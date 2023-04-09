package com.nassafy.aro.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesUtil(context: Context) {
    private var preferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun addUserRefreshToken(refresh_token: String) {
        val editor = preferences.edit()
        editor.putString(REFRESH_TOKEN, refresh_token)
        editor.apply()
    } // End of addUserRefreshToken

    fun getUserRefreshToken(): String {
        return preferences.getString(REFRESH_TOKEN, "").toString()
    } // End of getUserRefreshToken

    fun addUserAccessToken(access_token: String) {
        val editor = preferences.edit()
        val temp =
            "Bearer $access_token"
        editor.putString(ACCESS_TOKEN, temp)
        editor.apply()
    } // End of addUserAccessToken

    fun getUserAccessToken(): String {
        return preferences.getString(ACCESS_TOKEN, "").toString()
    } // End of getUserAccessToken

    fun addFcmToken(fcm_token: String) {
        val editor = preferences.edit()
        editor.putString(FCM_TOKEN, fcm_token)
        editor.apply()
    } // End of addFcmToken

    fun getFcmToken(): String {
        return preferences.getString(FCM_TOKEN, "").toString()
    } // End of getUserRefreshToken


    companion object {
        const val SHARED_PREFERENCES_NAME = "aro_preference"
        const val ACCESS_TOKEN = "access_token"
        const val REFRESH_TOKEN = "refresh_token"
        const val FCM_TOKEN = "fcm_token"
    }
} // End of SharedPreferencesUtil class
