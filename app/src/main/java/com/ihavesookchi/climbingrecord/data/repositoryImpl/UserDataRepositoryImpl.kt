package com.ihavesookchi.climbingrecord.data.repositoryImpl

import android.net.Uri
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.data.repository.UserDataRepository
import com.ihavesookchi.climbingrecord.data.response.UserDataResponse
import com.ihavesookchi.climbingrecord.util.CommonUtil.retry
import com.ihavesookchi.climbingrecord.util.CommonUtil.toMap
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
): UserDataRepository {
    private val CLASS_NAME = this::class.java.simpleName

    private var userDataResponse: UserDataResponse = UserDataResponse()

    override fun initUserData() { userDataResponse = UserDataResponse() }

    override fun setUserData(documentSnapshot: DocumentSnapshot?) {
        userDataResponse = documentSnapshot?.toObject(UserDataResponse::class.java)?:UserDataResponse()

        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "setUserData Set GoalsDataResponse Done    goalsDataResponse  :  ${documentSnapshot?:userDataResponse}")
    }

    override fun getUserData(): UserDataResponse = userDataResponse


    // DB에 해당 User에 대한 Data가 없는 경우 DB에 Init Data 추가
    override suspend fun initUserDataToFirebaseDB(): Task<Void>? {
        return retry {
            db.collection("users")
                .document(firebaseAuth.currentUser?.uid!!)
                .set(UserDataResponse(), SetOptions.merge())    // 문서 존재 유무 판별에 실패한 경우 이전 데이터를 보호하기 위해 사용 (문서 전체 덮어쓰기 X, 병합 O)
                .run {
                    await()
                    this
                }
        }
    }

    override suspend fun getUserDataFromFirebaseDB(): Task<DocumentSnapshot>? {
        return retry {
            db.collection("users")
                    .document(firebaseAuth.currentUser?.uid!!)
                    .get()
                    .run {
                        await()
                        this
                    }
            }
    }

    override suspend fun updateUserData(userDataResponse: UserDataResponse): Task<Void>? {
        return retry {
                db.collection("users")
                    .document(firebaseAuth.currentUser?.uid!!)
                    .update(userDataResponse.toMap())
                    .run {
                        await()
                        this
                    }
            }
    }
    override suspend fun uploadProfileImageBitmapToFirebaseStorage(byteArray: ByteArray): Task<Uri>? {
        val storageReference = FirebaseStorage.getInstance().reference
        val imageRef = storageReference.child("${firebaseAuth.currentUser?.uid!!}/profileImage.jpg")

        return retry {
            imageRef.putBytes(byteArray).await()
            // 업로드 성공 시 이미지의 다운로드 URL을 얻어옴
            imageRef.downloadUrl.run {
                await()
                this
            }
        }
    }

    override suspend fun deleteProfileImageBitmapToFirebaseStorage(): Task<Void>? {
        val storageReference = FirebaseStorage.getInstance().reference
        val imageRef = storageReference.child("${firebaseAuth.currentUser?.uid!!}/profileImage.jpg")
        return retry {
            imageRef.delete().run {
                await()
                this
            }
        }
    }

//    override fun uploadProfileImageBitmapToFirebaseStorage(byteArray: ByteArray, completeCallback: (Uri?) -> Unit) {
//        val storageReference = FirebaseStorage.getInstance().reference
//        val imageRef = storageReference.child("${firebaseAuth.currentUser?.uid!!}/profileImage.jpg")
//
//        imageRef.putBytes(byteArray)
//            .addOnSuccessListener {
//                // 업로드 성공 시 이미지의 다운로드 URL을 얻어옴
//                imageRef.downloadUrl.addOnCompleteListener { task ->
//                    completeCallback(task.result)
//                }
//            }
//            .addOnFailureListener { exception ->
//                // 업로드 실패 시 처리
//                exception.printStackTrace()
//
//                completeCallback(null)
//            }
//    }
}