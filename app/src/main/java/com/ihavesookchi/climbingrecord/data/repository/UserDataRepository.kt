package com.ihavesookchi.climbingrecord.data.repository

import com.google.firebase.firestore.DocumentSnapshot
import com.ihavesookchi.climbingrecord.data.response.UserDataResponse
import com.ihavesookchi.climbingrecord.data.uistate.UserDataUiState

interface UserDataRepository {
    fun initUserData()
    suspend fun userDataApi(): DocumentSnapshot?
    fun setUserData(documentSnapshot: DocumentSnapshot)
    fun setUserData(userDataResponse: UserDataResponse)
    suspend fun setFirebaseUserData(): UserDataUiState
    fun getUserData(): UserDataResponse
}