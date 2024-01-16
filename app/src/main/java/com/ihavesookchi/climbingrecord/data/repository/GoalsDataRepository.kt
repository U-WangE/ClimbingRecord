package com.ihavesookchi.climbingrecord.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.ihavesookchi.climbingrecord.data.response.GoalsDataResponse

interface GoalsDataRepository {
    suspend fun goalsDataApi(): DocumentSnapshot?
    fun setGoalsData(documentSnapshot: DocumentSnapshot)
    fun getGoalDetails(): List<GoalsDataResponse.GoalsAchievementStatus.GoalDetail>
    fun getStartDate(): Long
    fun getEndDate(): Long
    fun getTrackingClimbingRecords(): List<GoalsDataResponse.TrackingClimbingRecord>
    fun getGoalsAchievementStatus(): GoalsDataResponse.GoalsAchievementStatus
    fun initResponse()
}