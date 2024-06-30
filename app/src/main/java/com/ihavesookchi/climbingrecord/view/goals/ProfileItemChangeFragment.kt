package com.ihavesookchi.climbingrecord.view.goals

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.provider.Settings
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.data.uistate.UserDataUiState
import com.ihavesookchi.climbingrecord.databinding.FragmentProfileItemChangeBinding
import com.ihavesookchi.climbingrecord.util.CommonUtil.hideSoftKeyboard
import com.ihavesookchi.climbingrecord.util.CommonUtil.setSVGColorFilter
import com.ihavesookchi.climbingrecord.util.CommonUtil.toast
import com.ihavesookchi.climbingrecord.util.CommonUtil.twoButtonPopupWindow
import com.ihavesookchi.climbingrecord.util.ImageLoadTask
import com.ihavesookchi.climbingrecord.view.BaseActivity
import com.ihavesookchi.climbingrecord.viewModel.BaseViewModel
import com.ihavesookchi.climbingrecord.viewModel.ProfileItemChangeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class ProfileItemChangeFragment : Fragment() {
    private var _binding: FragmentProfileItemChangeBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val READ_STORAGE_REQUEST_CODE = 1111
    }

    private val CLASS_NAME = this::class.java.simpleName

    private val sharedViewModel: BaseViewModel by activityViewModels()
    private lateinit var viewModel: ProfileItemChangeViewModel

    private val readStoragePermission = if (SDK_INT > VERSION_CODES.S_V2) READ_MEDIA_IMAGES else READ_EXTERNAL_STORAGE

    private var resizeBitmapImage: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentProfileItemChangeBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[ProfileItemChangeViewModel::class.java]

        // Image, EditText Setting
        setProfileImageSetting()
        setProfileNameDataIsWritten()

        // EditText Filter
        setEditInstagramUserNameFilter()
        setEditNickNameFilter()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // OnClickListener Setting
        setBackButtonOnClickListener()
        setProfileImageOnClickListener()
        setEditButtonOnClickListener()

        // Observer Setting
        observingUserDataUiState()
        observingSharedUserDataUiState()
    }


    private fun setProfileImageSetting() {
        if (sharedViewModel.getProfileImage().isEmpty()) {
            binding.ivProfileImage.setImageResource(R.drawable.ic_bot)
            setSVGColorFilter(binding.ivProfileImage, R.color.svgFilterColorMediumGrayDarkGray, requireContext())
        } else {
            binding.ivProfileImage.clearColorFilter()
            ImageLoadTask(binding.ivProfileImage).loadImage(sharedViewModel.getProfileImage())
        }
    }

    // 입력란 중 빈값이 있는 경우, 기존 유저 데이터로 자동 세팅
    private fun setProfileNameDataIsWritten() {
        val instagramUserName = binding.etInstagramUserNameContent.text
        val nickname = binding.etNicknameContent.text

        if (instagramUserName.isEmpty()) binding.etInstagramUserNameContent.setText(sharedViewModel.getInstagramUserName())
        if (nickname.isEmpty()) binding.etNicknameContent.setText(sharedViewModel.getNickName())
    }


    private fun setBackButtonOnClickListener() {
        setSVGColorFilter(binding.btBackButton, R.color.svgFilterColorMediumGrayDarkGray, requireContext())

        binding.btBackButton.setOnClickListener {
            (activity as BaseActivity).replaceFragment(GoalsFragment())
            (activity as BaseActivity).removeFragment(this)
        }
    }

    private fun setProfileImageOnClickListener() {
        val editProfileClickListener = OnClickListener { readStoragePermission() }

        binding.ivProfileImage.setOnClickListener(editProfileClickListener)
        binding.tvEditProfilePicture.setOnClickListener(editProfileClickListener)

        setSVGColorFilter(binding.ivRemoveProfile, R.color.rosewood, requireContext())

        binding.ivRemoveProfile.setOnClickListener {
            twoButtonPopupWindow(
                context = requireContext(),
                view = binding.root,
                title = null,
                contents = listOf(getString(R.string.toast_profile_image_reset_contents)),
                leftButtonText = getString(R.string.no),
                rightButtonText = getString(R.string.yes)
            ) { clickEvent ->
                when (clickEvent) {
                    "Left" -> {}
                    "Right" -> {
                        binding.ivProfileImage.setImageResource(R.drawable.ic_bot)
                        setSVGColorFilter(binding.ivProfileImage, R.color.svgFilterColorMediumGrayDarkGray, requireContext())

                        //TODO::
                        // 프로필 이미지 삭제 버튼 클릭시 Storage에 저장되는 형상은 적합하지 않은 것으로 판단
                        // 수정 버튼을 누른 경우에만 이미지 삭제가 Storage에 저장되야 한다 해당 부분 수정 해야함
                        viewModel.deleteBitmapToFirebaseStorage()
                    }
                }
            }
        }
    }

    /*
    * focus & keyboard 제거
    * 빈값 -> 기존 데이터로 자동 세팅
    * 수정 값 Firebase에 Update
    */
    private fun setEditButtonOnClickListener() {
        // profile 수정 처리
        binding.btEditButton.setOnClickListener {
            requireActivity().hideSoftKeyboard()

            setProfileNameDataIsWritten()

            if (checkProfileDataIsChanged())
                viewModel.updateUserData(binding.etInstagramUserNameContent.text.toString(), binding.etNicknameContent.text.toString())
        }
    }

    // Profile 데이터 중 하나라도 수정 되었으면 true
    private fun checkProfileDataIsChanged(): Boolean {
        val instagramUserName = binding.etInstagramUserNameContent.text
        val nickname = binding.etNicknameContent.text

        return viewModel.getSelectedImage() != null ||
                nickname.toString() != sharedViewModel.getNickName() ||
                instagramUserName.toString() != sharedViewModel.getInstagramUserName()
    }

    private fun observingUserDataUiState() {
        viewModel.userDataUiState.observe(viewLifecycleOwner) {
            when (it) {
                is UserDataUiState.UserDataUpdateSuccess -> {
                    toast(requireContext(), getString(R.string.toast_completed_revision))

                    sharedViewModel.getUserDataFromFirebaseDB()
                }
                is UserDataUiState.UserDataFailure -> {
                    toast(requireContext(), getString(R.string.toast_please_try_again_later))
                }
                is UserDataUiState.AttemptLimitExceeded -> {
                    toast(requireContext(), getString(R.string.toast_check_your_network_connection))
                }
                else -> {}
            }
        }
    }

    private fun observingSharedUserDataUiState() {
        sharedViewModel.userDataUiState.observe(viewLifecycleOwner) {
            when (it) {
                is UserDataUiState.UserDataSuccess -> {
                    setProfileImageSetting()
                    setProfileNameDataIsWritten()
                }
                else -> {}
            }
        }
    }

    /*
    * Emoji X
    * a-z, A-Z, ㄱ-힣, . , _  만 허용
    * Double-Space Full Stop X
    * -> Space 연속 2번 입력시 ". " 자동 입력 및 변환 되는 현상 해결
    * -> inputType="textVisiblePassword"
    */
    private fun setEditInstagramUserNameFilter() {
        binding.etInstagramUserNameContent.filters =
            arrayOf(InputFilter { charSequence, _, _, _, _, _ ->
                val ps = Pattern.compile("^[^\\wㄱ-ㅎㅏ-ㅣ가-힣\\.]+\$")

                return@InputFilter if (
                    charSequence.any { char ->
                        Character.getType(char) == Character.SURROGATE.toInt() ||
                                Character.getType(char) == Character.OTHER_SYMBOL.toInt() ||
                                ps.matcher(char.toString()).matches() })
                    ""
                else
                    null
            })
    }

    private fun setEditNickNameFilter() {
        binding.etNicknameContent.filters =
            arrayOf(InputFilter { charSequence, _, _, _, _, _ ->
                return@InputFilter if (
                    charSequence.any { char ->
                        Character.getType(char) == Character.SURROGATE.toInt() ||
                                Character.getType(char) == Character.OTHER_SYMBOL.toInt()})
                    ""
                else
                    null
            })
    }

    /**
     * Storage Permission
     * Gallery Selector Setting
     **/
    private val storageIntentResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "storageIntentResultLauncher Selected Image URI ${result.data?.data}")

        result.data?.data?.let { uri ->
            binding.ivProfileImage.clearColorFilter()

            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)

            // Exif 정보를 고려하여 이미지 회전
            val rotatedBitmap = rotateImageIfRequired(bitmap, uri)

            // 회전된 이미지를 원형 캔버스에 적용
            resizeBitmapImage = getCircularBitmap(rotatedBitmap)
            binding.ivProfileImage.setImageBitmap(resizeBitmapImage)
        }
    }

    private fun rotateImageIfRequired(bitmap: Bitmap, uri: Uri): Bitmap {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val ei = ExifInterface(inputStream!!)

        val orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(bitmap, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(bitmap, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(bitmap, 270)
            else -> bitmap
        }
    }

    private fun rotateImage(source: Bitmap, angle: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle.toFloat())
        return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
    }

    // 원형 캠버스에 정사각형 비트맵 이미지 제작
    private fun getCircularBitmap(bitmap: Bitmap): Bitmap {
        val targetSize = 90f

        // 비트맵의 가로세로 비율을 기반으로 너비와 높이 계산
        val width = if (bitmap.width > bitmap.height) {
            (targetSize * (bitmap.width.toFloat() / bitmap.height)).dpToPx()
        } else {
            targetSize.dpToPx()
        }
        val height = if (bitmap.width > bitmap.height) {
            targetSize.dpToPx()
        } else {
            (targetSize / (bitmap.width.toFloat() / bitmap.height)).dpToPx()
        }

        return viewModel.changeCircularBitmap(bitmap, width, height)
    }

    private fun Float.dpToPx(): Int {
        val scale = resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

    private fun intentStorage() {
        storageIntentResultLauncher.launch(
            Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
        )
    }

    private val storagePermissionResultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "storagePermissionResultLauncher Storage Permission Granted")

            intentStorage()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    readStoragePermission
                )
            ) {
                ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "storagePermissionResultLauncher Storage Permission Denied 1 Time")

                // 처음 권한 거부시 해당 PopupWindow 띄움
                twoButtonPopupWindow(
                    context = requireContext(),
                    view = binding.root,
                    title = getString(R.string.title_permission_notice),
                    contents = listOf(
                        getString(R.string.contents_permission_notice),
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
                ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "storagePermissionResultLauncher Storage Permission Denied 2 Time Or More")

                // 두 번 이상 권한 거부시 해당 PopupWindow 띄움
                twoButtonPopupWindow(
                    context = requireContext(),
                    view = binding.root,
                    title = getString(R.string.title_permission_notice),
                    contents = listOf(
                        getString(R.string.contents_permission_notice),
                        getString(R.string.permission_notice_read_storage)
                    ),
                    comments = getString(R.string.permission_notice_comment),
                    leftButtonText = getString(R.string.close),
                    rightButtonText = getString(R.string.setting)
                ) { clickEvent ->
                    when (clickEvent) {
                        "Left" -> {}
                        "Right" -> intentSetting()
                    }
                }
            }
        }
    }

    private fun readStoragePermission() {
        storagePermissionResultLauncher.launch(readStoragePermission)
    }

    private val settingResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        readStoragePermission()
    }

    private fun intentSetting() {
        settingResultLauncher.launch(
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