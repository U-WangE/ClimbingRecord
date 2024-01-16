package com.ihavesookchi.climbingrecord.adapter

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.data.response.GoalsDataResponse
import com.ihavesookchi.climbingrecord.databinding.ItemBarGraphBinding
import com.ihavesookchi.climbingrecord.util.CommonUtil
import com.ihavesookchi.climbingrecord.util.CommonUtil.setSVGColorFilter
import java.util.concurrent.TimeUnit

class GoalsAchievementBarGraphAdapter(
    private val getGoalsAchievementStatus: GoalsDataResponse.GoalsAchievementStatus
):
    RecyclerView.Adapter<GoalsAchievementBarGraphAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): GoalsAchievementBarGraphAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bar_graph, parent, false),
            parent.context)
    }

    override fun onBindViewHolder(
        holder: GoalsAchievementBarGraphAdapter.ViewHolder,
        position: Int,
    ) {
        if (position < getGoalsAchievementStatus.goalDetails.size)
            holder.bind(getGoalsAchievementStatus.goalDetails[position])
        else
            holder.bind()
    }

    override fun getItemCount(): Int {
        return getGoalsAchievementStatus.goalDetails.size + if (getGoalsAchievementStatus.startDate != 0L) 1 else 0
    }

    inner class ViewHolder(private val view: View, private val context: Context): RecyclerView.ViewHolder(view) {
        fun bind(goalDetail: GoalsDataResponse.GoalsAchievementStatus.GoalDetail ?= null) {
            with(ItemBarGraphBinding.bind(view)) {
                val constraintSet = ConstraintSet()

                constraintSet.clone(clBarGraphLayout)

                val horizontalBias: Float = if (goalDetail != null) {
                    tvEndingPointGoalText.text = goalDetail.goal.toString()
                    goalDetail.goalActual.toFloat() / goalDetail.goal.toFloat()
                } else {
                    tvEndingPointGoalText.text =
                        context.getString(R.string.y_m_d, getGoalsAchievementStatus.endDate)

                    val currentTimeDays = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()).toFloat()
                    val startDateDays = TimeUnit.MILLISECONDS.toDays(getGoalsAchievementStatus.startDate).toFloat()
                    val endDateDays = TimeUnit.MILLISECONDS.toDays(getGoalsAchievementStatus.endDate).toFloat()

                    (currentTimeDays - startDateDays) / (endDateDays - startDateDays)
                }.run {
                    if (this >= 1f) {
                        ivActualPointImage.setImageResource(R.drawable.ic_crown)
                        1f
                    } else {
                        setSVGColorFilter(ivActualPointImage, R.color.svgFilterColorWhiteBlack, context)
                        ivActualPointImage.setImageResource(R.drawable.ic_bot)
                        this
                    }
                }

                val valueAnimator = ValueAnimator.ofFloat(0f, horizontalBias)
                valueAnimator.addUpdateListener { animation ->
                    val animatedValue = animation.animatedValue as Float
                    constraintSet.setHorizontalBias(viActualPointBar.id, animatedValue)
                    constraintSet.applyTo(clBarGraphLayout)
                }

                valueAnimator.duration = 1000
                valueAnimator.interpolator = AccelerateDecelerateInterpolator()

                valueAnimator.start()
            }
        }
    }
}