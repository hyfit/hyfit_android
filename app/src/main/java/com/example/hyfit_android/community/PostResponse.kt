package com.example.hyfit_android.community

import com.google.gson.annotations.SerializedName
import java.util.Objects

data class PostResponse (
    @SerializedName(value="isSuccess")val isSuccess:Boolean,
    @SerializedName(value="code")val code:Int,
    @SerializedName(value="message")val message:String,
    @SerializedName(value="result")val result:Result
)

data class Result(
    @SerializedName(value="postId")val postId:Int,
    @SerializedName(value="email")val email:String,
    @SerializedName(value="boardId")val boardId:Int,
    @SerializedName(value="locationId")val locationId:Int,
    @SerializedName(value="title")val title:String,
    @SerializedName(value="content")val content:String
)
