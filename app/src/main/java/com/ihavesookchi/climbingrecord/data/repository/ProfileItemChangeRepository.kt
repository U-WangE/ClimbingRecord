package com.ihavesookchi.climbingrecord.data.repository

import android.net.Uri
import com.ihavesookchi.climbingrecord.data.dataState.UpdateDataState
import com.ihavesookchi.climbingrecord.data.response.UserDataResponse
import com.ihavesookchi.climbingrecord.data.uistate.UserDataUiState

interface ProfileItemChangeRepository {
    fun uploadBitmapToFirebaseStorage(byteArray: ByteArray, completeCallback: (Uri?) -> Unit)
    suspend fun updateUserData(userDataResponse: UserDataResponse): UpdateDataState
}