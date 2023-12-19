package com.ihavesookchi.climbingrecord.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.databinding.ActivityBaseBinding

open class BaseActivity : AppCompatActivity() {
    private var _binding: ActivityBaseBinding? = null
    private val binding get() = _binding!!

    private val CLASS_NAME = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(MapFragment())

        binding.bnBottomNavigationBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_map -> {
                    ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME,
                        "BottomNavigationBar Item Selected : menu_map")
                    replaceFragment(MapFragment())
                }
                R.id.menu_record_list -> {
                    ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME,
                        "BottomNavigationBar Item Selected : menu_record_list")
                    replaceFragment(RecordListFragment())
                }
                R.id.menu_add_record -> {
                    ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME,
                        "BottomNavigationBar Item Selected : menu_add_record")
                    replaceFragment(AddRecordFragment())
                }
                R.id.menu_goals -> {
                    ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME,
                        "BottomNavigationBar Item Selected : menu_goals")
                    replaceFragment(GoalsFragment())
                }
                R.id.menu_profile -> {
                    ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME,
                        "BottomNavigationBar Item Selected : menu_profile")
                    replaceFragment(ProfileFragment())
                }
                else -> {
                    ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME,
                        "BottomNavigationBar.setOnItemSelectedListener -> else  {  item: ${item},  itemId: ${item.itemId}, itemTitle: ${item.title}, itemOrder: ${item.order}  }")
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