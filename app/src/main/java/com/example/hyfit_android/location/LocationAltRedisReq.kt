package com.example.hyfit_android.location

data class LocationAltRedisReq(
   var latitude: String,
    val longitude: String,
    val altitude: String,
   val increase :String,
    val id: Long
)
