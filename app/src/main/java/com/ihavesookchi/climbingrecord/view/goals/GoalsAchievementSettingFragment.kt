package com.ihavesookchi.climbingrecord.view.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.databinding.FragmentGoalsAchievementSettingBinding
import com.ihavesookchi.climbingrecord.databinding.IncludeGoalsSettingBinding
import com.ihavesookchi.climbingrecord.util.CommonUtil
import com.ihavesookchi.climbingrecord.util.CommonUtil.hideSoftKeyboard
import com.ihavesookchi.climbingrecord.view.BaseActivity
import com.ihavesookchi.climbingrecord.viewModel.BaseViewModel
import com.ihavesookchi.climbingrecord.viewModel.GoalsViewModel

class GoalsAchievementSettingFragment : Fragment() {
    private var _binding: FragmentGoalsAchievementSettingBinding? = null
    private val binding get() = _binding!!

    private val CLASS_NAME = this::class.java.simpleName

    private val sharedViewModel: BaseViewModel by activityViewModels()
    private val goalsViewModel: GoalsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentGoalsAchievementSettingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBackButtonOnClickListener()
        setDefaultGoalsUISetting(binding.icGoalSettingOne)
        setDefaultGoalsUISetting(binding.icGoalSettingTwo)

    }

    private fun setDefaultGoalsUISetting(includeGoalsSettingBinding: IncludeGoalsSettingBinding) {
        setGoalColorOnClickListener(includeGoalsSettingBinding.tvGoalColorSetting)
        setGoalAchievementOnClickListener(includeGoalsSettingBinding.tvGoalAchievement)
        setGoalAchievementPeriodOnClickListener(includeGoalsSettingBinding.tvGoalAchievementPeriod)
    }

    private fun setGoalColorOnClickListener(tvGoalColorSetting: TextView) {
        tvGoalColorSetting.setOnClickListener {
            //TODO::색 고를 수 있는 Dialog Popup 나와야 함
        }
    }

    private fun setGoalAchievementOnClickListener(tvGoalAchievement: EditText) {
        tvGoalAchievement.setOnEditorActionListener { view, keyCode, keyEvent ->
            if (keyCode == EditorInfo.IME_ACTION_DONE) {
                hideSoftKeyboard(tvGoalAchievement)
                view.clearFocus()
                true
            } else false
        }
    }

    private fun setGoalAchievementPeriodOnClickListener(tvGoalAchievementPeriod: TextView) {
        //TODO:: 달력 or 날짜 입력 or 00 스크롤 ui
    }

    private fun setGoalSettingUI(icGoalSetting: IncludeGoalsSettingBinding) {
        icGoalSetting.tvGoalColorSetting
        icGoalSetting.tvGoalAchievement
        icGoalSetting.tvGoalAchievementPeriod

    }

    private fun setBackButtonOnClickListener() {
        CommonUtil.setSVGColorFilter(
            binding.btBackButton,
            R.color.svgFilterColorDarkGrayMediumGray,
            requireContext()
        )

        binding.btBackButton.setOnClickListener {
            (activity as BaseActivity).replaceFragment(GoalsFragment())
            (activity as BaseActivity).removeFragment(this)
        }
    }
}