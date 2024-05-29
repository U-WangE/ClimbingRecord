package com.ihavesookchi.climbingrecord.data.uistate

sealed class ClimbingCenterRecordUiState {
    object ClimbingCenterRecordSuccess: ClimbingCenterRecordUiState()
    object ClimbingCenterRecordFailure: ClimbingCenterRecordUiState()
}
