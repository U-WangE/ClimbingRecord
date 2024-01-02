package com.ihavesookchi.climbingrecord.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihavesookchi.climbingrecord.data.repository.SearchRepository
import com.ihavesookchi.climbingrecord.data.uistate.SearchKeywordUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(
    private val searchRepository: SearchRepository
): ViewModel() {

    fun searchKeywordApi(keyword: String) {
        viewModelScope.launch {
            searchRepository.searchKeywordApi(keyword)
                .collect { uiState ->
                    when (uiState) {
                        is SearchKeywordUiState.SearchKeywordSuccess -> {

                        }

                        is SearchKeywordUiState.SearchKeywordFailure -> {
                        }
                        else -> {}
                    }
                }
        }
    }
}