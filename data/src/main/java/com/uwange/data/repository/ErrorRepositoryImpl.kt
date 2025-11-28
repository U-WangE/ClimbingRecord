package com.uwange.data.repository

import com.uwange.domain.repository.ErrorRepository
import com.uwange.network.source.error.ErrorDataSource
import javax.inject.Inject

class ErrorRepositoryImpl @Inject constructor(
    private val errorDataSource: ErrorDataSource
): ErrorRepository {
    override suspend fun logError(exception: Throwable) = errorDataSource.logError(exception)
    override suspend fun setUserId(userId: String) = errorDataSource.setUserId(userId)
    override suspend fun clearUserId() = errorDataSource.clearUserId()
}