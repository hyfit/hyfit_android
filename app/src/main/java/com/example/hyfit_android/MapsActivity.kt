package com.example.hyfit_android

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.hyfit_android.databinding.ActivityMapsBinding
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nextnav.nn_app_sdk.NextNavSdk
import com.nextnav.nn_app_sdk.PhoneMetaData.mContext
import com.nextnav.nn_app_sdk.notification.AltitudeContextNotification
import com.nextnav.nn_app_sdk.notification.SdkStatus
import com.nextnav.nn_app_sdk.notification.SdkStatusNotification
import com.nextnav.nn_app_sdk.zservice.WarningMessages
import java.util.*


class MapsActivity :AppCompatActivity(),OnMapReadyCallback, Observer {

    // pinnacle
    private lateinit var sdk: NextNavSdk
    private val NEXTNAV_SERVICE_URL = "api.nextnav.io"
    private val API_KEY = BuildConfig.KEY_VALUE
    private lateinit var context: Context
    private lateinit var sdkMessageObservable: SdkStatusNotification
    private lateinit var altitudeObservable: AltitudeContextNotification

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapFragment : SupportMapFragment

    var mLocationManager: LocationManager? = null
    var mLocationListener: LocationListener? = null


    override fun onCreate(
        savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sdkMessageObservable = SdkStatusNotification.getInstance()
        sdkMessageObservable.addObserver(this)

        altitudeObservable = AltitudeContextNotification.getInstance()
        altitudeObservable.addObserver(this)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.onCreate(savedInstanceState)
        mapFragment.getMapAsync(this)

        mLocationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager
        mLocationListener = object : LocationListener {

            override fun onLocationChanged(location: Location) {
                var latitude = 0.0
                var longitude = 0.0
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    binding.locationText.text = "lat, lng : ${latitude} , ${longitude}";
                    calculateAlt(latitude.toString(), longitude.toString(),"5")
                }

                var currentLocation = LatLng(latitude, longitude)
                mMap.addMarker(MarkerOptions().position(currentLocation).title("current Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18f))
            }
            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}

        }
        //현재 위치 버튼 클릭 -> requestLocationUpdates 호출
        binding.getLocation.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mLocationManager!!.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    3000L,
                    30f,
                    mLocationListener as LocationListener
                )

            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val firstLocation = LatLng(37.383831666666666,-121.98756833333333)
        mMap.addMarker(MarkerOptions().position(firstLocation).title("Marker in nextNav"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(firstLocation))
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

    // pinnacle

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
//            Log.d("first code is ",o.code.toString())
            binding.firstCode.text = "init code is " + o.code
            when (o.code) {

                SdkStatus.STATUS_MESSAGES.INIT_SUCCESS.code -> {
//                    // SDK is initialized successfully, it’s ready to start altitude calculation

                }
            }
        }
        if (o is AltitudeContextNotification) {
            Log.d("current Location" , sdk.currentLocation.toString())
            Log.d("second status code is ",o.statusCode.toString())
            Log.d("second error code is ",o.errorCode.toString())
            binding.secondCode.text = "altitude code is " + o.statusCode
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
                            binding.heightText.text = "hat : " + hat.toString()
                            binding.haeText.text = "hae : " + hae.toString()
                            binding.pressureText.text = "pressure : " + mUserPressure
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