package com.ihavesookchi.climbingrecord.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.data.repository.RecordDataRepository
import com.ihavesookchi.climbingrecord.data.uistate.RecordsDataUiState
import com.ihavesookchi.climbingrecord.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RecordListViewModel @Inject constructor(
    private val recordDataRepository: RecordDataRepository
): ViewModel() {
    private var _recordListDataUiState: SingleLiveEvent<RecordsDataUiState> = SingleLiveEvent()
    val recordsDataUiState: SingleLiveEvent<RecordsDataUiState> get() = _recordListDataUiState

    private val CLASS_NAME = this::class.java.simpleName

    private suspend fun handleGoalsDataError(logMessage: String, exception: Exception) {
        withContext(Dispatchers.Main) {
            ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "$logMessage    exception : $exception")
            _recordListDataUiState.value = when (exception) {
                is IllegalStateException -> RecordsDataUiState.AttemptLimitExceeded
                else -> RecordsDataUiState.RecordsDataFailure
            }
        }
    }

    fun getFirebaseRecordListData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                recordDataRepository.getRecordListDataFromFirebaseDB().let {
                    launch(Dispatchers.Main) {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "getFirebaseRecordListData() Record List Api Success    DocumentSnapshot : ${it?.result?.documents}")

                        if (it?.isSuccessful == true)
                            if (it.result != null || !it.result.isEmpty) {
                                // set data
                                recordDataRepository.setRecordsDataResponse(it.result)
                                _recordListDataUiState.value = RecordsDataUiState.RecordsDataSuccess
                            } else {
                                // set init data
                                recordDataRepository.initRecordsDataResponse()
                            }
                        else
                            _recordListDataUiState.value = RecordsDataUiState.RecordsDataFailure
                    }
                }
            } catch (e: Exception) {
                handleGoalsDataError(::getFirebaseRecordListData.name, e)
            }
        }
    }
    fun getRecordList() = recordDataRepository.getRecordList()
}