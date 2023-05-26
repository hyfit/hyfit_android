package com.example.hyfit_android.community

interface GetOnePostView {

    fun onGetOnePostSuccess(result: PostResult)
    fun onGetOnePostFailure(code:Int, msg:String)

}