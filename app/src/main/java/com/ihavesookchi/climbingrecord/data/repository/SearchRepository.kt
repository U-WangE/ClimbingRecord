package com.ihavesookchi.climbingrecord.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.ihavesookchi.climbingrecord.data.response.SearchKeywordResponse
import retrofit2.Response

interface SearchRepository {
    suspend fun searchKeywordApi(keyword: String): Response<SearchKeywordResponse>
    fun setSearchKeywordResponse(searchKeywordResponse: SearchKeywordResponse?)
    fun getSearchData(): List<SearchKeywordResponse.Document>
    fun removeSearchData()
    suspend fun getClimbingCenterRecord(center: SearchKeywordResponse.Document): Task<DocumentSnapshot>?
}