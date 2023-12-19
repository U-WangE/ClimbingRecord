package com.ihavesookchi.climbingrecord.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.GoogleMap
import com.ihavesookchi.climbingrecord.databinding.FragmentRecordListBinding

class RecordListFragment : Fragment() {
    private var _binding: FragmentRecordListBinding? = null
    private val binding get() = _binding!!

    private val CLASS_NAME = this::class.java.simpleName

    private lateinit var googleMap: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentRecordListBinding.inflate(inflater, container, false)


        return binding.root
    }
}