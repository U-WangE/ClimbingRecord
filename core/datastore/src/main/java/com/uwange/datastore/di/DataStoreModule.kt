package com.uwange.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.uwange.datastore.datasource.token.LocalTokenDataSource
import com.uwange.datastore.datasource.token.LocalTokenDataSourceImpl
import com.uwange.datastore.datasource.user.LocalUserDataSource
import com.uwange.datastore.datasource.user.LocalUserDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreProvidesModule {
    private const val TOKEN_DATASTORE_NAME = "TOKEN_PREFERENCES"
    private val Context.tokenDataStore by preferencesDataStore(name = TOKEN_DATASTORE_NAME)

    @Provides
    @Singleton
    @Named("token")
    fun provideTokenDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.tokenDataStore
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreBindsModule {
    @Binds
    @Singleton
    abstract fun bindLocalTokenDataSource(
        localTokenDataSourceImpl: LocalTokenDataSourceImpl
    ): LocalTokenDataSource

    @Binds
    @Singleton
    abstract fun bindLocalTokenDataSource(
        localUserDataSourceImpl: LocalUserDataSourceImpl
    ): LocalUserDataSource
}