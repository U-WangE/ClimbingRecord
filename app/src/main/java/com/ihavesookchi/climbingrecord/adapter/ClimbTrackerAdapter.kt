package com.ihavesookchi.climbingrecord.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.data.response.GoalsDataResponse
import com.ihavesookchi.climbingrecord.databinding.ItemTrackingClimbingRecordsBinding
import com.ihavesookchi.climbingrecord.util.CommonUtil.setSVGColorFilter

class ClimbTrackerAdapter(
    private val trackingClimbingRecords: List<GoalsDataResponse.TrackingClimbingRecord>,
    private val positionSwitchCallBack: (Int) -> Unit
): RecyclerView.Adapter<ClimbTrackerAdapter.ViewHolder>() {
    private val CLASS_NAME = this::class.java.simpleName
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClimbTrackerAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_tracking_climbing_records, parent, false),
            parent.context
        )
    }

    override fun getItemCount() = trackingClimbingRecords.size

    override fun onBindViewHolder(holder: ClimbTrackerAdapter.ViewHolder, position: Int) {
        holder.bind(trackingClimbingRecords[position], position)
    }

    inner class ViewHolder(
        private val view: View,
        private val context: Context
    ): RecyclerView.ViewHolder(view) {
        fun bind(trackingClimbingRecord: GoalsDataResponse.TrackingClimbingRecord, position: Int ) {
            with (ItemTrackingClimbingRecordsBinding.bind(view)) {
                tvTrackingByDateType.text = trackingClimbingRecord.dateType
                tvExerciseCount.text = trackingClimbingRecord.exerciseCount.toString()
                tvAchievementTotalCount.text = trackingClimbingRecord.achievementTotalCount.toString()
                tvExerciseTime.text = trackingClimbingRecord.exerciseTime.toString()
                setSVGColorFilter(ivDateTypeSwitchButton, R.color.svgFilterColorWhiteBlack, context)
                ivDateTypeSwitchButton.setOnClickListener {
                    positionSwitchCallBack(if (position == 0) 1 else 0)
                }
            }
        }
    }
}