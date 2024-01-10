package com.ihavesookchi.climbingrecord

import android.app.Application
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ClimbingRecord: Application() {
    override fun onCreate() {
        super.onCreate()

        ClimbingRecordLogger.initInstance(this)
        FirebaseApp.initializeApp(this)
    }
}