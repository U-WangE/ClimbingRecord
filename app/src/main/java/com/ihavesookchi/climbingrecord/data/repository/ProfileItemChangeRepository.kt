package com.ihavesookchi.climbingrecord.data.repository

import android.net.Uri
import com.ihavesookchi.climbingrecord.data.response.UserDataResponse

interface ProfileItemChangeRepository {
    fun uploadBitmapToFirebaseStorage(byteArray: ByteArray, completeCallback: (Uri?) -> Unit)
    suspend fun updateUserData(userDataResponse: UserDataResponse): Void?
}