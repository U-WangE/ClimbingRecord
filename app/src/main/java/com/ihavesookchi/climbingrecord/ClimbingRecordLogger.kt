package com.ihavesookchi.climbingrecord

import android.content.Context
import android.util.Log
import androidx.annotation.Keep
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody

class ClimbingRecordLogger(context: Context) {
    private val mContext : Context

    init {
        mContext = context
    }

    @Keep
    companion object {
        @Volatile
        private var instance: ClimbingRecordLogger? = null

        @JvmStatic
        fun initInstance(context: Context) =
            instance ?: synchronized(this) {
                instance ?: ClimbingRecordLogger(context.applicationContext).also {
                    instance = it
                }
            }

        @JvmStatic
        fun getInstance(): ClimbingRecordLogger? =
            instance ?: synchronized(this) {
                instance
            }
    }

    fun saveLog(title: String, msg: String) {
        fileSave(title, msg)
    }

    private fun fileSave(title: String, msg: String) {

        //TODO:: User Id 에 대한 정의가 확립 되면, 수정
        val fileName = "AOS_ClimbingRecord_Test_${
            SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(
                Date()
            )}.txt"

//        val fileName = "AOS_ClimbingRecord_${user_id}_${
//            SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(
//                Date()
//            )}.txt"

        val fileOutputStream = mContext.openFileOutput(fileName, Context.MODE_PRIVATE)
        val now = System.currentTimeMillis() // 현재시간 받아오기

        val date = Date(now) // Date 객체 생성

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val nowTime: String = sdf.format(date)

        fileOutputStream.write("$nowTime   ||   Title  [  $title  ]   ||   Message  [  $msg  ]\n".toByteArray())
        fileOutputStream.close()

        val fileInputStream = mContext.openFileInput(fileName)
        val content = fileInputStream.bufferedReader().use { it.readText() }
        fileInputStream.close()
    }

    fun fileDetector() {
        val files = mContext.filesDir.listFiles()

        if (files != null) {
            for (file in files) {
                if (file.name.contains("AOS")) {
                    if (file.name.contains("zip")) {
                        fileZipReceiver(file)
                        file.nameWithoutExtension
                    } else {
                        val fileName = file.nameWithoutExtension.split("_")
                        if (fileName[fileName.size - 1].toInt() == SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date()).toInt())
                            fileZipCompress(file.nameWithoutExtension)
                    }
                }
            }
        }
    }

    private fun fileZipCompress(fileName: String) {
        val textFile = File(mContext.filesDir, "${fileName}.txt")
        val zipFile = File(mContext.filesDir, "${fileName}.zip")

        if (!textFile.exists() || textFile.length() == 0L) {
            Log.e("fileZipCompress", "Text file is not found or is empty")
            return
        }

        try {
            FileOutputStream(zipFile).use {
                ZipOutputStream(it).use { zos ->
                    val zipEntry = ZipEntry(textFile.name)
                    zos.putNextEntry(zipEntry)

                    FileInputStream(textFile).use { fis ->
                        fis.copyTo(zos, 1024)
                    }
                    zos.closeEntry()
                }
            }
            if (zipFile.exists()) {
                textFile.delete()
                fileZipReceiver(zipFile)
            }
        } catch (e: FileNotFoundException) {
            Log.e("FileNotFoundException", "Text file is not found or is empty")
        }
    }

    private fun fileZipReceiver(zipFile: File) {
        val fileReqBody = zipFile.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("android", zipFile.name, fileReqBody)
        CoroutineScope(Dispatchers.IO).launch {

            //TODO:: .zip 파일 전송 방식 정해지면, 수정

//            getPhenotypeRetrofit.fileReceiver(
//                Config.API_KEY,
//                filePart
//            )?.let {
//                if (it.result == "success") {
//                    Log.i("FileZipReceiver", "Upload Success")
//                    zipFile.delete()
//                }
//            }
        }
    }
}