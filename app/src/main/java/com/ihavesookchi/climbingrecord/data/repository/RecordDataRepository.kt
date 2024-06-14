package com.ihavesookchi.climbingrecord.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.ihavesookchi.climbingrecord.data.response.RecordsDataResponse

interface RecordDataRepository {
    suspend fun getRecordListDataFromFirebaseDB(): Task<DocumentSnapshot>?
    fun setRecordsDataResponse(documentSnapshot: DocumentSnapshot?)
    fun initRecordsDataResponse()
    fun getRecordList(): List<RecordsDataResponse.Record>
}