package com.uwange.datastore.datasource.token

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.uwange.datastore.util.clear
import com.uwange.datastore.util.getValue
import com.uwange.datastore.util.setValue
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class LocalTokenDataSourceImpl @Inject constructor(
    @Named("token") private val dataStore: DataStore<Preferences>
): LocalTokenDataSource {
    override val accessToken: Flow<String> = dataStore.getValue(ACCESS_TOKEN, "")
    override val refreshToken: Flow<String> = dataStore.getValue(REFRESH_TOKEN, "")

    override suspend fun setAccessToken(accessToken: String) {
        dataStore.setValue(ACCESS_TOKEN, accessToken)
    }

    override suspend fun setRefreshToken(refreshToken: String) {
        dataStore.setValue(ACCESS_TOKEN, refreshToken)
    }

    override suspend fun clearToken() {
        dataStore.clear(ACCESS_TOKEN)
        dataStore.clear(REFRESH_TOKEN)
    }

    companion object {
        private val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        private val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
    }
}