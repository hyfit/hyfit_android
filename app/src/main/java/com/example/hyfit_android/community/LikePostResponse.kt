package com.example.hyfit_android.community

data class LikePostResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: LikePostResult
)

data class LikePostResult(
    val postLikeId: Int,
    val postId: Int,
    val email: String
)