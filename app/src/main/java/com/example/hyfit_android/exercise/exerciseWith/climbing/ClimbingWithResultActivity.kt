package com.example.hyfit_android.exercise.exerciseWith.climbing

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import com.example.hyfit_android.BuildConfig
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.ActivityClimbingWithBinding
import com.example.hyfit_android.databinding.ActivityClimbingWithResultBinding
import com.example.hyfit_android.exercise.Exercise
import com.example.hyfit_android.exercise.ExerciseService
import com.example.hyfit_android.exercise.GetExerciseView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.nextnav.nn_app_sdk.NextNavSdk
import com.nextnav.nn_app_sdk.notification.AltitudeContextNotification
import com.nextnav.nn_app_sdk.notification.SdkStatusNotification
import com.nextnav.zsdkplus.altimeter.Altimeter
import com.nextnav.zsdkplus.altimeter.NNUser
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class ClimbingWithResultActivity : AppCompatActivity() , OnMapReadyCallback,GetExerciseView {
    private lateinit var binding :  ActivityClimbingWithResultBinding
    // 고도 표시계
    private lateinit var altimeterView: Altimeter

    // pinnacle
    private lateinit var sdk: NextNavSdk
    private val NEXTNAV_SERVICE_URL = "api.nextnav.io"
    private val API_KEY = BuildConfig.KEY_VALUE
    private lateinit var context: Context
    private lateinit var sdkMessageObservable: SdkStatusNotification
    private lateinit var altitudeObservable: AltitudeContextNotification

    // googla map
    private var mMap: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var polyline: Polyline
    var mLocationManager: LocationManager? = null
    var mLocationListener: LocationListener? = null
    private lateinit var mapFragment : SupportMapFragment

    var exerciseId = 0L
    var distance = 0.0
    private var user2distance = 0.0
    var locationList = ArrayList<String>()
    private var latLngList: MutableList<LatLng> = mutableListOf()
    private lateinit var myFirstList: LatLng
    private lateinit var myLastList: LatLng

    var user2locationList = ArrayList<String>()
    private var latLngList2: MutableList<LatLng> = mutableListOf()
    private lateinit var user2firstList: LatLng
    private lateinit var user2lastList: LatLng

    var peakAlt = ""
    private var user2peakAlt = ""
    var totalTime = 0L
    var increase = 0L
    private var user2increase = 0L
    var pace = ""
    private var user2pace = ""
    var myNickName = ""
    var user2nickName = ""
    private var user2ExerciseId = 0
    private var profile1 = ""
    private var profile2 = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClimbingWithResultBinding.inflate(layoutInflater)
        altimeterView = binding.altimeter
//        altimeterView.setMode(Altimeter.AltimeterMode.DELTA)
//        altimeterView.setUnitMode(Altimeter.UnitMode.METER)
//        altimeterView.setAltimeterHeight(500)


        exerciseId = intent.getLongExtra("exerciseId",0L)

        // distance
//        distance = (intent.getDoubleExtra("distance", 0.0))
//        user2distance =  (intent.getDoubleExtra("user2Distance", 0.0))
//        val distanceResult = String.format("%.2f",distance.toFloat())
//        val distanceResult2 = String.format("%.2f", user2distance.toFloat())
        // 우선 my distance
        // binding.exerciseDistanceText.text = distanceResult + "km"

        user2ExerciseId = intent.getIntExtra("user2ExerciseId",user2ExerciseId)
        // getExercise()
        // 경로 지정

        locationList = intent.getStringArrayListExtra("locationList") as ArrayList<String>
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
            myFirstList = LatLng(firstLatLngArr[0].toDouble(), firstLatLngArr[1].toDouble())
            myLastList = LatLng(lastLatLngArr[0].toDouble(), lastLatLngArr[1].toDouble())
        }


        // user2 경로
        user2locationList = intent.getStringArrayListExtra("user2locationList") as ArrayList<String>
        if (user2locationList != null) {
            for (location in user2locationList) {
                val latLngArr = location.split(",")
                val lat = latLngArr[0].toDouble()
                val lng = latLngArr[1].toDouble()
                val latLng = LatLng(lat, lng)
                latLngList2.add(latLng)
            }
            val firstLocation = user2locationList.first()
            val lastLocation = user2locationList.last()
            val firstLatLngArr = firstLocation.split(",")
            val lastLatLngArr = lastLocation.split(",")
            user2firstList = LatLng(firstLatLngArr[0].toDouble(), firstLatLngArr[1].toDouble())
            user2lastList = LatLng(lastLatLngArr[0].toDouble(), lastLatLngArr[1].toDouble())
        }

        // 최고 고도
        peakAlt = intent.getStringExtra("peakAlt").toString()
        user2peakAlt = intent.getStringExtra("user2peakAlt").toString()
        profile1 = intent.getStringExtra("myProfile").toString()
        profile2 = intent.getStringExtra("user2Profile").toString()

        // 상승값
//        increase = intent.getLongExtra("increase",0L)
//        user2increase = intent.getLongExtra("user2increase",0L)

        // 시간
        totalTime = intent.getStringExtra("totalTime")!!.toLong()
        binding.resultTimeText.text = formatTime(totalTime)

        // pace
