package com.example.hyfit_android.community

interface ModifyPostView {
    fun onModifyPostSuccess(result: PostImageRes)
    fun onModifyPostFailure(code:Int, msg:String)
}