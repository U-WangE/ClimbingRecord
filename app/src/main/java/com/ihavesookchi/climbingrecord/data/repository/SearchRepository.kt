package com.ihavesookchi.climbingrecord.data.repository

import com.ihavesookchi.climbingrecord.data.response.SearchKeywordResponse
import com.ihavesookchi.climbingrecord.data.uistate.SearchKeywordUiState
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface SearchRepository {
    suspend fun searchKeywordApi(keyword: String): Response<SearchKeywordResponse>
    fun setSearchKeywordResponse(searchKeywordResponse: SearchKeywordResponse?)
    fun getClimbingCenters(): List<SearchKeywordResponse.Document>
}