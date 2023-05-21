package com.example.hyfit_android.exercise

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class ExerciseListRes(
    @SerializedName("isSuccess")val isSuccess:Boolean,
    @SerializedName("code")val code:Int,
    @SerializedName("message")val message:String,
    @SerializedName("result")val result:List<Exercise>
)


