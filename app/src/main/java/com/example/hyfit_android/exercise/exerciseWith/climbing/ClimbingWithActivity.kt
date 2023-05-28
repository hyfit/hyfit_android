package com.example.hyfit_android.exercise.exerciseWith.climbing

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.bumptech.glide.Glide
import com.example.hyfit_android.*
import com.example.hyfit_android.UserInfo.GetUserView
import com.example.hyfit_android.UserInfo.UserGetByEmailView
import com.example.hyfit_android.databinding.ActivityClimbingBinding
import com.example.hyfit_android.databinding.ActivityClimbingWithBinding
import com.example.hyfit_android.exercise.*
import com.example.hyfit_android.location.*
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
import com.nextnav.zsdkplus.altimeter.Altimeter
import com.nextnav.zsdkplus.altimeter.NNUser
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow
import kotlin.properties.Delegates

class ClimbingWithActivity : AppCompatActivity(), UserGetByEmailView, Observer,BarocalCallback,
    OnMapReadyCallback ,SaveAltRedisLocView,EndExerciseView,SaveExerciseLocView,GetAllRedisExerciseView{
    private lateinit var binding : ActivityClimbingWithBinding
    // 고도 표시계
    private lateinit var altimeterView: Altimeter

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
    private var circle: Circle? = null
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
    private var increase = 0.0
    private var pace = "0"
    private var peakAlt = "0"


    // 운동 위치
    private var myHat = 0.0
    private var myHae = 0.0
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

    private var ifGet = 0


    // user2의 경로
    private var user2lat = ""
    private var user2lon = ""
    private  var locList : MutableList<String> = mutableListOf()
    private var latLngList :  MutableList<LatLng> = mutableListOf()
    private var user2nickName = ""
    private var user2img = ""
    private var user2previousLat = ""
    private var user2previousLong = ""
    private var user2previousAlt = ""
    private var user2peakAlt = ""
    private var user2increase = 0.0
    private var user2distance = 0.0
    private var user2pace = ""




    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClimbingWithBinding.inflate(layoutInflater)
        altimeterView = binding.altimeter

        // 우선 intent 받기
        user2Email = intent.getStringExtra("user2Email").toString()
        myNickName = intent.getStringExtra("myNickName").toString()
        myProfileImage = intent.getStringExtra("myProfileImg").toString()
        myExerciseId = intent.getIntExtra("myExerciseId", 0)
        user2ExerciseId = intent.getIntExtra("user2ExerciseId", 0)
        myEmail = intent.getStringExtra("myEmail").toString()
        user2lat = intent.getStringExtra("user2lat").toString()
        user2lon = intent.getStringExtra("user2lon").toString()
        Log.d("THISISLOC","$user2lat , $user2lon")
        // 다른 유저 정보 가져오기
        getUserByEmail(user2Email)

        // 이미지, 닉네임 설정
        // 내 이미지
        Glide.with(this)
            .load(BuildConfig.S3_URL + myProfileImage)
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
        altimeterView.setMode(Altimeter.AltimeterMode.DELTA)
        altimeterView.setUnitMode(Altimeter.UnitMode.METER)
        altimeterView.setAltimeterHeight(500)

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

            // 시작!
            override fun onFinish() {
                dialog.dismiss()
                // 운동 시작하기작
                getCurrentLocation()
                startTimer()
                // subscribe()
                // 위치 가져오기
                // startTimer()
            }
        }

        // 내 채널 구독
        countDownTimer.start()

        // myMap sync
        myMapFragment = supportFragmentManager
            .findFragmentById(R.id.climbing_with_map1) as SupportMapFragment
        myMapFragment.onCreate(savedInstanceState)
        myMapFragment.getMapAsync(object : OnMapReadyCallback {
            override fun onMapReady(googleMap: GoogleMap) {
                map1 = googleMap
                if (ActivityCompat.checkSelfPermission(
                        this@ClimbingWithActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this@ClimbingWithActivity,
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
            .findFragmentById(R.id.climbing_with_map2) as SupportMapFragment
        user2MapFragment.onCreate(savedInstanceState)
        user2MapFragment.getMapAsync(object : OnMapReadyCallback {
            override fun onMapReady(googleMap: GoogleMap) {
                map2 = googleMap
                if (ActivityCompat.checkSelfPermission(
                        this@ClimbingWithActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this@ClimbingWithActivity,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                val user2Loc = LatLng(user2lat.toDouble(), user2lon.toDouble())
               //  Log.d("THISISLOC!!!!!","${user2lat.toDouble()} , ${user2lon.toDouble()}, $user2Loc")
                // map2!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
                polyline2 = map2?.addPolyline(PolylineOptions().width(15f).color(Color.BLUE).geodesic(true))!!
                circle = map2?.addCircle(
                    CircleOptions()
                        .center(user2Loc)
                        .radius(5.0)
                        .strokeWidth(1f)
                        .strokeColor(Color.WHITE)
                        .fillColor(R.color.map_circle)
                )
//                map2!!.addCircle(
//                    CircleOptions()
//                        .center(user2Loc)
//                        .radius(5.0)
//                        .strokeWidth(1f)
//                        .strokeColor(Color.WHITE)
//                        .fillColor(R.color.map_circle)
//                )
                map2!!.moveCamera(CameraUpdateFactory.newLatLngZoom(user2Loc, 18f))
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
                        this@ClimbingWithActivity,
                        "I think you are currently indoors.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (lastLocation == null || location.distanceTo(lastLocation) >= MIN_DISTANCE) {
                    lastLocation = location
                    latitude = location.latitude
                    longitude = location.longitude
                    if (myExerciseId > 0) {
                        calculateAlt(latitude.toString(), longitude.toString(), "5")
                        calibration(latitude.toString(), longitude.toString(), "5")
                    }
                    var currentLocation = LatLng(latitude, longitude)
                    map1?.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18f))
                    if (polyline == null) {
                        polyline = map1?.addPolyline(PolylineOptions().width(15f).color(Color.BLUE))!!
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
                pace = calculateAveragePace(timeInSeconds,distance)
                val hatAndHae = "${myHae}+$myHat"
               // val hatAndHae = "${sdk.currentLocation.altitude}+$myHat"
                saveRedisAltExercise(myExerciseId.toLong(), sdk.currentLocation.latitude.toString(), sdk.currentLocation.longitude.toString(), hatAndHae,increase.toString())
                isEnd = 1
                // 종료 메세지 보내기
                quit(myEmail,user2Email)
                // 로딩화면 띄우기
                showLoading()
                endExercise()
            }
            else{
                runOnUiThread {
                    Toast.makeText(
                        this@ClimbingWithActivity,
                        "I'm sorry, there seems to be an error.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                inError()

            }
        }

        setContentView(binding.root)
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
    @RequiresApi(Build.VERSION_CODES.O)
    fun startTimer() {
        subscribe()

        // 위치 가져오기
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

        timer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (isReady == 1 || ifGet == 1) {
                    timeInSeconds++
                    val hours = timeInSeconds / 3600
                    val minutes = (timeInSeconds % 3600) / 60
                    val seconds = timeInSeconds % 60
                    val timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                    runOnUiThread { binding.timerText.text = timeString}
                }
            }
            override fun onFinish() {
                // 카운트다운이 끝났을 때 실행할 작업
            }
        }.start()
    }
    fun getCurrentLocation(){
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

        mLocationListener?.let { listener ->
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, listener)

            val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastKnownLocation == null) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, listener)
            } else {
                // Use the last known GPS location
                listener.onLocationChanged(lastKnownLocation)
            }
        }
    }

    // Stop
    fun stopTimer() {
        totalTime = timeInSeconds
        Log.d("TOTALTIME",totalTime.toString())
        timer?.cancel()
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
        saveExerciseLoc(myExerciseId.toLong(),"0","0",peakAlt)
        exerciseService.setEndExerciseView(this)
         Log.d("THISISEXERCISE",ExerciseEndReq(myExerciseId.toLong(),timeInSeconds,pace,distance.toString(),increase.toString(),peakAlt,current.toString()).toString())
        exerciseService.endExercise(ExerciseEndReq(myExerciseId.toLong(),timeInSeconds,pace,distance.toString(),increase.toString(),peakAlt,current.toString()))
    }
    // location save
    private fun saveExerciseLoc(id : Long,latitude : String, longitude : String, altitude : String){
        val locationService = LocationService()
        locationService.setSaveExerciseLocView(this)
        locationService.saveExerciseLoc(LocationExerciseSaveReq(latitude,longitude,altitude,id))
    }
    private fun getAllRedisExercise(){
        val locationService = LocationService()
        locationService.setGetAllRedisExerciseView(this)
        locationService.getAllRedisExercise(myExerciseId)
    }

    override fun onEndExerciseSuccess(result: Exercise) {
        Log.d("THISISRESULT",result.toString())
        getAllRedisExercise()
    }

    override fun onEndExerciseFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
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
                                val hae = String.format("%.1f", o.height.toDouble())
                                val hat = String.format("%.1f", o.heightHat.toDouble())
                                myHat = hat.toDouble()
                                myHae = hae.toDouble()

                                runOnUiThread {
                                    val userList = listOf(
                                        NNUser("1", myNickName, "", myHat,urlToBitmap(myProfileImage)),
                                    )
                                    // 고도bar 바꾸기
                                    configAltimeterVisual(userList)
                                }
                                val hatAndHae = "$hae+$hat"
                                Log.d("THISISLOCATION",sdk.currentLocation.toString())
                                saveRedisAltExercise(
                                    myExerciseId.toLong(),
                                    sdk.currentLocation.latitude.toString(),
                                    sdk.currentLocation.longitude.toString(),
                                    hatAndHae,
                                    increase.toString()
                                )
                                val result = "${sdk.currentLocation.latitude.toString()},${sdk.currentLocation.longitude.toString()},${hatAndHae}"
                                workout(myEmail,user2Email,result)
                            } else {
                                val hae = String.format("%.1f", o.height.toDouble())
                                val hat = String.format("%.1f", o.heightHat.toDouble())
                                myHat = hat.toDouble()
                                myHae = hae.toDouble()
                                val hatAndHae = "$hae+$hat"
                                val result = "${sdk.currentLocation.latitude.toString()},${sdk.currentLocation.longitude.toString()},${hatAndHae}"
                                workout(myEmail,user2Email,result)
                                saveRedis(hatAndHae)

                            }
                        }
                    }
                    // in korea
                    600 -> {
                        if(isReady == 0) {
                            isReady = 1
                            val hatAndHae = "9.0+9.0"
                            myHae = 9.0
                            myHat =9.0
                            val result = "${sdk.currentLocation.latitude.toString()},${sdk.currentLocation.longitude.toString()},${hatAndHae}"
                            workout(myEmail,user2Email,result)
                            saveRedisAltExercise(myExerciseId.toLong(), sdk.currentLocation.latitude.toString(), sdk.currentLocation.longitude.toString(), hatAndHae,"0")
                        }
                        else {
                            Log.d("KOREACODE", o.statusCode.toString())
                            myHae =9.0
                            myHat = 9.0
                            val hatAndHae = "9.0+9.0"
                            val result = "${sdk.currentLocation.latitude.toString()},${sdk.currentLocation.longitude.toString()},${hatAndHae}"
                            workout(myEmail,user2Email,result)
                            saveRedis(hatAndHae)
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


    fun inError(){
        mLocationListener?.let { mLocationManager?.removeUpdates(it) }
        stopCalculate()
        val intent= Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    // save Redis
    private fun saveRedisAltExercise(id : Long,latitude : String, longitude : String, altitude : String, increase : String){
        val locationService = LocationService()
        locationService.setSaveAltRedisView(this)
        locationService.saveRedisAltExercise(LocationAltRedisReq(latitude, longitude, altitude, increase,id))
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

    private fun quit(sender : String, receiver: String){
        val jsonObject = JSONObject()
        jsonObject.put("type","QUIT")
        jsonObject.put("sender",sender)
        jsonObject.put("receiver",receiver)
        val jsonString = jsonObject.toString()

        stomp.send("/pub/quit", jsonString).subscribe {
            if(it){   Log.d("QUITDATA!",jsonString)}
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
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
        topic = stomp.join("/sub/channel/${myEmail}")
            .doOnError { error -> Log.d("ERROR", "subscribe error") }
            .subscribe { chatData ->
                val chatObject = JSONObject(chatData)
                if (chatObject.getString("type").equals("WORKOUT")) {
                    // 만약 아직 한번도 못받았으면
                    if (ifGet == 0) {
                        ifGet = 1
                    } else {
                        locList.add(chatObject.getString("locList"))
                        if (locList.isNotEmpty()) {
                            runOnUiThread {
                                val location = chatObject.getString("locList")
                                val lat = location.split(",")[0]
                                val long = location.split(",")[1]
                                val currentLoc = LatLng(lat.toDouble(), long.toDouble())
                                // hat , hae 보여주기
                                val hat = (location.split(",")[2]).split("+")[1]
                                val userList = listOf(
                                    NNUser("1", myNickName, "", myHat, urlToBitmap(myProfileImage)),
                                    NNUser("2", user2nickName, "", hat.toDouble(), urlToBitmap(user2img)),
                                )
                                // 고도bar 바꾸기
                                configAltimeterVisual(userList)
                                // 지도 바꾸기
                                latLngList.add(currentLoc)
                                circle?.remove()
                                circle = map2?.addCircle(
                                    CircleOptions()
                                        .center(currentLoc)
                                        .radius(5.0)
                                        .strokeWidth(1f)
                                        .strokeColor(Color.WHITE)
                                        .fillColor(R.color.map_circle)
                                )
                                polyline2.points = polyline2.points.plus(currentLoc)
                                map2!!.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 18f))
                                Log.d("THISISRESULTLIST", locList.toString())

                                // 계산
                                val alt = (location.split(",")[2]).split("+")[0]

                                if (user2previousLat == null && user2previousLong == null && user2previousAlt == null) {
                                    user2previousLat = lat
                                    user2previousLong = long
                                    user2previousAlt = alt
                                    user2peakAlt = alt
                                } else {
                                    // distance 계산
                                    var currentDistance =
                                        calDistance(previousLat!!, previousLong!!, previousAlt!!, lat, long, alt)
                                    user2distance += currentDistance
                                    user2previousLat = lat
                                    user2previousLong = long
                                    user2peakAlt = alt
                                    // increase 계산
                                    if (alt.toDouble() > previousAlt!!.toDouble()) user2peakAlt = alt
                                    val currentIncrease = calIncrease(previousAlt!!, alt)
                                    user2increase += currentIncrease
                                    user2previousAlt = alt

                                }
                            }
                        }
                    }
                    }

                    else if (chatObject.getString("type").equals("QUIT")) {
                        if (isReady == 1) {
                            runOnUiThread {
                                Toast.makeText(
                                    this@ClimbingWithActivity,
                                    "The workout end as the other person has finished their workout.",
                                    Toast.LENGTH_SHORT
                                ).show()

                                // 로딩화면 띄우기
                                // 타이머 종료
                                stopTimer()
                                showLoading()
                            }
                            user2pace = calculateAveragePace(timeInSeconds,user2distance)
                            pace = calculateAveragePace(timeInSeconds, distance)
                            val hatAndHae = "${myHae}+$myHat"
                            // val hatAndHae = "${sdk.currentLocation.altitude}+$myHat"
                            saveRedisAltExercise(
                                myExerciseId.toLong(),
                                sdk.currentLocation.latitude.toString(),
                                sdk.currentLocation.longitude.toString(),
                                hatAndHae,
                                increase.toString()
                            )
                            isEnd = 1
                            endExercise()
                        } else {
                            runOnUiThread {
                                Toast.makeText(
                                    this@ClimbingWithActivity,
                                    "I'm sorry, there seems to be an error.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            inError()

                        }

                    }


                }

            }

        // calculate
        fun calculateAveragePace(timeInSeconds: Long, distanceInKm: Double): String {
            val totalTimeInMinutes = timeInSeconds / 60.0
            val paceInMinutesPerKm = totalTimeInMinutes / distanceInKm
            val paceMinutes = paceInMinutesPerKm.toInt()
            val paceSeconds = ((paceInMinutesPerKm - paceMinutes) * 60).toInt()
            var result = String.format("%02d'%02d''", paceMinutes, paceSeconds)
            if (result.length >= 20) result = "0"
            return result
        }

        private fun saveRedis(hat: String) {
            if (myExerciseId != 0) {
                val lat = sdk.currentLocation?.latitude.toString()
                val long = sdk.currentLocation?.longitude.toString()
                val alt = hat.split("+")[0]

                if (previousLat == null && previousLong == null && previousAlt == null) {
                    previousLat = lat
                    previousLong = long
                    previousAlt = alt
                    peakAlt = alt
                } else {
                    // distance 계산
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
                if (timeInSeconds % 30 == 0L) {
                    saveRedisAltExercise(myExerciseId.toLong(), lat, long, hat, increase.toString())
                }
            }
        }

    fun calDistance(Lat1: String, Lon1: String, Alt1: String, Lat2: String, Lon2: String, Alt2: String): Double{
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


    // userGetByEmail
    fun getUserByEmail(email : String){
        val userRetrofitService = UserRetrofitService()
        userRetrofitService.setUserGetByEmailView(this)
        userRetrofitService.usergetByEmail(email)
    }

    override fun onUserGetByEmailSuccess(code: Int, result: User) {
        user2nickName =  result.nickName.toString()
        user2img = BuildConfig.S3_URL + result.profile_img
        Glide.with(this)
            .load(BuildConfig.S3_URL + result.profile_img)
            .into(binding.user2Image)

        binding.user2NickName.text = result.nickName

    }

    override fun onUserGetByEmailFailure(code: Int, msg: String) {
        Log.d("UserGetByEmail",msg)
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

    override fun onSaveAltRedisLocSuccess(result: String) {
        // user2 에게 지금 내 위치 알려주기
        Log.d("SAVEALTREDIS",result)
      //  workout(myEmail,user2Email,result)
    }

    override fun onSaveAltRedisLocFailure(code: Int, msg: String) {
        inError()
    }

    override fun onSaveExerciseLocSuccess(result: com.example.hyfit_android.location.Location) {
        Log.d("save Loc Result",result.toString())
    }

    override fun onSaveExerciseLocFailure(code: Int, msg: String) {
        inError()
    }

    override fun onGetAllRedisExerciseSuccess(result: ArrayList<String>) {
        val intent= Intent(this, ClimbingWithResultActivity::class.java)
        intent.putExtra("distance", distance)
        intent.putExtra("user2Distance",user2distance)
        intent.putExtra("user2ExerciseId",user2ExerciseId)
        intent.putExtra("increase",increase)
        intent.putExtra("user2increase",user2increase)
        intent.putStringArrayListExtra("locationList",result )
        intent.putExtra("totalTime",totalTime.toString())
        intent.putExtra("peakAlt",peakAlt)
        intent.putExtra("user2peakAlt",user2peakAlt)
        intent.putExtra("pace",pace)
        intent.putExtra("user2pace",user2pace)
        intent.putExtra("myNickName",myNickName)
        intent.putExtra("user2NickName",user2nickName)
        intent.putStringArrayListExtra("user2locationList",locList as ArrayList<String>)
        Log.d("USER2LOCLIST",locList.toString())
        Log.d("USER2NAME",user2nickName)
        loadingDialog.dismiss()
        startActivity(intent)
    }

    override fun onGetAllRedisExerciseFailure(code: Int, msg: String) {
        inError()
    }

}