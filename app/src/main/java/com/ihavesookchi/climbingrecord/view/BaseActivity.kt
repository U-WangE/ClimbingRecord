package com.ihavesookchi.climbingrecord.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.databinding.ActivityBaseBinding
import com.ihavesookchi.climbingrecord.viewModel.BaseViewModel

open class BaseActivity : AppCompatActivity() {
    private var _binding: ActivityBaseBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: BaseViewModel by viewModels()

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