package com.uwange.analytics.di

import android.annotation.SuppressLint
import android.content.Context
import com.google.firebase.BuildConfig
import com.google.firebase.analytics.FirebaseAnalytics
import com.uwange.analytics.AnalyticsHelper
import com.uwange.analytics.DebugAnalyticsHelper
import com.uwange.analytics.FirebaseAnalyticsHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {
    @SuppressLint("MissingPermission")
    @Provides
    @Singleton
    fun provideFirebase(@ApplicationContext appContext: Context): FirebaseAnalytics =
        FirebaseAnalytics.getInstance(appContext)

    @Provides
    @Singleton
    @Debug
    fun provideDebugAnalyticsHelper(): AnalyticsHelper =
        DebugAnalyticsHelper()

    @Provides
    @Singleton
    @Release
    fun provideReleaseAnalyticsHelper(firebaseAnalytics: FirebaseAnalytics): AnalyticsHelper =
        FirebaseAnalyticsHelper(firebaseAnalytics)

    @Provides
    @Singleton
    fun provideAnalyticsHelper(
        @Debug debugHelper: AnalyticsHelper,
        @Release releaseHelper: AnalyticsHelper
    ): AnalyticsHelper = if (BuildConfig.BUILD_TYPE == "release") releaseHelper else debugHelper
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Debug

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Release
