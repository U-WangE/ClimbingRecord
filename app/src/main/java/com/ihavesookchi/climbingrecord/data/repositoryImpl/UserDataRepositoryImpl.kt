package com.ihavesookchi.climbingrecord.data.repositoryImpl

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.data.repository.UserDataRepository
import com.ihavesookchi.climbingrecord.data.response.UserDataResponse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(

): UserDataRepository {
    private var userDataResponse: UserDataResponse = UserDataResponse()
    private val CLASS_NAME = this::class.java.simpleName

    private val db = Firebase.firestore
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun initUserData() { userDataResponse = UserDataResponse() }

    override suspend fun userDataApi(): DocumentSnapshot? {
        return db.collection("users")
            .document(firebaseAuth.currentUser?.uid ?: "anonymous")
            .get()
            .await()
    }

    override fun setUserData(documentSnapshot: DocumentSnapshot) {
        userDataResponse = documentSnapshot.toObject(UserDataResponse::class.java)!!

        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "Set GoalsDataResponse Done    goalsDataResponse  :  $userDataResponse")
    }

    override suspend fun setFirebaseUserData(): Void? {
        return db.collection("users")
            .document(firebaseAuth.currentUser?.uid ?: "anonymous")
            // 문서 존재 유무 판별에 실패한 경우 이전 데이터를 보호하기 위해 사용 (문서 전체 덮어쓰기 X, 병합 O)
            .set(UserDataResponse(), SetOptions.merge())
            .await()
    }

    override fun getUserData(): UserDataResponse = userDataResponse
}