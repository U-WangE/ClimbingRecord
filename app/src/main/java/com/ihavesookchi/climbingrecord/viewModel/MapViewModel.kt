package com.ihavesookchi.climbingrecord.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.data.repository.SearchRepository
import com.ihavesookchi.climbingrecord.data.response.SearchKeywordResponse
import com.ihavesookchi.climbingrecord.data.uistate.ClimbingCenterRecordUiState
import com.ihavesookchi.climbingrecord.data.uistate.SearchUiState
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
    private var _searchUiState: SingleLiveEvent<SearchUiState> = SingleLiveEvent()
    val searchUiState: SingleLiveEvent<SearchUiState> get() = _searchUiState

    private var _climbingCenterRecordUiState: SingleLiveEvent<ClimbingCenterRecordUiState> = SingleLiveEvent()
    val climbingCenterRecordUiState: SingleLiveEvent<ClimbingCenterRecordUiState> get() = _climbingCenterRecordUiState

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

                        _searchUiState.value = SearchUiState.SearchSuccess
                    }
                    else {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "Search Keyword Api failure    response  :  ${it.body()}")

                        _searchUiState.value = SearchUiState.SearchFailure
                    }
                }
            }
        }
    }

    fun getSearchData(): List<SearchKeywordResponse.Document> {
        return searchRepository.getSearchData()
    }

    fun removeSearchData() {
        searchRepository.removeSearchData()
    }

    fun getClimbingCenterRecord(climbingCenter: SearchKeywordResponse.Document) {
        viewModelScope.launch(Dispatchers.IO) {
            searchRepository.getClimbingCenterRecord(climbingCenter).let {
                launch(Dispatchers.Main) {
                    if (it?.isSuccessful == true) {
                        //TODO::Firebase Function 추가 완료 후 개발할 기능
                        _climbingCenterRecordUiState
                    }
                }
            }
        }
    }
}