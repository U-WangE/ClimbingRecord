package com.ihavesookchi.climbingrecord.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.data.response.GoalsDataResponse

class ClimbTrackerAdapter(
    private val trackingClimbingRecords: List<GoalsDataResponse.TrackingClimbingRecord>
): RecyclerView.Adapter<ClimbTrackerAdapter.ViewHolder>() {
    private val CLASS_NAME = this::class.java.simpleName
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClimbTrackerAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.include_tracking_climbing_records, parent, false)
        )
    }

    override fun getItemCount() = trackingClimbingRecords.size

    override fun onBindViewHolder(holder: ClimbTrackerAdapter.ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(
        view: View
    ): RecyclerView.ViewHolder(view) {

        fun bind() {

        }
    }
}