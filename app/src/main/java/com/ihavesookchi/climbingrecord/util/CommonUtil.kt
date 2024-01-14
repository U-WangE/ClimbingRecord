package com.ihavesookchi.climbingrecord.util

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.databinding.LayoutPopupYesNoBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CommonUtil {
    private val CLASS_NAME = this::class.java.simpleName

    fun formatDateFromMillis(dateFormat: String, milliseconds: Long): String {
        return try {
            SimpleDateFormat(dateFormat, Locale.getDefault()).format(Date(milliseconds))
        } catch (e: IllegalArgumentException) {
            ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "Invalid date format : $dateFormat,\n $e")
            ""
        } catch (e: Exception) {
            ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "Error formatting date : $dateFormat,\n $e")
            ""
        }
    }

    fun setSVGColorFilter(appCompatImageView: AppCompatImageView, goalColorRGB: String) {
        appCompatImageView.setColorFilter(Color.parseColor(goalColorRGB), PorterDuff.Mode.SRC_IN)
    }

    fun setSVGColorFilter(appCompatImageView: AppCompatImageView, goalColorId: Int, context: Context) {
        appCompatImageView.setColorFilter(ContextCompat.getColor(context, goalColorId), PorterDuff.Mode.SRC_IN)
    }

    fun twoButtonPopupWindow(
        context: Context,
        view: View,
        title: String?,
        contents: String?,
        comments: String? = null,
        leftButtonText: String,
        rightButtonText: String,
        selectedButton: (String) -> Unit
    ) {
        val popupBinding = LayoutPopupYesNoBinding.inflate(LayoutInflater.from(context))
        val popupView = popupBinding.root
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            false
        )

        with(popupBinding) {
            tvTitle.text = title
            tvContents.text = contents

            comments?.let {
                tvComments.visibility = View.VISIBLE
                tvComments.text = comments
            }?: run {
                tvComments.visibility = View.GONE
            }

            btLeft.text = leftButtonText
            btRight.text = rightButtonText

            btLeft.setOnClickListener {
                popupWindow.dismiss()
                selectedButton("Left")
            }

            btRight.setOnClickListener {
                popupWindow.dismiss()
                selectedButton("Right")
            }
        }

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
    }
}