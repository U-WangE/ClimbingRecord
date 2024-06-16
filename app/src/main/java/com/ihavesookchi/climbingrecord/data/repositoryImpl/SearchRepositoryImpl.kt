package com.ihavesookchi.climbingrecord.data.repositoryImpl

import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
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

    private val db = Firebase.firestore
    private val firebaseAuth = FirebaseAuth.getInstance()

    override suspend fun searchKeywordApi(keyword: String): Response<SearchKeywordResponse> {
        return kakaoRetrofit.searchKeywordApi(keyword = keyword)
    }

    override suspend fun getClimbingCenterRecord(center: SearchKeywordResponse.Document): Task<DocumentSnapshot>? {
        return null //TODO::차후 수정
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
}