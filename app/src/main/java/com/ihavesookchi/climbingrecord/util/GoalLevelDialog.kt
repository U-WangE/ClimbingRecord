package com.ihavesookchi.climbingrecord.util

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.constraintlayout.widget.ConstraintSet
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.databinding.LayoutPopupSetGoalColorBinding
import com.ihavesookchi.climbingrecord.util.CommonUtil.setSVGColorFilter

class GoalLevelDialog(context: Context) {
    private var _binding: LayoutPopupSetGoalColorBinding? = null
    private val binding get() = _binding!!

    private lateinit var popupWindow: PopupWindow

    private var selectedColorId: Int? = null

    init {
        _binding = LayoutPopupSetGoalColorBinding.inflate(LayoutInflater.from(context))
        setSVGColorFilter(binding.ivClose, R.color.svgFilterColorMediumGrayDarkGray, context)
    }

    private fun init() {
        selectedColorId = null
        binding.viSelectedImpact.visibility = GONE
    }

    fun show(view: View, colorCallBack: (Int?) -> Unit) {
        init()

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

        setButtonClickListener(colorCallBack)
        setColorClickListener()

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
    }

    private fun setColorClickListener() {
        with(binding) {
            val clickListener = OnClickListener {
                setSelectedImpact(it)

                selectedColorId = if (it == tvLevelNone)
                    null
                else
                    (it.background as ColorDrawable).color
            }
            val colorIds = arrayListOf(tvLevelNone, viLevelWhite, viLevelYellow, viLevelOrange, viLevelGreen, viLevelBlue,viLevelRed,viLevelPurple,viLevelGray,viLevelBrown,viLevelBlack)
            colorIds.forEach {
                it.setOnClickListener (clickListener)
            }
        }
    }

    // Color Click 시 현재 선택한 Color 가 어떤 것인지 표시해 주는 기능
    private fun setSelectedImpact(view: View) {
        binding.viSelectedImpact.visibility = VISIBLE
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.clPopupSetGoalColorLayout)
        constraintSet.connect(binding.viSelectedImpact.id, ConstraintSet.TOP, view.id, ConstraintSet.TOP, 0)
        constraintSet.connect(binding.viSelectedImpact.id, ConstraintSet.BOTTOM, view.id, ConstraintSet.BOTTOM, 0)
        constraintSet.connect(binding.viSelectedImpact.id, ConstraintSet.START, view.id, ConstraintSet.START, 0)
        constraintSet.connect(binding.viSelectedImpact.id, ConstraintSet.END, view.id, ConstraintSet.END, 0)
        constraintSet.applyTo(binding.clPopupSetGoalColorLayout)
    }

    private fun setButtonClickListener(colorCallBack: (Int?) -> Unit) {
        binding.ivClose.setOnClickListener {
            binding.viSelectedImpact.visibility = GONE
            colorCallBack(null)
            dismiss()
        }

        binding.btAccept.setOnClickListener {
            binding.viSelectedImpact.visibility = GONE
            colorCallBack(selectedColorId)
            dismiss()
        }
    }

    fun isShowing() = ::popupWindow.isInitialized && popupWindow.isShowing
    fun dismiss() {
        popupWindow.dismiss()
    }
}