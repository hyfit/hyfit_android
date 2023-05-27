package com.example.hyfit_android.community

interface SavePostView {
    fun onSavePostSuccess(result: PostImageRes)
    fun onSavePostFailure(code:Int, msg:String)
}