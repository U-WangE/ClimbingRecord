package com.ihavesookchi.climbingrecord.data.repository

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.ihavesookchi.climbingrecord.data.response.UserDataResponse

interface UserDataRepository {
    fun initUserData()
    suspend fun getUserDataFromFirebaseDB(): Task<DocumentSnapshot>?
    fun setUserData(documentSnapshot: DocumentSnapshot? = null, userDataResponse: UserDataResponse? = null)
    suspend fun initUserDataToFirebaseDB(): Task<Void>?
    fun getUserData(): UserDataResponse
    suspend fun updateUserData(userDataResponse: UserDataResponse): Task<Void>?
    suspend fun uploadProfileImageBitmapToFirebaseStorage(byteArray: ByteArray): Task<Uri>?
    suspend fun deleteProfileImageBitmapToFirebaseStorage(): Task<Void>?
}