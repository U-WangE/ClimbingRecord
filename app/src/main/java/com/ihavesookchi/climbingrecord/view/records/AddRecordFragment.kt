package com.ihavesookchi.climbingrecord.view.records

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ihavesookchi.climbingrecord.adapter.AddRecordGalleryImageListAdapter
import com.ihavesookchi.climbingrecord.databinding.FragmentAddRecordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddRecordFragment : Fragment() {
    private var _binding: FragmentAddRecordBinding? = null
    private val binding get() = _binding!!

    private val imageList = mutableListOf<String>()
    private lateinit var imageListAdapter: AddRecordGalleryImageListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAddRecordBinding.inflate(inflater, container, false)

        imageListAdapter = AddRecordGalleryImageListAdapter(imageList)
        binding.rvGalleryImageList.adapter = imageListAdapter

        getGalleryImages()

        return binding.root
    }

    private fun getGalleryImages() {
        if (checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED)
            loadImages()
        else
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)


    }
    private fun loadImages() {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val selection = "${MediaStore.Images.Media.MIME_TYPE}=? OR ${MediaStore.Images.Media.MIME_TYPE}=? OR ${MediaStore.Images.Media.MIME_TYPE}=?"
        val selectionArgs = arrayOf("image/jpeg", "image/png", "image/gif")
        val cursor = requireContext().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )

        cursor?.use {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (cursor.moveToNext()) {
                val imagePath = cursor.getString(columnIndex)
                imageList.add(imagePath)
            }
        }

        if (imageList.isNotEmpty())
            Glide.with(requireContext())
                .load(imageList[0])
                .into(binding.ivPreviewSelectImage)

        imageListAdapter.notifyDataSetChanged()
    }

//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            loadImages()
//        }
//    }
}