package com.ihavesookchi.climbingrecord.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.data.repository.GoalsDataRepository
import com.ihavesookchi.climbingrecord.data.repository.UserDataRepository
import com.ihavesookchi.climbingrecord.data.response.GoalsDataResponse
import com.ihavesookchi.climbingrecord.data.uistate.GoalsDataUiState
import com.ihavesookchi.climbingrecord.util.CommonUtil.convertTimeMillisToCalendar
import com.ihavesookchi.climbingrecord.util.CommonUtil.getDDay
import com.ihavesookchi.climbingrecord.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val goalsDataRepository: GoalsDataRepository,
    private val userDataRepository: UserDataRepository
): ViewModel() {
    private var _goalsDataUiState: SingleLiveEvent<GoalsDataUiState> = SingleLiveEvent()
    val goalsDataUiState: SingleLiveEvent<GoalsDataUiState> get() = _goalsDataUiState

    private val CLASS_NAME = this::class.java.simpleName

    private fun initData() {
        goalsDataRepository.initResponse()
    }

    private suspend fun handleGoalsDataError(logMessage: String, exception: Exception) {
        withContext(Dispatchers.Main) {
            ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "$logMessage    exception : $exception")
            _goalsDataUiState.value = when (exception) {
                is IllegalStateException -> GoalsDataUiState.AttemptLimitExceeded
                else -> GoalsDataUiState.GoalsDataFailure
            }
        }
    }

    fun getFirebaseGoalsData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                goalsDataRepository.getGoalsDataFromFirebaseDB().let {
                    launch(Dispatchers.Main) {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "getFirebaseGoalsData() Goals Api Success    DocumentSnapshot : ${it?.result}")

                        if (it?.isSuccessful == true) {
                            if (it.result.exists()) {
                                goalsDataRepository.setGoalsData(it.result)
                                _goalsDataUiState.value = GoalsDataUiState.GoalsDataSuccess
                            } else {
                                // db 에 값이 없는 경우 init 초기화 해주는 코드
                                initGoalsDataFromFirebaseDB()
                            }
                        } else
                            _goalsDataUiState.value = GoalsDataUiState.GoalsDataFailure
                    }
                }
            } catch (e: Exception) {
                handleGoalsDataError(::getFirebaseGoalsData.name, e)
            }
        }
    }

    private fun initGoalsDataFromFirebaseDB() {
        viewModelScope.launch(Dispatchers.IO) {
            goalsDataRepository.initGoalsDataFromFirebaseDB().let {
                launch(Dispatchers.Main) {
                    if (it?.isSuccessful == true) {
                        goalsDataRepository.setGoalsData()
                        _goalsDataUiState.value = GoalsDataUiState.GoalsDataSuccess
                    } else {
                        _goalsDataUiState.value = GoalsDataUiState.GoalsDataFailure
                    }
                }
            }
        }
    }

    fun getGoalsData(): GoalsDataResponse {
        return goalsDataRepository.getGoalsData()
    }

    fun getTrackingClimbingRecords(): List<GoalsDataResponse.TrackingClimbingRecord> = goalsDataRepository.getTrackingClimbingRecords()

    fun getGoalDetails(): List<GoalsDataResponse.GoalsAchievementStatus.GoalDetail> = goalsDataRepository.getGoalDetails()
    fun getStartDate(): Calendar {
        return convertTimeMillisToCalendar(goalsDataRepository.getStartDate()?:System.currentTimeMillis())
    }

    fun getEndDate(): Calendar {
        return convertTimeMillisToCalendar(goalsDataRepository.getEndDate()?:System.currentTimeMillis())
    }

    fun getGoalsDDay(): Long = getDDay(goalsDataRepository.getEndDate()?:System.currentTimeMillis(), System.currentTimeMillis())

    fun getGoalsAchievementStatus(): GoalsDataResponse.GoalsAchievementStatus = goalsDataRepository.getGoalsAchievementStatus()
}