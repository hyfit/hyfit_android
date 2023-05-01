package com.example.hyfit_android.exercise

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.ActivityExerciseResultBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.nextnav.nn_app_sdk.PhoneMetaData
import java.util.ArrayList
import kotlin.properties.Delegates

class ExerciseResultActivity : AppCompatActivity(), OnMapReadyCallback{
    private lateinit var binding: ActivityExerciseResultBinding

    // google map
    private lateinit var context: Context
    private lateinit var mMap: GoogleMap
    private lateinit var polyline: Polyline
    private var latLngList: MutableList<LatLng> = mutableListOf()
    private lateinit var locationList : ArrayList<String>
    private var  totalTime by Delegates.notNull<Int>()
    private lateinit var firstList: LatLng
    private lateinit var lastList: LatLng
    private lateinit var mapFragment : SupportMapFragment



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = applicationContext
        PhoneMetaData.mContext = this
        binding = ActivityExerciseResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.exercise_result_map) as SupportMapFragment
        mapFragment.onCreate(savedInstanceState)
        mapFragment.getMapAsync(this)
        val distance:Double= (intent.getDoubleExtra("distance", 0.0))
        val distanceResult = String.format("%.2f", (Math.round(distance * 100000.0) / 100000.0))

        // 경로 지정
        val locationList = intent.getStringArrayListExtra("locationList")
        if (locationList != null) {
            for (location in locationList) {
                val latLngArr = location.split(",")
                val lat = latLngArr[0].toDouble()
                val lng = latLngArr[1].toDouble()
                val latLng = LatLng(lat, lng)
                latLngList.add(latLng)
            }
        }

        // 첫번째와 마지막위치 가져옴
        if (locationList != null) {
            val firstLocation = locationList.first()
            val lastLocation = locationList.last()
            val firstLatLngArr = firstLocation.split(",")
            val lastLatLngArr = lastLocation.split(",")
            firstList = LatLng(firstLatLngArr[0].toDouble(), firstLatLngArr[1].toDouble())
            lastList = LatLng(lastLatLngArr[0].toDouble(), lastLatLngArr[1].toDouble())
        }


        // 시간 가져오기
        totalTime = intent.getIntExtra("totalTime",0)

        binding.exerciseDistanceText.text="distance : " + distanceResult.toString() + "km"

        // 메인 이동
        binding.goToMain.setOnClickListener{
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        // 마커
        mMap.addMarker(MarkerOptions().position(firstList).title("start").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
        mMap.addMarker(MarkerOptions().position(lastList).title("end").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))

//        mMap.addMarker(MarkerOptions().position(firstList).title("start"))
//        mMap.addMarker(MarkerOptions().position(lastList).title("end"))

        // 경로
        mMap.addPolyline(
            PolylineOptions()
                .addAll(latLngList)
                .width(15f)
                .color(Color.BLUE)
                .geodesic(true)
        )

        mMap.setOnMapLoadedCallback {
            val boundsBuilder = LatLngBounds.builder()
            for (latLng in latLngList) {
                boundsBuilder.include(latLng)
            }
            val bounds = boundsBuilder.build()
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200))
        }


    }


}

