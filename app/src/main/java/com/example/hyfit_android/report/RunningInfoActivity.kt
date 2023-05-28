package com.example.hyfit_android.report

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.ActivityExerciseDataBinding
import com.example.hyfit_android.databinding.ActivityRunningInfoBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

class RunningInfoActivity : AppCompatActivity() , OnMapReadyCallback {
    private lateinit var binding : ActivityRunningInfoBinding

    // google map
    // private lateinit var context:
    private lateinit var mapView: MapView
    private lateinit var mMap: GoogleMap
    private lateinit var polyline: Polyline
    private var latLngList: MutableList<LatLng> = mutableListOf()
    private lateinit var locationList : ArrayList<String>
    //    private var  totalTime by Delegates.notNull<Long>()
    private lateinit var firstList: LatLng
    private lateinit var lastList: LatLng
    private lateinit var mapFragment : SupportMapFragment

    var exerciseId = 0L

    var distance = ""
    var totalTime = 0L
    var date = ""
    var type = ""
    var title = ""
    var pace = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunningInfoBinding.inflate(layoutInflater)

        // 경로 지정
        locationList = intent.getStringArrayListExtra("locationList") as ArrayList<String>
        date = intent.getStringExtra("date").toString()
        type = intent.getStringExtra("type").toString()
        pace = intent.getStringExtra("pace").toString()
        distance = intent.getStringExtra("distance").toString()
        totalTime = intent.getLongExtra("time",0L)
        val distanceResult = String.format("%.2f", (Math.round(distance.toDouble() * 100000.0) / 100000.0))
        binding.exerciseDistanceText1.text = distanceResult + "km"
        binding.totalTime1.text = formatTime(totalTime)
        binding.totalTime1.text = distanceResult + "km"
        binding.exercisePace1.text = pace
        title = "$date / $type"
        binding.exerciseInfoText.text = title

        // map
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.running_info_map) as SupportMapFragment
        mapFragment.onCreate(savedInstanceState)
        mapFragment.getMapAsync(this)

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
            val firstLocation = locationList.first()
            val lastLocation = locationList.last()
            val firstLatLngArr = firstLocation.split(",")
            val lastLatLngArr = lastLocation.split(",")
            firstList = LatLng(firstLatLngArr[0].toDouble(), firstLatLngArr[1].toDouble())
            lastList = LatLng(lastLatLngArr[0].toDouble(), lastLatLngArr[1].toDouble())
        }

        // 뒤로가기
        binding.prevBtn.setOnClickListener{
            onBackPressed()

        }
        setContentView(binding.root)
    }

    override fun onMapReady(googleMap:  GoogleMap) {
        mMap = googleMap
        // 마커
        fun vectorToBitmap(vector: Drawable): BitmapDescriptor {
            val bitmap = Bitmap.createBitmap(vector.intrinsicWidth, vector.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            vector.setBounds(0, 0, canvas.width, canvas.height)
            vector.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bitmap)
        }

// 마커 아이콘으로 vector_icon.xml 설정
        val vectorIcon = resources.getDrawable(R.drawable.ic_location_marker, null)
        val bitmapIcon = vectorToBitmap(vectorIcon)
        mMap.addMarker(MarkerOptions().position(firstList).title("start").icon(bitmapIcon))
        mMap.addMarker(MarkerOptions().position(lastList).title("end").icon(bitmapIcon))


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
    fun formatTime(totalSeconds: Long): String {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }



}