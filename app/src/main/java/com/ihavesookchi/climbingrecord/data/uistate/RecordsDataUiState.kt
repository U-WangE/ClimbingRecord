package com.ihavesookchi.climbingrecord.data.uistate

sealed class RecordsDataUiState {
    object RecordsDataSuccess: RecordsDataUiState()
    object RecordsDataFailure: RecordsDataUiState()
    object AttemptLimitExceeded : RecordsDataUiState()
}