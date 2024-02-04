package com.ihavesookchi.climbingrecord.view.goals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ihavesookchi.climbingrecord.databinding.FragmentProfileItemChangeBinding
import com.ihavesookchi.climbingrecord.viewModel.BaseViewModel

class ProfileItemChangeFragment : Fragment() {
    private var _binding: FragmentProfileItemChangeBinding? = null
    private val binding get() = _binding!!

    private val CLASS_NAME = this::class.java.simpleName

    private val sharedViewModel: BaseViewModel by activityViewModels()
//    private val viewModel: ProfileItemChangeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentProfileItemChangeBinding.inflate(inflater, container, false)


        return binding.root
    }
}