package com.example.hyfit_android


import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import com.example.hyfit_android.BuildConfig.STOMP_URL
import com.example.hyfit_android.Login.LoginActivity
import com.example.hyfit_android.UserInfo.GetUserView
import com.example.hyfit_android.community.CommunityFragment
import com.example.hyfit_android.databinding.ActivityMainBinding
import com.example.hyfit_android.exercise.exerciseWith.RequestFragment
import com.example.hyfit_android.goal.*
import com.example.hyfit_android.home.MainFragment
import com.gmail.bishoybasily.stomp.lib.Event.Type.*
import com.gmail.bishoybasily.stomp.lib.StompClient
import com.nextnav.nn_app_sdk.NextNavSdk
import com.nextnav.nn_app_sdk.notification.AltitudeContextNotification
import com.nextnav.nn_app_sdk.notification.SdkStatus
import com.nextnav.nn_app_sdk.notification.SdkStatusNotification
import com.nextnav.nn_app_sdk.zservice.WarningMessages
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.util.*


class MainActivity : AppCompatActivity() , Observer, GetUserView, GetMountainView, GetBuildingView , GetGoalView, GetDoneGoalView{

    lateinit var binding: ActivityMainBinding
    lateinit var loginActivity: LoginActivity
    val PERMISSIONS_REQUEST_LOCATION = 1000


    // pinncale
    private lateinit var context: Context
    private lateinit var sdk: NextNavSdk
    private val NEXTNAV_SERVICE_URL = "api.nextnav.io"
    private val API_KEY = BuildConfig.KEY_VALUE
    private lateinit var sdkMessageObservable: SdkStatusNotification
    private lateinit var altitudeObservable: AltitudeContextNotification

    var userNickName = ""

    var initCode = 0

    // goal List
    var mountainList: ArrayList<Goal>? = null
    var buildingList:  ArrayList<Goal>?= null

    // websocket
    lateinit var stompConnection: Disposable
    lateinit var topic: Disposable
    private val intervalMillis = 1000L
    private val client = OkHttpClient()
    private val stomp = StompClient(client, intervalMillis)

    private lateinit var email : String
    // 나한테 메세지를 보낸 사람 (sender)
    private lateinit var receiver : String
    private var exerciseWithId =0

    private var requestFragment  = RequestFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // pinnacle setting
        context = applicationContext
        sdkMessageObservable = SdkStatusNotification.getInstance()
        sdkMessageObservable.addObserver(this)
        altitudeObservable = AltitudeContextNotification.getInstance()
        altitudeObservable.addObserver(this)
        userNickName = intent.getStringExtra("userNickName").toString()


        binding = ActivityMainBinding.inflate(layoutInflater)
        //bindingrp = FragmentReportBinding.inflate(inflater, container, false)
        setContentView(binding.root)
        loginActivity=LoginActivity()

        // 메인에 들어오자마자 initPinnacle
//        initPinnacle()
        userget()
        getMountainProgress()
        getBuildingProgress()

        var showSetFragment = intent.getBooleanExtra("showSetFragment", false)
        var showMainFragment = intent.getBooleanExtra("showMainFragment", false)

