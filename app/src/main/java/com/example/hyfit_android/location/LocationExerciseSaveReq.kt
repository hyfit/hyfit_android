package com.example.hyfit_android.location

data class LocationExerciseSaveReq(
    var latitude: String,
    val longitude: String,
    val altitude: String,
    val exerciseId: Long
)
