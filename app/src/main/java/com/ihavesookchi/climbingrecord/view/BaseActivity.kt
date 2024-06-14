package com.ihavesookchi.climbingrecord.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.data.uistate.UserDataUiState
import com.ihavesookchi.climbingrecord.databinding.ActivityBaseBinding
import com.ihavesookchi.climbingrecord.util.CommonUtil.toast
import com.ihavesookchi.climbingrecord.view.goals.GoalsFragment
import com.ihavesookchi.climbingrecord.view.records.AddRecordFragment
import com.ihavesookchi.climbingrecord.view.records.RecordListFragment
import com.ihavesookchi.climbingrecord.viewModel.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
    private var _binding: ActivityBaseBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: BaseViewModel by viewModels()

    private val CLASS_NAME = this::class.java.simpleName

    private val menuFragmentMap: Map<Int, Fragment> = mapOf(
        R.id.menu_map to MapFragment(),
        R.id.menu_record_list to RecordListFragment(),
        R.id.menu_add_record to AddRecordFragment(),
        R.id.menu_goals to GoalsFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedViewModel.getUserDataFromFirebaseDB()
        observingGoalsDataUiState()

        replaceFragment(MapFragment())

        setNavigationBarSelectedListener()
    }

    private fun observingGoalsDataUiState() {
        sharedViewModel.userDataUiState.observe(this) {
            when (it) {
                is UserDataUiState.UserDataSuccess -> {

                }
                is UserDataUiState.UserDataFailure -> {
                    toast(this, "UserDataUiState.UserDataFailure")

                    startActivity(
                        Intent(this, LoginActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK or
                                    Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                    )
                }
                is UserDataUiState.AttemptLimitExceeded -> {
                    // 잠시 후 다시 시도 Message
                }
                else -> {}
            }
        }
    }

    private fun setNavigationBarSelectedListener() {
        binding.bnBottomNavigationBar.setOnItemSelectedListener { item ->
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fl_frame_layout)

            menuFragmentMap[item.itemId]?.let { fragment ->
                if (fragment != currentFragment) {
                    ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "BottomNavigationBar Item Selected : ${fragment}")

                    replaceFragment(fragment)
                }
                true
            }?: false
        }
    }

    fun replaceFragment(fragment: Fragment, bundle: Bundle? = null) {
        fragment.arguments = bundle

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction
            .replace(R.id.fl_frame_layout, fragment)
            .commit()
    }

    fun addFragment(fragment: Fragment, bundle: Bundle? = null) {
        fragment.arguments = bundle

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction
            .add(R.id.fl_frame_layout, fragment)
            .addToBackStack(fragment::class.simpleName)
            .commit()
    }

    fun removeFragment(fragment: Fragment, bundle: Bundle? = null) {
        fragment.arguments = bundle

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction
            .remove(fragment)
            .commit()
    }
}