package com.ihavesookchi.climbingrecord.adapter

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.data.response.GoalsDataResponse
import com.ihavesookchi.climbingrecord.databinding.ItemBarGraphBinding
import com.ihavesookchi.climbingrecord.util.CommonUtil.setSVGColorFilter
import java.util.concurrent.TimeUnit

class GoalsAchievementBarGraphAdapter(
    private val getGoalsAchievementStatus: GoalsDataResponse.GoalsAchievementStatus
): RecyclerView.Adapter<GoalsAchievementBarGraphAdapter.ViewHolder>() {
    private val CLASS_NAME = this::class.java.simpleName

    private var isGoalsEmpty = false

    init {
        isGoalsEmpty = getGoalsAchievementStatus.goalDetails.isEmpty()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): GoalsAchievementBarGraphAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_bar_graph, parent, false),
            parent.context
        )
    }

    override fun getItemCount(): Int {
        return if (!isGoalsEmpty)
            getGoalsAchievementStatus.goalDetails.size + if (getGoalsAchievementStatus.startDate != 0L) 1 else 0
        else  // goal achievement data 가 없는 경우
            2
    }

    override fun onBindViewHolder(
        holder: GoalsAchievementBarGraphAdapter.ViewHolder,
        position: Int,
    ) {
        if (!isGoalsEmpty) {
            if (position < getGoalsAchievementStatus.goalDetails.size)
                holder.bind(getGoalsAchievementStatus.goalDetails[position])
            else // 기간 bar graph 표시용
                holder.bind()
        } else {  // goal achievement data 가 없는 경우
            holder.bind(position = position)
        }
    }

    inner class ViewHolder(private val view: View, private val context: Context): RecyclerView.ViewHolder(view) {
        fun bind(goalDetail: GoalsDataResponse.GoalsAchievementStatus.GoalDetail? = null, position: Int = 0) {
            with(ItemBarGraphBinding.bind(view)) {
                val constraintSet = ConstraintSet()

                constraintSet.clone(clBarGraphLayout)

                if (!isGoalsEmpty) {
                    // Actual Point 의 위치를 비율로 나타낸 값
                    val horizontalBias: Float =
                        if (goalDetail != null) {
                            setSVGColorFilter(ivActualPointImage, R.color.svgFilterColorLightGrayishBlack, context)
                            ivActualPointImage.setImageResource(R.drawable.ic_bot)

                            val color = Color.parseColor(goalDetail.goalColorRGB)
                            viHorizontalLine.setBackgroundColor(color)
                            viActualPointBar.setBackgroundColor(color)
                            viStartingPointBar.setBackgroundColor(color)
                            viEndingPointBar.setBackgroundColor(color)

                            tvActualPointGoalText.text = goalDetail.goalActual.toString()
                            tvEndingPointGoalText.text = goalDetail.goal.toString()

                            // 반환값
                            goalDetail.goalActual.toFloat() / goalDetail.goal.toFloat()
                        } else {
                            setSVGColorFilter(ivActualPointImage, R.color.svgFilterColorLightGrayishBlack, context)
                            ivActualPointImage.setImageResource(R.drawable.ic_stopwatch)

                            tvEndingPointGoalText.text =
                                context.getString(R.string.y_m_d, getGoalsAchievementStatus.endDate)

                            val currentTimeDays =
                                TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis()).toFloat()
                            val startDateDays =
                                TimeUnit.MILLISECONDS.toDays(getGoalsAchievementStatus.startDate)
                                    .toFloat()
                            val endDateDays =
                                TimeUnit.MILLISECONDS.toDays(getGoalsAchievementStatus.endDate)
                                    .toFloat()

                            // 반환값
                            if (startDateDays <= currentTimeDays)
                                (currentTimeDays - startDateDays) / (endDateDays - startDateDays)
                            else 0f
                        }.run {
                            // this == 반환값
                            if (this >= 1f) {
                                ivActualPointImage.setImageResource(R.drawable.ic_crown)
                                1f
                            } else {
                                this
                            }
                        }

                    ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "Actual Point layout_constraintHorizontal_bias   horizontalBias  :  $horizontalBias")

                    /*
                    비율(horizontalBias) 에 따른 layout_constraintHorizontal_bias 값 적용
                    0부터 Actual bias 까지 이동 하는 Animation
                     */
                    val valueAnimator = ValueAnimator.ofFloat(0f, horizontalBias)
                    valueAnimator.addUpdateListener { animation ->
                        val animatedValue = animation.animatedValue as Float
                        constraintSet.setHorizontalBias(viActualPointBar.id, animatedValue)
                        constraintSet.applyTo(clBarGraphLayout)
                    }

                    valueAnimator.duration = 1000
                    valueAnimator.interpolator = AccelerateDecelerateInterpolator()

                    valueAnimator.start()
                } else {
                    // goal achievement data 가 없는 경우
                    setSVGColorFilter(ivActualPointImage, R.color.svgFilterColorLightGrayishBlack, context)
                    ivActualPointImage.setImageResource(
                        if (position == 0) R.drawable.ic_bot else R.drawable.ic_stopwatch
                    )

                    constraintSet.setHorizontalBias(viActualPointBar.id, 0f)
                    constraintSet.applyTo(clBarGraphLayout)
                }
            }
        }
    }
}