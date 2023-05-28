package com.example.hyfit_android.exercise.exerciseWith

import com.google.gson.annotations.SerializedName

data class DeleteExerciseRes(
    @SerializedName("isSuccess")val isSuccess:Boolean,
    @SerializedName("code")val code:Int,
    @SerializedName("message")val message:String,
    @SerializedName("result")val result:Int
)
