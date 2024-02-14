package com.ihavesookchi.climbingrecord.data.repositoryImpl

import android.net.Uri
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.ihavesookchi.climbingrecord.data.repository.ProfileItemChangeRepository
import com.ihavesookchi.climbingrecord.data.response.UserDataResponse
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ProfileItemChangeRepositoryImpl @Inject constructor(

): ProfileItemChangeRepository {
    private val CLASS_NAME = this::class.java.simpleName

    private val db = Firebase.firestore
    private val firebaseAuth = FirebaseAuth.getInstance()


    override fun uploadBitmapToFirebaseStorage(byteArray: ByteArray, completeCallback: (Uri?) -> Unit) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val storageReference = FirebaseStorage.getInstance().reference
        val imageRef = storageReference.child("${firebaseAuth.uid}/profileImage.jpg")
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

    override suspend fun updateUserData(userDataResponse: UserDataResponse): Void? {
        return db.collection("users")
            .document(firebaseAuth.currentUser?.uid?: "anonymous")
            .set(UserDataResponse(), SetOptions.merge())
            .await()
    }
}