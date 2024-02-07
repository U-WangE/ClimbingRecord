package com.ihavesookchi.climbingrecord.util

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.databinding.LayoutPopupYesNoBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object CommonUtil {
    private val CLASS_NAME = this::class.java.simpleName

    private var toast: Toast? = null

    fun toast(context: Context, message: String) {
        toast?.cancel()
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast?.show()
    }

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
        contents: List<String> = emptyList(),
        comments: String? = null,
        leftButtonText: String,
        rightButtonText: String,
        selectedButton: (String) -> Unit,
    ) {
        val popupBinding = LayoutPopupYesNoBinding.inflate(LayoutInflater.from(context))
        val popupView = popupBinding.root
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            false
        )

        with(popupBinding) {
            tvTitle.text = title

            if (contents.isEmpty())
                tvContents.visibility = GONE
            else {
                var contentText = contents[0]
                for (i in 1 until contents.size)
                    contentText += "\n${contents[i]}"

                tvContents.text = contentText
            }

            comments?.let {
                tvComments.visibility = VISIBLE
                tvComments.text = comments
            }?: run {
                tvComments.visibility = GONE
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