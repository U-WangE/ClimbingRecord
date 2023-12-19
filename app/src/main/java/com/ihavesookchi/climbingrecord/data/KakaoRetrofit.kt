package com.ihavesookchi.climbingrecord.data

import com.ihavesookchi.climbingrecord.data.Const.KAKAO_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object KakaoRetrofit {
    val kakaoRetrofit = Retrofit.Builder()
        .baseUrl(KAKAO_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(KakaoApi::class.java)
}