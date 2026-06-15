package com.aliexplainer.app.utils

import android.content.SharedPreferences

object SettingsManager {
    private const val PREFS_NAME = "ali_explainer_prefs"
    private const val KEY_BASE_URL = "base_url"
    private const val DEFAULT_URL = "http://10.0.2.2:8080/"

    private lateinit var prefs: SharedPreferences

    fun init(preferences: SharedPreferences) {
        prefs = preferences
    }

    fun getBaseUrl(): String {
        val url = prefs.getString(KEY_BASE_URL, DEFAULT_URL) ?: DEFAULT_URL
        return if (url.endsWith("/")) url else "$url/"
    }

    fun setBaseUrl(url: String) {
        prefs.edit().putString(KEY_BASE_URL, if (url.endsWith("/")) url else "$url/").apply()
    }
}
