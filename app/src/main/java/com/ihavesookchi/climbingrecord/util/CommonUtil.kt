package com.ihavesookchi.climbingrecord.util

import android.app.Activity
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.databinding.LayoutPopupYesNoBinding
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object CommonUtil {
    private val CLASS_NAME = this::class.java.simpleName

    private var toast: Toast? = null

    fun toast(context: Context, message: String) {
        toast?.cancel()
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
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

    fun setSVGColorFilter(appCompatImageView: AppCompatImageView, goalColorId: Int?, context: Context) {
        if (goalColorId != null) {
            appCompatImageView.setColorFilter(ContextCompat.getColor(context, goalColorId), PorterDuff.Mode.SRC_IN)
        } else {
            appCompatImageView.colorFilter = null
        }
    }

    fun Any.toMap(): Map<String, Any?> {
        val json = Gson().toJson(this)
        val mapType = object : TypeToken<Map<String, Any>>() {}.type
        return Gson().fromJson(json, mapType)
    }

    suspend fun <T> retry(
        times: Int = 3,
        delayMillis: Long = 1000,
        block: suspend () -> T,
    ): T? {
        var lastException: Exception? = null

        repeat(times) { attempt ->
            try {
                return block()
            } catch (e: Exception) {
                lastException = e

                // 실패 시 재시도 대기 (마지막 대기 x)
                if (attempt < times - 1) {
                    delay(delayMillis)
                }
            }
        }

        // 최대 재시도 횟수를 초과하면 마지막 예외를 던집니다.
        throw lastException ?: IllegalStateException("Unexpected state in retry function.")
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
            title?.let {
                tvTitle.text = it
            }?: run { tvTitle.visibility = GONE }

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

    fun Activity.isSoftKeyboardShow(): Boolean {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }

    // soft keyboard 숨기기 Activity 전체
    fun Activity.hideSoftKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        currentFocus?.clearFocus()
    }

    // soft keyboard 숨기기 특정 View
    fun View.hideSoftKeyboard() {
        if (hasFocus()) {
            val inputMethodManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            clearFocus()
        }
    }

    fun Fragment.setBackPressedCallback(setHandleOnBackPressed: () -> Unit) {
        requireActivity()
            .onBackPressedDispatcher
            .addCallback(
                this,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        setHandleOnBackPressed()
                    }
                }
            )
    }
}