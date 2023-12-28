package com.ihavesookchi.climbingrecord

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ClimbingRecord: Application() {
    override fun onCreate() {
        super.onCreate()

        ClimbingRecordLogger.initInstance(this)
    }
}