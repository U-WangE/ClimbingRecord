package com.ihavesookchi.climbingrecord

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.initialize
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ClimbingRecord: Application() {
    override fun onCreate() {
        super.onCreate()

        Firebase.initialize(context = this)
        Firebase.appCheck.installAppCheckProviderFactory(
            DebugAppCheckProviderFactory.getInstance(),
//            PlayIntegrityAppCheckProviderFactory.getInstance(),
        )
//        10393062-c876-4d92-97c5-ef22cb39f16f
        ClimbingRecordLogger.initInstance(this)
        FirebaseApp.initializeApp(this)
    }
}