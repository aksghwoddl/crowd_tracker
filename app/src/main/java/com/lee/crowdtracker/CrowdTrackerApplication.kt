package com.lee.crowdtracker.root

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CrowdTrackerApplication: Application() {
    companion object{
        private lateinit var instance : CrowdTrackerApplication
        fun getInstance() = instance
    }

    override fun onCreate() {
        super.onCreate()
        instance = CrowdTrackerApplication()
    }
}