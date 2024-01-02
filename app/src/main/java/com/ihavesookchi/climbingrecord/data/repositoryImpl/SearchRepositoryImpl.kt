package com.ihavesookchi.climbingrecord.data.repositoryImpl

import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.data.KakaoApi
import com.ihavesookchi.climbingrecord.data.repository.SearchRepository
import com.ihavesookchi.climbingrecord.data.response.SearchKeywordResponse
import com.ihavesookchi.climbingrecord.data.uistate.SearchKeywordUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val kakaoRetrofit: KakaoApi
): SearchRepository {
    private var searchKeywordResponse: SearchKeywordResponse? = null
    private val CLASS_NAME = this::class.java.simpleName

    override fun searchKeywordApi(keyword: String): Flow<SearchKeywordUiState> {
        return flow {
            ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "searchKeyWordApi() flow start")

            try {
                val response = kakaoRetrofit.searchKeywordApi(keyword = keyword)

                when (response.code()) {
                    200 -> {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "kakao search Keyword api Success    response  :  $response")

                        withContext(Dispatchers.IO) {
                            searchKeywordResponse = response.body()

                            ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "kakao search Keyword response Saved    searchKeywordResponse  :  $searchKeywordResponse")
                        }

                        emit(SearchKeywordUiState.SearchKeywordSuccess)
                    }
                    else -> {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "kakao search Keyword api Else    response : $response")

                        searchKeywordResponse = null

                        emit(SearchKeywordUiState.SearchKeywordFailure)
                    }
                }
            } catch (e: Exception) {
                ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "kakao search Keyword api exception    error : $e")

                searchKeywordResponse = null

                emit(SearchKeywordUiState.SearchKeywordFailure)
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getClimbingCenters(): List<SearchKeywordResponse.Document> {
        return searchKeywordResponse?.document ?: emptyList()
    }
}