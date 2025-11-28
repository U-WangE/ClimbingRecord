package com.uwange.domain.repository

import com.uwange.domain.model.configure.ForceUpdate

interface ConfigureRepository {
    suspend fun getUpdateInfo(): ForceUpdate
}