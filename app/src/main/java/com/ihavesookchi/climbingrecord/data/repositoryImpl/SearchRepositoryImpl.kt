package com.ihavesookchi.climbingrecord.data.repositoryImpl

import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.data.KakaoApi
import com.ihavesookchi.climbingrecord.data.dao.ClimbingCenterDao
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
    private val kakaoRetrofit: KakaoApi,
    private val climbingCenterDao: ClimbingCenterDao
): SearchRepository {

    private val CLASS_NAME = this::class.java.simpleName

    override fun searchKeywordApi(keyword: String): Flow<SearchKeywordUiState> {
        return flow {
            ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "searchKeyWordApi() flow start")

            try {
                val response = kakaoRetrofit.searchKeywordApi(keyword = keyword)

                when (response.code()) {
                    200 -> {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "kakao search Keyword api success -> response : $response")

                        withContext(Dispatchers.IO) {
                            checkInsertData(response.body()!!)
                        }
                        emit(SearchKeywordUiState.SearchKeywordSuccess)
                    }
                    else -> {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "kakao search Keyword api else -> response : $response")

                        emit(SearchKeywordUiState.SearchKeywordFailure)
                    }
                }
            } catch (e: Exception) {
                ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "kakao search Keyword api exception -> error : $e")

                emit(SearchKeywordUiState.SearchKeywordFailure)
            }
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun checkInsertData(response: SearchKeywordResponse) {
        val documents = response.document
        climbingCenterDao.insertAllClimbingCenter(documents)
    }
}