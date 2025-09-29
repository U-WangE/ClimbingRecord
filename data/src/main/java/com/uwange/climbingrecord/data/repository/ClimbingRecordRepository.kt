package com.uwange.climbingrecord.data.repository

import com.uwange.climbingrecord.domain.model.ClimbingRecord

interface ClimbingRecordRepository {
    suspend fun getAllRecords(): List<ClimbingRecord>
    suspend fun saveRecord(record: ClimbingRecord)
    suspend fun deleteRecord(id: String)
}
