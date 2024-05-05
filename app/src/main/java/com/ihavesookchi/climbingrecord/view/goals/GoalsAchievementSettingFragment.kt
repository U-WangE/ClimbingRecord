package com.ihavesookchi.climbingrecord.view.goals

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.databinding.FragmentGoalsAchievementSettingBinding
import com.ihavesookchi.climbingrecord.databinding.IncludeGoalsSettingBinding
import com.ihavesookchi.climbingrecord.util.CalendarDialog
import com.ihavesookchi.climbingrecord.util.ClearFocusEditText
import com.ihavesookchi.climbingrecord.util.CommonUtil.convertTimeMillisToCalendar
import com.ihavesookchi.climbingrecord.util.CommonUtil.getDDay
import com.ihavesookchi.climbingrecord.util.CommonUtil.hideSoftKeyboard
import com.ihavesookchi.climbingrecord.util.CommonUtil.setSVGColorFilter
import com.ihavesookchi.climbingrecord.util.GoalLevelDialog
import com.ihavesookchi.climbingrecord.view.BaseActivity
import com.ihavesookchi.climbingrecord.viewModel.BaseViewModel
import com.ihavesookchi.climbingrecord.viewModel.GoalsAchievementSettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class GoalsAchievementSettingFragment : Fragment() {
    private var _binding: FragmentGoalsAchievementSettingBinding? = null
    private val binding get() = _binding!!

    private val CLASS_NAME = this::class.java.simpleName

    private val sharedViewModel: BaseViewModel by activityViewModels()
    private val viewModel: GoalsAchievementSettingViewModel by viewModels()


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

        setUI()
    }

    private fun setUI() {
        setSVGColorFilter(binding.btBackButton, R.color.svgFilterColorMediumGrayDarkGray, requireContext())
        setSVGColorFilter(binding.ivCalendarImage, R.color.svgFilterColorLightGrayishBlack, requireContext())

        // Back Button
        setBackButtonOnClickListener()

        // Goal Click Listener Setting
        setGoalsSettingClickListener(binding.icGoalSettingOne)
        setGoalsSettingClickListener(binding.icGoalSettingTwo)

        // Goal Period Setting
        setGoalPeriodSetting()
    }

    private fun setBackButtonOnClickListener() {
        binding.btBackButton.setOnClickListener {
            (activity as BaseActivity).replaceFragment(GoalsFragment())
            (activity as BaseActivity).removeFragment(this)
        }
    }

    // Goal 설정 하는 UI Click Listener
    private fun setGoalsSettingClickListener(includeGoalsSetting: IncludeGoalsSettingBinding) {
        includeGoalsSetting.apply {
            setGoalColorOnClickListener(includeGoalsSetting)
            setOnFocusChangeListener(etGoalAchievement)
        }
    }

    private fun setGoalColorOnClickListener(includeGoalsSetting: IncludeGoalsSettingBinding) {
        with (includeGoalsSetting) {
            val clickListener = OnClickListener {
                binding.root.hideSoftKeyboard()

                GoalLevelDialog(requireContext()).show(tvGoalColorSetting) {
                    if (it != null) {
                        tvGoalColorSetting.visibility = INVISIBLE
                        viSelectedLevel.visibility = VISIBLE
                        viSelectedLevel.setBackgroundColor(it)
                    } else {
                        tvGoalColorSetting.visibility = VISIBLE
                        viSelectedLevel.visibility = GONE
                    }
                }
            }

            tvGoalColorSetting.setOnClickListener(clickListener)
            viSelectedLevel.setOnClickListener(clickListener)
        }
    }

    // Edit Text 에 Focusing 돼있을 경우 Edit Button 숨기기
    private fun setOnFocusChangeListener(clearFocusEditText: ClearFocusEditText) {
        clearFocusEditText.setOnFocusChangeListener { _, hasFocus ->
            binding.btEditButton.visibility = if (hasFocus) GONE else VISIBLE
        }
    }

    // Goal Period Setting
    private fun setGoalPeriodSetting() {
        viewModel.initStartAndEndDate()
        setCalendarDialog()
    }

    private fun setCalendarDialog() {
        binding.ivCalendarImage.setOnClickListener {
            showCalendarDialog()
        }

        setupDayOfDDayTextChangeListener()
        setupDayOfDDayFocusChangeListener()
    }

    private fun showCalendarDialog() {
        binding.root.hideSoftKeyboard()

        CalendarDialog(requireContext()).show(binding.ivCalendarImage, viewModel.getStartDate(), viewModel.getEndDate()) { startDate, endDate ->
            if (startDate != null && endDate != null) {
                viewModel.setStartDate(startDate)
                viewModel.setEndDate(endDate)

                updateGoalPeriodText(startDate, endDate)
                binding.etDayOfDDay.setText(getDDay(startDate, endDate).toString())
            }
        }
    }

    private fun setupDayOfDDayTextChangeListener() {
        binding.etDayOfDDay.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(editable: Editable?) {
                updateEndDateFromDayCount(editable)
            }
        })
    }

    // Day Of DDay EditText 포커스 리스너 설정
    private fun setupDayOfDDayFocusChangeListener() {
        binding.etDayOfDDay.setOnFocusChangeListener { _, hasFocus ->
            binding.btEditButton.visibility = if (hasFocus) View.GONE else View.VISIBLE

            binding.etDayOfDDay.let {
                if (it.text.isNullOrBlank()) it.setText("0")
            }
        }
    }

    // Day Of DDay에 입력된 날짜로 기간 업데이트
    private fun updateEndDateFromDayCount(editable: Editable?) {
        val input = editable?.toString() ?: return
        val days = input.toLongOrNull() ?: return

        val startDate = viewModel.getStartDate()
        if (startDate != null && startDate != 0L) {
            val daysInMillis = TimeUnit.DAYS.toMillis(days)
            viewModel.setEndDate(startDate + daysInMillis)
            updateGoalPeriodText(startDate, startDate + daysInMillis)
        }
    }

    private fun updateGoalPeriodText(startDate: Long, endDate: Long) {
        binding.tvGoalAchievementPeriod.text = getString(R.string.y_m_d_tilde_y_m_d_slash, convertTimeMillisToCalendar(startDate), convertTimeMillisToCalendar(endDate))
    }
}