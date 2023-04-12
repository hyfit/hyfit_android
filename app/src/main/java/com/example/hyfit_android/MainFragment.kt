package com.example.hyfit_android

import android.content.Context
import android.content.Intent
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hyfit_android.Join.JoinActivity1
import com.example.hyfit_android.Login.LogoutActivity
import com.example.hyfit_android.databinding.ActivityMapsBinding
import com.example.hyfit_android.databinding.FragmentMainBinding
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.nextnav.nn_app_sdk.NextNavSdk
import com.nextnav.nn_app_sdk.notification.AltitudeContextNotification
import com.nextnav.nn_app_sdk.notification.SdkStatusNotification

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    lateinit var binding: FragmentMainBinding
    // pinnacle
    private lateinit var sdk: NextNavSdk
    private val NEXTNAV_SERVICE_URL = "api.nextnav.io"
    private val API_KEY = BuildConfig.KEY_VALUE
    
    private lateinit var sdkMessageObservable: SdkStatusNotification
    private lateinit var altitudeObservable: AltitudeContextNotification

    // google map
    private lateinit var mMap: GoogleMap
    private lateinit var mapFragment : SupportMapFragment
    var mLocationManager: LocationManager? = null
    var mLocationListener: LocationListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }


}