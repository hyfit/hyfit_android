package com.example.hyfit_android.goal

import com.google.gson.annotations.SerializedName

data class SaveGoalRes(
    @SerializedName("isSuccess")val isSuccess:Boolean,
    @SerializedName("code")val code:Int,
    @SerializedName("message")val message:String,
    @SerializedName("result")val result:Goal
)
