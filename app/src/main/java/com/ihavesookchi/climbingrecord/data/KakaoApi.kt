package com.ihavesookchi.climbingrecord.data

import com.ihavesookchi.climbingrecord.data.Const.SEARCH_KEYWORD
import com.ihavesookchi.climbingrecord.data.response.SearchKeywordResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoApi {
    @GET(SEARCH_KEYWORD)
    suspend fun searchKeywordApi(
        @Query("query") keyword: String
    ): Response<SearchKeywordResponse>
}