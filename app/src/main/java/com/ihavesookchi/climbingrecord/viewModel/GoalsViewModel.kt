package com.ihavesookchi.climbingrecord.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.data.repository.GoalsDataRepository
import com.ihavesookchi.climbingrecord.data.response.GoalsDataResponse
import com.ihavesookchi.climbingrecord.data.uistate.GoalsDataUiState
import com.ihavesookchi.climbingrecord.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val goalsDataRepository: GoalsDataRepository
): ViewModel() {
    private var _goalsDataUiState: SingleLiveEvent<GoalsDataUiState> = SingleLiveEvent()
    val goalsDataUiState: SingleLiveEvent<GoalsDataUiState> get() = _goalsDataUiState

    private val CLASS_NAME = this::class.java.simpleName


    fun goalsApi() {
        viewModelScope.launch(Dispatchers.IO) {
            goalsDataRepository.goalsDataApi().let {
                launch(Dispatchers.Main) {
                    try {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "Goals Api Success    DocumentSnapshot : $it")

                        goalsDataRepository.setGoalsData(it!!)

                        _goalsDataUiState.value = GoalsDataUiState.GoalsDataSuccess

                    } catch (nullPointerException: NullPointerException) {
                        _goalsDataUiState.value = GoalsDataUiState.GoalsDataIsNull

                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "Unexpected null value    nullException : $nullPointerException")
                    } catch(e: Exception) {
                        _goalsDataUiState.value = GoalsDataUiState.GoalsDataFailure

                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "Other exception    exception : $e")
                    }
                }
            }
        }
    }

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

    fun getTrackingClimbingRecords(): List<GoalsDataResponse.TrackingClimbingRecord> = goalsDataRepository.getTrackingClimbingRecords()

    fun getGoalsAchievementStatus(): GoalsDataResponse.GoalsAchievementStatus = goalsDataRepository.getGoalsAchievementStatus()
}