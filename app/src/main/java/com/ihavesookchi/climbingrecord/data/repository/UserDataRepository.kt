package com.ihavesookchi.climbingrecord.data.repository

import com.google.firebase.firestore.DocumentSnapshot

interface UserDataRepository {
    fun initUserData()
    suspend fun userDataApi(): DocumentSnapshot?
    fun setUserData(documentSnapshot: DocumentSnapshot)
    suspend fun setFirebaseUserData(): Void?
}