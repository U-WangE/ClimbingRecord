package com.ihavesookchi.climbingrecord.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.data.response.RecordsDataResponse
import com.ihavesookchi.climbingrecord.databinding.ItemRecordListBinding
import com.ihavesookchi.climbingrecord.util.CommonUtil.convertTimeMillisToCalendar
import com.ihavesookchi.climbingrecord.util.CommonUtil.setSVGColorFilter
import com.ihavesookchi.climbingrecord.util.ImageLoadTask
import com.ihavesookchi.climbingrecord.view.BaseActivity
import com.ihavesookchi.climbingrecord.view.records.RecordDetailFragment

class RecordListAdapter(
    private val recordList: List<RecordsDataResponse.Record>,
    private val activity: BaseActivity
): RecyclerView.Adapter<RecordListAdapter.ViewHolder>() {

    private val CLASS_NAME = this::class.java.simpleName

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecordListAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_record_list, parent, false),
            parent.context
        )
    }

    override fun getItemCount() = recordList.size

    override fun onBindViewHolder(
        holder: RecordListAdapter.ViewHolder,
        position: Int,
    ) {
        holder.bind(recordList[position])
    }

    inner class ViewHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {
        val binding = ItemRecordListBinding.bind(view)
        fun bind(record: RecordsDataResponse.Record) {
            // item image setting
            if (record.recordImages.isNotEmpty())
                ImageLoadTask(binding.ivRecordImages).loadImage(record.recordImages[0])
            else {
                binding.ivRecordImages.setImageResource(R.drawable.ic_photo)
                setSVGColorFilter(binding.ivRecordImages, R.color.svgFilterColorMediumGrayDarkGray, context)
            }

            binding.ivRecordImageMoreIcon.visibility = if (record.recordImages.size > 1) VISIBLE else GONE
            binding.ivRecordImageMoreIcon.visibility = if (record.recordImages.size > 1) VISIBLE else GONE

            binding.tvRecordContents.text = record.content
            binding.tvAchievementDate.text = context.getString(R.string.y_m_d_slash, convertTimeMillisToCalendar(record.achievementDate))

            // item achievement setting
            binding.rvAchievementLayout.adapter = RecordListAchievementAdapter(record.achievementDataList)

            binding.clRecordListItemLayout.setOnClickListener {
                activity.addFragment(RecordDetailFragment())
            }
        }
    }
}