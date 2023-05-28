package com.example.hyfit_android.community

interface GetOnePostView {

    fun onGetOnePostSuccess(result: OnePost)
    fun onGetOnePostFailure(code:Int, msg:String)

}