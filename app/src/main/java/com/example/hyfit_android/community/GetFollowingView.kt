package com.example.hyfit_android.community

interface GetFollowingView {
    fun onFollowingSuccess(result: List<String>)
    fun onFollowingFailure(code:Int, msg:String)
}