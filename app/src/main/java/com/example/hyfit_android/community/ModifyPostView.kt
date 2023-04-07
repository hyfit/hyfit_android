package com.example.hyfit_android.community

interface ModifyPostView {
    fun onModifyPostSuccess(result: Post)
    fun onModifyPostFailure(code:Int, msg:String)
}