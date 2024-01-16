package com.ihavesookchi.climbingrecord.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearSnapHelper
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.adapter.ClimbTrackerAdapter
import com.ihavesookchi.climbingrecord.adapter.GoalsAchievementBarGraphAdapter
import com.ihavesookchi.climbingrecord.data.uistate.GoalsDataUiState
import com.ihavesookchi.climbingrecord.databinding.FragmentGoalsBinding
import com.ihavesookchi.climbingrecord.util.CommonUtil.setSVGColorFilter
import com.ihavesookchi.climbingrecord.viewModel.BaseViewModel
import com.ihavesookchi.climbingrecord.viewModel.GoalsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoalsFragment : Fragment() {
    private var _binding: FragmentGoalsBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: BaseViewModel by activityViewModels()
    private val viewModel: GoalsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)

        viewModel.goalsApi()

        observingGoalsDataUiState()

        return binding.root
    }

    private fun observingGoalsDataUiState() {
        viewModel.goalsDataUiState.observe(viewLifecycleOwner) {
            when(it) {
                is GoalsDataUiState.GoalsDataSuccess -> {
                    setGoalsAchievement()
                    setClimbTracker()
                    setGoalsAchievementGraph()
                }
                else -> {}
            }
        }
    }

    private fun setGoalsAchievement() {
        // 목표 및 달성율 Ui에 해당 하는 기능
        setDDay()
        setGoalsAchievementDetail()
        setGoalsAchievementPeriod()
        intentGoalsAchievementSetting()
        setSVGColorFilter(binding.icGoalsStatus.ivGoalsModify, R.color.svgFilterColorWhiteBlack, requireContext())
    }

    private fun setDDay() {
        val dDay = viewModel.getGoalsDDay()

        //TODO:: 추후 색 지정 필요
        binding.icGoalsStatus.tvDayOfDDay.setTextColor(
            getColor(requireContext(),
                when {
                    dDay < 3L -> R.color.purple_700
                    dDay < 7L -> R.color.purple_200
                    else -> R.color.white
                }
            )
        )

        binding.icGoalsStatus.tvDayOfDDay.text = dDay.toString()

    }

    private fun setGoalsAchievementDetail() {
        with(binding.icGoalsStatus) {
            val goalImageList = listOf(ivFirstGoalAchievementImage, ivSecondGoalAchievementImage)
            val goalStatusList = listOf(tvFirstGoalAchievementStatus, tvSecondGoalAchievementStatus)
            val getGoalDetails = viewModel.getGoalDetails()

            if (getGoalDetails.isNotEmpty())
                for (i in getGoalDetails.indices) {
                    getGoalDetails[i].run {
                        setSVGColorFilter(goalImageList[i], goalColorRGB)
                        goalStatusList[i].text = getString(R.string.number_out_of_number, goalActual, goal)
                        goalStatusList[i].setTextColor(getColor(requireContext(), if (goalActual == goal) R.color.purple_200 else R.color.white))
                    }
                    setSecondGoalVisibility(i > 0)
                }
            else {
                resetFirstGoal()
            }
        }
    }
    private fun resetFirstGoal() {
        with(binding.icGoalsStatus) {
            ivFirstGoalAchievementImage.colorFilter = null
            tvFirstGoalAchievementStatus.text = getString(R.string.default_hyphen_out_of_hyphen)
        }
    }
    private fun setSecondGoalVisibility(isVisible : Boolean) {
        with(binding.icGoalsStatus) {
            listOf(tvCommas, ivSecondGoalAchievementImage, tvSecondGoalAchievementStatus).forEach {
                it.visibility = if (isVisible) VISIBLE else GONE
            }
        }
    }

    private fun setGoalsAchievementPeriod() {
        binding.icGoalsStatus.tvGoalAchievementPeriod.text =
            getString(R.string.y_m_d_tilde_y_m_d_slash, viewModel.getStartDate(), viewModel.getEndDate())
    }


    // Goals Setting Activity 로 이동
    private fun intentGoalsAchievementSetting() {
        binding.icGoalsStatus.ivGoalsModify.setOnClickListener {
            startActivity(Intent(requireContext(), GoalsAchievementSettingActivity::class.java))
        }
    }

    private fun setClimbTracker() {
        // 달, 년 별 Climbing 기록 Ui에 해당 하는 기능
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvTrackingClimbingRecords)
        binding.rvTrackingClimbingRecords.adapter = ClimbTrackerAdapter(viewModel.getTrackingClimbingRecords()) {
            binding.rvTrackingClimbingRecords.smoothScrollToPosition(it)
        }
    }

    private fun setGoalsAchievementGraph() {
        binding.rvBarGraph.adapter = GoalsAchievementBarGraphAdapter(viewModel.getGoalsAchievementStatus())
    }
}