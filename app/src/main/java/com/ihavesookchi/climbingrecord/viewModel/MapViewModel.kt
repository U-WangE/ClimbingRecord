package com.ihavesookchi.climbingrecord.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.data.repository.SearchRepository
import com.ihavesookchi.climbingrecord.data.uistate.SearchKeywordUiState
import com.ihavesookchi.climbingrecord.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val searchRepository: SearchRepository
): ViewModel() {
    private var _searchKeywordUiState: SingleLiveEvent<SearchKeywordUiState> = SingleLiveEvent()
    val searchKeywordUiState: SingleLiveEvent<SearchKeywordUiState> get() = _searchKeywordUiState

    private val CLASS_NAME = this::class.java.simpleName

    fun searchKeywordApi(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchRepository.searchKeywordApi(keyword).let {
                launch(Dispatchers.Main) {
                    if (it.isSuccessful) {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "Search Keyword Api Success    response  :  ${it.body()}")

                        withContext(Dispatchers.IO) {
                            searchRepository.setSearchKeywordResponse(it.body())
                        }

                        _searchKeywordUiState.value = SearchKeywordUiState.SearchKeywordSuccess
                    }
                    else {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "Search Keyword Api failure    response  :  ${it.body()}")

                        _searchKeywordUiState.value = SearchKeywordUiState.SearchKeywordFailure
                    }
                }
            }
        }
    }

}