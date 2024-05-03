package com.ihavesookchi.climbingrecord.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GoalsDataResponse(
    @SerializedName("goalsAchievementStatus")
    val goalsAchievementStatus: GoalsAchievementStatus,
    @SerializedName("trackingClimbingRecords")
    val trackingClimbingRecords: List<TrackingClimbingRecord>
): Parcelable {

    constructor() : this(
        goalsAchievementStatus = GoalsAchievementStatus(null, null, emptyList()),
        trackingClimbingRecords = emptyList()
    )

    @Parcelize
    data class GoalsAchievementStatus(
        @SerializedName("startDate")
        val startDate: Long? = null,
        @SerializedName("endDate")
        val endDate: Long? = null,
        @SerializedName("goalDetails")
        val goalDetails: List<GoalDetail> = emptyList()
    ): Parcelable {
        @Parcelize
        data class GoalDetail(
            @SerializedName("goal")
            val goal: Int = 0,
            @SerializedName("goalActual")
            val goalActual: Int = 0,
            @SerializedName("goalColorRGB")
            val goalColorRGB: String = "#ffffff"
        ): Parcelable
    }

    @Parcelize
    data class TrackingClimbingRecord(
        @SerializedName("dateType")
        val dateType: String = "",
        @SerializedName("exerciseTime")
        val exerciseTime: Int = 0,
        @SerializedName("achievementTotalCount")
        val achievementTotalCount: Int = 0,
        @SerializedName("exerciseCount")
        val exerciseCount: Int = 0,
    ): Parcelable
}
