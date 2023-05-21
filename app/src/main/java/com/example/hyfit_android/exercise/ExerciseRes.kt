package com.example.hyfit_android.exercise

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ExerciseRes(
    @SerializedName("isSuccess")val isSuccess:Boolean,
    @SerializedName("code")val code:Int,
    @SerializedName("message")val message:String,
    @SerializedName("result")val result:Exercise
)

data class Exercise(

    @SerializedName("exerciseId")val exerciseId:Long?=null,
    @SerializedName("goalId")val goalId:Long?=null,
    @SerializedName("email")val email:String?=null,
    @SerializedName("type")val type:String?=null,
    @SerializedName("pace")val pace:String?=null,
    @SerializedName("start")val start:String?=null,
    @SerializedName("end")val end:String?=null,
    @SerializedName("totalTime")val totalTime:Long?=null,
    @SerializedName("distance") val distance:String?=null,
    @SerializedName("increase") val increase:String?=null,
    @SerializedName("peakAlt") val peakAlt:String?=null
)
