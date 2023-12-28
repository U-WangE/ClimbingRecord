package com.ihavesookchi.climbingrecord.data.repositoryImpl

import com.ihavesookchi.climbingrecord.data.KakaoApi
import com.ihavesookchi.climbingrecord.data.repository.SearchRepository
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
    override fun searchKeywordApi(keyword: String): Flow<SearchKeywordUiState> {
        return flow {
            val response = kakaoRetrofit.searchKeywordApi(keyword = keyword)

            when (response.code()) {
                200 -> {
                    withContext(Dispatchers.IO) {
                        saveData(response)
                    }
                    emit(SearchKeywordUiState.SearchKeywordSuccess)
                }
                else -> {
                    emit(SearchKeywordUiState.SearchKeywordFailure)
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun <T> saveData(response: T) {
    }
}