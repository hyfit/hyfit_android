package com.example.hyfit_android.community

import com.google.gson.annotations.SerializedName

data class GetOnePostResponse (
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: PostResult
)

data class PostResult (
    @SerializedName("postDto") val post: Post,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("userProfileDto") val userProfile: UserProfile,
    @SerializedName("postLikeNumber") val postLikeNumber: Int,
    @SerializedName("type") val type: String
)

//data class Post (
//    @SerializedName("postId") val postId: Long,
//    @SerializedName("email") val email: String,
//    @SerializedName("exercise_data_id") val exerciseDataId: Int,
//    @SerializedName("content") val content: String
//)

data class UserProfile (
    @SerializedName("email") val email: String,
    @SerializedName("nickName") val nickName: String,
    @SerializedName("profileImgUrl") val profileImageUrl: String
)