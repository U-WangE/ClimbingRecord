package com.ihavesookchi.climbingrecord.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BaseViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val searchRepository: SearchRepository
) : ViewModel() {
    private var _userDataUiState: MutableLiveData<UserDataUiState> = MutableLiveData()
    val userDataUiState: LiveData<UserDataUiState> get() = _userDataUiState

    private val CLASS_NAME = this::class.java.simpleName

    private fun initUserData() {
        userDataRepository.initUserData()
    }

    private suspend fun handleUserDataError(logMessage: String, exception: Exception) {
        withContext(Dispatchers.Main) {
            ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "$logMessage    exception : $exception")
            _userDataUiState.value = when (exception) {
                is IllegalStateException -> UserDataUiState.AttemptLimitExceeded
                else -> UserDataUiState.UserDataFailure
            }
        }
    }

    fun getUserDataFromFirebaseDB() {
        viewModelScope.launch(Dispatchers.IO) {
            initUserData()

            try {
                userDataRepository.getUserDataFromFirebaseDB().let {
                    launch(Dispatchers.Main) {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "getFirebaseUserData() get User Data Api isSuccess    DocumentSnapshot : ${it?.result}")

                        if (it?.isSuccessful == true) {
                            if (it.result.exists()) {
                                userDataRepository.setUserData(it.result, null)
                                _userDataUiState.value = UserDataUiState.UserDataSuccess
                            } else
                                initUserDataToFirebaseDB()
                        } else
                            _userDataUiState.value = UserDataUiState.UserDataUpdateFailure
                    }
                }
            } catch (e: Exception) {
                handleUserDataError(::getUserDataFromFirebaseDB.name, e)
            }
        }
    }

    // 처음 앱을 사용한 경우 Firebase DB에 Default 사용자 데이터를 저장한다.
    private fun initUserDataToFirebaseDB() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userDataRepository.initUserDataToFirebaseDB().let {
                    launch(Dispatchers.Main) {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "initUserDataToFirebaseDB() set User Data Api    DocumentSnapshot : ${it?.result}")

                        if (it?.isSuccessful == true)
                            getUserDataFromFirebaseDB()
                        else
                            _userDataUiState.value = UserDataUiState.UserDataFailure
                    }
                }
            } catch (e: Exception) {
                handleUserDataError(::initUserDataToFirebaseDB.name, e)
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
    fun getProfileImage(): String {
        return getUserData().profileImage
    }
}