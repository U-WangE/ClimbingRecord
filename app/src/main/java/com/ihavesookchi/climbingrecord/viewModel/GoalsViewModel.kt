package com.ihavesookchi.climbingrecord.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.data.repository.GoalsDataRepository
import com.ihavesookchi.climbingrecord.data.repository.UserDataRepository
import com.ihavesookchi.climbingrecord.data.response.GoalsDataResponse
import com.ihavesookchi.climbingrecord.data.uistate.GoalsDataUiState
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
            initData()

            try {
                goalsDataRepository.getGoalsDataFromFirebaseDB().let {
                    launch(Dispatchers.Main) {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "getFirebaseGoalsData() Goals Api Success    DocumentSnapshot : $it")

                        if (it?.isSuccessful == true) {
                            if (it.result.exists()) {
                                goalsDataRepository.setGoalsData(it.result)
                                _goalsDataUiState.value = GoalsDataUiState.GoalsDataSuccess
                            } else
                                _goalsDataUiState.value = GoalsDataUiState.GoalsDataIsNull
                        } else
                            _goalsDataUiState.value = GoalsDataUiState.GoalsDataFailure
                    }
                }
            } catch (e: Exception) {
                handleGoalsDataError(::getFirebaseGoalsData.name, e)
            }
        }
    }

    fun getTrackingClimbingRecords(): List<GoalsDataResponse.TrackingClimbingRecord> = goalsDataRepository.getTrackingClimbingRecords()

    fun getGoalDetails(): List<GoalsDataResponse.GoalsAchievementStatus.GoalDetail> = goalsDataRepository.getGoalDetails()
    fun getStartDate(): Calendar {
        return Calendar.getInstance().apply {
            timeInMillis = goalsDataRepository.getStartDate()
        }
    }

    fun getEndDate(): Calendar {
        return Calendar.getInstance().apply {
            timeInMillis = goalsDataRepository.getEndDate()
        }
    }

    fun getGoalsDDay(): Long = TimeUnit.MILLISECONDS.toDays(goalsDataRepository.getEndDate() - System.currentTimeMillis())

    fun getGoalsAchievementStatus(): GoalsDataResponse.GoalsAchievementStatus = goalsDataRepository.getGoalsAchievementStatus()
}