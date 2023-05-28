package com.example.hyfit_android.community

import android.app.slice.Slice
import com.google.gson.annotations.SerializedName

data class SaveCommentRes (
        @SerializedName(value="isSuccess")val isSuccess:Boolean,
        @SerializedName(value="code")val code:Int,
        @SerializedName(value="message")val message:String,
        @SerializedName(value="result")val result: PostComment
    )

data class PostComment (
    @SerializedName(value="commentId")val commentId: Long,
    @SerializedName(value="postId")val postId: Long,
    @SerializedName(value="content")val content: String,
    @SerializedName(value="email")val email: String
    )