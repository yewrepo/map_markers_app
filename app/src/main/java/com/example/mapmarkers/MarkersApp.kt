package com.example.mapmarkers

import android.app.Application
import timber.log.Timber

class MarkersApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}