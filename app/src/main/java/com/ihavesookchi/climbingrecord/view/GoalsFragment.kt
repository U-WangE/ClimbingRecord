package com.ihavesookchi.climbingrecord.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ihavesookchi.climbingrecord.databinding.FragmentGoalsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GoalsFragment : Fragment() {
    private var _binding: FragmentGoalsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentGoalsBinding.inflate(inflater, container, false)

        return binding.root
    }
}