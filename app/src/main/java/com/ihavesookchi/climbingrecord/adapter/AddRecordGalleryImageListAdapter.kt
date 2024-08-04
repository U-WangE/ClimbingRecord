package com.ihavesookchi.climbingrecord.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.databinding.ItemRecordDetailImageBinding


class AddRecordGalleryImageListAdapter(
    private val imageList: List<String>
) : RecyclerView.Adapter<AddRecordGalleryImageListAdapter.ViewHolder>() {
    private val CLASS_NAME = this::class.java.simpleName

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): AddRecordGalleryImageListAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_record_detail_image, parent, false)
        )
    }

    override fun getItemCount() = imageList.size

    override fun onBindViewHolder(holder: AddRecordGalleryImageListAdapter.ViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRecordDetailImageBinding.bind(view)
        fun bind(imagePath: String) {
            Glide.with(view.context)
                .load(imagePath)
                .into(binding.ivRecordImage)
        }
    }
}