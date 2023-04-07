package com.example.hyfit_android.community

interface GetOnePostView {

    fun onGetOnePostSuccess(result: Post)
    fun onGetOnePostFailure(code:Int, msg:String)

}