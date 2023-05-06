package com.example.hyfit_android.location

import com.google.gson.annotations.SerializedName

data class LocationRedisExerciseRes(
    @SerializedName("isSuccess")val isSuccess:Boolean,
    @SerializedName("code")val code:Int,
    @SerializedName("message")val message:String,
    @SerializedName("result")val result: RedisExercise
)

data class RedisExercise(
    @SerializedName("start")val start: String,
    @SerializedName("middle")val middle: String,
    @SerializedName("end")val end: String

)