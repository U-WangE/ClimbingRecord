package com.ihavesookchi.climbingrecord.data

import com.ihavesookchi.climbingrecord.data.Const.SEARCH_KEYWORD
import com.ihavesookchi.climbingrecord.data.response.SearchKeyWordResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoApi {
    @GET(SEARCH_KEYWORD)
    fun getSearchKeyword(
        @Header("Authorization") apiKey : String,
        @Query("query") keyWord: String,
        @Query("rect") rect: String
    ): SearchKeyWordResponse
}