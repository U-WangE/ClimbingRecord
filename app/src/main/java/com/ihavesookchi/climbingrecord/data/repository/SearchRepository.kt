package com.ihavesookchi.climbingrecord.data.repository

import com.ihavesookchi.climbingrecord.data.response.SearchKeywordResponse
import retrofit2.Response

interface SearchRepository {
    suspend fun searchKeywordApi(keyword: String): Response<SearchKeywordResponse>
    fun setSearchKeywordResponse(searchKeywordResponse: SearchKeywordResponse?)
    fun getSearchData(): List<SearchKeywordResponse.Document>
    fun removeSearchData()
    fun setSelectedClimbingCenter(selectedClimbingCenter: SearchKeywordResponse.Document)
    fun getSelectedClimbingCenter(): SearchKeywordResponse.Document?
}