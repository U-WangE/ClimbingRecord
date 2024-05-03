package com.ihavesookchi.climbingrecord.util

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.databinding.LayoutPopupSetPeriodBinding
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

class CalendarDialog(context: Context) {
    private var _binding: LayoutPopupSetPeriodBinding? = null
    private val binding get() = _binding!!

    private lateinit var popupWindow: PopupWindow
    private var pageCounter = 0

    private var updateStartDate: Long? = null
    private var updateEndDate: Long? = null

    init {
        _binding = LayoutPopupSetPeriodBinding.inflate(LayoutInflater.from(context))
        pageCounter = 0
    }

    fun show(view: View, startDate: Long? = null, endDate: Long? = null, periodCallback: (Long?, Long?) -> Unit) {
        //TODO::EndDate가 StartDate보다 작으면, 설정 안 되게 또는 Error, 경고 표시
        if (binding.root.parent != null)
            (binding.root.parent as? ViewGroup)?.removeView(binding.root)

        if (!::popupWindow.isInitialized)
            popupWindow = PopupWindow(
                binding.root,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                false)

        if (::popupWindow.isInitialized && popupWindow.isShowing)
            popupWindow.dismiss()

        pageCounter = 0
        updateStartDate = startDate
        updateEndDate = endDate

        updatePageUI(periodCallback)
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    private fun updatePageUI(periodCallback: (Long?, Long?) -> Unit) {
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
                periodCallback(null, null)
                popupWindow.dismiss()
            } else {
                pageCounter--
                updatePageUI(periodCallback)
            }
        }

        binding.btRight.setOnClickListener {
            if (pageCounter % 2 == 0) {
                pageCounter++
                updatePageUI(periodCallback)
            } else {
                periodCallback(updateStartDate?:System.currentTimeMillis(), updateEndDate?:System.currentTimeMillis())
                popupWindow.dismiss()
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
        }
    }

    // 이전에 설정 or 선택한 날짜 또는 현재 날짜를 Date Picker 의 초기 Date로 설정
    private fun setDateForDatePicker() {
//        val updateDate = if (pageCounter % 2 == 0) updateStartDate else updateEndDate
//        val localDateTime = LocalDateTime.ofEpochSecond(updateDate ?: (System.currentTimeMillis() / 1000L), 0, ZoneOffset.UTC)
//        binding.dpDatePicker.updateDate(localDateTime.year, localDateTime.monthValue - 1, localDateTime.dayOfMonth)

        val updateDateMillis = if (pageCounter % 2 == 0) updateStartDate else updateEndDate
        val updateInstant = Instant.ofEpochMilli(updateDateMillis?:System.currentTimeMillis())
        val localDateTime = LocalDateTime.ofInstant(updateInstant, ZoneOffset.UTC)
        binding.dpDatePicker.updateDate(localDateTime.year, localDateTime.monthValue - 1, localDateTime.dayOfMonth)
    }

    private fun convertDateToUnixTime(year: Int, month: Int, day: Int): Long {
        val selectedDate = LocalDate.of(year, month + 1, day)
        val selectedDateTime = selectedDate.atStartOfDay().toInstant(ZoneOffset.UTC)
        return selectedDateTime.toEpochMilli()
//        return selectedDateTime.toEpochSecond(ZoneOffset.UTC)
    }
}