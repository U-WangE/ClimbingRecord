package com.ihavesookchi.climbingrecord.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.data.response.SearchKeywordResponse
import com.ihavesookchi.climbingrecord.databinding.ItemSearchListBinding
import com.ihavesookchi.climbingrecord.databinding.ItemSearchListEmptyBinding
import kotlin.math.min

class SearchListAdapter(
    private val keyword: String,
    private val climbingCenters: List<SearchKeywordResponse.Document>,
    private val onItemSelected: (SearchKeywordResponse.Document) -> Unit
): RecyclerView.Adapter<SearchListAdapter.ViewHolder>() {
    private val CLASS_NAME = this::class.java.simpleName

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchListAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(
                    if (climbingCenters.isNotEmpty())
                        R.layout.item_search_list
                    else
                        R.layout.item_search_list_empty
                    , parent, false
                ),
            parent.context
        )
    }

    override fun getItemCount() = if (climbingCenters.isNotEmpty()) min(climbingCenters.size, 5) else 1

    override fun onBindViewHolder(holder: SearchListAdapter.ViewHolder, position: Int) {
        try {
            if (climbingCenters.isNotEmpty())
                holder.bind(climbingCenters[position])
            else
                holder.emptyBind()
        } catch (e: Exception) {
            ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "onBindViewHolder Error    e : $e")
        }
    }

    inner class ViewHolder(
        private val view: View,
        private val context: Context
    ): RecyclerView.ViewHolder(view) {
        fun bind(climbingCenter: SearchKeywordResponse.Document) {
            with (ItemSearchListBinding.bind(view)) {
                climbingCenter.let { center ->
                    tvClimbingCenterName.text = center.placeName
                    tvAddress.text = center.roadAddressName.ifEmpty { center.addressName }

                    clClimbingCenterLayout.setOnClickListener {
                        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, " Climbing Center Item Select Event    center  :  $center")

                        onItemSelected(center)
                    }
                }
            }
        }

        fun emptyBind() {
            with (ItemSearchListEmptyBinding.bind(view)) {
                tvNoticeNoSearchResult.text = context.getString(R.string.notice_no_search_result_for_keyword, keyword)
            }
        }
    }
}