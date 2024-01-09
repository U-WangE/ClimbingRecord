package com.ihavesookchi.climbingrecord.view

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.data.response.GoalsDataResponse
import com.ihavesookchi.climbingrecord.data.uistate.GoalsDataUiState
import com.ihavesookchi.climbingrecord.databinding.FragmentGoalsBinding
import com.ihavesookchi.climbingrecord.viewModel.BaseViewModel
import com.ihavesookchi.climbingrecord.viewModel.GoalsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

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
                }
                else -> {}
            }
        }
    }

    private fun setGoalsAchievement() {
        // goalsAchievement Ui에 해당 하는 기능
        setDDay()
        setGoalsAchievementDetail()
        setGoalsAAchievementPeriod()
        intentGoalsAchievementSetting()
    }

    private fun setDDay() {
        val dDay = TimeUnit.MILLISECONDS.toDays(viewModel.getEndDate() - viewModel.getStartDate())

        //TODO:: 추후 색 지정 필요
        binding.icGoalsStatus.tvDayOfDDay.setTextColor(
            getColor(requireContext(),
                when {
                    dDay < 3L -> R.color.purple_700
                    dDay < 7L -> R.color.purple_200
                    else -> R.color.black
                }
            )
        )

        binding.icGoalsStatus.tvDayOfDDay.text = dDay.toString()

    }

    private fun setGoalsAchievementDetail() {
        with(binding.icGoalsStatus) {
            val goalImageList = listOf(ivFirstGoalAchievementImage, ivSecondGoalAchievementImage)
            val goalStatusList = listOf(tvFirstGoalAchievementStatus, tvSecondGoalAchievementStatus)
            val goalsDetails = viewModel.getGoalsDetails()

            if (goalsDetails.isNotEmpty())
                for (i in goalsDetails.indices) {
                    goalsDetails[i].run {
                        goalImageList[i].setColorFilter(Color.parseColor(goalColorRGB), PorterDuff.Mode.SRC_IN)
                        goalStatusList[i].text = getString(R.string.number_out_of_number, goalActual, goal)
                        goalStatusList[i].setTextColor(getColor(requireContext(), if (goalActual == goal) R.color.purple_200 else R.color.black))
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


    private fun setGoalsAAchievementPeriod() {
        binding.icGoalsStatus.tvFirstGoalAchievementStatus.text =
            getString(R.string.y_m_d_tilde_y_m_d_slash, viewModel.getStartDate(), viewModel.getEndDate())
    }


    // Goals Setting Activity 로 이동
    private fun intentGoalsAchievementSetting() {
        binding.icGoalsStatus.ivGoalsModify.setOnClickListener {
            startActivity(Intent(requireContext(), GoalsAchievementSettingActivity::class.java))
        }
    }
}