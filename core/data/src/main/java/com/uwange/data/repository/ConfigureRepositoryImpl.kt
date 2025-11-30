package com.uwange.data.repository

import com.uwange.domain.model.configure.ForceUpdate
import com.uwange.domain.repository.ConfigureRepository
import com.uwange.network.model.configure.GetForceUpdateInfoResponse
import com.uwange.network.source.configure.ConfigDataSource
import com.uwange.network.source.configure.ConfigDataSource.Key
import javax.inject.Inject

class ConfigureRepositoryImpl @Inject constructor(
    private val configureDataSource: ConfigDataSource
) : ConfigureRepository {
    override suspend fun getUpdateInfo(): ForceUpdate {
        return configureDataSource.getReferenceType(
            key = Key.getKey(ConfigDataSource.FORCE_UPDATE),
            defaultValue = GetForceUpdateInfoResponse()
        ).toDomain()
    }
}