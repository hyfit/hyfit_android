package com.example.hyfit_android.exercise.exerciseWith.exercise

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.example.hyfit_android.BuildConfig
import com.example.hyfit_android.BuildConfig.S3_URL
import com.example.hyfit_android.R
import com.example.hyfit_android.User
import com.example.hyfit_android.UserInfo.UserGetByEmailView
import com.example.hyfit_android.UserRetrofitService
import com.example.hyfit_android.databinding.ActivityExerciseWithBinding
import com.example.hyfit_android.location.LocationRedisReq
import com.example.hyfit_android.location.LocationService
import com.example.hyfit_android.location.SaveExerciseRedisLocView
import com.gmail.bishoybasily.stomp.lib.Event
import com.gmail.bishoybasily.stomp.lib.StompClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.nextnav.nn_app_sdk.BaroCalUploadManager
import com.nextnav.nn_app_sdk.BarocalCallback
import com.nextnav.nn_app_sdk.NextNavSdk
import com.nextnav.nn_app_sdk.PhoneMetaData
import com.nextnav.nn_app_sdk.notification.AltitudeContextNotification
import com.nextnav.nn_app_sdk.notification.SdkStatus
import com.nextnav.nn_app_sdk.notification.SdkStatusNotification
import com.nextnav.nn_app_sdk.zservice.WarningMessages
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.util.*
import kotlin.math.pow
import kotlin.properties.Delegates

