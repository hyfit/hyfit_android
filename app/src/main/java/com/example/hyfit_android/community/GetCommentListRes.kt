package com.example.hyfit_android.community

import android.provider.ContactsContract.CommonDataKinds.Nickname
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class GetCommentListRes (
    @SerializedName(value="isSuccess")val isSuccess:Boolean,
    @SerializedName(value="code")val code:Int,
    @SerializedName(value="message")val message:String,
    @SerializedName(value="result")val result: PostCommentList
        )

data class PostCommentList (
    @SerializedName(value="commentId")val commentId: Long,
    @SerializedName(value="postId")val postId: Long,
    @SerializedName(value="content")val content: String,
    @SerializedName(value="email")val email: String,
    @SerializedName(value="profileImg")val profileImg: String,
    @SerializedName(value="nickName")val nickName: String,
    @SerializedName(value="createdAt")val createdAt: LocalDateTime
        )