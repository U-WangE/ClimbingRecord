package com.ihavesookchi.climbingrecord.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.ihavesookchi.climbingrecord.data.response.RecordsDataResponse

interface RecordDataRepository {
    suspend fun getRecordListDataFromFirebaseDB(): Task<QuerySnapshot>?
    fun setRecordsDataResponse(querySnapshot: QuerySnapshot?)
    fun initRecordsDataResponse()
    fun getRecordList(): List<RecordsDataResponse.Record>
}