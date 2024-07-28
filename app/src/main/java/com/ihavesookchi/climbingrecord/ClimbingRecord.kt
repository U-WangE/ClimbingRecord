package com.ihavesookchi.climbingrecord

import android.app.Application
import android.content.Context
import android.util.TypedValue
import com.google.firebase.Firebase
import com.google.firebase.appcheck.appCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory
import com.google.firebase.initialize
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ClimbingRecord: Application() {
    init {
        instance = this
    }

    companion object {
        var instance: ClimbingRecord? = null
        fun context(): Context = instance!!.applicationContext
    }

    override fun onCreate() {
        super.onCreate()

        ClimbingRecordLogger.initInstance(this)
        Firebase.initialize(context = this)
        Firebase.appCheck.installAppCheckProviderFactory(
            if (BuildConfig.DEBUG)
                DebugAppCheckProviderFactory.getInstance()
            else
                PlayIntegrityAppCheckProviderFactory.getInstance()
        )
    }
}