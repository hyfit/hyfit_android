package com.example.hyfit_android.goal

import com.google.gson.annotations.SerializedName

data class GetPlaceRes(
    @SerializedName("isSuccess")val isSuccess:Boolean,
    @SerializedName("code")val code:Int,
    @SerializedName("message")val message:String,
    @SerializedName("result")val result:ArrayList<Place>)

data class Place(
    @SerializedName("placeId")val placeId:Int?=null,
    @SerializedName("name")val name:String?=null,
    @SerializedName("continents")val continents:String?=null,
    @SerializedName("type")val type:String?=null,
    @SerializedName("altitude")val altitude:String?=null,
    @SerializedName("location")val location:String?=null,
)
