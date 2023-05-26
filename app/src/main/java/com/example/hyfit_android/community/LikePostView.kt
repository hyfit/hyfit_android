package com.example.hyfit_android.community

interface LikePostView {
    fun onLikeSuccess(result: LikePostResult)
    fun onLikeFailure(code:Int)
}