package com.example.hyfit_android.community

import com.google.gson.annotations.SerializedName

data class UnlikePostResponse(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: String
)