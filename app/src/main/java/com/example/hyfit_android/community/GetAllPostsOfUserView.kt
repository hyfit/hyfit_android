package com.example.hyfit_android.community

interface GetAllPostsOfUserView {
    fun onGetAllUserPostsSuccess(result: ArrayList<Post>)
    fun onGetAllUserPostsFailure(code: Int, msg: String)
}