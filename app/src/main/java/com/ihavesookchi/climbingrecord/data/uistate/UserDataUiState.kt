package com.ihavesookchi.climbingrecord.data.uistate

sealed class UserDataUiState {
    object UserDataSuccess: UserDataUiState()
    object UserDataFailure: UserDataUiState()
    object UserDataUpdateSuccess: UserDataUiState()
    object UserDataUpdateFailure: UserDataUiState()
    object AttemptLimitExceeded : UserDataUiState()
}
