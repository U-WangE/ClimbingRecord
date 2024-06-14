package com.ihavesookchi.climbingrecord.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.data.response.RecordsDataResponse
import com.ihavesookchi.climbingrecord.databinding.ItemRecordListAchievementBinding

class RecordListAchievementAdapter(
    private val achievementDataList: List<RecordsDataResponse.Record.AchievementData>
): RecyclerView.Adapter<RecordListAchievementAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecordListAchievementAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_record_list_achievement, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecordListAchievementAdapter.ViewHolder, position: Int) {
        holder.bind(achievementDataList[position], position)
    }

    override fun getItemCount() = if (achievementDataList.size > 3) 3 else achievementDataList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)  {
        val binding = ItemRecordListAchievementBinding.bind(view)
        fun bind(achievementData: RecordsDataResponse.Record.AchievementData, position: Int) {
            binding.ivAchievementImage.setBackgroundColor(Color.parseColor(achievementData.achievementColorRGB))
            binding.tvAchievementQuantity.text = achievementData.achievementQuantity.toString()

            if (position == achievementDataList.size - 1)
                binding.tvCommas.visibility = GONE
        }
    }
}