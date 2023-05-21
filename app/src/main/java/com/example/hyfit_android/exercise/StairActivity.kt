package com.example.hyfit_android.exercise

import android.Manifest
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.example.hyfit_android.BuildConfig
import com.example.hyfit_android.MainActivity
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.ActivityStairBinding
import com.example.hyfit_android.home.MainFragment
import com.example.hyfit_android.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
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
import kotlin.properties.Delegates

class StairActivity : AppCompatActivity(), Observer, ExerciseStartView,EndExerciseView, SaveExerciseRedisLocView,
    SaveExerciseLocView, GetRedisExerciseView, GetAllExerciseListView, GetAllRedisExerciseView, SaveAltRedisLocView {

    private lateinit var binding: ActivityStairBinding

    // pinnacle
    private lateinit var sdk: NextNavSdk
    private val NEXTNAV_SERVICE_URL = "api.nextnav.io"
    private val API_KEY = BuildConfig.KEY_VALUE
    private lateinit var context: Context
    private lateinit var sdkMessageObservable: SdkStatusNotification
    private lateinit var altitudeObservable: AltitudeContextNotification

    private var timer: CountDownTimer? = null
    private var timeInSeconds by Delegates.notNull<Long>()
    private var totalTime by Delegates.notNull<Long>()
    private var increase = 0.0
    private var distance by Delegates.notNull<Double>()
    private var peakAlt = ""

    var mLocationManager: LocationManager? = null
    var mLocationListener: LocationListener? = null
    private var previousAlt: String? = null
    private var goalId = -1
    private var exerciseId = 0
    private var isReady by Delegates.notNull<Int>()
    private var isEnd by Delegates.notNull<Int>()
    private lateinit var animationDrawable : AnimationDrawable

    private lateinit var loadingDialog : Dialog


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // pinnacle
        context = applicationContext
        PhoneMetaData.mContext = this
        sdkMessageObservable = SdkStatusNotification.getInstance()
        sdkMessageObservable.addObserver(this)
        altitudeObservable = AltitudeContextNotification.getInstance()
        altitudeObservable.addObserver(this)

        // binding
        binding = ActivityStairBinding.inflate(layoutInflater)
        animationDrawable = resources.getDrawable(R.drawable.stair_animate) as AnimationDrawable
        binding.stairImageView.setImageDrawable(animationDrawable)
        setContentView(binding.root)

        // 타이머
        timeInSeconds = 0

        // 초기화
        isReady = 0
        isEnd = 0
        distance = 0.0

        // goalId
        goalId = intent.getIntExtra("goalId",-1)

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

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onFinish() {
                dialog.dismiss()
                // 운동 시작하기
                startExercise()
            }
        }
        countDownTimer.start()

        mLocationManager = PhoneMetaData.mContext.getSystemService(LOCATION_SERVICE) as LocationManager

        mLocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Log.d("LOCATION11111",location.toString())
                var latitude = 0.0
                var longitude = 0.0
                if (location == null) {
                    Toast.makeText(
                        this@StairActivity,
                        "I think you are currently indoors.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (location != null) {
                    Log.d("LOCATION",location.toString())
                    latitude = location.latitude
                    longitude = location.longitude
                    if (exerciseId > 0) {
                        calculateAlt(latitude.toString(), longitude.toString(), "5")
                    }
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
                saveRedisAltExercise(exerciseId.toLong(), sdk.currentLocation.latitude.toString(), sdk.currentLocation.longitude.toString(), sdk.currentLocation.altitude.toString(),increase.toString())
                isEnd = 1

                // 로딩화면 띄우기
                showLoading()

                endExercise() }
            else{

                runOnUiThread {
                    Toast.makeText(
                        this@StairActivity,
                        "I'm sorry, there seems to be an error.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                inError()

//                binding.progressBar.visibility = View.VISIBLE
//                binding.exerciseEndBtn.isEnabled = false
//                binding.exerciseEndBtn.postDelayed({
//                    if(isReady == 1) {
//                        binding.progressBar.visibility = View.GONE
//                        binding.exerciseEndBtn.isEnabled = true
//                        isEnd = 1
//                        // 로딩화면 띄우기
//                        val dialog = Dialog(this)
//                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//                        dialog.setCancelable(false)
//                        dialog.setContentView(R.layout.loading_result)
//                        dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
//                        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//                        dialog.show()
//
//                        stopTimer()
//                        endExercise()
//                        saveExerciseRedisLoc(exerciseId.toLong(), sdk.currentLocation.latitude.toString(), sdk.currentLocation.longitude.toString(), sdk.currentLocation.altitude.toString())
//                    } else {
//                        binding.exerciseEndBtn.isEnabled = true
//                    }
//                }, 1000)
            }

        }
    }

    fun inError(){
        mLocationListener?.let { mLocationManager?.removeUpdates(it) }
        stopCalculate()
        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
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
        animationDrawable.start() // 애니메이션 시작
        val jwt = getJwt()
        val current = LocalDateTime.now()
        val exerciseService = ExerciseService()
        exerciseService.setExerciseStartView(this)
        exerciseService.startExercise(jwt!!,ExerciseStartReq("stair",current.toString(),goalId.toLong() ))
    }
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
        animationDrawable.stop() // 애니메이션 종료
        val current = LocalDateTime.now()
        val exerciseService = ExerciseService()
        exerciseService.setEndExerciseView(this)
        exerciseService.endExercise(ExerciseEndReq(exerciseId.toLong(),timeInSeconds,"0","0",increase.toString(),peakAlt,current.toString()))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun endExerciseInKorea(){
        stopCalculate()
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = MainFragment()
        fragmentTransaction.add(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
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

    private fun calIncrease(alt1:String, alt2:String) : Double{
            return alt2.toDouble() - alt1.toDouble()
    }

    private fun saveRedis(hat : Double){
        if(exerciseId!=0){
            val lat = sdk.currentLocation.latitude.toString()
            val long = sdk.currentLocation.longitude.toString()
            val alt = hat.toString()
            runOnUiThread { binding.altitudeText.text = "${alt}m" }
            if ( previousAlt == null) {
                previousAlt = alt
            } else {
                if (alt.toDouble() > previousAlt!!.toDouble()) peakAlt = alt
                val currentIncrease = calIncrease(previousAlt!!, alt)
                increase += currentIncrease
                runOnUiThread { binding.totalAltitudeText.text = "${String.format("%.2f", increase)}m" }
                previousAlt = alt
            }

            // 30초에 한번씩 저장
            if((timeInSeconds % 30).toDouble() == 0.0){
                saveRedisAltExercise(exerciseId.toLong(), lat, long, alt,increase.toString())
            }
        }

    }

    private fun getAllRedisExercise(){
        val locationService = LocationService()
        locationService.setGetAllRedisExerciseView(this)
        locationService.getAllRedisExercise(exerciseId)
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

    // pinnacle
    private fun initPinnacle() {
        sdk = NextNavSdk.getInstance()
        sdk.init(context, NEXTNAV_SERVICE_URL, API_KEY)
    }

    private fun calculateAlt(a : String, b:String, c : String) {
        sdk = NextNavSdk.getInstance()
        sdk.startAltitudeCalculation(NextNavSdk.AltitudeCalculationFrequency.ONE,a,b,c)
    }

    @RequiresApi(Build.VERSION_CODES.O)
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
                            val hat = String.format("%.1f", o.height).toDouble()
                            saveRedisAltExercise(exerciseId.toLong(), sdk.currentLocation.latitude.toString(), sdk.currentLocation.longitude.toString(), hat.toString(),"0.0")
                        }

                        else {
                            if (o.heightHat != null && o.heightHatUncertainty != null && o.height != null &&
                                o.heightUncertainty != null
                            ) {
                                val hat = String.format("%.1f", o.height.toDouble()).toDouble()
                                saveRedis(hat)
                            }
                        }
                    }
                    // in korea
                    600 -> {
//                        runOnUiThread {
//                            Toast.makeText(
//                                this@StairActivity,
//                                "Location is out of Pinnacle service area",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                        endExerciseInKorea()
//                        if(isReady == 0) {
//                            isReady = 1
//                            saveExerciseRedisLoc(exerciseId.toLong(), sdk.currentLocation.latitude.toString(), sdk.currentLocation.longitude.toString(), "9.0")
//                        }
//                        else {
//                            Log.d("KOREACODE", o.statusCode.toString())
//                            saveRedis(9.0)
//                        }

                    }
                    else -> {
                        Log.d("ERRORLOG", o.statusCode.toString())
//                        runOnUiThread {
//                            Toast.makeText(
//                                this@StairActivity,
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


    override fun onEndExerciseSuccess(result: Exercise) {
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

    }

    override fun onSaveExerciseLocFailure(code: Int, msg: String) {
        inError()
    }

    override fun onSaveExerciseRedisLocSuccess(result: List<String>) {

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
        val intent= Intent(this, StairResultActivity::class.java)
        intent.putExtra("distance", distance)
        intent.putStringArrayListExtra("locationList",result)
        intent.putExtra("totalTime",totalTime.toString())
        loadingDialog.dismiss()
        startActivity(intent)
    }

    override fun onGetAllRedisExerciseFailure(code: Int, msg: String) {
        inError()
    }

    // 고도 저장
    override fun onSaveAltRedisLocSuccess(result: List<String>) {
        Log.d("SAVEALT",result.toString())
    }

    override fun onSaveAltRedisLocFailure(code: Int, msg: String) {
        inError()
    }


}