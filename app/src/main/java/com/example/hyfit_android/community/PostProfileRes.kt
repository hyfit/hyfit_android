package com.example.hyfit_android.community

import com.google.gson.annotations.SerializedName

data class PostProfileRes (
    @SerializedName(value="isSuccess")val isSuccess:Boolean,
    @SerializedName(value="code")val code:Int,
    @SerializedName(value="message")val message:String,
    @SerializedName(value="result")val result:PostProfile
        )
data class PostProfile (
    @SerializedName(value="email")val email: String,
    @SerializedName(value="userProfile")val userProfile: UserProfile,
    @SerializedName(value="postNum")val postNum: Long,
    @SerializedName(value="followingNum")val followingNum: Long,
    @SerializedName(value="followerNum")val followerNum: Long
        )

data class UserProfile(
    @SerializedName(value="email")val email: String,
    @SerializedName(value="nickName")val nickName: String,
    @SerializedName(value="profileImgUrl")val profileImgUrl: String?=null
)