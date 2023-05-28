package com.example.hyfit_android.community

import com.google.gson.annotations.SerializedName

data class GetOnePostRes (
    @SerializedName(value="isSuccess")val isSuccess:Boolean,
    @SerializedName(value="code")val code:Int,
    @SerializedName(value="message")val message:String,
    @SerializedName(value="result")val result:OnePost
        )

data class OnePost(
    @SerializedName(value="postDto")val post: Post,
    @SerializedName(value="imageUrl")val imageUrl: String,
    @SerializedName(value="userProfileDto")val userProfile: UserProfile,
    @SerializedName(value="postLikeNumber")val postLikeNum: Long,
    @SerializedName(value="type")val type: String
)