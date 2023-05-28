package com.example.hyfit_android.community

interface GetAllPostsOfUserView {
    fun onGetAllUserPostsSuccess(result: List<PostImgInfo>)
    fun onGetAllUserPostsFailure(code: Int, msg: String)
}