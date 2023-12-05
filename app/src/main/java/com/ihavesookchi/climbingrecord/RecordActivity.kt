package com.ihavesookchi.climbingrecord

import android.os.Bundle
import com.ihavesookchi.climbingrecord.databinding.ActivityRecordBinding

class RecordActivity : BaseActivity() {
    private var _binding: ActivityRecordBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}