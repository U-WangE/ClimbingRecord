package com.ihavesookchi.climbingrecord

import android.os.Bundle
import com.ihavesookchi.climbingrecord.databinding.ActivityMapBinding
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import java.lang.Exception

class MapActivity : BaseActivity() {
    private var _binding: ActivityMapBinding? = null
    private val binding get() = _binding!!

    private val CLASS_NAME = this::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mvMap.start(object: MapLifeCycleCallback() {
            override fun onMapDestroy() {
                // 지도 API 가 정상적으로 종료될 때 호출됨
            }

            override fun onMapError(error: Exception?) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                ClimbingRecordLogger.getInstance()?.saveLog(CLASS_NAME,
                    "onMapError   code : ${error.hashCode()}   cause : ${error?.cause}   message : ${error?.message}")
            }

        }, object: KakaoMapReadyCallback() {
            override fun onMapReady(kakaoMap: KakaoMap) {
                // 인증 후 API 가 정상적으로 실행될 때 호출됨
            }
        })
    }
}