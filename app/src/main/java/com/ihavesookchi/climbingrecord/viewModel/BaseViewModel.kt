package com.ihavesookchi.climbingrecord.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.data.repository.SearchRepository
import com.ihavesookchi.climbingrecord.data.response.SearchKeywordResponse
import com.ihavesookchi.climbingrecord.data.uistate.SearchKeywordUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(
    private val searchRepository: SearchRepository
): ViewModel() {
        private var _searchKeywordUiState: MutableLiveData<SearchKeywordUiState> = MutableLiveData()
        val searchKeywordUiState: MutableLiveData<SearchKeywordUiState> get() = _searchKeywordUiState

    fun searchKeywordApi(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchRepository.searchKeywordApi(keyword).let {
                launch(Dispatchers.Main) {
                    if (it.isSuccessful) {
                        withContext(Dispatchers.IO) {
                            searchRepository.setSearchKeywordResponse(it.body())
                        }
                        _searchKeywordUiState.value = SearchKeywordUiState.SearchKeywordSuccess
                    }
                    else {
                        _searchKeywordUiState.value = SearchKeywordUiState.SearchKeywordFailure
                    }
                }
            }
        }
    }
}