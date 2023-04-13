package com.example.hyfit_android.exercise

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.hyfit_android.BuildConfig
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.ActivityExerciseBinding
import com.example.hyfit_android.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nextnav.nn_app_sdk.NextNavSdk
import com.nextnav.nn_app_sdk.PhoneMetaData.mContext
import com.nextnav.nn_app_sdk.notification.AltitudeContextNotification
import com.nextnav.nn_app_sdk.notification.SdkStatus
import com.nextnav.nn_app_sdk.notification.SdkStatusNotification
import com.nextnav.nn_app_sdk.zservice.WarningMessages
import java.lang.Math.cos
import java.lang.Math.sqrt
import java.time.LocalDateTime
import java.util.*
import kotlin.math.pow
import kotlin.properties.Delegates


class ExerciseActivity :AppCompatActivity(),OnMapReadyCallback, Observer, ExerciseStartView,EndExerciseView,SaveExerciseRedisLocView, SaveExerciseLocView{

    // pinnacle
    private lateinit var sdk: NextNavSdk
    private val NEXTNAV_SERVICE_URL = "api.nextnav.io"
    private val API_KEY = BuildConfig.KEY_VALUE
    private lateinit var context: Context
    private lateinit var sdkMessageObservable: SdkStatusNotification
    private lateinit var altitudeObservable: AltitudeContextNotification

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityExerciseBinding
    private lateinit var mapFragment : SupportMapFragment
    private lateinit var timer: Timer
    private var timeInSeconds by Delegates.notNull<Long>()
    var mLocationManager: LocationManager? = null
    var mLocationListener: LocationListener? = null
    private var mCircle: Circle? = null
    private var isStart = 0
    private var exerciseId by Delegates.notNull<Long>()

    // distance에 사용
    private var previousLat: String? = null
    private var previousLong: String? = null
    private var previousAlt: String? = null
    private var currentLat: String? = null
    private var currentLong: String? = null
    private var currentAlt: String? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(
        savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
//                    binding.locationText.text = "lat, lng : ${latitude} , ${longitude}";
                    calculateAlt(latitude.toString(), longitude.toString(),"5")
                }
                var currentLocation = LatLng(latitude, longitude)
                addCircleToMap(currentLocation)
//                mMap.addMarker(MarkerOptions().position(currentLocation).title("current Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18f))
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}

        }
        // 시작버튼
        binding.exerciseStartBtn.setOnClickListener{
            startExercise()

        }

        // 종료버튼
        binding.exerciseEndBtn.setOnClickListener{
            Log.d("time", timeInSeconds.toString() )
            timer.cancel()
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
    fun startExercise(){
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
    fun endExercise(){
        val current = LocalDateTime.now()
        val exerciseService = ExerciseService()
        exerciseService.setEndExerciseView(this)
//        exerciseService.endExercise(ExerciseEndReq(exerciseId,timeInSeconds,))
    }

    // location save
    fun saveExerciseLoc(id : Long,latitude : String, longitude : String, altitude : String){
        val locationService = LocationService()
        locationService.setSaveExerciseLocView(this)
        locationService.saveExerciseLoc(LocationExerciseSaveReq(latitude,longitude,altitude,id))
    }

    // location save in redis
    fun saveExerciseRedisLoc(id : Long,latitude : String, longitude : String, altitude : String){
        val locationService = LocationService()
        locationService.setSaveExerciseRedisLocView(this)
        locationService.saveExerciseRedisLoc(LocationRedisReq(latitude,longitude,altitude,id))
    }

    fun calDistance(Lat1: String, Lon1: String, Alt1: String, Lat2: String, Lon2: String, Alt2: String): Double{
        // DCMA 알고리즘 사용
        val lat1 = Lat1.toDouble()
        val lat2 = Lat2.toDouble()
        val lon1 = Lon1.toDouble()
        val lon2 = Lon2.toDouble()
        val alt1 = kotlin.math.abs(Alt1.toDouble())
        val alt2 = kotlin.math.abs(Alt2.toDouble())

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
    private fun addCircleToMap(location: LatLng) {
        if (mCircle == null) {
            val circleOptions = CircleOptions()
                .center(location)
                .radius(2.0)
                .strokeWidth(1f)
                .strokeColor(ContextCompat.getColor(this, R.color.black))
                .fillColor(ContextCompat.getColor(this, R.color.string_gray))
            mCircle = mMap.addCircle(circleOptions)
        } else {
            mCircle!!.center = location
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
        mMap.isMyLocationEnabled = true // 현재 위치 표시 활성화

        // 현재 위치 가져와서 지도 중심으로 설정
        mLocationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
            val currentLocation = LatLng(it.latitude, it.longitude)
            addCircleToMap(currentLocation)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18f))
        }
//        val firstLocation = LatLng(37.383831666666666,-121.98756833333333)
//        mMap.addMarker(MarkerOptions().position(firstLocation).title("Marker in nextNav"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(firstLocation))
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

    // pinnacle
    private fun initPinnacle() {
        sdk = NextNavSdk.getInstance()
        sdk.init(context, NEXTNAV_SERVICE_URL, API_KEY)
    }

    private fun calculateAlt(a : String, b:String, c : String) {
        sdk = NextNavSdk.getInstance()
        sdk.startAltitudeCalculation(NextNavSdk.AltitudeCalculationFrequency.THIRTY,a,b,c)
//        sdk.startBarocalUpload(barocalCallback, true)
//        startBarocal(a,b,c,null)
    }

    private fun stopCalAlt(){
        sdk.stopAltitudeCalculation()
    }
    override fun update(o: Observable?, p: Any?) {

        if (o is SdkStatusNotification) {
            when (o.code) {

                SdkStatus.STATUS_MESSAGES.INIT_SUCCESS.code -> {
//                    // SDK is initialized successfully, it’s ready to start altitude calculation

                }
            }
        }
        if (o is AltitudeContextNotification) {
            Log.d("second status code is ",o.statusCode.toString())
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

                            // redis에 저장
                            saveExerciseRedisLoc(exerciseId,sdk.currentLocation.latitude.toString(),sdk.currentLocation.longitude.toString(), hat.toString())
                                currentLat = sdk.currentLocation.latitude.toString()
                                currentLong =sdk.currentLocation.longitude.toString()
                                currentAlt= sdk.currentLocation.longitude.toString()
                                previousLat =currentLat
                                previousLong =currentLong
                                previousAlt =  currentAlt
                            val currentDistance = calDistance(currentLat!!,currentLong!!,currentAlt!!,previousLat!!,previousLong!!,previousAlt!!)
                            Log.d("currentDistance",currentDistance.toString())

                            }


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
        TODO("Not yet implemented")
    }

    override fun onEndExerciseFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    override fun onExerciseStartSuccess(result: Exercise) {
        Log.d("start","success")
        isStart= 1
        startTimer()
        exerciseId = result.exerciseId!!
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


}
