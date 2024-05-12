package com.ihavesookchi.climbingrecord.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GoalsDataResponse(
    @SerializedName("goalsAchievementStatus")
    var goalsAchievementStatus: GoalsAchievementStatus,
    @SerializedName("trackingClimbingRecords")
    val trackingClimbingRecords: ArrayList<TrackingClimbingRecord>
): Parcelable {

    constructor() : this(
        goalsAchievementStatus = GoalsAchievementStatus(null, null, arrayListOf()),
        trackingClimbingRecords = arrayListOf()
    )

    @Parcelize
    data class GoalsAchievementStatus(
        @SerializedName("startDate")
        var startDate: Long? = null,
        @SerializedName("endDate")
        var endDate: Long? = null,
        @SerializedName("goalDetails")
        var goalDetails: ArrayList<GoalDetail> = arrayListOf()
    ): Parcelable {
        @Parcelize
        data class GoalDetail(
            @SerializedName("goal")
            var goal: Int = 0,
            @SerializedName("goalActual")
            var goalActual: Int = 0,
            @SerializedName("goalColorRGB")
            var goalColorRGB: String = ""
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
