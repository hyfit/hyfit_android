package com.example.hyfit_android.goal

import com.google.gson.annotations.SerializedName

data class GetGoalRes(
    @SerializedName("isSuccess")val isSuccess:Boolean,
    @SerializedName("code")val code:Int,
    @SerializedName("message")val message:String,
    @SerializedName("result")val result:ArrayList<Goal>)

//data class GetGoalResult(
//    @SerializedName("goalList") val postList: ArrayList<Goal>
//)

data class Goal(
    @SerializedName("goalId")val goalId:Int?=null,
    @SerializedName("email")val email:String?=null,
    @SerializedName("place")val place:String?=null,
    @SerializedName("type")val type:String?=null,
    @SerializedName("goalStatus")val goalStatus:Int?=null,
    @SerializedName("rate")val rate:Int?=null,
    @SerializedName("description")val description:String?=null,
    )