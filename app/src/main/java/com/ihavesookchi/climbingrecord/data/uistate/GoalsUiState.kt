package com.ihavesookchi.climbingrecord.data.uistate

sealed class GoalsDataUiState {
    object GoalsDataSuccess: GoalsDataUiState()
    object GoalsDataIsNull: GoalsDataUiState()
    object GoalsDataFailure: GoalsDataUiState()
    object AttemptLimitExceeded : GoalsDataUiState()
}