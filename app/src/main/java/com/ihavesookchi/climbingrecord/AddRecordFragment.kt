package com.ihavesookchi.climbingrecord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ihavesookchi.climbingrecord.databinding.FragmentAddRecordBinding

class AddRecordFragment : Fragment() {
    private var _binding: FragmentAddRecordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAddRecordBinding.inflate(inflater, container, false)

        return binding.root
    }
}