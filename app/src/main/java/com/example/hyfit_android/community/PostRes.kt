package com.example.hyfit_android.community

import com.google.gson.annotations.SerializedName

data class PostResponse (
    @SerializedName(value="isSuccess")val isSuccess:Boolean,
    @SerializedName(value="code")val code:Int,
    @SerializedName(value="message")val message:String,
    @SerializedName(value="result")val result:PostImageRes
)

data class PostImageRes (
    @SerializedName(value="post")val post: Post,
    @SerializedName(value="image")val image: Image
        )

data class Post(
    @SerializedName(value="postId")val postId:Long,
    @SerializedName(value="email")val email:String,
    @SerializedName(value="exercise_data_id")val exercise_data_id:Int?=null,
    @SerializedName(value="content")val content:String
)

data class Image(
    @SerializedName(value="imageId")val imageId:Long,
    @SerializedName(value="postId")val postId: Long?=null,
    @SerializedName(value="placeId")val placeId: Long?=null,
    @SerializedName(value="imageUrl")val imageUrl: String
)
