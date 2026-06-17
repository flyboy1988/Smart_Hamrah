package com.smartbank.smarthamrah.core.utils


import android.content.Context
import android.net.Uri

object PreferenceManager {
    private const val PREF_NAME = "smartbank_prefs"
    private const val KEY_PROFILE_IMAGE = "profile_image_uri"

    // Saves the URI string representation
    fun saveProfileImage(context: Context, uri: Uri?) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_PROFILE_IMAGE, uri?.toString()).apply()
    }

    // Retrieves the URI string and converts it back to a Uri object
    fun getProfileImage(context: Context): Uri? {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val uriString = prefs.getString(KEY_PROFILE_IMAGE, null)
        return if (!uriString.isNullOrEmpty()) Uri.parse(uriString) else null
    }
}