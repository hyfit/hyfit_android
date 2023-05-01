package com.example.hyfit_android.exercise

import android.Manifest
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.*
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
import kotlin.collections.ArrayList
import kotlin.math.pow
import kotlin.properties.Delegates


class ExerciseActivity :AppCompatActivity(),OnMapReadyCallback, Observer, ExerciseStartView,EndExerciseView,SaveExerciseRedisLocView, SaveExerciseLocView, GetRedisExerciseView,GetAllExerciseListView ,GetAllRedisExerciseView{

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
    private var timer: CountDownTimer? = null
    private var timeInSeconds by Delegates.notNull<Long>()
    var mLocationManager: LocationManager? = null
    var mLocationListener: LocationListener? = null
    private var mCircle: Circle? = null
    private var exerciseId = 0
    private var isReady by Delegates.notNull<Int>()
    private var isEnd by Delegates.notNull<Int>()

    // distance에 사용
    private var previousLat: String? = null
    private var previousLong: String? = null
    private var previousAlt: String? = null
//    private var currentLat: String? = null
//    private var currentLong: String? = null
//    private var currentAlt: String?  = null


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
                if(location==null){
                    Toast.makeText(
                        this@ExerciseActivity,
                        "I think you are currently indoors.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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
//        binding.exerciseStartBtn.setOnClickListener{
//            distance = 0.0
//            startExercise()
//        }

        // 종료버튼
        binding.exerciseEndBtn.setOnClickListener{
            if(isReady == 1){
                saveExerciseRedisLoc(exerciseId.toLong(), sdk.currentLocation.latitude.toString(), sdk.currentLocation.longitude.toString(), sdk.currentLocation.altitude.toString())
                isEnd = 1
                stopTimer()
                endExercise()
            }
            else{
                binding.progressBar.visibility = View.VISIBLE
                binding.exerciseEndBtn.isEnabled = false
                binding.exerciseEndBtn.postDelayed({
                    if(isReady == 1) {
                        binding.progressBar.visibility = View.GONE
                        binding.exerciseEndBtn.isEnabled = true
                        isEnd = 1
                        stopTimer()
                        endExercise()
                        saveExerciseRedisLoc(exerciseId.toLong(), sdk.currentLocation.latitude.toString(), sdk.currentLocation.longitude.toString(), sdk.currentLocation.altitude.toString())
                    } else {
                        binding.exerciseEndBtn.isEnabled = true
                    }
                }, 1000)
            }

        }
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
        exerciseService.startExercise(jwt!!,ExerciseStartReq("running",current.toString(),0 ))

        // 현재 위치 받아오기
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//        mLocationListener?.let { mLocationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, it) }

    }
//    fun startTimer(){
//        timer.scheduleAtFixedRate(object : TimerTask() {
//            override fun run() {
//                if(isReady==0){
//                    runOnUiThread {
//                        Toast.makeText(
//                            this@ExerciseActivity,
//                            "wait!",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//                else {
//                    timeInSeconds++
//                    val hours = timeInSeconds / 3600
//                    val minutes = (timeInSeconds % 3600) / 60
//                    val seconds = timeInSeconds % 60
//                    val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
//                    runOnUiThread { binding.timerText.text = timeString }
//
//
//            }}
//        }, 0, 1000)
//    }
//        fun startTimer() {
//            timerTask = object : TimerTask() {
//                override fun run() {
//                    if (isReady == 1) {
//                        timeInSeconds++
//                        val hours = timeInSeconds / 3600
//                        val minutes = (timeInSeconds % 3600) / 60
//                        val seconds = timeInSeconds % 60
//                        val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
//                        runOnUiThread { binding.timerText.text = timeString }
//                    }
//                }
//            }
//            Timer().scheduleAtFixedRate(timerTask, 0, 1000)
//        }
        fun startTimer() {
            object : CountDownTimer(Long.MAX_VALUE, 1000) {
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
    fun stopTimer() {
        timer?.cancel()
    }

    fun stopCalculate(){
        sdk = NextNavSdk.getInstance()
        sdk.stopAltitudeCalculation()
    }
    // exercise end
    @RequiresApi(Build.VERSION_CODES.O)
    private fun endExercise(){
        mLocationListener?.let { mLocationManager?.removeUpdates(it) }
        stopCalculate()
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
        locationService.getRedisExercise(exerciseId)
    }
    private fun getAllExerciseList() {
        val locationService = LocationService()
        locationService.setGetAllExerciseListView(this)
        locationService.getAllExerciseList(exerciseId)
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

            // 30초에 한번씩 저장
            if((timeInSeconds % 30).toDouble() == 0.0){
                saveExerciseRedisLoc(exerciseId.toLong(), lat, long, alt)
            }
//            if (timeInSeconds < 300 && ((timeInSeconds % 30).toDouble() == 0.0)){
//                saveExerciseRedisLoc(exerciseId.toLong(), lat, long, alt)
//            }
//            // 10분 보다 적을땐 1분에 한번씩
//            else if (timeInSeconds < 600 && ((timeInSeconds % 60).toDouble() == 0.0)){
//                saveExerciseRedisLoc(exerciseId.toLong(), lat, long, alt)
//            }


        }

    }

    private fun getAllRedisExercise(){
        val locationService = LocationService()
        locationService.setGetAllRedisExerciseView(this)
        locationService.getAllRedisExercise(exerciseId)
    }
//    private fun saveRedisLoc(hat : Double) {
//        if(exerciseId!=0) {
//            saveExerciseRedisLoc(
//                exerciseId.toLong(),
//                sdk.currentLocation.latitude.toString(),
//                sdk.currentLocation.longitude.toString(),
//                hat.toString()
//            )
//            Thread.sleep(60000) // 1분 대기
//        }
//    }


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
                        // pinnacle ready
                        if(isReady == 0) {
                            isReady = 1
                            val hat = String.format("%.1f", o.heightHat.toDouble()).toDouble()
                            saveExerciseRedisLoc(exerciseId.toLong(), sdk.currentLocation.latitude.toString(), sdk.currentLocation.longitude.toString(), hat.toString())
                        }

                        else {
                            if (o.heightHat != null && o.heightHatUncertainty != null && o.height != null &&
                                o.heightUncertainty != null
                            ) {
                                val hat = String.format("%.1f", o.heightHat.toDouble()).toDouble()
                                saveRedis(hat)
                            }
                        }

                    }
                    // in korea
                    600 -> {
                        if(isReady == 0) {
                            isReady = 1
                            saveExerciseRedisLoc(exerciseId.toLong(), sdk.currentLocation.latitude.toString(), sdk.currentLocation.longitude.toString(), "9.0")
                        }
                        else {
                                Log.d("KOREACODE", o.statusCode.toString())
                                saveRedis(9.0)
                        }

                    }
                    else -> {
                        runOnUiThread {
                            Toast.makeText(
                                this@ExerciseActivity,
                                o.statusCode.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
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
       // getRedisExercise()
       // getAllExerciseList()
        getAllRedisExercise()
    }

    override fun onEndExerciseFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
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
        mLocationListener?.let { mLocationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, it) }

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

    }

    override fun onSaveExerciseRedisLocFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }


