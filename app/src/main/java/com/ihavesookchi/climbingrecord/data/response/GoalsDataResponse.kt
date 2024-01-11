package com.ihavesookchi.climbingrecord.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GoalsDataResponse(
    @SerializedName("goalsAchievementStatus")
    val goalsAchievementStatus: GoalsAchievementStatus
): Parcelable {

    constructor() : this(
        goalsAchievementStatus = GoalsAchievementStatus(0L, 0L, emptyList())
    )

    @Parcelize
    data class GoalsAchievementStatus(
        @SerializedName("startDate")
        val startDate: Long = 0,
        @SerializedName("endDate")
        val endDate: Long = 0,
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
}
