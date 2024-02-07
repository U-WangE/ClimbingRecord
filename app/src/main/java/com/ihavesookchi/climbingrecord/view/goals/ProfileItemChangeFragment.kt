package com.ihavesookchi.climbingrecord.view.goals

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.databinding.FragmentProfileItemChangeBinding
import com.ihavesookchi.climbingrecord.util.CommonUtil.twoButtonPopupWindow
import com.ihavesookchi.climbingrecord.viewModel.BaseViewModel

class ProfileItemChangeFragment : Fragment() {
    private var _binding: FragmentProfileItemChangeBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val READ_STORAGE_REQUEST_CODE = 1111
    }

    private val CLASS_NAME = this::class.java.simpleName

    private val sharedViewModel: BaseViewModel by activityViewModels()
//    private val viewModel: ProfileItemChangeViewModel by viewModels()

    private val readStoragePermission = if (SDK_INT > VERSION_CODES.S_V2) READ_MEDIA_IMAGES else READ_EXTERNAL_STORAGE
    private var settingResultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentProfileItemChangeBinding.inflate(inflater, container, false)

        settingResult()
        setEditProfileOnClickListener()

        return binding.root
    }

    private fun setEditProfileOnClickListener() {
        val editProfileClickListener = OnClickListener { readStoragePermission() }
        binding.ivProfileImage.setOnClickListener(editProfileClickListener)
        binding.tvEditProfilePicture.setOnClickListener(editProfileClickListener)
    }


    private fun readStoragePermission() {
        requestPermission.launch(readStoragePermission)
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    readStoragePermission
                )
            ) {
                // 처음 권한 거부시 해당 PopupWindow 띄움
                twoButtonPopupWindow(
                    context = requireContext(),
                    view = binding.root,
                    title = getString(R.string.permission_notice_title),
                    contents = listOf(
                        getString(R.string.permission_notice_contents),
                        getString(R.string.permission_notice_location)
                    ),
                    leftButtonText = getString(R.string.close),
                    rightButtonText = getString(R.string.permission_re_request)
                ) { clickEvent ->
                    when (clickEvent) {
                        "Left" -> {}
                        "Right" -> readStoragePermission()
                    }
                }
            } else {
                // 두 번 이상 권한 거부시 해당 PopupWindow 띄움
                twoButtonPopupWindow(
                    context = requireContext(),
                    view = binding.root,
                    title = getString(R.string.permission_notice_title),
                    contents = listOf(
                        getString(R.string.permission_notice_contents),
                        getString(R.string.permission_notice_read_storage)
                    ),
                    comments = getString(R.string.permission_notice_comment),
                    leftButtonText = getString(R.string.close),
                    rightButtonText = getString(R.string.setting)
                ) { clickEvent ->
                    when (clickEvent) {
                        "Left" -> {}
                        "Right" -> {
                            settingResultLauncher?.launch(
                                Intent().apply {
                                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                                    data = Uri.fromParts(
                                        "package",
                                        requireActivity().packageName,
                                        null
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun settingResult() {
        settingResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                readStoragePermission()
            }
    }
}