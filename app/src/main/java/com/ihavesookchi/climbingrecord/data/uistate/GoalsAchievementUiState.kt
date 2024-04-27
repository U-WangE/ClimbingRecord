package com.ihavesookchi.climbingrecord.data.uistate

sealed class GoalsAchievementUiState {
    object GoalsAchievementSuccess: GoalsAchievementUiState()
    object GoalsAchievementFailure: GoalsAchievementUiState()
    object AttemptLimitExceeded : GoalsAchievementUiState()
}