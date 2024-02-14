package com.ihavesookchi.climbingrecord.data.repositoryImpl

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.data.repository.UserDataRepository
import com.ihavesookchi.climbingrecord.data.response.UserDataResponse
import com.ihavesookchi.climbingrecord.data.uistate.UserDataUiState
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
        return try {
            db.collection("users")
                .document(firebaseAuth.currentUser?.uid ?: "anonymous")
                .get()
                .await()
        } catch (e: FirebaseFirestoreException) {
            // storage rule 위반
            ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "userDataApi()   Firebase FireStore Permission Denied  :  $e")
            null
        }
    }

    override fun setUserData(documentSnapshot: DocumentSnapshot) {
        userDataResponse = documentSnapshot.toObject(UserDataResponse::class.java)!!

        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "Set GoalsDataResponse Done    goalsDataResponse  :  $userDataResponse")
    }

    override suspend fun setFirebaseUserData(): UserDataUiState {
        return try {
            db.collection("users")
                .document(firebaseAuth.currentUser?.uid ?: "anonymous")
                // 문서 존재 유무 판별에 실패한 경우 이전 데이터를 보호하기 위해 사용 (문서 전체 덮어쓰기 X, 병합 O)
                .set(UserDataResponse(), SetOptions.merge())
                .await()
            UserDataUiState.UserDataSuccess
        } catch (e: FirebaseFirestoreException) {
            // storage rule 위반
            ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "setFirebaseUserData()   Firebase FireStore Permission Denied  :  $e")
            UserDataUiState.UserDataFailure
        }
    }

    override fun getUserData(): UserDataResponse = userDataResponse
}