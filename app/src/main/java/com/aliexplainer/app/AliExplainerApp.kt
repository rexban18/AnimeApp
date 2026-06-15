package com.aliexplainer.app

import android.app.Application
import com.aliexplainer.app.utils.SettingsManager

class AliExplainerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SettingsManager.init(getSharedPreferences("ali_explainer_prefs", MODE_PRIVATE))
    }
}
