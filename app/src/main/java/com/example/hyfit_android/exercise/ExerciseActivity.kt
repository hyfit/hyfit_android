package com.example.hyfit_android.exercise

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.hyfit_android.BuildConfig
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.ActivityExerciseBinding
import com.example.hyfit_android.location.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.nextnav.nn_app_sdk.NextNavSdk
import com.nextnav.nn_app_sdk.PhoneMetaData.mContext
import com.nextnav.nn_app_sdk.notification.AltitudeContextNotification
import com.nextnav.nn_app_sdk.notification.SdkStatus
import com.nextnav.nn_app_sdk.notification.SdkStatusNotification
import com.nextnav.nn_app_sdk.zservice.WarningMessages
import kotlinx.coroutines.launch
import java.lang.Math.cos
import java.lang.Math.sqrt
import java.time.LocalDateTime
import java.util.*
import kotlin.math.pow
import kotlin.properties.Delegates


class ExerciseActivity :AppCompatActivity(),OnMapReadyCallback, Observer, ExerciseStartView,EndExerciseView,SaveExerciseRedisLocView, SaveExerciseLocView, GetRedisExerciseView{

    // pinnacle
    private lateinit var sdk: NextNavSdk
    private val NEXTNAV_SERVICE_URL = "api.nextnav.io"
    private val API_KEY = BuildConfig.KEY_VALUE
    private lateinit var context: Context
    private lateinit var sdkMessageObservable: SdkStatusNotification
    private lateinit var altitudeObservable: AltitudeContextNotification

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var polyline: Polyline
    private lateinit var binding: ActivityExerciseBinding
    private lateinit var mapFragment : SupportMapFragment
    private lateinit var timer: Timer
    private var timeInSeconds by Delegates.notNull<Long>()
    var mLocationManager: LocationManager? = null
    var mLocationListener: LocationListener? = null
    private var mCircle: Circle? = null
    private var exerciseId = 0

    // distance에 사용
    private var previousLat: String? = null
    private var previousLong: String? = null
    private var previousAlt: String? = null
    private var currentLat: String? = null
    private var currentLong: String? = null
    private var currentAlt: String?  = null
    // 운동량
    private var distance by Delegates.notNull<Double>()
    private var pace = "null"

    // 운동 위치
    private lateinit var firstList :List<String>
    private lateinit  var middleList :List<String>
    private  lateinit var lastList:List<String>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(
        savedInstanceState: Bundle?) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        super.onCreate(savedInstanceState)
        // distance
        context = applicationContext
        mContext = this

        // 타이머
        timer = Timer()
        timeInSeconds = 0

        sdkMessageObservable = SdkStatusNotification.getInstance()
        sdkMessageObservable.addObserver(this)

        altitudeObservable = AltitudeContextNotification.getInstance()
        altitudeObservable.addObserver(this)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapFragment = supportFragmentManager
            .findFragmentById(R.id.exercise_map) as SupportMapFragment
        mapFragment.onCreate(savedInstanceState)
        mapFragment.getMapAsync(this)

        mLocationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager


        mLocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                var latitude = 0.0
                var longitude = 0.0
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    if(exerciseId > 0){
                        calculateAlt(latitude.toString(), longitude.toString(),"5")
                    }
                }
                var currentLocation = LatLng(latitude, longitude)
                polyline.points = polyline.points.plus(currentLocation)
