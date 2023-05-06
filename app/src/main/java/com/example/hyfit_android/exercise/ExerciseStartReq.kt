package com.example.hyfit_android.exercise

import java.time.LocalDateTime

data class ExerciseStartReq(
    val type: String,
    val start: String,
    val goalId: Long
)
