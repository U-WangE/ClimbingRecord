package com.ihavesookchi.climbingrecord

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ihavesookchi.climbingrecord.databinding.ActivityBaseBinding

open class BaseActivity : AppCompatActivity() {
    private var _binding: ActivityBaseBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bnBottomNavigationBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_map -> {
                    startActivity(Intent(this, MapActivity::class.java))
                }
                R.id.menu_record_list -> {
                    startActivity(Intent(this, MapActivity::class.java))
                }
                R.id.menu_add_record -> {
                    replaceFragment(AddRecordFragment())
                }
                R.id.menu_goals -> {
                    replaceFragment(GoalsFragment())
                }
                R.id.menu_profile -> {
                    replaceFragment(ProfileFragment())
                }
                else -> {

                }
            }
            return@setOnItemSelectedListener true
        }
    }

    fun replaceFragment(fragment: Fragment, bundle: Bundle? = null) {
        fragment.arguments = bundle

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction
            .replace(R.id.fl_frame_layout, fragment)
            .commit()
    }
}