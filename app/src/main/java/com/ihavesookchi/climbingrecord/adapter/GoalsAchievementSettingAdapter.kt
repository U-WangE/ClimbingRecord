package com.ihavesookchi.climbingrecord.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.data.response.GoalsDataResponse
import com.ihavesookchi.climbingrecord.databinding.IncludeGoalsSettingBinding
import com.ihavesookchi.climbingrecord.util.CommonUtil.hideSoftKeyboard
import com.ihavesookchi.climbingrecord.util.GoalLevelDialog

class GoalsAchievementSettingAdapter(
    private var goalDetailList: List<GoalsDataResponse.GoalsAchievementStatus.GoalDetail>,
    private val goalLevelDialog: GoalLevelDialog,
    private val focusChangeCallback: (Boolean) -> Unit
): RecyclerView.Adapter<GoalsAchievementSettingAdapter.ViewHolder>() {

    private val CLASS_NAME = this::class.java.simpleName

    private val modifyGoalDetailList = arrayListOf<GoalsDataResponse.GoalsAchievementStatus.GoalDetail>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): GoalsAchievementSettingAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.include_goals_setting, parent, false)
        )
    }

    override fun getItemCount() = if (goalDetailList.size > 2) goalDetailList.size else 2

    override fun onBindViewHolder(
        holder: GoalsAchievementSettingAdapter.ViewHolder,
        position: Int,
    ) {
        holder.bind(
            if (position in goalDetailList.indices)
                goalDetailList[position]
            else
                GoalsDataResponse.GoalsAchievementStatus.GoalDetail(),
            position
        )
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = IncludeGoalsSettingBinding.bind(view)

        fun bind(goalDetail: GoalsDataResponse.GoalsAchievementStatus.GoalDetail, position: Int) {
            init(goalDetail)

            setOnClickListener(position)
            setOnEdit(position)
            setOnFocusChangeListener()
        }

        private fun init(goalDetail: GoalsDataResponse.GoalsAchievementStatus.GoalDetail) {
            with(binding) {
                if (goalDetail.goalColorRGB.isNotBlank() || goalDetail.goal != 0) {
                    tvGoalColorSetting.visibility = INVISIBLE
                    viSelectedLevel.visibility = VISIBLE
                    viSelectedLevel.setBackgroundColor(Color.parseColor(goalDetail.goalColorRGB))
                } else {
                    tvGoalColorSetting.visibility = VISIBLE
                    viSelectedLevel.visibility = GONE
                    viSelectedLevel.background = null
                }

                etGoalAchievement.setText(goalDetail.goal.toString())
                modifyGoalDetailList.add(goalDetail)
            }
        }

        private fun setOnClickListener(position: Int) {
            with(binding) {
                val clickListener = View.OnClickListener {
                    ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "GoalsAchievementSettingAdapter setOnClickListener onClickListener   click position  :  $position")
                    binding.root.hideSoftKeyboard()

                    goalLevelDialog.show(tvGoalColorSetting) {
                        if (!it.isNullOrBlank()) {
                            tvGoalColorSetting.visibility = INVISIBLE
                            viSelectedLevel.visibility = VISIBLE
                            viSelectedLevel.setBackgroundColor(Color.parseColor(it))
                            modifyGoalDetailList[position].goalColorRGB = it
                        } else {
                            tvGoalColorSetting.visibility = VISIBLE
                            viSelectedLevel.visibility = GONE
                            viSelectedLevel.background = null
                            modifyGoalDetailList[position].goalColorRGB = ""
                        }
                    }
                }

                tvGoalColorSetting.setOnClickListener(clickListener)
                viSelectedLevel.setOnClickListener(clickListener)
            }
        }

        private fun setOnFocusChangeListener() {
            binding.etGoalAchievement.setOnFocusChangeListener { _, hasFocus ->
                focusChangeCallback(hasFocus)
            }
        }

        private fun setOnEdit(position: Int) {
            binding.etGoalAchievement.addTextChangedListener(object: TextWatcher {
                override fun beforeTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(editable: Editable?) {
                    try {
                        modifyGoalDetailList[position].goal = editable?.toString()?.toInt()?:0
                    } catch (e: NumberFormatException) {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "setOnEdit(position) etGoalAchievement.text.toInt   NumberFormatException  :  $e")
                    }
                }
            })
        }
    }

    fun getItems(): List<GoalsDataResponse.GoalsAchievementStatus.GoalDetail> {
        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "Adapter getItems() return   Result  :  $modifyGoalDetailList")
        return modifyGoalDetailList
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newGoalDetailList: List<GoalsDataResponse.GoalsAchievementStatus.GoalDetail>) {
        goalDetailList = newGoalDetailList
        notifyDataSetChanged()
    }
}