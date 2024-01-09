package com.ihavesookchi.climbingrecord.viewModel

import androidx.lifecycle.ViewModel
import com.ihavesookchi.climbingrecord.data.response.GoalsDataResponse
import com.ihavesookchi.climbingrecord.data.uistate.GoalsDataUiState
import com.ihavesookchi.climbingrecord.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class GoalsViewModel: ViewModel() {
    private var _goalsDataUiState: SingleLiveEvent<GoalsDataUiState> = SingleLiveEvent()
    val goalsDataUiState: SingleLiveEvent<GoalsDataUiState> get() = _goalsDataUiState

    private val CLASS_NAME = this::class.java.simpleName

    fun goalsApi() {

    }

    fun getGoalsDetails(): List<GoalsDataResponse.GoalsAchievementStatus.GoalDetail> {
        return TODO("")
    }

    fun getStartDate(): Long {
        return TODO("")
    }
    fun getEndDate(): Long {
        return TODO("")
    }

}