package com.example.hyfit_android.community

import com.google.gson.annotations.SerializedName

data class GetAllPostsOfUserRes (
    @SerializedName(value="isSuccess")val isSuccess:Boolean,
    @SerializedName(value="code")val code:Int,
    @SerializedName(value="message")val message:String,
    @SerializedName(value="result")val result:ArrayList<PostImgInfo>
)

data class PostImgInfo (
    @SerializedName(value="postId")val postId: Long,
    @SerializedName(value="postImageUrl")val postImgUrl: String,
    @SerializedName(value="postLikeNum")val postLikeNum: Long,
    @SerializedName(value="postCommentNum")val postCommentNum: Long
        )


