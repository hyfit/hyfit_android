package com.example.hyfit_android.exercise

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
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

class ExerciseResultActivity : AppCompatActivity(), OnMapReadyCallback{
    private lateinit var binding: ActivityExerciseResultBinding

    // google map
    private lateinit var context: Context
    private lateinit var mMap: GoogleMap
    private lateinit var polyline: Polyline
    private var latLngList: MutableList<LatLng> = mutableListOf()
    private lateinit var firstList: List<String>
    private lateinit var middleList: List<String>
    private lateinit var lastList: List<String>
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
        val distanceResult = (Math.round(distance * 100000.0) / 100000.0)
        firstList = intent.getStringExtra("firstList")?.split(",")!!
        middleList = intent.getStringExtra("middleList")?.split(",")!!
        lastList= intent.getStringExtra("lastList")?.split(",")!!
        // 경로 표시
        latLngList.add(LatLng(firstList[0].toDouble(),firstList[1].toDouble()))
        latLngList.add(LatLng(middleList[0].toDouble(),middleList[1].toDouble()))
        latLngList.add(LatLng(lastList[0].toDouble(),lastList[1].toDouble()))

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
        mMap.addPolyline(
            PolylineOptions()
                .addAll(latLngList)
                .width(10f)
                .color(Color.BLUE)
                .geodesic(true)
        )

        val firstLocation = LatLng(firstList[0].toDouble(),firstList[1].toDouble())
        val middleLocation = LatLng(middleList[0].toDouble(),middleList[1].toDouble())
        val endLocation = LatLng(lastList[0].toDouble(),lastList[1].toDouble())

//        mMap.addMarker(MarkerOptions().position(firstLocation).title("start"))
//        mMap.addMarker(MarkerOptions().position(endLocation).title("end"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(middleLocation,16f))
        mMap.setOnMapLoadedCallback {
            val bounds = LatLngBounds.builder()
                .include(firstLocation)
                .include(middleLocation)
                .include(endLocation)
                .build()
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        }

    }

}

