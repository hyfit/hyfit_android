package com.example.hyfit_android.location

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class LocationRedisRes(
    @SerializedName("isSuccess")val isSuccess:Boolean,
    @SerializedName("code")val code:Int,
    @SerializedName("message")val message:String,
    @SerializedName("result")val result: ArrayList<String>
)


