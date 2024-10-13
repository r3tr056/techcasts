package com.xdivision.techcasts

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timer.log.Timber

@HiltAndroidApp
class Techcasts : Application() {
    override fun onCreate() {
        super.onCreate()
        Timer.plant(Timber.DebugTree())
    }
}