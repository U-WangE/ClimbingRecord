package com.ihavesookchi.climbingrecord

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ihavesookchi.climbingrecord.databinding.ActivityBaseBinding

open class BaseActivity : AppCompatActivity() {
    private var _binding: ActivityBaseBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}