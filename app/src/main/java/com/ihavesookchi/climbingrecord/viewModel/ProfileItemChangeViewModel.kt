package com.ihavesookchi.climbingrecord.viewModel

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.service.autofill.UserData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.data.repository.UserDataRepository
import com.ihavesookchi.climbingrecord.data.response.UserDataResponse
import com.ihavesookchi.climbingrecord.data.uistate.UserDataUiState
import com.ihavesookchi.climbingrecord.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class ProfileItemChangeViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository
): ViewModel() {
    private var _userDataUiState: SingleLiveEvent<UserDataUiState> = SingleLiveEvent()
    val userDataUiState: SingleLiveEvent<UserDataUiState> get() = _userDataUiState

    private val CLASS_NAME = this::class.java.simpleName

    private var userDataResponse = userDataRepository.getUserData()
    private var selectedImage: Bitmap? = null

    fun changeCircularBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        // 정사각형 비트맵 생성
        val diameter  = min(width, height)
        val dstBitmap = Bitmap.createBitmap(diameter, diameter, Bitmap.Config.ARGB_8888)

        // 정사각형 비트맵에 대한 캔버스 생성
        val canvas = Canvas(dstBitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)  // 안티 에일리어싱 플래그 적용

        // 캔버스에 원 그리기
        val rect = Rect(0, 0, diameter, diameter)
        val rectF = RectF(rect)
        canvas.drawOval(rectF, paint)

        // PorterDuffXfermode를 적용하여 원 안의 비트맵 내용 만들기
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        // 리사이징된 비트맵을 중앙에 위치시키기 위한 좌표 계산
        val left = ((diameter - width) / 2).toFloat()
        val top = ((diameter - height) / 2).toFloat()

        canvas.drawBitmap(Bitmap.createScaledBitmap(bitmap, width, height, false), left, top, paint)

        setSelectedImage(dstBitmap)

        return dstBitmap
    }

    private fun setSelectedImage(bitmap: Bitmap) {
        selectedImage = bitmap
    }

    fun getSelectedImage(): Bitmap? = selectedImage

    fun bitmapToByteArray(): ByteArray? {
        return selectedImage?.let {
            val byteArrayOutputStream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            byteArrayOutputStream.toByteArray()
        }
    }

    fun updateUserData(instagramUserName: String, nickname: String) {
        userDataResponse = userDataResponse.apply {
            this.instagramUserName = instagramUserName
            this.nickname = nickname
        }
        uploadBitmapToFirebaseStorage(userDataResponse)
    }

    // firebase Storage 에 저장 처리
    private fun uploadBitmapToFirebaseStorage(userData: UserDataResponse) {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    bitmapToByteArray()?.let {
                        // Profile Image Firebase Storage 에 저장
                        userDataRepository.uploadProfileImageBitmapToFirebaseStorage(it).let {
                            launch(Dispatchers.Main) {
                                ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "uploadBitmapToFirebaseStorage() Firebase Storage Api Success   image uri : ${it?.result}")

                                if (it?.isSuccessful == true) {
                                    userDataResponse = userData.apply {
                                        profileImage = it.result.toString()

                                        updateUserDataToFirebaseDB(userDataResponse)
                                    }
                                } else
                                    _userDataUiState.value = UserDataUiState.UserDataFailure

                            }
                        }
                    }?: run {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "uploadBitmapToFirebaseStorage() image null   userData : $userData")

                        updateUserDataToFirebaseDB(userData)
                    }
                } catch (e: Exception) {
                    handleUserDataError(::uploadBitmapToFirebaseStorage.name, e)
                }
            }
    }

    // firebase db에 저장 처리
    private fun updateUserDataToFirebaseDB(userDataResponse: UserDataResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userDataRepository.updateUserData(userDataResponse).let {
                    launch(Dispatchers.Main) {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "updateUserDataToFirebaseDB() User data Update    Update Result : ${it?.isSuccessful}")

                        if (it?.isSuccessful == true)
                            _userDataUiState.value = UserDataUiState.UserDataUpdateSuccess
                        else
                            _userDataUiState.value = UserDataUiState.UserDataFailure
                    }
                }
            } catch (e: Exception) {
                handleUserDataError(::updateUserDataToFirebaseDB.name, e)
            }
        }
    }

    fun deleteBitmapToFirebaseStorage() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userDataRepository.deleteProfileImageBitmapToFirebaseStorage().let {
                    launch(Dispatchers.Main) {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "deleteBitmapToFirebaseStorage() Firebase Storage Api Success   image uri : ${it?.result}")

                        if (it?.isSuccessful == true) {
                            userDataResponse = userDataResponse.apply {
                                profileImage = ""
                            }
                            updateUserDataToFirebaseDB(userDataResponse)
                        } else
                            _userDataUiState.value = UserDataUiState.UserDataFailure
                    }
                }
            } catch (e: Exception) {
                handleUserDataError(::deleteBitmapToFirebaseStorage.name, e)
            }
        }
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
}