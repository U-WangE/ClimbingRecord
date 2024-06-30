package com.ihavesookchi.climbingrecord.view.records

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.adapter.RecordListAdapter
import com.ihavesookchi.climbingrecord.data.uistate.RecordsDataUiState
import com.ihavesookchi.climbingrecord.databinding.FragmentRecordListBinding
import com.ihavesookchi.climbingrecord.util.CommonUtil.setSVGColorFilter
import com.ihavesookchi.climbingrecord.view.BaseActivity
import com.ihavesookchi.climbingrecord.viewModel.BaseViewModel
import com.ihavesookchi.climbingrecord.viewModel.RecordListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordListFragment : Fragment() {
    private var _binding: FragmentRecordListBinding? = null
    private val binding get() = _binding!!

    private val CLASS_NAME = this::class.java.simpleName

    private val sharedViewModel: BaseViewModel by activityViewModels()
    private lateinit var viewModel: RecordListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRecordListBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[RecordListViewModel::class.java]

        viewModel.getFirebaseRecordListData()
        setSVGColorFilter(binding.ivRecordAdd, R.color.svgFilterColorLightGrayishBlack, requireContext())

        observingRecordsDataUiState()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.clRecordAddLayout.setOnClickListener {
            (requireActivity() as BaseActivity).replaceFragment(AddRecordFragment())
            (requireActivity() as BaseActivity).removeFragment(this)
        }
    }

    private fun observingRecordsDataUiState() {
        viewModel.recordsDataUiState.observe(viewLifecycleOwner) {
            when(it) {
                RecordsDataUiState.RecordsDataSuccess -> {
                    binding.clRecordAddLayout.visibility = if (viewModel.getRecordList().isNotEmpty()) GONE else VISIBLE
                    binding.rvRecordListLayout.adapter = RecordListAdapter(viewModel.getRecordList())
                }
                else -> {}
            }
        }
    }
}