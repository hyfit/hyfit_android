package com.example.hyfit_android.community

interface SavePostView {
    fun onSavePostSuccess(result: Post)
    fun onSavePostFailure(code:Int, msg:String)
}