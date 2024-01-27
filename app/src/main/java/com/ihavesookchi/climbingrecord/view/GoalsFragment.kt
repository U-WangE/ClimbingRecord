package com.ihavesookchi.climbingrecord.view

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
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
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.adapter.ClimbTrackerAdapter
import com.ihavesookchi.climbingrecord.adapter.GoalsAchievementBarGraphAdapter
import com.ihavesookchi.climbingrecord.data.uistate.GoalsDataUiState
import com.ihavesookchi.climbingrecord.databinding.FragmentGoalsBinding
import com.ihavesookchi.climbingrecord.util.CommonUtil.setSVGColorFilter
import com.ihavesookchi.climbingrecord.util.CommonUtil.toast
import com.ihavesookchi.climbingrecord.viewModel.BaseViewModel
import com.ihavesookchi.climbingrecord.viewModel.GoalsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoalsFragment : Fragment() {
    private var _binding: FragmentGoalsBinding? = null
    private val binding get() = _binding!!

    private val CLASS_NAME = this::class.java.simpleName

    private val sharedViewModel: BaseViewModel by activityViewModels()
    private val viewModel: GoalsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)

        setDefaultUISetting()

        viewModel.getFirebaseGoalsData()

        observingGoalsDataUiState()

        return binding.root
    }
    private fun setDefaultUISetting() {
        binding.icGoalsStatus.clGoalsStatusLayout.visibility = GONE

        setSVGColorFilter(binding.icGoalsStatus.ivGoalsModify, R.color.svgFilterColorWhiteBlack, requireContext())
        setSVGColorFilter(binding.icProfile.ivProfileImage, R.color.svgFilterColorSteelGrayMediumGray, requireContext())
        setSVGColorFilter(binding.icProfile.ivInstagramSetButton, R.color.svgFilterColorWhiteBlack, requireContext())
        setSVGColorFilter(binding.icProfile.ivModify, R.color.svgFilterColorSteelGrayMediumGray, requireContext())
        intentInstagramSetting()
        intentGoalsAchievementSetting()
    }

    private fun observingGoalsDataUiState() {
        viewModel.goalsDataUiState.observe(viewLifecycleOwner) {

            binding.icGoalsStatus.clGoalsStatusLayout.visibility = VISIBLE

            setProfile()
            setClimbTracker()
            setGoalsAchievementGraph()
            setGoalsAchievement()

            when(it) {
                is GoalsDataUiState.GoalsDataSuccess -> {
                    setVisibilityOfGoalAchievement(VISIBLE)
                }
                is GoalsDataUiState.GoalsDataIsNull -> {
                    setVisibilityOfGoalAchievement(GONE)
                }
                else -> {}
            }
        }
    }

    private fun setProfile() {
//        viewModel.getProfileImage()
        setInstagramLink()
//        viewModel.setNm
    }

    private fun setInstagramLink() {
        //TODO:: 인스타그램 id 세팅
    }


    private fun intentInstagramSetting() {
        binding.icProfile.llInstagramLayout.setOnClickListener {
            val instagramUserName = binding.icProfile.tvInstagramUserName.text
            if (instagramUserName != getString(R.string.hint_link_to_instagram)) {
                intentInstagram(instagramUserName)
            } else {
                toast(requireContext(), getString(R.string.toast_input_instagram_user_name))

                ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "setInstagramLink()   instagram 사용자 이름이 입력되지 않았습니다.")
            }
        }
    }

    /*
    * Instagram 설치 유/무에 따라 앱/웹에서 열기
    */
    private fun intentInstagram(instagramUserName: CharSequence) {
        if (isInstagramAppInstalled()) {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://instagram.com/_u/$instagramUserName")
            intent.setPackage("com.instagram.android")
            startActivity(intent)
        } else {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("http://instagram.com/$instagramUserName")
            startActivity(intent)
        }
    }

    private fun isInstagramAppInstalled(): Boolean {
        val packageManager = requireContext().packageManager
        return try {
            packageManager.getPackageInfo("com.instagram.android", PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }


    /**
     * 이번 월,년 운동 Layout
     **/
    /*
    이번달, 이번연도  운동 시간, 완등 개수, 운동 횟수 UI
    switch button 클릭시 Month <-> Year
     */
    private fun setClimbTracker() {
        // 달, 년 별 Climbing 기록 Ui에 해당 하는 기능
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvTrackingClimbingRecords)
        binding.rvTrackingClimbingRecords.adapter = ClimbTrackerAdapter(viewModel.getTrackingClimbingRecords()) {
            binding.rvTrackingClimbingRecords.smoothScrollToPosition(it)
        }
    }

    /**
     * D-Day Layout
     **/
    // DB에 데이터가 없는 경우 Default 문구 보여줌
    private fun setVisibilityOfGoalAchievement(visibility: Int) {
        with(binding) {
            when (visibility) {
                VISIBLE -> {
                    icGoalsStatus.clGoalsAchievementLayout.visibility = VISIBLE
                    icGoalsStatus.tvAdviseSettingGoal.visibility = GONE
                }
                GONE -> {
                    icGoalsStatus.clGoalsAchievementLayout.visibility = GONE
                    icGoalsStatus.tvAdviseSettingGoal.visibility = VISIBLE
                }

                else -> {}
            }
        }
    }

    private fun setGoalsAchievement() {
        // 목표 및 달성율 Ui에 해당 하는 기능
        if (viewModel.getGoalDetails().isNotEmpty()) {
            setDDay()
            setGoalsAchievementDetail()
            setGoalsAchievementPeriod()
        } else {  // goal achievement data 가 없는 경우
            setVisibilityOfGoalAchievement(GONE)
        }
    }

    // D-Day Setting || 3, 7일 마다 색 변경 적용
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

    /*
     (Icon : Actual / Goal)
     작성한 Goal 에 따른 UI Setting
     Goal 달성 정도에 따른 색 변화
     Goal 없는 경우 처리
     */

    private fun setGoalsAchievementDetail() {
        with(binding.icGoalsStatus) {
            val goalImageList = listOf(ivFirstGoalAchievementImage, ivSecondGoalAchievementImage)
            val goalStatusList = listOf(tvFirstGoalAchievementStatus, tvSecondGoalAchievementStatus)
            val getGoalDetails = viewModel.getGoalDetails()

            for (i in getGoalDetails.indices) {
                getGoalDetails[i].run {
                    setSVGColorFilter(goalImageList[i], goalColorRGB)
                    goalStatusList[i].text = getString(R.string.number_out_of_number, goalActual, goal)
                    goalStatusList[i].setTextColor(getColor(requireContext(), if (goalActual == goal) R.color.purple_200 else R.color.white))
                }
                setSecondGoalVisibility(i > 0)
            }
        }
    }
    private fun setSecondGoalVisibility(isVisible : Boolean) {
        with(binding.icGoalsStatus) {
            listOf(tvCommas, ivSecondGoalAchievementImage, tvSecondGoalAchievementStatus).forEach {
                it.visibility = if (isVisible) VISIBLE else GONE
            }
        }
    }

    // yyyy/mm/dd ~ yyyy/mm/dd
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

    /**
     * D-Day Graph
     **/
    // Goals, Goals Period 의 달성, 진행율 표시 Bar Graph
    private fun setGoalsAchievementGraph() {
        binding.rvBarGraph.adapter = GoalsAchievementBarGraphAdapter(viewModel.getGoalsAchievementStatus())
    }
}