        // BottomNavigationView 초기화
        binding.navigationView.selectedItemId = R.id.MainFragment
        binding.navigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.GoalFragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, GoalFragment()).commit()
                    true
                }
                R.id.ReportFragment -> {

                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, ReportFragment()).commit()
                    true
                }
                R.id.MainFragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainFragment()).commit()
                    true
                }
                R.id.CommunityFragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, CommunityFragment()).commit()
                    true
                }
                R.id.SetFragment -> {
                    supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SetFragment()).commit()
                    true
                }
                else -> false
            }
        }

        // 초기 fragment 설정
        if(showSetFragment){
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, SetFragment()).commit()
        }
        else {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, MainFragment()).commit()
        }

        // permission code
        if ((ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) ||
            (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACTIVITY_RECOGNITION),
                PERMISSIONS_REQUEST_LOCATION
            )
            return
        }
    }

    // websocket
    private fun send(type:String,sender : String,receiver:  String, data : Int){
        val jsonObject = JSONObject()
        jsonObject.put("type",type)
        jsonObject.put("sender",sender)
        jsonObject.put("receiver",receiver)
        jsonObject.put("data",data)
        val jsonString = jsonObject.toString()

        stomp.send("/pub/request", jsonString).subscribe {
            if(it){ }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        send("QUIT", email,receiver,exerciseWithId)
        topic.dispose()
        stompConnection.dispose()
    }

    private fun subscribe(email : String) {
        stomp.url =STOMP_URL
        stompConnection = stomp.connect().subscribe {
            when (it.type) {
                OPENED -> {
                    Log.d("CONNECT", "OPENED")
                }
                CLOSED -> {
                    Log.d("CONNECT", "CLOSED")
                }
                ERROR -> {
                    Log.d("CONNECT", "ERROR")
                }

                null -> TODO()
            }
        }

        // exerciseWith 채널 구독
        topic = stomp.join("/sub/channel/${email}")
            .doOnError { error -> Log.d("ERROR", "subscribe error") }
            .subscribe { chatData ->
                val chatObject = JSONObject(chatData)
                Log.d("MAINDATA!!!", chatObject.toString())
                // 운동 요청 알림
                if(chatObject.getString("type").equals("REQUEST")){
                    val bundle = Bundle().apply{
                        putString("email",chatObject.getString("sender"))
                        putString("nickName",chatObject.getString("sender_nickName"))
                        putInt("exerciseWithId",chatObject.getInt("data"))
                        putString("workoutType",chatObject.getString("workoutType"))

                    }
                    requestFragment.arguments = bundle
                    val fragmentManager: FragmentManager = supportFragmentManager
                    requestFragment.show(fragmentManager,"requestFragment")
                }
                // 운동 승낙 알림
                else if(chatObject.getString("type").equals("ACCEPT")){
                    Log.d("THISISACCEPT!!!",chatObject.toString())
                }


            }
    }
    override fun onUserSuccess(code: Int, result: User) {
        email = result.email.toString()
        subscribe(email)
    }

    override fun onUserFailure(code: Int, msg: String) {
        Log.d("ONUSERFAILUER",msg)
    }


    private fun getGoalProgress(){
    val jwt = getJwt()
    val goalService = GoalService()
    goalService.setGetGoalView(this)
    goalService.getGoalProgress(jwt!!)

}
    private fun getGoalDone(){
        val jwt = getJwt()
//        val goalDoneJson = arguments?.getString("goal")
//        val goalDone = gson.fromJson(goalDoneJson, Goal::class.java)
        val goalService = GoalService()
        goalService.setGetDoneGoalView(this)
        goalService.getGoalDone(jwt!!)
    }
    private fun getMountainProgress(){
        val jwt: String? = getJwt()
        val goalService = GoalService()
        goalService.setGetMountainView(this)
        goalService.getMountainProgress(jwt!!)
    }

    private fun getBuildingProgress(){
        val jwt: String? = getJwt()
        val goalService = GoalService()
        goalService.setGetBuildingView(this)
        goalService.getBuildingProgress(jwt!!)
    }
    // 권한 요청
    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACTIVITY_RECOGNITION), PERMISSIONS_REQUEST_LOCATION)
        }
    }



    private fun initPinnacle() {
        sdk = NextNavSdk.getInstance()
        sdk.init(context, NEXTNAV_SERVICE_URL, API_KEY)
    }
    override fun update(o: Observable?, p: Any?) {

        if (o is SdkStatusNotification) {
            // initCode update
            initCode = o.code
            when (o.code) {
                SdkStatus.STATUS_MESSAGES.INIT_SUCCESS.code -> {
//                    // SDK is initialized successfully, it’s ready to start altitude calculation
                }
            }
        }
        if (o is AltitudeContextNotification) {
            Log.d("current Location" , sdk.currentLocation.toString())
            Log.d("second status code is ", o.statusCode.toString())
            Log.d("second error code is ", o.errorCode.toString())
            if (Date().time - o.timestamp <= 1000) {
                when (o.statusCode) {
                    200 -> {

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
    }

    override fun onStart() {
        super.onStart()
        requestPermissions() // 액티비티가 시작되면 권한 요청 실행
        initPinnacle() // initPinnacle

    }

    private fun userget() {
        val jwt: String? = getJwt()
        Log.d("jwtjwt", jwt.toString())

        val usService = UserRetrofitService()
        usService.setgetuserView(this)
        usService.userget(jwt)
    }

    private fun getJwt():String?{
        val spf = getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getString("jwt","0")
    }
    // permission code
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode === PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.size > 0) {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) System.exit(0)
//                    else {
//                        binding.MoveButton.setOnClickListener{
//                            startActivity(Intent(this@MainActivity, PinnacleActivity::class.java))
//                        }
                    }
                }
            }
        }


    override fun onGetBuildingSuccess(result: ArrayList<Goal>) {
        buildingList = result
        Log.d("MAINBUILDINGLIST",buildingList.toString())
    }

    override fun onGetBuildingFailure(code: Int, msg: String) {
        if(code==2203){
            buildingList = ArrayList<Goal>()

        }
        if(code==2202){
            buildingList = ArrayList<Goal>()
        }
    }

    override fun onGetMountainSuccess(result: ArrayList<Goal>) {
        mountainList = result
        Log.d("MAINMOUNTAINLIST",mountainList.toString())
    }

    override fun onGetMountainFailure(code: Int, msg: String) {
        if(code==2203){
            mountainList = ArrayList<Goal>()

        }
        if(code==2202){
            mountainList = ArrayList<Goal>()
        }
    }

    override fun onGetDoneGoalSuccess(result: ArrayList<Goal>) {
        TODO("Not yet implemented")
    }

    override fun onGetDoneGoalFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }

    override fun onGetGoalSuccess(result: ArrayList<Goal>) {
        TODO("Not yet implemented")
    }

    override fun onGetGoalFailure(code: Int, msg: String) {
        TODO("Not yet implemented")
    }


}


