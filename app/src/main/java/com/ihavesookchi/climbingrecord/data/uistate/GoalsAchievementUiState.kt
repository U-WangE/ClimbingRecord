package com.ihavesookchi.climbingrecord.data.uistate

sealed class GoalsAchievementUiState {
    object GoalsAchievementSuccess: GoalsAchievementUiState()
    object GoalsAchievementFailure: GoalsAchievementUiState()
    object AttemptLimitExceeded : GoalsAchievementUiState()

    // Goal Entered Check Ui State
    object GoalSettingSuccess: GoalsAchievementUiState()
    object NotGoalSetting: GoalsAchievementUiState()
    object UnusualGoalSetting: GoalsAchievementUiState()
    object NotGoalPeriodSetting: GoalsAchievementUiState()
    object UnusualGoalPeriod: GoalsAchievementUiState()
}