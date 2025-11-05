package com.uwange.domain.repository

import com.uwange.domain.model.ForceUpdate

interface ConfigureRepository {
    suspend fun getUpdateInfo(): ForceUpdate
}