package com.example.hyfit_android.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.hyfit_android.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions

class MapsFragment : Fragment(), OnMapReadyCallback {

    private val polylineOptions = PolylineOptions().width(5f).color(Color.BLUE)
    private lateinit var mapView : MapView
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: CurrentLocationCallBack

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_maps, container, false)
        mapView = rootView.findViewById(R.id.mapFragment) as MapView
        mapView.onCreate(savedInstanceState)
        locationInit()
        mapView.getMapAsync(this)
        return rootView
    }

    private fun locationInit() {
        fusedLocationProviderClient = FusedLocationProviderClient(this.requireActivity())
        locationCallback = CurrentLocationCallBack()
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
    }

    inner class CurrentLocationCallBack: LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            super.onLocationResult(locationResult)

            val location = locationResult?.lastLocation
            location?.run {
                val latLng = LatLng(latitude, longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20f))
                polylineOptions.add(latLng)
                mMap.addPolyline(polylineOptions)
            }
        }
    }

    override fun onMapReady(googlemap: GoogleMap) {
        mMap = googlemap
        val point = LatLng(37.514655, 126.979974)
        // mMap.addMarker(MarkerOptions().position(point).title("here"))
        // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 12f))
    }

    /*
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }
    */
    /*
    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }
    */
    override fun onResume() {
        super.onResume()
        permissionCheck(cancel = {Toast.makeText(this.requireContext(), "권한을 허용해 주세요", Toast.LENGTH_SHORT).show()},
            ok={addLocationListener()})
        //mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        //mapView.onPause()
    }

    /*
    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
    */
    /*
    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
     */

    @SuppressLint("MissingPermission")
    private fun addLocationListener() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback,null)
    }

    private val gps_request_code = 1000
    private fun permissionCheck(cancel:()->Unit, ok:()->Unit) {
        if(ContextCompat.checkSelfPermission(this.requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this.requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)){
                cancel()
            }
            else
                ActivityCompat.requestPermissions(this.requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), gps_request_code
                )
        }
        else
            ok()
    }
}