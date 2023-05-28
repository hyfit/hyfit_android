package com.example.hyfit_android.community

interface LikePostView {
    fun onLikePostSuccess(result: PostLike)
    fun onLikePostFailure(code:Int, msg:String)
}