package com.example.hyfit_android


import android.Manifest
import android.app.PendingIntent.getActivity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.hyfit_android.Login.LoginActivity
import com.example.hyfit_android.UserInfo.GetUserView
import com.example.hyfit_android.community.CommunityFragment
import com.example.hyfit_android.databinding.ActivityMainBinding
import com.example.hyfit_android.databinding.FragmentReportBinding
import com.example.hyfit_android.databinding.FragmentSetBinding
import com.example.hyfit_android.goal.GoalFragment
import com.nextnav.nn_app_sdk.NextNavSdk
import com.nextnav.nn_app_sdk.notification.AltitudeContextNotification
import com.nextnav.nn_app_sdk.notification.SdkStatus
import com.nextnav.nn_app_sdk.notification.SdkStatusNotification
import com.nextnav.nn_app_sdk.zservice.WarningMessages
import java.util.*
import com.example.hyfit_android.home.MainFragment
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() , Observer, GetUserView{
    lateinit var binding: ActivityMainBinding
    lateinit var loginActivity: LoginActivity
    val PERMISSIONS_REQUEST_LOCATION = 1000
    //val reportxml=ReportFragment().binding.puthere


    // pinncale
    private lateinit var context: Context
    private lateinit var sdk: NextNavSdk
    private val NEXTNAV_SERVICE_URL = "api.nextnav.io"
    private val API_KEY = BuildConfig.KEY_VALUE
    private lateinit var sdkMessageObservable: SdkStatusNotification
    private lateinit var altitudeObservable: AltitudeContextNotification

    lateinit var userNickName : String
    var initCode = 0
    var usergetCode = 0


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

//    private fun report(){
//        val rpService=ReportRetrofitService()
//        rpService.report()
//    }
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

    override fun onUserSuccess(code: Int, result: User) {
    }

    override fun onUserFailure(code: Int, msg: String) {
        Log.d("ONUSERFAILUER",msg)
    }


    }


