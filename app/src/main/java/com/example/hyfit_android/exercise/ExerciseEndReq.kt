package com.example.hyfit_android.exercise

import java.time.LocalDateTime

data class ExerciseEndReq(
    val exerciseId: Long,
    val totalTime: Long,
    val pace: String,
    val distance:String,
    val increase : String,
    val peakAlt : String,
    val end:String
)