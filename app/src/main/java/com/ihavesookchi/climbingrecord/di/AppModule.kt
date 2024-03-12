package com.ihavesookchi.climbingrecord.di

import android.content.Context
import android.content.SharedPreferences
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.ihavesookchi.climbingrecord.data.Const
import com.ihavesookchi.climbingrecord.data.Const.KAKAO_URL
import com.ihavesookchi.climbingrecord.data.KakaoApi
import com.ihavesookchi.climbingrecord.data.repository.GoalsDataRepository
import com.ihavesookchi.climbingrecord.data.repository.SearchRepository
import com.ihavesookchi.climbingrecord.data.repository.UserDataRepository
import com.ihavesookchi.climbingrecord.data.repositoryImpl.GoalsDataRepositoryImpl
import com.ihavesookchi.climbingrecord.data.repositoryImpl.SearchRepositoryImpl
import com.ihavesookchi.climbingrecord.data.repositoryImpl.UserDataRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun kakaoRetrofit(): KakaoApi {
        return Retrofit.Builder()
            .baseUrl(KAKAO_URL)
            .client(
                OkHttpClient()
                    .newBuilder()
                    .addInterceptor(Interceptor { chain ->
                        val newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", Const.KAKAO_API_KEY)
                            .build()
                        chain.proceed(newRequest)
                    })
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(KakaoApi::class.java)
    }

    // 시범 사용
    @Singleton
    @Provides
    @Named("db")
    fun provideDB(): FirebaseFirestore = Firebase.firestore

    @Singleton
    @Provides
    @Named("firebaseAuth")
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideUserDataRepository(
        @Named("db") db: FirebaseFirestore,
        @Named("firebaseAuth") firebaseAuth: FirebaseAuth
    ): UserDataRepository = UserDataRepositoryImpl(db, firebaseAuth)

    @Provides
    @Singleton
    fun provideSearchRepository(kakaoRetrofit: KakaoApi): SearchRepository = SearchRepositoryImpl(kakaoRetrofit)

    @Provides
    @Singleton
    fun provideGoalsDataRepository(kakaoRetrofit: KakaoApi): GoalsDataRepository = GoalsDataRepositoryImpl(kakaoRetrofit)
}