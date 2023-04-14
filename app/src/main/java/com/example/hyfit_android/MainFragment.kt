package com.example.hyfit_android

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.transition.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.hyfit_android.Join.JoinActivity1
import com.example.hyfit_android.Login.LogoutActivity
import com.example.hyfit_android.databinding.ActivityMapsBinding
import com.example.hyfit_android.databinding.FragmentMainBinding
import com.example.hyfit_android.exercise.ExerciseActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.nextnav.nn_app_sdk.NextNavSdk
import com.nextnav.nn_app_sdk.notification.AltitudeContextNotification
import com.nextnav.nn_app_sdk.notification.SdkStatus
import com.nextnav.nn_app_sdk.notification.SdkStatusNotification
import com.nextnav.nn_app_sdk.zservice.WarningMessages
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment(), Observer {
    lateinit var binding: FragmentMainBinding
    // pinncale
    private lateinit var sdk: NextNavSdk
    private val NEXTNAV_SERVICE_URL = "api.nextnav.io"
    private val API_KEY = BuildConfig.KEY_VALUE

    private lateinit var sdkMessageObservable: SdkStatusNotification
    private lateinit var altitudeObservable: AltitudeContextNotification

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sdkMessageObservable = SdkStatusNotification.getInstance()
        sdkMessageObservable.addObserver(this)
        altitudeObservable = AltitudeContextNotification.getInstance()
        altitudeObservable.addObserver(this)
        binding = FragmentMainBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun initPinnacle() {
        sdk = NextNavSdk.getInstance()
        sdk.init(requireContext(), NEXTNAV_SERVICE_URL, API_KEY)
    }

    private fun calculateAlt(a : String, b:String, c : String) {
        sdk = NextNavSdk.getInstance()
        sdk.startAltitudeCalculation(NextNavSdk.AltitudeCalculationFrequency.ONE,a,b,c)
//        sdk.startBarocalUpload(barocalCallback, true)
//        startBarocal(a,b,c,null)
    }

    override fun update(o: Observable?, p: Any?) {

        if (o is SdkStatusNotification) {
//            Toast.makeText(this@MainActivity, "current Status code is " + o.code.toString(), Toast.LENGTH_SHORT).show()
//            Log.d("first code is ",o.code.toString())
            when (o.code) {
                SdkStatus.STATUS_MESSAGES.INIT_SUCCESS.code -> {
//                    // SDK is initialized successfully, it’s ready to start altitude calculation
//                    Toast.makeText(this@MainActivity, "current Status code is " + o.code.toString(), Toast.LENGTH_SHORT).show()
//                    binding.progressBar.visibility = ProgressBar.GONE
            val intent = Intent(requireActivity(), ExerciseActivity::class.java)
            startActivity(intent)
//          startActivity(Intent(this@PinnacleActivity, MapsActivity::class.java))
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