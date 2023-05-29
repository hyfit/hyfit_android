package com.example.hyfit_android.community

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class PostPageRes (
    @SerializedName(value="isSuccess")val isSuccess:Boolean,
    @SerializedName(value="code")val code:Int,
    @SerializedName(value="message")val message:String,
    @SerializedName(value="result")val result: Slice
        )

data class Slice (
    @SerializedName(value="content")val content: ArrayList<PostPagination>,
    @SerializedName(value="pageable")val pageable: Pageable,
    @SerializedName(value="number")val number: Int,
    @SerializedName(value="sort")val sort: Sort,
    @SerializedName(value="size")val size: Int,
    @SerializedName(value="numberOfElements")val numberOfElements: Int,
    @SerializedName(value="first")val first: Boolean,
    @SerializedName(value="last")val last: Boolean,
    @SerializedName(value="empty")val empty: Boolean
        )

data class PostPagination (
    @SerializedName(value="email")val email: String,
    @SerializedName(value="nickName")val nickName: String,
    @SerializedName(value="profile_img")val profileImg: String?=null,
    @SerializedName(value="postId")val postId: Long,
    @SerializedName(value="content")val content: String,
    @SerializedName(value="imageUrl")val imageUrl: String,
    @SerializedName(value="type")val type: String,
    @SerializedName(value="createdAt")val createdAt: String
    )

data class Pageable (
    @SerializedName(value="sort")val sort: Sort,
    @SerializedName(value="offset")val offset: Long,
    @SerializedName(value="pageNumber")val pageNumber: Int,
    @SerializedName(value="pageSize")val pageSize: Int,
    @SerializedName(value="paged")val paged: Boolean,
    @SerializedName(value="unpaged")val unpaged: Boolean
    )

data class Sort(
    @SerializedName(value="empty")val empty: Boolean,
    @SerializedName(value="sorted")val sorted: Boolean,
    @SerializedName(value="unsorted")val unsorted: Boolean
)

