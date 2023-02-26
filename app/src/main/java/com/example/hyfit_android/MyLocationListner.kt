package com.example.hyfit_android

import android.location.Location
import android.location.LocationListener
import android.os.Bundle

class MyLocationListener: LocationListener {
    // 위치가 변경되었을 때 호출됩니다.
    override fun onLocationChanged(location: Location) {
        // 위치 정보를 처리합니다.
    }

    // 위치 공급자가 사용 불가능해졌을 때 호출됩니다.
    override fun onProviderDisabled(provider: String) {
        // 위치 공급자 사용 불가능 상태를 처리합니다.
    }

    // 위치 공급자가 사용 가능해졌을 때 호출됩니다.
    override fun onProviderEnabled(provider: String) {
        // 위치 공급자 사용 가능 상태를 처리합니다.
    }

    // 위치 공급자 상태가 변경되었을 때 호출됩니다.
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        // 위치 공급자 상태 변경을 처리합니다.
    }
}