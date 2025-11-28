package com.uwange.data.di

import com.uwange.data.repository.ConfigureRepositoryImpl
import com.uwange.data.repository.ErrorRepositoryImpl
import com.uwange.domain.repository.ConfigureRepository
import com.uwange.domain.repository.ErrorRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindsErrorRepository(
        errorRepositoryImpl: ErrorRepositoryImpl
    ): ErrorRepository

    @Binds
    @Singleton
    abstract fun bindsConfigureRepository(
        configureRepositoryImpl: ConfigureRepositoryImpl
    ): ConfigureRepository
}