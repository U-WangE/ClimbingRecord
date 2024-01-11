package com.ihavesookchi.climbingrecord.data.response

import com.google.gson.annotations.SerializedName

data class GoalsDataResponse(
    @SerializedName("goalsAchievement_status")
    val goalsAchievementStatus: GoalsAchievementStatus
) {
    data class GoalsAchievementStatus(
        @SerializedName("startDate")
        val startDate: Long,
        @SerializedName("endDate")
        val endDate: Long,
        @SerializedName("goalsDetail")
        val goalsDetail: List<GoalDetail>
    ) {
        data class GoalDetail(
            @SerializedName("goal")
            val goal: Int,
            @SerializedName("goalActual")
            val goalActual: Int,
            @SerializedName("goalColorRGB")
            val goalColorRGB: String,
        )
    }
}
