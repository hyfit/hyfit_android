package com.example.hyfit_android.exercise.exerciseWith

import com.example.hyfit_android.exercise.Exercise
import com.google.gson.annotations.SerializedName

data class ExerciseWithRes(
    @SerializedName("isSuccess")val isSuccess:Boolean,
    @SerializedName("code")val code:Int,
    @SerializedName("message")val message:String,
    @SerializedName("result")val result:ExreciseWith
)

data class  ExreciseWith(
    @SerializedName("exerciseWithId")val exerciseWithId:Int?=null,
    @SerializedName("user1Email")val user1Email:String?=null,
    @SerializedName("user2Email")val user2Email:String?=null,  @SerializedName("user1ExerciseId")val user1ExerciseId:Int?=null,  @SerializedName("user2ExerciseId")val user2ExerciseId:Int?=null,


)
