package com.smartbank.smarthamrah

import android.app.Application
import androidx.core.content.res.ResourcesCompat
import com.droidman.ktoasty.KToasty
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SmartBankApplication : Application(){


    override fun onCreate() {
        super.onCreate()

        // Configure KToasty Global Settings
        KToasty.Config.getInstance()
            // Replace R.font.your_custom_font with your actual font file name
            .setToastTypeface(ResourcesCompat.getFont(this, R.font.light)!!)
            .setTextSize(14) // Set a consistent size for your app

            .apply() // Don't forget to call apply!
    }
}
