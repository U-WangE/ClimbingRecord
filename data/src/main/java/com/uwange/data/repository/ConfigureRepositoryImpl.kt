package com.uwange.data.repository

import com.uwange.domain.model.ForceUpdate
import com.uwange.domain.repository.ConfigureRepository
import com.uwange.network.source.configure.ConfigureDataSource
import javax.inject.Inject

class ConfigureRepositoryImpl @Inject constructor(
    private val configureDataSource: ConfigureDataSource
) : ConfigureRepository {
    override suspend fun getUpdateInfo(): ForceUpdate {
        return configureDataSource
    }
}