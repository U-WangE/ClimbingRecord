package com.ihavesookchi.climbingrecord.viewModel

import android.util.Log
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
import java.lang.NumberFormatException
import java.util.LinkedList
import javax.inject.Inject

@HiltViewModel
class GoalsAchievementSettingViewModel @Inject constructor(
    private val goalsDataRepository: GoalsDataRepository
): ViewModel() {
    private var _goalsAchievementDataUiState: SingleLiveEvent<GoalsAchievementUiState> = SingleLiveEvent()
    val goalsAchievementDataUiState: SingleLiveEvent<GoalsAchievementUiState> get() = _goalsAchievementDataUiState

    private val CLASS_NAME = this::class.java.simpleName

    private var goalsAchievementStatus = goalsDataRepository.getGoalsAchievementStatus()

    private var linkedHashMap = LinkedHashMap<Int, GoalsDataResponse.GoalsAchievementStatus.GoalDetail>()

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

    fun isValueEntered(
        goalOneGoal: String,
        goalTwoGoal: String
    ) {
        setGoal(goalOneGoal, goalTwoGoal)

        if (linkedHashMap.size > 0) {
            val iterator = linkedHashMap.iterator()
            var goalDetails = arrayListOf<GoalsDataResponse.GoalsAchievementStatus.GoalDetail>()
            while(iterator.hasNext()) {
                val entry = iterator.next()

                if (entry.value.goal != 0 && entry.value.goalColorRGB.isNotBlank())
                    goalDetails.add(entry.value)
            }
            goalsAchievementStatus.goalDetails = goalDetails
        } else {
            _goalsAchievementDataUiState.value = GoalsAchievementUiState.NotGoalSetting
            return
        }

        _goalsAchievementDataUiState.value =
            when {
                goalsAchievementStatus.startDate == null || goalsAchievementStatus.endDate == null -> GoalsAchievementUiState.NotGoalPeriodSetting
                ((goalsAchievementStatus.endDate?:0) - (goalsAchievementStatus.startDate?:0)) < 0 -> GoalsAchievementUiState.UnusualGoalPeriod
                goalsAchievementStatus.goalDetails.size == 0 -> GoalsAchievementUiState.NotGoalSetting
                else -> {
                    GoalsAchievementUiState.GoalSettingSuccess
                }
            }
    }

    fun initLinkedHashMap() {
        goalsAchievementStatus.goalDetails.forEachIndexed { index, goalDetail ->
            linkedHashMap[index] = goalDetail
        }
    }

    fun getLinkedHashMap(): LinkedHashMap<Int, GoalsDataResponse.GoalsAchievementStatus.GoalDetail> {
        return linkedHashMap
    }

    fun setGoalColor(goalNumber: Int, colorHex: String?) {
        if (colorHex == null)
            linkedHashMap.remove(goalNumber)
        else if (linkedHashMap.containsKey(goalNumber))
            linkedHashMap[goalNumber]?.goalColorRGB = colorHex
        else
            linkedHashMap[goalNumber] = GoalsDataResponse.GoalsAchievementStatus.GoalDetail(goalColorRGB = colorHex)
    }

    fun setGoal(goalOne: String, goalTwo: String) {
        try {
            when {
                goalOne.isBlank() || goalOne.toInt() == 0 -> linkedHashMap.remove(0)
                linkedHashMap.containsKey(0) -> linkedHashMap[0]?.goal = goalOne.toInt()
            }
            when {
                goalTwo.isBlank() || goalTwo.toInt() == 0 -> linkedHashMap.remove(1)
                linkedHashMap.containsKey(1) -> linkedHashMap[1]?.goal = goalTwo.toInt()
            }
        } catch (e: NumberFormatException) {
            ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "setGoal(goalNumber, goal) goal.toInt   NumberFormatException  :  $e")
        }
    }

    fun setStartDate(startDate: Long?) {
        goalsAchievementStatus.startDate = startDate
    }
    fun setEndDate(endDate: Long?) {
        goalsAchievementStatus.endDate = endDate
    }
    fun getStartDate(): Long? {
        return goalsAchievementStatus.startDate
    }
    fun getEndDate(): Long? {
        return goalsAchievementStatus.endDate
    }

    fun resetData() {
        goalsAchievementStatus = GoalsDataResponse.GoalsAchievementStatus()
        linkedHashMap = LinkedHashMap<Int, GoalsDataResponse.GoalsAchievementStatus.GoalDetail>()
    }
}