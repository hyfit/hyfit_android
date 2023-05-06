package com.example.hyfit_android.exercise

import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.hyfit_android.BuildConfig
import com.example.hyfit_android.R
import com.example.hyfit_android.databinding.ActivityStairBinding
import com.example.hyfit_android.home.MainFragment
import com.example.hyfit_android.location.*
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
    SaveExerciseLocView, GetRedisExerciseView, GetAllExerciseListView, GetAllRedisExerciseView {

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
    private var distance by Delegates.notNull<Double>()

    var mLocationManager: LocationManager? = null
    var mLocationListener: LocationListener? = null
    private var previousAlt: String? = null
    private var exerciseId = 0
    private var isReady by Delegates.notNull<Int>()
    private var isEnd by Delegates.notNull<Int>()
    private lateinit var animationDrawable : AnimationDrawable


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
        animationDrawable.start() // 애니메이션 시작
        val jwt = getJwt()
        val current = LocalDateTime.now()
        val exerciseService = ExerciseService()
        exerciseService.setExerciseStartView(this)
        exerciseService.startExercise(jwt!!,ExerciseStartReq("stair",current.toString(),0 ))
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
    }

    // exercise end
    @RequiresApi(Build.VERSION_CODES.O)
    private fun endExercise(){
        stopCalculate()
        animationDrawable.stop() // 애니메이션 종료
        val current = LocalDateTime.now()
        val exerciseService = ExerciseService()
        exerciseService.setEndExerciseView(this)
        exerciseService.endExercise(ExerciseEndReq(exerciseId.toLong(),timeInSeconds,"0",distance.toString(),current.toString()))
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

    private fun calDistance(alt1:String, alt2:String) : Double{
        val deltaAlt = alt2.toDouble() - alt1.toDouble()
        return if (deltaAlt >= 0) deltaAlt else 0.0
    }
    private fun saveRedis(hat : Double){
        if(exerciseId!=0){
            val lat = sdk.currentLocation.latitude.toString()
            val long = sdk.currentLocation.longitude.toString()
            val alt = hat.toString()
            runOnUiThread { binding.altitudeText.text = "${alt}m" }
            if ( previousAlt == null) {
                previousAlt = alt
            } else if(alt.toDouble() - previousAlt!!.toDouble()>=0) {
                val currentDistance = calDistance(previousAlt!!, alt)
                distance += currentDistance
                runOnUiThread { binding.totalAltitudeText.text = "${String.format("%.2f", distance)}m" }
                previousAlt = alt
            }

            // 30초에 한번씩 저장
            if((timeInSeconds % 30).toDouble() == 0.0){
                saveExerciseRedisLoc(exerciseId.toLong(), lat, long, alt)
            }
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
//                        runOnUiThread {
//                            Toast.makeText(
//                                this@StairActivity,
//                                "Location is out of Pinnacle service area",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                        endExerciseInKorea()
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
                                this@StairActivity,
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
        getAllRedisExercise()
    }

    override fun onEndExerciseFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    override fun onExerciseStartSuccess(result: Exercise) {
        exerciseId = result.exerciseId?.toInt() ?: 0
        // 우선 기본 위치 nextNav으로 하고 고도만 받기
        calculateAlt("37.3840208214713","-121.98749410182668","5")
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
        val intent= Intent(this, StairResultActivity::class.java)
        intent.putExtra("distance", distance)
        intent.putStringArrayListExtra("locationList",result)
        intent.putExtra("totalTime",totalTime.toString())
        startActivity(intent)
    }

    override fun onGetAllRedisExerciseFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }


}