package com.ihavesookchi.climbingrecord.view.goals

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.adapter.GoalsAchievementSettingAdapter
import com.ihavesookchi.climbingrecord.data.uistate.GoalsAchievementUiState
import com.ihavesookchi.climbingrecord.databinding.FragmentGoalsAchievementSettingBinding
import com.ihavesookchi.climbingrecord.util.CalendarDialog
import com.ihavesookchi.climbingrecord.util.CommonUtil.getDDay
import com.ihavesookchi.climbingrecord.util.CommonUtil.hideSoftKeyboard
import com.ihavesookchi.climbingrecord.util.CommonUtil.isSoftKeyboardShow
import com.ihavesookchi.climbingrecord.util.CommonUtil.setBackPressedCallback
import com.ihavesookchi.climbingrecord.util.CommonUtil.setGoalPeriod
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

    private lateinit var adapter: GoalsAchievementSettingAdapter

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

        setSVGColorFilter(binding.btBackButton, R.color.svgFilterColorMediumGrayDarkGray, requireContext())
        setSVGColorFilter(binding.ivCalendarImage, R.color.svgFilterColorLightGrayishBlack, requireContext())
        initGoalPeriodUi()

        observingGoalsAchievementUiState()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Goal Calendar Setting
        setGoalAchievementAdapter()
        setCalendarDialog()

        setUiClickListener()
    }

    private fun initGoalPeriodUi() {
        val startDate = viewModel.getStartDate()
        val endDate = viewModel.getEndDate()

        binding.tvGoalAchievementPeriod.setGoalPeriod(startDate, endDate)
        binding.etDayOfDDay.setText(getDDay(startDate, endDate).toString())
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

    private fun setGoalAchievementAdapter() {
        goalLevelDialog = GoalLevelDialog(requireContext())

        adapter = GoalsAchievementSettingAdapter(viewModel.getGoalDetails(), goalLevelDialog) { hasFocus ->
            binding.btEditButton.visibility = if (hasFocus) GONE else VISIBLE
        }
        binding.rvGoalsAchievementLayout.adapter = adapter
    }

    // Goal Calendar Setting
    private fun setCalendarDialog() {
        // DialogInit
        calendarDialog = CalendarDialog(requireContext())

        binding.ivCalendarImage.setOnClickListener {
            showCalendarDialog()
        }

        setupDayOfDDayTextChangeListener()
        setupDayOfDDayFocusChangeListener()
    }

    private fun showCalendarDialog() {
        binding.root.hideSoftKeyboard()

        calendarDialog.show(binding.ivCalendarImage, viewModel.getStartDate(), viewModel.getEndDate()) { startDate, endDate ->
            viewModel.setStartDate(startDate)
            viewModel.setEndDate(endDate)
            binding.tvGoalAchievementPeriod.setGoalPeriod(startDate, endDate)
            binding.etDayOfDDay.setText(getDDay(startDate, endDate).toString())
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

    // Day Of DDay에 입력된 날짜로 기간 업데이트
    private fun updateEndDateFromDayCount(editable: Editable?) {
        val input = editable?.toString() ?: return
        val days = input.toLongOrNull() ?: return

        val startDate = viewModel.getStartDate()
        val daysInMillis = TimeUnit.DAYS.toMillis(days)
        binding.tvGoalAchievementPeriod.setGoalPeriod(startDate, startDate + daysInMillis)
        if (startDate != 0L)
            viewModel.setEndDate(startDate + daysInMillis)
    }

    // Day Of DDay EditText 포커스 리스너 설정
    private fun setupDayOfDDayFocusChangeListener() {
        binding.etDayOfDDay.setOnFocusChangeListener { view, hasFocus ->
            binding.btEditButton.visibility = if (hasFocus) GONE else VISIBLE

            (view as EditText).let {
                if (it.text.isNullOrBlank()) it.setText("0")
            }
        }
    }

    private fun setUiClickListener() {
        // Back Button
        setBackButtonOnClickListener()
        // Reset Button
        setResetButtonOnClickListener()
        // Edit Button Click Listener
        setEditButtonOnClickListener()
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
//            initGoalUi() //TODO:: Adapter Refresh 하는 코드 적용 해야함
            //TODO::Reset 시 Period 적용 안 됨  // calendar 에서 startDate가 0으로 고정 되어 있음 해당 값 수정 필요
            initGoalPeriodUi()
        }
    }

    private fun setEditButtonOnClickListener() {
        binding.btEditButton.setOnClickListener {
            // setGoal 함수의 모든 기능을 수행 하면, isValueEntered 로 모든 값이 적합 한지 판단
            viewModel.setGoal(adapter.getItems()).run {
                viewModel.isValueEntered()
            }
        }
    }
}