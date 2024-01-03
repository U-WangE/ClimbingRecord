package com.ihavesookchi.climbingrecord.viewModel

import androidx.lifecycle.ViewModel
import com.ihavesookchi.climbingrecord.data.repository.SearchRepository
import com.ihavesookchi.climbingrecord.data.response.SearchKeywordResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(
    private val searchRepository: SearchRepository
): ViewModel() {
    private val CLASS_NAME = this::class.java.simpleName

    fun getClimbingCenters(): List<SearchKeywordResponse.Document> {
        return searchRepository.getClimbingCenters()
    }
}