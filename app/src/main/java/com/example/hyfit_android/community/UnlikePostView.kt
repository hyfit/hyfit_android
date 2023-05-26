package com.example.hyfit_android.community

interface UnlikePostView {
    fun onUnlikeSuccess(result: String)
    fun onUnlikeFailure(code:Int)
}