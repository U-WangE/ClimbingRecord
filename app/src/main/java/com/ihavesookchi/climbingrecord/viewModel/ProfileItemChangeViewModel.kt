package com.ihavesookchi.climbingrecord.viewModel

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.data.repository.ProfileItemChangeRepository
import com.ihavesookchi.climbingrecord.data.repository.UserDataRepository
import com.ihavesookchi.climbingrecord.data.response.UserDataResponse
import com.ihavesookchi.climbingrecord.data.uistate.UserDataUiState
import com.ihavesookchi.climbingrecord.util.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.math.min

@HiltViewModel
class ProfileItemChangeViewModel @Inject constructor(
    private val profileItemChangeRepository: ProfileItemChangeRepository,
    private val userDataRepository: UserDataRepository
): ViewModel() {
    private var _userDataUiState: SingleLiveEvent<UserDataUiState> = SingleLiveEvent()
    val userDataUiState: SingleLiveEvent<UserDataUiState> get() = _userDataUiState

    private val CLASS_NAME = this::class.java.simpleName

    private var selectedImage: Bitmap? = null

    fun changeCircularBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        // 정사각형 비트맵 생성
        val squareBitmapWidth = min(width, height)
        val dstBitmap = Bitmap.createBitmap(squareBitmapWidth, squareBitmapWidth, Bitmap.Config.ARGB_8888)

        // 정사각형 비트맵에 대한 캔버스 생성
        val canvas = Canvas(dstBitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)  // 안티 에일리어싱 플래그 적용

        // 캔버스에 원 그리기
        val rect = Rect(0, 0, squareBitmapWidth, squareBitmapWidth)
        val rectF = RectF(rect)
        canvas.drawOval(rectF, paint)

        // PorterDuffXfermode를 적용하여 원 안의 비트맵 내용 만들기
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

        // 리사이징된 비트맵을 중앙에 위치시키기 위한 좌표 계산
        val left = ((squareBitmapWidth - width) / 2).toFloat()
        val top = ((squareBitmapWidth - height) / 2).toFloat()

        canvas.drawBitmap(Bitmap.createScaledBitmap(bitmap, width, height, false), left, top, paint)

        setSelectedImage(dstBitmap)

        return dstBitmap
    }

    fun setSelectedImage(bitmap: Bitmap?) {
        selectedImage = bitmap
    }

    fun bitmapToByteArray(): ByteArray? {
        return selectedImage?.let {
            val byteArrayOutputStream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            byteArrayOutputStream.toByteArray()
        }
    }

    fun updateUserData(nickname: String, instagramUserName: String) {
        val userDataResponse = userDataRepository.getUserData().apply {
            this.nickname = nickname
            this.instagramUserName = instagramUserName
        }
        uploadBitmapToFirebaseStorage(userDataResponse)
    }

    // firebase Storage에 저장 처리
    private fun uploadBitmapToFirebaseStorage(userData: UserDataResponse) {
        selectedImage?.let {
            bitmapToByteArray()?.let {
                profileItemChangeRepository.uploadBitmapToFirebaseStorage(it) { uri ->
                    val userDataResponse = userData.apply {
                        profileImage = (uri?:profileImage).toString()
                    }
                    updateUserDataToFirebaseDB(userDataResponse)
                }
            }
        }?: run {
            updateUserDataToFirebaseDB(userData)
        }
    }

    // firebase db에 저장 처리
    private fun updateUserDataToFirebaseDB(userDataResponse: UserDataResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            profileItemChangeRepository.updateUserData(userDataResponse).let {
                launch(Dispatchers.Main) {
                    Log.d(CLASS_NAME, it.toString())
                    try {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "setFirebaseUserData() User Api Success    DocumentSnapshot : $it")

                        _userDataUiState.value = UserDataUiState.UserDataUpdateSuccess

                    } catch (e: Exception) {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "setFirebaseUserData() Other exception    exception : $e")

                        _userDataUiState.value = UserDataUiState.UserDataFailure
                    }
                }
            }
        }
    }
}