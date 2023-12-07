package com.ihavesookchi.climbingrecord

import android.os.Bundle
import com.ihavesookchi.climbingrecord.databinding.ActivityMapBinding

class MapActivity : BaseActivity() {
    private var _binding: ActivityMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}