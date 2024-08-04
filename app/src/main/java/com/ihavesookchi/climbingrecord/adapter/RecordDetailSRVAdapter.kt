package com.ihavesookchi.climbingrecord.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.databinding.ItemRecordDetailImageBinding
import com.ihavesookchi.climbingrecord.util.CommonUtil.getWindowWidth


class RecordDetailSRVAdapter(
    private val recordImages: List<String>
) : RecyclerView.Adapter<RecordDetailSRVAdapter.ViewHolder>() {
    private val CLASS_NAME = this::class.java.simpleName

    private val colors: MutableList<Int>

    private var widthDiffItemToWindow = 0
    private var isWidthCalculated = false

    //TODO:: Example
    init {
        colors = ArrayList()
        colors.add(Color.RED)
        colors.add(Color.CYAN)
        colors.add(Color.YELLOW)
        colors.add(Color.GREEN)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecordDetailSRVAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_record_detail_image, parent, false)
        )
    }

    override fun getItemCount() = 5

    override fun onBindViewHolder(holder: RecordDetailSRVAdapter.ViewHolder, position: Int) {
        /** item 과 window 의 width 의 차
         * 처음 RecyclerView 가 실행 되었을 때에만,
         * 0 position 의 item width 만 정확히 표시됨
         * RecyclerView 의 재사용성으로 현 View 에 표시 되지 않은 값은
         * 정확이 width 가 표기 되지 않는 것으로 보임
         * 또한, 해당 차 하나만 있으면, 이후 item 의 차는 필요 없는 것으로 판단
         **/
        if (position == 0 && !isWidthCalculated) {
            holder.itemView.post {
                val itemWidth = holder.itemView.width
                val windowWidth = getWindowWidth()
                widthDiffItemToWindow = windowWidth - itemWidth
                isWidthCalculated = true

                ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME,
                    "setStartEndMargin(view, position)   \n" +
                            "itemWidth : $windowWidth\n" +
                            "windowWidth : $windowWidth\n" +
                            "widthDiffItemToWindow : $widthDiffItemToWindow\n" +
                            "isWidthCalculated : $isWidthCalculated")
            }
        }

        holder.bind(position)
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRecordDetailImageBinding.bind(view)
        fun bind(position: Int) {
            val backgroundColor = colors[position % colors.size]
            binding.ivRecordImage.setBackgroundColor(backgroundColor)

            setStartEndMargin(view, position)
        }
    }

    // first, last item 의 start, end Margin 설정
    private fun setStartEndMargin(view: View, position: Int) {
        view.post {
            val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams

            if (position == 0)
                layoutParams.marginStart = widthDiffItemToWindow / 2
            else if (position == 4)
                layoutParams.marginEnd = widthDiffItemToWindow / 2

            view.layoutParams = layoutParams
        }
    }
}