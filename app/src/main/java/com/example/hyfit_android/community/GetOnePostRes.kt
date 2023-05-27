package com.example.hyfit_android.community

import com.google.gson.annotations.SerializedName

data class GetOnePostRes (
    @SerializedName(value="isSuccess")val isSuccess:Boolean,
    @SerializedName(value="code")val code:Int,
    @SerializedName(value="message")val message:String,
    @SerializedName(value="result")val result:OnePost
        )

data class OnePost(
    @SerializedName(value="post")val post: Post,
    @SerializedName(value="imageUrl")val imageUrl: String,
    @SerializedName(value="userProfile")val userProfile: UserProfile,
    @SerializedName(value="postLikeNum")val postLikeNum: Long,
    @SerializedName(value="type")val type: String
)