package com.ihavesookchi.climbingrecord.util

import android.content.Context
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.core.content.ContextCompat.getColor
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.databinding.LayoutPopupSetPeriodBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

class CalendarDialog(context: Context) {
    private var _binding: LayoutPopupSetPeriodBinding? = null
    private val binding get() = _binding!!

    private lateinit var popupWindow: PopupWindow
    private var pageCounter = 0

    private var defaultStartDate: Long = 0L
    private var defaultEndDate: Long = 0L
    private var updateStartDate: Long = 0L
    private var updateEndDate: Long = 0L

    init {
        _binding = LayoutPopupSetPeriodBinding.inflate(LayoutInflater.from(context))

    }

    private fun init(startDate: Long, endDate: Long) {
        pageCounter = 0
        defaultStartDate = startDate
        defaultEndDate = endDate
        updateStartDate = if (startDate != 0L) startDate else System.currentTimeMillis()
        updateEndDate = if (endDate != 0L) endDate else System.currentTimeMillis()
    }

    fun show(view: View, startDate: Long, endDate: Long, periodCallback: (Long, Long) -> Unit) {
        if (binding.root.parent != null)
            (binding.root.parent as? ViewGroup)?.removeView(binding.root)

        if (!::popupWindow.isInitialized)
            popupWindow = PopupWindow(
                binding.root,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                false)

        if (isShowing())
            dismiss()

        init(startDate, endDate)

        updatePageUI(periodCallback)
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    private fun updatePageUI(periodCallback: (Long, Long) -> Unit) {
        updateTextWithPageCounter()
        setDateForDatePicker()

        binding.dpDatePicker.setOnDateChangedListener { _, year, month, day ->
            if (pageCounter % 2 == 0)
                updateStartDate = convertDateToUnixTime(year, month, day)
            else
                updateEndDate = convertDateToUnixTime(year, month, day)
        }

        binding.btLeft.setOnClickListener {
            if (pageCounter % 2 == 0) {
                periodCallback(defaultStartDate, defaultEndDate)
                dismiss()
            } else {
                pageCounter--
                updatePageUI(periodCallback)
            }
        }

        binding.btRight.setOnClickListener {
            if (pageCounter % 2 == 0) {
                pageCounter++
                updatePageUI(periodCallback)
            } else if (updateStartDate >= updateEndDate || System.currentTimeMillis() > updateEndDate) {
                // 시작 날짜와 종료 날짜가 같거나 시작 날짜가 더 미래인 경우
                // 종료 날짜가 과거나 당일인 경우

                CoroutineScope(Dispatchers.Main).launch {
                    //3초 Error 문구 & Color Red
                    binding.tvErrorMessage.visibility = VISIBLE
                    binding.tvErrorMessage.text = binding.root.context.getString(R.string.notice_please_check_the_period_again)
                    binding.btRight.backgroundTintList = ColorStateList.valueOf(getColor(binding.root.context, R.color.theclimb_red))

                    delay(3000L)

                    binding.tvErrorMessage.visibility = GONE
                    binding.btRight.backgroundTintList = ColorStateList.valueOf(getColor(binding.root.context, R.color.white))
                }
            } else {
                periodCallback(updateStartDate, updateEndDate)
                dismiss()
            }
        }
    }

    private fun updateTextWithPageCounter() {
        with(binding.root.context) {
            val titleResId = if (pageCounter % 2 == 0) R.string.title_start_date else R.string.title_end_date
            val leftButtonResId = if (pageCounter % 2 == 0) R.string.close else R.string.prev
            val rightButtonResId = if (pageCounter % 2 == 0) R.string.next else R.string.save

            binding.tvDatePickerTitle.text = getString(titleResId)
            binding.btLeft.text = getString(leftButtonResId)
            binding.btRight.text = getString(rightButtonResId)
            binding.tvErrorMessage.visibility = GONE
        }
    }

    // 이전에 설정 or 선택한 날짜 또는 현재 날짜를 Date Picker 의 초기 Date로 설정
    private fun setDateForDatePicker() {
        val updateDateMillis = if (pageCounter % 2 == 0) updateStartDate else updateEndDate
        val updateInstant = Instant.ofEpochMilli(updateDateMillis)
        val localDateTime = LocalDateTime.ofInstant(updateInstant, ZoneOffset.UTC)
        binding.dpDatePicker.updateDate(localDateTime.year, localDateTime.monthValue - 1, localDateTime.dayOfMonth)
    }

    private fun convertDateToUnixTime(year: Int, month: Int, day: Int): Long {
        val selectedDate = LocalDate.of(year, month + 1, day)
        val selectedDateTime = selectedDate.atStartOfDay().toInstant(ZoneOffset.UTC)
        return selectedDateTime.toEpochMilli()
    }

    fun isShowing() = ::popupWindow.isInitialized && popupWindow.isShowing
    fun dismiss() = popupWindow.dismiss()
}