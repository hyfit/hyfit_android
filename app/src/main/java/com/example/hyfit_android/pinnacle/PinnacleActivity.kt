package com.example.hyfit_android.pinnacle


import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import com.google.android.gms.location.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.hyfit_android.BuildConfig.KEY_VALUE
import com.example.hyfit_android.CommunityMainActivity
import com.example.hyfit_android.MapsActivity
//import com.example.hyfit_android.MapsActivity
import com.example.hyfit_android.databinding.ActivityPinnacleBinding
import com.nextnav.nn_app_sdk.BarocalCallback
import com.nextnav.nn_app_sdk.NextNavSdk
import com.nextnav.nn_app_sdk.notification.AltitudeContextNotification
import com.nextnav.nn_app_sdk.notification.SdkStatus
import com.nextnav.nn_app_sdk.notification.SdkStatusNotification
import com.nextnav.nn_app_sdk.zservice.WarningMessages
import java.io.IOException
import java.util.*
import kotlin.properties.Delegates

class PinnacleActivity : AppCompatActivity(), Observer{
    lateinit var binding : ActivityPinnacleBinding

    private lateinit var sdk: NextNavSdk
    private val NEXTNAV_SERVICE_URL = "api.nextnav.io"
    private val API_KEY = KEY_VALUE
    private lateinit var context: Context

    private lateinit var sdkMessageObservable: SdkStatusNotification
    private lateinit var altitudeObservable: AltitudeContextNotification

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = applicationContext
        binding = ActivityPinnacleBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        binding.calButton.setOnClickListener {
//            startActivity(Intent(this@PinnacleActivity, MapsActivity::class.java))
//        }

        sdkMessageObservable = SdkStatusNotification.getInstance()
        sdkMessageObservable.addObserver(this)

        altitudeObservable = AltitudeContextNotification.getInstance()
        altitudeObservable.addObserver(this)

        // 우선 init
        initPinnacle()

//        barocalCallback = object : BarocalCallback {
//            override fun onBrocalSuccess() {
//                Log.d("result","success")
//            }
//
//            override fun onBarocalError() {
//                Log.d("result ","fail")
//            }
//        }
    }

    private fun initPinnacle() {
        sdk = NextNavSdk.getInstance()
        sdk.init(context, NEXTNAV_SERVICE_URL, API_KEY)
    }

    private fun calculateAlt(a : String, b:String, c : String) {
        sdk = NextNavSdk.getInstance()
        sdk.startAltitudeCalculation(NextNavSdk.AltitudeCalculationFrequency.ONE,a,b,c)
//        sdk.startBarocalUpload(barocalCallback, true)
//        startBarocal(a,b,c,null)
    }

    override fun update(o: Observable?, p: Any?) {

        if (o is SdkStatusNotification) {
            Toast.makeText(this@PinnacleActivity, "current Status code is " + o.code.toString(), Toast.LENGTH_SHORT).show()
//            Log.d("first code is ",o.code.toString())
            when (o.code) {
                SdkStatus.STATUS_MESSAGES.INIT_SUCCESS.code -> {
//                    // SDK is initialized successfully, it’s ready to start altitude calculation
                    Toast.makeText(this@PinnacleActivity, "current Status code is " + o.code.toString(), Toast.LENGTH_SHORT).show()
                    binding.textView.text = "init!!!"
                    startActivity(Intent(this@PinnacleActivity, CommunityMainActivity::class.java))
                }
            }
        }
        if (o is AltitudeContextNotification) {
            Log.d("current Location" , sdk.currentLocation.toString())
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
    }

}