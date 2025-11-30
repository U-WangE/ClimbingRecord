package com.uwange.network.di

import com.uwange.climbingrecord.network.BuildConfig
import com.uwange.network.source.error.DebugErrorDataSourceImpl
import com.uwange.network.source.error.ErrorDataSource
import com.uwange.network.source.error.ErrorDataSourceImpl
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

}

@Module
@InstallIn(SingletonComponent::class)
object NetworkProvidesModule {

    @Singleton
    @Provides
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
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