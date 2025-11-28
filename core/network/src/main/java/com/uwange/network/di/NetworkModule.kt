package com.uwange.network.di

import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.uwange.climbingrecord.network.BuildConfig
import com.uwange.network.source.firebase.FirebaseAuthDataSource
import com.uwange.network.source.firebase.FirebaseAuthDataSourceImpl
import com.uwange.network.source.error.DebugErrorDataSourceImpl
import com.uwange.network.source.error.ErrorDataSource
import com.uwange.network.source.error.ErrorDataSourceImpl
import com.uwange.network.source.firebase.FireStoreDataSource
import com.uwange.network.source.firebase.FireStoreDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkBindsModule {

    @Binds
    @Singleton
    abstract fun bindsFirebaseAuthDataSource(
        firebaseAuthDataSourceImpl: FirebaseAuthDataSourceImpl
    ): FirebaseAuthDataSource

    @Binds
    @Singleton
    abstract fun bindsFireStoreDataSource(
        fireStoreDataSourceImpl: FireStoreDataSourceImpl
    ): FireStoreDataSource
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkProvidesModule {

    @Singleton
    @Provides
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Singleton
    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig = Firebase.remoteConfig.apply {
        val configSettings = remoteConfigSettings { minimumFetchIntervalInSeconds = 3600 }
        setConfigSettingsAsync(configSettings)
    }

    @Provides
    @Singleton
    @Debug
    fun provideDebugErrorDataSource(
        debugErrorDataSourceImpl: DebugErrorDataSourceImpl
    ): ErrorDataSource = debugErrorDataSourceImpl

    @Provides
    @Singleton
    @Release
    fun provideReleaseErrorDataSource(
        errorDataSourceImpl: ErrorDataSourceImpl
    ): ErrorDataSource = errorDataSourceImpl

    @Provides
    @Singleton
    fun provideErrorDataSource(
        @Debug debugErrorDataSource: ErrorDataSource,
        @Release releaseErrorDataSource: ErrorDataSource
    ): ErrorDataSource {
        return if (BuildConfig.BUILD_TYPE == "release") releaseErrorDataSource else debugErrorDataSource
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Debug

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Release