package com.ihavesookchi.climbingrecord.data.repositoryImpl

import android.net.Uri
import android.service.autofill.UserData
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.data.dataState.UpdateDataState
import com.ihavesookchi.climbingrecord.data.repository.ProfileItemChangeRepository
import com.ihavesookchi.climbingrecord.data.response.UserDataResponse
import com.ihavesookchi.climbingrecord.data.uistate.UserDataUiState
import com.ihavesookchi.climbingrecord.util.CommonUtil.retry
import com.ihavesookchi.climbingrecord.util.CommonUtil.toMap
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileItemChangeRepositoryImpl @Inject constructor(

): ProfileItemChangeRepository {
    private val CLASS_NAME = this::class.java.simpleName

    private val db = Firebase.firestore
    private val firebaseAuth = FirebaseAuth.getInstance()


    override fun uploadBitmapToFirebaseStorage(byteArray: ByteArray, completeCallback: (Uri?) -> Unit) {
        val storageReference = FirebaseStorage.getInstance().reference
        val imageRef = storageReference.child("${firebaseAuth.currentUser?.uid}/profileImage.jpg")
        imageRef.putBytes(byteArray)
            .addOnSuccessListener {
                // 업로드 성공 시 이미지의 다운로드 URL을 얻어옴
                imageRef.downloadUrl.addOnCompleteListener { task ->
                    completeCallback(task.result)
                }
            }
            .addOnFailureListener { exception ->
                // 업로드 실패 시 처리
                exception.printStackTrace()

                completeCallback(null)
            }
    }

    override suspend fun updateUserData(userDataResponse: UserDataResponse): UpdateDataState {
        return try {
            val updateTask = retry(times = 3) {
                val task = db.collection("users")
                    .document(firebaseAuth.currentUser?.uid ?: "anonymous")
                    .update(userDataResponse.toMap())

                task.await()

                task.isSuccessful.apply {
                    if (this)
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "updateUserData() Success")
                    else
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "updateUserData() Failure: ${task.exception}")
                }
            }

            if (updateTask == true)
                UpdateDataState.UpdateSuccess
            else
                UpdateDataState.AttemptLimitExceeded
        } catch (firebaseFirestoreException: FirebaseFirestoreException) {
            ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "updateUserData() Other exception    exception : $firebaseFirestoreException")

            UpdateDataState.UpdateFailure
        } catch (e: Exception) {
            ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "updateUserData() Other exception    exception : $e")

            UpdateDataState.UpdateFailure
        }
    }
}