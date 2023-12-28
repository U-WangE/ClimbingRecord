package com.ihavesookchi.climbingrecord.data.uistate

sealed class SearchKeywordUiState {
    object SearchKeywordSuccess: SearchKeywordUiState()
    object SearchKeywordFailure: SearchKeywordUiState()
}
