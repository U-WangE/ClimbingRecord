package com.ihavesookchi.climbingrecord.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.data.repository.SearchRepository
import com.ihavesookchi.climbingrecord.data.repository.UserDataRepository
import com.ihavesookchi.climbingrecord.data.response.GoalsDataResponse
import com.ihavesookchi.climbingrecord.data.response.UserDataResponse
import com.ihavesookchi.climbingrecord.data.uistate.UserDataUiState
import com.ihavesookchi.climbingrecord.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val searchRepository: SearchRepository
) : ViewModel() {
    private var _userDataUiState: SingleLiveEvent<UserDataUiState> = SingleLiveEvent()
    val userDataUiState: SingleLiveEvent<UserDataUiState> get() = _userDataUiState

    private val CLASS_NAME = this::class.java.simpleName

    private fun initUserData() {
        userDataRepository.initUserData()
    }

    fun getFirebaseUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            initUserData()

            userDataRepository.userDataApi().let {
                launch(Dispatchers.Main) {
                    try {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "getFirebaseUserData() get User Data Api Success    DocumentSnapshot : $it")

                        userDataRepository.setUserData(it!!)

                        _userDataUiState.value = UserDataUiState.UserDataSuccess

                    } catch (nullPointerException: NullPointerException) {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "getFirebaseUserData() Unexpected null value    nullException : $nullPointerException")

                        setFirebaseUserData()
                    } catch (e: Exception) {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "getFirebaseUserData() Other exception    exception : $e")

                        _userDataUiState.value = UserDataUiState.UserDataFailure
                    }
                }
            }
        }
    }

    private fun setFirebaseUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            userDataRepository.setFirebaseUserData().let {
                launch(Dispatchers.Main) {
                    try {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "setFirebaseUserData() set User Data Api    DocumentSnapshot : $it")

                        _userDataUiState.value = it
                    } catch (e: Exception) {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "setFirebaseUserData() Other exception    exception : $e")

                        _userDataUiState.value = UserDataUiState.UserDataFailure
                    }
                }
            }
        }
    }

    fun getSelectedClimbingCenter() {
        searchRepository.getSelectedClimbingCenter()
    }

    fun getGoalsAchievementData(): GoalsDataResponse.GoalsAchievementStatus {
        return TODO("Provide the return value")
    }

    /**
     * Profile
     **/
    private fun getUserData(): UserDataResponse = userDataRepository.getUserData()

    fun getInstagramUserName(): String = getUserData().instagramUserName
    fun getNickName(): String = getUserData().nickname
    fun getProfileImage() {

    }
}