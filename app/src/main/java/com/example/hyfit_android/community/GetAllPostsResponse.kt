package com.example.hyfit_android.community

import com.google.gson.annotations.SerializedName
import java.util.Objects

data class GetAllPostsResponse (
    @SerializedName(value="isSuccess")val isSuccess:Boolean,
    @SerializedName(value="code")val code:Int,
    @SerializedName(value="message")val message:String,
    @SerializedName(value="result")val result:ArrayList<Post>
)


