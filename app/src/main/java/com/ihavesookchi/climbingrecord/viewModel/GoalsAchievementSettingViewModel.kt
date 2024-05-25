package com.ihavesookchi.climbingrecord.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.data.repository.GoalsDataRepository
import com.ihavesookchi.climbingrecord.data.response.GoalsDataResponse
import com.ihavesookchi.climbingrecord.data.uistate.GoalsAchievementUiState
import com.ihavesookchi.climbingrecord.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GoalsAchievementSettingViewModel @Inject constructor(
    private val goalsDataRepository: GoalsDataRepository
): ViewModel() {
    private var _goalsAchievementDataUiState: SingleLiveEvent<GoalsAchievementUiState> = SingleLiveEvent()
    val goalsAchievementDataUiState: SingleLiveEvent<GoalsAchievementUiState> get() = _goalsAchievementDataUiState

    private val CLASS_NAME = this::class.java.simpleName

    private var goalsAchievementStatus = goalsDataRepository.getGoalsAchievementStatus()

    private suspend fun handleGoalAchievementDataError(logMessage: String, exception: Exception) {
        withContext(Dispatchers.Main) {
            ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "$logMessage    exception : $exception")
            _goalsAchievementDataUiState.value = when (exception) {
                is IllegalStateException -> GoalsAchievementUiState.AttemptLimitExceeded
                else -> GoalsAchievementUiState.GoalsAchievementFailure
            }
        }
    }

    fun uploadGoalAchievementDataToFirebaseDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                goalsDataRepository.uploadGoalAchievementDataToFirebaseDB(goalsAchievementStatus).let {
                    launch(Dispatchers.Main) {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "uploadGoalAchievementDataToFirebaseDB() upload Goal Achievement Data Api    DocumentSnapshot : ${it?.result}")

                        _goalsAchievementDataUiState.value = if (it?.isSuccessful == true)
                            GoalsAchievementUiState.GoalsAchievementSuccess
                        else
                            GoalsAchievementUiState.GoalsAchievementFailure
                    }
                }
            } catch(e: Exception) {
                handleGoalAchievementDataError(::uploadGoalAchievementDataToFirebaseDB.name, e)
            }
        }
    }

    fun isValueEntered() {
        _goalsAchievementDataUiState.value =
            when {
                goalsAchievementStatus.endDate == null || goalsAchievementStatus.startDate == goalsAchievementStatus.endDate -> GoalsAchievementUiState.NotGoalPeriodSetting
                ((goalsAchievementStatus.endDate) - (goalsAchievementStatus.startDate)) < 0 -> GoalsAchievementUiState.UnusualGoalPeriod
                goalsAchievementStatus.goalDetails.size == 0 -> GoalsAchievementUiState.NotGoalSetting
                else -> {
                    GoalsAchievementUiState.GoalSettingSuccess
                }
            }
    }

    fun setGoal(goalDetailList: List<GoalsDataResponse.GoalsAchievementStatus.GoalDetail>) {
        val goalDetailList = goalDetailList as ArrayList

        goalDetailList.removeAll { it.goal == 0 || it.goalColorRGB.isBlank() }
        goalsAchievementStatus.goalDetails = goalDetailList
    }

    fun setStartDate(startDate: Long) {
        goalsAchievementStatus.startDate = startDate
    }
    fun setEndDate(endDate: Long) {
        goalsAchievementStatus.endDate = endDate
    }
    fun getStartDate(): Long {
        return goalsAchievementStatus.startDate
    }
    fun getEndDate(): Long {
        return goalsAchievementStatus.endDate
    }

    fun resetData() {
        goalsAchievementStatus = GoalsDataResponse.GoalsAchievementStatus()
    }

    fun getGoalDetails(): List<GoalsDataResponse.GoalsAchievementStatus.GoalDetail> {
        return goalsAchievementStatus.goalDetails
    }
}