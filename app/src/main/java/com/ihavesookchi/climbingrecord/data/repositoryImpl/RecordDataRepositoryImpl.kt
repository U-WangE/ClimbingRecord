package com.ihavesookchi.climbingrecord.data.repositoryImpl

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.data.repository.RecordDataRepository
import com.ihavesookchi.climbingrecord.data.response.RecordsDataResponse
import com.ihavesookchi.climbingrecord.util.CommonUtil.retry
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RecordDataRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
): RecordDataRepository {
    private val CLASS_NAME = this::class.java.simpleName

    private var recordsDataResponse: RecordsDataResponse = RecordsDataResponse()

    override fun initRecordsDataResponse() {
        recordsDataResponse = RecordsDataResponse()
    }

    // Profile Image 저장하는 방식과 같이 Image 는 Storage 에 저장하고, 해당 이미지의 Uri를 firebase DB 에 저장하여 불러오는 방식 사용
    // Firebase Storage 의 무료 사용 용량이 크기 때문에 해당 방식으로 결정
    override suspend fun getRecordListDataFromFirebaseDB(): Task<DocumentSnapshot>? {
        return retry {
            db.collection("records")
                .document(firebaseAuth.currentUser?.uid?: "anonymous")
                .get()
                .run {
                    await()
                    this
                }
        }
    }

    override fun setRecordsDataResponse(documentSnapshot: DocumentSnapshot?) {
        val recordsDataResponse = documentSnapshot?.toObject(RecordsDataResponse::class.java)?:RecordsDataResponse()
        recordsDataResponse.recordList.takeIf { it.isNotEmpty() }?.sortedByDescending { it.achievementDate }
        this.recordsDataResponse = recordsDataResponse

        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "setRecordListData Set RecordListDataResponse Done    recordListDataResponse  :  ${documentSnapshot?:RecordsDataResponse()}")
    }

    override fun getRecordList() = recordsDataResponse.recordList
}