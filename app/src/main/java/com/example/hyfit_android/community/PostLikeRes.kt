package com.example.hyfit_android.community

import android.app.slice.Slice
import com.google.gson.annotations.SerializedName

data class PostLikeRes (
    @SerializedName(value="isSuccess")val isSuccess:Boolean,
    @SerializedName(value="code")val code:Int,
    @SerializedName(value="message")val message:String,
    @SerializedName(value="result")val result: PostLike
)

data class PostLike(
    @SerializedName(value="postLikeId")val postLikeId: Long,
    @SerializedName(value="postId")val postId: Long,
    @SerializedName(value="email")val email: String
)