//                addCircleToMap(currentLocation)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18f))
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}

        }
        // 시작버튼
        binding.exerciseStartBtn.setOnClickListener{
            distance = 0.0
            startExercise()
        }

        // 종료버튼
        binding.exerciseEndBtn.setOnClickListener{
            Log.d("time", timeInSeconds.toString() )
            timer.cancel()
            endExercise()
        }
        // redis 위치 저장

    }
    // get jwt
    private fun getJwt():String?{
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }
    // exercise start
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startExercise(){
        val jwt = getJwt()
        val current = LocalDateTime.now()
        val exerciseService = ExerciseService()
        exerciseService.setExerciseStartView(this)

        // 현재 위치 받아오기
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
        mLocationListener?.let { mLocationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, it) }

        exerciseService.startExercise(jwt!!,ExerciseStartReq("running",current.toString(),0 ))

    }
    fun startTimer(){
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                timeInSeconds++
                val hours = timeInSeconds / 3600
                val minutes = (timeInSeconds % 3600) / 60
                val seconds = timeInSeconds % 60
                val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                runOnUiThread { binding.timerText.text = timeString }
            }
        }, 0, 1000)
    }

    // exercise end
    @RequiresApi(Build.VERSION_CODES.O)
    private fun endExercise(){
        val current = LocalDateTime.now()
        val exerciseService = ExerciseService()
        exerciseService.setEndExerciseView(this)
        exerciseService.endExercise(ExerciseEndReq(exerciseId.toLong(),timeInSeconds,pace,distance.toString(),current.toString()))
    }

    // location save
    private fun saveExerciseLoc(id : Long,latitude : String, longitude : String, altitude : String){
        val locationService = LocationService()
        locationService.setSaveExerciseLocView(this)
        locationService.saveExerciseLoc(LocationExerciseSaveReq(latitude,longitude,altitude,id))
    }

    // location save in redis
    private fun saveExerciseRedisLoc(id : Long,latitude : String, longitude : String, altitude : String){
        val locationService = LocationService()
        locationService.setSaveExerciseRedisLocView(this)
        locationService.saveExerciseRedisLoc(LocationRedisReq(latitude,longitude,altitude,id))
    }

    private fun getRedisExercise(){
        val locationService = LocationService()
        locationService.setGetRedisExerciseView(this)
        locationService.getRedisExercise(exerciseId.toInt())
    }

    private fun calDistance(Lat1: String, Lon1: String, Alt1: String, Lat2: String, Lon2: String, Alt2: String): Double{
        // DCMA 알고리즘 사용
        val lat1 = Lat1.toDouble()
        val lat2 = Lat2.toDouble()
        val lon1 = Lon1.toDouble()
        val lon2 = Lon2.toDouble()
        val alt1 = (kotlin.math.abs(Alt1.toDouble())/1000)
        val alt2 = (kotlin.math.abs(Alt2.toDouble())/1000)

        val ML = (lat1 + lat2) / 2
        val KPD1 = 111.13209 - 0.56605 * cos(2 * ML) + 0.00120 * cos(4 * ML)
        val KPD2 = 111.41513 * cos(ML) - 0.09455 * cos(3 * ML) + 0.00012 * (5 * ML)
        val NS = KPD1 * (lat1 - lat2)
        val EW = KPD2 * (lon1 - lon2)
        val DISR = sqrt(NS.pow(2) + EW.pow(2))
        val DIFFalt = alt1 - alt2
        val DISTANCEmove = sqrt(DISR.pow(2) + DIFFalt.pow(2))

        return DISTANCEmove
    }


    // google map
