package com.ihavesookchi.climbingrecord.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
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
    private var isRecordEmpty = false

    init {
        isRecordEmpty = trackingClimbingRecords.isEmpty()
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ClimbTrackerAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_tracking_climbing_records, parent, false),
            parent.context
        )
    }

    override fun getItemCount() = if (trackingClimbingRecords.isNotEmpty()) trackingClimbingRecords.size else 1

    override fun onBindViewHolder(holder: ClimbTrackerAdapter.ViewHolder, position: Int) {
        holder.bind(
            if (!isRecordEmpty)
                trackingClimbingRecords[position]
            else  // tracking climbing record data 가 없는 경우
                GoalsDataResponse.TrackingClimbingRecord(),
            position
        )
    }

    inner class ViewHolder(
        private val view: View,
        private val context: Context
    ): RecyclerView.ViewHolder(view) {
        fun bind(trackingClimbingRecord: GoalsDataResponse.TrackingClimbingRecord, position: Int) {
            with (ItemTrackingClimbingRecordsBinding.bind(view)) {
                if (!isRecordEmpty) {
                    tvTrackingByDateType.visibility = VISIBLE
                    ivDateTypeSwitchButton.visibility = VISIBLE

                    tvTrackingByDateType.text = trackingClimbingRecord.dateType

                    ivDateTypeSwitchButton.setOnClickListener {
                        positionSwitchCallBack(if (position == 0) 1 else 0)
                    }
                } else {  // tracking climbing record data 가 없는 경우
                    tvTrackingByDateType.visibility = GONE
                    ivDateTypeSwitchButton.visibility = GONE
                }

                tvExerciseCount.text = trackingClimbingRecord.exerciseCount.toString()
                tvAchievementTotalCount.text = trackingClimbingRecord.achievementTotalCount.toString()
                tvExerciseTime.text = trackingClimbingRecord.exerciseTime.toString()

                setSVGColorFilter(ivDateTypeSwitchButton, R.color.svgFilterColorWhiteBlack, context)
            }
        }
    }
}