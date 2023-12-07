package com.ihavesookchi.climbingrecord

import android.app.Application

class ClimbingRecord: Application() {
    override fun onCreate() {
        super.onCreate()

        ClimbingRecordLogger.initInstance(this)
    }
}