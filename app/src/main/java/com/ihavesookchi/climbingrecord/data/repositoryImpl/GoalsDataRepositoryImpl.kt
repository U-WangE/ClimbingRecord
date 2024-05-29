package com.ihavesookchi.climbingrecord.data.repositoryImpl

import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.data.KakaoApi
import com.ihavesookchi.climbingrecord.data.repository.GoalsDataRepository
import com.ihavesookchi.climbingrecord.data.response.GoalsDataResponse
import com.ihavesookchi.climbingrecord.util.CommonUtil.retry
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoalsDataRepositoryImpl @Inject constructor(
    private val kakaoRetrofit: KakaoApi
): GoalsDataRepository {
    private var goalsDataResponse: GoalsDataResponse = GoalsDataResponse()

    private val CLASS_NAME = this::class.java.simpleName

    private val db = Firebase.firestore
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun initResponse() { goalsDataResponse = GoalsDataResponse() }

    override suspend fun initGoalsDataFromFirebaseDB(): Task<Void>? {
        return retry {
            db.collection("goals")
                .document(firebaseAuth.currentUser?.uid ?: "anonymous")
                .set(GoalsDataResponse())
                .run {
                    await()
                    this
                }
        }
    }
    override suspend fun getGoalsDataFromFirebaseDB(): Task<DocumentSnapshot>? {
        return retry {
            db.collection("goals")
                .document(firebaseAuth.currentUser?.uid ?: "anonymous")
                .get()
                .run {
                    await()
                    this
                }
        }
    }

    override suspend fun uploadGoalAchievementDataToFirebaseDB(goalsAchievementStatus: GoalsDataResponse.GoalsAchievementStatus): Task<Void>? {
        return retry {
            db.collection("goals")
                .document(firebaseAuth.currentUser?.uid ?: "anonymous")
                .set(mapOf("goalsAchievementStatus" to goalsAchievementStatus))
                .run {
                    await()
                    this
                }
        }
    }

    override fun setGoalsData(documentSnapshot: DocumentSnapshot?) {
        goalsDataResponse = documentSnapshot?.toObject(GoalsDataResponse::class.java)?:GoalsDataResponse()

        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "setGoalsData Set GoalsDataResponse Done    goalsDataResponse  :  $goalsDataResponse")
    }

    override fun getGoalsData(): GoalsDataResponse {
        return goalsDataResponse
    }

    override fun getGoalDetails(): List<GoalsDataResponse.GoalsAchievementStatus.GoalDetail> = goalsDataResponse.goalsAchievementStatus.goalDetails
    override fun getStartDate(): Long? = goalsDataResponse.goalsAchievementStatus.startDate
    override fun getEndDate(): Long? = goalsDataResponse.goalsAchievementStatus.endDate
    override fun getTrackingClimbingRecords(): List<GoalsDataResponse.TrackingClimbingRecord> = goalsDataResponse.trackingClimbingRecords
    override fun getGoalsAchievementStatus(): GoalsDataResponse.GoalsAchievementStatus = goalsDataResponse.goalsAchievementStatus
}