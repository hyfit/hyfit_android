package com.example.hyfit_android.goal

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GoalSearchEntity(
    val address : String,
    val latitude: String,
    val longitude: String,
    val name : String
) : Parcelable