class ExerciseWithActivity : AppCompatActivity(),UserGetByEmailView, SaveExerciseRedisLocView, OnMapReadyCallback,
    Observer,BarocalCallback{

    // binding
    private lateinit var binding : ActivityExerciseWithBinding

    // pinnacle
    private lateinit var sdk: NextNavSdk
    private val NEXTNAV_SERVICE_URL = "api.nextnav.io"
    private val API_KEY = BuildConfig.KEY_VALUE
    private lateinit var context: Context
    private lateinit var sdkMessageObservable: SdkStatusNotification
    private lateinit var altitudeObservable: AltitudeContextNotification

    // barocal
    private lateinit var callback : BarocalCallback
    private lateinit var baroCalManager : BaroCalUploadManager


    // googla map
    private var map1: GoogleMap? = null
    private var map2: GoogleMap? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var polyline: Polyline
    private lateinit var polyline2 : Polyline
    private lateinit var myMapFragment : SupportMapFragment
    private lateinit var user2MapFragment : SupportMapFragment
    var mLocationManager: LocationManager? = null
    var mLocationListener: LocationListener? = null

    // 운동 기록을 위해
    private var timer: CountDownTimer? = null
    private var timeInSeconds by Delegates.notNull<Long>()
    private var totalTime by Delegates.notNull<Long>()
    private var isReady by Delegates.notNull<Int>()
    private var isEnd by Delegates.notNull<Int>()

    // distance에 사용
    private var previousLat: String? = null
    private var previousLong: String? = null
    private var previousAlt: String? = null

    // 운동량
    private var distance by Delegates.notNull<Double>()
    private var pace = "0"

    // 운동 위치
    private lateinit var firstList :List<String>
    private lateinit  var middleList :List<String>
    private  lateinit var lastList:List<String>

    private lateinit var loadingDialog : Dialog

    // intent 받은것들
    private var myEmail = ""
    private var user2Email = ""
    private var myNickName = ""
    private var myProfileImage = ""
    private var myExerciseId = 0
    private var user2ExerciseId = 0

    // websocket
    lateinit var stompConnection: Disposable
    lateinit var topic: Disposable
    private val intervalMillis = 1000L
    private val client = OkHttpClient()
    private val stomp = StompClient(client, intervalMillis)


    // user2의 경로
    private  var locList : MutableList<String> = mutableListOf()
    private var latLngList :  MutableList<LatLng> = mutableListOf()




    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // binding
        binding = ActivityExerciseWithBinding.inflate(layoutInflater)

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
                startTimer()
            }
        }
        // 내 채널 구독
        countDownTimer.start()

        // 우선 intent 받기
        user2Email = intent.getStringExtra("user2Email").toString()
        myNickName = intent.getStringExtra("myNickName").toString()
        myProfileImage = intent.getStringExtra("myProfileImg").toString()
        myExerciseId = intent.getIntExtra("myExerciseId", 0)
        user2ExerciseId = intent.getIntExtra("user2ExerciseId", 0)
        myEmail = intent.getStringExtra("myEmail").toString()

        // 다른 유저 정보 가져오기
        getUserByEmail(user2Email)

        // 이미지, 닉네임 설정
        // 내 이미지
        Glide.with(this)
            .load(S3_URL + myProfileImage)
            .into(binding.myImage)

        // 내 닉네임
        binding.myNickName.text = myNickName

        // distance
        context = applicationContext
        PhoneMetaData.mContext = this

        // 타이머
        timeInSeconds = 0

        // 초기화
        isReady = 0
        isEnd = 0
        distance = 0.0
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // pinnacle 초기화
        sdkMessageObservable = SdkStatusNotification.getInstance()
        sdkMessageObservable.addObserver(this)

        altitudeObservable = AltitudeContextNotification.getInstance()
        altitudeObservable.addObserver(this)

        // barocal
        callback = this

        // myMap sync
        myMapFragment = supportFragmentManager
            .findFragmentById(R.id.exerciseWith_map1) as SupportMapFragment
        myMapFragment.onCreate(savedInstanceState)
        myMapFragment.getMapAsync(object : OnMapReadyCallback {
            override fun onMapReady(googleMap: GoogleMap) {
                map1 = googleMap
                if (ActivityCompat.checkSelfPermission(
                        this@ExerciseWithActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this@ExerciseWithActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                polyline = map1!!.addPolyline(
                    PolylineOptions()
                        .color(Color.BLUE)
                        .width(15f)
                        .geodesic(true)
                )
                map1!!.isMyLocationEnabled = true // 현재 위치 표시 활성화
                // 현재 위치 가져와서 지도 중심으로 설정
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        val currentLocation = LatLng(it.latitude, it.longitude)
                        map1!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18f))
                    }
                }
            }
        })
        // user2Map sync
        user2MapFragment = supportFragmentManager
            .findFragmentById(R.id.exerciseWith_map2) as SupportMapFragment
         user2MapFragment.onCreate(savedInstanceState)
        user2MapFragment.getMapAsync(object : OnMapReadyCallback {
            override fun onMapReady(googleMap: GoogleMap) {
                map2 = googleMap
                if (ActivityCompat.checkSelfPermission(
                        this@ExerciseWithActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this@ExerciseWithActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                val sydney = LatLng(-34.0, 151.0)
                map2!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
                map2!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
            }})
        mLocationManager = PhoneMetaData.mContext.getSystemService(LOCATION_SERVICE) as LocationManager

        var lastLocation: Location? = null
        val MIN_DISTANCE = 10 // 이전 위치와 현재 위치의 최소 거리
        mLocationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                var latitude = 0.0
                var longitude = 0.0
                if (location == null) {
                    Toast.makeText(
                        this@ExerciseWithActivity,
                        "I think you are currently indoors.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else if(lastLocation == null|| location.distanceTo(lastLocation) >= MIN_DISTANCE) {
                    lastLocation = location
                    latitude = location.latitude
                    longitude = location.longitude
                    if (myExerciseId > 0) {
                        calculateAlt(latitude.toString(), longitude.toString(), "5")
                        calibration(latitude.toString(), longitude.toString(),"5")
                    }
                    var currentLocation = LatLng(latitude, longitude)
                    map1?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18f))
                    if (polyline == null) {
                        polyline = map1?.addPolyline(PolylineOptions().width(10f).color(Color.BLUE))!!
                    }
                    polyline.points = polyline.points.plus(currentLocation)
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}

        }


        setContentView(binding.root)
    }

    fun startTimer() {
        subscribe()

        // 위치 가져오기
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
    fun stopTimer() {
        totalTime = timeInSeconds
        Log.d("TOTALTIME",totalTime.toString())
        timer?.cancel()
    }
    // pinnacle
    private fun calculateAlt(a : String, b:String, c : String) {
        sdk = NextNavSdk.getInstance()
        sdk.startAltitudeCalculation(NextNavSdk.AltitudeCalculationFrequency.ONE,a,b,c)
    }
    fun calibration(lat : String, long : String, accuracy : String){
        sdk = NextNavSdk.getInstance()
        // start barocal
        sdk.setAltitude(lat,long,accuracy,null)
        sdk.startBarocalUpload(callback,true)

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
                                saveExerciseRedisLoc(
                                    myExerciseId.toLong(),
                                    sdk.currentLocation.latitude.toString(),
                                    sdk.currentLocation.longitude.toString(),
                                    hat
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
                            saveExerciseRedisLoc( myExerciseId.toLong(), sdk.currentLocation.latitude.toString(), sdk.currentLocation.longitude.toString(), "9.0")
                        }
                        else {
                            Log.d("KOREACODE", o.statusCode.toString())
                            saveRedis("9.0")
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


    // userGetByEmail
    fun getUserByEmail(email : String){
        val userRetrofitService = UserRetrofitService()
        userRetrofitService.setUserGetByEmailView(this)
        userRetrofitService.usergetByEmail(email)
    }

    override fun onUserGetByEmailSuccess(code: Int, result: User) {
        Glide.with(this)
            .load(S3_URL + result.profile_img)
            .into(binding.user2Image)

        binding.user2NickName.text = result.nickName
    }

    override fun onUserGetByEmailFailure(code: Int, msg: String) {
        Log.d("UserGetByEmail",msg)
    }

    // websocket
    private fun workout( sender : String, receiver:  String, locList : String){
        val jsonObject = JSONObject()
        jsonObject.put("type","WORKOUT")
        jsonObject.put("sender",sender)
        jsonObject.put("receiver",receiver)
        jsonObject.put("locList",locList)
        val jsonString = jsonObject.toString()

        stomp.send("/pub/workout", jsonString).subscribe {
            if(it){   Log.d("THISISSENDERDATA",jsonString)}
        }
    }

    private fun subscribe() {
        stomp.url = BuildConfig.STOMP_URL
        stompConnection = stomp.connect().subscribe {
            when (it.type) {
                Event.Type.OPENED -> {
                    Log.d("CONNECT", "OPENED")
                }
                Event.Type.CLOSED -> {
                    Log.d("CONNECT", "CLOSED")
                }
                Event.Type.ERROR -> {
                    Log.d("CONNECT", "ERROR")
                }

                null -> TODO()
            }
        }
        topic =  stomp.join("/sub/channel/${myEmail}")
            .doOnError { error -> Log.d("ERROR", "subscribe error") }
            .subscribe { chatData ->
                val chatObject = JSONObject(chatData)
                locList.add(chatObject.getString("locList"))
                if(locList.isNotEmpty()){
                         runOnUiThread {
                             val location = locList[locList.size -1]
                             val lat = location.split(",")[0]
                             val long = location.split(",")[1]
                             val currentLoc = LatLng(lat.toDouble(), long.toDouble())
                             latLngList.add(currentLoc)
                             map2!!.addCircle(
                                 CircleOptions()
                                     .center(currentLoc)
                                     .radius(10.0)
                                     .strokeWidth(2f)
                                     .strokeColor(Color.RED)
                                     .fillColor(R.color.map_circle)
                             )
                             polyline2 = map2!!.addPolyline(
                                 PolylineOptions()
                                     .addAll(latLngList)
                                     .width(15f)
                                     .color(Color.BLUE)
                                     .geodesic(true)
                             )
                             map2!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 18f))

                             Log.d("THISISRESULTLIST", locList.toString())
                         }
                     }

            }

    }


    // 운동량 계산
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

    // 운동 위치 redis에 저장
    private fun saveRedis(hat :String){
        if(myExerciseId!=0){
            val lat = String.format("%.4f", sdk.currentLocation.latitude)
            val long = String.format("%.4f", sdk.currentLocation.longitude)
            val alt = hat
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

            // 10초에 한번씩 저장
            if((timeInSeconds % 10).toDouble() == 0.0){
                saveExerciseRedisLoc(myExerciseId.toLong(), sdk.currentLocation.latitude.toString(), sdk.currentLocation.longitude.toString(), alt)
            }
        }

    }
    private fun saveExerciseRedisLoc(id : Long,latitude : String, longitude : String, altitude : String){
        val locationService = LocationService()
        locationService.setSaveExerciseRedisLocView(this)
        locationService.saveExerciseRedisLoc(LocationRedisReq(latitude,longitude,altitude,id))
    }

    override fun onSaveExerciseRedisLocSuccess(result: String) {
        // user2 에게 지금 내 위치 알려주기
        workout(myEmail,user2Email,result)
    }

    override fun onSaveExerciseRedisLocFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    override fun onBrocalSuccess() {
        Log.d("BAROCAL","SUCCESS")
    }

    override fun onBarocalError() {
        Log.d("BAROCAL","ERROR")
    }

    override fun onMapReady(p0: GoogleMap) {
        TODO("Not yet implemented")
    }

}