//    private fun addCircleToMap(location: LatLng) {
//        if (mCircle == null) {
//            val circleOptions = CircleOptions()
//                .center(location)
//                .radius(2.0)
//                .strokeWidth(1f)
//                .strokeColor(ContextCompat.getColor(this, R.color.black))
//                .fillColor(ContextCompat.getColor(this, R.color.string_gray))
//            mCircle = mMap.addCircle(circleOptions)
//        } else {
//            mCircle!!.center = location
//        }
//    }

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
        polyline = mMap.addPolyline(
            PolylineOptions()
                .color(Color.BLUE)
                .width(5f)
                .geodesic(true)
        )
        mMap.isMyLocationEnabled = true // 현재 위치 표시 활성화
        // 현재 위치 가져와서 지도 중심으로 설정
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLocation = LatLng(it.latitude, it.longitude)
//                addCircleToMap(currentLocation)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18f))
            }
        }

    }
    override fun onStart() {
        super.onStart()
        mapFragment.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapFragment.onStop()
    }

    override fun onResume() {
        super.onResume()
        mapFragment.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapFragment.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapFragment.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapFragment.onDestroy()
    }

    private fun saveRedis(hat : Double){
        if(exerciseId!=0){
            val lat = sdk.currentLocation.latitude.toString()
            val long = sdk.currentLocation.longitude.toString()
            val alt = hat.toString()
            runOnUiThread { binding.locationText.text = "($lat , $long , $alt)" }
            if (previousLat == null && previousLong == null && previousAlt == null) {
                previousLat = lat
                previousLong = long
                previousAlt = alt
            } else {
                val currentDistance = calDistance(previousLat!!, previousLong!!, previousAlt!!, lat, long, alt)
                distance += currentDistance
                Log.d("CURRENTDISTANCE", distance.toString())
                previousLat = lat
                previousLong = long
                previousAlt = alt
            }
            saveExerciseRedisLoc(exerciseId.toLong(), lat, long, alt)
        }
    }

    // pinnacle
    private fun initPinnacle() {
        sdk = NextNavSdk.getInstance()
        sdk.init(context, NEXTNAV_SERVICE_URL, API_KEY)
    }

    private fun calculateAlt(a : String, b:String, c : String) {
        sdk = NextNavSdk.getInstance()
        sdk.startAltitudeCalculation(NextNavSdk.AltitudeCalculationFrequency.ONE,a,b,c)
    }

    override fun update(o: Observable?, p: Any?) {

        if (o is SdkStatusNotification) {
            when (o.code) {
                SdkStatus.STATUS_MESSAGES.INIT_SUCCESS.code -> {
//                    // SDK is initialized successfully, it’s ready to start altitude calculation
                    Toast.makeText(this, "init complete", Toast.LENGTH_LONG).show()

                }
            }
        }
        if (o is AltitudeContextNotification) {
            Log.d("STATUSCODE_SECOND",o.statusCode.toString())
            Log.d("second error code is ",o.errorCode.toString())
            if (Date().time - o.timestamp <= 1000) {
                when (o.statusCode) {
                    200 -> {
                        //Deliver data to Pinnacle service successfully, //process Altitude data!
                        if (o.heightHat != null && o.heightHatUncertainty != null && o.height != null &&
                            o.heightUncertainty != null
                        ) {
                            val mUserPressure = o.mUserPressure.toString();
                            val hat = o.heightHat.toDouble()
                            val hatUnc = o.heightHatUncertainty.toDouble()
                            val hae = o.height.toDouble()
                            val haeUnc = o.heightUncertainty.toDouble()
                            Log.d("hat", hat.toString())
                            Log.d("hatUnc", hatUnc.toString())
                            Log.d("hae", hae.toString())
                            Log.d("haeUnc", haeUnc.toString())
                            Log.d("mUserPressure",mUserPressure)
                            saveRedis(hat)
                        }
                    }
                    // in korea
                    600 -> {
                        Log.d("STATUSCODE", o.statusCode.toString())
                        saveRedis(9.0)
                    }
                }
            }
            when(o.warningCode){
                WarningMessages.HIGH_DELTA_LOCATION.code -> {
                    Log.w(
                        ContentValues.TAG, "update: " +
                                WarningMessages.HIGH_DELTA_LOCATION.code)
                }
                WarningMessages.HIGH_DELTA_PRESSURE.code -> {
                    Log.w(
                        ContentValues.TAG, "update: " +
                                WarningMessages.HIGH_DELTA_PRESSURE.code)
                }
            }
        }
    }


    override fun onEndExerciseSuccess(result: Exercise) {
        getRedisExercise()
    }

    override fun onEndExerciseFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    override fun onExerciseStartSuccess(result: Exercise) {
        startTimer()
        exerciseId = result.exerciseId?.toInt() ?: 0
    }

    override fun onExerciseStartFailure(code: Int, msg: String) {
        Log.d("start","fail")
    }

    override fun onSaveExerciseLocSuccess(result: com.example.hyfit_android.location.Location) {
        Log.d("save Loc Result",result.toString())

    }

    override fun onSaveExerciseLocFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    override fun onSaveExerciseRedisLocSuccess(result: List<String>) {
        Log.d("save Loc Result",result.toString())

    }

    override fun onSaveExerciseRedisLocFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    override fun onGetRedisExerciseViewSuccess(result: RedisExercise) {
        firstList = result.start.split(",")
        middleList = result.middle.split(",")
        lastList = result.end.split(",")
        lifecycleScope.launch {
            saveExerciseLoc(exerciseId.toLong(),firstList[0],firstList[1],firstList[2])
            saveExerciseLoc(exerciseId.toLong(),middleList[0],middleList[1],middleList[2])
            saveExerciseLoc(exerciseId.toLong(),lastList[0],lastList[1],lastList[2])
        }
        val intent= Intent(this, ExerciseResultActivity::class.java)
        intent.putExtra("distance", distance)
        intent.putExtra("firstList",result.start)
        intent.putExtra("middleList",result.middle)
        intent.putExtra("lastList",result.end)
        sdk.stop()
        sdk.stopAltitudeCalculation()
        startActivity(intent)
    }

    override fun onGetRedisExerciseViewFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }


}

