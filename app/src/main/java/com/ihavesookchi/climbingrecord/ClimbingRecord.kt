package com.ihavesookchi.climbingrecord

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.initialize
import com.ihavesookchi.climbingrecord.viewModel.BaseViewModel
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ClimbingRecord: Application() {

    override fun onCreate() {
        super.onCreate()

        Firebase.initialize(context = this)
        ClimbingRecordLogger.initInstance(this)
        FirebaseApp.initializeApp(this)
        Firebase.appCheck.installAppCheckProviderFactory(
            if (BuildConfig.DEBUG)
                DebugAppCheckProviderFactory.getInstance()
            else
                PlayIntegrityAppCheckProviderFactory.getInstance()
        )
    }
}