package com.ihavesookchi.climbingrecord.data.repositoryImpl

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.data.KakaoApi
import com.ihavesookchi.climbingrecord.data.repository.GoalsDataRepository
import com.ihavesookchi.climbingrecord.data.response.GoalsDataResponse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GoalsDataRepositoryImpl @Inject constructor(
    private val kakaoRetrofit: KakaoApi
): GoalsDataRepository {
    private var goalsDataResponse: GoalsDataResponse = GoalsDataResponse()

    private val CLASS_NAME = this::class.java.simpleName

    private val db = Firebase.firestore
    private val firebaseAuth = FirebaseAuth.getInstance()
    override suspend fun goalsDataApi(): DocumentSnapshot? {
        return db.collection("goals")
            .document(firebaseAuth.currentUser?.uid ?: "anonymous")
            .get()
            .await()
    }

    override fun setGoalsData(documentSnapshot: DocumentSnapshot) {
        goalsDataResponse = documentSnapshot.toObject(GoalsDataResponse::class.java)!!

        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "Set GoalsDataResponse Done    goalsDataResponse  :  $goalsDataResponse")
    }

    override fun getGoalDetails(): List<GoalsDataResponse.GoalsAchievementStatus.GoalDetail> = goalsDataResponse.goalsAchievementStatus.goalDetails
    override fun getStartDate(): Long = goalsDataResponse.goalsAchievementStatus.startDate
    override fun getEndDate(): Long = goalsDataResponse.goalsAchievementStatus.endDate
    override fun getTrackingClimbingRecords(): List<GoalsDataResponse.TrackingClimbingRecord> = goalsDataResponse.trackingClimbingRecords
}