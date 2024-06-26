package com.ihavesookchi.climbingrecord.view

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.adapter.SearchListAdapter
import com.ihavesookchi.climbingrecord.data.KakaoApi
import com.ihavesookchi.climbingrecord.data.response.SearchKeywordResponse
import com.ihavesookchi.climbingrecord.data.uistate.SearchUiState
import com.ihavesookchi.climbingrecord.databinding.FragmentMapBinding
import com.ihavesookchi.climbingrecord.viewModel.BaseViewModel
import com.ihavesookchi.climbingrecord.viewModel.MapViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val CLASS_NAME = this::class.java.simpleName

    private val sharedViewModel: BaseViewModel by activityViewModels()

    private lateinit var viewModel: MapViewModel

    @Inject
    lateinit var kakaoApi: KakaoApi

    // map
    private lateinit var googleMap: GoogleMap
    private var marker: Marker? = null

    // location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var location: Location? = null


    override fun onStart() {
        super.onStart()
        locationUpdate()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[MapViewModel::class.java]

        searchViewAction()

        observingSearchKeywordUiState()

        val mapFragment = childFragmentManager.findFragmentById(R.id.fg_map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        this.googleMap.setMinZoomPreference(7.0f)
        this.googleMap.setMaxZoomPreference(19.0f)


        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.5077,127.0549), 13.0f))

        // 캐시에 위치 로직이 남아 있으면 0.1 ~ 0.5 사이로 지도 딜레이와 맞게 현재 위치로 이동하게 설정
        Handler(Looper.getMainLooper()).postDelayed({
            if (location != null)
                this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location?.latitude!!,location?.longitude!!), 13.0f))
        }, 400)
    }


    // 현재 위치 확인하는 로직
    private fun locationUpdate() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        locationCallback = object: LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                location = locationResult.lastLocation
            }
        }

        requestLocationUpdate()

        stopLocationUpdateAfterDelay(20000)
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdate() {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10000L
        )
            .setWaitForAccurateLocation(true)
            .setMinUpdateIntervalMillis(5000)  // 최소 업데이트 시간 5초
            .setMaxUpdateDelayMillis(15000)  // 최대 업데이트 지연 시간 15초
            .build()

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun stopLocationUpdateAfterDelay(delayMillis: Long) {
        Handler(Looper.getMainLooper()).postDelayed({
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }, delayMillis)
    }

    // Keyboard Search 클릭 시 이벤트 처리
    private fun searchViewAction() {
        binding.sbSearchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "Get Search Event    Search Keyword  :  ${binding.sbSearchBar.query}")

                binding.rvSearchList.adapter?.notifyItemRangeRemoved(0, viewModel.getSearchData().size)

                viewModel.searchKeywordApi(binding.sbSearchBar.query.toString())

                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                return true
            }
        })


        binding.sbSearchBar.findViewById<View>(androidx.appcompat.R.id.search_close_btn).setOnClickListener {
            ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "Listen Search close button event")

            binding.sbSearchBar.setQuery("", false)

            viewModel.removeSearchData()
        }
        
        binding.sbSearchBar.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME, "Focus removed from Search Bar")

                binding.rvSearchList.visibility = GONE
                binding.clSearchLayout.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
                binding.clSearchLayout.invalidate()
            }
        }
    }

    private fun observingSearchKeywordUiState() {
        viewModel.searchUiState.observe(viewLifecycleOwner) {
            when (it) {
                is SearchUiState.SearchSuccess -> {
                    setSearchListAdapter(binding.sbSearchBar.query.toString(), viewModel.getSearchData())
                }

                is SearchUiState.SearchFailure -> {
                    //TODO:: 어떻게 처리할지 정하지 않음
                }
                else -> {}
            }
        }
    }

    private fun setSearchListAdapter(keyword: String, climbingCenters: List<SearchKeywordResponse.Document>) {
        binding.rvSearchList.visibility = VISIBLE
        binding.clSearchLayout.backgroundTintList = ColorStateList.valueOf(getColor(requireContext(), R.color.dark_gray))
        binding.clSearchLayout.invalidate()

        binding.rvSearchList.adapter = SearchListAdapter(keyword, climbingCenters) { center ->
            binding.sbSearchBar.setQuery(center.placeName, false)

            binding.sbSearchBar.clearFocus()

            setMarker(center)

            //TODO::record 를 암장에 따라 최신 순으로 가져오는 Firebase Function 추가해야함
            // 추후 Map 에서 암장 기록 추가 시 사용할 함수
            //TODO:: viewModel.setSelectedClimbingCenter(center)
            viewModel.getClimbingCenterRecord(center)

        }
    }

    private fun setMarker(center: SearchKeywordResponse.Document) {
        val latLng = LatLng(center.latitude.toDouble(),center.longitude.toDouble())
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f))

        marker?.remove()
        marker = googleMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title(center.placeName)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
        )
        marker?.showInfoWindow()
    }

    private fun observingClimbingCenterRecord() {

    }
}