package com.example.hyfit_android.exercise

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.FragmentClimbingResult1Binding
import com.example.hyfit_android.location.GetAllRedisExerciseView
import com.example.hyfit_android.location.LocationService
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

import kotlin.properties.Delegates

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ClimbingResultFragment1.newInstance] factory method to
 * create an instance of this fragment.
 */
class ClimbingResultFragment1() : Fragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentClimbingResult1Binding

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentClimbingResult1Binding.inflate(inflater, container, false)
        mapFragment = childFragmentManager.findFragmentById(R.id.climbing_result_map) as SupportMapFragment
        mapFragment.onCreate(savedInstanceState)
        mapFragment.getMapAsync(this)
        val climbingResultActivity = activity as ClimbingResultActivity

        // MapView 초기화
//        mapView = view.findViewById(R.id.climbing_result_map)
//        mapView.onCreate(savedInstanceState)
//        mapView.getMapAsync(this)

        // 거리
        val distanceResult = String.format("%.2f", (Math.round(climbingResultActivity.distance * 100000.0) / 100000.0))
        binding.exerciseDistanceText1.text = distanceResult + "km"

        // 경로
        val locationList = climbingResultActivity.locationList
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

        // 시간 변경
        binding.totalTime1.text = formatTime(climbingResultActivity.totalTime)
        binding.exercisePace1.text = climbingResultActivity.pace

        // 메인이동
        binding.goToMain1.setOnClickListener{
            launchMainActivity()
        }

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

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

//        mMap.addMarker(MarkerOptions().position(firstList).title("start").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
//        mMap.addMarker(MarkerOptions().position(lastList).title("end").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))

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

    fun formatTime(totalSeconds: Long): String {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun launchMainActivity() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }




}