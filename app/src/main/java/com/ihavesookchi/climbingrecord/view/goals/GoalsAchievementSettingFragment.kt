package com.ihavesookchi.climbingrecord.view.goals

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.data.uistate.GoalsAchievementUiState
import com.ihavesookchi.climbingrecord.databinding.FragmentGoalsAchievementSettingBinding
import com.ihavesookchi.climbingrecord.databinding.IncludeGoalsSettingBinding
import com.ihavesookchi.climbingrecord.util.CalendarDialog
import com.ihavesookchi.climbingrecord.util.CommonUtil.convertTimeMillisToCalendar
import com.ihavesookchi.climbingrecord.util.CommonUtil.getDDay
import com.ihavesookchi.climbingrecord.util.CommonUtil.hideSoftKeyboard
import com.ihavesookchi.climbingrecord.util.CommonUtil.isSoftKeyboardShow
import com.ihavesookchi.climbingrecord.util.CommonUtil.setBackPressedCallback
import com.ihavesookchi.climbingrecord.util.CommonUtil.setSVGColorFilter
import com.ihavesookchi.climbingrecord.util.CommonUtil.toast
import com.ihavesookchi.climbingrecord.util.GoalLevelDialog
import com.ihavesookchi.climbingrecord.view.BaseActivity
import com.ihavesookchi.climbingrecord.viewModel.GoalsAchievementSettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class GoalsAchievementSettingFragment : Fragment() {
    private var _binding: FragmentGoalsAchievementSettingBinding? = null
    private val binding get() = _binding!!

    private val CLASS_NAME = this::class.java.simpleName

    private val viewModel: GoalsAchievementSettingViewModel by viewModels()

    private lateinit var calendarDialog: CalendarDialog
    private lateinit var goalLevelDialog: GoalLevelDialog


    override fun onStart() {
        super.onStart()
        setBackPressedCallback {
            if (calendarDialog.isShowing())
                calendarDialog.dismiss()
            else if (goalLevelDialog.isShowing())
                goalLevelDialog.dismiss()
            else if (requireActivity().isSoftKeyboardShow()) {
                requireActivity().hideSoftKeyboard()
            } else {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentGoalsAchievementSettingBinding.inflate(inflater, container, false)

        //TODO::
        // 기간의 start 가 미래인 경우에 대한 bar graph 수정해야하고,
        // 색, 숫자 에 대한 예외 처리 했으나 refactoring 필요
        // goals view 부터 goalsAchievementSetting view 까지 refactoring 필요
        setUi()
        observingGoalsAchievementUiState()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUiClickListener()

        // Goal Calendar Setting
        setCalendarDialog()
    }

    private fun setUi() {
        setSVGColorFilter(binding.btBackButton, R.color.svgFilterColorMediumGrayDarkGray, requireContext())
        setSVGColorFilter(binding.ivCalendarImage, R.color.svgFilterColorLightGrayishBlack, requireContext())

        viewModel.initLinkedHashMap()

        initGoalUi()
        initGoalPeriodUi()
    }

    // 이전에 설정 했던 목표 설정
    private fun initGoalUi() {
        with(viewModel.getLinkedHashMap()) {
            if (size > 0) {
                when {
                    containsKey(0) -> {
                        binding.icGoalSettingOne.tvGoalColorSetting.visibility = INVISIBLE
                        binding.icGoalSettingOne.viSelectedLevel.visibility = VISIBLE
                        binding.icGoalSettingOne.viSelectedLevel.setBackgroundColor(Color.parseColor(this[0]?.goalColorRGB))
                        binding.icGoalSettingOne.etGoalAchievement.setText(this[0]?.goal.toString())
                    }
                    containsKey(1) -> {
                        binding.icGoalSettingTwo.tvGoalColorSetting.visibility = INVISIBLE
                        binding.icGoalSettingTwo.viSelectedLevel.visibility = VISIBLE
                        binding.icGoalSettingTwo.viSelectedLevel.setBackgroundColor(Color.parseColor(this[1]?.goalColorRGB))
                        binding.icGoalSettingTwo.etGoalAchievement.setText(this[0]?.goal.toString())
                    }
                }
            } else {
                binding.icGoalSettingOne.tvGoalColorSetting.visibility = VISIBLE
                binding.icGoalSettingOne.viSelectedLevel.visibility = GONE
                binding.icGoalSettingOne.etGoalAchievement.setText("")
                binding.icGoalSettingTwo.tvGoalColorSetting.visibility = VISIBLE
                binding.icGoalSettingTwo.viSelectedLevel.visibility = GONE
                binding.icGoalSettingTwo.etGoalAchievement.setText("")
            }
        }
    }

    private fun initGoalPeriodUi() {
        val startDate = viewModel.getStartDate()
        val endDate = viewModel.getEndDate()

        if (startDate != null && startDate != 0L && endDate != null && endDate != 0L) {
            updateGoalPeriodText(startDate, endDate)
            binding.etDayOfDDay.setText(getDDay(startDate, endDate).toString())
        } else {
            binding.etDayOfDDay.setText("")
            binding.tvGoalAchievementPeriod.text = getString(R.string.default_y_m_d_tilde_y_m_d_dotted)
        }
    }

    private fun setUiClickListener() {
        // Back Button
        setBackButtonOnClickListener()

        // Reset Button
        setResetButtonOnClickListener()

        // Edit Button Click Listener
        setEditButtonOnClickListener()

        // Goal Click Listener Setting
        setGoalColorOnClickListener(binding.icGoalSettingOne, 0)
        setGoalColorOnClickListener(binding.icGoalSettingTwo, 1)
        setOnFocusChangeListener(binding.icGoalSettingOne)
        setOnFocusChangeListener(binding.icGoalSettingTwo)
    }

    private fun setBackButtonOnClickListener() {
        binding.btBackButton.setOnClickListener {
            (activity as BaseActivity).replaceFragment(GoalsFragment())
            (activity as BaseActivity).removeFragment(this)
        }
    }

    private fun setResetButtonOnClickListener() {
        binding.btResetButton.setOnClickListener {
            viewModel.resetData()
            initGoalUi()
            initGoalPeriodUi()
        }
    }

    private fun setEditButtonOnClickListener() {
        binding.btEditButton.setOnClickListener {
            viewModel.isValueEntered(
                binding.icGoalSettingOne.etGoalAchievement.text.toString(),
                binding.icGoalSettingTwo.etGoalAchievement.text.toString()
            )
        }
    }

    // Goal 설정 하는 UI Click Listener
    // Goal 의 Color 를 정하는 Dialog 띄우고, 선택한 Color 를 적용하는 코드
    private fun setGoalColorOnClickListener(includeGoalsSetting: IncludeGoalsSettingBinding, goalNumber: Int) {
        with (includeGoalsSetting) {
            val clickListener = OnClickListener {
                binding.root.hideSoftKeyboard()

                goalLevelDialog.show(tvGoalColorSetting) {
                    if (it != null) {
                        tvGoalColorSetting.visibility = INVISIBLE
                        viSelectedLevel.visibility = VISIBLE
                        viSelectedLevel.setBackgroundColor(it)
                    } else {
                        tvGoalColorSetting.visibility = VISIBLE
                        viSelectedLevel.visibility = GONE
                    }

                    val colorHex = it?.let { "#" + Integer.toHexString(it and 0x00ffffff).toString() }
                    viewModel.setGoalColor(goalNumber, colorHex)
                }
            }

            tvGoalColorSetting.setOnClickListener(clickListener)
            viSelectedLevel.setOnClickListener(clickListener)
        }
    }

    // Edit Text 에 Focusing 돼있을 경우 Edit Button 숨기기
    private fun setOnFocusChangeListener(includeGoalsSetting: IncludeGoalsSettingBinding) {
        includeGoalsSetting.etGoalAchievement.setOnFocusChangeListener { _, hasFocus ->
            binding.btEditButton.visibility = if (hasFocus) GONE else VISIBLE
        }
    }

    // Goal Calendar Setting
    private fun setCalendarDialog() {
        // DialogInit
        calendarDialog = CalendarDialog(requireContext())
        goalLevelDialog = GoalLevelDialog(requireContext())

        binding.ivCalendarImage.setOnClickListener {
            showCalendarDialog()
        }

        setupDayOfDDayTextChangeListener()
        setupDayOfDDayFocusChangeListener()
    }

    private fun showCalendarDialog() {
        binding.root.hideSoftKeyboard()

        calendarDialog.show(binding.ivCalendarImage, viewModel.getStartDate(), viewModel.getEndDate()) { startDate, endDate ->
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
        binding.tvGoalAchievementPeriod.text =
            getString(R.string.y_m_d_tilde_y_m_d_dotted, convertTimeMillisToCalendar(startDate), convertTimeMillisToCalendar(endDate))
    }

    private fun observingGoalsAchievementUiState() {
        viewModel.goalsAchievementDataUiState.observe(viewLifecycleOwner) {
            when(it) {
                GoalsAchievementUiState.GoalsAchievementSuccess ->
                    toast(requireContext(), getString(R.string.toast_completed_goals_update))
                GoalsAchievementUiState.GoalSettingSuccess ->
                    viewModel.uploadGoalAchievementDataToFirebaseDB()
                GoalsAchievementUiState.NotGoalSetting ->
                    toast(requireContext(), getString(R.string.toast_not_goal_setting))
                GoalsAchievementUiState.UnusualGoalSetting ->
                    toast(requireContext(), getString(R.string.toast_unusual_goal_setting))
                GoalsAchievementUiState.NotGoalPeriodSetting ->
                    toast(requireContext(), getString(R.string.toast_not_goal_period_setting))
                GoalsAchievementUiState.UnusualGoalPeriod ->
                    toast(requireContext(), getString(R.string.toast_unusual_goal_period))
                else -> {
                    //TODO:: API Failure 이 반환 된 경우 처리 필요
                }
            }
        }
    }
}