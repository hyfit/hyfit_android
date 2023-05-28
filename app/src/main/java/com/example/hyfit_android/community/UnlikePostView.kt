package com.example.hyfit_android.community

interface UnlikePostView {
    fun onUnlikePostSuccess(result: String)
    fun onUnlikePostFailure(code:Int, msg:String)
}