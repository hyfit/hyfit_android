package com.example.hyfit_android

import com.google.gson.annotations.SerializedName
import java.util.Objects

data class GetResponse (
    @SerializedName(value="isSuccess")val isSuccess:Boolean,
    @SerializedName(value="code")val code:Int,
    @SerializedName(value="message")val message:String,
    @SerializedName(value="result")val result:User
)

data class User(
    @SerializedName("userId")val userId:Int?=null,
    @SerializedName("role")val role:String?=null,
    @SerializedName("name") val name:String?=null,
    @SerializedName("email")val email:String?=null,
    @SerializedName("password")var password:String?=null,
    @SerializedName("nickName")var nickName:String?=null,
    @SerializedName("phone")var phone:String?=null,
    @SerializedName("birth")var birth:Int?=null,
    @SerializedName("profile_img")var profile_img:String?=null,
    @SerializedName("user_status") var user_status:Int?=null,
    @SerializedName("introduce") var introduce:String?=null,
    @SerializedName("gender") var gender:String?=null
)
