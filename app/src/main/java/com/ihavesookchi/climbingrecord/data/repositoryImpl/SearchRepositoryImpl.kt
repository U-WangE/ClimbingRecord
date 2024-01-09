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

    private var selectedClimbingCenter: SearchKeywordResponse.Document? = null

    private val CLASS_NAME = this::class.java.simpleName

    override suspend fun searchKeywordApi(keyword: String): Response<SearchKeywordResponse> {
        return kakaoRetrofit.searchKeywordApi(keyword = keyword)
    }

    override fun setSearchKeywordResponse(searchKeywordResponse: SearchKeywordResponse?) {
        this.searchKeywordResponse = searchKeywordResponse
        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "Set SearchKeywordResponse Done    searchKeywordResponse  :  ${this.searchKeywordResponse}")
    }

    override fun getSearchData(): List<SearchKeywordResponse.Document> {
        return searchKeywordResponse?.documents ?: emptyList()
    }

    override fun removeSearchData() {
        searchKeywordResponse = null
    }

    override fun setSelectedClimbingCenter(selectedClimbingCenter: SearchKeywordResponse.Document) {
        this.selectedClimbingCenter = selectedClimbingCenter
    }

    override fun getSelectedClimbingCenter(): SearchKeywordResponse.Document? {
        return this.selectedClimbingCenter
    }
}