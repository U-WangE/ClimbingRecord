package com.ihavesookchi.climbingrecord.data.repositoryImpl

import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.data.KakaoApi
import com.ihavesookchi.climbingrecord.data.repository.SearchRepository
import com.ihavesookchi.climbingrecord.data.response.SearchKeywordResponse
import retrofit2.Response
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val kakaoRetrofit: KakaoApi
): SearchRepository {
    private var searchKeywordResponse: SearchKeywordResponse? = null
    private val CLASS_NAME = this::class.java.simpleName

    override suspend fun searchKeywordApi(keyword: String): Response<SearchKeywordResponse> {
        return kakaoRetrofit.searchKeywordApi(keyword = keyword)
    }

    override fun setSearchKeywordResponse(searchKeywordResponse: SearchKeywordResponse?) {
        this.searchKeywordResponse = searchKeywordResponse
        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "Set SearchKeywordResponse Done    searchKeywordResponse  :  ${this.searchKeywordResponse}")
    }

    override fun getClimbingCenters(): List<SearchKeywordResponse.Document> {
        return searchKeywordResponse?.document ?: emptyList()
    }
}