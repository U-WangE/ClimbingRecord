package com.ihavesookchi.climbingrecord

import android.os.Bundle
import com.ihavesookchi.climbingrecord.databinding.ActivityRecordListBinding

class RecordListActivity : BaseActivity() {
    private var _binding: ActivityRecordListBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRecordListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}