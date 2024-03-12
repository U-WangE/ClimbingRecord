package com.ihavesookchi.climbingrecord.util

import android.graphics.BitmapFactory
import android.widget.ImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

class ImageLoadTask(private val imageView: ImageView) {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun loadImage(imageUrl: String) {
        coroutineScope.launch {
            val bitmap = withContext(Dispatchers.IO) {
                try {
                    val inputStream = URL(imageUrl).openStream()
                    BitmapFactory.decodeStream(inputStream)
                } catch (e: IOException) {
                    e.printStackTrace()
                    null
                }
            }
            bitmap?.let { imageView.setImageBitmap(it) }
        }
    }
}