package com.ihavesookchi.climbingrecord.view.records

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.ihavesookchi.climbingrecord.adapter.RecordDetailSRVAdapter
import com.ihavesookchi.climbingrecord.databinding.FragmentRecordDetailBinding
import com.ihavesookchi.climbingrecord.util.CommonUtil.dpToPx
import com.ihavesookchi.climbingrecord.util.recyclerView.ItemOffsetDecoration


class RecordDetailFragment : Fragment() {
    private var _binding: FragmentRecordDetailBinding? = null
    private val binding get() = _binding!!

    private val CLASS_NAME = this::class.java.simpleName

//    private val sharedViewModel: BaseViewModel by activityViewModels()
//    private lateinit var viewModel: RecordListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRecordDetailBinding.inflate(inflater, container, false)

        setRecordImageAdapter()

        return binding.root
    }

    private fun setRecordImageAdapter() {
        with (binding.rvRecordImageList) {
            adapter = RecordDetailSRVAdapter(listOf())

            PagerSnapHelper().attachToRecyclerView(this)

            // item 간 간격 설정
            addItemDecoration(
                ItemOffsetDecoration(
                    ItemOffsetDecoration.Orientation.HORIZONTAL,
                    25,
                    false
                )
            )

            // 처음 실행 시 position 0 으로 이동
            // 이동 하는 과정 UI에 표시 되지 않도록 visible 설정
            post {
                scrollToPosition(0)
                visibility = VISIBLE
            }
        }
    }
}