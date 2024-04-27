package com.ihavesookchi.climbingrecord.util

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.PopupWindow
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.databinding.LayoutPopupSetPeriodBinding

class CalenderDialog(context: Context) {
    private var _binding: LayoutPopupSetPeriodBinding? = null
    private val binding get() = _binding!!

    private var pageCounter = 0

    init {
        _binding = LayoutPopupSetPeriodBinding.inflate(LayoutInflater.from(context))
        pageCounter = 0
    }

    fun show(view: View) {
        val popupWindow = PopupWindow(
            binding.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            false
        )

        setUI(pageCounter)

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    private fun setUI(pageCounter: Int) {
        with(binding.root.context) {
            when (pageCounter % 2) {
                0 -> {
                    binding.btLeft.visibility = GONE

                    binding.tvDatePickerTitle.text = getString(R.string.title_start_date)
                    binding.btRight.text = getString(R.string.next)
                }
                1 -> {
                    binding.btLeft.visibility = VISIBLE

                    binding.tvDatePickerTitle.text = getString(R.string.title_end_date)
                    binding.btLeft.text = getString(R.string.prev)
                    binding.btRight.text = getString(R.string.save)
                }
            }
        }
    }

    private fun setOnClickListener() {

    }
}