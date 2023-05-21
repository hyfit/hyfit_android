package com.example.hyfit_android.goal.info

import android.Manifest
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
import com.example.hyfit_android.databinding.FragmentClimbingInfo1Binding
import com.example.hyfit_android.databinding.FragmentClimbingResult1Binding
import com.example.hyfit_android.exercise.ClimbingResultActivity
import com.example.hyfit_android.exercise.Exercise
import com.example.hyfit_android.exercise.ExerciseService
import com.example.hyfit_android.exercise.GetExerciseView
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

class ClimbingInfoFragment1 : Fragment(), OnMapReadyCallback,GetExerciseView {
    private lateinit var binding: FragmentClimbingInfo1Binding

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
        binding = FragmentClimbingInfo1Binding.inflate(inflater, container, false)
        mapFragment = childFragmentManager.findFragmentById(R.id.climbing_info_map) as SupportMapFragment
        mapFragment.onCreate(savedInstanceState)
        mapFragment.getMapAsync(this)

        val climbingInfoActivity = activity as ClimbingInfoActivity
        binding.exerciseInfoText.text = climbingInfoActivity.title
        getExercise(climbingInfoActivity.exerciseId)

        // 경로
        val locationList = climbingInfoActivity.locationList
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

        binding.prevBtn.setOnClickListener{
            climbingInfoActivity.onBackPressed()
           // requireActivity().supportFragmentManager.popBackStack()
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

    private fun getExercise(exerciseId : Long){
        val exerciseService = ExerciseService()
        exerciseService.setGetExerciseView(this)
        exerciseService.getExercise(exerciseId)
    }

    override fun onGetExerciseViewSuccess(result: Exercise) {
        // 시간 변경
        binding.totalTime1.text = result.totalTime?.let { formatTime(it) }
        // 거리
        val distanceResult = String.format("%.2f", (Math.round((result.distance?.toDouble() ?: 0.0) * 100000.0) / 100000.0))
        binding.exerciseDistanceText1.text = distanceResult + "km"
        binding.exercisePace1.text = result.pace

    }

    override fun onGetExerciseViewFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }


}