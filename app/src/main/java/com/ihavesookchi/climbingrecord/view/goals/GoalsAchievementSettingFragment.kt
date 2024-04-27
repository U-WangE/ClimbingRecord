package com.ihavesookchi.climbingrecord.view.goals

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.databinding.FragmentGoalsAchievementSettingBinding
import com.ihavesookchi.climbingrecord.databinding.IncludeGoalsSettingBinding
import com.ihavesookchi.climbingrecord.util.CalenderDialog
import com.ihavesookchi.climbingrecord.util.ClearFocusEditText
import com.ihavesookchi.climbingrecord.util.CommonUtil.hideSoftKeyboard
import com.ihavesookchi.climbingrecord.util.CommonUtil.setSVGColorFilter
import com.ihavesookchi.climbingrecord.util.PeriodDatePicker
import com.ihavesookchi.climbingrecord.view.BaseActivity
import com.ihavesookchi.climbingrecord.viewModel.BaseViewModel
import com.ihavesookchi.climbingrecord.viewModel.GoalsAchievementSettingViewModel
import java.util.Calendar

class GoalsAchievementSettingFragment : Fragment() {
    private var _binding: FragmentGoalsAchievementSettingBinding? = null
    private val binding get() = _binding!!

    private val CLASS_NAME = this::class.java.simpleName

    private val sharedViewModel: BaseViewModel by activityViewModels()
    private val goalsAchievementSettingViewModel: GoalsAchievementSettingViewModel by viewModels()

//    override fun onStart() {
//        super.onStart()
//        setBackPressedCallback {
//            Log.d("여기",requireActivity().isSoftKeyboardShow().toString())
//            if (requireActivity().isSoftKeyboardShow()) {
//                requireActivity().hideSoftKeyboard()
//            } else {
//                requireActivity().supportFragmentManager.popBackStack()
//            }
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGoalsAchievementSettingBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDefaultUISetting()
    }

    private fun setDefaultUISetting() {
        setSVGColorFilter(binding.btBackButton, R.color.svgFilterColorDarkGrayMediumGray, requireContext())
        setSVGColorFilter(binding.icGoalSettingOne.ivCalendarImage, R.color.svgFilterColorLightGrayishBlack, requireContext())
        setSVGColorFilter(binding.icGoalSettingTwo.ivCalendarImage, R.color.svgFilterColorLightGrayishBlack, requireContext())

        // Back Button
        setBackButtonOnClickListener()

        // Goal Click Listener Setting
        setGoalsSettingClickListener(binding.icGoalSettingOne)
        setGoalsSettingClickListener(binding.icGoalSettingTwo)
    }
    // Goal 설정 하는 UI Click Listener
    private fun setGoalsSettingClickListener(includeGoalsSetting: IncludeGoalsSettingBinding) {
        includeGoalsSetting.apply {
            setGoalColorOnClickListener(tvGoalColorSetting)
            setGoalAchievementOnClickListener(etGoalAchievement)
            setGoalAchievementPeriodOnClickListener(llGoalAchievementPeriodLayout, etGoalAchievementPeriod)
        }
    }

    private fun setGoalColorOnClickListener(textView: TextView) {
        textView.setOnClickListener {

        }
    }

    // Soft Keyboard 에 Done 버튼 클릭시, Soft Keyboard 숨기기
    private fun setGoalAchievementOnClickListener(clearFocusEditText: ClearFocusEditText) {
        clearFocusEditText.setOnEditorActionListener { view, keyCode, _ ->
            if (keyCode == EditorInfo.IME_ACTION_DONE) {
                ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "setGoalAchievementOnClickListener(ClearFocusEditText) EditText Done Button Click   EditText.Text  :  ${view.text}")
                view.hideSoftKeyboard()
                true
            } else false
        }
        clearFocusEditText.setOnFocusChangeListener { _, boolean ->
            binding.btEditButton.visibility = if (boolean) GONE else VISIBLE
        }
    }
    private fun setGoalAchievementPeriodOnClickListener(linearLayout: LinearLayout, clearFocusEditText: ClearFocusEditText) {

        //TODO:: 다른 방식으로 변경
        linearLayout.setOnClickListener {
            //todo::CalenderDialog
        }
    }

    private fun setBackButtonOnClickListener() {
        binding.btBackButton.setOnClickListener {
            (activity as BaseActivity).replaceFragment(GoalsFragment())
            (activity as BaseActivity).removeFragment(this)
        }
    }
}