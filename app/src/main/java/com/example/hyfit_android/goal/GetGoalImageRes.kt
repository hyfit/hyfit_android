package com.example.hyfit_android.goal

import com.google.gson.annotations.SerializedName

data class GetGoalImageRes(
    @SerializedName("isSuccess")val isSuccess:Boolean,
    @SerializedName("code")val code:Int,
    @SerializedName("message")val message:String,
    @SerializedName("result")val result:ArrayList<PlaceImage>)

data class PlaceImage(
    @SerializedName("placeId")val placeId:Int?=null,
    @SerializedName("name")val name:String?=null,
    @SerializedName("continents")val continents:String?=null,
    @SerializedName("type")val type:String?=null,
    @SerializedName("altitude")val altitude:String?=null,
    @SerializedName("location")val location:String?=null,
    @SerializedName("src")val src:String?=null,
)


