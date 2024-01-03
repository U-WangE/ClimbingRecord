package com.ihavesookchi.climbingrecord.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color.RED
import android.graphics.ColorFilter
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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
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
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ihavesookchi.climbingrecord.ClimbingRecordLogger
import com.ihavesookchi.climbingrecord.R
import com.ihavesookchi.climbingrecord.adapter.SearchListAdapter
import com.ihavesookchi.climbingrecord.data.KakaoApi
import com.ihavesookchi.climbingrecord.data.response.SearchKeywordResponse
import com.ihavesookchi.climbingrecord.data.uistate.SearchKeywordUiState
import com.ihavesookchi.climbingrecord.databinding.FragmentMapBinding
import com.ihavesookchi.climbingrecord.viewModel.BaseViewModel
import com.ihavesookchi.climbingrecord.viewModel.MapViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: BaseViewModel by activityViewModels()
    private val viewModel: MapViewModel by viewModels()

    @Inject
    lateinit var kakaoApi: KakaoApi

    private val CLASS_NAME = this::class.java.simpleName

    // map
    private lateinit var googleMap: GoogleMap

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
            .setMinUpdateIntervalMillis(5000)  // 최소 업데이트 시간 10초
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

                viewModel.searchKeywordApi(binding.sbSearchBar.query.toString())
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                binding.rvSearchList.visibility = VISIBLE

                //TODO::전체 데이터 저장 후 비교 필요
                setSearchListAdapter(query?:"", sharedViewModel.getClimbingCenters())

                return true
            }
        })
    }

    private fun observingSearchKeywordUiState() {
        viewModel.searchKeywordUiState.observe(viewLifecycleOwner) {
            when (it) {
                is SearchKeywordUiState.SearchKeywordSuccess -> {
                    setSearchListAdapter(binding.sbSearchBar.query.toString(), sharedViewModel.getClimbingCenters())
                }

                is SearchKeywordUiState.SearchKeywordFailure -> {

                }
            }
        }
    }

    private fun setSearchListAdapter(keyword: String, climbingCenters: List<SearchKeywordResponse.Document>) {
        binding.rvSearchList.adapter = SearchListAdapter(keyword, climbingCenters) { center ->
            binding.sbSearchBar.setQuery(center.placeName, false)
            binding.rvSearchList.visibility = GONE

            val latLng = LatLng(center.latitude.toDouble(),center.longitude.toDouble())
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f))

            googleMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(center.placeName)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
            )
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        }
    }
}