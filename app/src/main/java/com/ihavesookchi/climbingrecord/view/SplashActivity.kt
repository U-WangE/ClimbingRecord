package com.ihavesookchi.climbingrecord.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.ihavesookchi.climbingrecord.util.CommonUtil.twoButtonPopupWindow
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private var _binding: ActivitySplashBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val LOCATION_REQUEST_CODE = 1111
    }

    private var settingResultLauncher: ActivityResultLauncher<Intent>? = null
    private var gpsLocationResultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingResult()
        gpsLocationResult()

        locationPermission()
    }

    private fun permissionChecker(requestCode: Int, vararg permissions: String): Boolean {
        val isPermissionGranted = permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }

        if (!isPermissionGranted)
            ActivityCompat.requestPermissions(this, permissions, requestCode)

        return isPermissionGranted
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
            when (requestCode) {
                LOCATION_REQUEST_CODE -> {
                    checkLoggedIn()
                }
            }
        } else {
            permissions.all {
                ActivityCompat.shouldShowRequestPermissionRationale(this@SplashActivity, it)
            }.let {
                if(it) {
                    // 처음 권한 거부시 해당 PopupWindow 띄움
                    twoButtonPopupWindow(
                        context = this@SplashActivity,
                        view = binding.root,
                        title = getString(R.string.permission_notice_title),
                        contents = getString(R.string.permission_notice_contents),
                        leftButtonText = getString(R.string.close),
                        rightButtonText = getString(R.string.permission_re_request)
                    ) { clickEvent ->
                        when (clickEvent) {
                            "Left" -> {
                                finishAffinity()
                                exitProcess(0)
                            }
                            "Right" -> {
                                when (requestCode) {
                                    LOCATION_REQUEST_CODE -> {
                                        locationPermission()
                                    }
                                    else -> {
                                        locationPermission()
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // 두 번 이상 권한 거부시 해당 PopupWindow 띄움
                    twoButtonPopupWindow(
                        context = this@SplashActivity,
                        view = binding.root,
                        title = getString(R.string.permission_notice_title),
                        contents = getString(R.string.permission_notice_contents),
                        comments = getString(R.string.permission_notice_comment),
                        leftButtonText = getString(R.string.close),
                        rightButtonText = getString(R.string.setting)
                    ) { clickEvent ->
                        when (clickEvent) {
                            "Left" -> {
                                finishAffinity()
                                exitProcess(0)
                            }
                            "Right" -> {
                                settingResultLauncher?.launch(
                                    Intent().apply {
                                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                        data = Uri.fromParts("package", packageName, null)
                                        putExtra("requestCode", requestCode)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun gpsLocationResult() {
        gpsLocationResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    checkLoggedIn()
                } else {
                    locationPermission()
                }
            }
    }

    @SuppressLint("NewApi")
    fun locationPermission() {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        when (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            true -> {
                val isLocationPermissionGranted = permissionChecker(
                    LOCATION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                if (isLocationPermissionGranted) {
                    Log.d("권한", "locationPermission() -> Granted Location Permission")
                    checkLoggedIn()
                }
            }
            false -> {
                Log.d("권한", "locationPermission() -> Disabled GPS Service")
            }
        }
    }

    private fun settingResult() {
        settingResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    when (result.data?.extras?.getInt("requestCode")) {
                        LOCATION_REQUEST_CODE -> {
                            checkLoggedIn()
                        }
                        else -> {
                            locationPermission()
                        }
                    }
                } else {
                    when (result.data?.extras?.getInt("requestCode")) {
                        LOCATION_REQUEST_CODE -> {
                            checkLoggedIn()
                        }
                        else -> {
                            // setting에서 권한 수락 안 한 경우
                            locationPermission()
                        }
                    }
                }
            }
    }

    private fun checkLoggedIn() {
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null)
            startActivity(Intent(this, BaseActivity::class.java))
        else
            startActivity(Intent(this, LoginActivity::class.java))
    }
}