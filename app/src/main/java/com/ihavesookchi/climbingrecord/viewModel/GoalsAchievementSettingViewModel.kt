package com.ihavesookchi.climbingrecord.viewModel

import androidx.lifecycle.ViewModel
import com.ihavesookchi.climbingrecord.data.repository.GoalsDataRepository
import com.ihavesookchi.climbingrecord.data.uistate.GoalsDataUiState
import com.ihavesookchi.climbingrecord.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GoalsAchievementSettingViewModel @Inject constructor(
    private val goalsDataRepository: GoalsDataRepository
): ViewModel() {
    private var _goalsAchievementDataUiState: SingleLiveEvent<GoalsDataUiState> = SingleLiveEvent()
    val goalsAchievementDataUiState: SingleLiveEvent<GoalsDataUiState> get() = _goalsAchievementDataUiState

    private val CLASS_NAME = this::class.java.simpleName

    private var startDate: Long? = null
    private var endDate: Long? = null

    fun initStartAndEndDate() {
        startDate = goalsDataRepository.getStartDate()
        endDate = goalsDataRepository.getEndDate()
    }

    fun setStartDate(startDate: Long?) {
        this.startDate = startDate
    }
    fun setEndDate(endDate: Long?) {
        this.endDate = endDate
    }
    fun getStartDate(): Long? {
        return startDate
    }
    fun getEndDate(): Long? {
        return endDate
    }
}