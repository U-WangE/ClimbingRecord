package com.ihavesookchi.climbingrecord.data.uistate

sealed class SearchUiState {
    object SearchSuccess: SearchUiState()
    object SearchFailure: SearchUiState()
}
