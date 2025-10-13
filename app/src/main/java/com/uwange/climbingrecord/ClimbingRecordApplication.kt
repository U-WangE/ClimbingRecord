package com.uwange.climbingrecord

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ClimbingRecordApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initKakao()
    }

    private fun initKakao() = KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)
}