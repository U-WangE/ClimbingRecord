package com.ihavesookchi.climbingrecord.data.response

import com.google.gson.annotations.SerializedName

data class GoalsDataResponse(
    @SerializedName("goals_achievement_status")
    val goalsAchievementStatus: GoalsAchievementStatus
) {
    data class GoalsAchievementStatus(
        @SerializedName("start_date")
        val startDate: Long,
        @SerializedName("end_date")
        val endDate: Long,
        @SerializedName("goalsDetail")
        val goalsDetail: List<GoalDetail>
    ) {
        data class GoalDetail(
            @SerializedName("goal")
            val goal: Int,
            @SerializedName("goal_actual")
            val goalActual: Int,
            @SerializedName("goal_rgb")
            val goalColorRGB: String,
        )
    }
}
