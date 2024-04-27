package com.ihavesookchi.climbingrecord.viewModel

import androidx.lifecycle.ViewModel
import com.ihavesookchi.climbingrecord.data.repository.GoalsDataRepository
import com.ihavesookchi.climbingrecord.data.repository.UserDataRepository
import com.ihavesookchi.climbingrecord.data.uistate.GoalsDataUiState
import com.ihavesookchi.climbingrecord.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GoalsAchievementSettingViewModel @Inject constructor(
): ViewModel() {
    private var _goalsAchievementDataUiState: SingleLiveEvent<GoalsDataUiState> = SingleLiveEvent()
    val goalsAchievementDataUiState: SingleLiveEvent<GoalsDataUiState> get() = _goalsAchievementDataUiState

    private val CLASS_NAME = this::class.java.simpleName


}