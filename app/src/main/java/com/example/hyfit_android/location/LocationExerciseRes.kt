package com.example.hyfit_android.location

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class LocationExerciseRes(
    @SerializedName("isSuccess")val isSuccess:Boolean,
    @SerializedName("code")val code:Int,
    @SerializedName("message")val message:String,
    @SerializedName("result")val result: Location
)
data class Location(

    @SerializedName("locationId")val locationId:Long?=null,
    @SerializedName("exerciseId")val goalId:Long?=null,
    @SerializedName("postId")val Long:Long?=null,
    @SerializedName("latitude")val latitude:String?=null,
    @SerializedName("longitude")val longitude:String?=null,
    @SerializedName("altitude")val altitude: String?=null,

)