    override fun onGetRedisExerciseViewSuccess(result: RedisExercise) {
        firstList = result.start.split(",")
        if(result.middle == null && result.end == null){
            middleList = firstList
            lastList = firstList
        }
        else if(result.middle!=null && result.end==null) {
            lastList = middleList
        }
        else if(result.middle == null && result.end!=null){
            middleList = lastList
        }
        else {
            middleList = result.middle.split(",")
            lastList = result.end.split(",")
        }

//        lifecycleScope.launch {
//            saveExerciseLoc(exerciseId.toLong(),firstList[0],firstList[1],firstList[2])
//            saveExerciseLoc(exerciseId.toLong(),middleList[0],middleList[1],middleList[2])
//            saveExerciseLoc(exerciseId.toLong(),lastList[0],lastList[1],lastList[2])
//        }
        val intent= Intent(this, ExerciseResultActivity::class.java)
        intent.putExtra("distance", distance)
        intent.putExtra("firstList",result.start)
        intent.putExtra("middleList",result.middle)
        intent.putExtra("lastList",result.end)
        startActivity(intent)
    }

    override fun onGetRedisExerciseViewFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    override fun onGetAllExerciseListSuccess(result: ArrayList<String>) {


    }

    override fun onGetAllExerciseListFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    override fun onGetAllRedisExerciseSuccess(result: ArrayList<String>) {
        val intent= Intent(this, ExerciseResultActivity::class.java)
        intent.putExtra("distance", distance)
        intent.putStringArrayListExtra("locationList",result )
        intent.putExtra("totalTime",timeInSeconds)
        startActivity(intent)
    }

    override fun onGetAllRedisExerciseFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }


}

