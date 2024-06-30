package com.ihavesookchi.climbingrecord.data.repositoryImpl

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
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
    override suspend fun getRecordListDataFromFirebaseDB(): Task<QuerySnapshot>? {
        return retry {
            db.collection("records")
                .document(firebaseAuth.currentUser?.uid?: "anonymous")
                .collection("recordList")
                .get()
                .run {
                    await()
                    this
                }
        }
    }

    override fun setRecordsDataResponse(querySnapshot: QuerySnapshot?) {
        val recordsList = mutableListOf<RecordsDataResponse.Record>()

        querySnapshot?.let { snapshot ->
            snapshot.documents.forEach { doc ->
                val content = doc.getString("content") ?: ""
                val recordImages = doc.get("recordImages") as? List<String> ?: emptyList()
                val achievementDataList = (doc.get("achievementList") as? List<HashMap<String, Any>>)?.map {
                    RecordsDataResponse.Record.AchievementData(
                        it["achievementQuantity"] as? Int ?: 0,
                        it["achievementColorRGB"] as? String ?: ""
                    )
                } ?: emptyList()
                val achievementDate = doc.getLong("achievementDate") ?: 0L

                // Create a Record object and add it to recordsList
                val record = RecordsDataResponse.Record(content, recordImages, achievementDataList, achievementDate)
                recordsList.add(record)
            }
        }

        // Sort recordsList by achievementDate in descending order if needed
        recordsList.sortByDescending { it.achievementDate }

        // Set recordsList to recordsDataResponse
        recordsDataResponse = RecordsDataResponse(recordsList)

        // Log the operation
        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "setRecordListData Set RecordListDataResponse Done    recordListDataResponse  :  ${recordsDataResponse}")
    }
//    override fun setRecordsDataResponse(documentSnapshot: DocumentSnapshot?) {
//        val recordsDataResponse = documentSnapshot?.toObject(RecordsDataResponse::class.java)?:RecordsDataResponse()
//        recordsDataResponse.recordList.takeIf { it.isNotEmpty() }?.sortedByDescending { it.achievementDate }
//        this.recordsDataResponse = recordsDataResponse
//
//        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "setRecordListData Set RecordListDataResponse Done    recordListDataResponse  :  ${this.recordsDataResponse}")
//    }

    override fun getRecordList() = recordsDataResponse.recordList
}