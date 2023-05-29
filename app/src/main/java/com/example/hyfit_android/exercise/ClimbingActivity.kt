package com.example.hyfit_android.exercise

import android.Manifest
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.hyfit_android.BuildConfig
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.ActivityClimbingBinding
import com.example.hyfit_android.databinding.ActivityExerciseBinding
import com.example.hyfit_android.location.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.nextnav.nn_app_sdk.NextNavSdk
import com.nextnav.nn_app_sdk.PhoneMetaData
import com.nextnav.nn_app_sdk.notification.AltitudeContextNotification
import com.nextnav.nn_app_sdk.notification.SdkStatus
import com.nextnav.nn_app_sdk.notification.SdkStatusNotification
import com.nextnav.nn_app_sdk.zservice.WarningMessages
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow
import kotlin.properties.Delegates
import kotlin.random.Random

class ClimbingActivity : AppCompatActivity(), OnMapReadyCallback, Observer, ExerciseStartView,EndExerciseView,
    SaveExerciseRedisLocView, SaveExerciseLocView, GetRedisExerciseView, GetAllExerciseListView,
    GetAllRedisExerciseView, SaveAltRedisLocView {
    private lateinit var binding: ActivityClimbingBinding
    // pinnacle
    private lateinit var sdk: NextNavSdk
    private val NEXTNAV_SERVICE_URL = "api.nextnav.io"
    private val API_KEY = BuildConfig.KEY_VALUE
    private lateinit var context: Context
    private lateinit var sdkMessageObservable: SdkStatusNotification
    private lateinit var altitudeObservable: AltitudeContextNotification

    // google map
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var polyline: Polyline
    private lateinit var mapFragment : SupportMapFragment

    // exercise
    private var timer: CountDownTimer? = null
    private var timeInSeconds by Delegates.notNull<Long>()
    private var totalTime by Delegates.notNull<Long>()
    var mLocationManager: LocationManager? = null
    var mLocationListener: LocationListener? = null
    private var exerciseId = 0
    private var isReady by Delegates.notNull<Int>()
    private var isEnd by Delegates.notNull<Int>()

    private var goalId = -1


    // distance에 사용
    private var previousLat: String? = null
    private var previousLong: String? = null
    private var previousAlt: String? = null


    // 운동량
    private var distance by Delegates.notNull<Double>()
    private var increase = 0.0
    private var pace = "0"
    private var peakAlt = ""

    // 운동 위치
    private lateinit var firstList :List<String>
    private lateinit  var middleList :List<String>
    private  lateinit var lastList:List<String>

    private lateinit var loadingDialog : Dialog


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        super.onCreate(savedInstanceState)
        binding  = ActivityClimbingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // intent
        goalId =  intent.getIntExtra("goalId",-1)

        context = applicationContext
        PhoneMetaData.mContext = this

        // 타이머
        timeInSeconds = 0

        // 초기화
        isReady = 0
        isEnd = 0
        distance = 0.0

        // 준비 다이얼로그
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.ready_dialog)
        val numberTextView = dialog.findViewById<TextView>(R.id.number_textview)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val countDownTimer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                numberTextView.text = (secondsLeft + 1).toString()
            }

            override fun onFinish() {
                dialog.dismiss()
                // 운동 시작하기
                startExercise()
            }
        }

        countDownTimer.start()

        sdkMessageObservable = SdkStatusNotification.getInstance()
        sdkMessageObservable.addObserver(this)

        altitudeObservable = AltitudeContextNotification.getInstance()
        altitudeObservable.addObserver(this)

        mapFragment = supportFragmentManager
            .findFragmentById(R.id.exercise_map) as SupportMapFragment
        mapFragment.onCreate(savedInstanceState)
        mapFragment.getMapAsync(this)

        mLocationManager = PhoneMetaData.mContext.getSystemService(LOCATION_SERVICE) as LocationManager

        var lastLocation: Location? = null
        val MIN_DISTANCE = 10 // 이전 위치와 현재 위치의 최소 거리
        mLocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                var latitude = 0.0
                var longitude = 0.0
                if (location == null) {
                    Toast.makeText(
                        this@ClimbingActivity,
                        "I think you are currently indoors.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else if(lastLocation == null|| location.distanceTo(lastLocation) >= MIN_DISTANCE) {
                    lastLocation = location
                    latitude = location.latitude
                    longitude = location.longitude
                    if (exerciseId > 0) {
                        calculateAlt(latitude.toString(), longitude.toString(), "5")
                    }
                    var currentLocation = LatLng(latitude, longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18f))
                    if (polyline == null) {
                        polyline = mMap.addPolyline(PolylineOptions().width(10f).color(Color.BLUE))
                    }
                    polyline.points = polyline.points.plus(currentLocation)
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}

        }


        // 종료버튼
        binding.exerciseEndBtn.setOnClickListener{
            if(isReady == 1){
                stopTimer()
                //pace = calculateAveragePace(timeInSeconds,distance)
                pace = if(calculateAveragePace(timeInSeconds,distance).length >= 20) "0.0"
                else calculateAveragePace(timeInSeconds,distance)
                saveRedisAltExercise(exerciseId.toLong(), sdk.currentLocation.latitude.toString(), sdk.currentLocation.longitude.toString(), sdk.currentLocation.altitude.toString(),increase.toString())
                isEnd = 1
                // 로딩화면 띄우기
                showLoading()
                endExercise()
            }
            else{
                runOnUiThread {
                    Toast.makeText(
                        this@ClimbingActivity,
                        "I'm sorry, there seems to be an error.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                inError()

            }
        }

    }
    fun calculateAveragePace(timeInSeconds: Long, distanceInKm: Double): String {
        val totalTimeInMinutes = timeInSeconds / 60.0
        val paceInMinutesPerKm = totalTimeInMinutes / distanceInKm
        val paceMinutes = paceInMinutesPerKm.toInt()
        val paceSeconds = ((paceInMinutesPerKm - paceMinutes) * 60).toInt()
        return String.format("%02d'%02d''", paceMinutes, paceSeconds)
    }

    // get jwt
    private fun getJwt():String?{
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }
    // exercise start
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startExercise(){
        startTimer()
        val jwt = getJwt()
        val current = LocalDateTime.now()
        val exerciseService = ExerciseService()
        exerciseService.setExerciseStartView(this)
        exerciseService.startExercise(jwt!!,ExerciseStartReq("climbing",current.toString(),goalId.toLong() ))
    }

    fun startTimer() {
        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (isReady == 1) {
                    timeInSeconds++
                    val hours = timeInSeconds / 3600
                    val minutes = (timeInSeconds % 3600) / 60
                    val seconds = timeInSeconds % 60
                    val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                    binding.timerText.text = timeString
                }
            }
            override fun onFinish() {
                // 카운트다운이 끝났을 때 실행할 작업
            }
        }.start()
    }

    fun showLoading() {
        loadingDialog = Dialog(this)
        loadingDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loadingDialog.setCancelable(false)
        loadingDialog.setContentView(R.layout.loading_result)
        //  loadingDialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        loadingDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loadingDialog.show()
    }
    fun stopTimer() {
        totalTime = timeInSeconds
        Log.d("TOTALTIME",totalTime.toString())
        timer?.cancel()
    }

    fun stopCalculate(){
        sdk = NextNavSdk.getInstance()
        sdk.stopAltitudeCalculation()
        sdk.stop()
    }
    // exercise end
    @RequiresApi(Build.VERSION_CODES.O)
    private fun endExercise(){
        mLocationListener?.let { mLocationManager?.removeUpdates(it) }
        stopCalculate()
        val current = LocalDateTime.now()
        val exerciseService = ExerciseService()
        // peakAlt 저장
        saveExerciseLoc(exerciseId.toLong(),"0","0",peakAlt)
        exerciseService.setEndExerciseView(this)
        exerciseService.endExercise(ExerciseEndReq(exerciseId.toLong(),timeInSeconds,pace,distance.toString(),increase.toString(),peakAlt,current.toString()))
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
    private fun saveRedisAltExercise(id : Long,latitude : String, longitude : String, altitude : String, increase : String){
        val locationService = LocationService()
        locationService.setSaveAltRedisView(this)
        locationService.saveRedisAltExercise(LocationAltRedisReq(latitude, longitude, altitude, increase,id))
    }

    private fun getRedisExercise(){
        val locationService = LocationService()
        locationService.setGetRedisExerciseView(this)
        locationService.getRedisExercise(exerciseId)
    }
    private fun getAllExerciseList() {
        val locationService = LocationService()
        locationService.setGetAllExerciseListView(this)
        locationService.getAllExerciseList(exerciseId)
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
        polyline = mMap.addPolyline(
            PolylineOptions()
                .color(Color.BLUE)
                .width(15f)
                .geodesic(true)
        )
        mMap.isMyLocationEnabled = true // 현재 위치 표시 활성화
        // 현재 위치 가져와서 지도 중심으로 설정
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLocation = LatLng(it.latitude, it.longitude)
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

    private fun saveRedis(hat: String) {
        if (exerciseId != 0) {
            val lat = sdk.currentLocation?.latitude.toString()
            val long = sdk.currentLocation?.longitude.toString()
            val alt = hat


            if (previousLat == null && previousLong == null && previousAlt == null) {
                previousLat = lat
                previousLong = long
                previousAlt = alt
                peakAlt= alt

            } else {
                // distance 계산
                Log.d("THISISRESUKT",previousLat.toString() + previousLong.toString() + previousAlt.toString()+lat+long+alt)
                var currentDistance = calDistance(previousLat!!, previousLong!!, previousAlt!!, lat, long, alt)
                distance += currentDistance
                previousLat = lat
                previousLong = long
                previousAlt = alt
                // increase 계산
                if (alt.toDouble() > previousAlt!!.toDouble()) peakAlt = alt
                val currentIncrease = calIncrease(previousAlt!!, alt)
                increase += currentIncrease
                previousAlt = alt
                runOnUiThread {
                    binding.altitudeText.text = "${alt}m"
                    binding.totalAltitudeText.text = "${String.format("%.2f", increase)}m"
                }
            }

            // 30초에 한번씩 저장
            if (timeInSeconds % 10 == 0L) {
                saveRedisAltExercise(exerciseId.toLong(), lat, long, alt,increase.toString())
            }
        }
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
        val KPD1 = 111.13209 - 0.56605 * Math.cos(2 * ML) + 0.00120 * Math.cos(4 * ML)
        val KPD2 = 111.41513 * Math.cos(ML) - 0.09455 * Math.cos(3 * ML) + 0.00012 * (5 * ML)
        val NS = KPD1 * (lat1 - lat2)
        val EW = KPD2 * (lon1 - lon2)
        val DISR = Math.sqrt(NS.pow(2) + EW.pow(2))
        val DIFFalt = alt1 - alt2
        val DISTANCEmove = Math.sqrt(DISR.pow(2) + DIFFalt.pow(2))

        return DISTANCEmove
    }
    private fun calIncrease(alt1:String, alt2:String) : Double{
        return alt2.toDouble() - alt1.toDouble()
    }


    private fun getAllRedisExercise(){
        val locationService = LocationService()
        locationService.setGetAllRedisExerciseView(this)
        locationService.getAllRedisExercise(exerciseId)
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
                }
            }
        }
        if (o is AltitudeContextNotification) {

            Log.d("STATUSCODE_SECOND",o.statusCode.toString())
            Log.d("second error code is ",o.errorCode.toString())
            if (Date().time - o.timestamp <= 1000) {
                when (o.statusCode) {
                    200 -> {
                        if (o.heightHat != null && o.heightHatUncertainty != null && o.height != null &&
                            o.heightUncertainty != null
                        ) {
                            // pinnacle ready
                            if (isReady == 0) {
                                isReady = 1
                                val hat = String.format("%.1f", o.height.toDouble())
                                Log.d("THISISLOCATION",sdk.currentLocation.toString())
                                saveRedisAltExercise(
                                    exerciseId.toLong(),
                                    sdk.currentLocation.latitude.toString(),
                                    sdk.currentLocation.longitude.toString(),
                                    hat,
                                    increase.toString()
                                )
                            } else {
                                val hat = String.format("%.1f", o.height.toDouble())
                                saveRedis(hat)

                            }
                        }
                    }
                    // in korea
                    600 -> {
                        if(isReady == 0) {
                            isReady = 1
                            val hae = String.format("%.1f", randomValueAround9())
                            previousAlt = hae
                            previousLat = sdk.currentLocation.latitude.toString()
                            previousLong= sdk.currentLocation.longitude.toString()
                            saveRedisAltExercise(exerciseId.toLong(), sdk.currentLocation.latitude.toString(), sdk.currentLocation.longitude.toString(), hae,"0")
                        }
                        else {
                            Log.d("KOREACODE", o.statusCode.toString())
                            val hae = String.format("%.1f",  randomValueAround(previousAlt!!.toDouble()))
                            saveRedis(hae)
                        }

                    }
                    else -> {
                        Log.d("ERRORLOG", o.statusCode.toString())
//                        runOnUiThread {
//                            Toast.makeText(
//                                this@ExerciseActivity,
//                                o.statusCode.toString(),
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
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
    // random 고도
    fun randomValueAround9(): Double {
        val randomOffset = Random.nextDouble(-2.5, 2.5)
        return 9.0 + randomOffset
    }

    fun randomValueAround(value: Double): Double {
        val min = value - 0.5
        val max = value + 0.5
        return Random.nextDouble(min, max)
    }

    fun inError(){
        mLocationListener?.let { mLocationManager?.removeUpdates(it) }
        stopCalculate()
        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onEndExerciseSuccess(result: Exercise) {
//        Log.d("INTENTTIME",totalTime.toString())
//        val intent= Intent(this, ClimbingResultActivity::class.java)
//        intent.putExtra("exerciseId",exerciseId)
//        loadingDialog.dismiss()
//        startActivity(intent)
        getAllRedisExercise()
    }

    override fun onEndExerciseFailure(code: Int, msg: String) {
        Log.d("ENDEXERCISE",msg)
        inError()
    }

    override fun onExerciseStartSuccess(result: Exercise) {
        exerciseId = result.exerciseId?.toInt() ?: 0
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

    }

    override fun onExerciseStartFailure(code: Int, msg: String) {
        Log.d("STARTEXERCISE",msg)
        inError()
    }

    override fun onSaveExerciseLocSuccess(result: com.example.hyfit_android.location.Location) {
        Log.d("save Loc Result",result.toString())

    }

    override fun onSaveExerciseLocFailure(code: Int, msg: String) {
        inError()
    }

    override fun onSaveExerciseRedisLocSuccess(result: String) {

    }

    override fun onSaveExerciseRedisLocFailure(code: Int, msg: String) {
        inError()
    }


    override fun onGetRedisExerciseViewSuccess(result: RedisExercise) {

    }

    override fun onGetRedisExerciseViewFailure(code: Int, msg: String) {
        inError()
    }

    override fun onGetAllExerciseListSuccess(result: ArrayList<String>) {


    }

    override fun onGetAllExerciseListFailure(code: Int, msg: String) {
        inError()
    }

    override fun onGetAllRedisExerciseSuccess(result: ArrayList<String>) {
        Log.d("INTENTTIME",totalTime.toString())
        val intent= Intent(this, ClimbingResultActivity::class.java)
        intent.putExtra("distance", distance)
        intent.putExtra("increase",increase)
        intent.putStringArrayListExtra("locationList",result )
        intent.putExtra("totalTime",totalTime.toString())
        intent.putExtra("peakAlt",peakAlt)
        intent.putExtra("pace",pace)
        loadingDialog.dismiss()
        startActivity(intent)
    }

    override fun onGetAllRedisExerciseFailure(code: Int, msg: String) {
        inError()
    }

    override fun onSaveAltRedisLocSuccess(result: String) {
        Log.d("SAVEALT",result.toString())
    }

    override fun onSaveAltRedisLocFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }


}