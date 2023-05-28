package com.example.hyfit_android.community

import com.google.gson.annotations.SerializedName

data class PostResponse (
    @SerializedName(value="isSuccess")val isSuccess:Boolean,
    @SerializedName(value="code")val code:Int,
    @SerializedName(value="message")val message:String,
    @SerializedName(value="result")val result:Post
)

data class Post(
    @SerializedName(value="postId")val postId:Int?=null,
    @SerializedName(value="email")val email:String,
    @SerializedName(value="exercise_data_id")val exercise_data_id:Int?=null,
    @SerializedName(value="content")val content:String
)