//        pace = intent.getStringExtra("pace").toString()
//        user2pace  = intent.getStringExtra("pace").toString()
//        binding.paceText.text = pace

        // 닉네임
        myNickName = intent.getStringExtra("myNickName").toString()
        user2nickName = intent.getStringExtra("user2NickName").toString()
        binding.clickMe.text = myNickName
        binding.clickUser2.text = user2nickName
        // 고도계
        altimeterView.setMode(Altimeter.AltimeterMode.DELTA)
        altimeterView.setUnitMode(Altimeter.UnitMode.METER)
        altimeterView.setAltimeterHeight(500)
        val userList = listOf(
            NNUser("1", myNickName, "", peakAlt.toDouble(),urlToBitmap(profile1)),
            NNUser("2", user2nickName, "", user2peakAlt?.toDouble()!!,urlToBitmap(profile2)),
        )
        // 고도bar 바꾸기
        configAltimeterVisual(userList)

        // 지도
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.climbingWith_result_map) as SupportMapFragment
        mapFragment.onCreate(savedInstanceState)
        mapFragment.getMapAsync(this)


        // 내 결과
        binding.clickMe.setOnClickListener {
            mMap!!.clear()
            // 이전 경로 제거
            polyline?.remove()
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
            mMap!!.addMarker(MarkerOptions().position(myFirstList).title("start").icon(bitmapIcon))
            mMap!!.addMarker(MarkerOptions().position(myLastList).title("end").icon(bitmapIcon))

            polyline = mMap!!.addPolyline(
                PolylineOptions()
                    .addAll(latLngList)
                    .width(15f)
                    .color(Color.BLUE)
                    .geodesic(true)
            )

            mMap!!.setOnMapLoadedCallback {
                val boundsBuilder = LatLngBounds.builder()
                for (latLng in latLngList) {
                    boundsBuilder.include(latLng)
                }
                val bounds = boundsBuilder.build()
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200))

//                binding.paceText.text = pace
//                binding.exerciseIncreaseText.text = String.format("%.2f", increase.toFloat()) + "m"
//                //  val distanceResult = String.format("%.2f", distance )
//                binding.exerciseDistanceText.text = distanceResult + "km"
            }
        }

        // 상대방 결과
        binding.clickUser2.setOnClickListener{
            mMap!!.clear()
            // 이전 경로 제거
            polyline?.remove()

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
            mMap!!.addMarker(MarkerOptions().position(user2firstList).title("start").icon(bitmapIcon))
            mMap!!.addMarker(MarkerOptions().position(user2lastList).title("end").icon(bitmapIcon))

            polyline = mMap!!.addPolyline(
                PolylineOptions()
                    .addAll(latLngList2)
                    .width(15f)
                    .color(Color.BLUE)
                    .geodesic(true)
            )

            mMap!!.setOnMapLoadedCallback {
                val boundsBuilder = LatLngBounds.builder()
                for (latLng in latLngList2) {
                    boundsBuilder.include(latLng)
                }
                val bounds = boundsBuilder.build()
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200))
            }


//            binding.paceText.text = user2pace
//            binding.exerciseIncreaseText.text = String.format("%.2f", user2increase.toFloat()) + "m"
//            binding.exerciseDistanceText.text = distanceResult2 + "km"
        }

        binding.goToMain.setOnClickListener{
            val intent= Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        setContentView(binding.root)
    }

    fun formatTime(totalSeconds: Long): String {
        val hours = totalSeconds / 3600
        val minutes = (totalSeconds % 3600) / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    @SuppressLint("ResourceAsColor")
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
        mMap!!.addMarker(MarkerOptions().position(myFirstList).title("start").icon(bitmapIcon))
        mMap!!.addMarker(MarkerOptions().position(myLastList).title("end").icon(bitmapIcon))

        polyline = mMap!!.addPolyline(
            PolylineOptions()
                .addAll(latLngList)
                .width(15f)
                .color(Color.BLUE)
                .geodesic(true)
        )

        mMap!!.setOnMapLoadedCallback {
            val boundsBuilder = LatLngBounds.builder()
            for (latLng in latLngList) {
                boundsBuilder.include(latLng)
            }
            val bounds = boundsBuilder.build()
            mMap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200))
        }
    }

    fun getExercise(){
        binding.progressBar.visibility = View.VISIBLE
        val exerciseService = ExerciseService()
        exerciseService.setGetExerciseView(this)
        exerciseService.getExercise(user2ExerciseId.toLong())
    }

    override fun onGetExerciseViewSuccess(result: Exercise) {
//        val userList = listOf(
//            NNUser("1", myNickName, "", peakAlt.toDouble()),
//            NNUser("2", user2nickName, "", result.peakAlt?.toDouble()!!),
//        )
//        // 고도bar 바꾸기
//        altimeterView.setUsers(userList)
//
//        // user2peakAlt = result.peakAlt
//        user2increase = result.increase.toString()
//        user2pace = result.pace.toString()
//        user2distance = result.distance?.toDouble() ?: 0.0
//
//        binding.progressBar.visibility = View.GONE
    }

    override fun onGetExerciseViewFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }
    private fun urlToBitmap(imageUrl: String): Bitmap? {
        var bitmap: Bitmap? = null
        var inputStream: InputStream? = null
        try {
            val url = URL(imageUrl)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            inputStream = connection.inputStream
            bitmap = BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }
        return bitmap
    }
    private fun configAltimeterVisual(userList : List<NNUser>) {
        altimeterView.setUsers(userList)
    }
}