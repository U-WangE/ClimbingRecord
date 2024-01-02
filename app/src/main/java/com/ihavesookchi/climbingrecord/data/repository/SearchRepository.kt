package com.ihavesookchi.climbingrecord.data.repository

import com.ihavesookchi.climbingrecord.data.response.SearchKeywordResponse
import com.ihavesookchi.climbingrecord.data.uistate.SearchKeywordUiState
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    fun searchKeywordApi(keyword: String): Flow<SearchKeywordUiState>
    fun getClimbingCenters(): List<SearchKeywordResponse.Document>
}